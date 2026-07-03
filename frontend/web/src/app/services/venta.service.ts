import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Venta {
  idVenta: number;
  idUsuario: number;
  nombreUsuario: string;
  fechaVenta: string;
  total: number;
  estado: string;
  metodoPago: string;
  direccionEnvio: string;
  ciudadEnvio: string;
  detalles: any[];
}

@Injectable({ providedIn: 'root' })
export class VentaService {
  private apiUrl = 'https://chanieldeluxe-web.onrender.com/api/ventas';
  constructor(private http: HttpClient) {}
  crear(data: any): Observable<Venta> { return this.http.post<Venta>(this.apiUrl, data); }
  misPedidos(idUsuario: number): Observable<Venta[]> { return this.http.get<Venta[]>(`${this.apiUrl}/usuario/${idUsuario}`); }
  listarTodas(): Observable<Venta[]> { return this.http.get<Venta[]>(this.apiUrl); }
  actualizarEstado(idVenta: number, estado: string): Observable<Venta> {
    return this.http.put<Venta>(`${this.apiUrl}/${idVenta}/estado`, null, { params: { estado } });
  }
}
