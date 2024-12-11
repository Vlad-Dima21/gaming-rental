package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.DeviceBaseResponseDTO;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import com.vladima.gamingrental.services.DeviceBaseService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceBaseController {
    private final DeviceBaseService deviceBaseService;


    @GetMapping("/{id}")
    public ResponseEntity<DeviceBaseResponseDTO> getDeviceById(
            @PathVariable @Min(1) Long id
    ) {
        return new ResponseEntity<>(deviceBaseService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponseDTO<DeviceBaseResponseDTO>> getFilteredDevices(
            @RequestParam(required = false)  String name,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "false") boolean ifAvailable,
            @RequestParam(required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "asc") SortDirection sort
    ) {
        return new ResponseEntity<>(deviceBaseService.getFiltered(name, producer, year, ifAvailable, page, sort), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceBaseResponseDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceBaseService.getAll());
    }
}
