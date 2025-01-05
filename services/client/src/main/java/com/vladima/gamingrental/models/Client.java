package com.vladima.gamingrental.models;

import com.vladima.gamingrental.dtos.ClientDTO;
import com.vladima.gamingrental.dtos.ClientResponseDTO;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Table
@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long clientId;

    @Column(nullable = false)
    private String clientName;

    @Column(unique = true, nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private String clientPhone;

    public Client(String clientName, String clientEmail, String clientPhone) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }

    public ClientResponseDTO toResponse() {
        return new ClientResponseDTO(clientId, clientName, clientEmail, clientPhone);
    }
}
