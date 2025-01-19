### README - Programa de Búsqueda de Libros 📚

---

## Descripción

Este programa permite buscar y explorar libros a partir de su título, autor, idioma y otros datos relevantes. Es ideal para quienes desean acceder rápidamente a información organizada de libros, incluyendo detalles sobre el autor, como su nombre, fecha de nacimiento y fallecimiento.

El programa utiliza una base de datos que almacena información detallada sobre libros y autores, con soporte para consultas rápidas y eficientes. Gracias a su diseño, asegura una experiencia fluida y confiable para los usuarios.

---

## Características Principales ✨

- **Búsqueda por Título del Libro**: Encuentra libros específicos ingresando su título.
- **Información del Autor**:
  - Nombre completo del autor.
  - Año de nacimiento y fallecimiento (si aplica).
- **Idiomas Disponibles**: Muestra el idioma en que está escrito el libro.
- **Número de Descargas**: Consulta la popularidad de los libros basándote en su cantidad de descargas.

---

## Requisitos del Sistema

- **Java**: Versión 17 o superior.
- **Spring Boot**: Framework utilizado para el backend.
- **Base de Datos**: PostgreSQL.
- **Dependencias Adicionales**:
  - Hibernate (para la gestión de entidades).
  - HikariCP (para el pool de conexiones).
  - Jackson (para el manejo de DTOs).

---

## Instalación

1. **Clona el Repositorio**:
   ```bash
   git clone https://github.com/Elgona9/ProyectoLiterAlura.git
   cd programa-busqueda-libros
   ```

2. **Configura las Variables de Entorno**:
   Asegúrate de configurar las siguientes variables de entorno:
   - `DB_HOST`: Host de la base de datos PostgreSQL.
   - `DB_USER`: Usuario de la base de datos.
   - `DB_PASSWORD`: Contraseña de la base de datos.

3. **Configura el Archivo `application.properties`**:
   Asegúrate de incluir la configuración básica:
   ```properties
   spring.application.name=busqueda-libros
   spring.datasource.url=jdbc:postgresql://${DB_HOST}/libros
   spring.datasource.username=${DB_USER}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Ejecuta el Programa**:
   Compila y ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Uso del Programa

1. **Búsqueda de Libros**:
   El programa ofrece un menú con opciones. Para buscar libros, selecciona la opción correspondiente y explora los resultados.

2. **Visualización de Resultados**:
   Los resultados incluyen:
   - Título del libro.
   - Idioma del libro.
   - Nombre del autor (o "Desconocido" si no se encuentra).
   - Año de nacimiento y fallecimiento del autor (si aplica).
   - Número de descargas.

3. **Ejemplo de Salida**:
   ```plaintext
   -------LIBRO-------
   Título: Don Quijote de la Mancha
   Autor: Miguel de Cervantes
   Idioma: Español
   Número de descargas: 25000
   ```

---

## Estructura del Proyecto

- **Entidades (`model`)**:
  - `Libro`: Representa los libros almacenados en la base de datos.
  - `Autor`: Representa a los autores con sus datos personales.
- **DTOs (`dto`)**:
  - `LibroDTO`: Para transferir datos de los libros.
  - `AutorDTO`: Para transferir datos de los autores.
- **Servicios (`service`)**:
  - `LibroService`: Lógica de negocio para gestionar libros.
- **Repositorios (`repository`)**:
  - `LibroRepository`: Consultas personalizadas a la base de datos.

---

## Autor ✍️

Desarrollado por Elgona9 (David Riveros). Para consultas, escribe a [elgona22@gmail.com]. 😊

--- 

¡Disfruta explorando el mundo de los libros! 📖✨