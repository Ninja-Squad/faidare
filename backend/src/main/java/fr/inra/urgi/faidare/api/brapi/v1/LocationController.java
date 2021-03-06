package fr.inra.urgi.faidare.api.brapi.v1;

import javax.validation.Valid;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiLocation;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.LocationCriteria;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 */
@Tag(name = "Breeding API", description = "BrAPI endpoint")
@RestController
public class LocationController {

    private final LocationRepository repository;

    @Autowired
    public LocationController(LocationRepository repository) {
        this.repository = repository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/LocationDetails.md
     */
    @Operation(summary = "Get location")
    @GetMapping("/brapi/v1/locations/{locationDbId}")
    public BrapiResponse<BrapiLocation> getLocation(@PathVariable String locationDbId) {
        BrapiLocation location = repository.getById(locationDbId);
        if (location == null) {
            throw new NotFoundException("Location not found for id '" + locationDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(location, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/ListLocations.md
     */
    @Operation(summary = "List locations")
    @GetMapping("/brapi/v1/locations")
    public BrapiListResponse<? extends BrapiLocation> listLocations(
        @Valid LocationCriteria criteria
    ) {
        PaginatedList<? extends BrapiLocation> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }
}
