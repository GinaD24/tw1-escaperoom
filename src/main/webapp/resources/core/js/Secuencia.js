document.addEventListener('DOMContentLoaded', () => {
    const botones = Array.from(document.querySelectorAll('.boton-secuencia'));
    const respuestaInput = document.getElementById('respuestaSecuencia');
    let secuenciaJugador = [];
    let secuenciaCorrecta = botones
        .sort((a,b) => a.dataset.orden - b.dataset.orden)
        .map(b => b.dataset.id);

    let permitirClic = false;
    let secuenciaMostrada = false;


    const btnMostrar = document.getElementById('btnMostrarSecuencia');


    const iluminar = (boton) => {
        boton.style.opacity = 3;
        setTimeout(() => boton.style.opacity = 0.6, 500);
    };


    const mostrarSecuencia = async () => {
        if (secuenciaMostrada) return;
        secuenciaMostrada = true;

        for (let id of secuenciaCorrecta) {
            let boton = botones.find(b => b.dataset.id === id);
            iluminar(boton);
            await new Promise(r => setTimeout(r, 700));
        }
        permitirClic = true;
    };



    btnMostrar.addEventListener('click', () => {
        mostrarSecuencia().then(() => {
            btnMostrar.disabled = true;
            btnMostrar.textContent = 'Secuencia mostrada';
        });
    });



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

