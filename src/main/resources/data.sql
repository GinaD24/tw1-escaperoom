INSERT INTO Usuario( email, password, rol) VALUES( 'test@unlam.edu.ar', 'test', 'ADMIN');

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
                                                                                          ('La Mansión Misteriosa', 'PRINCIPIANTE', 'Mansion', 'Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.', TRUE, 5,'puerta-mansion.png', 'mansion-misteriosa.png', 5, 'mansion-ganada.png', 'mansion-perdida.png'),
                                                                                          ('El Laboratorio Secreto', 'INTERMEDIO', 'Laboratorio', 'Un científico desaparecido dejo pistas en su laboratorio. ¿Podras descubrir que tramaba?', TRUE, 10, 'puerta-laboratorio.png', 'laboratorio-secreto.png', 8, 'laboratorio-ganado.png', 'laboratorio-perdido.png'),
                                                                                          ('La Carcel Abandonada', 'AVANZADO', 'Prision', 'Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podras escapar.', TRUE, 12,'puerta-prision.png', 'carcel-abandonada.png', 10, 'carcel-ganada.png', 'carcel-perdida.png');

-- ETAPAS PARA LA SALA 'La Mansión Misteriosa' (id_sala = 1)
INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
                                                             ('Lobby', 1, 'La puerta se ha cerrado a tus espaldas. El aire es denso y el único camino es adelante. Resuelve los enigmas del vestíbulo para avanzar.', 1, 'etapa-lobby.png'), --adivinanza
                                                             ('Biblioteca', 2, 'Entre estanterías polvorientas y libros antiguos, descubres una puerta secreta junto a una serie de botones misteriosos.', 1, 'etapa-biblioteca.png'), -- secuencia
                                                             ('Salón', 3, 'Un gran salón donde aún resuenan las risas y los lamentos del pasado. La música y el silencio guardan las claves para continuar.', 1, 'etapa-salon.png'), -- ordenar
                                                             ('Despacho', 4, 'Este era el refugio del antiguo dueño. Sus objetos personales, mapas y cartas, ocultan las piezas finales de su misterio.', 1, 'etapa-despacho.png'), -- adivinanza
                                                             ('Ático', 5, 'El último piso. Entre baúles y objetos olvidados yace el secreto final que te permitirá encontrar la salida de la mansión.', 1, 'etapa-atico.png'); -- drag drop

-- ACERTIJOS ETAPA 1: Lobby (id_etapa = 1)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Poseo un rostro sin alma y te devuelvo la mirada sin pestañear. Contengo tu reflejo, pero mi interior es un vacío helado.', 1),
                                                 ('ADIVINANZA','Mis dos manos recorren mi rostro sin cesar, devorando el tiempo que no volverá. Mi voz es un pulso constante que mide tu estadía en este lugar.', 1),
                                                 ('ADIVINANZA','Nací de la luz, pero vivo en la penumbra. Soy tu fiel compañera bajo el sol, pero la oscuridad total me hace desaparecer.', 1);

-- ACERTIJOS ETAPA 2: Biblioteca (id_etapa = 2)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('SECUENCIA', 'Observa la secuencia de luces y pulsa los botones en el mismo orden para continuar.', 2);

-- Acertijo 4 sala 1
-- SECUENCIA
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
                                                                           (4, 'boton-rojo.png', 1),
                                                                           (4, 'boton-amarillo.png', 2),
                                                                           (4, 'boton-verde.png', 3),
                                                                           (4, 'boton-azul.png', 4),
                                                                           (4, 'boton-naranja.png', 5);
-- ACERTIJO ETAPA 3: Salón (id_etapa = 3)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ORDENAR_IMAGEN', 'Ordena las tarjetas de los objetos del salón según su tamaño de menor a mayor.', 3);
-- ORDENAR IMAGENES etapa 3
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
                                                                           (5, 'img-copa.png', 1),
                                                                           (5, 'img-silla.png', 2),
                                                                           (5, 'img-mesa.png', 4),
                                                                           (5, 'img-sillon.png', 3);
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Piensa en el tamaño de los objetos en la vida real.', 1, 5);

-- ACERTIJOS ETAPA 4: Despacho (id_etapa = 4)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                 ('ADIVINANZA','Mis dientes no mastican y mi cuello no sostiene una cabeza. Fui forjada para guardar secretos y solo me rindo ante la mano correcta.', 4),
                                                 ('ADIVINANZA','No soy grande, pero hago que el mundo pequeño crezca ante tus ojos. Ayudo a leer lo que el tiempo ha borrado y a encontrar lo que se esconde a simple vista.', 4),
                                                 ('ADIVINANZA','No tengo brazos, pero siempre apunto a un lugar. No tengo piernas, pero te ayudo a caminar. Mi aguja siempre tiembla buscando su hogar.', 4);

-- ACERTIJO ETAPA 5: Ático (id_etapa = 5)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('DRAG_DROP', 'Clasifica los objetos del ático en sus contenedores de utilidad para conseguir la llave de escape.', 5);

-- DRAG DROP etapa 5
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(9, 'item-radio-rota.png', 'Sonido'),
(9, 'item-silbato.png', 'Sonido'),
(9, 'item-campana.png', 'Sonido'),
(9, 'item-libro-viejo.png', 'Combustible'),
(9, 'item-alcohol.png', 'Combustible'),
(9, 'item-tela.png', 'Combustible'),
(9, 'item-linterna.png', 'Iluminacion'),
(9, 'item-candelabro-con-vela.png', 'Iluminacion'),
(9, 'item-caja-fosforos.png', 'Iluminacion');
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Todos los contenedores deben tener la misma cantidad de objetos.', 1, 9);


--13 aacertijos
-- PISTAS Y RESPUESTAS PARA ACERTIJOS 1-3 (Etapa 1)
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Busca tu propio reflejo en la sala.', 1, 1);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('ESPEJO', TRUE, 1);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('El tiempo es la clave. Escucha su constante pasar en el vestíbulo.', 1, 2);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('RELOJ', TRUE, 2);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('No puedes tocarme, pero siempre estoy contigo cuando hay una fuente de luz.', 1, 3);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('SOMBRA', TRUE, 3);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 10-12 (Etapa 4)
    INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Se esconde en el cajón más pequeño del escritorio, esperando abrir algo importante.', 1, 6);
    INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LLAVE', TRUE, 6);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los detalles más pequeños son a menudo los más importantes.', 1, 7); -- Pista nueva
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LUPA', TRUE, 7); -- Respuesta nueva

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Incluso en la quietud de esta habitación, siempre busca el norte.', 1, 8);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('BRUJULA', TRUE, 8);


INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
                                                                     ('Entrada', 1, 'La puerta del laboratorio se cierra detrás de ti. Debes reorganizar los componentes del experimento para avanzar.', 2, 'etapa-entrada-laboratorio.png'),
                                                                     ('Sala de Reactivos', 2, 'Botellas y tubos de ensayo llenos de líquidos de colores te rodean. Organízalos correctamente para desbloquear la siguiente puerta.', 2, 'etapa-reactivos-laboratorio.png'),
                                                                     ('Mesa de Experimentos', 3, 'Te encontras con una mesa llena de instrumentos desordenados y experimentos sin terminar. Solo cuando resuelvas el siguiente acertijo podrás continuar.', 2, 'etapa-mesa-laboratorio.png'),
                                                                     ('Sala de Microscopios', 4, 'Observa las muestras y ordénalas según la secuencia correcta de análisis.', 2, 'etapa-microscopios.png'),
                                                                     ('Almacén de Equipos', 5, 'Hay varios aparatos de laboratorio tirados por todas partes. Organízalos para avanzar.', 2, 'etapa-almacen-laboratorio.png'),
                                                                     ('Cámara de Cultivos', 6, 'Estas en la camara de cultivos, las placas contienen bacterias, que podrian ser mortales. Resolve el siguiente acertijo para salir pronto de aqui.', 2, 'etapa-camara-laboratorio.png'),
                                                                     ('Oficina del Científico', 7, 'El escritorio del científico tiene papeles y herramientas. Colócalos en la secuencia correcta para entender su investigación.', 2, 'etapa-oficina-laboratorio.png'),
                                                                     ('Laboratorio Secreto', 8, 'El corazón del laboratorio. Solo ordenando correctamente todos los elementos del gran experimento podrás acceder al resultado final.', 2, 'etapa-secreto-laboratorio.png');

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
                                                       ('ORDENAR_IMAGEN', 'Observa los frascos de diferentes colores. Organízalos siguiendo el cambio gradual de tonalidad de claro a oscuro.', 6),
                                                       ('DRAG_DROP', 'Clasifica correctamente los materiales de laboratorio en sus categorías correspondientes.', 7),
                                                       ('ADIVINANZA', 'No tengo cuerpo, pero lleno espacios. Soy invisible, pero puedo ser mortal. Nací de reacciones químicas y si escapo, el silencio es mi señal de peligro. ¿Qué soy?', 8);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
    (10, 'img-frasco1.png', 2),
    (10, 'img-frasco2.png', 3),
    (10, 'img-frasco3.png', 1),
    (10, 'img-frasco4.png', 4);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
                                                                         (11, 'Beaker.png', 'Vidrio'),
                                                                         (11, 'Probeta.png', 'Vidrio'),
                                                                         (11, 'Guantes.png', 'Proteccion'),
                                                                         (11, 'Mascarilla.png', 'Proteccion'),
                                                                         (11, 'ReactivoA.png', 'Quimicos'),
                                                                         (11, 'ReactivoB.png', 'Quimicos');

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Algunos extinguen llamas, otros las avivan.', 1, 12);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('GAS', TRUE, 12);

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
('ORDENAR_IMAGEN', 'En el monitor se ven las imágenes de una muestra biológica. Ordénalas para reconstruir el ciclo de vida completo.', 9);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
(13, 'ciclo-huevo.png', 1),
(13, 'ciclo-oruga.png', 2),
(13, 'ciclo-crisalida.png', 3),
(13, 'ciclo-mariposa.png', 4);

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
('DRAG_DROP', '¡Hay que reciclar para poder salir! Clasifica los objetos del almacén según su material principal: "Vidrio", "Metal" o "Plástico".', 10);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(14, 'item-tubo-ensayo.png', 'Vidrio'),
(14, 'item-frasco.png', 'Vidrio'),
(14, 'item-pinzas.png', 'Metal'),
(14, 'item-bisturi.png', 'Metal'),
(14, 'item-jeringa.png', 'Plastico'),
(14, 'item-guantes.png', 'Plastico');

INSERT INTO Acertijo ( tipo, descripcion, id_etapa) VALUES
( 'ADIVINANZA', 'Paso siempre adelante pero nunca me muevo de mi sitio. Soy crucial para todo experimento, pero no me puedes guardar en un frasco. ¿Qué soy?', 11);

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los relojes y los calendarios son mis sirvientes.', 1, 15);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('TIEMPO', TRUE, 15);

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
('DRAG_DROP', 'El escritorio del científico es un desastre. Ayúdalo a organizarse colocando cada objeto en su lugar: "Cajón de escritura", "Estante de lectura" o "Caja de herramientas".', 12);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(16, 'objeto-lapicera.png', 'Cajon de escritura'),
(16, 'objeto-cuaderno.png', 'Cajon de escritura'),
(16, 'objeto-libro.png', 'Estante de lectura'),
(16, 'objeto-lupa.png', 'Caja de herramientas'),
(16, 'objeto-manual.png', 'Estante de lectura'),
(16, 'objeto-destornillador.png', 'Caja de herramientas'),
(16, 'objeto-regla.png', 'Cajon de escritura'),
(16, 'objeto-mapa.png', 'Estante de lectura'),
(16, 'objeto-martillo.png', 'Caja de herramientas'),
(16, 'objeto-goma-borrar.png', 'Cajon de escritura'),
(16, 'objeto-diario.png', 'Estante de lectura'),
(16, 'objeto-pinza.png', 'Caja de herramientas');

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
('DRAG_DROP', '¡Alerta de Fusión del Núcleo! El reactor está fallando. Para desviar la energía a la puerta de escape, debes arrastrar los componentes funcionales al "Núcleo" y desechar los defectuosos en el "Contenedor de Residuos". ¡No te equivoques!', 13);

INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(17, 'bateria-llena.png', 'Nucleo'),
(17, 'engranaje-roto.png', 'Residuos'),
(17, 'bombilla-encendida.png', 'Nucleo'),
(17, 'cable-cortado.png', 'Residuos'),
(17, 'engranaje-nuevo.png', 'Nucleo'),
(17, 'bateria-rota.png', 'Residuos');

-- ------------------------------------------------------------------
-- ETAPAS PARA LA SALA 3: 'La Carcel Abandonada' (id_sala = 3)
-- ------------------------------------------------------------------
INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
('La Celda', 1, 'Despiertas en una celda fría y oscura. La puerta de barrotes está cerrada. Tu primer desafío es encontrar la forma de salir de aquí.', 3, 'etapa-celda.png'),
('Pabellón A', 2, 'Has salido de tu celda, pero ahora te encuentras en el pasillo principal del pabellón. Las miradas vacías de otras celdas te observan. Debes encontrar la salida antes de que un guardia te descubra.', 3, 'etapa-pabellon.png'),
('Comedor', 3, 'El desorden y los restos de una revuelta cubren el suelo del comedor. Entre las mesas y sillas volcadas se esconde la clave para acceder a las áreas de servicio.', 3, 'etapa-comedor.png'),
('Enfermería', 4, 'Un olor a antiséptico inunda la enfermería. Entre historiales médicos y viejas medicinas, hay algo que te ayudará a neutralizar un obstáculo más adelante.', 3, 'etapa-enfermeria.png'),
('Patio de Recreo', 5, 'Finalmente, un respiro de aire "libre", aunque rodeado por muros altos y alambre de púas. La libertad parece cerca, pero el camino a la salida está vigilado. Resuelve el acertijo del patio para no llamar la atención.', 3, 'etapa-patio.png'),
('Sala de Guardias', 6, 'Una pequeña sala con monitores apagados y un tablero de llaves. Es tu oportunidad para conseguir acceso a la zona administrativa, pero cuidado con las alarmas silenciosas.', 3, 'etapa-sala-guardias.png'),
('Lavandería', 7, 'El vapor y el ruido de las máquinas industriales llenan el ambiente. Un antiguo túnel de servicio se rumorea que conecta esta sala con el exterior. Encuentra la entrada.', 3, 'etapa-lavanderia.png'),
('Sala de Visitas', 8, 'El cristal blindado que separa a los reclusos de sus familias ahora es un obstáculo para ti. Hay un mensaje críptico dejado en uno de los teléfonos.', 3, 'etapa-visitas.png'),
('Oficina del Alcaide', 9, 'El corazón de la prisión. Aquí se guardan los secretos y, lo más importante, el control manual de la puerta principal. Resuelve el último enigma para tomar el control.', 3, 'etapa-oficina-alcaide.png'),
('Puerta Principal', 10, 'Estás a un paso de la libertad. Has desactivado la seguridad desde la oficina del alcaide, pero la puerta principal tiene un último mecanismo de bloqueo. Resuélvelo y escapa.', 3, 'etapa-puerta.png');

-- ------------------------------------------------------------------
-- ACERTIJOS PARA LA SALA 3: 'La Carcel Abandonada'
-- ------------------------------------------------------------------

-- ACERTIJO ETAPA 1: La Celda (id_etapa = 14) - ADIVINANZA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(18, 'ADIVINANZA', 'Te retengo sin tocarte, te observo sin tener ojos y aunque soy fuerte, un pequeño objeto es mi amo. ¿Qué soy?', 14);
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Busca en la puerta de tu celda, es lo único que te impide salir.', 1, 18);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('CERRADURA', TRUE, 18);

-- ACERTIJO ETAPA 2: Pabellón A (id_etapa = 15) - SECUENCIA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(19, 'SECUENCIA', 'Una luz de emergencia parpadea en la oscuridad del pasillo. Replica la secuencia en el panel de seguridad para abrir la siguiente puerta.', 15);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
(19, 'luz-roja.png', 5),
(19, 'luz-azul.png', 2),
(19, 'luz-amarilla.png', 6),
(19, 'luz-verde.png', 3),
(19, 'luz-violeta.png', 4),
(19, 'luz-blanca.png', 1);

-- ACERTIJO ETAPA 3: Comedor (id_etapa = 16) - DRAG & DROP
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(20, 'DRAG_DROP', 'El motín dejó todo revuelto. Un guardia perdio una llave, debes limpiar el desorden para encontrarla. Clasifica los objetos entre "Vajilla" y "Contrabando".', 16);
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(20, 'item-bandeja-metalica.png', 'Vajilla'),
(20, 'item-cuchara.png', 'Vajilla'),
(20, 'item-vaso-plastico.png', 'Vajilla'),
(20, 'item-tenedor-afilado.png', 'Contrabando'),
(20, 'item-celular-escondido.png', 'Contrabando'),
(20, 'item-ganzua-improvisada.png', 'Contrabando');
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los presos no deberian tener objetos puntiagudos.', 1, 20);

-- ACERTIJO ETAPA 4: Enfermería (id_etapa = 17) - ORDENAR IMAGEN
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(21, 'ORDENAR_IMAGEN', 'En un manual de primeros auxilios encuentras los pasos para tratar una herida. Ordénalos correctamente para revelar el código de la siguiente sala.', 17);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
(21, 'aplicar-gasa.png', 3),
(21, 'vendar.png', 4),
(21, 'limpiar-herida.png', 1),
(21, 'desinfectar.png', 2);


-- ACERTIJO ETAPA 5: Patio de Recreo (id_etapa = 18) - ADIVINANZA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
    (22, 'ADIVINANZA', 'Siempre estoy cerca pero no me ves, observo cada movimiento y aunque todos me ignoran, nunca me pierdo nada. ¿Qué soy?', 18);
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES
    ('Vigila sin moverse y está en todas partes en la carcel.', 1, 22);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('CAMARA', TRUE, 22);

-- ACERTIJO ETAPA 6: Sala de Guardias (id_etapa = 19) - SECUENCIA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(23, 'SECUENCIA', 'El código de anulación de la alarma se muestra brevemente en un monitor. Memoriza la secuencia de símbolos y reprodúcela en el teclado numérico.', 19);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
(23, 'simbolo-triangulo.png', 1),
(23, 'simbolo-cuadrado.png', 2),
(23, 'simbolo-circulo.png', 3),
(23, 'simbolo-rectangulo.png', 4),
(23, 'simbolo-rombo.png', 5);

-- ACERTIJO ETAPA 7: Lavandería (id_etapa = 20) - ORDENAR IMAGEN
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(24, 'ORDENAR_IMAGEN', 'Un cartel muestra el procedimiento correcto para usar las lavadoras industriales. Ordena las imágenes para encontrar la combinación que abre el conducto de ventilación.', 20);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
(24, 'agregar-jabon.png', 2),
(24, 'cargar-ropa.png', 1),
(24, 'iniciar.png', 4),
(24, 'seleccionar-ciclo.png', 3);

-- ACERTIJO ETAPA 8: Sala de Visitas (id_etapa = 21) - DRAG & DROP
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(25, 'DRAG_DROP', 'Los guardias han confiscado varios objetos durante las visitas. Ayuda a clasificarlos correctamente en "Permitido" y "Prohibido" para no levantar sospechas.', 21);
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(25, 'carta-familiar.png', 'Permitido'),
(25, 'libro.png', 'Permitido'),
(25, 'mapa-prision.png', 'Prohibido'),
(25, 'telefono-pequenio.png', 'Prohibido'),
(25, 'fotos-personales.png', 'Permitido'),
(25, 'lima-metalica.png', 'Prohibido');
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los contenedores tienen la misma cantidad de objetos.', 1, 25);

-- ACERTIJO ETAPA 9: Oficina del Alcaide (id_etapa = 22) - DRAG & DROP
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(26, 'DRAG_DROP', 'El alcaide es un hombre de orden. Clasifica los objetos de su escritorio en sus cajas correspondientes: "Documentos Oficiales", "Afectos Personales" y "Equipo de Seguridad".', 22);
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
(26, 'informe-recluso.png', 'Documentos Oficiales'),
(26, 'foto-familiar.png', 'Efectos Personales'),
(26, 'walkie-talkie.png', 'Equipo de Seguridad'),
(26, 'orden-traslado.png', 'Documentos Oficiales'),
(26, 'esposas.png', 'Equipo de Seguridad'),
(26, 'pluma-fuente.png', 'Efectos Personales');

-- ACERTIJO ETAPA 10: Puerta Principal (id_etapa = 23) - ADIVINANZA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(27, 'ADIVINANZA', 'Todos me anhelan aquí dentro, pero no tengo peso ni forma. No se me puede ver ni tocar, pero se siente cuando se ha ido. Para conseguirme, debes resolverlo todo. ¿Qué soy?', 23);
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Es lo que has estado buscando desde que despertaste en la celda.', 1, 27);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LIBERTAD', TRUE, 27);