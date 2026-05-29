# GamarraLoop Backend API

Este repositorio contiene el código fuente del backend para **GamarraLoop**, una plataforma móvil B2C diseñada para conectar talleres de confección con artesanos textiles en el emporio comercial de Gamarra (Lima, Perú), fomentando la economía circular mediante la reutilización de retazos de tela.

## 🚀 Tecnologías Principales
- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3.4.0
- **Base de Datos:** PostgreSQL (alojado en Supabase)
- **ORM:** Hibernate / Spring Data JPA
- **Inteligencia Artificial:** Google Cloud Vision API (Machine Learning para clasificación de textiles)
- **Documentación de API:** Swagger / OpenAPI 3

## 🏗 Arquitectura
El proyecto fue construido siguiendo estrictos estándares académicos y de la industria:
- **Domain-Driven Design (DDD):** El código está dividido en 7 Bounded Contexts independientes.
- **Arquitectura Hexagonal (Ports & Adapters):** Clara separación entre el dominio de negocio, los casos de uso (aplicación) y la infraestructura.
- **RESTful API:** Controladores estructurados con recursos DTOs y ensambladores.
- **API Gateway Pattern / Method Override:** Implementación de un filtro HTTP (`X-HTTP-Method-Override`) para enrutamiento de peticiones avanzadas, compatible con las limitaciones de plataformas Low-Code (ej. FlutterFlow).

## 📦 Bounded Contexts (Módulos)
El monolito modular está dividido en:
1. **User Profiles:** Gestión de roles (Artesano, Confeccionista) y datos de usuario.
2. **Lots:** Creación y publicación de lotes de retazos textiles.
3. **Classification (IA):** Integración con Google Cloud Vision para analizar fotos de telas y extraer etiquetas automáticas.
4. **Reservations:** Lógica de reservas de lotes con expiración automática.
5. **Delivery:** Seguimiento de la entrega y recepción de los textiles.
6. **Notifications:** Sistema de alertas para los usuarios.
7. **Expiration (Scheduler):** Tarea programada en segundo plano que cancela reservas vencidas.

## ⚙️ Configuración y Ejecución Local

### Prerrequisitos
- Java 21 JDK instalado.
- Maven (Opcional, se incluye el wrapper `mvnw`).
- Cuenta en Google Cloud con Vision API habilitada.

### Variables de Entorno (Environment Variables)
Antes de arrancar el servidor, debes configurar las siguientes variables en tu sistema (ejemplo en PowerShell):
```powershell
$env:DATABASE_URL="jdbc:postgresql://tu-host-de-supabase.com:5432/postgres"
$env:DATABASE_USER="tu_usuario"
$env:DATABASE_PASSWORD="tu_password"
$env:VISION_API_KEY="AIzaSy...TU_CLAVE_DE_GOOGLE..."
```

### Arrancar el Servidor
En la raíz del proyecto, ejecuta:
```powershell
# En Windows:
.\mvnw.cmd spring-boot:run

# En Mac/Linux:
./mvnw spring-boot:run
```

## 📖 Documentación de la API (Swagger)
Una vez que el servidor esté corriendo en tu máquina local, puedes explorar y probar todos los endpoints gráficamente ingresando a:
`http://localhost:8080/api/v1/swagger-ui/index.html`
