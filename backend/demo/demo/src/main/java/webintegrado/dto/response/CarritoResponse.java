package webintegrado.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarritoResponse {
    private Integer idCarrito;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}