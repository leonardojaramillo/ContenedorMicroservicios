# Grapevine — Sistema de Microservicios

Proyecto de arquitectura de microservicios para una empresa vitivinícola, desarrollado con Spring Boot, Spring Cloud (Eureka, Config Server, Gateway, OpenFeign) y Angular.

## Arquitectura

**Infraestructura (3):**
| Servicio | Puerto | Función |
|---|---|---|
| `config-server` | 8888 | Configuración centralizada de todos los microservicios |
| `discovery-service` | 8761 | Registro de servicios (Eureka) |
| `api-gateway` | 8080 | Punto de entrada único, enruta a cada microservicio |

**Negocio (8):**
| Servicio | Puerto | Motor de BD | Dominio |
|---|---|---|---|
| `auth-service` | 8081 | PostgreSQL | Autenticación, usuarios, perfil |
| `product-service` | 8082 | PostgreSQL | Catálogo de productos, almacenes |
| `purchase-service` | 8083 | MySQL | Compras, proveedores, solicitudes de compra |
| `finance-service` | 8084 | PostgreSQL | Cuentas bancarias, caja y movimientos |
| `sales-service` | 8085 | MySQL | Ventas, clientes |
| `inventory-service` | 8086 | TimescaleDB | Ajustes de stock, guías de traslado |
| `audit-service` | 8087 | Elasticsearch | Logs de auditoría del sistema |
| `reporting-service` | 8088 | Redis (cache) | Dashboard y reportes agregados |

Todos los microservicios se comunican entre sí vía **Feign Client** (HTTP interno), a través de **Eureka** para descubrimiento de servicios. La autenticación usa **JWT** con claims de rol embebidos — ningún microservicio de negocio depende de la base de datos de usuarios de otro.

---

## Requisitos previos

- **Docker Desktop** instalado y corriendo.
- Si vas a probar con Kubernetes: habilita Kubernetes en Docker Desktop (**Settings → Kubernetes → Enable Kubernetes**).
- Puertos libres en tu máquina: `8080-8088`, `8761`, `8888`, `5432-5435`, `3306-3307`, `9200`, `6379`.

---

## Opción A: Despliegue con Docker Compose (recomendado, más simple)

Desde la raíz del proyecto (`grapevineContainer/`):

```bash
docker compose up -d --build
```

Este único comando:
1. Compila las 11 imágenes Docker (una por microservicio).
2. Levanta las 8 bases de datos (PostgreSQL x3, MySQL x2, TimescaleDB, Elasticsearch, Redis).
3. Levanta los 11 microservicios, respetando el orden de dependencias (bases de datos → config-server → discovery-service → resto).

### Verificar que todo esté arriba

```bash
docker compose ps
```

Todos los contenedores deben mostrar estado `Up` (o `healthy` en las bases de datos).

### Ver logs de un servicio específico

```bash
docker compose logs -f auth-service
```

### Confirmar que los microservicios se registraron en Eureka

Abre en el navegador: **http://localhost:8761** — deberías ver los 8 microservicios de negocio + el gateway en estado `UP`.

### Detener todo

```bash
docker compose down
```

Para borrar también los datos de las bases de datos (reinicio completo):
```bash
docker compose down -v
```

### Convertir las tablas de `inventory-service` en hypertables (una sola vez, tras el primer arranque)

TimescaleDB necesita este paso manual después de que Hibernate cree las tablas por primera vez:

```bash
docker exec -it timescaledb-inventory psql -U postgres -d inventory_db
```

Dentro de `psql`:
```sql
SELECT create_hypertable('inventory_adjustments', 'created_at', migrate_data => true);
SELECT create_hypertable('inventory_movements', 'created_at', migrate_data => true);
SELECT create_hypertable('transfer_guides', 'created_at', migrate_data => true);
```

---

## Opción B: Despliegue con Kubernetes

### 1. Construir las imágenes localmente

```bash
docker build -t grapevine/config-server ./config-server
docker build -t grapevine/discovery-service ./discovery-service
docker build -t grapevine/api-gateway ./api-gateway
docker build -t grapevine/auth-service ./auth-service
docker build -t grapevine/product-service ./product-service
docker build -t grapevine/purchase-service ./purchase-service
docker build -t grapevine/finance-service ./finance-service
docker build -t grapevine/sales-service ./sales-service
docker build -t grapevine/inventory-service ./inventory-service
docker build -t grapevine/audit-service ./audit-service
docker build -t grapevine/reporting-service ./reporting-service
```

### 2. Aplicar los manifiestos

```bash
kubectl apply -f k8s/
```

### 3. Verificar

```bash
kubectl get pods
kubectl get services
```

Espera a que todos los pods estén en estado `Running`.

### 4. Acceder al sistema

El Gateway está expuesto como `LoadBalancer`, así que en Docker Desktop se mapea automáticamente a:
```
http://localhost:8080
```

### 5. Convertir hypertables en Kubernetes

```bash
kubectl exec -it deployment/timescaledb-inventory -- psql -U postgres -d inventory_db
```
(mismos comandos SQL que en la Opción A)

### 6. Eliminar todo

```bash
kubectl delete -f k8s/
```

---

## Probar el sistema (con cualquiera de las 2 opciones)

### 1. Crear un usuario administrador (primera vez)

Como no hay endpoint público de registro, el primer usuario se crea directo en la base de datos:

```bash
docker exec -it postgres-auth psql -U postgres -d auth_db
```
```sql
INSERT INTO users (full_name, email, password, role, enabled, must_change_password, created_at)
VALUES ('Admin', 'admin@grapevine.com', '$2b$10$w2t7UnsPetR8r4UTMd/rDea6jySNicc6uh/IGY4TGdDZBMhza1e7e', 'SOFTWARE_ENGINEER', true, false, now());
```
(La contraseña de ese hash es: `Admin123!`)

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@grapevine.com","password":"Admin123!"}'
```

Copia el `token` de la respuesta y úsalo en el header `Authorization: Bearer <token>` para el resto de peticiones.

### 3. Explorar los endpoints

Todos pasan por el Gateway en `http://localhost:8080`:
- `POST /api/products` — crear producto
- `POST /api/warehouses` — crear almacén
- `POST /api/purchases` — crear compra
- `POST /api/orders` — crear venta
- `GET /api/dashboard` — métricas generales
- `GET /api/audit-logs` — logs de auditoría

---

## Variables sensibles

Todas las credenciales (usuarios/contraseñas de BD, `JWT_SECRET`) ya vienen escritas directamente en `docker-compose.yml` y en los manifiestos de `k8s/` — no necesitas configurar nada manualmente. Son valores de desarrollo/entrega académica, no aptos para producción real.

---

## Notas y deuda técnica conocida

- **Atomicidad distribuida:** operaciones como "vender y descontar stock" no son transaccionales entre `sales-service`/`purchase-service` e `inventory-service` — es una limitación conocida de microservicios (se resolvería con el patrón *Saga*, fuera del alcance de esta entrega).
- **`Product.stock`** fue eliminado de `product-service` por ser redundante — la fuente de verdad del stock es siempre `inventory-service`.

---

## Contacto

Cualquier duda sobre la arquitectura o el código, contactar a Leonardo Jaramillo.
