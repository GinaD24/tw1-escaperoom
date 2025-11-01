
    var fileUpload = document.getElementById("fileUpload");
   var previewContainer = document.getElementById("previewContainer");
   var inputFile = document.getElementById("imagen");
   var preview = document.getElementById("preview");
   var uploadContent = document.getElementById("uploadContent");
   var botonCambiarFoto = document.getElementById("botonCambiarFoto");

   previewContainer.style.display = "block";

   if (!preview.src || preview.src === "") {
       preview.src = "";
       preview.style.backgroundColor = "#ffff00";
       preview.style.objectFit = "contain";
       preview.style.opacity = "0.3";
   }

   inputFile.addEventListener('change', (event) => {
       var file = event.target.files[0];
       if (file) {
           const url = URL.createObjectURL(file);
           preview.src = url;
           uploadContent.style.display = "none";
           previewContainer.style.display = "block";
           preview.style.opacity = "1";
       }
   });

   botonCambiarFoto.addEventListener('click', () => {
       inputFile.click();
   });