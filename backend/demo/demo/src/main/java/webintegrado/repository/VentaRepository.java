package webintegrado.repository;

import webintegrado.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByUsuarioIdUsuario(Integer idUsuario);
    List<Venta> findByEstado(Venta.EstadoVenta estado);
}