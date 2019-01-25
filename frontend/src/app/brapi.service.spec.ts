import { TestBed } from '@angular/core/testing';

import { BrapiService } from '.brapi.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {
    BrapiContacts,
    BrapiGermplasme,
    BrapiLocation,
    BrapiObservationVariables,
    BrapiResult,
    BrapiResults,
    BrapiStudy,
    BrapiTrial
} from './modelsbrapi.model';
import { DataDiscoverySource } from './models/data-discovery.model';
import { SiteModel } from './models/site.model';

describe('BrapiService', () => {

    let brapiService: BrapiService;
    let http: HttpTestingController;

    beforeEach(() => TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
    }));

    beforeEach(() => {
        brapiService = TestBed.get(BrapiService);
        http = TestBed.get(HttpTestingController);
    });

    afterAll(() => http.verify());

    const location: BrapiLocation = {
        locationDbId: 1,
        name: 'loc1',
        locationType: 'Collecting site',
        abbreviation: null,
        countryCode: 'Fr',
        countryName: 'France',
        institutionAdress: null,
        institutionName: 'Insti',
        altitude: null,
        latitude: null,
        longitude: null,
    };

    const contacts: BrapiContacts = {
        contactDbId: 'c1',
        name: 'contact1',
        email: 'contact1@email.com',
        type: 'contact',
        institutionName: 'Inst',
    };

    const searchStudy: BrapiResult<BrapiStudy> = {
        metadata: null,
        result: {
            studyDbId: 's1',
            studyType: 'phenotype',
            name: 'study1',
            studyDescription: null,
            seasons: ['winter', '2019'],
            startDate: '2018',
            endDate: null,
            active: true,
            trialDbIds: ['10', '20'],
            location: location,
            contacts: [contacts],
            additionalInfo: null,
            dataLinks: []
        }
    };

    const trial1: BrapiResult<BrapiTrial> = {
        metadata: null,
        result: {
            trialDbId: '10',
            trialName: 'trial_10',
            trialType: 'project',
            active: true,
            studies: [
                { studyDbId: 's1' },
                { studyDbId: 's2' }
            ]

        }
    };

    const osbVariable: BrapiResults<BrapiObservationVariables> = {
        metadata: null,
        result: {
            data: [{
                observationVariableDbId: 'var1',
                contextOfUse: null,
                institution: 'Insti',
                crop: 'WoodyPlant',
                name: 'varaiable1',
                ontologyDbId: 'WPO',
                ontologyName: 'Woody Plant Ontology',
                synonyms: ['First synonym'],
                language: 'EN',
                trait: {
                    traitDbId: 't1',
                    name: 'trait1',
                    description: null,
                },
                documentationURL: null,
            }]
        }
    };

    const germplasm: BrapiResults<BrapiGermplasme> = {
        metadata: null,
        result: {
            data: [{
                germplasmDbId: 'g1',
                accessionNumber: 'G_10',
                germplasmName: 'germplam1',
                genus: 'Populus',
                species: 'x generosa',
                subtaxa: ''
            }, {
                germplasmDbId: 'g2',
                accessionNumber: 'G_20',
                germplasmName: 'germplam2',
                genus: 'Triticum',
                species: 'aestivum',
                subtaxa: 'subsp'
            }],
        }
    };

    const source: DataDiscoverySource = {
        '@id': 'src1',
        '@type': ['schema:DataCatalog'],
        'schema:identifier': 'srcId',
        'schema:name': 'source1',
        'schema:url': 'srcUrl',
        'schema:image': null
    };

    it('should fetch the study', () => {
        let fetchedStudy: BrapiResult<BrapiStudy>;
        const studyDbId: string = searchStudy.result.studyDbId;
        brapiService.getStudy(searchStudy.result.studyDbId).subscribe(response => {
            fetchedStudy = response;
        });
        http.expectOne(`brapi/v1/studies/${studyDbId}`)
            .flush(searchStudy);

        expect(fetchedStudy).toEqual(searchStudy);

    });

    it('should fetch the germplasm', () => {

        let fetchedGermplasm: BrapiResults<BrapiGermplasme>;
        const studyDbId: string = searchStudy.result.studyDbId;
        brapiService.getStudyGermplasms(searchStudy.result.studyDbId).subscribe(response => {
            fetchedGermplasm = response;
        });
        http.expectOne(`brapi/v1/studies/${studyDbId}/germplasm`)
            .flush(germplasm);

        expect(fetchedGermplasm).toEqual(germplasm);

    });

    it('should fetch the variables', () => {

        let fetchedVariables: BrapiResults<BrapiObservationVariables>;
        const studyDbId: string = searchStudy.result.studyDbId;
        brapiService.getStudyObservationVariables(searchStudy.result.studyDbId).subscribe(response => {
            fetchedVariables = response;
        });
        http.expectOne(`brapi/v1/studies/${studyDbId}/observationVariables`)
            .flush(osbVariable);

        expect(fetchedVariables).toEqual(osbVariable);

    });

    it('should fetch the trials', () => {

        let fetchedTrials: BrapiResult<BrapiTrial>;
        const trialDbId: string = trial1.result.trialDbId;
        brapiService.getTrials(trialDbId).subscribe(response => {
            fetchedTrials = response;
        });
        http.expectOne(`brapi/v1/trials/${trialDbId}`)
            .flush(trial1);

        expect(fetchedTrials).toEqual(trial1);

    });
    
    it('should return an Observable of 1 SiteModel', () => {
        const hardCodedSite: SiteModel = {
            result: {
                locationDbId: 1,
                latitude: 1,
                longitude: 1,
                altitude: 1,
                institutionName: '',
                institutionAdress: '',
                countryName: '',
                countryCode: '',
                locationType: '',
                abbreviation: '',
                name: 'site1',
                additionalInfo: {}
            }
        };
        let actualSite: SiteModel;
        const locationId = hardCodedSite.result.locationDbId;
        brapiService.location(hardCodedSite.result.locationDbId).subscribe(site => actualSite = site);

        http.expectOne(`brapi/v1/locations/${locationId}`)
            .flush(hardCodedSite);

        expect(actualSite).toEqual(hardCodedSite);
    });

});
