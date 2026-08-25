-- 生成器の不具合を再現・回帰防止するためのテーブル

-- PK なし × 明示的マッピング
--   ROW_MAPPER の宣言はPK の有無に関わらず出力されるが、Mapper クラス本体は
--   PK があるときしか出力されていなかった。この組み合わせでコンパイルできなくなる
create table mapper_no_pk (
    rename_target text,
    other_col     text
);

-- クォートが必要なカラム名 × setNow
--   update のreturning 句だけカラム名がクォートされず、
--   大文字を含む識別子が小文字に畳まれて実行時エラーになる
create table quoted_column_now (
    pk        bigserial primary key,
    "Updated" timestamptz not null default '2000-01-01',
    col_text  text
);

-- setNow カラムが全てUPDATE 対象外のテーブル
--   returning の対象が0件になり "returning " という不正なSQL が生成されていた
--   created_at はparam.yml の "*" でsetNow とUPDATE 対象外の両方に登録されている
create table now_all_excluded (
    pk         bigserial primary key,
    created_at timestamptz not null default '2000-01-01',
    col_text   text
);
