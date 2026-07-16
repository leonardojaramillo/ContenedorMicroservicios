--
-- PostgreSQL database dump
--

\restrict IsWnTjRd4cs9aBZbrW5USv4iWKwqBUe8MeCJpTccBTI56Z9IsCiiU528Bbn4zJV

-- Dumped from database version 17.6
-- Dumped by pg_dump version 18.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- Name: inventory_adjustments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.inventory_adjustments (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    new_stock integer,
    previous_stock integer,
    reason character varying(255),
    product_id bigint,
    warehouse_id bigint
);

ALTER TABLE ONLY public.inventory_adjustments ADD CONSTRAINT inventory_adjustments_pkey PRIMARY KEY (id);


--
-- Name: inventory_movements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.inventory_movements (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    quantity integer,
    reference_id bigint,
    reference_type character varying(255),
    type character varying(255),
    product_id bigint,
    CONSTRAINT inventory_movements_type_check CHECK (((type)::text = ANY ((ARRAY['IN'::character varying, 'OUT'::character varying, 'TRANSFER'::character varying, 'ADJUSTMENT'::character varying])::text[])))
);

ALTER TABLE ONLY public.inventory_movements ADD CONSTRAINT inventory_movements_pkey PRIMARY KEY (id);


--
-- Name: stock_transfer_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stock_transfer_details (
    id bigint NOT NULL,
    quantity integer,
    product_id bigint,
    transfer_id bigint
);

ALTER TABLE ONLY public.stock_transfer_details ADD CONSTRAINT stock_transfer_details_pkey PRIMARY KEY (id);


--
-- Name: stock_transfers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stock_transfers (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    from_warehouse_id bigint,
    to_warehouse_id bigint
);

ALTER TABLE ONLY public.stock_transfers ADD CONSTRAINT stock_transfers_pkey PRIMARY KEY (id);


--
-- Name: transfer_guide_items; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.transfer_guide_items (
    id bigint NOT NULL,
    quantity integer NOT NULL,
    guide_id bigint NOT NULL,
    product_id bigint NOT NULL
);

ALTER TABLE ONLY public.transfer_guide_items ADD CONSTRAINT transfer_guide_items_pkey PRIMARY KEY (id);


--
-- Name: transfer_guides; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.transfer_guides (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by character varying(255) NOT NULL,
    description character varying(255),
    type character varying(255) NOT NULL,
    destination_warehouse_id bigint,
    origin_warehouse_id bigint,
    incident_evidence_url character varying(255),
    incident_reason character varying(255),
    stock_recoverable boolean,
    updated_at timestamp(6) without time zone,
    status character varying(255) DEFAULT 'BORRADOR'::character varying NOT NULL,
    CONSTRAINT transfer_guides_type_check CHECK (((type)::text = ANY ((ARRAY['COMPRA'::character varying, 'VENTA'::character varying, 'TRASLADO'::character varying, 'IMPORTACION'::character varying])::text[])))
);

ALTER TABLE ONLY public.transfer_guides ADD CONSTRAINT transfer_guides_pkey PRIMARY KEY (id);


--
-- Name: warehouse_stock; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.warehouse_stock (
    id bigint NOT NULL,
    stock integer,
    product_id bigint,
    warehouse_id bigint
);

ALTER TABLE ONLY public.warehouse_stock ADD CONSTRAINT warehouse_stock_pkey PRIMARY KEY (id);


--
-- Name: warehouses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.warehouses (
    id bigint NOT NULL,
    active boolean,
    address character varying(255),
    name character varying(255),
    department character varying(255),
    district character varying(255),
    province character varying(255),
    ubigeo_code character varying(255)
);

ALTER TABLE ONLY public.warehouses ADD CONSTRAINT warehouses_pkey PRIMARY KEY (id);


--
-- Data for Name: inventory_adjustments; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.inventory_adjustments (id, created_at, new_stock, previous_stock, reason, product_id, warehouse_id) FROM stdin;
1	2026-05-16 11:16:22.330954	24	24	Merma	2	\N
2	2026-05-26 18:23:16.31966	50	51	Devolución	2	\N
3	2026-05-31 12:20:14.35513	30	0	Conteo físico	1	1
4	2026-05-31 12:20:25.329243	34	0	Conteo físico	4	1
5	2026-05-31 12:20:35.790676	50	0	Conteo físico	2	1
6	2026-05-31 12:28:40.767058	2	0	Conteo físico	1	2
7	2026-06-14 18:17:24.635018	60	55	Conteo físico mensual	5	3
8	2026-06-19 18:17:24.635018	35	38	Merma por botella rota	7	4
9	2026-06-21 18:17:24.635018	40	35	Recepción no documentada	6	6
10	2026-06-23 18:17:24.635018	25	30	Ajuste por devolución cliente	10	6
11	2026-06-25 18:17:24.635018	40	42	Conteo físico mensual	8	3
\.


--
-- Data for Name: inventory_movements; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.inventory_movements (id, created_at, quantity, reference_id, reference_type, type, product_id) FROM stdin;
1	2026-05-19 18:17:24.635018	50	\N	COMPRA	IN	5
2	2026-06-08 18:17:24.635018	40	\N	COMPRA	IN	6
3	2026-06-18 18:17:24.635018	6	\N	VENTA	OUT	7
4	2026-06-25 18:17:24.635018	3	\N	VENTA	OUT	10
5	2026-06-25 18:17:24.635018	2	\N	AJUSTE	OUT	8
\.


--
-- Data for Name: stock_transfer_details; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.stock_transfer_details (id, quantity, product_id, transfer_id) FROM stdin;
1	20	5	1
2	10	7	2
3	15	6	3
4	5	10	4
\.


--
-- Data for Name: stock_transfers; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.stock_transfers (id, created_at, from_warehouse_id, to_warehouse_id) FROM stdin;
1	2026-06-11 18:17:24.635018	3	4
2	2026-06-22 18:17:24.635018	4	5
3	2026-06-04 18:17:24.635018	6	3
4	2026-06-27 18:17:24.635018	5	6
\.


--
-- Data for Name: transfer_guide_items; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.transfer_guide_items (id, quantity, guide_id, product_id) FROM stdin;
1	12	1	1
2	10	2	2
3	4	2	4
4	20	3	5
5	10	5	6
6	15	4	7
7	12	7	8
8	5	6	10
\.


--
-- Data for Name: transfer_guides; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.transfer_guides (id, created_at, created_by, description, type, destination_warehouse_id, origin_warehouse_id, incident_evidence_url, incident_reason, stock_recoverable, updated_at, status) FROM stdin;
2	2026-05-31 12:44:02.685261	Fabio Raul Santos Paucar	Ajustar stock	IMPORTACION	2	1	\N	\N	\N	2026-05-31 12:46:20.890126	ENTREGADO
1	2026-05-16 12:21:21.984861	Fabio Raul Santos Paucar	Equilibrar producto en almacén	TRASLADO	2	1		Camión sin combustible en plena carretera	t	2026-05-31 12:50:06.789125	CANCELADO
3	2026-06-13 18:17:24.635018	Andrea Quispe	Reabastecimiento sucursal centro	TRASLADO	4	3	\N	\N	\N	2026-06-13 18:17:24.635018	ENTREGADO
4	2026-06-24 18:17:24.635018	Pedro Salazar	Traslado a sede Arequipa	TRASLADO	5	4	\N	\N	\N	2026-06-24 18:17:24.635018	EN_TRANSITO
5	2026-06-27 18:17:24.635018	Andrea Quispe	Pendiente de despacho	COMPRA	6	3	\N	\N	\N	2026-06-27 18:17:24.635018	BORRADOR
6	2026-06-06 18:17:24.635018	Pedro Salazar	Cancelado por error de pedido	TRASLADO	3	6	\N	\N	\N	2026-06-06 18:17:24.635018	CANCELADO
7	2026-06-26 18:17:24.635018	Andrea Quispe	Preparando guía de importación	IMPORTACION	4	5	\N	\N	\N	2026-06-26 18:17:24.635018	PREPARANDO
\.


--
-- Data for Name: warehouse_stock; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.warehouse_stock (id, stock, product_id, warehouse_id) FROM stdin;
4	2	1	2
3	40	2	1
6	4	4	2
1	30	1	1
5	8	2	2
2	35	4	1
7	60	5	4
8	60	5	3
9	40	6	6
10	45	6	3
11	25	7	5
12	35	7	4
13	40	8	3
14	25	10	6
\.


--
-- Data for Name: warehouses; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.warehouses (id, active, address, name, department, district, province, ubigeo_code) FROM stdin;
1	t	Av. Venezuela 892, Breña 15082	Vinos y Rosas Almacen	LIMA	BREÑA	LIMA	150105
2	t	Av. Insurgentes 1819, Callao 07011	Viñera Parada	LIMA	LIMA	LIMA	150101
3	t	Av. Túpac Amaru 1500	Almacén Lima Norte	LIMA	COMAS	LIMA	150110
4	t	Jr. Cusco 200	Almacén Lima Centro	LIMA	LIMA	LIMA	150101
5	t	Av. Ejercito 800	Almacén Arequipa	AREQUIPA	AREQUIPA	AREQUIPA	040101
6	t	Carretera Panamericana 45	Almacén Ica	ICA	ICA	ICA	110101
7	f	Av. España 600	Almacén Trujillo (cerrado)	LA LIBERTAD	TRUJILLO	TRUJILLO	130101
\.



--
-- Sequence auto-generada para inventory_adjustments (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.inventory_adjustments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.inventory_adjustments_id_seq OWNED BY public.inventory_adjustments.id;

ALTER TABLE ONLY public.inventory_adjustments ALTER COLUMN id SET DEFAULT nextval('public.inventory_adjustments_id_seq'::regclass);

SELECT pg_catalog.setval('public.inventory_adjustments_id_seq', COALESCE((SELECT MAX(id) FROM public.inventory_adjustments), 1), true);



--
-- Sequence auto-generada para inventory_movements (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.inventory_movements_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.inventory_movements_id_seq OWNED BY public.inventory_movements.id;

ALTER TABLE ONLY public.inventory_movements ALTER COLUMN id SET DEFAULT nextval('public.inventory_movements_id_seq'::regclass);

SELECT pg_catalog.setval('public.inventory_movements_id_seq', COALESCE((SELECT MAX(id) FROM public.inventory_movements), 1), true);



--
-- Sequence auto-generada para stock_transfer_details (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.stock_transfer_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.stock_transfer_details_id_seq OWNED BY public.stock_transfer_details.id;

ALTER TABLE ONLY public.stock_transfer_details ALTER COLUMN id SET DEFAULT nextval('public.stock_transfer_details_id_seq'::regclass);

SELECT pg_catalog.setval('public.stock_transfer_details_id_seq', COALESCE((SELECT MAX(id) FROM public.stock_transfer_details), 1), true);



--
-- Sequence auto-generada para stock_transfers (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.stock_transfers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.stock_transfers_id_seq OWNED BY public.stock_transfers.id;

ALTER TABLE ONLY public.stock_transfers ALTER COLUMN id SET DEFAULT nextval('public.stock_transfers_id_seq'::regclass);

SELECT pg_catalog.setval('public.stock_transfers_id_seq', COALESCE((SELECT MAX(id) FROM public.stock_transfers), 1), true);



--
-- Sequence auto-generada para transfer_guide_items (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.transfer_guide_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.transfer_guide_items_id_seq OWNED BY public.transfer_guide_items.id;

ALTER TABLE ONLY public.transfer_guide_items ALTER COLUMN id SET DEFAULT nextval('public.transfer_guide_items_id_seq'::regclass);

SELECT pg_catalog.setval('public.transfer_guide_items_id_seq', COALESCE((SELECT MAX(id) FROM public.transfer_guide_items), 1), true);



--
-- Sequence auto-generada para transfer_guides (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.transfer_guides_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.transfer_guides_id_seq OWNED BY public.transfer_guides.id;

ALTER TABLE ONLY public.transfer_guides ALTER COLUMN id SET DEFAULT nextval('public.transfer_guides_id_seq'::regclass);

SELECT pg_catalog.setval('public.transfer_guides_id_seq', COALESCE((SELECT MAX(id) FROM public.transfer_guides), 1), true);



--
-- Sequence auto-generada para warehouse_stock (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.warehouse_stock_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.warehouse_stock_id_seq OWNED BY public.warehouse_stock.id;

ALTER TABLE ONLY public.warehouse_stock ALTER COLUMN id SET DEFAULT nextval('public.warehouse_stock_id_seq'::regclass);

SELECT pg_catalog.setval('public.warehouse_stock_id_seq', COALESCE((SELECT MAX(id) FROM public.warehouse_stock), 1), true);



--
-- Sequence auto-generada para warehouses (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.warehouses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.warehouses_id_seq OWNED BY public.warehouses.id;

ALTER TABLE ONLY public.warehouses ALTER COLUMN id SET DEFAULT nextval('public.warehouses_id_seq'::regclass);

SELECT pg_catalog.setval('public.warehouses_id_seq', COALESCE((SELECT MAX(id) FROM public.warehouses), 1), true);


--
-- PostgreSQL database dump complete
--

\unrestrict IsWnTjRd4cs9aBZbrW5USv4iWKwqBUe8MeCJpTccBTI56Z9IsCiiU528Bbn4zJV