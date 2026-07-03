package webintegrado.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webintegrado.dto.request.UsuarioRequest;
import webintegrado.dto.response.UsuarioResponse;
import webintegrado.service.UsuarioService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(usuarioService.login(body.get("email"), body.get("contrasena")));
    }

    @GetMapping("/generar-hash")
    public ResponseEntity<String> generarHash() {
        return ResponseEntity.ok(usuarioService.generarHash("123456"));
    }
}