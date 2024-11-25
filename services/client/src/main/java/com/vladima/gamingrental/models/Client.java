package com.vladima.gamingrental.models;

import com.vladima.gamingrental.dtos.ClientDTO;
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

    @Column(nullable = false)
    private Long clientUserId;

    public Client(String clientName, String clientEmail, String clientPhone) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }

    public ClientDTO toDTO() {
        return new ClientDTO(clientName, clientEmail, clientPhone);
    }
}
