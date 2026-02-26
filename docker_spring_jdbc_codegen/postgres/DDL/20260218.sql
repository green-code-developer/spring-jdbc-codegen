CREATE SCHEMA spring_jdbc_codegen AUTHORIZATION spring_jdbc_codegen;
ALTER ROLE spring_jdbc_codegen SET search_path = spring_jdbc_codegen;
GRANT ALL ON SCHEMA spring_jdbc_codegen TO spring_jdbc_codegen;
SET search_path TO spring_jdbc_codegen;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE OR REPLACE FUNCTION refresh_meta_columns()
RETURNS TRIGGER AS $$
BEGIN
    -- 常に updated_at を現在時刻にする
    NEW.updated_at = NOW();
    IF (TG_OP = 'INSERT') THEN
        -- 新規作成時は created_at を現在時刻にする
        NEW.created_at = NOW();
    ELSIF (TG_OP = 'UPDATE') THEN
        -- 更新時は OLD の値を維持して上書きを防止する
        NEW.created_at = OLD.created_at;
        NEW.created_by = OLD.created_by;
END IF;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TYPE status_enum AS ENUM ('NEW', 'DOING', 'DONE', 'DELETED');

-- 全ての型
CREATE TABLE all_types
(
    pk                   bigserial primary key,
    col_smallint         smallint,
    col_smallserial      smallserial,
    col_integer          integer,
    col_serial           serial,
    col_bigint           bigint,
    col_bigserial        bigserial,
    col_real             real,
    col_double_precision double precision,
    col_numeric          numeric(10, 2),
    col_boolean          boolean,
    col_char             character(10),
    col_varchar          character varying(50),
    col_text             text,
    col_date             date,
    col_time             time without time zone,
    col_time_tz          time with time zone,
    col_timestamp        timestamp without time zone,
    col_timestamp_tz     timestamp with time zone,
    col_interval interval,
    col_bytea            bytea,
    col_uuid             uuid,
    col_json             json,
    col_jsonb            jsonb,
    col_xml              xml,
    col_inet             inet,
    col_cidr             cidr,
    col_macaddr          macaddr,
    col_box              box,
    col_point            point,
    col_line             line,
    col_lseg             lseg,
    col_path             path,
    col_polygon          polygon,
    col_circle           circle,
    col_status_enum      status_enum
);

-- null とdefault のテスト PKなし（自動テスト対象外）
create table normal_pk0 (
    col_text text,
    col_text_not_null text not null,
    col_text_not_null_default_x text not null default 'X',
    col_text_default_y text not null default 'y'
);

-- null とdefault のテスト PK1個
create table normal_pk1 (
    pk bigserial primary key,
    col_text text,
    col_text_not_null text not null,
    col_text_not_null_default_x text not null default 'X',
    col_text_default_y text not null default 'y'
);

-- null とdefault のテスト PK3個
create table normal_pk3 (
    pk1 bigserial,
    pk2 timestamp with time zone not null default now(),
    pk3 uuid default gen_random_uuid(),
    col_text text,
    col_text_not_null text not null,
    col_text_not_null_default_x text not null default 'X',
    col_text_default_y text not null default 'y',
    primary key(pk1, pk2, pk3)
);

-- 省略可能カラム PKなし
create table omittable_pk0 (
    col_text_not_null_default_x text not null default 'X'
);

-- 省略可能カラム PK1個
create table omittable_pk1 (
    pk bigserial primary key,
    col_text_not_null_default_x text not null default 'X'
);

-- 省略可能カラム PK3個
create table omittable_pk3 (
    pk1 bigserial,
    pk2 timestamp with time zone not null default now(),
    pk3 uuid default gen_random_uuid(),
    col_text_not_null_default_x text not null default 'X',
    primary key(pk1, pk2, pk3)
);

-- Update 対象カラムなし PKなし
create table no_update_pk0 (
    col_no_update_text_not_null_default_x text not null default 'X'
);

-- Update 対象カラムなし PK1個
create table no_update_pk1 (
    pk bigserial primary key,
    col_no_update_text_not_null_default_x text not null default 'X'
);

-- Update 対象カラムなし PK3個
create table no_update_pk3 (
    pk1 bigserial,
    pk2 timestamp with time zone not null default now(),
    pk3 uuid default gen_random_uuid(),
    col_no_update_text_not_null_default_x text not null default 'X',
    primary key(pk1, pk2, pk3)
);

-- Now のみ PKなし
create table now_pk0 (
    col_now timestamp with time zone not null default now()
);

-- Now のみ PK1個
create table now_pk1 (
    pk bigserial primary key,
    col_now timestamp with time zone not null default now()
);

-- Now のみ PK3個
create table now_pk3 (
    pk1 bigserial,
    pk2 timestamp with time zone not null default now(),
    pk3 uuid default gen_random_uuid(),
    col_now timestamp with time zone not null default now(),
    primary key(pk1, pk2, pk3)
);

-- PK のみ PK1個
create table only_pk1 (
    pk bigserial primary key
);

-- PK のみ PK3個
create table only_pk3 (
    pk1 bigserial,
    pk2 timestamp with time zone not null default now(),
    pk3 uuid default gen_random_uuid(),
    primary key(pk1, pk2, pk3)
);

-- PK のみ PK3個 全てnow
create table only_pk3_now (
    pk1 timestamp with time zone not null default now(),
    -- pk2_now は param.yml でsetNow を指定している
    pk2_now timestamp with time zone not null,
    pk3 timestamp with time zone not null default now(),
    primary key(pk1, pk2_now, pk3)
);

-- 全てがPK
CREATE TABLE all_types_as_pk
(
    col_smallint         smallint,
    col_smallserial      smallserial,
    col_integer          integer,
    col_serial           serial,
    col_bigint           bigint,
    col_bigserial        bigserial,
    col_real             real,
    col_double_precision double precision,
    col_numeric          numeric(10, 2),
    col_boolean          boolean,
    col_char             character(10),
    col_varchar          character varying(50),
    col_text             text,
    col_date             date,
    col_time             time without time zone,
    col_time_tz          time with time zone,
    col_timestamp        timestamp without time zone,
    col_timestamp_tz     timestamp with time zone,
    col_interval interval,
    col_bytea            bytea,
    col_uuid             uuid,
    col_jsonb            jsonb,
    col_inet             inet,
    col_cidr             cidr,
    col_macaddr          macaddr,
    col_status_enum      status_enum,
    PRIMARY KEY (
        col_smallint,
        col_smallserial,
        col_integer,
        col_serial,
        col_bigint,
        col_bigserial,
        col_real,
        col_double_precision,
        col_numeric,
        col_boolean,
        col_char,
        col_varchar,
        col_text,
        col_date,
        col_time,
        col_time_tz,
        col_timestamp,
        col_timestamp_tz,
        col_interval,
        col_bytea,
        col_uuid,
        col_jsonb,
        col_inet,
        col_cidr,
        col_macaddr,
        col_status_enum
    )
);

create table "日本語Table" (
    "order" bigserial,
    "param" bigserial,
    "sql" bigserial,
    "helper" bigserial,
    "joining" bigserial,
    "List" bigserial,
    rename text,
    "where" timestamp,
    "select" text,
    "Abc" text,
    PRIMARY KEY ("order", "param", "sql", "helper", "joining", "List", rename)
);
