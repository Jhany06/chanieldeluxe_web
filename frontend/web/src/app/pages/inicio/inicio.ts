import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TitleCasePipe, DecimalPipe } from '@angular/common';
import { CatalogoService, Producto } from '../../services/catalogo.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink, TitleCasePipe, DecimalPipe],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio implements OnInit {
  private catalogoSvc = inject(CatalogoService);
  auth = inject(AuthService);
  destacados = signal<Producto[]>([]);

  ngOnInit() {
    this.catalogoSvc.listar().subscribe({
      next: items => this.destacados.set(items.slice(0, 4)),
      error: () => this.destacados.set([])
    });
  }

  imagenUrl(p: Producto) {
    return p.imagenUrl || `https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400&q=80`;
  }
}
