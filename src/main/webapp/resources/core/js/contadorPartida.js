document.addEventListener("DOMContentLoaded", () => {
    const datos = document.getElementById("datos-partida");
    const duracionTotalMin = parseInt(datos.dataset.duracionMinutos, 10);
    const inicio = new Date(datos.dataset.inicio);

    let ahora = new Date();
    let tiempoRestanteMs = duracionTotalMin * 60000 - (ahora - inicio);

    function actualizarContador() {
        ahora = new Date();
        tiempoRestanteMs = duracionTotalMin * 60000 - (ahora - inicio);

        const contador = document.getElementById("contador-tiempo");
        if (contador) {
            const minutos = Math.floor(tiempoRestanteMs / 60000);
            const segundos = Math.floor((tiempoRestanteMs % 60000) / 1000);
            contador.textContent = `${minutos}:${segundos.toString().padStart(2, "0")}`;
        }

        if (tiempoRestanteMs <= 0) {
            clearInterval(intervalo);
            window.location.href = "/spring/partida/finalizarPartida";
        }
    }

    actualizarContador();
    const intervalo = setInterval(actualizarContador, 1000);
});
