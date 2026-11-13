# Franquicias API

API REST para la gestión de **franquicias, sucursales y productos**, desarrollada con Spring Boot, Java y PostgreSQL. Incluye documentación interactiva con Swagger/OpenAPI, empaquetado con Docker y una definición de infraestructura como código (**IaC**) para Render.

> **Ambientes y persistencia:** el proyecto utiliza **dos bases de datos PostgreSQL independientes**: una para el **ambiente de desarrollo y pruebas locales** (Maven, IntelliJ o Docker Compose) y otra para el **ambiente desplegado en Render**, gestionado mediante infraestructura como código (`render.yaml`). Docker Compose ejecuta únicamente la API; no crea un contenedor de PostgreSQL.

## Tecnologías

| Componente | Tecnología |
| --- | --- |
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Construcción | Maven |
| API | Spring Web / REST |
| Persistencia | Spring Data JPA, Hibernate y PostgreSQL |
| Mapeo y utilidades | MapStruct y Lombok |
| Validación | Jakarta Bean Validation |
| Documentación de API | Springdoc OpenAPI 2.6.0 / Swagger UI |
| Contenedores | Docker / Docker Compose |
| Despliegue e IaC | Render / Render Blueprints (`render.yaml`) |

## Estructura principal

```text
franquicias-api/
├── src/
│   └── main/
│       ├── java/com/empresa/franquicias/
│       │   ├── domain/                  # Modelos y contratos de dominio
│       │   ├── usecases/                # Casos de uso y lógica de negocio
│       │   └── infrastructure/          # REST, JPA, configuración y mapeadores
│       └── resources/
│           ├── application.properties
│           └── application-render.properties
├── pom.xml
├── Dockerfile
├── docker-compose.yml                # Solo la API; NO levanta PostgreSQL
├── render.yaml                       # Blueprint: API y PostgreSQL en Render
├── .env.example                      # Plantilla pública, sin secretos
├── .gitignore
└── README.md
```

## Requisitos para ejecutarlo localmente

- Git.
- **Opción recomendada:** Docker Desktop con Docker Compose instalado y funcionando.
- **Opción sin Docker:** JDK 21 y Maven disponibles desde la terminal (o IntelliJ IDEA con Maven).
- Acceso de red a PostgreSQL y credenciales del ambiente local/de pruebas, cuya instancia está alojada en Render.
- El esquema de la base de datos del ambiente local debe contener las tablas requeridas por las entidades JPA cuando se utilice `ddl-auto=validate`.

> **Seguridad:** nunca publiques el archivo `.env`, contraseñas, archivos de estado o cadenas de conexión que incluyan credenciales. Usa `.env.example` como plantilla. Si una contraseña fue compartida, rótala desde Render.

## 1. Clonar el repositorio

En una terminal, ejecuta (reemplaza la URL por la del repositorio público real):

```bash
git clone https://github.com/USUARIO/franquicias-api.git
cd franquicias-api
```

El código se entrega en un repositorio **público** para facilitar su revisión, instalación y evaluación. La URL anterior es un ejemplo y debe sustituirse por la dirección real antes de publicar la documentación final.

## 2. Configurar PostgreSQL para el ambiente local

En la raíz del proyecto, crea un archivo `.env` a partir de `.env.example`.

**PowerShell (Windows):**

```powershell
Copy-Item .env.example .env
```

**Linux/macOS:**

```bash
cp .env.example .env
```

Edita `.env` con los datos de conexión **externa** de PostgreSQL para el ambiente local:

```dotenv
LOCAL_POSTGRES_JDBC_URL=jdbc:postgresql://HOST_POSTGRES_LOCAL:5432/BASE_DESARROLLO?sslmode=require
LOCAL_POSTGRES_USER=USUARIO_DESARROLLO
LOCAL_POSTGRES_PASSWORD=CONTRASENA_DESARROLLO
```

Estos nombres (`LOCAL_POSTGRES_*`) corresponden al `docker-compose.yml` que acompaña esta versión del proyecto. La URL JDBC debe comenzar por `jdbc:postgresql://` y la contraseña no debe subirse a Git.

**Importante:** la base de datos del ambiente local **no se ejecuta dentro de Docker Compose**. La aplicación se conecta mediante la URL externa de PostgreSQL tanto desde el IDE como desde el contenedor local.

## 3. Ejecutar localmente con Docker (recomendado)

Desde la raíz del proyecto:

```powershell
docker compose up -d --build
```

Docker construye la aplicación con Maven y Java 21, crea **únicamente el contenedor de Spring Boot** y le pasa las credenciales de `.env`. No crea ni elimina ninguna base de datos.

Comandos útiles:

```powershell
# Ver estado de los servicios
docker compose ps

# Revisar los logs de Spring Boot
docker compose logs -f app

# Detener los contenedores (sin borrar PostgreSQL)
docker compose down
```

La aplicación queda disponible en **http://localhost:8080** si el puerto está libre y el arranque finaliza correctamente.

### Ejecutar sin Docker (Maven o IntelliJ IDEA)

El `.env` de Docker Compose **no se carga automáticamente** al iniciar Spring Boot desde IntelliJ o con `mvn spring-boot:run`. En la configuración de ejecución del IDE, define:

| Variable | Valor |
| --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC **externa** de PostgreSQL de pruebas, con `?sslmode=require` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de pruebas |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de pruebas |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` |

Con JDK 21 y Maven instalados:

```powershell
java -version
mvn -version
mvn clean package -DskipTests
mvn spring-boot:run
```

También puedes abrir `pom.xml` en IntelliJ IDEA e iniciar `BackendApiApplication` con esas variables de entorno.

> `validate` comprueba que las tablas existen, pero no las crea. Para preparar una base vacía de desarrollo, crea el esquema mediante scripts/migraciones antes de usar `validate`.

## 4. Swagger y endpoints

Con la API iniciada:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Especificación OpenAPI:** http://localhost:8080/v3/api-docs

En Render, sustituye `http://localhost:8080` por la URL pública real del servicio.

### Operaciones disponibles

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/api/franquicias/save` | Crear franquicia |
| `POST` | `/api/franquicias/update` | Actualizar franquicia |
| `POST` | `/api/sucursales/save` | Crear sucursal |
| `POST` | `/api/sucursales/update` | Actualizar nombre de sucursal |
| `POST` | `/api/productos/save` | Crear producto |
| `POST` | `/api/productos/update-stock` | Actualizar stock de un producto |
| `POST` | `/api/productos/update-nombre` | Actualizar nombre de un producto |
| `GET` | `/api/franquicias/{franquiciaId}/productos/max-stock` | Consultar productos con mayor stock por sucursal de una franquicia |
| `POST` | `/api/productos/delete` | Eliminar producto |

Las operaciones `POST` utilizan cuerpos JSON; la consulta de mayor stock utiliza **GET con un identificador en la ruta**. En el código actual se utiliza `POST` para eliminar un producto mediante JSON.

### Ejemplos de consumo

**Crear franquicia:**

```bash
curl -X POST http://localhost:8080/api/franquicias/save \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Franquicia Nacional"}'
```

**Crear sucursal:** sustituye `franquiciaId` por el ID retornado al crear la franquicia.

```bash
curl -X POST http://localhost:8080/api/sucursales/save \
  -H 'Content-Type: application/json' \
  -d '{"franquiciaId":1,"nombre":"Sucursal Bucaramanga"}'
```

**Crear producto:** sustituye `sucursalId` por un ID existente.

```bash
curl -X POST http://localhost:8080/api/productos/save \
  -H 'Content-Type: application/json' \
  -d '{"sucursalId":1,"nombre":"Arroz 500 g","stock":150}'
```

**Actualizar stock:**

```bash
curl -X POST http://localhost:8080/api/productos/update-stock \
  -H 'Content-Type: application/json' \
  -d '{"sucursalId":1,"productoId":1,"stock":300}'
```

**Consultar mayor stock por sucursal:**

```bash
curl http://localhost:8080/api/franquicias/1/productos/max-stock
```

**Eliminar producto:**

```bash
curl -X POST http://localhost:8080/api/productos/delete \
  -H 'Content-Type: application/json' \
  -d '{"sucursalId":1,"productoId":1}'
```

Los ejemplos son ilustrativos; reemplaza los identificadores por los realmente creados. La API utiliza validaciones de campos obligatorios y de stock no negativo.

## 5. Despliegue en Render con infraestructura como código (IaC)

El archivo **`render.yaml`**, en la raíz del repositorio, declara los recursos del **ambiente desplegado**:

| Recurso | Nombre declarado | Función |
| --- | --- | --- |
| Servicio web | `franquicias-api-iac` | Ejecutar la imagen Docker de Spring Boot |
| PostgreSQL | `franquicias-db-iac` | Persistencia del ambiente desplegado |

Render Blueprints permite aprovisionar y administrar los recursos del ambiente desplegado desde un archivo versionado en Git. El Blueprint proporciona a la API las variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` mediante referencias `fromDatabase`.

El perfil `render` usa `application-render.properties` para construir la URL JDBC interna:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
```

El servicio declara `SPRING_PROFILES_ACTIVE=render`. De esta manera, **cada ambiente utiliza su propia base de datos**: el ambiente local obtiene su conexión desde `.env` y el ambiente desplegado recibe su configuración desde Render.

### Aprovisionamiento inicial

1. Publicar los cambios del repositorio (incluido `render.yaml`) en GitHub.
2. Acceder a [Render Dashboard](https://dashboard.render.com/) y seleccionar el workspace del ambiente de despliegue.
3. Seleccionar **New → Blueprint**, conectar el repositorio y elegir la rama correspondiente.
4. Revisar el plan de cambios: deben figurar el servicio `franquicias-api-iac` y la base de datos `franquicias-db-iac` del ambiente desplegado.
5. Confirmar **Deploy Blueprint** y comprobar el resultado en los logs de la API.
6. Abrir la URL pública asignada por Render y verificar `/swagger-ui.html`.

> **No se ha verificado el aprovisionamiento remoto desde este README:** el archivo describe el procedimiento y la configuración, pero la creación efectiva debe comprobarse en el Dashboard de Render.

### Precauciones y límites

- **Mantener los ambientes separados.** La base de desarrollo/pruebas se configura mediante `.env` y la base del despliegue se administra mediante el Blueprint.
- Render admite **una sola base PostgreSQL Free activa por workspace**. Si ambos ambientes utilizan el plan gratuito, deberán contar con workspaces que permitan el aprovisionamiento correspondiente o utilizar un plan de pago.
- Las bases PostgreSQL gratuitas de Render caducan después de 30 días y no incluyen backups administrados. No están recomendadas para producción.
- El Blueprint configura `SPRING_JPA_HIBERNATE_DDL_AUTO=update` para inicializar las tablas del ambiente de demostración. Para un entorno real, utiliza migraciones controladas (Flyway/Liquibase) y cambia a `validate`.
- Verifica que cada servicio tenga las variables de entorno de su ambiente y revisa su configuración de Auto Deploy antes de publicar cambios.

Documentación: [Render Blueprints](https://render.com/docs/infrastructure-as-code) · [Blueprint YAML Reference](https://render.com/docs/blueprint-spec) · [Render Free](https://render.com/docs/free).

## 6. Flujo de trabajo con Git

Se utiliza Git para versionar el código fuente, la configuración de Docker, la documentación y la infraestructura declarada en `render.yaml`. Un flujo recomendado para cambios funcionales es:

```bash
# Crear una rama de trabajo
git checkout -b feature/nueva-funcionalidad

# Revisar cambios
git status

# Preparar y confirmar cambios
git add .
git commit -m "feat: implement new functionality"

# Publicar la rama
git push -u origin feature/nueva-funcionalidad
```

Después, abre un **Pull Request** hacia `main`, revisa los cambios y realiza el merge. Para cambios pequeños de documentación se puede utilizar, por ejemplo, `docs: update deployment instructions`. Los nombres de rama y los mensajes de commit son convenciones sugeridas, no requisitos impuestos por el proyecto.

Antes de entregar la prueba:

- Comprueba que el repositorio sea **público** y accesible sin iniciar sesión.
- Asegúrate de que `README.md`, `pom.xml`, `Dockerfile`, `docker-compose.yml`, `render.yaml` y `.env.example` estén versionados.
- Confirma que `.env`, claves y contraseñas **no estén en Git ni en el historial publicado**. Si alguna credencial fue expuesta, rótala.
- Revisa el historial con `git log --oneline` y el estado de tu rama con `git status`.
- En la entrega, incluye el enlace real del repositorio público y, una vez comprobado, el enlace público de la API de Render.

## 7. Solución de problemas habituales

| Mensaje o síntoma | Qué revisar |
| --- | --- |
| `Connection to localhost:5432 refused` dentro de Docker/Render | La URL de conexión está apuntando al propio contenedor; verifica las variables de entorno recibidas por Spring Boot. |
| `Driver ... does not accept jdbcUrl` | La URL JDBC debe comenzar por `jdbc:postgresql://` y no por `postgresql://` ni `=jdbc:...`. |
| `Schema-validation: missing table [franquicia]` | Existe conexión a la base, pero falta la tabla o el esquema esperado; `validate` no crea tablas. |
| `Unable to determine Dialect without JDBC metadata` | Puede ser consecuencia de un error previo de conexión; revisa el primer `Caused by` en los logs. |
| Swagger no abre | Comprueba que la API haya iniciado y visita `/swagger-ui.html`. |
| Render usa `localhost` a pesar de usar `render.yaml` | Verifica el perfil `render`, las referencias `fromDatabase` y cualquier variable `SPRING_DATASOURCE_URL` que sobrescriba la URL del perfil. |

## Estado de la documentación

Este README describe la **configuración del proyecto y los pasos para reproducirla**, pero no certifica que el repositorio ya sea público ni que el Blueprint haya sido ejecutado con éxito. Esos dos puntos se deben confirmar en GitHub y Render al entregar la prueba técnica.
