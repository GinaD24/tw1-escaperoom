document.addEventListener("DOMContentLoaded", function() {
    const btnPista = document.getElementById("btnPista");
    const animacionPuntos = document.getElementById("animacion-puntos");
    const valorPuntaje = document.getElementById("valor-puntaje");

    if (btnPista) {
        btnPista.addEventListener("click", function() {
            const idAcertijo = this.dataset.acertijo;

            fetch(`/spring/partida/acertijo/${idAcertijo}/pista`)
                .then(response => response.text())
                .then(text => {
                    const pistaDiv = document.getElementById("pista");
                    pistaDiv.innerText = text;

                    if (text.trim() !== "Ya no quedan pistas.") {
                        mostrarAnimacionPuntos();
                        actualizarPuntaje(-25);
                    }
                });
        });
    }

    function mostrarAnimacionPuntos() {
        animacionPuntos.textContent = "-25";
        animacionPuntos.classList.add("mostrar");

        setTimeout(() => {
            animacionPuntos.classList.remove("mostrar");
        }, 2000);
    }

    function actualizarPuntaje(valor) {
        let puntajeActual = parseInt(valorPuntaje.textContent);
        let nuevoPuntaje = Math.max(0, puntajeActual + valor);
        valorPuntaje.textContent = nuevoPuntaje;
    }
});


document.addEventListener("DOMContentLoaded", () => {
    const btnSalir = document.getElementById("btnSalir");
    const modal = document.getElementById("modalSalir");
    const btnCancelar = document.getElementById("cancelarSalir");
    const btnConfirmar = document.getElementById("confirmarSalir");

    btnSalir.addEventListener("click", () => {
        modal.style.display = "flex";
    });

    btnCancelar.addEventListener("click", () => {
        modal.style.display = "none";
    });

    btnConfirmar.addEventListener("click", () => {
        window.location.href = "/spring/partida/finalizarPartida";
    });
});