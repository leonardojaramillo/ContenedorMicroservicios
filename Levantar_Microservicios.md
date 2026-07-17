# Grapevine — Sistema Vitivinícola (Microservicios)

Guía paso a paso para clonar y levantar el backend completo en local con Docker.

Repositorio: https://github.com/leonardojaramillo/ContenedorMicroservicios.git

---

## 1. Requisitos previos

Antes de empezar, instala:

- **Docker Desktop** (incluye Docker Compose) → https://www.docker.com/products/docker-desktop/
- **Git** → https://git-scm.com/downloads

Verifica que ambos estén instalados correctamente abriendo una terminal (PowerShell en Windows) y corriendo:

```powershell
docker --version
docker compose version
git --version
```

Si los tres comandos devuelven una versión sin error, estás listo para continuar.

> **Importante:** Docker Desktop debe estar abierto y corriendo (el ícono de la ballena en la barra de tareas) antes de ejecutar cualquier comando `docker compose`.
>
> No importa qué IDE uses (Eclipse, IntelliJ, VS Code) ni si tienes Java o Maven instalados en tu máquina — cada microservicio se compila **dentro** de su propio contenedor Docker (multi-stage build con `maven:3.9-eclipse-temurin-21`), totalmente aislado de tu sistema operativo local.

---

## 2. Clonar el repositorio

Abre PowerShell, ubícate en la carpeta donde quieras guardar el proyecto y ejecuta:

```powershell
git clone https://github.com/leonardojaramillo/ContenedorMicroservicios.git
cd ContenedorMicroservicios
```

---

## 3. Estructura del proyecto

El repo contiene:

- **Infraestructura**: `config-server`, `discovery-service` (Eureka), `api-gateway`
- **8 microservicios de negocio**: `auth-service`, `product-service`, `purchase-service`, `finance-service`, `sales-service`, `inventory-service`, `audit-service`, `reporting-service`
- **Bases de datos** (una por servicio, cada una en su propio contenedor): Postgres, MySQL, TimescaleDB, Elasticsearch, Redis
- **`backups/`**: scripts `.sql` que cargan datos de ejemplo al iniciar las bases de datos por primera vez
- **`docker-compose.yml`**: orquesta los ~19 contenedores

---

## 4. Levantar el proyecto

### 4.1 Construir las imágenes (uno por uno, no todos de golpe)

⚠️ **No uses `docker compose up -d --build` directo.** Si Docker intenta construir los 11 microservicios a la vez, todos descargan sus dependencias de Maven al mismo tiempo y **satura la conexión**, provocando builds fallidas por timeout. Construye cada servicio de a uno:

```powershell
docker compose build config-server
docker compose build discovery-service
docker compose build auth-service
docker compose build product-service
docker compose build purchase-service
docker compose build finance-service
docker compose build sales-service
docker compose build inventory-service
docker compose build audit-service
docker compose build reporting-service
docker compose build api-gateway
```

Cada comando compila un solo servicio (Maven corre *dentro* del contenedor, así que no necesitas Java ni Maven instalados localmente). Esto tarda bastante la primera vez — es normal, son 11 builds de Spring Boot en secuencia.

### 4.2 Levantar todos los contenedores

Una vez que terminaron todos los builds anteriores, levanta todo con un solo comando:

```powershell
docker compose up -d
```

Esto ya no vuelve a compilar nada (las imágenes ya existen); solo crea y arranca los ~19 contenedores (bases de datos + infraestructura + microservicios) usando las imágenes ya construidas.

---

## 5. Verificar que todo esté arriba

Cada microservicio de negocio tarda entre **20 y 80 segundos en arrancar por completo** después de que el contenedor "inicia" (Spring Boot + Eureka + config-server toman su tiempo). No te preocupes si algo no responde de inmediato.

Para ver el estado de todos los contenedores:

```powershell
docker compose ps
```

Todos deberían aparecer como `Up` (o `Up (healthy)` los que tienen healthcheck).

Para ver los logs de un servicio específico y confirmar que ya terminó de arrancar (busca la línea `Started XxxApplication in NN.NNN seconds`):

```powershell
docker compose logs -f auth-service
```

(Presiona `Ctrl + C` para salir del modo de seguimiento en vivo, sin que esto apague el contenedor.)

Para confirmar que los microservicios se registraron en Eureka, abre en el navegador:

```
http://localhost:8761
```

Deberías ver **10 servicios registrados** (los 8 de negocio + `discovery-service` + `api-gateway`; `config-server` no se registra en Eureka, y eso es normal).

---

## 6. Probar que el backend responde

Con todo arriba, prueba el login desde PowerShell:

```powershell
$body = '{"email":"caja@test.com","password":"123456"}'
$resp = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $body
$resp
```

Si te devuelve un `token` (JWT), el backend está funcionando correctamente de punta a punta (Gateway → auth-service → base de datos).

Las credenciales de todas las cuentas de prueba (distintos roles: ADMIN, CAJERO, LOGISTICA, SOFTWARE_ENGINEER, etc.) están en el documento que compartió el equipo aparte de este README.

---

## 7. Puertos de cada servicio

| Servicio | Puerto | Tipo |
|---|---|---|
| api-gateway | 8080 | Entrada principal del backend |
| discovery-service (Eureka) | 8761 | Panel de servicios registrados |
| config-server | 8888 | Configuración centralizada |
| auth-service | 8081 | Microservicio |
| product-service | 8082 | Microservicio |
| purchase-service | 8083 | Microservicio |
| finance-service | 8084 | Microservicio |
| sales-service | 8085 | Microservicio |
| inventory-service | 8086 | Microservicio |
| audit-service | 8087 | Microservicio |
| reporting-service | 8088 | Microservicio |
| postgres-auth | 5432 | Base de datos |
| postgres-product | 5433 | Base de datos |
| postgres-finance | 5434 | Base de datos |
| mysql-purchase | 3306 | Base de datos |
| mysql-sales | 3307 | Base de datos |
| timescaledb-inventory | 5435 | Base de datos |
| elasticsearch-audit | 9200 | Base de datos |
| redis-reporting | 6379 | Cache |

Todo el frontend y cualquier cliente externo debe apuntar **siempre al Gateway** (`http://localhost:8080`), nunca directo a los microservicios individuales, salvo para debugging.

---

## 8. Comandos útiles del día a día

**Apagar todo (sin borrar datos guardados):**
```powershell
docker compose down
```

**Volver a levantar (sin reconstruir, si no cambiaste código):**
```powershell
docker compose up -d
```

**Reconstruir un solo servicio después de modificar su código:**
```powershell
docker compose build nombre-del-servicio
docker compose up -d nombre-del-servicio
```
Ejemplo:
```powershell
docker compose build finance-service
docker compose up -d finance-service
```

**Ver logs de un servicio en vivo:**
```powershell
docker compose logs -f nombre-del-servicio
```

**Ver las últimas N líneas de log (sin seguimiento en vivo):**
```powershell
docker compose logs --tail 50 nombre-del-servicio
```

**Reiniciar un servicio sin reconstruirlo** (útil si solo cambió la configuración en config-server):
```powershell
docker compose restart nombre-del-servicio
```

---

## 9. Reset completo (borrar todos los datos y volver a cargar los datos de ejemplo)

Si necesitas empezar de cero (por ejemplo, si la base de datos quedó en un estado raro, o alguien modificó un archivo `.sql` en `backups/`):

```powershell
docker compose down -v
docker compose up -d
```

> ⚠️ **`-v` borra TODOS los volúmenes**, es decir, todos los datos de todas las bases de datos (usuarios creados, ventas, ajustes de inventario, etc.). Los scripts en `backups/*.sql` se vuelven a ejecutar automáticamente y recargan los datos de ejemplo desde cero.
>
> Los scripts de inicialización (`docker-entrypoint-initdb.d`) **solo se ejecutan la primera vez que un contenedor arranca con su volumen vacío**. Si solo haces `docker compose restart` o `up -d` sin el `-v`, los cambios en los archivos `.sql` de `backups/` **no se van a aplicar**, porque Postgres/MySQL ya consideran esa base de datos como "inicializada".
>
> Nota: `down -v` **no borra las imágenes ya construidas**, así que después del reset no hace falta repetir el paso 4.1 (build), solo el 4.2 (`up -d`).

---

## 10. Problemas comunes

**Las builds fallan o se cuelgan al construir varios servicios a la vez:**
Es justo el problema que evita la sección 4.1: construir de uno en uno en vez de con `--build` masivo. Si ya lo intentaste con `--build` y falló, corre `docker compose build nombre-servicio` para cada uno, uno por uno.

**"Connection refused" o 403 al probar un endpoint justo después de levantar todo:**
Espera un poco más. Los microservicios tardan hasta 80 segundos en registrarse en Eureka y estar listos para recibir tráfico del Gateway. Revisa con `docker compose logs -f nombre-del-servicio` hasta ver `Started XxxApplication`.

**Un microservicio no arranca / reinicia en bucle:**
```powershell
docker compose logs --tail 100 nombre-del-servicio
```
Busca la línea con `ERROR` o `Exception` (ojo: el filtro de PowerShell `Select-String` no distingue mayúsculas por defecto, así que si buscas "ERROR" puede confundirse con nombres de clases como `ErrorReportValve`; usa `-CaseSensitive` si necesitas precisión).

**Cambié un archivo `.sql` en `backups/` y no veo el cambio reflejado:**
Necesitas `docker compose down -v` (ver sección 9) para que el volumen se recree desde cero y el script se vuelva a ejecutar.

**El puerto ya está en uso (`port is already allocated`):**
Verifica que no tengas otra instancia de Postgres/MySQL/Redis corriendo en tu máquina en esos mismos puertos, o cierra el contenedor que lo esté usando:
```powershell
docker ps
docker stop <container_id>
```

---

## 11. Antes de hacer commit / push

No es necesario apagar los contenedores para subir cambios a git — son independientes. Solo asegúrate de revisar qué se va a subir:

```powershell
git status
```

Si ves carpetas `target/` de algún microservicio en la lista, avisa al equipo — no deberían subirse (ya están en `.gitignore`, pero conviene confirmarlo).
