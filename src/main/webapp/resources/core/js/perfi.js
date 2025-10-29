var fileUpload = document.getElementById("fileUpload");
var previewContainer = document.getElementById("previewContainer");
var inputFile = document.getElementById("imagen");
var preview = document.getElementById("preview");
var uploadContent = document.getElementById("uploadContent"); // Nota: este no existe en editar-perfil
var botonCambiarFoto = document.getElementById("botonCambiarFoto");


// 1. Condicionar la ejecución para evitar errores si los elementos no están
if (previewContainer && preview) {
    previewContainer.style.display = "block";

    // 2. CORRECCIÓN CLAVE: Esta lógica solo se debe aplicar si NO HAY URL 
    // y quieres mostrar un placeholder. Si Thymeleaf ya le puso una URL
    // válida, no debemos borrarla.

    // Comprobamos si la imagen cargada por Thymeleaf es la de por defecto
    // o si realmente está vacía (suele ser la URL completa después de resolución).
    // Si la URL es válida, no hacemos nada, la mostramos con opacidad normal.
    // Si la URL es la imagen por defecto o vacía:

    // Si la imagen es muy pequeña (placeholder), puedes aplicar el estilo, pero 
    // EVITA PONER preview.src = "" si la foto de Thymeleaf ya se cargó.

    // Si el 'uploadContent' NO existe en 'editar-perfil.html', elimínalo
    // de las variables y del código de abajo.

    // La foto del gatito SÍ se ve en editar perfil, por lo que esta lógica
    // está funcionando bien allí. Si el problema es que la foto se ve, 
    // pero la opacidad es baja, debes cambiar la condición.

    // Si necesitas aplicar los estilos de "foto vacía" solo cuando la URL es la por defecto
    const isDefaultPhoto = preview.src.endsWith('/img/perfil-default.png');

    if (isDefaultPhoto) {
        // La siguiente línea DEBE ELIMINARSE para evitar borrar la URL válida:
        // preview.src = ""; 
        preview.style.backgroundColor = "#ffff00";
        preview.style.objectFit = "contain";
        preview.style.opacity = "0.3";
    }

    // Lógica para el cambio de archivo (que está bien):
    inputFile.addEventListener('change', (event) => {
        var file = event.target.files[0];
        if (file) {
            const url = URL.createObjectURL(file);
            preview.src = url;
            // uploadContent.style.display = "none"; // Eliminar si no existe en editar-perfil
            previewContainer.style.display = "block";
            preview.style.opacity = "1"; // volver a opaco al tener imagen
        }
    });

    botonCambiarFoto.addEventListener('click', () => {
        inputFile.click();
    });
}