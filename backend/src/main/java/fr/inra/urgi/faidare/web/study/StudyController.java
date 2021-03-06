package fr.inra.urgi.faidare.web.study;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.study.StudySitemapVO;
import fr.inra.urgi.faidare.domain.data.variable.ObservationVariableVO;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.es.TrialRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Controller used to display a study card based on its ID.
 * @author JB Nizet
 */
@Controller("webStudyController")
@RequestMapping("/studies")
public class StudyController {

    private final StudyRepository studyRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentRepository xRefDocumentRepository;
    private final GermplasmRepository germplasmRepository;
    private final CropOntologyRepository cropOntologyRepository;
    private final TrialRepository trialRepository;
    private final LocationRepository locationRepository;

    public StudyController(StudyRepository studyRepository,
                           FaidareProperties faidareProperties,
                           XRefDocumentRepository xRefDocumentRepository,
                           GermplasmRepository germplasmRepository,
                           CropOntologyRepository cropOntologyRepository,
                           TrialRepository trialRepository,
                           LocationRepository locationRepository) {
        this.studyRepository = studyRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmRepository = germplasmRepository;
        this.cropOntologyRepository = cropOntologyRepository;
        this.trialRepository = trialRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/{studyId}")
    public ModelAndView get(@PathVariable("studyId") String studyId, Locale locale) {
        StudyDetailVO study = studyRepository.getById(studyId);

        if (study == null) {
            throw new NotFoundException("Study with ID " + studyId + " not found");
        }

        List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
            XRefDocumentSearchCriteria.forXRefId(study.getStudyDbId())
        );

        List<GermplasmVO> germplasms = getGermplasms(study);
        List<ObservationVariableVO> variables = getVariables(study, locale);
        List<TrialVO> trials = getTrials(study);
        LocationVO location = getLocation(study);

        return new ModelAndView("study",
                                "model",
                                new StudyModel(
                                    study,
                                    faidareProperties.getByUri(study.getSourceUri()),
                                    germplasms,
                                    variables,
                                    trials,
                                    crossReferences,
                                    location
                                )
        );
    }

    @GetMapping(value = "/sitemap-{index}.txt")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> sitemap(@PathVariable("index") int index) {
        if (index < 0 || index >= Sitemaps.BUCKET_COUNT) {
            throw new NotFoundException("no sitemap for this index");
        }
        StreamingResponseBody body = out -> {
            Iterator<StudySitemapVO> iterator = studyRepository.scrollAllForSitemap(1000);
            Sitemaps.generateSitemap(
                "/sudies/sitemap-" + index + ".txt",
                out,
                iterator,
                vo -> Math.floorMod(vo.getStudyDbId().hashCode(), Sitemaps.BUCKET_COUNT) == index,
                vo -> "/studies/" + vo.getStudyDbId());
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private LocationVO getLocation(StudyDetailVO study) {
        if (Strings.isBlank(study.getLocationDbId())) {
            return null;
        }
        return locationRepository.getById(study.getLocationDbId());
    }

    private List<GermplasmVO> getGermplasms(StudyDetailVO study) {
        if (study.getGermplasmDbIds() == null || study.getGermplasmDbIds().isEmpty()) {
            return Collections.emptyList();
        } else {
            GermplasmPOSTSearchCriteria germplasmCriteria = new GermplasmPOSTSearchCriteria();
            germplasmCriteria.setGermplasmDbIds(Lists.newArrayList(study.getGermplasmDbIds()));
            return germplasmRepository.find(germplasmCriteria)
                .stream()
                .sorted(Comparator.comparing(GermplasmVO::getGermplasmName))
                .collect(Collectors.toList());
        }
    }

    private List<ObservationVariableVO> getVariables(StudyDetailVO study, Locale locale) {
        Set<String> variableIds = studyRepository.getVariableIds(study.getStudyDbId());
        List<ObservationVariableVO> variables = cropOntologyRepository.getVariableByIds(variableIds);
        return filterVariablesForLocale(variables, locale)
            .sorted(Comparator.comparing(ObservationVariableVO::getObservationVariableDbId))
            .collect(Collectors.toList());
    }

    /**
     * Filter the variables by language. The principles are the following. First, the languages of the variables
     * are normalized (to transform FRA into fr for example).
     * Then, several cases are possible.
     *
     * If there is no variable with the requested language, then we find the reference language.
     * The reference language is en if there is at least one variable with that language.
     * The reference is the first non null language found if there is no variable with the en language.
     * Then, we keep all the variables with the reference language (if any), and all the variables without language.
     *
     * If there is at least one variable with the requested language, then we keep all the variables
     * with the requested language, and all the variables without language.
     */
    private Stream<ObservationVariableVO> filterVariablesForLocale(List<ObservationVariableVO> variables, Locale locale) {
        if (variables.isEmpty()) {
            return variables.stream();
        }

        String requestedLanguage = locale.getLanguage();
        String referenceLanguage = findReferenceLanguage(requestedLanguage, variables);

        return variables.stream()
                        .filter(variable ->
                                    referenceLanguage == null
                                        || !StringUtils.hasText(variable.getLanguage())
                                        || normalizeLanguage(variable.getLanguage()).equals(referenceLanguage));
    }

    private String findReferenceLanguage(String requestedLanguage, List<ObservationVariableVO> variables) {
        Set<String> normalizedVariableLanguages =
            variables.stream()
                     .map(ObservationVariableVO::getLanguage)
                     .filter(StringUtils::hasText)
                     .map(this::normalizeLanguage)
                     .collect(Collectors.toSet());

        String referenceLanguage = null;
        if (normalizedVariableLanguages.contains(requestedLanguage)) {
            referenceLanguage = requestedLanguage;
        } else if (normalizedVariableLanguages.contains("en")) {
            referenceLanguage = "en";
        } else if (!normalizedVariableLanguages.isEmpty()) {
            referenceLanguage = normalizedVariableLanguages.iterator().next();
        }
        return referenceLanguage;
    }

    private String normalizeLanguage(String language) {
        // this is a hack trying to accomodate for languages not bein standard in the data
        String languageInLowerCase = language.toLowerCase();
        if (languageInLowerCase.length() == 3) {
            switch (languageInLowerCase) {
                case "fra":
                    return "fr";
                case "esp":
                case "spa":
                    return "es";
                case "eng":
                    return "en";
            }
        }
        return languageInLowerCase;
    }

    private List<TrialVO> getTrials(StudyDetailVO study) {
        if (study.getTrialDbIds() == null || study.getTrialDbIds().isEmpty()) {
            return Collections.emptyList();
        }

        return study.getTrialDbIds()
                    .stream()
                    .sorted(Comparator.naturalOrder())
                    .map(trialRepository::getById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }


}
