package webintegrado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webintegrado.dto.request.CarritoRequest;
import webintegrado.dto.response.CarritoResponse;
import webintegrado.model.Carrito;
import webintegrado.model.Catalogo;
import webintegrado.model.Usuario;
import webintegrado.repository.CarritoRepository;
import webintegrado.repository.CatalogoRepository;
import webintegrado.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CatalogoRepository catalogoRepository;

    public CarritoResponse agregar(CarritoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Catalogo producto = catalogoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito carrito = carritoRepository
                .findByUsuarioIdUsuarioAndProductoIdProducto(
                        request.getIdUsuario(), request.getIdProducto())
                .map(c -> { c.setCantidad(c.getCantidad() + request.getCantidad()); return c; })
                .orElse(Carrito.builder()
                        .usuario(usuario)
                        .producto(producto)
                        .cantidad(request.getCantidad())
                        .fechaAgregado(LocalDateTime.now())
                        .build());

        return toResponse(carritoRepository.save(carrito));
    }

    public List<CarritoResponse> listarPorUsuario(Integer idUsuario) {
        return carritoRepository.findByUsuarioIdUsuario(idUsuario)
                .stream().map(this::toResponse).toList();
    }

    public void eliminar(Integer idCarrito) {
        carritoRepository.deleteById(idCarrito);
    }

    public void vaciar(Integer idUsuario) {
        carritoRepository.deleteByUsuarioIdUsuario(idUsuario);
    }

    private CarritoResponse toResponse(Carrito c) {
        CarritoResponse r = new CarritoResponse();
        r.setIdCarrito(c.getIdCarrito());
        r.setIdProducto(c.getProducto().getIdProducto());
        r.setNombreProducto(c.getProducto().getNombre());
        r.setCantidad(c.getCantidad());
        r.setPrecioUnitario(c.getProducto().getPrecioUnitario());
        r.setSubtotal(c.getProducto().getPrecioUnitario()
                .multiply(java.math.BigDecimal.valueOf(c.getCantidad())));
        return r;
    }
}