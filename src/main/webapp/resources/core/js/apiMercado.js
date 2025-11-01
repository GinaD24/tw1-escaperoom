
    const mercadoUrl = '/spring/mercado';

    fetch(mercadoUrl)
        .then(response => response.json())
        .then(data => {
            const initPoint = data.init_point;
            document.getElementById('checkout-btn').addEventListener('click', function() {
                window.location.href = initPoint;
            });
        })
        .catch(error => console.error("Error al obtener la preferencia:", error));