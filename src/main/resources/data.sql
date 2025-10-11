
INSERT INTO Usuario(email, password, rol, activo) VALUES( 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Sala (nombre, dificultad, escenario, historia, esta_habilitada, duracion, imagen)VALUES
                                                                                          ('La Mansión Misteriosa', 'PRINCIPIANTE', 'Mansion', 'Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.', TRUE, 10,'puerta-mansion.png'),
                                                                                          ('El Laboratorio Secreto', 'INTERMEDIO', 'Laboratorio', 'Un científico desaparecido dejo pistas en su laboratorio. ¿Podras descubrir que tramaba?', TRUE, 15, 'puerta-laboratorio.png'),
                                                                                          ('La Carcel Abandonada', 'AVANZADO', 'Prision', 'Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podras escapar.', TRUE, 20,'puerta-prision.png');

-- ETAPAS PARA LA SALA 'La Mansión Misteriosa' (id_sala = 1)
INSERT INTO Etapa (nombre, numero, descripcion, id_sala) VALUES
                                                             ('Lobby', 1, 'La puerta se ha cerrado a tus espaldas. El aire es denso y el único camino es adelante. Resuelve los enigmas del vestíbulo para avanzar.', 1),
                                                             ('Biblioteca', 2, 'Has llegado a una biblioteca cubierta de polvo. Entre miles de libros se esconden secretos que ansían ser leídos en voz alta.', 1),
                                                             ('Salón', 3, 'Un gran salón donde aún resuenan las risas y los lamentos del pasado. La música y el silencio guardan las claves para continuar.', 1),
                                                             ('Despacho', 4, 'Este era el refugio del antiguo dueño. Sus objetos personales, mapas y cartas, ocultan las piezas finales de su misterio.', 1),
                                                             ('Ático', 5, 'El último piso. Entre baúles y objetos olvidados yace el secreto final que te permitirá encontrar la salida de la mansión.', 1);

-- ACERTIJOS ETAPA 1: Lobby (id_etapa = 1)
INSERT INTO Acertijo (descripcion, id_etapa) VALUES
                                                 ('Poseo un rostro sin alma y te devuelvo la mirada sin pestañear. Contengo tu reflejo, pero mi interior es un vacío helado.', 1),
                                                 ('Mis dos manos recorren mi rostro sin cesar, devorando el tiempo que no volverá. Mi voz es un pulso constante que mide tu estadía en este lugar.', 1),
                                                 ('Nací de la luz, pero vivo en la penumbra. Soy tu fiel compañera bajo el sol, pero la oscuridad total me hace desaparecer.', 1);

-- ACERTIJOS ETAPA 2: Biblioteca (id_etapa = 2)
INSERT INTO Acertijo (descripcion, id_etapa) VALUES
                                                 ('Mis hojas guardan bosques y mis lomos sostienen mundos. Hablo sin voz, tejo historias en silencio para quien se atreva a abrirme.', 2),
                                                 ('Mi cuerpo de cera se consume para darte luz. Lloro lágrimas ardientes mientras mi vida se extingue lentamente en la penumbra.', 2),
                                                 ('Soy un río de noche líquida, nacido para dar cuerpo a los pensamientos. Con mi esencia, las palabras se graban y los secretos perduran en el papel.', 2);

-- ACERTIJOS ETAPA 3: Salón (id_etapa = 3)
INSERT INTO Acertijo (descripcion, id_etapa) VALUES
                                                 ('Una dentadura de marfil y ébano espera ser tocada. No muerdo, pero si me presionas con arte, liberaré los ecos de una triste melodía.', 3),
                                                 ('Reino en la ausencia de todo sonido. Me puedes sentir en el aire, pero si pronuncias mi nombre, me habrás roto.', 3),
                                                 ('Soy una cascada de cristal congelada en el tiempo. Cuelgo sobre tus cabezas, portador de una luz que ya no existe, reflejando glorias pasadas.', 3);

-- ACERTIJOS ETAPA 4: Despacho (id_etapa = 4)
INSERT INTO Acertijo (descripcion, id_etapa) VALUES
                                                 ('Mis dientes no mastican y mi cuello no sostiene una cabeza. Fui forjada para guardar secretos y solo me rindo ante la mano correcta.', 4),
                                                 ('No soy grande, pero hago que el mundo pequeño crezca ante tus ojos. Ayudo a leer lo que el tiempo ha borrado y a encontrar lo que se esconde a simple vista.', 4),
                                                 ('No tengo brazos, pero siempre apunto a un lugar. No tengo piernas, pero te ayudo a caminar. Mi aguja siempre tiembla buscando su hogar.', 4);

-- ACERTIJOS ETAPA 5: Ático (id_etapa = 5)
INSERT INTO Acertijo (descripcion, id_etapa) VALUES
                                                 ('Un encaje frágil y polvoriento, tejido en la oscuridad de los rincones. Soy una trampa de seda, una obra de arte abandonada por su artista.', 5),
                                                 ('Soy un instante congelado, una memoria atrapada en papel. Mi mundo no tiene sonido ni movimiento, solo el eco de una sonrisa que ya no está.', 5),
                                                 ('Soy la promesa al final del pasillo, la luz tras la última puerta. No soy un objeto, sino un destino. Has resuelto todo para encontrarme.', 5);

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
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Mira hacia las esquinas altas y oscuras, donde el tiempo ha dejado su marca.', 1, 13);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('TELARAÑA', TRUE, 13);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Un viejo álbum guarda momentos congelados en el tiempo. Sonríen desde el pasado.', 1, 14);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('FOTOGRAFIA', TRUE, 14);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Has resuelto todo, solo queda un último paso: cruzar el umbral.', 1, 15);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('SALIDA', TRUE, 15);