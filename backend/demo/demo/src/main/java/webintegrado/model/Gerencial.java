package webintegrado.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gerencial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gerencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Catalogo producto;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Usuario admin;

    @Column(name = "cantidad_cambio", nullable = false)
    private Integer cantidadCambio;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_nuevo", nullable = false)
    private Integer stockNuevo;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    public enum TipoMovimiento { venta, ajuste_manual, devolucion, entrada_stock }
}