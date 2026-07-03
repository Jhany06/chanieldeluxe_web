package webintegrado.dto.response;

import lombok.Data;

@Data
public class StockResponse {
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidadDisponible;
    private Integer cantidadMinima;
    private Boolean bajominimo;
}