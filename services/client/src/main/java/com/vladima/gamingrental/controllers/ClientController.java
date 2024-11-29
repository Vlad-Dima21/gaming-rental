package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.dtos.ClientDTO;
import com.vladima.gamingrental.dtos.ClientResponseDTO;
import com.vladima.gamingrental.services.ClientService;
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
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(clientService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.create(clientDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable @Min(1) Long id)
    {
        clientService.delete(id);
        return new ResponseEntity<>("Client deleted", HttpStatus.NO_CONTENT);
    }

}
