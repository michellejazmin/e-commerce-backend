package com.uade.tpo.e_commerce.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String message) {
        super(message);
    }

    public StockInsuficienteException(Long ingredienteId, Integer stockActual, Integer cantidadSolicitada) {
        super(String.format("Stock insuficiente para el ingrediente ID %d. Stock disponible: %d, cantidad solicitada: %d",
                ingredienteId, stockActual, cantidadSolicitada));
    }
}
