# franquicias-api

API REST con **Spring Boot 3.3.5 / Java 21** y JPA. Todos los endpoints reciben un objeto JSON por `@RequestBody`; no usan parametros de ruta.

## Ambientes: base antigua para pruebas y base nueva aprovisionada por IaC

| Ambiente | API | PostgreSQL | Conexion |
|---|---|---|---|
| Local | IntelliJ o `docker compose` | Base **antigua** de Render | URL **externa** JDBC con `sslmode=require` |
| Render (IaC) | Servicio **nuevo** `franquicias-api-iac` | Base **nueva** `franquicias-db-iac` | Host **interno** obtenido de `fromDatabase` |

No se crea ninguna base de datos al ejecutar el `docker-compose.yml` local.

### 1. Ejecutar en local (usando la base antigua de Render)

1. Copia `.env.example` a `.env` en la raiz: en PowerShell, `Copy-Item .env.example .env`.
2. Consulta los datos **externos** de conexion de la base antigua en Render, y reemplaza los valores de `.env`.
3. Configura una nueva contrasena en Render si la anterior fue compartida o filtrada.
4. Ejecuta:

```powershell
docker compose up -d --build
docker compose logs -f app
```

La API se expone en `http://localhost:8080` y usa `ddl-auto=validate` para no modificar las tablas de pruebas.

Para ejecutar desde IntelliJ, configura las variables `SPRING_DATASOURCE_URL` (JDBC externa con SSL),
`SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` en la configuracion de ejecucion.
El archivo `.env` lo lee Docker Compose; IntelliJ no lo carga automaticamente.

### 2. Crear la nueva base + nueva API mediante Render Blueprint

> **Limitacion del plan gratuito:** Render admite solamente una base PostgreSQL Free activa por workspace.
> Para conservar la base antigua Free y crear otra Free, crea **un segundo workspace Hobby**.
> Si quieres usar el mismo workspace, cambia el plan de la nueva base a uno de pago antes del aprovisionamiento.

1. Sube `render.yaml` junto con el codigo a GitHub (no subas `.env`).
2. En Render, selecciona el **workspace nuevo** (no el que contiene la base antigua).
3. Entra en `New > Blueprint`, conecta este repositorio y la rama deseada.
4. Revisa que Render vaya a crear **exactamente** `franquicias-api-iac` y `franquicias-db-iac`.
5. Confirma `Deploy Blueprint` y revisa los logs del servicio web.
6. Consulta la URL publica del nuevo servicio y abre `/swagger-ui.html`.

La configuracion `render.yaml` genera las credenciales automaticamente y las suministra a la API
con `fromDatabase`. El perfil `render` usa el host/puerto/base internos de Render para construir
una URL JDBC sin almacenar contrasenas en Git. La API se conecta a la **nueva** base, no a la antigua.

`SPRING_JPA_HIBERNATE_DDL_AUTO=update` se utiliza solamente para inicializar tablas en esta base nueva
vacía de demostracion. Para sistemas reales, utiliza Flyway/Liquibase y cambia a `validate`.

**No elimines la base antigua**: permanece en el workspace original y se usa para desarrollo local.
Las bases Free de Render expiran 30 dias despues de su creacion y no tienen backups gestionados.

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
