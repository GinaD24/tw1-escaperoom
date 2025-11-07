document.addEventListener("DOMContentLoaded", function() {
    const btnPista = document.getElementById("btnPista");
    const animacionPuntos = document.getElementById("animacion-puntos");
    const valorPuntaje = document.getElementById("valor-puntaje");

    if (btnPista) {
        btnPista.addEventListener("click", function() {


            fetch(`/spring/partida/pista`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error al obtener la pista');
                    }
                    return response.text();
                })
                .then(text => {
                    const pistaDiv = document.getElementById("pista");
                    pistaDiv.innerHTML = `<p class="pista-texto">${text}</p>`;

                    if (text.trim() !== "Ya no quedan pistas.") {
                        mostrarAnimacionPuntos();
                        actualizarPuntaje(-25);
                    } else {

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
        if (!animacionPuntos) return;
        animacionPuntos.textContent = "-25";
        animacionPuntos.classList.add("mostrar");

        setTimeout(() => {
            animacionPuntos.classList.remove("mostrar");
        }, 2000);
    }

    function actualizarPuntaje(valor) {
        if (!valorPuntaje) return;
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

document.addEventListener("DOMContentLoaded", () => {
    const zonaSecreta = document.getElementById("zona-secreta");
    const modalBonus = document.getElementById("modalBonus");
    const cerrarBonus = document.getElementById("cerrarBonus");
    const enviarBonus = document.getElementById("enviarBonus");
    const resultadoBonus = document.getElementById("resultadoBonus");
    const respuestaBonus = document.getElementById("respuestaBonus");

    if (zonaSecreta) {
        zonaSecreta.addEventListener("click", () => {
            modalBonus.style.display = "block";
        });
    }

    cerrarBonus.addEventListener("click", () => {
        modalBonus.style.display = "none";
        respuestaBonus.value = "";
        resultadoBonus.textContent = "";
    });

    enviarBonus.addEventListener("click", () => {
        const respuesta = respuestaBonus.value.trim().toLowerCase();

        // Acá ponés la lógica del acertijo bonus
        if (respuesta === "piano") {
            resultadoBonus.textContent = "¡Correcto! Ganaste 50 puntos extra 🎉";
            resultadoBonus.style.color = "lime";
            setTimeout(() => {
                modalBonus.style.display = "none";
            }, 2000);
        } else {
            resultadoBonus.textContent = "Respuesta incorrecta...";
            resultadoBonus.style.color = "red";
        }
    });
});
