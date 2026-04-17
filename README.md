# Backend Bancario - Prueba Técnica

## Descripción

Este proyecto consiste en el desarrollo de un backend para una entidad financiera que permite la gestión de:

* Clientes
* Productos financieros (cuentas)
* Transacciones (consignaciones, retiros y transferencias)

El sistema fue diseñado priorizando claridad del dominio, buenas prácticas de arquitectura y una estructura escalable.

---

## Arquitectura

Se implementó una arquitectura hexagonal (Ports & Adapters) con separación clara de responsabilidades:

```text
com.trinity.banco
├── domain          → núcleo del negocio
├── application     → casos de uso
├── infrastructure  → persistencia y adaptadores
├── rest            → exposición de API REST
├── config          → configuración de beans
```

---

## Estructura del Proyecto

```text
src/main/java/com/trinity/banco
├── application
│   ├── service
│   │   ├── cliente
│   │   ├── cuenta
│   │   └── transaccion
│   └── validator
│
├── domain
│   ├── model
│   ├── enums
│   ├── ports
│   └── Service (NumeroCuentaGenerator)
│
├── infrastructure
│   ├── entity
│   ├── repository (JPA)
│   └── adapters
│
├── rest
│   ├── controller
│   ├── dto (request/response)
│   ├── mapper
│   └── exceptions
│
└── config
```

---

## Decisiones de Diseño

### Dominio con comportamiento

Las entidades contienen comportamiento:

* `Cuenta.depositar()`
* `Cuenta.retirar()`
* `Cuenta.cancelar()`
* `Cuenta.activar()` / `inactivar()`

Esto centraliza reglas de negocio dentro del dominio.

---

### Separación por capas

* Controller → manejo HTTP
* Service (UseCase) → orquestación
* Domain → lógica de negocio
* Repository (Port) → contratos
* Adapter → implementación

---

### Manejo de dinero

Se utiliza `BigDecimal` para evitar errores de precisión en operaciones financieras.

---

### Identificadores como String

* número de cuenta
* número de identificación

Se manejan como `String` ya que no representan valores matemáticos y pueden tener formato específico.

---

### Generación de número de cuenta

Se implementó un servicio de dominio:

* 10 dígitos
* AHORROS → inicia en `53`
* CORRIENTE → inicia en `33`
* único

---

### Modelado de transacciones

Las transferencias se implementan como dos transacciones independientes:

* Débito en cuenta origen
* Crédito en cuenta destino

Esto garantiza trazabilidad y consistencia.

---

### Validaciones por capas

* DTO → validaciones de formato (`@Valid`, `@Pattern`, etc.)
* Service → reglas de negocio
* Domain → invariantes

---

### Manejo global de errores

Se implementó:

* `@RestControllerAdvice`
* clase `ApiError`

Incluye manejo de:

* errores de validación (`@Valid`)
* errores de parsing JSON (`HttpMessageNotReadableException`)
* errores de tipo (`MethodArgumentTypeMismatchException`)
* recursos no encontrados (`RecursoNoEncontradoException`)

---

### Reglas de negocio adicionales

Se incluyeron reglas no explícitas en el enunciado:

* Solo cuentas activas pueden realizar transacciones
* Cuentas canceladas no pueden operar
* Validaciones de formato en inputs

---

## Funcionalidades

### Clientes

* Crear cliente (mayor de edad)
* Actualizar cliente
* Eliminar cliente (si no tiene productos)
* Obtener cliente
* Listar clientes

---

### Cuentas

* Crear cuenta (ahorros / corriente)
* Activar / Inactivar
* Cancelar (saldo = 0)
* Listar cuentas por cliente
* Obtener cuenta

---

### Transacciones

* Consignación
* Retiro
* Transferencia entre cuentas
* Listar transacciones por cuenta

---

## Endpoints principales

### Clientes

```http
POST   /clientes
PUT    /clientes/{id}
DELETE /clientes/{id}
GET    /clientes
GET    /clientes/{id}
```

---

### Cuentas

```http
POST   /cuentas
PATCH  /cuentas/{numero}/activar
PATCH  /cuentas/{numero}/inactivar
PATCH  /cuentas/{numero}/cancelar
GET    /cuentas/cliente/{clienteId}
GET    /cuentas/{numero}
```

---

### Transacciones

```http
POST /transacciones/consignar
POST /transacciones/retirar
POST /transacciones/transferir
GET  /transacciones/{numeroCuenta}
```

---

## Pruebas

Se implementaron pruebas unitarias para:

* capa de servicios
* controladores REST

Utilizando:

* JUnit 5
* Mockito

Cobertura enfocada en lógica de negocio y endpoints.

---

## Ejecución del proyecto

```bash
mvn spring-boot:run
```

---

## Configuración

Archivo:

```text
src/main/resources/application.yaml
```

Configurar conexión a base de datos según entorno.

---

## Consideraciones y posibles mejoras

Durante el desarrollo se identificaron algunos puntos que podrían implementarse dependiendo del contexto del sistema:

* Validación de unicidad del correo electrónico a nivel de negocio
* Eliminación del uso de anotaciones como `@Service` en la capa application para una adherencia más estricta a arquitectura hexagonal
* Ampliación de funcionalidades de consulta, como listados más avanzados de transacciones

Adicionalmente, se implementó la funcionalidad de listar transacciones por número de cuenta, la cual no estaba explícitamente solicitada en la prueba, pero aporta valor al sistema al permitir trazabilidad de movimientos.

---

## Consideraciones finales

Este proyecto fue desarrollado con enfoque en:

* diseño orientado a dominio
* separación de responsabilidades
* buenas prácticas de arquitectura
* código limpio y mantenible

Se priorizó una solución cercana a un entorno productivo real más allá de cumplir únicamente los requerimientos mínimos.

---
Desarrollado por: [Daniel Felipe Ordoñez Amaya]