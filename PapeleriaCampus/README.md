
# PAPELERÍA CAMPUS
### Laboratorio CRUD — JavaFX y PostgreSQL

## 1. Autor

**Estudiante:** Zahid Benjamín Reyes López  
**Asignatura:** Programación de Aplicaciones de Escritorio  
**Fecha:** 15/10/2026

---

## 2. Versiones utilizadas

- **Java:** 21
- **JavaFX:** 21.0.6
- **PostgreSQL:** 18
- **pgAdmin:** 4
- **IDE:** IntelliJ IDEA
- **Gestor de dependencias:** Maven
- **PostgreSQL JDBC:** 42.7.14

---

## 3. Dependencias

El proyecto utiliza **Maven** para administrar las siguientes dependencias:

- **javafx-controls:** permite utilizar los componentes gráficos de JavaFX, como TableView, Button, TextField y ComboBox.
- **javafx-fxml:** permite cargar la interfaz gráfica desarrollada en FXML.
- **postgresql:** permite establecer la conexión JDBC entre Java y PostgreSQL.

---

## 4. Configuración

La aplicación utiliza la base de datos **papeleria_campus**, creada en PostgreSQL 18.

**Base de datos:** `papeleria_campus`  
**Esquema:** `public`  
**Tabla:** `producto`

### 4.1 Creación de la base de datos

1. Abrir pgAdmin 4 y conectarse al servidor PostgreSQL.
2. Abrir Query Tool desde la base de datos `postgres`.
3. Ejecutar la siguiente instrucción:

```sql
CREATE DATABASE papeleria_campus;
```

4. Seleccionar la base de datos `papeleria_campus` y abrir un nuevo Query Tool.
5. Ejecutar el archivo `sql/estructura.sql` para crear la tabla producto y sus restricciones.
6. Ejecutar el archivo `sql/datos.sql` para insertar los 12 productos iniciales.

### 4.2 Restauración de los datos iniciales

Para restaurar los 12 productos originales en la base local de pruebas, ejecutar:

```sql
TRUNCATE TABLE public.producto RESTART IDENTITY;
```

Posteriormente, ejecutar nuevamente el archivo `sql/datos.sql`.

**Importante:** este procedimiento elimina los registros existentes y debe realizarse únicamente en la base de datos local de pruebas.

### 4.3 Configuración de la conexión

La conexión con PostgreSQL se realiza mediante la clase `DataBaseConnection.java`, utilizando JDBC y `DriverManager.getConnection()`.

Las credenciales se almacenan en un archivo local llamado `db.properties`.

Para configurarlo:

1. Copiar el archivo `db.properties.example`.
2. Renombrar la copia como `db.properties`.
3. Colocarlo en la carpeta principal del proyecto, junto a `pom.xml`.
4. Configurar los siguientes datos:

```properties
url=jdbc:postgresql://localhost:5432/papeleria_campus
user=postgres
password=TU_CONTRASEÑA
```

El archivo `db.properties` está excluido del repositorio mediante `.gitignore`, para evitar publicar las credenciales reales.

---

## 5. Ejecución

Para ejecutar la aplicación desde IntelliJ IDEA:

1. Abrir IntelliJ IDEA.
2. Seleccionar el proyecto `PapeleriaCampus`.
3. Configurar Java 21 como JDK.
4. Cargar las dependencias del archivo `pom.xml`.
5. Verificar que PostgreSQL esté funcionando.
6. Configurar el archivo local `db.properties`.
7. Ingresar a **Run → Edit Configurations**.
8. Establecer como **Working directory** la carpeta `PapeleriaCampus`.
9. Ejecutar la clase `HelloApplication.java`.

También se puede ejecutar desde la terminal, ubicada en la carpeta del proyecto, mediante:

```bash
mvn javafx:run
```

---

## 6. Organización de clases

El proyecto está organizado en los siguientes archivos y paquetes:

### HelloApplication.java

Clase principal encargada de iniciar la aplicación JavaFX y cargar el formulario `producto-view.fxml`.

### DataBaseConnection.java

Clase encargada de establecer la conexión con la base de datos PostgreSQL mediante JDBC.

### Producto.java

Clase modelo que representa los productos mediante seis atributos:

- ID del producto.
- Código.
- Nombre.
- Categoría.
- Precio.
- Stock.

### ProductoDAO.java

Clase encargada del acceso a datos. Contiene las consultas SQL necesarias para realizar las operaciones CRUD y las búsquedas utilizando `PreparedStatement`.

### ProductoController.java

Controlador encargado de gestionar los componentes del formulario, las validaciones, los botones y la visualización de los productos en el TableView.

### producto-view.fxml

Archivo que contiene la interfaz gráfica de la aplicación, incluyendo el formulario, el TableView, los filtros y los botones.

### Flujo de información

**FXML → ProductoController → ProductoDAO → PostgreSQL**

Los resultados obtenidos desde PostgreSQL se convierten en objetos Producto, se almacenan en una ObservableList y se muestran en el TableView.

---

## 7. Fuentes de apoyo

- Guía de laboratorio *Acceso a datos y visualización en TableView*, proporcionada por el docente.
- Documentación oficial de JavaFX: https://openjfx.io/
- Documentación oficial de PostgreSQL: https://www.postgresql.org/docs/
- Documentación oficial de PostgreSQL JDBC: https://jdbc.postgresql.org/
- Materiales y ejemplos desarrollados durante las clases.
- ProyectoCRUD anterior como referencia de implementación.
