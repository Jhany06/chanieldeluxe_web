import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CarritoService } from '../../services/carrito.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  auth = inject(AuthService);
  carrito = inject(CarritoService);
  router = inject(Router);
  menuOpen = false;

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
  toggleMenu() { this.menuOpen = !this.menuOpen; }
}
