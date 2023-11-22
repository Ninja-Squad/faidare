package fr.inrae.urgi.faidare.web.study;

import fr.inrae.urgi.faidare.api.NotFoundException;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepository;
import fr.inrae.urgi.faidare.dao.v1.GermplasmV1Dao;
import fr.inrae.urgi.faidare.dao.v1.LocationV1Dao;
import fr.inrae.urgi.faidare.dao.v1.StudyV1Dao;
import fr.inrae.urgi.faidare.dao.v1.TrialV1Dao;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.TrialV1VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;
import fr.inrae.urgi.faidare.utils.Sitemaps;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller used to display a study card based on its ID.
 * @author JB Nizet
 */
@Controller("webStudyController")
@RequestMapping("/studies")
public class StudyController {

    private final StudyV1Dao studyRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentDao xRefDocumentRepository;
    private final GermplasmV1Dao germplasmRepository;
    private final CropOntologyRepository cropOntologyRepository;
    private final TrialV1Dao trialRepository;
    private final LocationV1Dao locationRepository;

    public StudyController(StudyV1Dao studyRepository,
                           FaidareProperties faidareProperties,
                           XRefDocumentDao xRefDocumentRepository,
                           GermplasmV1Dao germplasmRepository,
                           CropOntologyRepository cropOntologyRepository,
                           TrialV1Dao trialRepository,
                           LocationV1Dao locationRepository) {
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
        StudyV1VO study = studyRepository.getByStudyDbId(studyId);

        if (study == null) {
            throw new NotFoundException("Study with ID " + studyId + " not found");
        }

        List<XRefDocumentVO> crossReferences = xRefDocumentRepository.findByLinkedResourcesID(study.getStudyDbId());

        List<GermplasmV1VO> germplasms = getGermplasms(study);
        List<ObservationVariableVO> variables = getVariables(study, locale);
        List<TrialV1VO> trials = getTrials(study);
        LocationVO location = getLocation(study);

        return new ModelAndView("study",
                                "model",
                                new StudyModel(
                                    study,
                                    // FIXME JBN replace the next line by the commented-out one when study has a sourceUri
                                    faidareProperties.getByUri("https://urgi.versailles.inrae.fr/gnpis"),
                                    // faidareProperties.getByUri(study.getSourceUri()),
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
            try (Stream<StudySitemapVO> stream = studyRepository.findAllForSitemap()) {
                Sitemaps.generateSitemap(
                    "/sudies/sitemap-" + index + ".txt",
                    out,
                    stream,
                    vo -> Math.floorMod(vo.getStudyDbId().hashCode(),
                                        Sitemaps.BUCKET_COUNT) == index,
                    vo -> "/studies/" + vo.getStudyDbId());
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private LocationVO getLocation(StudyV1VO study) {
        if (Strings.isBlank(study.getLocationDbId())) {
            return null;
        }
        return locationRepository.getByLocationDbId(study.getLocationDbId());
    }

    private List<GermplasmV1VO> getGermplasms(StudyV1VO study) {
        if (study.getGermplasmDbIds() == null || study.getGermplasmDbIds().isEmpty()) {
            return Collections.emptyList();
        } else {
            return germplasmRepository.findByGermplasmDbIdIn(Set.copyOf(study.getGermplasmDbIds()))
                .sorted(Comparator.comparing(GermplasmV1VO::getGermplasmName))
                .collect(Collectors.toList());
        }
    }

    private List<ObservationVariableVO> getVariables(StudyV1VO study, Locale locale) {
        // FIXME JBN uncomment this line once StudyV1Dao has a getVariableIds() method
        // Set<String> variableIds = studyRepository.getVariableIds(study.getStudyDbId());
        Set<String> variableIds = Set.of();

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
            return Stream.empty();
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

    private List<TrialV1VO> getTrials(StudyV1VO study) {
        if (study.getTrialsDbIds() == null || study.getTrialsDbIds().isEmpty()) {
            return Collections.emptyList();
        }

        return study.getTrialsDbIds()
                    .stream()
                    .sorted(Comparator.naturalOrder())
                    .map(trialRepository::getByTrialDbId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }


}
