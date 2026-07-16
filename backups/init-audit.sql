--
-- PostgreSQL database dump
--

\restrict hxhxnRNv2gMIGOGUacjjmokk5yeLrtenevcD6iyLgOsOSFEB1Y2E0U6bqOaSw7J

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
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.audit_logs (
    id bigint NOT NULL,
    action character varying(255) NOT NULL,
    description text,
    performed_by character varying(255),
    performed_by_email character varying(255),
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Data for Name: audit_logs; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.audit_logs (id, action, description, performed_by, performed_by_email, created_at) FROM stdin;
1	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 08:20:42.379547
2	MOVIMIENTO_CREADO	INCOME S/ 320 - Cuentas por cobrar	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 08:22:26.427296
3	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 08:39:43.885094
4	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 09:02:15.902271
5	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 09:15:59.51133
6	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 09:24:31.57501
7	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 09:38:23.003339
8	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 10:01:09.035128
9	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 17:12:56.696914
10	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 17:16:13.491733
11	LOGIN	Inicio de sesión: fabio.santos@unmsm.edu.pe	Fabio Raul Santos Paucar	fabio.santos@unmsm.edu.pe	2026-06-28 17:28:15.386922
12	LOGIN	Inicio de sesión: leonardo.jaramillo@unmsm.edu.pe	Leonardo Daniel Jaramillo Seminario	leonardo.jaramillo@unmsm.edu.pe	2026-06-28 17:32:59.120148
13	LOGIN	Inicio de sesión: leonardo.jaramillo@unmsm.edu.pe	Leonardo Daniel Jaramillo Seminario	leonardo.jaramillo@unmsm.edu.pe	2026-06-28 18:12:07.008249
14	LOGIN	Inicio de sesión: leonardo.jaramillo@unmsm.edu.pe	Leonardo Daniel Jaramillo Seminario	leonardo.jaramillo@unmsm.edu.pe	2026-06-28 18:14:32.601405
15	LOGIN	Inicio de sesión: leonardo.jaramillo@unmsm.edu.pe	Leonardo Daniel Jaramillo Seminario	leonardo.jaramillo@unmsm.edu.pe	2026-06-28 18:21:43.668386
16	LOGIN	Inicio de sesión: leonardo.jaramillo@unmsm.edu.pe	Leonardo Daniel Jaramillo Seminario	leonardo.jaramillo@unmsm.edu.pe	2026-06-28 18:22:14.702638
\.


--
-- PostgreSQL database dump complete
--

\unrestrict hxhxnRNv2gMIGOGUacjjmokk5yeLrtenevcD6iyLgOsOSFEB1Y2E0U6bqOaSw7J