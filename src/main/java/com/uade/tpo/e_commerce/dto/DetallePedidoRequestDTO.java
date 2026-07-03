package com.uade.tpo.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoRequestDTO {
    private Integer cantidad;
    private Double precioTotal;
    private Long ingredienteId;
    private Long pedidoId;
}
