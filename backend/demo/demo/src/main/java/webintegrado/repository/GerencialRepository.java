package webintegrado.repository;

import webintegrado.model.Gerencial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GerencialRepository extends JpaRepository<Gerencial, Integer> {
    List<Gerencial> findByProductoIdProducto(Integer idProducto);
    List<Gerencial> findByTipoMovimiento(Gerencial.TipoMovimiento tipo);
}