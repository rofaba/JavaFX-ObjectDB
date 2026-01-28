🎬 Catálogo de Copias de Películas
Aplicación de escritorio desarrollada en JavaFX + Object DB para gestionar un catálogo de copias de películas.  
Permite visualizar información detallada, imágenes locales y administrar los registros desde una interfaz gráfica moderna.

🚀 Características
Interfaz desarrollada completamente con FXML + JavaFX
Carga de imágenes locales para cada película
Vista de detalle con diseño limpio y fondos personalizados
Tabla principal con ordenamiento y selección
Botones que se habilitan/deshabilitan automáticamente
Integración local para persistencia en BD utilizando ObjectDB
Barra de búsqueda funcional para perfil administrador
CRUD mediante la interfaz

🛠️ Tecnologías utilizadas
Java 17+
JavaFX
JPA - ObjectDB
Maven
CSS para estilos

📂 Estructura del proyecto
src/main/java/org/example/javafx_objectdb/
config/
config/
dao/
entity/
seed/
service/
ui.controller/

src/main/resources/
images/
fondos/
peliculas/

🖥️ Vistas principales

MainView: tabla de copias con barra superior personalizada
DetalleCopiaView: vista de detalles con imagen y fondo translúcido

▶️ Ejecución

Puedes ejecutar desde tu IDE (IntelliJ) o compilar con Maven y ejecutar el JAR generado.
Credenciales de Prueba: 
Admin/1234
user/1234
user2/1234