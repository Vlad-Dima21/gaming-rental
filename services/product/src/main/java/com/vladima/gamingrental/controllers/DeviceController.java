package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;
import com.vladima.gamingrental.services.DeviceService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping("/{id}")
    public ResponseEntity<List<DeviceResponseDTO>> getByDeviceBaseId(
            @PathVariable @Min(1) Long id
    ) {
        return new ResponseEntity<>(deviceService.getByDeviceBaseId(id), HttpStatus.OK);
    }
}
