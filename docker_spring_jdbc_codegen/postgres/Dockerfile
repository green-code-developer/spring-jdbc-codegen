# 日本語対応
# see also https://qiita.com/mayu_AJ/items/93db75f718a9c284e781
FROM postgres:17
RUN apt-get update && apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
    && localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8
ENV LANG="ja_JP.UTF-8" \
    LANGUAGE="ja_JP:ja" \
    LC_ALL="ja_JP.UTF-8" \
    TZ=Asia/Tokyo
ADD ./DDL/ /docker-entrypoint-initdb.d/
ADD ./psqlrc /root/.psqlrc