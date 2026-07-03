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

import com.uade.tpo.e_commerce.dto.IngredienteDTO;
import com.uade.tpo.e_commerce.dto.IngredienteSaveDTO;
import com.uade.tpo.e_commerce.dto.IngredienteStockDTO;
import com.uade.tpo.e_commerce.dto.DisminuirStockDTO;
import com.uade.tpo.e_commerce.service.IngredienteService;



@RestController
// para acceder a este controlador, la URL base será /api/ingredientes
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    // https://localhost:8080/api/ingredientes -> ejecutar este método y devolver un mensaje ejemplo
    @GetMapping
    public List<IngredienteDTO> getAllListaIngredientes() {
        return ingredienteService.getAllIngredientes();
    }

    @GetMapping("/{id}")
    public IngredienteDTO getIngredienteById(@PathVariable Long id) {
        return ingredienteService.getIngredienteById(id);
    }
 
    // del http://localhost:8080/api/ingredientes/1 -> elimina el ingrediente con id 1
    @DeleteMapping("/{id}")
    public void deleteIngredienteById(@PathVariable Long id) {
        ingredienteService.deleteIngredienteById(id);
    }

    // para crear un nuevo ingrediente, se envía un JSON con los datos del ingrediente al endpoint http://localhost:8080/api/ingredientes con el método POST
    @PostMapping
    public ResponseEntity<IngredienteDTO> createIngrediente(@RequestBody IngredienteSaveDTO dto) {
        IngredienteDTO created = ingredienteService.createIngrediente(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    //para actualizar un ingrediente, se envía un JSON con los datos actualizados al endpoint http://localhost:8080/api/ingredientes/{id} con el método PUT
    @PutMapping("/{id}")
    public ResponseEntity<IngredienteDTO> updateIngrediente(@PathVariable Long id, @RequestBody IngredienteSaveDTO dto) {
        IngredienteDTO existing = ingredienteService.getIngredienteById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ingredienteService.updateIngrediente(id, dto));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<IngredienteDTO> updateStock(@PathVariable Long id, @RequestBody IngredienteStockDTO stockDTO) {
        IngredienteDTO ingrediente = ingredienteService.getIngredienteById(id);
        if (ingrediente == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ingredienteService.updateStock(id, stockDTO.getStock()));
    }

    @PutMapping("/{id}/disminuir-stock")
    public ResponseEntity<IngredienteDTO> disminuirStock(@PathVariable Long id, @RequestBody DisminuirStockDTO dto) {
        return ResponseEntity.ok(ingredienteService.disminuirStock(id, dto.getCantidad()));
    }
}

