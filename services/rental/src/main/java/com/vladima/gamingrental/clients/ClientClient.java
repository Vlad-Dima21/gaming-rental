package com.vladima.gamingrental.clients;

import com.vladima.gamingrental.dtos.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-client", url = "${application.config.client-url}")
public interface ClientClient {
    @GetMapping("/{clientId}")
    ResponseEntity<ClientDTO> getClientById(@PathVariable Long clientId);
}
