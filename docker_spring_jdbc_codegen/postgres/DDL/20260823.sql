-- カラム属性とparam.yml 設定の組み合わせ網羅用
--   型やPK 数ではなく、属性と設定の掛け合わせを検証する
create table coverage_test (
    pk                        bigserial primary key,
    -- nullable × default 有（insert 省略可にならないこと）
    col_nullable_default      text        default 'x',
    -- nonnull × default 無
    col_notnull_nodefault     text        not null,
    -- setNow × default 有（setNow 判定が省略可判定より優先されること）
    --   判定順を入れ替えても差が出るようdefault はnow() 以外にする
    col_now_with_default      timestamptz not null default '2000-01-01',
    -- update 対象外 × setNow（param.yml の"*" に登録済み）
    created_at                timestamptz not null default '2000-01-01',
    -- update 対象外 × default 無（param.yml の"*" に登録済み）
    created_by                text        not null,
    -- setNow × nullable（param.yml の"*" に登録済み）
    updated_at                timestamptz,
    -- update 対象外 × nullable（param.yml のテーブル名指定に登録）
    col_no_update_nullable    text,
    -- enum × nonnull × default 有（insert 省略可）
    col_enum_default          status_enum not null default 'NEW',
    -- enum × nullable × default 有
    col_enum_nullable_default status_enum default 'DOING',
    -- 明示マッピング × nullable
    mapped_nullable           text
);
