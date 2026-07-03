package webintegrado.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @Email @NotBlank
    private String email;
    @NotBlank @Size(min = 6)
    private String contrasena;
    private String telefono;
    private String direccion;
    private String ciudad;
}