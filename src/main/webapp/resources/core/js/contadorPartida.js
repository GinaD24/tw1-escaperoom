document.addEventListener("DOMContentLoaded", () => {
    const datos = document.getElementById("datos-partida");
    const duracionTotalMin = parseInt(datos.dataset.duracionMinutos, 10); // duración en minutos
    const inicio = new Date(datos.dataset.inicio); // fecha de inicio de la partida

    // Calcular tiempo restante en milisegundos
    const ahora = new Date();
    let tiempoRestanteMs = duracionTotalMin * 60000 - (ahora - inicio);

    // Si ya se terminó el tiempo, finalizar la partida
    if (tiempoRestanteMs <= 0) {
        finalizarPorTiempo();
        return;
    }

    // Función para actualizar el contador
    function actualizarContador(ms) {
        const minutos = Math.floor(ms / 60000);
        const segundos = Math.floor((ms % 60000) / 1000);
        const contador = document.getElementById("contador-tiempo");
        if (contador) {
            contador.textContent = `${minutos}:${segundos.toString().padStart(2, "0")}`;
        }
    }

    // Mostrar inmediatamente al cargar
    actualizarContador(tiempoRestanteMs);

    // Intervalo para actualizar cada segundo
    const intervalo = setInterval(() => {
        tiempoRestanteMs -= 1000;

        if (tiempoRestanteMs <= 0) {
            clearInterval(intervalo);
            finalizarPorTiempo();
            return;
        }

        actualizarContador(tiempoRestanteMs);
    }, 1000);

    // Función que finaliza la partida por tiempo
    function finalizarPorTiempo() {
        fetch("/spring/partida/validarTiempo")
            .then(() => window.location.href = "/spring/partida/finalizarPartida");
    }
});
