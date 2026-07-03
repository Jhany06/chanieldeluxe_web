package webintegrado.repository;

import webintegrado.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findByProductoIdProducto(Integer idProducto);

    @Query("SELECT s FROM Stock s WHERE s.cantidadDisponible <= s.cantidadMinima")
    List<Stock> findStockBajoMinimo();
}