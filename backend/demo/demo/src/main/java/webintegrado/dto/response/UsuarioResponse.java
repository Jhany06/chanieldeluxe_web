package webintegrado.dto.response;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String rol;
    private String estado;
    private String token;
}