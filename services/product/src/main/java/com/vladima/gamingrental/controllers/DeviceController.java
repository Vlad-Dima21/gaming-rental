package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;
import com.vladima.gamingrental.handlers.ExceptionsHandler;
import com.vladima.gamingrental.services.DeviceService;
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
@RequestMapping("/api/units")
@RequiredArgsConstructor
@RateLimiter(name = "deviceControllerLimiter")
public class DeviceController {
    private final DeviceService deviceService;

    @Operation(summary = "Get device units by the device ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DeviceResponseDTO.class))
                    ),
                    description = "Device units returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Device base not found"
            )
    })
    @GetMapping("/of-id/{id}")
    public ResponseEntity<List<DeviceResponseDTO>> getByDeviceBaseId(
            @PathVariable @Parameter(description = "Device base ID") @Min(1) Long id
    ) {
        return new ResponseEntity<>(deviceService.getByDeviceBaseId(id), HttpStatus.OK);
    }

    @Operation(summary = "Get device unit by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeviceResponseDTO.class)
                    ),
                    description = "Device unit returned"
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
    public ResponseEntity<DeviceResponseDTO> getById(
            @PathVariable @Parameter(description = "Device unit ID") @Min(1) Long id
    ) {
        return new ResponseEntity<>(deviceService.getById(id), HttpStatus.OK);
    }
}
