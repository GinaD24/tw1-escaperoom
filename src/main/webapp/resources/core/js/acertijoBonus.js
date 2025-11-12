document.addEventListener("DOMContentLoaded", function () {
    const zonaSecreta = document.getElementById("zona-secreta");
    const modalBonus = document.getElementById("modalBonus");
    const cerrarBonus = document.getElementById("cerrarBonus");
    const formBonus = modalBonus.querySelector("form");
    const inputRespuesta = document.getElementById("input-respuesta");
    const contenedorImagenes = formBonus.querySelector("div.pos-relative");
    const popupExito = document.getElementById("popup-exito");
    const animacionPuntos = document.getElementById("animacion-puntos");
    const valorPuntaje = document.getElementById("valor-puntaje");

    // Crear mensaje de error debajo del input
    const mensajeError = document.createElement("p");
    mensajeError.style.color = "red";
    mensajeError.style.marginTop = "10px";
    mensajeError.style.textAlign = "center";
    mensajeError.style.display = "none";
    formBonus.appendChild(mensajeError);

    // 🟣 Cuando el usuario encuentra la zona secreta
    if (zonaSecreta) {
        zonaSecreta.addEventListener("click", function () {
            fetch(`/spring/partida/bonus/`)
                .then(res => {
                    if (!res.ok || res.status === 204) return null;
                    return res.json();
                })
                .then(data => {
                    if (!data) return;
                    if (data.error) {
                        alert(data.error);
                        return;
                    }

                    contenedorImagenes.innerHTML = "";

                    data.imagenes.forEach(nombreArchivo => {
                        const img = document.createElement("img");
                        img.src = `/spring/img/acertijo/${nombreArchivo}`;
                        img.classList.add("img-fluid", "mb-3");
                        contenedorImagenes.appendChild(img);
                    });

                    const descripcion = document.createElement("p");
                    descripcion.textContent = data.descripcion;
                    descripcion.classList.add("mb-3", "text-center");
                    contenedorImagenes.prepend(descripcion);

                    mensajeError.style.display = "none";
                    modalBonus.style.display = "flex";
                })
                .catch(err => console.error("Error al obtener acertijo bonus:", err));
        });
    }

    // 🔴 Cerrar modal
    if (cerrarBonus) {
        cerrarBonus.addEventListener("click", function (e) {
            e.preventDefault();
            modalBonus.style.display = "none";
        });
    }

    // 🟢 Enviar respuesta del bonus
    formBonus.addEventListener("submit", function (e) {
        e.preventDefault();
        mensajeError.style.display = "none";

        const respuesta = inputRespuesta.value.trim();
        if (!respuesta) {
            mensajeError.textContent = "Por favor, escribir una respuesta.";
            mensajeError.style.display = "block";
            return;
        }

        const actionUrl = formBonus.getAttribute("action");

        fetch(actionUrl, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ respuesta })
        })
            .then(res => res.text())
            .then(data => {
                if (data === "ok") {
                    // ✅ Correcto → cerrar modal + mostrar popup
                    modalBonus.style.display = "none";
                    zonaSecreta.style.pointerEvents = "none";
                    inputRespuesta.value = "";

                    popupExito.style.display = "block";
                    setTimeout(() => {
                        popupExito.style.display = "none";
                    }, 3000);

                    mostrarAnimacionPuntos();
                    actualizarPuntaje(+50);
                } else if (data === "error:vacio") {
                    mensajeError.textContent = "No se puede enviar una respuesta vacía.";
                    mensajeError.style.display = "block";
                } else if (data === "error:incorrecta") {
                    mensajeError.textContent = "Incorrecto. Intente nuevamente.";
                    mensajeError.style.display = "block";
                } else {
                    mensajeError.textContent = "Error desconocido: " + data;
                    mensajeError.style.display = "block";
                }
            })
            .catch(err => console.error("Error al validar el bonus:", err));
    });

    function mostrarAnimacionPuntos() {
        if (!animacionPuntos) return;
        animacionPuntos.textContent = "+50";
        animacionPuntos.style.color = "green";
        animacionPuntos.classList.add("mostrar");

        setTimeout(() => {
            animacionPuntos.classList.remove("mostrar");
        }, 2000);
    }

    function actualizarPuntaje(valor) {
        if (!valorPuntaje) return;
        let puntajeActual = parseInt(valorPuntaje.textContent);
        let nuevoPuntaje = Math.max(0, puntajeActual + valor);
        valorPuntaje.textContent = nuevoPuntaje;
    }

});


