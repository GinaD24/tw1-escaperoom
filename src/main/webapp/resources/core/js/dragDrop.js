const dragItems = document.querySelectorAll('#drag-items .drag-item');
const dropSlots = document.querySelectorAll('#drop-container .drop-slot');
const inputRespuesta = document.getElementById('respuestaDragDrop');

dragItems.forEach(item => {
    item.addEventListener('dragstart', e => {
        e.dataTransfer.setData('text/plain', item.dataset.id);
    });
});

dropSlots.forEach(slot => {
    slot.addEventListener('dragover', e => e.preventDefault());

    slot.addEventListener('drop', e => {
        e.preventDefault();
        const id = e.dataTransfer.getData('text/plain');
        const dragged = document.querySelector(`#drag-items .drag-item[data-id='${id}']`);
        if (dragged) {
            slot.appendChild(dragged); // Ya no removemos nada del slot
        }

        // Guardar el estado: id del item + slot donde cayó
        const estado = Array.from(dropSlots).map(s => {
            return Array.from(s.querySelectorAll('.drag-item'))
                .map(item => item.dataset.id + ':' + s.dataset.categoria)
                .join(';'); // varias imágenes separadas por ';' dentro del slot
        }).filter(x => x !== '');
        inputRespuesta.value = estado.join(',');
    });
});

