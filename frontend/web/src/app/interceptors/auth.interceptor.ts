import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const usuario = localStorage.getItem('usuario');
    const token = usuario ? JSON.parse(usuario).token : null;

    if (token) {
        const authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
        });
        return next(authReq);
    }
    return next(req);
};