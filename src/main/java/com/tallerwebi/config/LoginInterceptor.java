package com.tallerwebi.config;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Si Spring no excluyó esta URL, verificamos la sesión.
        // Solo aplica a las URLs que el registry NO excluyó.

        if (request.getSession().getAttribute("id_usuario") == null) {
            // No hay sesión, redirigimos al login
            // Usamos request.getContextPath() para manejar el /spring si lo tuvieras
            response.sendRedirect(request.getContextPath() + "/login");
            return false; // Detiene la ejecución
        }

        return true; // Continuar
    }
}