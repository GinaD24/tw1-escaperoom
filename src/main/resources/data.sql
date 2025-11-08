INSERT INTO Usuario( email, password, rol, nombreUsuario,fotoPerfil) VALUES( 'test@unlam.edu.ar', 'test', 'ADMIN', 'UsuarioTest','fotoPerfil_juan.png');

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



INSERT INTO Sala (nombre, dificultad, escenario, historia, es_paga, esta_habilitada, duracion, imagenPuerta, imagenSala, cantidadDeEtapas, imagenGanada, imagenPerdida)VALUES
                                                                                          ('La Mansión Misteriosa', 'PRINCIPIANTE', 'Mansion', 'Una noche tormentosa te encuentras atrapado en una vieja mansion llena de acertijos.', FALSE, TRUE, 5,'puerta-mansion.png', 'mansion-misteriosa.png', 5, 'mansion-ganada.png', 'mansion-perdida.png'),
                                                                                          ('El Laboratorio Secreto', 'INTERMEDIO', 'Laboratorio', 'Un científico desaparecido dejo pistas en su laboratorio. ¿Podras descubrir que tramaba?', FALSE,TRUE, 10, 'puerta-laboratorio.png', 'laboratorio-secreto.png', 8, 'laboratorio-ganado.png', 'laboratorio-perdido.png'),
                                                                                          ('La Carcel Abandonada', 'AVANZADO', 'Prision', 'Despiertas en una celda oxidada. Solo resolviendo complejos acertijos podras escapar.', FALSE,TRUE, 12,'puerta-prision.png', 'carcel-abandonada.png', 10, 'carcel-ganada.png', 'carcel-perdida.png'),
                                                                                          ('El campamento maldito', 'PRINCIPIANTE', 'Campamento', 'Estás perdido en un campamento de verano olvidado, envuelto en una densa niebla. Hay pistas de un ritual a medio terminar y debes buscar la llave del predio y encontrar el camino antes de que ''algo'' te encuentre.', TRUE,FALSE, 5, 'puerta-campamento.png', 'campamento-maldito.png', 5, 'campamento-ganado.png', 'campamento-perdido.png'),
                                                                                          ('El hospital psiquiatrico', 'AVANZADO', 'Hospital', 'Te han encerrado en el ala de contención de un hospital clausurado. Los antiguos pacientes no se han ido, y debes encontrar la llave de la morgue para escapar antes de que la medianoche te convierta en uno de ellos.', TRUE,FALSE, 12, 'puerta-psiquiatrico.png', 'hospital-psiquiatrico.png', 10, 'psiquiatrico-ganado.png', 'psiquiatrico-perdido.png');

-- ETAPAS PARA LA SALA 'La Mansión Misteriosa' (id_sala = 1)
INSERT INTO Etapa (nombre, numero, tieneBonus, bonusTop, bonusLeft, descripcion, id_sala, imagen) VALUES
                                                             ('Lobby', 1, FALSE, null, null, 'La puerta se ha cerrado a tus espaldas. El aire es denso y el único camino es adelante. Resuelve los enigmas del vestíbulo para avanzar.', 1, 'etapa-lobby.png'), --adivinanza
                                                             ('Biblioteca', 2,FALSE , null, null,'Entre estanterías polvorientas y libros antiguos, descubres una puerta secreta junto a una serie de botones misteriosos.', 1, 'etapa-biblioteca.png'), -- secuencia
                                                             ('Salón', 3, TRUE, 85, 15,'Un gran salón donde aún resuenan las risas y los lamentos del pasado. La música y el silencio guardan las claves para continuar.', 1, 'etapa-salon.png'), -- ordenar
                                                             ('Despacho', 4,FALSE , null, null,'Este era el refugio del antiguo dueño. Sus objetos personales, mapas y cartas, ocultan las piezas finales de su misterio.', 1, 'etapa-despacho.png'), -- adivinanza
                                                             ('Ático', 5, FALSE, null, null,'El último piso. Entre baúles y objetos olvidados yace el secreto final que te permitirá encontrar la salida de la mansión.', 1, 'etapa-atico.png'); -- drag drop

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
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
                                                                           (4, 'boton-rojo.png'),
                                                                           (4, 'boton-amarillo.png'),
                                                                           (4, 'boton-verde.png'),
                                                                           (4, 'boton-azul.png'),
                                                                           (4, 'boton-naranja.png');
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
-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Busca tu propio reflejo en la sala.', 1, 1);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('ESPEJO', TRUE, 1);

-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('El tiempo es la clave. Escucha su constante pasar en el vestíbulo.', 1, 2);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('RELOJ', TRUE, 2);

-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('No puedes tocarme, pero siempre estoy contigo cuando hay una fuente de luz.', 1, 3);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('SOMBRA', TRUE, 3);

-- PISTAS Y RESPUESTAS PARA ACERTIJOS 10-12 (Etapa 4)
 --    INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Se esconde en el cajón más pequeño del escritorio, esperando abrir algo importante.', 1, 6);
    INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LLAVE', TRUE, 6);

-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los detalles más pequeños son a menudo los más importantes.', 1, 7); -- Pista nueva
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LUPA', TRUE, 7); -- Respuesta nueva

-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Incluso en la quietud de esta habitación, siempre busca el norte.', 1, 8);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('BRUJULA', TRUE, 8);


INSERT INTO Etapa (nombre, numero, tieneBonus, bonusTop, bonusLeft, descripcion, id_sala, imagen) VALUES
                                                                     ('Entrada', 1, FALSE, null, null,'La puerta del laboratorio se cierra detrás de ti. Debes reorganizar los componentes del experimento para avanzar.', 2, 'etapa-entrada-laboratorio.png'),
                                                                     ('Sala de Reactivos', 2, FALSE, null, null, 'Botellas y tubos de ensayo llenos de líquidos de colores te rodean. Organízalos correctamente para desbloquear la siguiente puerta.', 2, 'etapa-reactivos-laboratorio.png'),
                                                                     ('Mesa de Experimentos', 3, FALSE, null, null, 'Te encontras con una mesa llena de instrumentos desordenados y experimentos sin terminar. Solo cuando resuelvas el siguiente acertijo podrás continuar.', 2, 'etapa-mesa-laboratorio.png'),
                                                                     ('Sala de Microscopios', 4, FALSE, null, null, 'Observa las muestras y ordénalas según la secuencia correcta de análisis.', 2, 'etapa-microscopios.png'),
                                                                     ('Almacén de Equipos', 5, FALSE, null, null, 'Hay varios aparatos de laboratorio tirados por todas partes. Organízalos para avanzar.', 2, 'etapa-almacen-laboratorio.png'),
                                                                     ('Cámara de Cultivos', 6, FALSE, null, null, 'Estas en la camara de cultivos, las placas contienen bacterias, que podrian ser mortales. Resolve el siguiente acertijo para salir pronto de aqui.', 2, 'etapa-camara-laboratorio.png'),
                                                                     ('Oficina del Científico', 7, FALSE, null, null, 'El escritorio del científico tiene papeles y herramientas. Colócalos en la secuencia correcta para entender su investigación.', 2, 'etapa-oficina-laboratorio.png'),
                                                                     ('Laboratorio Secreto', 8,  TRUE, 22, 79,'El corazón del laboratorio. Solo ordenando correctamente todos los elementos del gran experimento podrás acceder al resultado final.', 2, 'etapa-secreto-laboratorio.png');

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

-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Los relojes y los calendarios son mis sirvientes.', 1, 15);
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
INSERT INTO Etapa (nombre, numero, tieneBonus, bonusTop, bonusLeft, descripcion, id_sala, imagen) VALUES
('La Celda', 1, FALSE, null, null, 'Despiertas en una celda fría y oscura. La puerta de barrotes está cerrada. Tu primer desafío es encontrar la forma de salir de aquí.', 3, 'etapa-celda.png'),
('Pabellón A', 2, FALSE, null, null, 'Has salido de tu celda, pero ahora te encuentras en el pasillo principal del pabellón. Las miradas vacías de otras celdas te observan. Debes encontrar la salida antes de que un guardia te descubra.', 3, 'etapa-pabellon.png'),
('Comedor', 3, FALSE, null, null, 'El desorden y los restos de una revuelta cubren el suelo del comedor. Entre las mesas y sillas volcadas se esconde la clave para acceder a las áreas de servicio.', 3, 'etapa-comedor.png'),
('Enfermería', 4, FALSE, null, null, 'Un olor a antiséptico inunda la enfermería. Entre historiales médicos y viejas medicinas, hay algo que te ayudará a neutralizar un obstáculo más adelante.', 3, 'etapa-enfermeria.png'),
('Patio de Recreo', 5, FALSE, null, null, 'Finalmente, un respiro de aire "libre", aunque rodeado por muros altos y alambre de púas. La libertad parece cerca, pero el camino a la salida está vigilado. Resuelve el acertijo del patio para no llamar la atención.', 3, 'etapa-patio.png'),
('Sala de Guardias', 6, TRUE, 72, 13,'Una pequeña sala con monitores apagados y un tablero de llaves. Es tu oportunidad para conseguir acceso a la zona administrativa, pero cuidado con las alarmas silenciosas.', 3, 'etapa-sala-guardias.png'),
('Lavandería', 7, FALSE, null, null, 'El vapor y el ruido de las máquinas industriales llenan el ambiente. Un antiguo túnel de servicio se rumorea que conecta esta sala con el exterior. Encuentra la entrada.', 3, 'etapa-lavanderia.png'),
('Sala de Visitas', 8, FALSE, null, null, 'El cristal blindado que separa a los reclusos de sus familias ahora es un obstáculo para ti. Hay un mensaje críptico dejado en uno de los teléfonos.', 3, 'etapa-visitas.png'),
('Oficina del Alcaide', 9, FALSE, null, null,'El corazón de la prisión. Aquí se guardan los secretos y, lo más importante, el control manual de la puerta principal. Resuelve el último enigma para tomar el control.', 3, 'etapa-oficina-alcaide.png'),
('Puerta Principal', 10, FALSE, null, null, 'Estás a un paso de la libertad. Has desactivado la seguridad desde la oficina del alcaide, pero la puerta principal tiene un último mecanismo de bloqueo. Resuélvelo y escapa.', 3, 'etapa-puerta.png');

-- ------------------------------------------------------------------
-- ACERTIJOS PARA LA SALA 3: 'La Carcel Abandonada'
-- ------------------------------------------------------------------

-- ACERTIJO ETAPA 1: La Celda (id_etapa = 14) - ADIVINANZA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(18, 'ADIVINANZA', 'Te retengo sin tocarte, te observo sin tener ojos y aunque soy fuerte, un pequeño objeto es mi amo. ¿Qué soy?', 14);
-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Busca en la puerta de tu celda, es lo único que te impide salir.', 1, 18);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('CERRADURA', TRUE, 18);

-- ACERTIJO ETAPA 2: Pabellón A (id_etapa = 15) - SECUENCIA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(19, 'SECUENCIA', 'Una luz de emergencia parpadea en la oscuridad del pasillo. Replica la secuencia en el panel de seguridad para abrir la siguiente puerta.', 15);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
(19, 'luz-roja.png'),
(19, 'luz-azul.png'),
(19, 'luz-amarilla.png'),
(19, 'luz-verde.png'),
(19, 'luz-violeta.png'),
(19, 'luz-blanca.png');

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
 -- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES
 --   ('Vigila sin moverse y está en todas partes en la carcel.', 1, 22);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('CAMARA', TRUE, 22);

-- ACERTIJO ETAPA 6: Sala de Guardias (id_etapa = 19) - SECUENCIA
INSERT INTO Acertijo (id, tipo, descripcion, id_etapa) VALUES
(23, 'SECUENCIA', 'El código de anulación de la alarma se muestra brevemente en un monitor. Memoriza la secuencia de símbolos y reprodúcela en el teclado numérico.', 19);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
(23, 'simbolo-triangulo.png' ),
(23, 'simbolo-cuadrado.png'),
(23, 'simbolo-circulo.png'),
(23, 'simbolo-rectangulo.png'),
(23, 'simbolo-rombo.png');

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
-- INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('Es lo que has estado buscando desde que despertaste en la celda.', 1, 27);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LIBERTAD', TRUE, 27);


-- ------------------------------------------------------------------
-- ETAPAS PARA LA SALA 5: 'El campamento maldito'
-- ------------------------------------------------------------------
INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
                                                 ('La Cabaña de Intrusos', 1, 'Te despiertas encerrado en una pequeña cabaña abandonada. La cerradura de la puerta tiene 7 botones marcados con números.', 4, 'etapa-cabana-intrusos.png'), -- Secuencia sencilla de números (contar nudos).
                                                 ('El Fuego Muerto', 2, 'Llegas al área de la fogata. Está apagada, pero hay cenizas y una hoja escrita con un acertijo. Resolvelo para continuar', 4, 'etapa-fuego-muerto.png'),
                                                 ('El Muelle', 3, 'Un muelle deteriorado se adentra en el lago. La niebla es tan espesa que solo ves siluetas. En la barandilla hay cinco fotos viejas de un campista. Debes ordenarlas cronológicamente para revelar la combinación grabada debajo del muelle.', 4, 'etapa-muelle.png'),
                                                 ('Cabaña de Vigilancia', 4, 'La casa del guardián. El sistema de comunicación está roto. 5 interruptores controlan las luces exteriores. Debes encender las luces en el orden correcto.', 4, 'etapa-cabana-vigilancia.png'),
                                                 ('El Borde del Bosque', 5, 'El portón está sellado con tres símbolos de elementos: FUEGO, TIERRA y AGUA. Tienes seis objetos que recogiste. Arrastra los objetos que pertenecen a cada elemento correspondiente para abrir la salida.', 4, 'etapa-borde-bosque-elemental.png');

-- ACERTIJO ETAPA 1: La Cabaña de Intrusos  - SECUENCIA
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('SECUENCIA', 'El código de la cerradura se muestra brevemente. Memoriza la secuencia y reprodúcela en el teclado numérico.', 24);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
                                                            (28, 'btn-numero-1.png' ),
                                                            (28, 'btn-numero-2.png'),
                                                            (28, 'btn-numero-3.png'),
                                                            (28, 'btn-numero-4.png'),
                                                            (28, 'btn-numero-5.png'),
                                                            (28, 'btn-numero-6.png'),
                                                            (28, 'btn-numero-7.png');

-- ACERTIJO ETAPA 2: El Fuego Muerto - ADIVINANZA
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ADIVINANZA', 'Cuando vivo, no hago ruido. Cuando muero, canto. Mi vida se cuenta en anillos, pero mi fin es dar calor. ¿Qué soy?', 25);

INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('MADERA', TRUE, 29);

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ADIVINANZA', 'Tengo costa pero no arenas, siempre estoy mojado. Guardo el reflejo de la luna y mis profundidades esconden secretos.', 25);

INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('LAGO', TRUE, 30);

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ( 'ADIVINANZA', 'No tengo cuerpo ni alas, pero viajo sin cesar. Oculto el camino y el destino, y si me ves muy cerca, es porque estás perdido.', 25);

INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('NIEBLA', TRUE, 31);

-- ACERTIJO ETAPA 4: Cabaña de Vigilancia - ORDENAR IMAGEN

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ORDENAR_IMAGEN', 'Las fotos viejas de un campista se desordenaron. Ordénalas cronológicamente de la más antigua a la más reciente para revelar el código grabado debajo del escritorio.', 26);

-- ORDENAR IMAGENES: Fotos de campistas (validación por ordenCorrecto)
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
                                                                           (32, 'foto-campista-nino.png', 1),
                                                                           (32, 'foto-campista-preadolescente.png', 2),
                                                                           (32, 'foto-campista-adolescente.png', 3),
                                                                           (32, 'foto-campista-adultoJoven.png', 4),
                                                                           (32, 'foto-campista-adulto.png', 5);

-- ACERTIJO ETAPA 4: Cabaña de Vigilancia - SECUENCIA
INSERT INTO Acertijo ( tipo, descripcion, id_etapa) VALUES
    ('SECUENCIA', 'Un diagrama borroso muestra el orden de activación de las luces del sistema: blanca, amarilla, azul, roja, naranja. Replica la secuencia que se muestra una vez.', 27);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
                                                            (33, 'luz-blanca-interr.png'),
                                                            (33, 'luz-amarilla-interr.png'),
                                                            (33, 'luz-azul-interr.png'),
                                                            (33, 'luz-naranja-interr.png'),
                                                            (33, 'luz-roja-interr.png');

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ( 'DRAG_DROP', 'El portón está sellado con tres símbolos de elementos: FUEGO, TIERRA y AGUA. Arrastra los dos objetos que pertenecen a cada elemento al símbolo correspondiente para liberar la cerradura.', 28);
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
                                                                         (34, 'item-caja-fosforos.png', 'FUEGO'),
                                                                         (34, 'item-encendedor.png', 'FUEGO'),
                                                                         (34, 'item-hojas.png', 'TIERRA'),
                                                                         (34, 'item-piedras.png', 'TIERRA'),
                                                                         (34, 'item-corteza.png', 'TIERRA'),
                                                                         (34, 'item-botella-agua.png', 'AGUA');
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES
    ('Busca elementos que puedan arder, que provengan del suelo o que sean nativos del lago.', 1, 34);

-- SALA 5 AVANZADA

-- ------------------------------------------------------------------
-- ETAPAS PARA LA SALA 'El Hospital Psiquiatrico' (id_sala = 5)
-- ------------------------------------------------------------------
INSERT INTO Etapa (nombre, numero, descripcion, id_sala, imagen) VALUES
                                                                     ('Dormitorio Principal', 1, 'Estás encerrado en una pequeña celda. La puerta tiene una mirilla que te permite ver el pasillo. Debes descifrar el misterio que te retiene para forzar la cerradura.', 5, 'etapa-dormitorio-principal.png'),
                                                                     ('Pasillo de Celdas', 2, 'El pasillo principal del ala de dormitorios. Hay un antiguo panel de control que se activa al replicar la secuencia de un patrón periódico.', 5, 'etapa-pasillo-celdas.png'),
                                                                     ('Sala de Terapia Grupal', 3, 'Una sala llena de objetos rotos. Debes restaurar el orden de unos registros abandonados siguiendo la lógica del hospital para abrir la puerta.', 5, 'etapa-terapia-grupal.png'),
                                                                     ('Consultorio de Archivos', 4, 'El escritorio está cubierto de expedientes desordenados. Debes clasificar la documentación urgente para encontrar la llave oculta.', 5, 'etapa-consultorio-archivos.png'),
                                                                     ('Sala de Tratamientos', 5, 'Un equipo obsoleto y un panel de control con luces de advertencia. Debes replicar un patrón visual para desactivar el bloqueo que impide tu paso.', 5, 'etapa-tratamientos.png'),
                                                                     ('Cocina Industrial', 6, 'Una gran sala con mesas volcadas. Una nota te desafía a resolver un enigma que apunta al objeto más peligroso y prohibido de este lugar.', 5, 'etapa-cocina.png'),
                                                                     ('Farmacia', 7, 'Miles de frascos y pastillas polvorientas. Debes ordenar el protocolo de una receta olvidada para abrir un compartimento secreto en el mostrador.', 5, 'etapa-farmacia.png'),
                                                                     ('Baños Comunitarios', 8, 'El vapor y la humedad han cubierto las paredes. Un mensaje críptico te obliga a resolver un enigma sobre una ilusión que aparece en los espejos.', 5, 'etapa-banios.png'),
                                                                     ('Unidad de Observación', 9, 'Máquinas de soporte vital obsoletas. Debes clasificar el instrumental médico abandonado en esta sala para liberar el acceso al subsuelo.', 5, 'etapa-unidad-observacion.png'),
                                                                     ('Subsuelo y Calderas', 10, 'El último tramo oscuro. Un panel de control de las calderas es tu única oportunidad. Debes forzar el sistema activando una secuencia precisa para escapar a la Morgue.', 5, 'etapa-subsuelo.png');


-- ACERTIJO ETAPA 1: Dormitorio Principal
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ADIVINANZA', 'Vivo en tu cabeza, pero no soy tu cerebro. Me hago más fuerte cuando estás solo y me alimento de tus miedos. Aunque intentes gritar, soy la voz que nadie escucha. ¿Qué soy?', 29);
INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES ('LOCURA', TRUE, 35);

-- ------------------------------------------------------------------
-- ACERTIJO PARA ETAPA 2: Pasillo de Celdas (id_etapa = 30)

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('SECUENCIA', 'Los botones de este panel corresponden a los colores de los uniformes del personal. Observa la secuencia visual de colores que se muestra y replícala para abrir la puerta.', 30);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
                                                            (36, 'boton-uniforme-gris.png'),
                                                            (36, 'boton-uniforme-azul.png'),
                                                            (36, 'boton-uniforme-blanco.png'),
                                                            (36, 'boton-uniforme-rojo.png'),
                                                            (36, 'boton-uniforme-verde.png');

-- ------------------------------------------------------------------
-- ETAPA 3: Sala de Terapia Grupal (id_etapa = 31)

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ORDENAR_IMAGEN', 'Encuentras cinco fichas de pacientes. Ordénalas según el proceso lógico de tratamiento en el hospital, desde la entrada hasta la salida.', 31);
INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
                                                                           (37, 'paso-admision.png', 1),     -- Entrada al hospital
                                                                           (37, 'paso-diagnostico.png', 2),  -- Evaluación médica
                                                                           (37, 'paso-medicacion.png', 3),   -- Inicio del tratamiento
                                                                           (37, 'paso-sesion-grupal.png', 4), -- Terapia de grupo (o individual)
                                                                           (37, 'paso-alta-medica.png', 5);    -- Salida (Alta)

INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES
    ('El orden comienza con la entrada del paciente y termina con su salida del centro.', 1, 37);

-- ------------------------------------------------------------------
-- ACERTIJO ETAPA 4: Consultorio de Archivos (id_etapa = 32)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('DRAG_DROP', 'Clasifica los historiales médicos: el sello verde es para expedientes "Cerrados" y el sello rojo para "Abiertos".', 32);
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
                                                                         (38, 'expediente-sello-rojo-abierto.png', 'Abiertos'),
                                                                         (38, 'expediente-sello-verde-cerrado.png', 'Cerrados'),
                                                                         (38, 'expediente-sello-rojo-abierto2.png', 'Abiertos'),
                                                                         (38, 'expediente-sello-verde-cerrado2.png', 'Cerrados'),
                                                                         (38, 'expediente-sello-rojo-abierto3.png', 'Abiertos'),
                                                                         (38, 'expediente-sello-verde-cerrado3.png', 'Cerrados');
INSERT INTO Pista (descripcion, numero, id_acertijo) VALUES ('El color rojo siempre indica una alerta, un caso que aún está en proceso.', 1, 38);

-- ------------------------------------------------------------------
-- ETAPA 5: Sala de Tratamientos (id_etapa = 33)

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('SECUENCIA', 'El panel de control tiene luces de advertencia. Observa la secuencia que se muestra en el monitor y replícala en los botones de colores para detener la sobrecarga del sistema.', 33);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
                                                            (39, 'boton-luz-roja.png'),
                                                            (39, 'boton-luz-naranja.png'),
                                                            (39, 'boton-luz-amarilla.png'),
                                                            (39, 'boton-luz-verde.png'),
                                                            (39, 'boton-luz-azul.png'),
                                                            (39, 'boton-luz-violeta.png');

-- ------------------------------------------------------------------
-- ETAPA 6: Cocina Industrial (id_etapa = 34)

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ADIVINANZA', 'Tengo filo sin ser herramienta, sirvo a la mano que me empuña y divido todo lo que toco. En estos muros, mi ausencia era ley y mi encuentro, un gran peligro. ¿Qué soy?', 34);

INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('CUCHILLO', TRUE, 40);

-- ------------------------------------------------------------------
-- ACERTIJO ETAPA 7: Farmacia Antigua (id_etapa = 35)

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ORDENAR_IMAGEN', 'Un recetario polvoriento muestra los pasos para preparar un potente sedante. Ordénalos correctamente para que la fórmula funcione y puedas avanzar sin ser notado.', 35);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo, ordenCorrecto) VALUES
                                                                           (41, 'paso-pesar-ingredientes.png', 1),
                                                                           (41, 'paso-mezclar-liquidos.png', 2),
                                                                           (41, 'paso-calentar-suave.png', 3),
                                                                           (41, 'paso-verter-frasco.png', 4);

-- ------------------------------------------------------------------
--ETAPA 8: Baños Comunitarios (id_etapa = 36)

INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('ADIVINANZA', 'No tengo cuerpo, pero puedo ocultarte la visión. Lleno este cuarto sin ser invitado y hago que tu reflejo en el espejo se desvanezca por completo. Solo el frío me hace desaparecer. ¿Qué soy?', 36);

INSERT INTO Respuesta (respuesta, es_correcta, id_acertijo) VALUES
    ('VAPOR', TRUE, 42);


-- ------------------------------------------------------------------
-- ACERTIJO ETAPA 9: Unidad de Observación (id_etapa = 37)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('DRAG_DROP', 'Clasifica los objetos abandonados en "Instrumental de Cirugía" y "Equipo Básico" para desechar lo que no sirve.', 37);
INSERT INTO DragDropItem (id_acertijo, contenido, categoriaCorrecta) VALUES
                                                                         (43, 'obj-bisturi.png', 'Instrumental de Cirugia'),
                                                                         (43, 'obj-martillo-reflejos.png', 'Equipo Basico'),
                                                                         (43, 'obj-tijeras-medicas.png', 'Instrumental de Cirugia'),
                                                                         (43, 'obj-estetoscopio.png', 'Equipo Basico'),
                                                                         (43, 'obj-vendas-gasa.png', 'Equipo Basico'),
                                                                         (43, 'obj-pinzas-quirurgicas.png', 'Instrumental de Cirugia'),
                                                                         (43, 'obj-termometro.png', 'Equipo Basico');


-- ------------------------------------------------------------------
-- ETAPA 10: Subsuelo y Calderas (id_etapa = 38)
INSERT INTO Acertijo (tipo, descripcion, id_etapa) VALUES
    ('SECUENCIA', 'Un antiguo panel de control monitorea las 7 válvulas principales de presión que bloquean la puerta de la Morgue. Observa la secuencia y actívalas en orden para sobrecargar el sistema de seguridad y forzar tu escape.', 38);

INSERT INTO ImagenAcertijo (id_acertijo, nombreArchivo) VALUES
                                                            (44, 'valvula-simbolo-alfa.png'),
                                                            (44, 'valvula-simbolo-beta.png'),
                                                            (44, 'valvula-simbolo-gamma.png'),
                                                            (44, 'valvula-simbolo-delta.png'),
                                                            (44, 'valvula-simbolo-epsilon.png'),
                                                            (44, 'valvula-simbolo-zeta.png'),
                                                            (44, 'valvula-simbolo-psi.png');
