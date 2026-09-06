-- ENTITY-010 / ENTITY-012 の検証用
--   not null かつリテラルの既定値を持つカラムは、プリミティブまたは非null で生成され、
--   DB の既定値がフィールドの初期値になる
create table primitive_default (
    pk              bigserial primary key,
    col_long        bigint           not null default -1,
    col_int         integer          not null default 7,
    col_short       smallint         not null default 3,
    col_bool        boolean          not null default true,
    col_double      double precision not null default 1.5,
    col_numeric     numeric          not null default 0.5,
    col_text        text             not null default 'X',
    col_timestamptz timestamptz      not null default '2000-01-01 00:00:00+09',
    col_date        date             not null default '2000-01-01',
    col_uuid        uuid             not null default '9529478b-20d7-4232-ba79-000000000001',
    col_enum        status_enum      not null default 'DONE',
    -- 既定値なし。@Nullable のままになる
    col_no_default  text             not null
);
