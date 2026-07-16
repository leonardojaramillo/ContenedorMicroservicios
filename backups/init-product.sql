--
-- PostgreSQL database dump
--

\restrict oEajqL8GZKFJlcKJ7ObgCWSgzlZiqTasstItimrqyvwaduxHBSpchTcAsup3m7d

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
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    active boolean,
    category character varying(255),
    created_at timestamp(6) without time zone,
    description character varying(1000),
    image_url character varying(255),
    name character varying(255),
    price numeric(38,2),
    stock integer,
    volume integer,
    year integer,
    CONSTRAINT products_category_check CHECK (((category)::text = ANY ((ARRAY['VINO_TINTO'::character varying, 'VINO_BLANCO'::character varying, 'VINO_ROSADO'::character varying, 'ESPUMANTE'::character varying, 'PISCO'::character varying])::text[])))
);


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.products (id, active, category, created_at, description, image_url, name, price, stock, volume, year) FROM stdin;
2	t	VINO_TINTO	2026-05-12 19:51:14.515026	Vino tinto peruano	https://imagen.com/vino.jpg	Vino Intipalka Malbec	89.90	53	750	2022
1	t	VINO_TINTO	2026-05-12 19:27:03.684708	Vino tinto premium	https://imagen.com/vino.jpg	Vino Cabernet Sauvignon	89.90	37	750	2022
4	t	VINO_TINTO	2026-05-12 20:59:58.30529	Reserva	https://imagen.com/vino.jpg	Vino Tacama	120.00	39	750	2021
5	t	PISCO	2026-03-20 18:17:24.635018	Pisco acholado de añejamiento especial	\N	Pisco Acholado Reserva	45.90	120	750	2022
6	t	PISCO	2026-03-25 18:17:24.635018	Pisco puro de uva quebranta	\N	Pisco Quebranta Puro	38.50	85	750	2023
7	t	VINO_TINTO	2026-03-30 18:17:24.635018	Vino tinto cuerpo medio, barrica de roble	\N	Vino Tinto Reserva	52.00	60	750	2021
8	t	VINO_BLANCO	2026-04-04 18:17:24.635018	Vino blanco fresco y afrutado	\N	Vino Blanco Sauvignon	48.00	40	750	2023
9	t	VINO_ROSADO	2026-04-09 18:17:24.635018	Vino rosado ligero, notas frutales	\N	Vino Rosado Malbec	44.00	30	750	2023
10	t	ESPUMANTE	2026-04-14 18:17:24.635018	Espumante seco método tradicional	\N	Espumante Brut Nature	65.00	25	750	2022
11	t	PISCO	2026-04-19 18:17:24.635018	Pisco aromático de uva italia	\N	Pisco Italia Premium	50.00	4	750	2022
12	f	VINO_TINTO	2026-04-29 18:17:24.635018	Vino tinto cabernet sauvignon	\N	Vino Tinto Cabernet	55.00	0	750	2020
\.



--
-- Sequence auto-generada para products (para permitir INSERTs nuevos desde el microservicio)
--

CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);

SELECT pg_catalog.setval('public.products_id_seq', COALESCE((SELECT MAX(id) FROM public.products), 1), true);


--
-- PostgreSQL database dump complete
--

\unrestrict oEajqL8GZKFJlcKJ7ObgCWSgzlZiqTasstItimrqyvwaduxHBSpchTcAsup3m7d