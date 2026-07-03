package webintegrado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webintegrado.dto.request.CatalogoRequest;
import webintegrado.dto.response.CatalogoResponse;
import webintegrado.model.Catalogo;
import webintegrado.model.Stock;
import webintegrado.model.Usuario;
import webintegrado.repository.CatalogoRepository;
import webintegrado.repository.StockRepository;
import webintegrado.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoService {

        private final CatalogoRepository catalogoRepository;
        private final StockRepository stockRepository;
        private final UsuarioRepository usuarioRepository;

        public CatalogoResponse crear(CatalogoRequest request, Integer idAdmin) {
                Usuario admin = usuarioRepository.findById(idAdmin)
                        .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

                Catalogo producto = Catalogo.builder()
                        .nombre(request.getNombre())
                        .descripcion(request.getDescripcion())
                        .categoria(Catalogo.Categoria.valueOf(request.getCategoria()))
                        .talla(Catalogo.Talla.valueOf(request.getTalla()))
                        .color(request.getColor())
                        .material(request.getMaterial())
                        .precioUnitario(request.getPrecioUnitario())
                        .precioOferta(request.getPrecioOferta())
                        .imagenUrl(request.getImagenUrl())
                        .estado(Catalogo.EstadoProducto.activo)
                        .fechaCreacion(LocalDateTime.now())
                        .admin(admin)
                        .build();

                Catalogo saved = catalogoRepository.save(producto);

                Stock stock = Stock.builder()
                        .producto(saved)
                        .cantidadDisponible(request.getStockDisponible() != null ? request.getStockDisponible() : 0)
                        .cantidadMinima(3)
                        .build();
                stockRepository.save(stock);

                return toResponse(saved);
        }

        public List<CatalogoResponse> listarActivos() {
                return catalogoRepository.findByEstado(Catalogo.EstadoProducto.activo)
                        .stream().map(this::toResponse).toList();
        }

        public List<CatalogoResponse> listarTodos() {
                return catalogoRepository.findAll()
                        .stream().map(this::toResponse).toList();
        }

        public CatalogoResponse buscarPorId(Integer id) {
                return toResponse(catalogoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado")));
        }

        public CatalogoResponse actualizar(Integer id, CatalogoRequest request) {
                Catalogo producto = catalogoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                producto.setNombre(request.getNombre());
                producto.setDescripcion(request.getDescripcion());
                producto.setCategoria(Catalogo.Categoria.valueOf(request.getCategoria()));
                producto.setTalla(Catalogo.Talla.valueOf(request.getTalla()));
                producto.setColor(request.getColor());
                producto.setMaterial(request.getMaterial());
                producto.setPrecioUnitario(request.getPrecioUnitario());
                producto.setPrecioOferta(request.getPrecioOferta());
                producto.setImagenUrl(request.getImagenUrl());
                producto.setFechaActualizacion(LocalDateTime.now());

                if (request.getStockDisponible() != null) {
                stockRepository.findByProductoIdProducto(id).ifPresent(stock -> {
                        stock.setCantidadDisponible(request.getStockDisponible());
                        stockRepository.save(stock);
                });
                }

                return toResponse(catalogoRepository.save(producto));
        }

        public void eliminar(Integer id) {
                Catalogo producto = catalogoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                producto.setEstado(Catalogo.EstadoProducto.inactivo);
                catalogoRepository.save(producto);
        }

        private CatalogoResponse toResponse(Catalogo c) {
                CatalogoResponse r = new CatalogoResponse();
                r.setIdProducto(c.getIdProducto());
                r.setNombre(c.getNombre());
                r.setDescripcion(c.getDescripcion());
                r.setCategoria(c.getCategoria().name());
                r.setTalla(c.getTalla().name());
                r.setColor(c.getColor());
                r.setMaterial(c.getMaterial());
                r.setPrecioUnitario(c.getPrecioUnitario());
                r.setPrecioOferta(c.getPrecioOferta());
                r.setImagenUrl(c.getImagenUrl());
                r.setEstado(c.getEstado().name());
                stockRepository.findByProductoIdProducto(c.getIdProducto())
                        .ifPresent(s -> r.setStockDisponible(s.getCantidadDisponible()));
                return r;
        }
}