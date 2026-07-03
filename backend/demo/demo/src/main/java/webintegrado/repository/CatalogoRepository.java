package webintegrado.repository;

import webintegrado.model.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
    List<Catalogo> findByEstado(Catalogo.EstadoProducto estado);
    List<Catalogo> findByCategoria(Catalogo.Categoria categoria);
    List<Catalogo> findByEstadoAndCategoria(Catalogo.EstadoProducto estado, Catalogo.Categoria categoria);
}