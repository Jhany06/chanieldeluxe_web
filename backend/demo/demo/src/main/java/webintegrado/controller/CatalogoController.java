package webintegrado.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webintegrado.dto.request.CatalogoRequest;
import webintegrado.dto.response.CatalogoResponse;
import webintegrado.service.CatalogoService;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping
    public ResponseEntity<List<CatalogoResponse>> listarActivos() {
        return ResponseEntity.ok(catalogoService.listarActivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<CatalogoResponse>> listarTodos() {
        return ResponseEntity.ok(catalogoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(catalogoService.buscarPorId(id));
    }

    @PostMapping("/admin/{idAdmin}")
    public ResponseEntity<CatalogoResponse> crear(
            @Valid @RequestBody CatalogoRequest request,
            @PathVariable Integer idAdmin) {
        return ResponseEntity.ok(catalogoService.crear(request, idAdmin));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<CatalogoResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CatalogoRequest request) {
        return ResponseEntity.ok(catalogoService.actualizar(id, request));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        catalogoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}