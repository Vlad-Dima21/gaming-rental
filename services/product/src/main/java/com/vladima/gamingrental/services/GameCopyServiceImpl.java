package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.GameCopyResponseDTO;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import com.vladima.gamingrental.models.GameCopy;
import com.vladima.gamingrental.repositories.GameCopyRepository;
import com.vladima.gamingrental.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameCopyServiceImpl implements GameCopyService {
    private final GameCopyRepository repository;
    private final GameRepository gameRepository;
    private final DeviceBaseService deviceBaseService;

    @Override
    public List<GameCopyResponseDTO> getCopies(Long gameId, Long deviceId) {
        String incorrectModelName = null;
        if (gameId != null && !gameRepository.existsById(gameId)) incorrectModelName = "game";
        if (deviceId != null) {
            try {
                deviceBaseService.getById(deviceId);
            } catch (EntityOperationException e) {
                incorrectModelName = "device";
            }
        }
        if (incorrectModelName != null) {
            throw new EntityOperationException(
                    MessageFormat.format("Incorrect {0} ID", incorrectModelName),
                    "",
                    HttpStatus.NOT_FOUND
            );
        }
        return repository.findCopies(gameId, deviceId).stream().map(GameCopy::toResponse).toList();
    }
}
