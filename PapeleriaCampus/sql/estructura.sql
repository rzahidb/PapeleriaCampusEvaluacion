-- PAPELERIA CAMPUS
-- Nombre: Zahid Benjamin Reyes Lopez
-- Fecha: 15/10/2026

-- CREACIÓN DE LA TABLA PRODUCTO
-- Ejecutar dentro de la base papeleria_campus

CREATE TABLE IF NOT EXISTS public.producto
(
    id_producto INTEGER
    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    codigo VARCHAR(10) NOT NULL UNIQUE,

    nombre VARCHAR(80) NOT NULL,

    categoria VARCHAR(20) NOT NULL,

    precio NUMERIC(10,2) NOT NULL,

    stock INTEGER NOT NULL,

    -- Validar código normalizado
    CONSTRAINT chk_producto_codigo
    CHECK (
              char_length(btrim(codigo)) BETWEEN 1 AND 10
    AND codigo = upper(btrim(codigo))
    ),

    -- Validar longitud y espacios del nombre
    CONSTRAINT chk_producto_nombre
    CHECK (
              char_length(btrim(nombre)) BETWEEN 3 AND 80
    AND nombre = btrim(nombre)
    ),

    -- Validar las categorías permitidas
    CONSTRAINT chk_producto_categoria
    CHECK (
              categoria IN (
              'Papelería',
              'Tecnología',
              'Accesorios'
                           )
    ),

    -- Validar precio mayor que cero
    CONSTRAINT chk_producto_precio
    CHECK (precio > 0),

    -- Validar stock no negativo
    CONSTRAINT chk_producto_stock
    CHECK (stock >= 0)
    );