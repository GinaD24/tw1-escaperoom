document.addEventListener("DOMContentLoaded", function() {
    const btnPista = document.getElementById("btnPista");
    if (btnPista) {
        btnPista.addEventListener("click", function() {
            const idAcertijo = this.dataset.acertijo;

            fetch(`/spring/partida/acertijo/${idAcertijo}/pista`)
                .then(response => response.text())
                .then(text => {
                    document.getElementById("pista").innerText = text;
                });
        });
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