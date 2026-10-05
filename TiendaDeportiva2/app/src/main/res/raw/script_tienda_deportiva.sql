-- ============================================================
-- CHECKPOINT 3: SCRIPTS BASE DE DATOS / CARGA DE DATOS EXAMEN
-- Tienda de Artículos Deportivos - Programación III
-- ============================================================

-- Tabla de Categorías Deportivas
CREATE TABLE IF NOT EXISTS CategoriaDeportiva (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    iconoSimbolo VARCHAR(10),
    descripcion TEXT,
    popularidad INT DEFAULT 5
);

-- Tabla de Productos Deportivos (Base)
CREATE TABLE IF NOT EXISTS ProductoDeportivo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    precioBase DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    descripcion TEXT,
    categoriaId INT,
    tipoProducto VARCHAR(50), -- 'CALZADO' o 'EQUIPAMIENTO'
    FOREIGN KEY (categoriaId) REFERENCES CategoriaDeportiva(id)
);

-- Tabla específica de Calzado Deportivo (Herencia / Extensión)
CREATE TABLE IF NOT EXISTS CalzadoDeportivo (
    productoId INT PRIMARY KEY,
    tallaEu DECIMAL(3,1) NOT NULL,
    tipoSuela VARCHAR(100),
    porcentajeDescuento DECIMAL(3,2) DEFAULT 0.10,
    FOREIGN KEY (productoId) REFERENCES ProductoDeportivo(id) ON DELETE CASCADE
);

-- Tabla específica de Equipamiento Deportivo (Herencia / Extensión)
CREATE TABLE IF NOT EXISTS EquipamientoDeportivo (
    productoId INT PRIMARY KEY,
    deporte VARCHAR(100),
    material VARCHAR(100),
    esProfesional BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (productoId) REFERENCES ProductoDeportivo(id) ON DELETE CASCADE
);

-- INSERT DE DATOS INICIALES EN CATEGORÍAS DEPORTIVAS
INSERT INTO CategoriaDeportiva (id, nombre, iconoSimbolo, descripcion, popularidad) VALUES
(1, 'Fútbol', '⚽', 'Calzado, balones y equipamiento oficial de fútbol.', 5),
(2, 'Baloncesto', '🏀', 'Zapatillas de alto impacto, balones y canastas.', 5),
(3, 'Running / Atletismo', '🏃', 'Zapatillas ultraligeras y accesorios para corredores.', 4),
(4, 'Tenis & Padel', '🎾', 'Raquetas de precisión, raquetbol y pelotas de tenis.', 4),
(5, 'Gimnasio & Fitness', '🏋️', 'Mancuernas, bandas de resistencia y vestimenta.', 5);

-- INSERT DE PRODUCTOS BASE
INSERT INTO ProductoDeportivo (id, nombre, marca, precioBase, stock, descripcion, categoriaId, tipoProducto) VALUES
(101, 'Zapatillas Predator Elite FG', 'Adidas', 189.99, 12, 'Botines de alta precisión para césped natural firme.', 1, 'CALZADO'),
(102, 'Nike Air Zoom GT Cut 3', 'Nike', 169.50, 8, 'Zapatillas de básquetbol con amortiguación Zoom Air.', 2, 'CALZADO'),
(103, 'Asics Gel-Nimbus 26', 'Asics', 150.00, 15, 'Máxima amortiguación para largas distancias en asfalto.', 3, 'CALZADO'),
(201, 'Balón Oficial Champions League 2024', 'Adidas', 140.00, 25, 'Balón de fútbol termosellado con certificación FIFA Quality Pro.', 1, 'EQUIPAMIENTO'),
(202, 'Raqueta Wilson Pro Staff v14', 'Wilson', 260.00, 5, 'Raqueta de control superior utilizada por jugadores de élite.', 4, 'EQUIPAMIENTO'),
(203, 'Set de Mancuernas Hexagonales 20kg', 'PowerGym', 85.00, 10, 'Par de mancuernas recubiertas de goma para mayor durabilidad.', 5, 'EQUIPAMIENTO'),
(204, 'Balón Wilson NBA Official Game', 'Wilson', 175.00, 7, 'Balón de cuero genuino oficial de la NBA.', 2, 'EQUIPAMIENTO');

-- INSERT DE DETALLES DE CALZADO
INSERT INTO CalzadoDeportivo (productoId, tallaEu, tipoSuela, porcentajeDescuento) VALUES
(101, 42.5, 'FG (Firm Ground)', 0.15),
(102, 44.0, 'Goma antideslizante parquet', 0.10),
(103, 41.0, 'AHARPLUS de alta resistencia', 0.05);

-- INSERT DE DETALLES DE EQUIPAMIENTO
INSERT INTO EquipamientoDeportivo (productoId, deporte, material, esProfesional) VALUES
(201, 'Fútbol', 'Cuero Sintético TPU', TRUE),
(202, 'Tenis', 'Grafito de carbono braided', TRUE),
(203, 'Fitness', 'Hierro fundido recubierto', FALSE),
(204, 'Baloncesto', 'Cuero genuino', TRUE);
