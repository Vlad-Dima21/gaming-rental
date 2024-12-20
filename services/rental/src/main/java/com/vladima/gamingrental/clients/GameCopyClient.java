package com.vladima.gamingrental.clients;

import com.vladima.gamingrental.dtos.GameCopyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "game-client", url = "${application.config.game-url}")
public interface GameCopyClient {
    @GetMapping
    ResponseEntity<List<GameCopyDTO>> getGameCopies(@RequestParam Long deviceId);
}
