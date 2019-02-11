import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {
    BrapiGermplasm,
    BrapiLocation,
    BrapiObservationVariable,
    BrapiResult,
    BrapiResults,
    BrapiStudy,
    BrapiTrial
} from './models/brapi.model';

export const BASE_URL = 'brapi/v1';

@Injectable({
    providedIn: 'root'
})
export class BrapiService {

    constructor(private http: HttpClient) {
    }

    germplasm(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`${BASE_URL}/germplasm/${germplasmDbId}`);
    }

    germplasmPedigree(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`${BASE_URL}/germplasm/${germplasmDbId}/pedigree`);
    }

    germplasmProgeny(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`${BASE_URL}/germplasm/${germplasmDbId}/progeny`);
    }

    germplasmAttributes(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`${BASE_URL}/germplasm/${germplasmDbId}/attributes`);
    }

    study(studyDbId: string): Observable<BrapiResult<BrapiStudy>> {
        const options = { headers: { 'Accept': 'application/ld+json,application/json' } };
        return this.http.get<BrapiResult<BrapiStudy>>(`${BASE_URL}/studies/${studyDbId}`, options);
    }

    studyGermplasms(studyDbId: string): Observable<BrapiResults<BrapiGermplasm>> {
        return this.http.get<BrapiResults<BrapiGermplasm>>(`${BASE_URL}/studies/${studyDbId}/germplasm`);
    }

    studyObservationVariables(studyDbId: string): Observable<BrapiResults<BrapiObservationVariable>> {
        return this.http.get<BrapiResults<BrapiObservationVariable>>(`${BASE_URL}/studies/${studyDbId}/observationVariables`);
    }

    location(locationId: string): Observable<BrapiResult<BrapiLocation>> {
        return this.http.get<BrapiResult<BrapiLocation>>(`${BASE_URL}/locations/${locationId}`);
    }

    studyTrials(trialsId: string): Observable<BrapiResult<BrapiTrial>> {
        return this.http.get<BrapiResult<BrapiTrial>>(`${BASE_URL}/trials/${trialsId}`);
    }

}