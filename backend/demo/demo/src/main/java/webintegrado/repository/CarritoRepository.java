package webintegrado.repository;

import webintegrado.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    List<Carrito> findByUsuarioIdUsuario(Integer idUsuario);
    Optional<Carrito> findByUsuarioIdUsuarioAndProductoIdProducto(Integer idUsuario, Integer idProducto);
    void deleteByUsuarioIdUsuario(Integer idUsuario);
}