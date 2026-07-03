import { Component, inject, OnInit, signal } from '@angular/core';
import { VentaService, Venta } from '../../services/venta.service';
import { AuthService } from '../../services/auth.service';
import { DecimalPipe, DatePipe, TitleCasePipe } from '@angular/common';

@Component({
  selector: 'app-mis-pedidos',
  imports: [DecimalPipe, DatePipe, TitleCasePipe],
  templateUrl: './mis-pedidos.html',
  styleUrl: './mis-pedidos.css'
})
export class MisPedidos implements OnInit {
  private ventaSvc = inject(VentaService);
  auth = inject(AuthService);
  pedidos = signal<Venta[]>([]);
  loading = signal(true);
  pedidoDetalle = signal<Venta | null>(null);

  ngOnInit() {
    const user = this.auth.currentUser();
    if (!user) return;
    this.ventaSvc.misPedidos(user.idUsuario).subscribe({
      next: p => { this.pedidos.set(p); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  estadoBadge(estado: string) {
    const map: any = { pendiente: 'badge-warn', procesado: 'badge-success', enviado: 'badge-success', entregado: 'badge-success', cancelado: 'badge-danger' };
    return map[estado] || 'badge-warn';
  }

  verDetalle(p: Venta) { this.pedidoDetalle.set(p); }
  cerrarDetalle() { this.pedidoDetalle.set(null); }
}
