--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.23
-- Dumped by pg_dump version 9.6.23

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

--
-- Name: administracion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA administracion;


ALTER SCHEMA administracion OWNER TO postgres;

--
-- Name: portal; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA portal;


ALTER SCHEMA portal OWNER TO postgres;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: func_actualizar_catalogo(integer, character varying, boolean, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_catalogo(p_cata_id integer, p_cata_nombre character varying, p_cata_estado boolean, p_cata_descripcion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin

update catalogo set cata_nombre=p_cata_nombre,cata_estado=p_cata_estado,cata_descripcion=p_cata_descripcion where cata_id =p_cata_id;
    
return true;
end;$$;


ALTER FUNCTION public.func_actualizar_catalogo(p_cata_id integer, p_cata_nombre character varying, p_cata_estado boolean, p_cata_descripcion character varying) OWNER TO postgres;

--
-- Name: func_actualizar_categoria(integer, character varying, boolean, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_categoria(p_cate_id integer, p_cate_nombre character varying, p_cate_estado boolean, p_cate_descripcion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin

update categoria set cate_nombre=p_cate_nombre,cate_estado=p_cate_estado,cate_descripcion=p_cate_descripcion where cate_id =p_cate_id;
    
return true;
end;$$;


ALTER FUNCTION public.func_actualizar_categoria(p_cate_id integer, p_cate_nombre character varying, p_cate_estado boolean, p_cate_descripcion character varying) OWNER TO postgres;

--
-- Name: func_actualizar_documento(integer, character varying, text, boolean, boolean, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_documento(p_docu_id integer, p_docu_titulo character varying, p_docu_resumen text, p_docu_privado boolean, p_docu_publico boolean, p_tido_id integer, p_cate_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin


update documento set docu_titulo=p_docu_titulo,docu_resumen=p_docu_resumen,docu_privado=p_docu_privado,docu_publico=p_docu_publico,tido_id=p_tido_id,cate_id=p_cate_id	where docu_id =p_docu_id;
    
return true;
end;$$;


ALTER FUNCTION public.func_actualizar_documento(p_docu_id integer, p_docu_titulo character varying, p_docu_resumen text, p_docu_privado boolean, p_docu_publico boolean, p_tido_id integer, p_cate_id integer) OWNER TO postgres;

--
-- Name: func_actualizar_documento(integer, character varying, text, boolean, boolean, integer, integer, text, text, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_documento(p_docu_id integer, p_docu_titulo character varying, p_docu_resumen text, p_docu_privado boolean, p_docu_publico boolean, p_tido_id integer, p_cate_id integer, p_docu_fecha_doc text, p_docu_data text, p_tido_version integer, p_docu_metadata text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin


update documento set docu_titulo=p_docu_titulo,docu_resumen=p_docu_resumen,docu_privado=p_docu_privado,docu_publico=p_docu_publico,
tido_id=p_tido_id,cate_id=p_cate_id,docu_fecha_docx=p_docu_fecha_doc, docu_data = p_docu_data, tido_version = p_tido_version, docu_metadata = p_docu_metadata 
where docu_id =p_docu_id;
    
return true;
end;$$;


ALTER FUNCTION public.func_actualizar_documento(p_docu_id integer, p_docu_titulo character varying, p_docu_resumen text, p_docu_privado boolean, p_docu_publico boolean, p_tido_id integer, p_cate_id integer, p_docu_fecha_doc text, p_docu_data text, p_tido_version integer, p_docu_metadata text) OWNER TO postgres;

--
-- Name: func_actualizar_metadata(integer, character varying, boolean, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_metadata(p_meta_id integer, p_meta_descripcion character varying, p_meta_manual boolean, p_meta_valores text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare resultado boolean = true;
begin

    UPDATE metadata
   SET meta_descripcion=p_meta_descripcion, meta_manual=p_meta_manual, meta_valores = p_meta_valores 
 WHERE meta_id = p_meta_id;

return resultado;
end;$$;


ALTER FUNCTION public.func_actualizar_metadata(p_meta_id integer, p_meta_descripcion character varying, p_meta_manual boolean, p_meta_valores text) OWNER TO postgres;

--
-- Name: func_actualizar_password_usuario(integer, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_password_usuario(p_usua_id integer, p_usua_password character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
begin
resultado:=true;


UPDATE usuario
   SET  usua_password=p_usua_password
 WHERE usua_id=p_usua_id;


return resultado;
end;$$;


ALTER FUNCTION public.func_actualizar_password_usuario(p_usua_id integer, p_usua_password character varying) OWNER TO postgres;

--
-- Name: func_actualizar_tipo_documento(integer, character varying, boolean, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_tipo_documento(p_tido_id integer, p_tido_nombre character varying, p_tido_estado boolean, p_tido_descripcion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin

update tipo_documento set tido_nombre=p_tido_nombre,tido_estado=p_tido_estado,tido_descripcion=p_tido_descripcion where tido_id =p_tido_id;
    
return true;
end;$$;


ALTER FUNCTION public.func_actualizar_tipo_documento(p_tido_id integer, p_tido_nombre character varying, p_tido_estado boolean, p_tido_descripcion character varying) OWNER TO postgres;

--
-- Name: func_actualizar_usuario(integer, character, character varying, character varying, character varying, character varying, character varying, boolean, boolean, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_actualizar_usuario(p_usua_id integer, p_usua_dni character, p_usua_nombres character varying, p_usua_apellido_paterno character varying, p_usua_apellido_materno character varying, p_usua_direccion character varying, p_usua_email character varying, p_usua_administrador boolean, p_usua_estado boolean, p_cata_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
begin
resultado:=true;


UPDATE usuario
   SET  usua_dni=p_usua_dni, usua_nombres=p_usua_nombres, usua_apellido_paterno=p_usua_apellido_paterno, 
       usua_apellido_materno=p_usua_apellido_materno, usua_direccion=p_usua_direccion, usua_email=p_usua_email, usua_administrador=p_usua_administrador, 
       usua_estado=p_usua_estado, cata_id=p_cata_id
 WHERE usua_id=p_usua_id;


return resultado;
end;$$;


ALTER FUNCTION public.func_actualizar_usuario(p_usua_id integer, p_usua_dni character, p_usua_nombres character varying, p_usua_apellido_paterno character varying, p_usua_apellido_materno character varying, p_usua_direccion character varying, p_usua_email character varying, p_usua_administrador boolean, p_usua_estado boolean, p_cata_id integer) OWNER TO postgres;

--
-- Name: func_borrar_documento(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_borrar_documento(p_docu_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin

--primero borramos toda las etiquetas del documento
DELETE FROM documento_etiqueta
 WHERE docu_id=p_docu_id;

DELETE FROM documento
 where docu_id =p_docu_id;
    
return true;
end;$$;


ALTER FUNCTION public.func_borrar_documento(p_docu_id integer) OWNER TO postgres;

--
-- Name: func_eliminar_campos_tipo_documento(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_eliminar_campos_tipo_documento(p_tido_id integer, p_meta_id integer, p_tido_version integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
registro campos_tipo_documento%ROWTYPE;
begin

if exists(select c.tido_id from campos_tipo_documento c inner join documento d on c.tido_id = d.tido_id 
		and c.tido_version = d.tido_version where c.catd_estado = true and c.tido_id = p_tido_id) then
	total:=(select max(tido_version) from campos_tipo_documento where tido_id = p_tido_id);
	FOR registro in select * from campos_tipo_documento where tido_id = p_tido_id and catd_estado = true loop
		insert into campos_tipo_documento(tido_id, meta_id, catd_estado, tido_version)
		VALUES (registro.tido_id, registro.meta_id, true, total + 1);
	end loop;
	update campos_tipo_documento set catd_estado = false where tido_id = p_tido_id and tido_version = total;
	total:=total+1;
else
	total:=(select max(tido_version) from campos_tipo_documento where tido_id = p_tido_id);
end if;

delete from campos_tipo_documento where tido_id = p_tido_id and meta_id = p_meta_id and tido_version = total;
if not exists(select * from campos_tipo_documento where tido_id = p_tido_id and catd_estado = true) then
	total:=(select max(tido_version) from campos_tipo_documento where tido_id = p_tido_id);
	update campos_tipo_documento set catd_estado = true where tido_id = p_tido_id and tido_version = total;
end if;
return resultado;
end;$$;


ALTER FUNCTION public.func_eliminar_campos_tipo_documento(p_tido_id integer, p_meta_id integer, p_tido_version integer) OWNER TO postgres;

--
-- Name: func_existe_catalogo(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_existe_catalogo(p_cata_nombre character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
	DECLARE
	numero int:=0;
	BEGIN
	numero:= (select count(*)::int from catalogo where cata_nombre=upper(trim(p_cata_nombre)));
if numero >0 then
numero:=1;
end if;

	RETURN numero;
	END;
$$;


ALTER FUNCTION public.func_existe_catalogo(p_cata_nombre character varying) OWNER TO postgres;

--
-- Name: func_existe_categoria(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_existe_categoria(p_cate_nombre character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
	DECLARE
	numero int:=0;
	BEGIN
	numero:= (select count(*)::int from categoria where cate_nombre=upper(trim(p_cate_nombre)));
if numero >0 then
numero:=1;
end if;

	RETURN numero;
	END;
$$;


ALTER FUNCTION public.func_existe_categoria(p_cate_nombre character varying) OWNER TO postgres;

--
-- Name: func_existe_tipo_documento(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_existe_tipo_documento(p_tido_nombre character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
	DECLARE
	numero int:=0;
	BEGIN
	numero:= (select count(*)::int from tipo_documento where tido_nombre=upper(trim(p_tido_nombre)));
if numero >0 then
numero:=1;
end if;

	RETURN numero;
	END;
$$;


ALTER FUNCTION public.func_existe_tipo_documento(p_tido_nombre character varying) OWNER TO postgres;

--
-- Name: func_existe_usuario(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_existe_usuario(p_usua_usuario character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
	DECLARE
	numero int:=0;
	BEGIN
	numero:= (select count(*)::int from usuario where usua_usuario=upper(trim(p_usua_usuario)));
	if numero >0 then
		numero:=1;
	end if;

	RETURN numero;
	END;
$$;


ALTER FUNCTION public.func_existe_usuario(p_usua_usuario character varying) OWNER TO postgres;

--
-- Name: func_insertar_campos_tipo_documento(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_campos_tipo_documento(p_tido_id integer, p_meta_id integer, p_tido_version integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
registro campos_tipo_documento%ROWTYPE;
begin

if exists(select c.tido_id from campos_tipo_documento c inner join documento d on c.tido_id = d.tido_id 
		and c.tido_version = d.tido_version where c.catd_estado = true and c.tido_id = p_tido_id) then
	total:=(select max(tido_version) from campos_tipo_documento where tido_id = p_tido_id);
	FOR registro in select * from campos_tipo_documento where tido_id = p_tido_id and catd_estado = true loop
		insert into campos_tipo_documento(tido_id, meta_id, catd_estado, tido_version)
		VALUES (registro.tido_id, registro.meta_id, true, total + 1);
	end loop;
	update campos_tipo_documento set catd_estado = false where tido_id = p_tido_id and tido_version = total;
	total:=total+1;
else
	total:=(select max(tido_version) from campos_tipo_documento where tido_id = p_tido_id);
	if total is null then
	   total:=1;
	end if;
end if;
if not exists(select tido_id from campos_tipo_documento where tido_id = p_tido_id and tido_version = total and meta_id = p_meta_id) then
INSERT INTO campos_tipo_documento(
            tido_id, meta_id, catd_estado, tido_version)
    VALUES (p_tido_id, p_meta_id, true, total);
end if;
return resultado;
end;$$;


ALTER FUNCTION public.func_insertar_campos_tipo_documento(p_tido_id integer, p_meta_id integer, p_tido_version integer) OWNER TO postgres;

--
-- Name: func_insertar_catalogo(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_catalogo(p_cata_nombre character varying, p_cata_descripcion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
begin

total:=(select max(cata_id) from catalogo );
if total is null then
   total:=0;   
end if;
   total:=total+1;
INSERT INTO catalogo(
            cata_id, cata_nombre,cata_estado,cata_descripcion)
    VALUES (total, upper(p_cata_nombre),true,upper(p_cata_descripcion));



return total;
end;$$;


ALTER FUNCTION public.func_insertar_catalogo(p_cata_nombre character varying, p_cata_descripcion character varying) OWNER TO postgres;

--
-- Name: func_insertar_categoria(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_categoria(p_cate_nombre character varying, p_cate_descripcion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare total int;
begin
total:=(select max(cate_id) from categoria );
if total is null then
   total:=0;   
end if;
   total:=total+1;
INSERT INTO categoria(
            cate_id, cate_nombre,cate_estado,cate_descripcion)
    VALUES (total, upper(p_cate_nombre),true,upper(p_cate_descripcion));



return total;
end;$$;


ALTER FUNCTION public.func_insertar_categoria(p_cate_nombre character varying, p_cate_descripcion character varying) OWNER TO postgres;

--
-- Name: func_insertar_documento(integer, character varying, integer, text, text, numeric); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_documento(p_usua_id integer, p_docu_titulo character varying, p_cata_id integer, p_docu_origen_archivo text, p_docu_mime_types text, p_docu_version numeric) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare total int;
begin
total:=(select max(docu_id) from documento);
if total is null then
total:=0;
end if;
total:=total+1;



INSERT INTO documento(
            docu_id, usua_id, docu_titulo, docu_resumen, docu_privado, docu_publico, 
            tido_id, docu_estado,docu_fecha,cata_id,docu_version,docu_origen_archivo,docu_mime_types,cate_id, tido_version)
    VALUES (total, p_usua_id, p_docu_titulo, '', true, false, 7, true,now()::date,p_cata_id,p_docu_version,p_docu_origen_archivo,p_docu_mime_types,1, 0);

return total;
end;$$;


ALTER FUNCTION public.func_insertar_documento(p_usua_id integer, p_docu_titulo character varying, p_cata_id integer, p_docu_origen_archivo text, p_docu_mime_types text, p_docu_version numeric) OWNER TO postgres;

--
-- Name: func_insertar_etiqueta(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_etiqueta(p_etiq_nombre character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
begin

total:=(select max(etiq_id) from etiqueta );
if total is null then
   total:=0;   
end if;
   total:=total+1;
INSERT INTO etiqueta(
            etiq_id, etiq_nombre)
    VALUES (total, upper(p_etiq_nombre));

return total;
end;$$;


ALTER FUNCTION public.func_insertar_etiqueta(p_etiq_nombre character varying) OWNER TO postgres;

--
-- Name: func_insertar_historial_documento(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_historial_documento(p_hido_docuref integer, p_hido_docupa integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare total int;
begin
total:=(select max(hido_id) from historial_documentos);
if total is null then
total:=0;
end if;
total:=total+1;



INSERT INTO historial_documentos(
            hido_id, hido_docuref,hido_docupa)
    VALUES (total,p_hido_docuref ,p_hido_docupa);

    update documento set docu_estado=false where docu_id=p_hido_docupa;

return total;
end;$$;


ALTER FUNCTION public.func_insertar_historial_documento(p_hido_docuref integer, p_hido_docupa integer) OWNER TO postgres;

--
-- Name: func_insertar_metadata(integer, character varying, boolean, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_metadata(p_meta_id integer, p_meta_descripcion character varying, p_meta_manual boolean, p_meta_valores text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
begin

total:=(select max(meta_id) from metadata );
if total is null then
   total:=0;   
end if;
   total:=total+1;
   
INSERT INTO metadata(
            meta_id, meta_descripcion, meta_manual, meta_valores)
    VALUES (total, p_meta_descripcion, p_meta_manual, p_meta_valores);

return total;
end;$$;


ALTER FUNCTION public.func_insertar_metadata(p_meta_id integer, p_meta_descripcion character varying, p_meta_manual boolean, p_meta_valores text) OWNER TO postgres;

--
-- Name: func_insertar_tag_documento(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_tag_documento(p_docu_id integer, p_etiq_id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
begin

total:=(select max(doet_id) from documento_etiqueta );
if total is null then
   total:=0;   
end if;
   total:=total+1;
INSERT INTO documento_etiqueta(
            doet_id, docu_id,etiq_id)
    VALUES (total, p_docu_id,p_etiq_id);



return total;
end;$$;


ALTER FUNCTION public.func_insertar_tag_documento(p_docu_id integer, p_etiq_id integer) OWNER TO postgres;

--
-- Name: func_insertar_tipo_documento(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_tipo_documento(p_tido_nombre character varying, p_tido_descripcion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
begin

total:=(select max(tido_id) from tipo_documento );
if total is null then
   total:=0;   
end if;
   total:=total+1;
INSERT INTO tipo_documento(
            tido_id, tido_nombre,tido_estado,tido_descripcion)
    VALUES (total, upper(p_tido_nombre),true,upper(p_tido_descripcion));

return total;
end;$$;


ALTER FUNCTION public.func_insertar_tipo_documento(p_tido_nombre character varying, p_tido_descripcion character varying) OWNER TO postgres;

--
-- Name: func_insertar_usuario(character, character varying, character varying, character varying, character varying, character varying, boolean, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_insertar_usuario(p_usua_dni character, p_usua_nombres character varying, p_usua_apellido_paterno character varying, p_usua_apellido_materno character varying, p_usua_direccion character varying, p_usua_email character varying, p_usua_administrador boolean, p_usua_usuario character varying, p_usua_password character varying, p_cata_id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare total int;
begin

total:=(select max(usua_id) from usuario);
if total is null then
total:=0;
end if;
total:=total+1;

INSERT INTO usuario(
            usua_id, usua_dni, usua_nombres, usua_apellido_paterno, usua_apellido_materno, 
            usua_direccion, usua_email, usua_usuario, usua_password, 
            usua_estado,cata_id,usua_administrador)
    VALUES (total,trim(p_usua_dni), upper(trim(p_usua_nombres)), upper(trim(p_usua_apellido_paterno)), upper(trim(p_usua_apellido_materno)), 
            upper(trim(p_usua_direccion)),trim(p_usua_email),upper(trim(p_usua_usuario)), p_usua_password, 
            true,p_cata_id, p_usua_administrador);


return total;
end;$$;


ALTER FUNCTION public.func_insertar_usuario(p_usua_dni character, p_usua_nombres character varying, p_usua_apellido_paterno character varying, p_usua_apellido_materno character varying, p_usua_direccion character varying, p_usua_email character varying, p_usua_administrador boolean, p_usua_usuario character varying, p_usua_password character varying, p_cata_id integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: campos_tipo_documento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.campos_tipo_documento (
    tido_id integer NOT NULL,
    meta_id integer NOT NULL,
    catd_estado boolean,
    tido_version integer NOT NULL,
    tipo_dato character varying(25),
    obligado boolean DEFAULT false,
    defecto text,
    secuencia integer
);


ALTER TABLE public.campos_tipo_documento OWNER TO postgres;

--
-- Name: metadata; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.metadata (
    meta_id integer NOT NULL,
    meta_descripcion character varying(250),
    meta_manual boolean,
    meta_valores text,
    meta_estado boolean
);


ALTER TABLE public.metadata OWNER TO postgres;

--
-- Name: vista_campos_tipo_documento; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vista_campos_tipo_documento AS
 SELECT m.meta_id,
    m.meta_descripcion,
        CASE
            WHEN (c.catd_estado = true) THEN true
            ELSE false
        END AS catd_estado,
        CASE
            WHEN (c.tido_id IS NULL) THEN '-1'::integer
            ELSE c.tido_id
        END AS tido_id
   FROM (public.metadata m
     LEFT JOIN public.campos_tipo_documento c ON ((m.meta_id = c.meta_id)));


ALTER TABLE public.vista_campos_tipo_documento OWNER TO postgres;

--
-- Name: func_mostrar_campos_documento(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_mostrar_campos_documento(p_tido_id integer) RETURNS SETOF public.vista_campos_tipo_documento
    LANGUAGE plpgsql
    AS $$
	DECLARE
	registro vista_campos_tipo_documento%ROWTYPE;
	BEGIN
	
	FOR registro in  SELECT m.meta_id, m.meta_descripcion, catd_estado, c.tido_id FROM metadata m INNER JOIN 
	campos_tipo_documento c ON m.meta_id = c.meta_id where c.tido_id = p_tido_id and c.catd_estado = true
	union SELECT m.meta_id, m.meta_descripcion, true, -1 as tido_id FROM metadata m where m.meta_id not 
	in(select meta_id from campos_tipo_documento where tido_id = p_tido_id and catd_estado = true) order by meta_descripcion LOOP
		
		RETURN NEXT registro;
		
	END LOOP;
	
	END;
$$;


ALTER FUNCTION public.func_mostrar_campos_documento(p_tido_id integer) OWNER TO postgres;

--
-- Name: func_quitar_tag_documento(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_quitar_tag_documento(p_docu_id integer, p_etiq_id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$


begin



DELETE FROM documento_etiqueta where docu_id=p_docu_id and etiq_id=p_etiq_id;

return 1;
end;$$;


ALTER FUNCTION public.func_quitar_tag_documento(p_docu_id integer, p_etiq_id integer) OWNER TO postgres;

--
-- Name: func_verificar_version_tipo_documento(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_verificar_version_tipo_documento(p_tido_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare resultado boolean;
declare total int;
begin

if exists(select c.tido_id from campos_tipo_documento c inner join documento d on c.tido_id = d.tido_id 
		and c.tido_version = d.tido_version where c.catd_estado = true and tido_id = p_tido_id) then
	resultado:=true;
else
	resultado:=false;
end if;

return resultado;
end;$$;


ALTER FUNCTION public.func_verificar_version_tipo_documento(p_tido_id integer) OWNER TO postgres;

--
-- Name: isvaliddate(character, character); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.isvaliddate(character, character) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE
     result BOOL;
BEGIN
     SELECT TO_CHAR(TO_DATE($1,$2),$2) = $1
     INTO result;
     RETURN result;
END;
$_$;


ALTER FUNCTION public.isvaliddate(character, character) OWNER TO postgres;

--
-- Name: str_normalize(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.str_normalize(value text) RETURNS text
    LANGUAGE plpgsql IMMUTABLE
    AS $_$
BEGIN
RETURN upper(translate(value, 'áàéèíìóòúùäëïöüÁÀÉÈÍÌÓÒÚÙÄËÏÖÜñÑçÇ"?¿¡[]`{},:;=&$#|!\ºª<>-/', 'aaeeiioouuaeiouAAEEIIOOUUAEIOUnNcC '));
END;
$_$;


ALTER FUNCTION public.str_normalize(value text) OWNER TO postgres;

--
-- Name: cargo; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.cargo (
    carg_id integer NOT NULL,
    depe_id integer NOT NULL,
    pers_id integer NOT NULL,
    carg_responsable integer NOT NULL,
    fecha_inicio date,
    fecha_final date,
    carg_estado boolean NOT NULL
);


ALTER TABLE administracion.cargo OWNER TO postgres;

--
-- Name: custom_person; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.custom_person (
    docu_id integer NOT NULL,
    cupe_dni character varying(200) NOT NULL,
    fecha_hora timestamp without time zone DEFAULT now()
);


ALTER TABLE administracion.custom_person OWNER TO postgres;

--
-- Name: dependencia; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.dependencia (
    depe_id integer NOT NULL,
    depe_nombre character varying(100) NOT NULL,
    depe_abreviatura character varying(20),
    depe_estado boolean,
    depe_padre integer DEFAULT 0,
    depe_nivel integer DEFAULT 0,
    ejecutora boolean DEFAULT false,
    saip boolean DEFAULT false
);


ALTER TABLE administracion.dependencia OWNER TO postgres;

--
-- Name: dependencia_persona; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.dependencia_persona (
    deus_id integer NOT NULL,
    depe_id integer NOT NULL,
    pers_id integer NOT NULL
);


ALTER TABLE administracion.dependencia_persona OWNER TO postgres;

--
-- Name: entidad; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.entidad (
    enti_id integer NOT NULL,
    enti_nombre character varying(50),
    enti_abreviatura character varying(10) NOT NULL,
    enti_estado boolean,
    enti_direccion text,
    enti_ruc character varying(11),
    enti_representante_legal character varying(100),
    enti_carpeta_siaf character varying(250),
    enti_secuencia_ejecutora character(6),
    enti_frase text,
    enti_logo bytea,
    enti_pie text,
    enti_version text,
    enti_dni_representante character varying(8)
);


ALTER TABLE administracion.entidad OWNER TO postgres;

--
-- Name: etiqueta; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.etiqueta (
    etiq_id integer NOT NULL,
    etiq_descripcion character varying(120),
    etiq_url character varying(150),
    etiq_estado boolean,
    etiq_icon character varying(400)
);


ALTER TABLE administracion.etiqueta OWNER TO postgres;

--
-- Name: grupo; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.grupo (
    grup_id integer NOT NULL,
    grup_nombre character varying(100) NOT NULL,
    grup_estado boolean NOT NULL,
    grup_visible boolean
);


ALTER TABLE administracion.grupo OWNER TO postgres;

--
-- Name: grupo_rol; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.grupo_rol (
    grup_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE administracion.grupo_rol OWNER TO postgres;

--
-- Name: mes; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.mes (
    mes_nombre character varying(20) NOT NULL,
    mes_activo boolean,
    peri_anio character(4) NOT NULL,
    mes_id character(2) NOT NULL
);


ALTER TABLE administracion.mes OWNER TO postgres;

--
-- Name: modulo; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.modulo (
    modu_id integer NOT NULL,
    modu_nombre character varying(250) NOT NULL,
    modu_abreviatura character varying(10),
    modu_estado boolean NOT NULL,
    modu_descripcion text,
    modu_url character varying(150),
    modu_tipo_acceso integer
);


ALTER TABLE administracion.modulo OWNER TO postgres;

--
-- Name: modulo_grupo; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.modulo_grupo (
    id_g integer NOT NULL,
    id_m integer NOT NULL
);


ALTER TABLE administracion.modulo_grupo OWNER TO postgres;

--
-- Name: notify; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.notify (
    id integer NOT NULL,
    id_p character varying(6),
    descripcion text,
    id_t character varying(11),
    estado boolean,
    fecha timestamp with time zone,
    tabla character(1),
    id_w integer
);


ALTER TABLE administracion.notify OWNER TO postgres;

--
-- Name: notify_persona; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.notify_persona (
    id integer NOT NULL,
    id_p character varying(6),
    id_n integer,
    verify boolean,
    estado boolean,
    mensaje text,
    fecha timestamp without time zone DEFAULT now()
);


ALTER TABLE administracion.notify_persona OWNER TO postgres;

--
-- Name: COLUMN notify_persona.verify; Type: COMMENT; Schema: administracion; Owner: postgres
--

COMMENT ON COLUMN administracion.notify_persona.verify IS '
';


--
-- Name: periodo; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.periodo (
    peri_anio character(4) NOT NULL,
    peri_activo boolean,
    peri_descripcion character varying(250)
);


ALTER TABLE administracion.periodo OWNER TO postgres;

--
-- Name: permiso; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.permiso (
    role_id integer NOT NULL,
    sumo_id integer NOT NULL,
    perm_acceso boolean DEFAULT false,
    perm_xml xml
);


ALTER TABLE administracion.permiso OWNER TO postgres;

--
-- Name: permisos; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.permisos (
    role_id integer NOT NULL,
    sumo_id integer NOT NULL,
    perm_nuevo boolean,
    perm_editar boolean,
    perm_eliminar boolean,
    perm_imprimir boolean,
    perm_ver boolean,
    perm_publicar boolean
);


ALTER TABLE administracion.permisos OWNER TO postgres;

--
-- Name: persona; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.persona (
    pers_id integer NOT NULL,
    pers_dni character varying(15) NOT NULL,
    pers_nombre character varying(30),
    pers_apellido_paterno character varying(30),
    pers_apellido_materno character varying(30),
    pers_estado boolean DEFAULT true,
    pers_iniciales character varying(10),
    pers_cargo text,
    pers_global_id character varying(6),
    tipo character(1)
);


ALTER TABLE administracion.persona OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.roles (
    role_id integer NOT NULL,
    role_nombre character varying(100) NOT NULL,
    role_estado boolean NOT NULL
);


ALTER TABLE administracion.roles OWNER TO postgres;

--
-- Name: sesion; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.sesion (
    sesi_id bigint NOT NULL,
    sesi_key character varying(250) NOT NULL,
    sesi_fecha_ingreso date,
    sesi_fecha_salida date,
    sesi_hora_ingreso time without time zone,
    sesi_hora_salida time without time zone,
    sesi_ip character varying(100),
    usua_id integer,
    depe_id integer,
    sesi_estado boolean
);


ALTER TABLE administracion.sesion OWNER TO postgres;

--
-- Name: session; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.session (
    sesi_id character varying(250) NOT NULL,
    fecha_ingreso timestamp with time zone DEFAULT now(),
    fecha_salida timestamp with time zone,
    sesi_ip character varying(20),
    usua_id integer,
    depe_id integer,
    sesi_estado boolean
);


ALTER TABLE administracion.session OWNER TO postgres;

--
-- Name: sub_modulo; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.sub_modulo (
    sumo_id integer NOT NULL,
    sumo_nombre character varying(250) NOT NULL,
    sumo_url character varying(100),
    modu_id integer NOT NULL,
    sumo_estado boolean NOT NULL,
    etiq_id integer,
    sumo_padre integer,
    sumo_orden integer
);


ALTER TABLE administracion.sub_modulo OWNER TO postgres;

--
-- Name: thema; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.thema (
    the_id integer NOT NULL,
    the_nombre character varying(60),
    the_img character varying(50),
    the_path character varying(60)
);


ALTER TABLE administracion.thema OWNER TO postgres;

--
-- Name: thema_usuario; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.thema_usuario (
    thus_id integer NOT NULL,
    modu_id integer,
    the_id integer,
    usua_id integer
);


ALTER TABLE administracion.thema_usuario OWNER TO postgres;

--
-- Name: usuario; Type: TABLE; Schema: administracion; Owner: postgres
--

CREATE TABLE administracion.usuario (
    usua_id integer NOT NULL,
    usua_login character varying(20) NOT NULL,
    usua_clave character varying(40),
    usua_fecha_registro date,
    usua_fecha_caducidad date,
    usua_estado boolean,
    usua_firma_digital character varying(50),
    grup_id integer NOT NULL,
    pers_id integer NOT NULL,
    turn_id integer,
    usua_pin character varying(4)
);


ALTER TABLE administracion.usuario OWNER TO postgres;

--
-- Name: config_docu; Type: TABLE; Schema: portal; Owner: postgres
--

CREATE TABLE portal.config_docu (
    tipo integer NOT NULL,
    etiqueta integer NOT NULL,
    hijo_1 integer,
    hijo_2 integer
);


ALTER TABLE portal.config_docu OWNER TO postgres;

--
-- Name: carpeta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carpeta (
    carp_id integer NOT NULL,
    carp_tipo integer,
    carp_ruta character varying(500),
    carp_propietario character varying(8)
);


ALTER TABLE public.carpeta OWNER TO postgres;

--
-- Name: catalogo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.catalogo (
    cata_id integer NOT NULL,
    cata_nombre character varying(100),
    cata_estado boolean,
    cata_descripcion text,
    cata_carpeta character varying(200),
    cata_eliminado boolean DEFAULT false
);


ALTER TABLE public.catalogo OWNER TO postgres;

--
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    cate_id integer NOT NULL,
    cate_nombre character varying(100) NOT NULL,
    cate_estado boolean,
    cate_descripcion text
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- Name: documento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documento (
    docu_id integer NOT NULL,
    usua_id integer NOT NULL,
    docu_titulo character varying(500) NOT NULL,
    docu_resumen text DEFAULT '  '::text NOT NULL,
    docu_privado boolean NOT NULL,
    docu_publico boolean NOT NULL,
    tido_id integer,
    docu_estado boolean,
    docu_fecha date DEFAULT now(),
    cata_id integer DEFAULT 1,
    docu_version numeric(4,1) DEFAULT 1,
    docu_origen_archivo text,
    docu_mime_types text,
    cate_id integer DEFAULT 1,
    docu_fecha_docx text,
    docu_data text,
    tido_version integer,
    docu_metadata text,
    docu_ruta text,
    docu_hora timestamp without time zone DEFAULT now(),
    propietario character varying(8),
    personalizado boolean,
    docu_mini_titulo character varying(500),
    docu_mini_resumen text,
    docu_search tsvector,
    docu_group bigint,
    docu_codigo text,
    sub_id integer DEFAULT 1,
    orden integer DEFAULT 1,
    solo boolean DEFAULT false,
    sub_group integer DEFAULT 1,
    sub_group1 integer DEFAULT 1,
    sub_group2 integer DEFAULT 2
);


ALTER TABLE public.documento OWNER TO postgres;

--
-- Name: documento_etiqueta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documento_etiqueta (
    doet_id integer NOT NULL,
    docu_id integer NOT NULL,
    etiq_id integer NOT NULL
);


ALTER TABLE public.documento_etiqueta OWNER TO postgres;

--
-- Name: documento_usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documento_usuario (
    pers_dni character varying(8) NOT NULL,
    docu_id integer NOT NULL
);


ALTER TABLE public.documento_usuario OWNER TO postgres;

--
-- Name: etiqueta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.etiqueta (
    etiq_id integer NOT NULL,
    etiq_nombre text NOT NULL,
    etiq_estado boolean,
    etiq_nivel integer
);


ALTER TABLE public.etiqueta OWNER TO postgres;

--
-- Name: etiqueta_tipo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.etiqueta_tipo (
    etiq_id integer NOT NULL,
    tido_id integer NOT NULL
);


ALTER TABLE public.etiqueta_tipo OWNER TO postgres;

--
-- Name: grupo_etiqueta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grupo_etiqueta (
    id integer NOT NULL,
    etiq_id integer,
    modulo character varying(250),
    sistema integer,
    idmodulo integer
);


ALTER TABLE public.grupo_etiqueta OWNER TO postgres;

--
-- Name: log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log (
    log_tabla character varying(100) NOT NULL,
    log_fecha_time timestamp without time zone DEFAULT now() NOT NULL,
    log_dni character varying(8) NOT NULL,
    log_descripcion text,
    log_tabla_id integer
);


ALTER TABLE public.log OWNER TO postgres;

--
-- Name: log_oscinfor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log_oscinfor (
    log_id integer NOT NULL,
    log_fecha timestamp without time zone,
    "NUM_THABILITANTE" character varying(400),
    "TITULAR" character varying(400),
    "MODALIDAD" character varying(400),
    "CADUCADO_TH" character varying(2),
    "NOMBRE_POA" character varying(400),
    "ARESOLUCION_NUM" character varying(400),
    "COD_INFORME" character varying(400),
    "NUM_INFORME" character varying(400),
    "FECHA_SUPERVISION_INI" character varying(400),
    "FECHA_SUPERVISION_FIN" character varying(400),
    "NUM_RESOLUCION_INI" character varying(400),
    "FECHA_EMISION_INI" character varying(400),
    "MED_CAU" character varying(400),
    "INFRACCIONES_INI" character varying(400),
    "NUM_RESOLUCION_AMP" character varying(400),
    "FECHA_EMISION_AMP" character varying(400),
    "INFRACCIONES_AMP" character varying(400),
    "NUM_RESOLUCION_VAIMP" character varying(400),
    "FECHA_EMISION_VAIMP" character varying(400),
    "INFRACCIONES_VAIMP" character varying(400),
    "NUM_RESOLUCION_TER" character varying(400),
    "FECHA_EMISION_TER" character varying(400),
    "DETER_TER" character varying(400),
    "MONTO_MULTA_TER" character varying(400),
    "AMONESTACION" character varying(400),
    "CADUCIDAD" character varying(400),
    "MED_PRECAU" character varying(400),
    "MED_CORREC" character varying(400),
    "NUM_RESOLUCION_RECONS" character varying(400),
    "FECHA_EMISION_RECONS" character varying(400),
    "DETER_RECONS" character varying(400),
    "LEV_CADUCIDAD_RECONS" character varying(400),
    "CAMBIO_MULTA_RECONS" character varying(400),
    "MONTO_MULTA_RECONS" character varying(400),
    "NUM_RESOLUCION_RECTI" character varying(400),
    "FECHA_EMISION_RECTI" character varying(400),
    "MOTIVO_RECTI" character varying(400),
    "MONTO_MULTA_RECTI" character varying(400),
    "NUM_RESOLUCION_TFFS" character varying(400),
    "FECHA_EMISION_TFFS" character varying(400),
    "ESTADO_PAU" character varying(400),
    "FECHA_ACTUALIZACION" character varying(400)
);


ALTER TABLE public.log_oscinfor OWNER TO postgres;

--
-- Name: mine_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mine_type (
    mine_id integer NOT NULL,
    mine_nombre character varying(100),
    mine_ext character varying(5),
    mine_estado boolean
);


ALTER TABLE public.mine_type OWNER TO postgres;

--
-- Name: notificacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notificacion (
    id integer NOT NULL,
    fecha_vencimiento date,
    fecha_actual date DEFAULT now(),
    veces integer,
    proceso integer DEFAULT 0,
    pers_dni character varying(8),
    docu_id integer
);


ALTER TABLE public.notificacion OWNER TO postgres;

--
-- Name: permiso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permiso (
    dni character varying(8) NOT NULL,
    activo boolean,
    item1 boolean,
    item2 boolean,
    item3 boolean,
    item4 boolean,
    item5 boolean,
    tipo integer,
    item6 boolean,
    item7 boolean,
    item8 boolean,
    item9 boolean DEFAULT true,
    item10 boolean DEFAULT true
);


ALTER TABLE public.permiso OWNER TO postgres;

--
-- Name: rutas_rapida; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rutas_rapida (
    ruta character varying(300),
    cata_id integer,
    dni character varying(8),
    "time" timestamp without time zone DEFAULT now()
);


ALTER TABLE public.rutas_rapida OWNER TO postgres;

--
-- Name: siado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.siado (
    siad_id integer NOT NULL,
    siad_tituto_habitante character varying(1000),
    siad_mes_inicio character varying(5),
    siad_anio_inicio character varying(255),
    siad_area_ubicacion character varying(255),
    sedo_id character varying(255),
    sudo_id character varying(255),
    siad_numero_sitd character varying(255),
    siad_anio_fin character varying(255),
    siad_descripcion character varying(1000),
    siad_contrasenia character varying(255),
    siad_titular character varying(1000),
    siad_mes_fin character varying(5),
    siad_observacion character varying(1000),
    siad_estado boolean,
    siad_regente boolean,
    siad_fecha_envio character varying(255),
    siad_numero_expediente character varying(255),
    siad_numero_tramite character varying(255),
    estado_tramite_id integer,
    siad_sector_id integer
);


ALTER TABLE public.siado OWNER TO postgres;

--
-- Name: siado_detalle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.siado_detalle (
    side_id integer NOT NULL,
    side_tipodoc_id character varying(255),
    side_subtipodoc_id character varying(255),
    side_det_subtipodoc_id character varying(255),
    side_bregente boolean,
    side_num_zafra character varying(255),
    side_numero character varying(255),
    side_description character varying(1000),
    side_observacion character varying(1000),
    side_sregente character varying(255),
    side_anho_aprobacion character varying(255),
    side_anho_fin character varying(255),
    side_usuario_envio character varying(255),
    side_opcion character varying(255),
    side_siado_id integer,
    side_fecha_envio character varying(255),
    side_documento_id integer
);


ALTER TABLE public.siado_detalle OWNER TO postgres;

--
-- Name: sub_tipo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_tipo (
    id integer NOT NULL,
    nombre text,
    estado boolean,
    tido_id integer
);


ALTER TABLE public.sub_tipo OWNER TO postgres;

--
-- Name: sub_tipo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sub_tipo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sub_tipo_id_seq OWNER TO postgres;

--
-- Name: sub_tipo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sub_tipo_id_seq OWNED BY public.sub_tipo.id;


--
-- Name: tipo_documento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_documento (
    tido_id integer NOT NULL,
    tido_nombre character varying(50),
    tido_estado boolean,
    tido_descripcion text,
    tido_metadata text,
    tido_privacidad integer DEFAULT 2,
    cate_id integer NOT NULL
);


ALTER TABLE public.tipo_documento OWNER TO postgres;

--
-- Name: tmp_report1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tmp_report1 (
    titulo character varying(300),
    fecha timestamp without time zone,
    personal character varying(250),
    tipo_documento character varying(250)
);


ALTER TABLE public.tmp_report1 OWNER TO postgres;

--
-- Name: usuario_catalogo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario_catalogo (
    pers_id integer NOT NULL,
    cata_id integer NOT NULL
);


ALTER TABLE public.usuario_catalogo OWNER TO postgres;

--
-- Name: sub_tipo id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_tipo ALTER COLUMN id SET DEFAULT nextval('public.sub_tipo_id_seq'::regclass);


--
-- Name: cargo cargo_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.cargo
    ADD CONSTRAINT cargo_pk PRIMARY KEY (carg_id);


--
-- Name: dependencia_persona dependencia_persona_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.dependencia_persona
    ADD CONSTRAINT dependencia_persona_pk PRIMARY KEY (deus_id);


--
-- Name: entidad enti_id; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.entidad
    ADD CONSTRAINT enti_id PRIMARY KEY (enti_id);


--
-- Name: grupo grupo_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.grupo
    ADD CONSTRAINT grupo_pk PRIMARY KEY (grup_id);


--
-- Name: grupo_rol grupo_rol_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.grupo_rol
    ADD CONSTRAINT grupo_rol_pk PRIMARY KEY (grup_id, role_id);


--
-- Name: modulo_grupo id_m_g_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.modulo_grupo
    ADD CONSTRAINT id_m_g_pk PRIMARY KEY (id_g, id_m);


--
-- Name: notify_persona id_np; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.notify_persona
    ADD CONSTRAINT id_np PRIMARY KEY (id);


--
-- Name: notify id_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.notify
    ADD CONSTRAINT id_pk PRIMARY KEY (id);


--
-- Name: custom_person id_pk_cp; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.custom_person
    ADD CONSTRAINT id_pk_cp PRIMARY KEY (docu_id, cupe_dni);


--
-- Name: permiso id_pk_perm; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.permiso
    ADD CONSTRAINT id_pk_perm PRIMARY KEY (role_id, sumo_id);


--
-- Name: mes mes_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.mes
    ADD CONSTRAINT mes_pk PRIMARY KEY (peri_anio, mes_id);


--
-- Name: modulo modulo_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.modulo
    ADD CONSTRAINT modulo_pk PRIMARY KEY (modu_id);


--
-- Name: periodo periodo_pkey; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.periodo
    ADD CONSTRAINT periodo_pkey PRIMARY KEY (peri_anio);


--
-- Name: permisos permisos_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.permisos
    ADD CONSTRAINT permisos_pk PRIMARY KEY (role_id, sumo_id);


--
-- Name: usuario pers_id_enti_id; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.usuario
    ADD CONSTRAINT pers_id_enti_id PRIMARY KEY (usua_id);


--
-- Name: etiqueta pk_eti_id; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.etiqueta
    ADD CONSTRAINT pk_eti_id PRIMARY KEY (etiq_id);


--
-- Name: persona pk_pers; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.persona
    ADD CONSTRAINT pk_pers PRIMARY KEY (pers_id, pers_dni);


--
-- Name: dependencia pkey; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.dependencia
    ADD CONSTRAINT pkey PRIMARY KEY (depe_id);


--
-- Name: roles roles_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.roles
    ADD CONSTRAINT roles_pk PRIMARY KEY (role_id);


--
-- Name: session sesi_id_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.session
    ADD CONSTRAINT sesi_id_pk PRIMARY KEY (sesi_id);


--
-- Name: sesion sesion_pkey; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.sesion
    ADD CONSTRAINT sesion_pkey PRIMARY KEY (sesi_id);


--
-- Name: sub_modulo sub_modulo_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.sub_modulo
    ADD CONSTRAINT sub_modulo_pk PRIMARY KEY (sumo_id);


--
-- Name: thema thema_id_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.thema
    ADD CONSTRAINT thema_id_pk PRIMARY KEY (the_id);


--
-- Name: thema_usuario thus_id_pk; Type: CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.thema_usuario
    ADD CONSTRAINT thus_id_pk PRIMARY KEY (thus_id);


--
-- Name: config_docu id_pk_conf; Type: CONSTRAINT; Schema: portal; Owner: postgres
--

ALTER TABLE ONLY portal.config_docu
    ADD CONSTRAINT id_pk_conf PRIMARY KEY (tipo, etiqueta);


--
-- Name: carpeta carp_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carpeta
    ADD CONSTRAINT carp_id_pk PRIMARY KEY (carp_id);


--
-- Name: catalogo catalogo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.catalogo
    ADD CONSTRAINT catalogo_pkey PRIMARY KEY (cata_id);


--
-- Name: categoria categoria_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pk PRIMARY KEY (cate_id);


--
-- Name: documento_etiqueta documento_etiqueta_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_etiqueta
    ADD CONSTRAINT documento_etiqueta_pk PRIMARY KEY (doet_id);


--
-- Name: documento documento_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_pk PRIMARY KEY (docu_id);


--
-- Name: documento_usuario du_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_usuario
    ADD CONSTRAINT du_id_pk PRIMARY KEY (pers_dni, docu_id);


--
-- Name: etiqueta etiqueta_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.etiqueta
    ADD CONSTRAINT etiqueta_pk PRIMARY KEY (etiq_id);


--
-- Name: etiqueta_tipo etiqueta_tipo_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.etiqueta_tipo
    ADD CONSTRAINT etiqueta_tipo_pk PRIMARY KEY (etiq_id);


--
-- Name: grupo_etiqueta grupo_etiqueta_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grupo_etiqueta
    ADD CONSTRAINT grupo_etiqueta_pk PRIMARY KEY (id);


--
-- Name: log id_log_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT id_log_pk PRIMARY KEY (log_tabla, log_fecha_time, log_dni);


--
-- Name: permiso id_perm_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permiso
    ADD CONSTRAINT id_perm_pk PRIMARY KEY (dni);


--
-- Name: log_oscinfor log_id_osc_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_oscinfor
    ADD CONSTRAINT log_id_osc_pk PRIMARY KEY (log_id);


--
-- Name: metadata meta_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metadata
    ADD CONSTRAINT meta_id PRIMARY KEY (meta_id);


--
-- Name: campos_tipo_documento meta_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campos_tipo_documento
    ADD CONSTRAINT meta_id_pk PRIMARY KEY (tido_id, meta_id, tido_version);


--
-- Name: mine_type mine_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mine_type
    ADD CONSTRAINT mine_id_pk PRIMARY KEY (mine_id);


--
-- Name: notificacion noti_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notificacion
    ADD CONSTRAINT noti_id_pk PRIMARY KEY (id);


--
-- Name: usuario_catalogo pk_cata_usua_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario_catalogo
    ADD CONSTRAINT pk_cata_usua_pk PRIMARY KEY (pers_id, cata_id);


--
-- Name: siado pk_tramite_siado_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siado
    ADD CONSTRAINT pk_tramite_siado_id PRIMARY KEY (siad_id);


--
-- Name: siado_detalle siado_detalle_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siado_detalle
    ADD CONSTRAINT siado_detalle_pk PRIMARY KEY (side_id);


--
-- Name: sub_tipo sub_tipo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_tipo
    ADD CONSTRAINT sub_tipo_pkey PRIMARY KEY (id);


--
-- Name: tipo_documento tipo_documento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_documento
    ADD CONSTRAINT tipo_documento_pkey PRIMARY KEY (tido_id);


--
-- Name: docu_search_index_1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX docu_search_index_1 ON public.documento USING gist (docu_search);


--
-- Name: índice_ruta; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "índice_ruta" ON public.documento USING btree (docu_ruta);


--
-- Name: cargo dependencia_cargo_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.cargo
    ADD CONSTRAINT dependencia_cargo_fk FOREIGN KEY (depe_id) REFERENCES administracion.dependencia(depe_id);


--
-- Name: dependencia_persona dependencia_depencia_usuario; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.dependencia_persona
    ADD CONSTRAINT dependencia_depencia_usuario FOREIGN KEY (depe_id) REFERENCES administracion.dependencia(depe_id);


--
-- Name: grupo_rol grupo_grupo_rol_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.grupo_rol
    ADD CONSTRAINT grupo_grupo_rol_fk FOREIGN KEY (grup_id) REFERENCES administracion.grupo(grup_id);


--
-- Name: usuario grupo_usuario_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.usuario
    ADD CONSTRAINT grupo_usuario_fk FOREIGN KEY (grup_id) REFERENCES administracion.grupo(grup_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: notify_persona id_n_f; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.notify_persona
    ADD CONSTRAINT id_n_f FOREIGN KEY (id_n) REFERENCES administracion.notify(id);


--
-- Name: mes mes_peri_anio_fkey; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.mes
    ADD CONSTRAINT mes_peri_anio_fkey FOREIGN KEY (peri_anio) REFERENCES administracion.periodo(peri_anio);


--
-- Name: sub_modulo modulo_sub_modulo_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.sub_modulo
    ADD CONSTRAINT modulo_sub_modulo_fk FOREIGN KEY (modu_id) REFERENCES administracion.modulo(modu_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: grupo_rol roles_grupo_rol_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.grupo_rol
    ADD CONSTRAINT roles_grupo_rol_fk FOREIGN KEY (role_id) REFERENCES administracion.roles(role_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: permisos roles_permisos_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.permisos
    ADD CONSTRAINT roles_permisos_fk FOREIGN KEY (role_id) REFERENCES administracion.roles(role_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: permisos sub_modulo_permisos_fk; Type: FK CONSTRAINT; Schema: administracion; Owner: postgres
--

ALTER TABLE ONLY administracion.permisos
    ADD CONSTRAINT sub_modulo_permisos_fk FOREIGN KEY (sumo_id) REFERENCES administracion.sub_modulo(sumo_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: usuario_catalogo cata_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario_catalogo
    ADD CONSTRAINT cata_id_fk FOREIGN KEY (cata_id) REFERENCES public.catalogo(cata_id);


--
-- Name: documento catalogo_documento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT catalogo_documento_fk FOREIGN KEY (cata_id) REFERENCES public.catalogo(cata_id);


--
-- Name: documento documento_cate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_cate_id_fkey FOREIGN KEY (cate_id) REFERENCES public.categoria(cate_id);


--
-- Name: documento_etiqueta documento_documento_etiqueta_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_etiqueta
    ADD CONSTRAINT documento_documento_etiqueta_fk FOREIGN KEY (docu_id) REFERENCES public.documento(docu_id);


--
-- Name: documento_etiqueta documento_etiqueta_etiq_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento_etiqueta
    ADD CONSTRAINT documento_etiqueta_etiq_id_fkey FOREIGN KEY (etiq_id) REFERENCES public.etiqueta(etiq_id);


--
-- Name: documento documento_tido_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_tido_id_fkey FOREIGN KEY (tido_id) REFERENCES public.tipo_documento(tido_id);


--
-- Name: grupo_etiqueta grupo_etiqueta_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grupo_etiqueta
    ADD CONSTRAINT grupo_etiqueta_fk FOREIGN KEY (etiq_id) REFERENCES public.etiqueta(etiq_id);


--
-- Name: sub_tipo sub_tipo_tido_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_tipo
    ADD CONSTRAINT sub_tipo_tido_id_fkey FOREIGN KEY (tido_id) REFERENCES public.tipo_documento(tido_id);


--
-- Name: campos_tipo_documento tipo_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campos_tipo_documento
    ADD CONSTRAINT tipo_id_fk FOREIGN KEY (tido_id) REFERENCES public.tipo_documento(tido_id);


--
-- PostgreSQL database dump complete
--

