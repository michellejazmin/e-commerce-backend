package com.uade.tpo.e_commerce.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNotFoundException.class)
    public ResponseEntity<String> manejarRecursosNoEncontrados(RecursoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<String> manejarEmailYaRegistrado(EmailYaRegistradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(PrecioNegativoException.class)
    public ResponseEntity<String> manejarPrecioNegativo(PrecioNegativoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<String> manejarStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Maneja EntityNotFoundException (jakarta.persistence) como 404
    // Esto es necesario para que el frontend pueda detectar cuando el carrito
    // no existe y crearlo automáticamente (el CarritoService lanza esta excepción).
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> manejarEntidadNoEncontrada(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErroresGenerales(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + ex.getMessage());
    }

}
