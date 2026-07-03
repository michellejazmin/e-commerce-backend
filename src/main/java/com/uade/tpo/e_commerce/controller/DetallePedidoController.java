package com.uade.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.dto.DetallePedidoDTO;
import com.uade.tpo.e_commerce.dto.DetallePedidoRequestDTO;
import com.uade.tpo.e_commerce.service.DetallePedidoService;


@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public List<DetallePedidoDTO> getAllListaDetallePedido() {
        return detallePedidoService.getAllDetallesPedidos();
    }

    @GetMapping("/{id}")
    public DetallePedidoDTO getDetallePedidoById(@PathVariable Long id) {
        return detallePedidoService.getDetallePedidoById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDetallePedidoById(@PathVariable Long id) {
        detallePedidoService.deleteDetallePedidoById(id);
    }

    @PostMapping
    public ResponseEntity<DetallePedidoDTO> createDetallePedido(@RequestBody DetallePedidoRequestDTO requestDTO) {
        DetallePedidoDTO created = detallePedidoService.saveDetallePedidoConStock(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoDTO> updateDetallePedido(@PathVariable Long id, @RequestBody DetallePedidoDTO detallePedidoDTO) {
        DetallePedidoDTO existing = detallePedidoService.getDetallePedidoById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(detallePedidoService.updateDetallePedido(id, detallePedidoDTO));
    }
}