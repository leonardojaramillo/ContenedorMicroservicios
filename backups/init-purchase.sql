-- init-purchase.sql (MySQL-compatible)


CREATE TABLE purchase_details (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(38,2),
    quantity INT,
    subtotal DECIMAL(38,2),
    product_id BIGINT,
    purchase_id BIGINT
);

CREATE TABLE purchase_requests (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    justification VARCHAR(255),
    quantity INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    product_id BIGINT NOT NULL,
    requested_by VARCHAR(255) NOT NULL,
    purchase_created BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT purchase_requests_status_check CHECK (status IN ('PENDING','APPROVED','REJECTED'))
);

CREATE TABLE purchases (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6),
    total DECIMAL(38,2),
    supplier_id BIGINT,
    bank_account_id BIGINT,
    status VARCHAR(255) NOT NULL DEFAULT 'DRAFT',
    warehouse_id BIGINT,
    payment_proof_url TEXT
);

CREATE TABLE suppliers (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    active BOOLEAN,
    address VARCHAR(255),
    email VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(255)
);

INSERT INTO purchase_details (id, price, quantity, subtotal, product_id, purchase_id) VALUES
('1','89.90','4','359.60','1','1'),
('2','120.00','10','1200.00','4','2'),
('3','120.00','5','600.00','4','3'),
('4','89.90','13','1168.70','2','4'),
('8','89.90','15','1348.50','2','5'),
('13','89.90','31','2786.90','2','6'),
('14','120.00','5','600.00','4','6'),
('15','89.90','15','1348.50','2','7'),
('17','120.00','5','600.00','4','8'),
('18','89.90','5','449.50','2','9'),
('19','89.90','5','449.50','1','9'),
('21','120.00','5','600.00','4','10'),
('22','45.90','50','2295.00','5','11'),
('23','38.50','40','1540.00','6','12'),
('24','52.35','17','889.95','7','13'),
('25','64.70','17','1099.90','10','14'),
('26','51.33','15','769.95','11','15');

INSERT INTO purchase_requests (id, created_at, justification, quantity, status, product_id, requested_by, purchase_created) VALUES
(1,'2026-05-16 11:52:48.285334','Temporada alta',26,'REJECTED',4,'Fabio Raul Santos Paucar',0),
(3,'2026-05-16 16:56:48.098724','Stock bajo',16,'PENDING',4,'Fabio Raul Santos Paucar',0),
(4,'2026-05-16 16:57:16.344496','Nivelar stock',28,'PENDING',1,'Fabio Raul Santos Paucar',0),
(6,'2026-05-19 12:08:04.446125','Stock bajo',23,'REJECTED',2,'Fabio Raul Santos Paucar',0),
(7,'2026-05-21 22:57:49.312316','Stock muy bajo',13,'APPROVED',2,'Fabio Raul Santos Paucar',0),
(2,'2026-05-16 16:48:05.070464','Temporada Alta',31,'APPROVED',2,'Fabio Raul Santos Paucar',1),
(5,'2026-05-17 00:53:47.521059','Temporada alta',15,'APPROVED',2,'Fabio Raul Santos Paucar',1),
(8,'2026-05-26 18:06:53.469415','Celebración',1,'REJECTED',4,'Fabio Raul Santos Paucar',0),
(9,'2026-05-26 18:21:29.011291','Celebración de 20° aniversario',1,'APPROVED',4,'Fabio Raul Santos Paucar',1),
(10,'2026-06-22 18:30:30.138888','Bajo stock',16,'APPROVED',4,'Fabio Raul Santos Paucar',0),
(11,'2026-06-03 18:17:24.635018','Stock bajo, quedan 4 unidades',50,'APPROVED',11,'Andrea Quispe',1),
(12,'2026-06-23 18:17:24.635018','Producto sin stock',30,'PENDING',12,'Pedro Salazar',0),
(13,'2026-06-16 18:17:24.635018','Temporada de eventos',20,'APPROVED',10,'Andrea Quispe',0),
(14,'2026-06-10 18:17:24.635018','Rotación alta del producto',40,'REJECTED',6,'Pedro Salazar',0),
(15,'2026-06-26 18:17:24.635018','Reposición preventiva',25,'PENDING',8,'Andrea Quispe',0);

INSERT INTO purchases (id, created_at, total, supplier_id, bank_account_id, status, warehouse_id, payment_proof_url) VALUES
(3,'2026-05-17 00:56:51.473993',600.00,1,2,'CANCELLED',NULL,NULL),
(2,'2026-05-16 16:12:34.903188',1200.00,1,1,'PAID',NULL,NULL),
(1,'2026-05-16 01:27:36.044174',359.60,1,NULL,'CANCELLED',NULL,NULL),
(6,'2026-05-22 19:32:16.570767',3386.90,2,2,'PAID',NULL,NULL),
(4,'2026-05-22 13:58:58.098351',1168.70,1,2,'PAID',NULL,NULL),
(5,'2026-05-22 19:28:28.51832',1348.50,2,2,'CANCELLED',NULL,NULL),
(7,'2026-05-22 19:51:29.637893',1348.50,1,1,'CANCELLED',NULL,NULL),
(8,'2026-05-26 18:21:43.281385',600.00,3,NULL,'PAID',NULL,NULL),
(9,'2026-06-22 18:24:06.094676',899.00,1,2,'PAID',NULL,NULL),
(10,'2026-06-28 09:25:22.061194',600.00,1,3,'PAID',1,NULL),
(11,'2026-05-19 18:17:24.635018',2295.00,4,4,'PAID',3,'https://example.com/comprobante1.jpg'),
(12,'2026-06-08 18:17:24.635018',1540.00,6,5,'RECEIVED',6,NULL),
(13,'2026-06-18 18:17:24.635018',890.00,7,4,'CONFIRMED',5,NULL),
(14,'2026-06-25 18:17:24.635018',1100.00,8,9,'DRAFT',4,NULL),
(15,'2026-05-24 18:17:24.635018',770.00,5,4,'CANCELLED',3,NULL);

INSERT INTO suppliers (id, active, address, email, name, phone) VALUES
(1,1,'Limpa, Peru','vivoporelrock@gmail.com','Jaime Altozano','985233451'),
(2,1,'Av. Faucett 228','carloficial@gmail.com','Carlos Moira','932588421'),
(3,1,'Av Pablo Patron 501, La Victoria 15019','aronfisi2023@gmail.com','Aron Navarro','924566784'),
(4,1,'Av. Los Viñedos 123, Ica','contacto@vinasanjose.pe','Viña San José','987654321'),
(5,1,'Jr. Las Uvas 456, Ica','ventas@elcatador.pe','Bodega El Catador','912345678'),
(6,1,'Carretera Panamericana km 250','info@soldepisco.pe','Pisquera Sol de Pisco','998877665'),
(7,1,'Av. Industrial 789, Arequipa','pedidos@corchosdelsur.pe','Corchos del Sur SAC','955443322'),
(8,1,'Calle Comercio 321, Lima','compras@laviña.pe','Distribuidora La Viña','944556677'),
(9,0,'Fundo Tacama, Ica','contacto@tacama.pe','Viñedos Tacama','933221144');


ALTER TABLE purchase_details AUTO_INCREMENT = 27;
ALTER TABLE purchase_requests AUTO_INCREMENT = 16;
ALTER TABLE purchases AUTO_INCREMENT = 16;
ALTER TABLE suppliers AUTO_INCREMENT = 10;