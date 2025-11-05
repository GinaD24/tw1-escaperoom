const checkoutButtons = document.querySelectorAll('.checkout-btn');
const mercadoUrlBase = '/spring/mercado/preferencia/';

checkoutButtons.forEach(button => {
    const salaId = button.getAttribute('data-id-sala');
    const mercadoUrl = mercadoUrlBase + salaId;

    button.disabled = true;

    fetch(mercadoUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor al crear preferencia');
            }
            return response.json();
        })
        .then(data => {
            const initPoint = data.sandbox_init_point;
            button.disabled = false;

            button.addEventListener('click', function() {
                window.location.href = initPoint;
            });
        })
        .catch(error => {
            console.error(`Error al obtener la preferencia para sala ${salaId}:`, error);
            button.textContent = "Error de pago";
        });
});