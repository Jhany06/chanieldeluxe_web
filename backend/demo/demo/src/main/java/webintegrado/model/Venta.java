package webintegrado.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoVenta estado = EstadoVenta.pendiente;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    @Builder.Default
    private MetodoPago metodoPago = MetodoPago.otro;

    @Column(name = "direccion_envio", nullable = false, length = 300)
    private String direccionEnvio;

    @Column(name = "ciudad_envio", nullable = false, length = 100)
    private String ciudadEnvio;

    @Column(name = "codigo_seguimiento", length = 100)
    private String codigoSeguimiento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;

    public enum EstadoVenta { pendiente, procesado, enviado, entregado, cancelado }
    public enum MetodoPago { tarjeta_credito, tarjeta_debito, transferencia, efectivo, otro }
}