package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.DeviceBaseResponseDTO;
import com.vladima.gamingrental.handlers.ExceptionsHandler;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import com.vladima.gamingrental.services.DeviceBaseService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@RateLimiter(name = "deviceBaseControllerLimiter")
public class DeviceBaseController {
    private final DeviceBaseService deviceBaseService;


    @Operation(summary = "Get device by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeviceBaseResponseDTO.class)
                    ),
                    description = "Device found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Device not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceBaseResponseDTO> getDeviceById(
            @PathVariable @Min(1) @Parameter(description = "Device ID") Long id
    ) {
        return new ResponseEntity<>(deviceBaseService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get filtered devices")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DeviceBaseResponseDTO.class))
                    ),
                    description = "Valid filters"
            )
    })
    @GetMapping
    public ResponseEntity<PageableResponseDTO<DeviceBaseResponseDTO>> getFilteredDevices(
            @RequestParam(required = false) @Parameter(description = "Name of device") String name,
            @RequestParam(required = false) @Parameter(description = "Producer name") String producer,
            @RequestParam(required = false) @Parameter(description = "Released after the year") Integer year,
            @RequestParam(required = false, defaultValue = "1") @Parameter(description = "Page number") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Sort devices by name") SortDirection sort
    ) {
        return new ResponseEntity<>(deviceBaseService.getFiltered(name, producer, year, page, sort), HttpStatus.OK);
    }

    @Operation(summary = "Get all devices")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DeviceBaseResponseDTO.class))
                    ),
                    description = "All devices"
            )
    })
    @GetMapping("/all")
    public ResponseEntity<List<DeviceBaseResponseDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceBaseService.getAll());
    }
}
