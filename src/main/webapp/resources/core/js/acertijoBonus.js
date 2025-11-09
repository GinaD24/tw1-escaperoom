document.addEventListener("DOMContentLoaded", function () {
    const zonaSecreta = document.getElementById("zona-secreta");
    const modalBonus = document.getElementById("modalBonus");
    const cerrarBonus = document.getElementById("cerrarBonus");
    const formBonus = modalBonus.querySelector("form");
    const inputRespuesta = document.getElementById("input-respuesta");
    const contenedorImagenes = formBonus.querySelector("div.pos-relative");

    // 🟣 Cuando el usuario encuentra la zona secreta
    if (zonaSecreta) {
        zonaSecreta.addEventListener("click", function () {
            fetch(`/spring/partida/bonus/`)
                .then(res => {
                    if (!res.ok || res.status === 204) {
                        // Nada que hacer, el backend no devolvió contenido
                        return null;
                    }
                    return res.json();
                })
                .then(data => {
                    if (!data) return; // 👈 no hacer nada
                    if (data.error) {
                        alert(data.error);
                        return;
                    }

                    // Limpiar imágenes anteriores (si hubiera)
                    contenedorImagenes.innerHTML = "";

                    // Mostrar las imágenes del acertijo bonus
                    data.imagenes.forEach(nombreArchivo => {
                        const img = document.createElement("img");
                        img.src = `/spring/img/acertijo/${nombreArchivo}`;
                        img.classList.add("img-fluid", "mb-3");
                        contenedorImagenes.appendChild(img);
                    });

                    // Mostrar la descripción del acertijo
                    const descripcion = document.createElement("p");
                    descripcion.textContent = data.descripcion;
                    descripcion.classList.add("mb-3", "text-center", "fw-bold");
                    contenedorImagenes.prepend(descripcion);

                    // Mostrar el modal
                    modalBonus.style.display = "flex";
                })
                .catch(err => {
                    console.error("Error al obtener acertijo bonus:", err);
                });
        });
    }

    // 🔴 Botón para cerrar el modal
    if (cerrarBonus) {
        cerrarBonus.addEventListener("click", function (e) {
            e.preventDefault();
            modalBonus.style.display = "none";
        });
    }

    // 🟢 Enviar respuesta del bonus
    formBonus.addEventListener("submit", function (e) {
        e.preventDefault();

        const respuesta = inputRespuesta.value.trim();
        if (!respuesta) {
            alert("Por favor, escribí una respuesta.");
            return;
        }

        // Obtener los datos dinámicos desde el atributo "th:action" resuelto
        const actionUrl = formBonus.getAttribute("action");

        fetch(actionUrl, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ respuesta: respuesta })
        })
            .then(res => res.text())
            .then(data => {
                if (data === "ok") {
                    alert("✅ ¡Correcto! Ganaste puntos bonus.");
                    modalBonus.style.display = "none";
                    zonaSecreta.style.pointerEvents = "none";
                    inputRespuesta.value = "";
                } else if (data === "error:vacio") {
                    alert("⚠️ No podés enviar una respuesta vacía.");
                } else if (data === "error:incorrecta") {
                    alert("❌ Respuesta incorrecta, intentá otra vez.");
                } else {
                    alert("Error desconocido: " + data);
                }
            })
            .catch(err => {
                console.error("Error al validar el bonus:", err);
            });
    });
});
