package com.uade.tpo.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import com.uade.tpo.e_commerce.dto.IngredienteSaveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.uade.tpo.e_commerce.dto.IngredienteDTO;
import com.uade.tpo.e_commerce.model.Ingrediente;
import com.uade.tpo.e_commerce.repository.IngredienteRepository;
import com.uade.tpo.e_commerce.exception.IngredienteNotFoundException;
import com.uade.tpo.e_commerce.exception.StockInsuficienteException;

@Service
@Transactional
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<IngredienteDTO> getAllIngredientes() {
        return ingredienteRepository.findAll()
                .stream()
                .map(i -> new IngredienteDTO(
                        i.getIdIngrediente(),
                        i.getNombre(),
                        i.getDescripcion(),
                        i.getStock(),
                        i.getPrecio()))
                .collect(Collectors.toList());
    }

    public IngredienteDTO getIngredienteById(Long id) {
        Ingrediente ingrediente = ingredienteRepository.findById(id).orElse(null);

        if (ingrediente == null) {
            throw new IngredienteNotFoundException(id);

        }

        return new IngredienteDTO(
                ingrediente.getIdIngrediente(),
                ingrediente.getNombre(),
                ingrediente.getDescripcion(),
                ingrediente.getStock(),
                ingrediente.getPrecio());
    }

    public void deleteIngredienteById(Long id) {
        if (!ingredienteRepository.existsById(id)) {
            throw new IngredienteNotFoundException(id);
        }
        ingredienteRepository.deleteById(id);
    }

    public IngredienteDTO createIngrediente(IngredienteSaveDTO dto) {
        Ingrediente ingrediente = Ingrediente.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio()) 
                .stock(dto.getStock()) 
                .build();
        Ingrediente guardado = ingredienteRepository.save(ingrediente);
        return new IngredienteDTO(
                guardado.getIdIngrediente(),
                guardado.getNombre(),
                guardado.getDescripcion(),
                guardado.getStock(),
                guardado.getPrecio());
    }

    public IngredienteDTO updateIngrediente(Long id, IngredienteSaveDTO dto) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new IngredienteNotFoundException(id));
        ingrediente.setNombre(dto.getNombre());
        ingrediente.setDescripcion(dto.getDescripcion());
        ingrediente.setPrecio(dto.getPrecio());
        ingrediente.setStock(dto.getStock()); 
        Ingrediente actualizado = ingredienteRepository.save(ingrediente);
        return new IngredienteDTO(
                actualizado.getIdIngrediente(),
                actualizado.getNombre(),
                actualizado.getDescripcion(),
                actualizado.getStock(),
                actualizado.getPrecio()); 
    }

    public IngredienteDTO updateStock(Long id, Integer stock) {
        Ingrediente ingrediente = ingredienteRepository.findById(id).orElse(null);
        if (ingrediente == null) {
            throw new IngredienteNotFoundException(id);
        }
        ingrediente.setStock(stock);
        Ingrediente actualizado = ingredienteRepository.save(ingrediente);
        return new IngredienteDTO(
                actualizado.getIdIngrediente(),
                actualizado.getNombre(),
                actualizado.getDescripcion(),
                actualizado.getStock(),
                actualizado.getPrecio());
    }

    public IngredienteDTO disminuirStock(Long id, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a disminuir debe ser mayor a 0");
        }

        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new IngredienteNotFoundException(id));

        Integer stockActual = ingrediente.getStock();
        if (stockActual == null || stockActual < cantidad) {
            throw new StockInsuficienteException(id, stockActual != null ? stockActual : 0, cantidad);
        }

        ingrediente.setStock(stockActual - cantidad);
        Ingrediente actualizado = ingredienteRepository.save(ingrediente);

        return new IngredienteDTO(
                actualizado.getIdIngrediente(),
                actualizado.getNombre(),
                actualizado.getDescripcion(),
                actualizado.getStock(),
                actualizado.getPrecio());
    }
}