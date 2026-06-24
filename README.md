# Banco API

API REST backend para gestión financiera — administración de clientes, cuentas bancarias, transacciones y usuarios del sistema. Desarrollada como parte de un proceso técnico para una entidad financiera, con enfoque en arquitectura limpia, buenas prácticas de ingeniería y calidad de código.

## Características

- **Gestión de clientes**: CRUD completo con validaciones de mayoría de edad, email y unicidad de identificación.
- **Gestión de cuentas**: Creación de cuentas de ahorro (prefijo `53`) y corriente (prefijo `33`) con número generado automáticamente. Control de estados (ACTIVA, INACTIVA, CANCELADA).
- **Transacciones bancarias**: Consignaciones, retiros y transferencias con cálculo de GMF (Gravamen a los Movimientos Financieros del 0.4%).
- **Autenticación y autorización**: JWT con Spring Security y roles (ADMIN, ASESOR, AUDITOR).
- **Documentación interactiva**: OpenAPI/Swagger UI.
- **Contenedorización**: Docker multi-stage y Docker Compose con healthcheck.
- **Pipeline CI/CD**: GitHub Actions con compilación, tests, análisis SonarCloud y publicación de imagen Docker.
- **Pruebas**: Tests unitarios con JUnit 5 + Mockito y tests de componente con Testcontainers + PostgreSQL real.
- **Cobertura**: JaCoCo para reportes de cobertura + SonarCloud para calidad de código.

## Arquitectura

### Arquitectura Hexagonal (Ports & Adapters)

El proyecto aplica **Arquitectura Hexagonal** con **Vertical Slicing**, donde cada módulo de dominio (`cliente`, `cuenta`, `transaccion`, `usuario`) está aislado con sus propias capas interna y externa.

```
┌─────────────────────────────────────────────────────────────┐
│                      INFRASTRUCTURE                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    INBOUND                             │   │
│  │  Controller (REST)  ·  DTOs  ·  Mappers              │   │
│  └──────────────────────┬───────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼───────────────────────────────┐   │
│  │                   APPLICATION                          │   │
│  │  Use Cases (Casos de uso)  ·  Validators             │   │
│  └──────────────────────┬───────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼───────────────────────────────┐   │
│  │                     DOMAIN                            │   │
│  │  Model (Entidades puras)  ·  Ports (Interfaces)      │   │
│  └──────────────────────┬───────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼───────────────────────────────┐   │
│  │                    OUTBOUND                            │   │
│  │  Adapters  ·  JPA Repositories  ·  Mappers           │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

**Principios clave**:

- **Domain**: Entidades de negocio puras, sin anotaciones de frameworks. Contienen comportamiento (`Cliente.actualizarDatos()`). Las interfaces (puertos) definen los contratos.
- **Application**: Casos de uso que orquestan la lógica de negocio. Dependen de interfaces, nunca de implementaciones concretas.
- **Infrastructure Inbound**: Controladores REST, DTOs de request/response y mappers. Traducen HTTP a llamadas a casos de uso.
- **Infrastructure Outbound**: Adaptadores que implementan los puertos del dominio. Mapean entre entidades de dominio y entidades JPA.

### Flujo de una petición

```
Cliente HTTP → SecurityFilterChain (JWT) → Controller → UseCase → Port → Adapter → JPA Repository → PostgreSQL
```

## Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 4.0.5 (Spring Boot 3.x), Spring MVC, Spring Data JPA, Spring Security |
| Base de datos | PostgreSQL 16 |
| Autenticación | JWT (jjwt 0.12.6, HMAC-SHA) |
| Documentación API | Springdoc OpenAPI 3.0.3 (Swagger UI) |
| Validación | Jakarta Bean Validation |
| Build | Maven Wrapper |
| Testing | JUnit 5, Mockito, Testcontainers 1.20.4, JaCoCo 0.8.14 |
| Calidad | SonarCloud |
| CI/CD | GitHub Actions |
| Contenedores | Docker, Docker Compose |

## Estructura del proyecto

```
bancoBackend/
├── .github/workflows/ci.yml     # Pipeline CI/CD
├── .env.example                   # Variables de entorno de ejemplo
├── Dockerfile                     # Multi-stage build
├── docker-compose.yml             # Orquestación de servicios
├── pom.xml
├── schema.sql                     # DDL de referencia
│
└── src/
    ├── main/java/com/trinity/banco/
    │   ├── BancoApplication.java
    │   │
    │   ├── cliente/                # Módulo: Clientes
    │   │   ├── domain/
    │   │   │   ├── model/Cliente.java
    │   │   │   ├── model/enums/TipoIdentificacion.java
    │   │   │   └── ports/ClienteRepository.java
    │   │   ├── application/
    │   │   │   ├── usecases/       # (Crear, Actualizar, Eliminar, Obtener, Listar)
    │   │   │   └── validators/ClienteValidator.java
    │   │   └── infrastructure/
    │   │       ├── inbound/        # ClienteController, DTOs, Mappers
    │   │       └── outbound/       # Adapter, Entity, JpaRepository, Mappers
    │   │
    │   ├── cuenta/                 # Módulo: Cuentas
    │   │   ├── domain/
    │   │   │   ├── model/Cuenta.java
    │   │   │   ├── model/enums/    # TipoCuenta, EstadoCuenta
    │   │   │   └── ports/CuentaRepository.java
    │   │   ├── application/
    │   │   │   ├── usecases/       # (Crear, Activar, Inactivar, Cancelar, Obtener, Listar)
    │   │   │   ├── validators/CuentaValidator.java
    │   │   │   └── util/NumeroCuentaGenerator.java
    │   │   └── infrastructure/
    │   │       ├── inbound/        # CuentaController, DTOs
    │   │       └── outbound/       # Adapter, Entity, JpaRepository, Mappers
    │   │
    │   ├── transaccion/            # Módulo: Transacciones
    │   │   ├── domain/
    │   │   │   ├── model/Transaccion.java
    │   │   │   ├── model/enums/TipoTransaccion.java
    │   │   │   └── ports/TransaccionRepository.java
    │   │   ├── application/
    │   │   │   ├── usecases/       # (Consignar, Retirar, Transferir, Listar)
    │   │   │   └── util/GmfCalculator.java
    │   │   └── infrastructure/
    │   │       ├── inbound/        # TransaccionController, DTOs, Mappers
    │   │       └── outbound/       # Adapter, JpaRepository, Mappers
    │   │
    │   ├── usuario/                # Módulo: Usuarios del sistema
    │   │   ├── domain/
    │   │   │   ├── model/Usuario.java
    │   │   │   ├── model/enums/Rol.java
    │   │   │   └── ports/UsuarioRepository.java
    │   │   ├── application/
    │   │   │   ├── usecases/       # (Crear, Actualizar, Eliminar, Obtener, Listar, CambiarPassword)
    │   │   │   └── validators/UsuarioValidator.java
    │   │   └── infrastructure/
    │   │       ├── inbound/        # UsuarioController, DTOs, Mappers
    │   │       └── outbound/       # Adapter, Entity, JpaRepository, Mappers
    │   │
    │   └── shared/                 # Compartido entre módulos
    │       ├── domain/errors/      # ApiError, RecursoNoEncontradoException
    │       └── infrastructure/
    │           ├── config/         # BeanConfiguration, CorsConfig, OpenApiConfig
    │           ├── security/       # SecurityConfig, JwtProvider, JwtAuthFilter,
    │           │                   # AuthController, CustomUserDetailsService,
    │           │                   # DataInitializer, DTOs
    │           └── GlobalExceptionHandler.java
    │
    └── test/java/com/trinity/banco/
        ├── AbstractContainerConfig.java
        ├── BancoApplicationTests.java
        ├── component/               # Tests de componente (integración)
        │   ├── AbstractBaseIntegrationTest.java
        │   ├── TestDataFactory.java
        │   ├── cliente/ClienteComponentTest.java
        │   ├── cuenta/CuentaComponentTest.java
        │   ├── transaccion/TransaccionComponentTest.java
        │   ├── usuario/UsuarioComponentTest.java
        │   └── security/AuthComponentTest.java
        └── unit/                    # Tests unitarios
            ├── cliente/ (controller/ + usecase/)
            ├── cuenta/ (controller/ + usecase/)
            ├── transaccion/ (controller/ + usecase/)
            ├── usuario/ (usecase/)
            ├── security/JwtProviderTest.java
            └── shared/GlobalExceptionHandlerTest.java
```

## Configuración

### Variables de entorno

Copia el archivo de ejemplo y ajusta los valores:

```bash
cp .env.example .env
```

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `dev` o `prod` |
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `banco` |
| `DB_USER` | Usuario de BD | `postgres` |
| `DB_PASSWORD` | Contraseña de BD | — |
| `DB_URL` | URL JDBC completa (uso en Docker) | `jdbc:postgresql://db:5432/banco` |
| `JWT_SECRET` | Clave secreta para firmar JWT (mínimo 256 bits) | — |
| `JWT_EXPIRATION` | Duración del token en ms | `86400000` (24h) |
| `CORS_ALLOWED_ORIGINS` | Orígenes CORS permitidos | `http://localhost:5173` |

### Perfiles

- **`dev`** (default): `ddl-auto: update`, SQL visible en logs, `DataInitializer` crea usuario admin (`admin`/`admin123`).
- **`prod`**: `ddl-auto: validate`, pool HikariCP optimizado, SQL oculto.

## Ejecución local

### Requisitos

- Java 17+ (JDK)
- PostgreSQL 16 (o Docker Desktop)
- Maven (opcional, usar `mvnw`)

### Con Docker

```bash
# Iniciar API + PostgreSQL
docker compose up

# O en background
docker compose up -d

# Ver logs
docker compose logs -f api
```

La API estará disponible en `http://localhost:8080`.

### Sin Docker

```bash
# 1. Asegúrate de tener PostgreSQL corriendo en localhost

# 2. Configura .env con tus credenciales

# 3. Ejecutar con Maven Wrapper
./mvnw spring-boot:run

# O compilar y ejecutar el JAR
./mvnw clean package -DskipTests
java -jar target/banco-0.0.1-SNAPSHOT.jar
```

### Usuario inicial (perfil dev)

Al iniciar con perfil `dev`, se crea automáticamente:

- **Usuario**: `admin`
- **Contraseña**: `admin123`
- **Rol**: ADMIN

## Documentación API

La documentación interactiva (Swagger UI) está disponible en:

```
http://localhost:8080/swagger-ui.html
```

El endpoint OpenAPI:

```
http://localhost:8080/v3/api-docs
```

### Endpoints principales

| Método | Ruta | Descripción | Roles |
|--------|------|-------------|-------|
| `POST` | `/auth/login` | Iniciar sesión | Público |
| `POST` | `/clientes` | Crear cliente | ADMIN, ASESOR |
| `GET` | `/clientes` | Listar clientes | ADMIN, ASESOR, AUDITOR |
| `GET` | `/clientes/{id}` | Obtener cliente | ADMIN, ASESOR, AUDITOR |
| `PUT` | `/clientes/{id}` | Actualizar cliente | ADMIN, ASESOR |
| `DELETE` | `/clientes/{id}` | Eliminar cliente | ADMIN, ASESOR |
| `POST` | `/cuentas` | Crear cuenta | ADMIN, ASESOR |
| `GET` | `/cuentas` | Listar cuentas | ADMIN, ASESOR, AUDITOR |
| `GET` | `/cuentas/{numero}` | Obtener cuenta | ADMIN, ASESOR, AUDITOR |
| `GET` | `/cuentas/cliente/{id}` | Cuentas por cliente | ADMIN, ASESOR, AUDITOR |
| `PATCH` | `/cuentas/{numero}/activar` | Activar cuenta | ADMIN, ASESOR |
| `PATCH` | `/cuentas/{numero}/inactivar` | Inactivar cuenta | ADMIN, ASESOR |
| `PATCH` | `/cuentas/{numero}/cancelar` | Cancelar cuenta | ADMIN, ASESOR |
| `POST` | `/transacciones/consignar` | Consignar | ADMIN, ASESOR |
| `POST` | `/transacciones/retirar` | Retirar | ADMIN, ASESOR |
| `POST` | `/transacciones/transferir` | Transferir | ADMIN, ASESOR |
| `GET` | `/transacciones/cuenta/{numero}` | Historial transacciones | ADMIN, ASESOR, AUDITOR |
| `POST` | `/usuarios` | Crear usuario | ADMIN |
| `GET` | `/usuarios` | Listar usuarios | ADMIN |
| `PUT` | `/usuarios/{id}` | Actualizar usuario | ADMIN |
| `DELETE` | `/usuarios/{id}` | Eliminar usuario | ADMIN |
| `PATCH` | `/usuarios/{id}/password` | Cambiar contraseña | ADMIN |

## Seguridad

### Autenticación JWT

1. El cliente envía `POST /auth/login` con `username` y `password`.
2. Spring Security autentica contra la base de datos (passwords con BCrypt).
3. Se genera un token JWT firmado con HMAC-SHA que incluye `subject=username` y `claim=rol`.
4. El cliente debe enviar el token en cada petición: `Authorization: Bearer <token>`.
5. `JwtAuthenticationFilter` intercepta cada request, valida el token y establece el contexto de seguridad.

### Roles y permisos

| Rol | Clientes | Cuentas | Transacciones | Usuarios |
|-----|----------|---------|---------------|----------|
| ADMIN | CRUD | CRUD | CRUD | CRUD |
| ASESOR | CRUD | CRUD | CRUD | — |
| AUDITOR | Lectura | Lectura | Lectura | — |

Las rutas públicas son `/auth/**`, `/swagger-ui/**` y `/v3/api-docs/**`.

### Manejo de errores

`GlobalExceptionHandler` con `@RestControllerAdvice` captura y estructura todos los errores:

```json
{
  "status": 400,
  "message": "El cliente debe ser mayor de edad",
  "timestamp": "2025-01-15T10:30:00"
}
```

Casos manejados: validación de entrada (`@Valid`), errores de parsing JSON, tipos incorrectos, credenciales inválidas, acceso denegado, recursos no encontrados, reglas de negocio (RuntimeException) y errores internos.

## Testing

### Tests unitarios

34 clases de prueba que cubren casos de uso y controladores de todos los módulos.

- **Herramientas**: JUnit 5, Mockito, `@WebMvcTest` (controladores), `@InjectMocks` + `@Mock` (casos de uso).
- **Qué prueban**: Lógica de negocio, validaciones, edge cases, HTTP status codes y serialización JSON.
- **Velocidad**: Instantáneas (no levantan Spring ni BD).

```
src/test/java/.../unit/
├── cliente/controller/ClienteControllerTest.java
├── cliente/usecase/  (Crear, Actualizar, Eliminar, Obtener, Listar, Validator, Edge)
├── cuenta/controller/CuentaControllerTest.java
├── cuenta/usecase/   (Crear, Activar, Inactivar, Cancelar+Edge, Obtener, Listar, Validator)
├── transaccion/controller/TransaccionControllerTest.java
├── transaccion/usecase/ (Consignar+Edge, Retirar, Transferir, Listar, GmfCalculator)
├── usuario/usecase/  (Crear, Actualizar, Eliminar, Obtener, Listar, CambiarPassword, Validator)
├── security/JwtProviderTest.java
└── shared/GlobalExceptionHandlerTest.java
```

### Tests de componente (integración)

6 clases de prueba que validan el sistema completo contra PostgreSQL real.

- **Herramientas**: `@SpringBootTest`, `@AutoConfigureMockMvc`, Testcontainers, `@Transactional`
- **Infraestructura**:
  - `AbstractContainerConfig`: Inicia contenedor PostgreSQL 16 Alpine vía Testcontainers.
  - `AbstractBaseIntegrationTest`: Clase base con MockMvc, ObjectMapper y helpers de autenticación.
  - `TestDataFactory`: Generadores de JSON para requests.
- **Qué prueban**: Flujo completo controller → use case → adapter → BD real, incluyendo seguridad JWT real, reglas de negocio, constraints de BD y roles.

```
src/test/java/.../component/
├── cliente/ClienteComponentTest.java
├── cuenta/CuentaComponentTest.java
├── transaccion/TransaccionComponentTest.java
├── usuario/UsuarioComponentTest.java
└── security/AuthComponentTest.java
```

### Cobertura

JaCoCo genera reportes de cobertura en la fase `verify`. Integrado con SonarCloud para tracking histórico.

```bash
./mvnw clean verify
```

El reporte HTML se genera en `target/site/jacoco/index.html`.

## Calidad de código

### SonarCloud

El proyecto está configurado para análisis estático con SonarCloud en cada push/PR.

- **Org**: `ve312`
- **Project key**: `ve312_banco`
- **Host**: `https://sonarcloud.io`

Ejecución manual:

```bash
./mvnw sonar:sonar -Dsonar.token=$SONAR_TOKEN
```

El pipeline de CI ejecuta automáticamente el análisis después de los tests.

## CI/CD

### GitHub Actions

El pipeline `Banking API CI` (`.github/workflows/ci.yml`) ejecuta:

| Trigger | Acción |
|---------|--------|
| Push a `develop`, `feature/**` | Build + Test + SonarCloud |
| PR a `main`, `develop` | Build + Test + SonarCloud |
| Push a `main` | Build + Test + SonarCloud + Push Docker Image |

**Jobs**:

1. **quality**: Compila, ejecuta tests con `verify`, genera cobertura JaCoCo, ejecuta análisis SonarCloud y sube artefactos (reportes, JAR).
2. **docker** (solo `main`): Construye la imagen Docker y la publica en DockerHub (multi-stage build).

### Docker multi-stage

El `Dockerfile` usa dos etapas:

1. **Build** (`maven:3.9-eclipse-temurin-17`): Compila el proyecto con Maven.
2. **Run** (`eclipse-temurin:17-jre-alpine`): Imagen mínima con JRE, usuario no-root, flags de contenedor JVM.

## Reglas de negocio

| Regla | Implementación |
|-------|----------------|
| Mayoría de edad (18+) | `CrearClienteUseCase` — validación contra `LocalDate.now()` |
| Email único | `ClienteRepository.existePorEmail()` |
| Identificación única | `ClienteRepository.existePorNumeroIdentificacion()` |
| Número de cuenta genera automáticamente | AHORROS → prefijo `53`, CORRIENTE → prefijo `33`, 10 dígitos total |
| Solo cuentas activas pueden operar | `CuentaValidator.validarCuentaActiva()` |
| Ahorros no puede quedar en negativo | `CuentaValidator.validarSaldoDisponible()` |
| Cancelar cuenta solo con saldo $0 | `CancelarCuentaUseCase` |
| GMF del 0.4% en retiros y transferencias | `GmfCalculator` |
| Transferencias mismo cliente sin GMF | `TransferirUseCase` |
| Eliminar cliente solo sin cuentas | `EliminarClienteUseCase` |
| Una cuenta exenta de GMF por cliente | `CrearCuentaUseCase` |

## Posibles mejoras futuras

- **Locking optimista**: Agregar `@Version` en `CuentaEntity` para evitar inconsistencias en transferencias concurrentes.
- **Excepciones de dominio**: Reemplazar `RuntimeException` por excepciones personalizadas (`SaldoInsuficienteException`, `CuentaNoActivaException`, etc.).
- **Paginación**: Agregar `Pageable` de Spring Data en listados (`GET /clientes`, `GET /transacciones`).
- **Refresh tokens JWT**: Endpoint para renovar tokens sin reautenticar.
- **Rate limiting**: Proteger `/auth/login` contra fuerza bruta (Bucket4j o similar).
- **Índices en BD**: Agregar índices en `cuentas(cliente_id)` y `transacciones(numero_cuenta, fecha)`.
- **Pruebas de concurrencia**: Simular escenarios de alta concurrencia en transferencias.
- **Health checks**: Spring Boot Actuator para monitoreo en producción.
- **Logs estructurados**: Formato JSON para ingestión en sistemas de logging.
- **Caché**: Almacenar en caché consultas frecuentes de `NumeroCuentaGenerator`.

---

Desarrollado por [Daniel Felipe Ordoñez Amaya](https://github.com/ve312)
