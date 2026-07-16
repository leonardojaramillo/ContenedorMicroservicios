-- init-sales.sql (MySQL-compatible)


CREATE TABLE customers (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    active BOOLEAN NOT NULL,
    contacto VARCHAR(255),
    documento VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    razon_social VARCHAR(255) NOT NULL,
    segmento VARCHAR(255),
    telefono VARCHAR(255),
    tipo_documento VARCHAR(255) NOT NULL,
    CONSTRAINT customers_tipo_documento_check CHECK (tipo_documento IN ('DNI','RUC','CE'))
);

CREATE TABLE order_details (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(38,2),
    quantity INT,
    subtotal DECIMAL(38,2),
    order_id BIGINT,
    product_id BIGINT
);

CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6),
    customer_document VARCHAR(255),
    customer_name VARCHAR(255),
    status VARCHAR(255),
    total DECIMAL(38,2),
    user_id BIGINT,
    cancel_reason VARCHAR(255),
    cancelled_at DATETIME,
    warehouse_id BIGINT,
    CONSTRAINT orders_status_check CHECK (status IN ('PENDING','PAID','CANCELLED'))
);

INSERT INTO customers (id, active, contacto, documento, email, razon_social, segmento, telefono, tipo_documento) VALUES
(1,1,'01-558-3554','78596623','robertito4558@gmail.com','Roberto Campos','Minorista','988564128','DNI'),
(2,1,'Ana Belaunde','20458796321','compras@elmirador.pe','Restaurante El Mirador SAC','Mayorista','987112233','RUC'),
(3,1,'Jose Vargas','20512348877','logistica@costaazul.pe','Hotel Costa Azul','Mayorista','988223344','RUC'),
(4,1,'Juan Pérez','45678912','jperez@gmail.com','Juan Pérez Gómez','Minorista','999334455','DNI'),
(5,1,'Pancho Rivera','20399887766','ventas@donpancho.pe','Licorería Don Pancho','Minorista','977556677','RUC'),
(6,1,'María Quiroz','41234567','mquiroz@hotmail.com','María Quiroz Salas','Casual','966778899','DNI'),
(7,0,'Carla Núñez','20678123455','eventos@premium.pe','Eventos Premium EIRL','Mayorista','955889900','RUC');

INSERT INTO order_details (id, price, quantity, subtotal, order_id, product_id) VALUES
(1,89.90,2,179.80,1,1),
(2,89.90,5,449.50,2,2),
(3,89.90,12,1078.80,3,2),
(4,120.00,15,1800.00,4,4),
(5,89.90,22,1977.80,5,1),
(6,89.90,2,179.80,6,2),
(7,45.90,10,459.00,7,5),
(8,38.50,2,77.00,8,6),
(9,52.00,6,312.00,9,7),
(10,44.00,1,44.00,10,9),
(11,65.00,3,195.00,11,10);

INSERT INTO orders (id, created_at, customer_document, customer_name, status, total, user_id, cancel_reason, cancelled_at, warehouse_id) VALUES
(1,'2026-05-12 21:00:31.491831','74839211','Juan Perez','PAID',179.80,NULL,NULL,NULL,NULL),
(2,'2026-05-16 12:06:35.225027','77885860','Pedrito','PAID',449.50,NULL,NULL,NULL,NULL),
(3,'2026-05-17 01:09:23.970586','77491012','Dione Romani','PAID',1078.80,NULL,NULL,NULL,NULL),
(4,'2026-05-21 21:48:37.267629','77484510','Segio Ramos','PAID',1800.00,NULL,NULL,NULL,NULL),
(5,'2026-05-21 21:49:22.278335','74558699','Michael','PAID',1977.80,NULL,NULL,NULL,NULL),
(6,'2026-06-20 23:17:11.998044','78596623','Roberto Campos','PAID',179.80,NULL,NULL,NULL,'2'),
(7,'2026-06-08 18:17:24.635018','20458796321','Restaurante El Mirador SAC','PAID',459.00,NULL,NULL,NULL,'3'),
(8,'2026-06-13 18:17:24.635018','45678912','Juan Pérez Gómez','PAID',91.80,NULL,NULL,NULL,'4'),
(9,'2026-06-18 18:17:24.635018','20399887766','Licorería Don Pancho','PAID',312.00,NULL,NULL,NULL,'6'),
(10,'2026-06-20 18:17:24.635018','41234567','María Quiroz Salas','CANCELLED',65.00,NULL,NULL,NULL,'3'),
(11,'2026-06-25 18:17:24.635018','20512348877','Hotel Costa Azul','PAID',220.00,NULL,NULL,NULL,'5');


ALTER TABLE customers AUTO_INCREMENT = 8;
ALTER TABLE order_details AUTO_INCREMENT = 12;
ALTER TABLE orders AUTO_INCREMENT = 12;