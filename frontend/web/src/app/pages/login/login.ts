import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  auth = inject(AuthService);
  router = inject(Router);
  error = signal('');
  loading = signal(false);

  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    contrasena: new FormControl('', [Validators.required, Validators.minLength(6)])
  });

  get email() { return this.form.get('email')!; }
  get contrasena() { return this.form.get('contrasena')!; }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true);
    this.error.set('');
    const { email, contrasena } = this.form.value;
    this.auth.login(email!, contrasena!).subscribe({
      next: () => { this.loading.set(false); this.router.navigate(['/']); },
      error: () => { this.loading.set(false); this.error.set('Email o contraseña incorrectos.'); }
    });
  }
}