package com.uade.tpo.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.uade.tpo.e_commerce.dto.DetallePedidoDTO;
import com.uade.tpo.e_commerce.dto.DetallePedidoRequestDTO;
import com.uade.tpo.e_commerce.model.DetallePedido;
import com.uade.tpo.e_commerce.model.Ingrediente;
import com.uade.tpo.e_commerce.model.Pedido;
import com.uade.tpo.e_commerce.repository.DetallePedidoRepository;
import com.uade.tpo.e_commerce.repository.IngredienteRepository;
import com.uade.tpo.e_commerce.repository.PedidoRepository;

import com.uade.tpo.e_commerce.exception.DetallePedidoNotFoundException;
import com.uade.tpo.e_commerce.exception.IngredienteNotFoundException;
import com.uade.tpo.e_commerce.exception.PedidoNotFoundException;


@Service
@Transactional
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private IngredienteService ingredienteService;

    public List<DetallePedidoDTO> getAllDetallesPedidos() {
        return detallePedidoRepository.findAll()
                .stream()
                .map(d -> new DetallePedidoDTO(d.getId_DetallePedido(), d.getCantidad(), d.getPrecioTotal()))
                .collect(Collectors.toList());
    }

    public DetallePedidoDTO getDetallePedidoById(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id).orElse(null);

        if (detalle == null) {
            throw new DetallePedidoNotFoundException(id);
        }

        return new DetallePedidoDTO(
                detalle.getId_DetallePedido(),
                detalle.getCantidad(),
                detalle.getPrecioTotal()
        );
    }

    public void deleteDetallePedidoById(Long id) {
        detallePedidoRepository.deleteById(id);
    }

    public DetallePedidoDTO saveDetallePedido(DetallePedidoDTO detallePedidoDTO, Long ingredienteId) {
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new IngredienteNotFoundException(ingredienteId));

        ingredienteService.disminuirStock(ingredienteId, detallePedidoDTO.getCantidad());

        DetallePedido detalle = DetallePedido.builder()
                .cantidad(detallePedidoDTO.getCantidad())
                .precioTotal(detallePedidoDTO.getPrecioTotal())
                .ingrediente(ingrediente)
                .build();

        DetallePedido guardado = detallePedidoRepository.save(detalle);

        return new DetallePedidoDTO(
                guardado.getId_DetallePedido(),
                guardado.getCantidad(),
                guardado.getPrecioTotal()
        );
    }

    public DetallePedidoDTO updateDetallePedido(Long id, DetallePedidoDTO detallePedidoDTO) {
        DetallePedido detalle = detallePedidoRepository.findById(id).orElse(null);

        if (detalle == null) {
            throw new DetallePedidoNotFoundException(id);
        }

        detalle.setCantidad(detallePedidoDTO.getCantidad());
        detalle.setPrecioTotal(detallePedidoDTO.getPrecioTotal());

        DetallePedido actualizado = detallePedidoRepository.save(detalle);

        return new DetallePedidoDTO(
                actualizado.getId_DetallePedido(),
                actualizado.getCantidad(),
                actualizado.getPrecioTotal()
        );
    }

    public DetallePedidoDTO saveDetallePedidoConStock(DetallePedidoRequestDTO requestDTO) {
        Ingrediente ingrediente = ingredienteRepository.findById(requestDTO.getIngredienteId())
                .orElseThrow(() -> new IngredienteNotFoundException(requestDTO.getIngredienteId()));

        Pedido pedido = pedidoRepository.findById(requestDTO.getPedidoId())
                .orElseThrow(() -> new PedidoNotFoundException(requestDTO.getPedidoId()));

        ingredienteService.disminuirStock(requestDTO.getIngredienteId(), requestDTO.getCantidad());

        DetallePedido detalle = DetallePedido.builder()
                .cantidad(requestDTO.getCantidad())
                .precioTotal(requestDTO.getPrecioTotal())
                .ingrediente(ingrediente)
                .pedido(pedido)
                .build();

        DetallePedido guardado = detallePedidoRepository.save(detalle);

        return new DetallePedidoDTO(
                guardado.getId_DetallePedido(),
                guardado.getCantidad(),
                guardado.getPrecioTotal()
        );
    }
}