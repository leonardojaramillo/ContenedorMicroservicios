package com.grapevine.sales.customer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String razonSocial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType tipoDocumento;

    @Column(nullable = false)
    private String documento;

    private String contacto;
    private String telefono;
    private String email;
    private String segmento;

    @Column(nullable = false)
    private Boolean active;
}