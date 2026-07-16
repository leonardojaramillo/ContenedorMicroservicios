--
-- PostgreSQL database dump
--

\restrict B1CatrzkLt2o2YdhhE5scpSTSVrPUOIUxUc12RzLQFJ1yXBCxZp3477hWWfdD63

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
-- Name: customers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customers (
    id bigint NOT NULL,
    active boolean NOT NULL,
    contacto character varying(255),
    documento character varying(255) NOT NULL,
    email character varying(255),
    razon_social character varying(255) NOT NULL,
    segmento character varying(255),
    telefono character varying(255),
    tipo_documento character varying(255) NOT NULL,
    CONSTRAINT customers_tipo_documento_check CHECK (((tipo_documento)::text = ANY ((ARRAY['DNI'::character varying, 'RUC'::character varying, 'CE'::character varying])::text[])))
);


--
-- Name: order_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.order_details (
    id bigint NOT NULL,
    price numeric(38,2),
    quantity integer,
    subtotal numeric(38,2),
    order_id bigint,
    product_id bigint
);


--
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    customer_document character varying(255),
    customer_name character varying(255),
    status character varying(255),
    total numeric(38,2),
    user_id bigint,
    cancel_reason character varying(255),
    cancelled_at timestamp without time zone,
    warehouse_id bigint,
    CONSTRAINT orders_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying])::text[])))
);


--
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.customers (id, active, contacto, documento, email, razon_social, segmento, telefono, tipo_documento) FROM stdin;
1	t	01-558-3554	78596623	robertito4558@gmail.com	Roberto Campos	Minorista	988564128	DNI
2	t	Ana Belaunde	20458796321	compras@elmirador.pe	Restaurante El Mirador SAC	Mayorista	987112233	RUC
3	t	Jose Vargas	20512348877	logistica@costaazul.pe	Hotel Costa Azul	Mayorista	988223344	RUC
4	t	Juan Pérez	45678912	jperez@gmail.com	Juan Pérez Gómez	Minorista	999334455	DNI
5	t	Pancho Rivera	20399887766	ventas@donpancho.pe	Licorería Don Pancho	Minorista	977556677	RUC
6	t	María Quiroz	41234567	mquiroz@hotmail.com	María Quiroz Salas	Casual	966778899	DNI
7	f	Carla Núñez	20678123455	eventos@premium.pe	Eventos Premium EIRL	Mayorista	955889900	RUC
\.


--
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.order_details (id, price, quantity, subtotal, order_id, product_id) FROM stdin;
1	89.90	2	179.80	1	1
2	89.90	5	449.50	2	2
3	89.90	12	1078.80	3	2
4	120.00	15	1800.00	4	4
5	89.90	22	1977.80	5	1
6	89.90	2	179.80	6	2
7	45.90	10	459.00	7	5
8	38.50	2	77.00	8	6
9	52.00	6	312.00	9	7
10	44.00	1	44.00	10	9
11	65.00	3	195.00	11	10
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.orders (id, created_at, customer_document, customer_name, status, total, user_id, cancel_reason, cancelled_at, warehouse_id) FROM stdin;
1	2026-05-12 21:00:31.491831	74839211	Juan Perez	PAID	179.80	\N	\N	\N	\N
2	2026-05-16 12:06:35.225027	77885860	Pedrito	PAID	449.50	\N	\N	\N	\N
3	2026-05-17 01:09:23.970586	77491012	Dione Romani	PAID	1078.80	\N	\N	\N	\N
4	2026-05-21 21:48:37.267629	77484510	Segio Ramos	PAID	1800.00	\N	\N	\N	\N
5	2026-05-21 21:49:22.278335	74558699	Michael	PAID	1977.80	\N	\N	\N	\N
6	2026-06-20 23:17:11.998044	78596623	Roberto Campos	PAID	179.80	\N	\N	\N	2
7	2026-06-08 18:17:24.635018	20458796321	Restaurante El Mirador SAC	PAID	459.00	\N	\N	\N	3
8	2026-06-13 18:17:24.635018	45678912	Juan Pérez Gómez	PAID	91.80	\N	\N	\N	4
9	2026-06-18 18:17:24.635018	20399887766	Licorería Don Pancho	PAID	312.00	\N	\N	\N	6
10	2026-06-20 18:17:24.635018	41234567	María Quiroz Salas	CANCELLED	65.00	\N	\N	\N	3
11	2026-06-25 18:17:24.635018	20512348877	Hotel Costa Azul	PAID	220.00	\N	\N	\N	5
\.



--
-- Sequence auto-generada para customers (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.customers_id_seq OWNED BY public.customers.id;

ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);

SELECT pg_catalog.setval('public.customers_id_seq', COALESCE((SELECT MAX(id) FROM public.customers), 1), true);



--
-- Sequence auto-generada para order_details (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.order_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.order_details_id_seq OWNED BY public.order_details.id;

ALTER TABLE ONLY public.order_details ALTER COLUMN id SET DEFAULT nextval('public.order_details_id_seq'::regclass);

SELECT pg_catalog.setval('public.order_details_id_seq', COALESCE((SELECT MAX(id) FROM public.order_details), 1), true);



--
-- Sequence auto-generada para orders (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);

SELECT pg_catalog.setval('public.orders_id_seq', COALESCE((SELECT MAX(id) FROM public.orders), 1), true);


--
-- PostgreSQL database dump complete
--

\unrestrict B1CatrzkLt2o2YdhhE5scpSTSVrPUOIUxUc12RzLQFJ1yXBCxZp3477hWWfdD63