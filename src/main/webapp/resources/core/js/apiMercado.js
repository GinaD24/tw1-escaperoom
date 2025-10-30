// Obtiene todos los botones con la clase 'checkout-btn'
const checkoutButtons = document.querySelectorAll('.checkout-btn');
const mercadoUrlBase = '/spring/mercado/preferencia/'; // Usaremos el endpoint con ID

checkoutButtons.forEach(button => {
    const salaId = button.getAttribute('data-id-sala');
    const mercadoUrl = mercadoUrlBase + salaId;

    // Desactivamos el botón hasta que se cargue el init_point
    button.disabled = true;

    fetch(mercadoUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor al crear preferencia');
            }
            return response.json();
        })
        .then(data => {
            const initPoint = data.init_point;
            // Habilitamos el botón después de obtener el init_point
            button.disabled = false;

            // Al hacer clic, redirigimos al init_point
            button.addEventListener('click', function() {
                window.location.href = initPoint;
            });
        })
        .catch(error => {
            console.error(`Error al obtener la preferencia para sala ${salaId}:`, error);
            // Opcional: mostrar un mensaje de error en el botón.
            button.textContent = "Error de pago";
        });
});