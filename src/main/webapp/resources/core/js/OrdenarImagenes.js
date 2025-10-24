let seleccionadas = [];

document.querySelectorAll('.img-click').forEach(img => {
    img.addEventListener('click', function() {
        const imgId = this.getAttribute('data-id');
        const contenedor = this.parentElement;
        const numeroSpan = contenedor.querySelector('.numero-seleccion');
        numeroSpan.style.display = 'inline';

        if (seleccionadas.includes(imgId)) {
            seleccionadas = seleccionadas.filter(id => id !== imgId);
            numeroSpan.textContent = '';
            this.classList.remove('seleccionada');
        } else {

            seleccionadas.push(imgId);
            this.classList.add('seleccionada');
        }


        document.querySelectorAll('.img-click').forEach(im => {
            const id = im.getAttribute('data-id');
            const span = im.parentElement.querySelector('.numero-seleccion');
            if (seleccionadas.includes(id)) {
                span.textContent = seleccionadas.indexOf(id) + 1;
            } else {
                span.textContent = '';
                span.style.display = 'none';
            }
        });

        const contenedorPadre = document.querySelector('#imagenes-container');
        seleccionadas.forEach(id => {
            const img = document.querySelector(`.img-click[data-id='${id}']`);
            contenedorPadre.appendChild(img.parentElement);
        });


        document.getElementById('ordenImagenes').value = seleccionadas.join(',');
    });
});