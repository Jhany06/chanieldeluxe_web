package webintegrado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import webintegrado.dto.request.UsuarioRequest;
import webintegrado.dto.response.UsuarioResponse;
import webintegrado.model.Usuario;
import webintegrado.repository.UsuarioRepository;
import webintegrado.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  

    public UsuarioResponse registrar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        Usuario usuario = Usuario.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .email(request.getEmail())
            .contrasena(passwordEncoder.encode(request.getContrasena()))
            .telefono(request.getTelefono())
            .direccion(request.getDireccion())
            .ciudad(request.getCiudad())
            .rol(Usuario.Rol.cliente)
            .estado(Usuario.Estado.activo)
            .emailVerificado(false)
            .fechaRegistro(LocalDateTime.now())
            .ultimoAcceso(null)
            .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse).toList();
    }

    public UsuarioResponse buscarPorId(Integer id) {
        return toResponse(usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
    }

    public UsuarioResponse login(String email, String contrasena) {
        Usuario u = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(contrasena, u.getContrasena()))
            throw new RuntimeException("Contraseña incorrecta");
        UsuarioResponse response = toResponse(u);
        response.setToken(jwtUtil.generateToken(u.getEmail(), u.getRol().name()));
        return response;
    }

    private UsuarioResponse toResponse(Usuario u) {
        UsuarioResponse r = new UsuarioResponse();
        r.setIdUsuario(u.getIdUsuario());
        r.setNombre(u.getNombre());
        r.setApellido(u.getApellido());
        r.setEmail(u.getEmail());
        r.setTelefono(u.getTelefono());
        r.setDireccion(u.getDireccion());
        r.setCiudad(u.getCiudad());
        r.setRol(u.getRol().name());
        r.setEstado(u.getEstado().name());
        return r;
    }

    public String generarHash(String texto) {
        return passwordEncoder.encode(texto);
    }
    
    
}