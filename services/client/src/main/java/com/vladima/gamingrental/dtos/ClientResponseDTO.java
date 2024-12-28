package com.vladima.gamingrental.dtos;

import lombok.Data;

@Data
public class ClientResponseDTO {

    private Long clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;

    public ClientResponseDTO(Long clientId, String clientName, String clientEmail, String clientPhone) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }
}
