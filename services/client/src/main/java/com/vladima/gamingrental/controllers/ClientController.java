package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.ClientDTO;
import com.vladima.gamingrental.dtos.ClientResponseDTO;
import com.vladima.gamingrental.handlers.ExceptionsHandler;
import com.vladima.gamingrental.services.ClientService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@RateLimiter(name = "clientControllerLimiter")
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "Get a client by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientResponseDTO.class)
                    ),
                    description = "Client found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Client not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable @Parameter(description = "The client ID") @Min(1) Long id) {
        return new ResponseEntity<>(clientService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Add a new client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientResponseDTO.class)
                    ),
                    description = "Client added"
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Email or phone is already in use"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.create(clientDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove a client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Client removed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Client not found"
            )
    })
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable @Min(1) Long id)
    {
        clientService.delete(id);
        return new ResponseEntity<>("Client deleted", HttpStatus.NO_CONTENT);
    }

}
