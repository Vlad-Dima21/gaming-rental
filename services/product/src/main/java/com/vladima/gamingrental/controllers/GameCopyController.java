package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.GameCopyResponseDTO;
import com.vladima.gamingrental.services.GameCopyService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<List<GameCopyResponseDTO>> getGameCopies(
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) Long deviceId
    ) {
        return new ResponseEntity<>(gameCopyService.getCopies(gameId, deviceId), HttpStatus.OK);
    }
}
