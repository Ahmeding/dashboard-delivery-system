package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.driver.DriverResponse;
import com.hitecart.deliverysystem.dto.driver.DriverUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

/**
 * Driver API Controller Interface
 * This interface defines all REST endpoints for driver management operations
 * in the HitecCart delivery system.
 *
 * @author HitecCart-TN Development Team
 * @version 1.0
 * @since 2024
 */
@RequestMapping(DRIVER_ENDPOINT)
@Tag(name = "Driver")
public interface DriverApi {

    /**
     * Creates or updates a driver in the system.
     *
     * @param driverRequest the driver data to save or update
     * @return ResponseEntity containing the driver ID if successful
     */
    @PostMapping(
            value = CREATE_DRIVER_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveDriver",
            description = "Create a driver. This method allows " +
                    "to register or modify a driver",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Driver object has been created/modified successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DriverResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Driver object is not valid",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<Integer> saveDriver(@RequestBody DriverRequest driverRequest);

    /**
     * Updates specific driver information.
     *
     * @param driverId the ID of the driver to update
     * @param request the driver update data
     * @return ResponseEntity containing the updated driver ID
     */
    @PatchMapping(
            value = UPDATE_DRIVER_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateDriverInfo",
            description = "Update driver information. This method allows " +
                    "to modify specific driver details",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Driver object has been updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DriverResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Driver object is not valid",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<Integer> updateDriverInfo(@PathVariable("driverId") Integer driverId, @RequestBody DriverUpdateRequest request);

    /**
     * Retrieves a driver by their unique ID.
     *
     * @param id the driver ID to search for
     * @return ResponseEntity containing the driver data if found
     */
    @GetMapping(
            value = FIND_DRIVER_BY_ID_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findDriverById",
            description = "Search for a driver by ID. This method allows to find a driver by their ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Driver has been found in the database",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Driver with the provided ID does not exist in the database",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DriverResponse> findDriverById(@PathVariable("id") Integer id);

    /**
     * Retrieves a driver by their email address.
     *
     * @param email the driver email to search for
     * @return ResponseEntity containing the driver data if found
     *
     */
    @GetMapping(
            value = FIND_DRIVER_BY_EMAIL_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findDriverByEmail",
            description = "Search for a driver by email. This method allows to find a driver by their email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Driver has been found in the database",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Driver with the provided email does not exist in the database",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<DriverResponse> findDriverByEmail(@PathVariable("email") String email);

    /**
     * Retrieves all drivers filtered by their active status.
     *
     * @param isActive filter by active status (true for active, false for inactive)
     * @return ResponseEntity containing list of drivers matching the active status
     */
    @GetMapping(
            value = FIND_ALL_BY_ISACTIVE_DRIVER_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllDriverIsActive")
    ResponseEntity<List<DriverResponse>> findAllDriverIsActive(@PathVariable("isactive") Boolean isActive);

    /**
     * Retrieves all drivers in the system.
     *
     * @return ResponseEntity containing list of all drivers
     */
    @GetMapping(
            value = FIND_ALL_DRIVER_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllDriver",
            description = "Search for all drivers. This method returns all drivers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Drivers found successfully",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<DriverResponse>> findAllDriver();

    /**
     * Updates the active status of a driver.
     *
     * @param id the driver ID to update
     * @param isActive the new active status
     * @return ResponseEntity containing the updated driver data
     *
     */
    @Operation(operationId = "updateDriverIsActive")
    @PutMapping(value = UPDATE_ISACTIVE_DRIVER_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DriverResponse> updateDriverIsActive(@PathVariable("id") Integer id, @PathVariable("isactive") Boolean isActive);

    /**
     * Retrieves all drivers with pagination support.
     *
     * @param page the page number (default: 0)
     * @param size the page size (default: 10)
     * @return ResponseEntity containing paginated driver data
     */
    @Operation(operationId = "findAllDriversPageable")
    @GetMapping(value = FIND_ALL_DRIVERS_PAGEABLE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<DriverResponse>> findAllDriversPageable(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    );

    /**
     * Deletes a driver from the system.
     *
     * @param id the driver ID to delete
     */
    @DeleteMapping(value = DELETE_DRIVER_ENDPOINT)
    @Operation(operationId = "deleteDriver",
            description = "Delete a driver by ID. This method allows to remove a driver by their ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Driver has been deleted from the database",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Driver with the provided ID does not exist in the database",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteDriver(@PathVariable("id") Integer id);
}
