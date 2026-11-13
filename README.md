# franquicias-api

API REST con **Spring Boot 3.3.5 / Java 17** y JPA. Todos los endpoints reciben un objeto JSON por `@RequestBody`; no usan parametros de ruta.

## Ejecutar localmente

Configura `src/main/resources/application.properties` con la conexion a la base de datos, y ejecuta:

```bash
mvn clean spring-boot:run
```

La URL de ejemplo es `http://localhost:8080` (verifica el puerto configurado).

## Endpoints

| Metodo | Ruta | Funcion |
|---|---|---|
| POST | `/api/franquicias/save` | Crear franquicia |
| POST | `/api/franquicias/update` | Actualizar franquicia |
| POST | `/api/sucursales/save` | Crear sucursal |
| POST | `/api/sucursales/update` | Actualizar sucursal |
| POST | `/api/productos/save` | Crear producto |
| POST | `/api/productos/update-stock` | Actualizar stock |
| POST | `/api/productos/update-nombre` | Actualizar nombre del producto |
| POST | `/api/franquicias/productos/max-stock` | Mayor stock por sucursal |
| POST | `/api/productos/delete` | Eliminar producto |

Se usa `POST` tambien para eliminar y consultar porque se requiere que **todos** los IDs viajen en JSON y no en parametros de URL. `DELETE` con body y `GET` con body pueden no ser compatibles con ciertos clientes/proxies.

Al crear un recurso, reemplaza los IDs `1` por los IDs devueltos en la respuesta.

## Ejemplos cURL

### Crear franquicia

```bash
curl --location 'http://localhost:8080/api/franquicias/save' \
--header 'Content-Type: application/json' \
--data '{
  "nombre": "Franquicia Nacional"
}'
```

### Actualizar franquicia

```bash
curl --location 'http://localhost:8080/api/franquicias/update' \
--header 'Content-Type: application/json' \
--data '{
  "id": 1,
  "nombre": "Franquicia Nacional Actualizada"
}'
```

### Crear sucursal

```bash
curl --location 'http://localhost:8080/api/sucursales/save' \
--header 'Content-Type: application/json' \
--data '{
  "franquiciaId": 1,
  "nombre": "Sucursal Bucaramanga"
}'
```

### Actualizar sucursal

```bash
curl --location 'http://localhost:8080/api/sucursales/update' \
--header 'Content-Type: application/json' \
--data '{
  "franquiciaId": 1,
  "sucursalId": 1,
  "nombre": "Sucursal Centro"
}'
```

### Crear producto

```bash
curl --location 'http://localhost:8080/api/productos/save' \
--header 'Content-Type: application/json' \
--data '{
  "sucursalId": 1,
  "nombre": "Arroz Diana 500g",
  "stock": 150
}'
```

### Actualizar stock

```bash
curl --location 'http://localhost:8080/api/productos/update-stock' \
--header 'Content-Type: application/json' \
--data '{
  "sucursalId": 1,
  "productoId": 1,
  "stock": 300
}'
```

### Actualizar nombre del producto

```bash
curl --location 'http://localhost:8080/api/productos/update-nombre' \
--header 'Content-Type: application/json' \
--data '{
  "sucursalId": 1,
  "productoId": 1,
  "nombre": "Arroz Diana Premium"
}'
```

### Mayor stock por sucursal

```bash
curl --location 'http://localhost:8080/api/franquicias/productos/max-stock' \
--header 'Content-Type: application/json' \
--data '{
  "franquiciaId": 1
}'
```

### Eliminar producto

```bash
curl --location 'http://localhost:8080/api/productos/delete' \
--header 'Content-Type: application/json' \
--data '{
  "sucursalId": 1,
  "productoId": 1
}'
```

## Comportamiento

- Crear franquicia, sucursal o producto: HTTP 201.
- Actualizar: HTTP 200.
- Eliminar producto: HTTP 204.
- Consultar mayor stock por sucursal: HTTP 200 y una lista con sucursal, producto y stock (pueden salir empates).
- Las validaciones `@Valid` rechazan IDs obligatorios nulos, nombres vacios y stock negativo.
- Se conserva la logica de servicios y repositorios del proyecto anterior.

## Nota del IDE

Abre el archivo `pom.xml` en IntelliJ para importar el proyecto Maven. No se incluyen archivos locales `.idea`.
