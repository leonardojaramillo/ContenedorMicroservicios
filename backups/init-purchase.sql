--
-- PostgreSQL database dump
--

\restrict 3fh4PKf5id2Be3zuNSDtLFO0HDQZSJ2OOL7YdaShmvcs2T2D1XWg6mcSuL7RW1X

-- Dumped from database version 17.6
-- Dumped by pg_dump version 18.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: purchase_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.purchase_details (
    id bigint NOT NULL,
    price numeric(38,2),
    quantity integer,
    subtotal numeric(38,2),
    product_id bigint,
    purchase_id bigint
);


--
-- Name: purchase_requests; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.purchase_requests (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    justification character varying(255),
    quantity integer NOT NULL,
    status character varying(255) NOT NULL,
    product_id bigint NOT NULL,
    requested_by character varying(255) NOT NULL,
    purchase_created boolean DEFAULT false NOT NULL,
    CONSTRAINT purchase_requests_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


--
-- Name: purchases; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.purchases (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    total numeric(38,2),
    supplier_id bigint,
    bank_account_id bigint,
    status character varying(255) DEFAULT 'DRAFT'::character varying NOT NULL,
    warehouse_id bigint,
    payment_proof_url text
);


--
-- Name: suppliers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.suppliers (
    id bigint NOT NULL,
    active boolean,
    address character varying(255),
    email character varying(255),
    name character varying(255),
    phone character varying(255)
);


--
-- Data for Name: purchase_details; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.purchase_details (id, price, quantity, subtotal, product_id, purchase_id) FROM stdin;
1	89.90	4	359.60	1	1
2	120.00	10	1200.00	4	2
3	120.00	5	600.00	4	3
4	89.90	13	1168.70	2	4
8	89.90	15	1348.50	2	5
13	89.90	31	2786.90	2	6
14	120.00	5	600.00	4	6
15	89.90	15	1348.50	2	7
17	120.00	5	600.00	4	8
18	89.90	5	449.50	2	9
19	89.90	5	449.50	1	9
21	120.00	5	600.00	4	10
22	45.90	50	2295.00	5	11
23	38.50	40	1540.00	6	12
24	52.35	17	889.95	7	13
25	64.70	17	1099.90	10	14
26	51.33	15	769.95	11	15
\.


--
-- Data for Name: purchase_requests; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.purchase_requests (id, created_at, justification, quantity, status, product_id, requested_by, purchase_created) FROM stdin;
1	2026-05-16 11:52:48.285334	Temporada alta	26	REJECTED	4	Fabio Raul Santos Paucar	f
3	2026-05-16 16:56:48.098724	Stock bajo	16	PENDING	4	Fabio Raul Santos Paucar	f
4	2026-05-16 16:57:16.344496	Nivelar stock	28	PENDING	1	Fabio Raul Santos Paucar	f
6	2026-05-19 12:08:04.446125	Stock bajo	23	REJECTED	2	Fabio Raul Santos Paucar	f
7	2026-05-21 22:57:49.312316	Stock muy bajo	13	APPROVED	2	Fabio Raul Santos Paucar	f
2	2026-05-16 16:48:05.070464	Temporada Alta	31	APPROVED	2	Fabio Raul Santos Paucar	t
5	2026-05-17 00:53:47.521059	Temporada alta	15	APPROVED	2	Fabio Raul Santos Paucar	t
8	2026-05-26 18:06:53.469415	Celebración	1	REJECTED	4	Fabio Raul Santos Paucar	f
9	2026-05-26 18:21:29.011291	Celebración de 20° aniversario	1	APPROVED	4	Fabio Raul Santos Paucar	t
10	2026-06-22 18:30:30.138888	Bajo stock	16	APPROVED	4	Fabio Raul Santos Paucar	f
11	2026-06-03 18:17:24.635018	Stock bajo, quedan 4 unidades	50	APPROVED	11	Andrea Quispe	t
12	2026-06-23 18:17:24.635018	Producto sin stock	30	PENDING	12	Pedro Salazar	f
13	2026-06-16 18:17:24.635018	Temporada de eventos	20	APPROVED	10	Andrea Quispe	f
14	2026-06-10 18:17:24.635018	Rotación alta del producto	40	REJECTED	6	Pedro Salazar	f
15	2026-06-26 18:17:24.635018	Reposición preventiva	25	PENDING	8	Andrea Quispe	f
\.


--
-- Data for Name: purchases; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.purchases (id, created_at, total, supplier_id, bank_account_id, status, warehouse_id, payment_proof_url) FROM stdin;
3	2026-05-17 00:56:51.473993	600.00	1	2	CANCELLED	\N	\N
2	2026-05-16 16:12:34.903188	1200.00	1	1	PAID	\N	\N
1	2026-05-16 01:27:36.044174	359.60	1	\N	CANCELLED	\N	\N
6	2026-05-22 19:32:16.570767	3386.90	2	2	PAID	\N	\N
4	2026-05-22 13:58:58.098351	1168.70	1	2	PAID	\N	\N
5	2026-05-22 19:28:28.51832	1348.50	2	2	CANCELLED	\N	\N
7	2026-05-22 19:51:29.637893	1348.50	1	1	CANCELLED	\N	\N
8	2026-05-26 18:21:43.281385	600.00	3	\N	PAID	\N	\N
9	2026-06-22 18:24:06.094676	899.00	1	2	PAID	\N	\N
10	2026-06-28 09:25:22.061194	600.00	1	3	PAID	1	\N
11	2026-05-19 18:17:24.635018	2295.00	4	4	PAID	3	https://example.com/comprobante1.jpg
12	2026-06-08 18:17:24.635018	1540.00	6	5	RECEIVED	6	\N
13	2026-06-18 18:17:24.635018	890.00	7	4	CONFIRMED	5	\N
14	2026-06-25 18:17:24.635018	1100.00	8	9	DRAFT	4	\N
15	2026-05-24 18:17:24.635018	770.00	5	4	CANCELLED	3	\N
\.


--
-- Data for Name: suppliers; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.suppliers (id, active, address, email, name, phone) FROM stdin;
1	t	Limpa, Peru	vivoporelrock@gmail.com	Jaime Altozano	985233451
2	t	Av. Faucett 228	carloficial@gmail.com	Carlos Moira	932588421
3	t	Av Pablo Patron 501, La Victoria 15019	aronfisi2023@gmail.com	Aron Navarro	924566784
4	t	Av. Los Viñedos 123, Ica	contacto@vinasanjose.pe	Viña San José	987654321
5	t	Jr. Las Uvas 456, Ica	ventas@elcatador.pe	Bodega El Catador	912345678
6	t	Carretera Panamericana km 250	info@soldepisco.pe	Pisquera Sol de Pisco	998877665
7	t	Av. Industrial 789, Arequipa	pedidos@corchosdelsur.pe	Corchos del Sur SAC	955443322
8	t	Calle Comercio 321, Lima	compras@laviña.pe	Distribuidora La Viña	944556677
9	f	Fundo Tacama, Ica	contacto@tacama.pe	Viñedos Tacama	933221144
\.



--
-- Sequence auto-generada para purchase_details (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.purchase_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.purchase_details_id_seq OWNED BY public.purchase_details.id;

ALTER TABLE ONLY public.purchase_details ALTER COLUMN id SET DEFAULT nextval('public.purchase_details_id_seq'::regclass);

SELECT pg_catalog.setval('public.purchase_details_id_seq', COALESCE((SELECT MAX(id) FROM public.purchase_details), 1), true);



--
-- Sequence auto-generada para purchase_requests (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.purchase_requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.purchase_requests_id_seq OWNED BY public.purchase_requests.id;

ALTER TABLE ONLY public.purchase_requests ALTER COLUMN id SET DEFAULT nextval('public.purchase_requests_id_seq'::regclass);

SELECT pg_catalog.setval('public.purchase_requests_id_seq', COALESCE((SELECT MAX(id) FROM public.purchase_requests), 1), true);



--
-- Sequence auto-generada para purchases (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.purchases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.purchases_id_seq OWNED BY public.purchases.id;

ALTER TABLE ONLY public.purchases ALTER COLUMN id SET DEFAULT nextval('public.purchases_id_seq'::regclass);

SELECT pg_catalog.setval('public.purchases_id_seq', COALESCE((SELECT MAX(id) FROM public.purchases), 1), true);



--
-- Sequence auto-generada para suppliers (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.suppliers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.suppliers_id_seq OWNED BY public.suppliers.id;

ALTER TABLE ONLY public.suppliers ALTER COLUMN id SET DEFAULT nextval('public.suppliers_id_seq'::regclass);

SELECT pg_catalog.setval('public.suppliers_id_seq', COALESCE((SELECT MAX(id) FROM public.suppliers), 1), true);


--
-- PostgreSQL database dump complete
--

\unrestrict 3fh4PKf5id2Be3zuNSDtLFO0HDQZSJ2OOL7YdaShmvcs2T2D1XWg6mcSuL7RW1X