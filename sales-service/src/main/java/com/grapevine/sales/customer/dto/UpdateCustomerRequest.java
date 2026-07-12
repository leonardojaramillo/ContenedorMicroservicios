package com.grapevine.sales.customer.dto;

import com.grapevine.sales.customer.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRequest {
    private String razonSocial;
    private DocumentType tipoDocumento;
    private String documento;
    private String contacto;
    private String telefono;
    private String email;
    private String segmento;
}