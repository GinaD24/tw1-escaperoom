document.addEventListener('DOMContentLoaded', () => {
    const selectFiltroSalas = document.getElementById('filtroSalas');
    const btnFiltrarRanking = document.getElementById('btn-filtrarRanking');

    const actualizarEstadoBoton = () => {

        if (selectFiltroSalas.options.length > 1) {
            btnFiltrarRanking.disabled = false;
        } else {
            btnFiltrarRanking.disabled = true;
        }
    };

    actualizarEstadoBoton();

});