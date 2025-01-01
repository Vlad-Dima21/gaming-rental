package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;
import com.vladima.gamingrental.dtos.GameCopyResponseDTO;
import com.vladima.gamingrental.handlers.ExceptionsHandler;
import com.vladima.gamingrental.services.GameCopyService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@RateLimiter(name = "gameCopyControllerLimiter")
public class GameCopyController {
    private final GameCopyService gameCopyService;

    @Operation(summary = "Get filtered game copies")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GameCopyResponseDTO.class))
                    ),
                    description = "Game copies returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionsHandler.ExceptionFormat.class)
                    ),
                    description = "Game or device not found"
            )
    })
    @GetMapping
    public ResponseEntity<List<GameCopyResponseDTO>> getGameCopies(
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) Long deviceId
    ) {
        return new ResponseEntity<>(gameCopyService.getCopies(gameId, deviceId), HttpStatus.OK);
    }
}
