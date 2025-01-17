package com.vladima.gamingrental.clients;

import com.vladima.gamingrental.dtos.DeviceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "device-client", url = "${application.config.device-url}")
public interface DeviceClient {
    @GetMapping("/{id}")
    ResponseEntity<DeviceDTO> getDeviceById(@PathVariable Long id);
}
