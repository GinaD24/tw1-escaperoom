document.addEventListener('DOMContentLoaded', () => {
    const botones = Array.from(document.querySelectorAll('.boton-secuencia'));
    const respuestaInput = document.getElementById('respuestaSecuencia');
    let secuenciaJugador = [];
    let secuenciaCorrecta = botones
        .sort((a,b) => a.dataset.orden - b.dataset.orden)
        .map(b => b.dataset.id);

    let permitirClic = false; // para saber si el jugador puede clicar
    let secuenciaMostrada = false; // para evitar que se muestre más de una vez

    // Botón para mostrar la secuencia
    const btnMostrar = document.getElementById('btnMostrarSecuencia');

    // Función para “iluminar” un botón
    const iluminar = (boton) => {
        boton.style.opacity = 1;
        setTimeout(() => boton.style.opacity = 0.6, 500);
    };

    // Mostrar secuencia automática
    const mostrarSecuencia = async () => {
        if (secuenciaMostrada) return; // solo una vez
        secuenciaMostrada = true;

        for (let id of secuenciaCorrecta) {
            let boton = botones.find(b => b.dataset.id === id);
            iluminar(boton);
            await new Promise(r => setTimeout(r, 700)); // espera entre botones
        }
        permitirClic = true; // ahora el jugador puede clicar
    };

    // Evento del botón “Mostrar Secuencia”

    btnMostrar.addEventListener('click', () => {
        mostrarSecuencia().then(() => {
            btnMostrar.disabled = true;
            btnMostrar.textContent = 'Secuencia mostrada';
        });
    });


    // Manejar clics del jugador
    botones.forEach(boton => {
        boton.addEventListener('click', () => {
            if (!permitirClic) return;

            boton.style.opacity = 1;
            setTimeout(() => boton.style.opacity = 0.6, 300);

            secuenciaJugador.push(boton.dataset.id);
            respuestaInput.value = secuenciaJugador.join(',');
        });
    });
});

