import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CatalogoService, Producto } from '../../services/catalogo.service';
import { CarritoService } from '../../services/carrito.service';
import { AuthService } from '../../services/auth.service';
import { TitleCasePipe, NgClass, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-catalogo',
  imports: [FormsModule, RouterLink, TitleCasePipe, NgClass, DecimalPipe],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css'
})
export class Catalogo implements OnInit {
  private catalogoSvc = inject(CatalogoService);
  private carritoSvc = inject(CarritoService);
  auth = inject(AuthService);

  productos = signal<Producto[]>([]);
  loading = signal(true);
  toast = signal('');
  toastType = signal('success');

  filtroCategoria = '';
  filtroTalla = '';
  busqueda = '';
  ordenarPor = 'nombre';

  categorias = ['vestidos','blusas','pantalones','faldas','chaquetas','abrigos','ropa_interior','pijamas','ropa_deportiva','accesorios','calzado','otros'];
  tallas = ['XS','S','M','L','XL','XXL','UNICO'];

  ngOnInit() {
    this.catalogoSvc.listar().subscribe({
      next: p => { this.productos.set(p); this.loading.set(false); },
      error: () => { this.loading.set(false); }
    });
  }

  get productosFiltrados() {
    let list = this.productos();
    if (this.filtroCategoria) list = list.filter(p => p.categoria === this.filtroCategoria);
    if (this.filtroTalla) list = list.filter(p => p.talla === this.filtroTalla);
    if (this.busqueda) list = list.filter(p => p.nombre.toLowerCase().includes(this.busqueda.toLowerCase()));
    if (this.ordenarPor === 'precio_asc') list = [...list].sort((a,b) => (a.precioOferta??a.precioUnitario)-(b.precioOferta??b.precioUnitario));
    if (this.ordenarPor === 'precio_desc') list = [...list].sort((a,b) => (b.precioOferta??b.precioUnitario)-(a.precioOferta??a.precioUnitario));
    if (this.ordenarPor === 'nombre') list = [...list].sort((a,b) => a.nombre.localeCompare(b.nombre));
    return list;
  }

  limpiarFiltros() { this.filtroCategoria = ''; this.filtroTalla = ''; this.busqueda = ''; this.ordenarPor = 'nombre'; }

  agregarAlCarrito(p: Producto) {
    const user = this.auth.currentUser();
    if (!user) { this.showToast('Inicia sesión para agregar al carrito', 'error'); return; }
    if (p.stockDisponible < 1) { this.showToast('Sin stock disponible', 'error'); return; }
    this.carritoSvc.agregar(user.idUsuario, p.idProducto).subscribe({
      next: () => this.showToast(`"${p.nombre}" agregado al carrito 🛍️`),
      error: () => this.showToast('Error al agregar al carrito', 'error')
    });
  }

  imagenUrl(p: Producto) {
    return p.imagenUrl || `https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400&q=80`;
  }

  showToast(msg: string, type = 'success') {
    this.toast.set(msg);
    this.toastType.set(type);
    setTimeout(() => this.toast.set(''), 3000);
  }
}
