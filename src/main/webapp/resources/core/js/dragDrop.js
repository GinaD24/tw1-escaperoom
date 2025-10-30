document.addEventListener('DOMContentLoaded', () => {
    const dragItems = document.querySelectorAll('.drag-item');
    const dropSlots = document.querySelectorAll('.drop-slot');
    const dragContainer = document.getElementById('drag-items'); // Contenedor inicial
    const inputRespuesta = document.getElementById('respuestaDragDrop');

    dragItems.forEach(item => {
        item.addEventListener('dragstart', e => {
            e.dataTransfer.setData('text/plain', item.dataset.id);
        });
    });

    const allDropAreas = [...dropSlots, dragContainer]; // Todas las áreas donde se puede soltar

    allDropAreas.forEach(area => {
        area.addEventListener('dragover', e => e.preventDefault());

        area.addEventListener('drop', e => {
            e.preventDefault();
            const id = e.dataTransfer.getData('text/plain');

            // --- ESTA LÍNEA ES LA CORRECCIÓN CLAVE ---
            // Ahora busca el elemento en CUALQUIER PARTE de la página, no solo en el inicio.
            const dragged = document.querySelector(`.drag-item[data-id='${id}']`);

            if (dragged) {
                area.appendChild(dragged); // Mueve el elemento al nuevo contenedor
            }

            // Esta parte para generar la respuesta sigue igual y funciona bien.
            const itemsEnSlots = document.querySelectorAll('#drop-container .drag-item');
            const estado = Array.from(itemsEnSlots).map(item => {
                const itemId = item.dataset.id;
                const categoriaSlot = item.parentElement.dataset.categoria;
                return `${itemId}:${categoriaSlot}`;
            });

            inputRespuesta.value = estado.join(',');
        });
    });
});