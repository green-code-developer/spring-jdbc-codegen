-- returningColumnsByTable の検証用
--   トリガーがDB 側で値を書き換え、その結果をreturning で取得できることを確認する
create table trigger_test (
    pk         bigserial primary key,
    col_text   text,
    updated_at timestamptz not null default '2000-01-01'
);

create or replace function set_updated_at() returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

create trigger trigger_test_set_updated_at
    before insert or update on trigger_test
    for each row execute function set_updated_at();
