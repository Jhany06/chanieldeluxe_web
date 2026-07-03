package webintegrado.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CatalogoResponse {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String talla;
    private String color;
    private String material;
    private BigDecimal precioUnitario;
    private BigDecimal precioOferta;
    private String imagenUrl;
    private String estado;
    private Integer stockDisponible;
}