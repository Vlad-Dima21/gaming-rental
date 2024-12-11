package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.GameCopyResponseDTO;

import java.util.List;

public interface GameCopyService {
    List<GameCopyResponseDTO> getCopies(Long gameId, Long deviceId);
}
