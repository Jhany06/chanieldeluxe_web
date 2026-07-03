package webintegrado.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CatalogoRequest {
    @NotBlank
    private String nombre;
    private String descripcion;
    @NotBlank
    private String categoria;
    @NotBlank
    private String talla;
    private String color;
    private String material;
    @NotNull
    private BigDecimal precioUnitario;
    private BigDecimal precioOferta;
    private String imagenUrl;
    private Integer stockDisponible;
}