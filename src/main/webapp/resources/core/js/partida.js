document.addEventListener("DOMContentLoaded", function() {
    const btnPista = document.getElementById("btnPista");
    const animacionPuntos = document.getElementById("animacion-puntos");
    const valorPuntaje = document.getElementById("valor-puntaje");

    if (btnPista) {
        btnPista.addEventListener("click", function() {

            // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
            // 1. Usamos backticks (`) para que sea un string válido.
            // 2. Usamos la URL correcta del controlador (sin el ID).
            fetch(`/spring/partida/pista`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error al obtener la pista');
                    }
                    return response.text();
                })
                .then(text => {
                    const pistaDiv = document.getElementById("pista");
                    pistaDiv.innerHTML = `<p class="pista-texto">${text}</p>`; // Usar innerHTML

                    if (text.trim() !== "Ya no quedan pistas.") {
                        mostrarAnimacionPuntos();
                        actualizarPuntaje(-25);
                    } else {
                        // Deshabilitamos el botón si no hay más pistas
                        btnPista.disabled = true;
                    }
                })
                .catch(error => {
                     console.error('Error:', error);
                     const pistaDiv = document.getElementById("pista");
                     pistaDiv.innerHTML = `<p class="text-danger">No se pudo cargar la pista.</p>`;
                });
        });
    }

    function mostrarAnimacionPuntos() {
        if (!animacionPuntos) return; // Chequeo de seguridad
        animacionPuntos.textContent = "-25";
        animacionPuntos.classList.add("mostrar");

        setTimeout(() => {
            animacionPuntos.classList.remove("mostrar");
        }, 2000);
    }

    function actualizarPuntaje(valor) {
        if (!valorPuntaje) return; // Chequeo de seguridad
        let puntajeActual = parseInt(valorPuntaje.textContent);
        let nuevoPuntaje = Math.max(0, puntajeActual + valor);
        valorPuntaje.textContent = nuevoPuntaje;
    }
});


document.addEventListener("DOMContentLoaded", () => {
    // Este código (el del botón Salir) estaba bien,
    // pero no se ejecutaba por el error de sintaxis de arriba.
    const btnSalir = document.getElementById("btnSalir");
    const modal = document.getElementById("modalSalir");
    const btnCancelar = document.getElementById("cancelarSalir");
    const btnConfirmar = document.getElementById("confirmarSalir");

    if (btnSalir) {
        btnSalir.addEventListener("click", () => {
            if (modal) modal.style.display = "flex";
        });
    }

    if (btnCancelar) {
        btnCancelar.addEventListener("click", () => {
            if (modal) modal.style.display = "none";
        });
    }

    if (btnConfirmar) {
        btnConfirmar.addEventListener("click", () => {
            window.location.href = "/spring/partida/finalizarPartida";
        });
    }
});