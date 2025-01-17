package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.RentalRequestDTO;
import com.vladima.gamingrental.dtos.RentalResponseDTO;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import com.vladima.gamingrental.handlers.ExceptionsHandler;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import com.vladima.gamingrental.services.RentalService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@RateLimiter(name = "rentalControllerLimiter")
public class RentalController {
    private final RentalService rentalService;

    private static class OpenAPIPageableResponseDTO {
        int totalPages;
        List<RentalResponseDTO> items;
    }

    @Operation(summary = "Get filtered rentals")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = OpenAPIPageableResponseDTO.class))
                    ),
                    description = "Valid filters and rentals found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Client or device not found"
            )
    })
    @GetMapping
    public ResponseEntity<PageableResponseDTO<RentalResponseDTO>> getRentals(
            @RequestParam(required = false) @Parameter(description = "Client Id") Long  clientId,
            @RequestParam(required = false) @Parameter(description = "Device Id") Long  deviceId,
            @RequestParam(required = false) @Parameter(description = "Rental is returned") Boolean returned,
            @RequestParam(defaultValue = "false") @Parameter(description = "Only rentals that are past due") boolean pastDue,
            @RequestParam(required = false) @Parameter(description = "Page number") @Min(1) Integer page,
            @RequestParam(required = false) @Parameter(description = "Sort by return date") SortDirection sort
    ) {
        return new ResponseEntity<>(rentalService.getRentals(clientId, deviceId, returned, pastDue, page, sort), HttpStatus.OK);
    }

    @Operation(summary = "Register a new rental")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RentalResponseDTO.class)
                    ),
                    description = "Rental was registered successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Device unit not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Device unit not available or game copies not found/not available"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<RentalResponseDTO> createRental(
            @Valid @RequestBody RentalRequestDTO rentalRequestDTO
    ) {
        return new ResponseEntity<>(rentalService.createRental(rentalRequestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Register rental return")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RentalResponseDTO.class)
                    ),
                    description = "Rental was returned successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Rental was not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Rental has been already returned"
            )
    })
    @PatchMapping("/return/{id}")
    public ResponseEntity<RentalResponseDTO> returnRental(@PathVariable @Min(1) @Parameter(description = "Rental ID") Long id) {
        return new ResponseEntity<>(rentalService.rentalReturned(id), HttpStatus.OK);
    }

    @GetMapping("/is-unit-available/{deviceId}")
    public ResponseEntity<Boolean> isUnitAvailable(@PathVariable @Min(1) Long deviceId) {
        return new ResponseEntity<>(rentalService.isDeviceAvailable(deviceId), HttpStatus.OK);
    }
}
