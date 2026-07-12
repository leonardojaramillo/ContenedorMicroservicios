package com.grapevine.sales.customer.dto;

import com.grapevine.sales.customer.DocumentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {
    private Long id;
    private String razonSocial;
    private DocumentType tipoDocumento;
    private String documento;
    private String contacto;
    private String telefono;
    private String email;
    private String segmento;
    private Boolean active;
}