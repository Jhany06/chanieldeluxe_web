package webintegrado.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webintegrado.dto.response.StockResponse;
import webintegrado.model.Stock;
import webintegrado.repository.StockRepository;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;

    @GetMapping
    public ResponseEntity<List<StockResponse>> listarTodo() {
        return ResponseEntity.ok(stockRepository.findAll()
                .stream().map(this::toResponse).toList());
    }

    @GetMapping("/bajo-minimo")
    public ResponseEntity<List<StockResponse>> bajominimo() {
        return ResponseEntity.ok(stockRepository.findStockBajoMinimo()
                .stream().map(this::toResponse).toList());
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<StockResponse> actualizar(
            @PathVariable Integer idProducto,
            @RequestParam Integer cantidad) {
        Stock stock = stockRepository.findByProductoIdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado"));
        stock.setCantidadDisponible(cantidad);
        return ResponseEntity.ok(toResponse(stockRepository.save(stock)));
    }

    private StockResponse toResponse(Stock s) {
        StockResponse r = new StockResponse();
        r.setIdProducto(s.getProducto().getIdProducto());
        r.setNombreProducto(s.getProducto().getNombre());
        r.setCantidadDisponible(s.getCantidadDisponible());
        r.setCantidadMinima(s.getCantidadMinima());
        r.setBajominimo(s.getCantidadDisponible() <= s.getCantidadMinima());
        return r;
    }
}