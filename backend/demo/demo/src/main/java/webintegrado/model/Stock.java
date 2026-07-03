package webintegrado.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Integer idStock;

    @OneToOne
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private Catalogo producto;

    @Builder.Default
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible = 0;

    @Builder.Default
    @Column(name = "cantidad_minima", nullable = false)
    private Integer cantidadMinima = 3;

    @Builder.Default
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion = LocalDateTime.now();
}