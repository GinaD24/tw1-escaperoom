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
