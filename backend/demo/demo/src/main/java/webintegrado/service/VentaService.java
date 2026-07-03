package webintegrado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webintegrado.dto.request.VentaRequest;
import webintegrado.dto.response.VentaResponse;
import webintegrado.model.*;
import webintegrado.repository.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CatalogoRepository catalogoRepository;
    private final StockRepository stockRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CarritoRepository carritoRepository;

    @Transactional
    public VentaResponse crear(VentaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = Venta.builder()
                .usuario(usuario)
                .fechaVenta(LocalDateTime.now())
                .estado(Venta.EstadoVenta.pendiente)
                .metodoPago(Venta.MetodoPago.valueOf(request.getMetodoPago()))
                .direccionEnvio(request.getDireccionEnvio())
                .ciudadEnvio(request.getCiudadEnvio())
                .observaciones(request.getObservaciones())
                .build();

        venta = ventaRepository.save(venta);
        List<DetalleVenta> detalles = new ArrayList<>();

        for (VentaRequest.DetalleRequest d : request.getDetalles()) {
            Catalogo producto = catalogoRepository.findById(d.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getIdProducto()));

            Stock stock = stockRepository.findByProductoIdProducto(d.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Stock no encontrado"));

            if (stock.getCantidadDisponible() < d.getCantidad())
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());

            stock.setCantidadDisponible(stock.getCantidadDisponible() - d.getCantidad());
            stockRepository.save(stock);

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(d.getCantidad())
                    .precioUnitario(producto.getPrecioUnitario())
                    .build();
            detalles.add(detalleVentaRepository.save(detalle));
        }

        carritoRepository.deleteByUsuarioIdUsuario(request.getIdUsuario());
        return toResponse(venta, detalles);
    }

    public List<VentaResponse> listarPorUsuario(Integer idUsuario) {
        return ventaRepository.findByUsuarioIdUsuario(idUsuario)
                .stream().map(v -> toResponse(v, v.getDetalles())).toList();
    }

    public List<VentaResponse> listarTodas() {
        return ventaRepository.findAll()
                .stream().map(v -> toResponse(v, v.getDetalles())).toList();
    }

    public VentaResponse actualizarEstado(Integer idVenta, String nuevoEstado) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        venta.setEstado(Venta.EstadoVenta.valueOf(nuevoEstado));
        ventaRepository.save(venta);
        return toResponse(venta, venta.getDetalles());
    }

    private VentaResponse toResponse(Venta v, List<DetalleVenta> detalles) {
        VentaResponse r = new VentaResponse();
        r.setIdVenta(v.getIdVenta());
        r.setIdUsuario(v.getUsuario().getIdUsuario());
        r.setNombreUsuario(v.getUsuario().getNombre() + " " + v.getUsuario().getApellido());
        r.setFechaVenta(v.getFechaVenta());
        r.setTotal(v.getTotal());
        r.setEstado(v.getEstado().name());
        r.setMetodoPago(v.getMetodoPago().name());
        r.setDireccionEnvio(v.getDireccionEnvio());
        r.setCiudadEnvio(v.getCiudadEnvio());

        if (detalles != null) {
            r.setDetalles(detalles.stream().map(d -> {
                VentaResponse.DetalleResponse dr = new VentaResponse.DetalleResponse();
                dr.setIdProducto(d.getProducto().getIdProducto());
                dr.setNombreProducto(d.getProducto().getNombre());
                dr.setCantidad(d.getCantidad());
                dr.setPrecioUnitario(d.getPrecioUnitario());
                dr.setSubtotal(d.getPrecioUnitario()
                        .multiply(java.math.BigDecimal.valueOf(d.getCantidad())));
                return dr;
            }).toList());
        }
        return r;
    }
}