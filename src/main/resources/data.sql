INSERT INTO Usuario( email, password, rol, activo) VALUES( 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Usuario (email,nombre,apellido,password,fechaNacimiento,nombreUsuario,fotoPerfil)
VALUES (
             'marcos.lopez@example.com',
             'Marcos',
             'López',
             'clave123',
             '1998-03-12',
             'marcosl',
             'pruebafoto.png');

INSERT INTO Usuario (email,nombre,apellido,password,fechaNacimiento,nombreUsuario,fotoPerfil, rol)
VALUES (
           'juanP@gmail.com',
           'Juan',
           'Perez',
           'juan123',
           '1998-12-12',
           'juan_1',
           'fotoPerfil_juan.png',
           'USUARIO');


INSERT INTO Sala (nombre, dificultad, escenario, historia, esta_habilitada, duracion, imagenPuerta, imagenSala, cantidadDeEtapas, imagenGanada, imagenPerdida)VALUES
                                                                                          ('La Mansión Misteriosa', 'PRINCIPIANTE', 'Mansion', 'Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.', TRUE, 1,'puerta-mansion.png', 'mansion-misteriosa.png', 5, 'mansion-ganada.png', 'mansion-perdida.png'),
                                                                                          ('El Laboratorio Secreto', 'INTERMEDIO', 'Laboratorio', 'Un científico desaparecido dejo pistas en su laboratorio. ¿Podras descubrir que tramaba?', TRUE, 15, 'puerta-laboratorio.png', 'laboratorio-secreto.png', 8, 'laboratorio-ganado.png', 'mansion-perdido.png'),
                                                                                          ('La Carcel Abandonada', 'AVANZADO', 'Prision', 'Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podras escapar.', TRUE, 20,'puerta-prision.png', 'carcel-abandonada.png', 10, 'carcel-ganada.png', 'carcel-perdida.png');

-- ETAPAS PARA LA SALA 'La Mansión Misteriosa' (id_sala = 1)
INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
                                                             ('Lobby', 1, 'La puerta se ha cerrado a tus espaldas. El aire es denso y el único camino es adelante. Resuelve los enigmas del vestíbulo para avanzar.', 1, 'etapa-lobby.png'),
                                                             ('Biblioteca', 2, 'Has llegado a una biblioteca cubierta de polvo. Entre miles de libros se esconden secretos que ansían ser leídos en voz alta.', 1, 'etapa-biblioteca.png'),
                                                             ('Salón', 3, 'Un gran salón donde aún resuenan las risas y los lamentos del pasado. La música y el silencio guardan las claves para continuar.', 1, 'etapa-salon.png'),
                                                             ('Despacho', 4, 'Este era el refugio del antiguo dueño. Sus objetos personales, mapas y cartas, ocultan las piezas finales de su misterio.', 1, 'etapa-despacho.png'),
                                                             ('Ático', 5, 'El último piso. Entre baúles y objetos olvidados yace el secreto final que te permitirá encontrar la salida de la mansión.', 1, 'etapa-atico.png');

-- ACERTIJOS ETAPA 1: Lobby (id_etapa = 1)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Poseo un rostro sin alma y te devuelvo la mirada sin pestañear. Contengo tu reflejo, pero mi interior es un vacío helado.', 1),
                                                 ('ADIVINANZA','Mis dos manos recorren mi rostro sin cesar, devorando el tiempo que no volverá. Mi voz es un pulso constante que mide tu estadía en este lugar.', 1),
                                                 ('ADIVINANZA','Nací de la luz, pero vivo en la penumbra. Soy tu fiel compañera bajo el sol, pero la oscuridad total me hace desaparecer.', 1);

-- ACERTIJOS ETAPA 2: Biblioteca (id_etapa = 2)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Mis hojas guardan bosques y mis lomos sostienen mundos. Hablo sin voz, tejo historias en silencio para quien se atreva a abrirme.', 2),
                                                 ('ADIVINANZA','Mi cuerpo de cera se consume para darte luz. Lloro lágrimas ardientes mientras mi vida se extingue lentamente en la penumbra.', 2),
                                                 ('ADIVINANZA','Soy un río de noche líquida, nacido para dar cuerpo a los pensamientos. Con mi esencia, las palabras se graban y los secretos perduran en el papel.', 2);

-- ACERTIJOS ETAPA 3: Salón (id_etapa = 3)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Una dentadura de marfil y ébano espera ser tocada. No muerdo, pero si me presionas con arte, liberaré los ecos de una triste melodía.', 3),
                                                 ('ADIVINANZA','Reino en la ausencia de todo sonido. Me puedes sentir en el aire, pero si pronuncias mi nombre, me habrás roto.', 3),
                                                 ('ADIVINANZA','Soy una cascada de cristal congelada en el tiempo. Cuelgo sobre tus cabezas, portador de una luz que ya no existe, reflejando glorias pasadas.', 3);

-- ACERTIJOS ETAPA 4: Despacho (id_etapa = 4)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Mis dientes no mastican y mi cuello no sostiene una cabeza. Fui forjada para guardar secretos y solo me rindo ante la mano correcta.', 4),
                                                 ('ADIVINANZA','No soy grande, pero hago que el mundo pequeño crezca ante tus ojos. Ayudo a leer lo que el tiempo ha borrado y a encontrar lo que se esconde a simple vista.', 4),
                                                 ('ADIVINANZA','No tengo brazos, pero siempre apunto a un lugar. No tengo piernas, pero te ayudo a caminar. Mi aguja siempre tiembla buscando su hogar.', 4);

-- ACERTIJOS ETAPA 5: Ático (id_etapa = 5)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Me poso en todo lo que olvidas, me levanto con la brisa. Soy un testigo silencioso del paso del tiempo.', 5),
                                                 ('ADIVINANZA','Soy un instante congelado, una memoria atrapada en papel. Mi mundo no tiene sonido ni movimiento, solo el eco de una sonrisa que ya no está.', 5),
                                                 ('ADIVINANZA','Soy la promesa al final del pasillo, la luz tras la última puerta. No soy un objeto, sino un destino. Has resuelto todo para encontrarme.', 5);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 1-3 (Etapa 1)
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Busca tu propio reflejo en la sala.', 1, 1);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('ESPEJO', TRUE, 1);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('El tiempo es la clave. Escucha su constante pasar en el vestíbulo.', 1, 2);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('RELOJ', TRUE, 2);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('No puedes tocarme, pero siempre estoy contigo cuando hay una fuente de luz.', 1, 3);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('SOMBRA', TRUE, 3);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 4-6 (Etapa 2)
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('El conocimiento de la biblioteca se guarda entre tapas duras.', 1, 4);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LIBRO', TRUE, 4);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Su llama danza para guiarte, pero su calor también la consume.', 1, 5);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('VELA', TRUE, 5);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Las palabras del dueño necesitan un medio para nacer en el papel.', 1, 6);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('TINTA', TRUE, 6);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 7-9 (Etapa 3)
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('La música es la voz del alma de esta casa. Busca el instrumento más grande.', 1, 7);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('PIANO', TRUE, 7);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Para encontrar la respuesta, no debes hacer ningún ruido.', 1, 8);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('SILENCIO', TRUE, 8);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Mira hacia arriba. Algo brillante y fragmentado pende sobre el centro del salón.', 1, 9);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('CANDELABRO', TRUE, 9);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 10-12 (Etapa 4)
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Se esconde en el cajón más pequeño del escritorio, esperando abrir algo importante.', 1, 10);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LLAVE', TRUE, 10);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los detalles más pequeños son a menudo los más importantes.', 1, 11); -- Pista nueva
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LUPA', TRUE, 11); -- Respuesta nueva

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Incluso en la quietud de esta habitación, siempre busca el norte.', 1, 12);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('BRUJULA', TRUE, 12);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 13-15 (Etapa 5)

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Me acumulo en las superficies y vuelo al ser tocado.', 1, 13);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('POLVO', TRUE, 13);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Un viejo álbum guarda momentos congelados en el tiempo. Sonríen desde el pasado.', 1, 14);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('FOTOGRAFIA', TRUE, 14);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Has resuelto todo, solo queda un último paso: cruzar el umbral.', 1, 15);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('SALIDA', TRUE, 15);

INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
                                                                     ('Entrada', 1, 'La puerta del laboratorio se cierra detrás de ti. Debes reorganizar los componentes del experimento para avanzar.', 2, 'etapa-entrada-laboratorio.png'),
                                                                     ('Sala de Reactivos', 2, 'Botellas y tubos de ensayo llenos de líquidos de colores te rodean. Organízalos correctamente para desbloquear la siguiente puerta.', 2, 'etapa-reactivos-laboratorio.png'),
                                                                     ('Mesa de Experimentos', 3, 'Los instrumentos sobre la mesa están desordenados. Solo al colocarlos en el orden correcto podrás continuar.', 2, 'etapa-mesa-laboratorio.png'),
                                                                     ('Sala de Microscopios', 4, 'Observa las muestras y ordénalas según la secuencia correcta de análisis.', 2, 'etapa-microscopios.png'),
                                                                     ('Almacén de Equipos', 5, 'Hay varios aparatos de laboratorio tirados por todas partes. Organízalos para avanzar.', 2, 'etapa-almacen-laboratorio.png'),
                                                                     ('Cámara de Cultivos', 6, 'Las placas de cultivo contienen diferentes bacterias. Debes ordenarlas según su ciclo de crecimiento.', 2, 'etapa-camara-laboratorio.png'),
                                                                     ('Oficina del Científico', 7, 'El escritorio del científico tiene papeles y herramientas. Colócalos en la secuencia correcta para entender su investigación.', 2, 'etapa-oficina-laboratorio.png'),
                                                                     ('Laboratorio Secreto', 8, 'El corazón del laboratorio. Solo ordenando correctamente todos los elementos del gran experimento podrás acceder al resultado final.', 2, 'etapa-secreto-laboratorio.png');

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                       ('ORDENAR_IMAGEN', 'Observa los frascos de diferentes colores. Organízalos siguiendo el cambio gradual de tonalidad de claro a oscuro.', 6),
                                                       ('DRAG_DROP', 'Clasifica correctamente los materiales de laboratorio en sus categorías correspondientes.', 7),
                                                       ('ADIVINANZA', 'No tengo cuerpo, pero lleno espacios. Soy invisible, pero puedo ser mortal. Nací de reacciones químicas y si escapo, el silencio es mi señal de peligro. ¿Qué soy?', 8);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
    (16, 'img-frasco1.png', 2),
    (16, 'img-frasco2.png', 3),
    (16, 'img-frasco3.png', 1),
    (16, 'img-frasco4.png', 4);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
                                                                         (17, 'Beaker.png', 'Vidrio'),
                                                                         (17, 'Probeta.png', 'Vidrio'),
                                                                         (17, 'Guantes.png', 'Proteccion'),
                                                                         (17, 'Mascarilla.png', 'Proteccion'),
                                                                         (17, 'ReactivoA.png', 'Quimicos'),
                                                                         (17, 'ReactivoB.png', 'Quimicos');

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Algunos extinguen llamas, otros las avivan.', 1, 18);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('GAS', TRUE, 18);

INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(26, 'ORDENAR_IMAGEN', 'En el monitor se ven las imágenes de una muestra biológica. Ordénalas para reconstruir el ciclo de vida completo.', 4);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
(26, 'ciclo-huevo.png', 1),
(26, 'ciclo-oruga.png', 2),
(26, 'ciclo-crisalida.png', 3),
(26, 'ciclo-mariposa.png', 4);

INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(27, 'DRAG_DROP', '¡Hay que reciclar para poder salir! Clasifica los objetos del almacén según su material principal: "Vidrio", "Metal" o "Plástico".', 5);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(27, 'item-tubo-ensayo.png', 'Vidrio'),
(27, 'item-frasco.png', 'Vidrio'),
(27, 'item-pinzas.png', 'Metal'),
(27, 'item-bisturi.png', 'Metal'),
(27, 'item-jeringa.png', 'Plastico'),
(27, 'item-guantes.png', 'Plastico');

INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(28, 'ADIVINANZA', 'Paso siempre adelante pero nunca me muevo de mi sitio. Soy crucial para todo experimento, pero no me puedes guardar en un frasco. ¿Qué soy?', 6);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los relojes y los calendarios son mis sirvientes.', 1, 28);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('TIEMPO', TRUE, 28);

INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(29, 'DRAG_DROP', 'El escritorio del científico es un desastre. Ayúdalo a organizarse colocando cada objeto en su lugar: "Cajón de escritura", "Estante de lectura" o "Caja de herramientas".', 7);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(29, 'objeto-lapicera.png', 'Cajon de escritura'),
(29, 'objeto-cuaderno.png', 'Cajon de escritura'),
(29, 'objeto-libro.png', 'Estante de lectura'),
(29, 'objeto-lupa.png', 'Caja de herramientas'),
(29, 'objeto-manual.png', 'Estante de lectura'),
(29, 'objeto-destornillador.png', 'Caja de herramientas'),
(29, 'objeto-regla.png', 'Cajon de escritura'),
(29, 'objeto-mapa.png', 'Estante de lectura'),
(29, 'objeto-martillo.png', 'Caja de herramientas'),
(29, 'objeto-goma-borrar.png', 'Cajon de escritura'),
(29, 'objeto-diario.png', 'Estante de lectura'),
(29, 'objeto-pinza.png', 'Caja de herramientas');

INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(30, 'DRAG_DROP', '¡Alerta de Fusión del Núcleo! El reactor está fallando. Para desviar la energía a la puerta de escape, debes arrastrar los componentes funcionales al "Núcleo" y desechar los defectuosos en el "Contenedor de Residuos". ¡No te equivoques!', 8);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(30, 'bateria-llena.png', 'Nucleo'),
(30, 'engranaje-roto.png', 'Residuos'),
(30, 'bombilla-encendida.png', 'Nucleo'),
(30, 'cable-cortado.png', 'Residuos'),
(30, 'engranaje-nuevo.png', 'Nucleo'),
(30, 'bateria-rota.png', 'Residuos');




