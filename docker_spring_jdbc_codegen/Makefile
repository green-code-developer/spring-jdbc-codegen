.PHONY: build

docker:
	@make down
	@make up
	@make ps
up:
	# DDL はイメージに焼き込まれるため --build が必要
	docker compose up -d --build
down:
	-docker compose down
prune:
	-docker container prune -f
	-docker image prune -fa
ps:
	docker compose ps
stats:
	docker stats
sh:
	docker compose exec postgres bash
sql:
	docker compose exec postgres psql -Uspring_jdbc_codegen spring_jdbc_codegen
help:
	echo usage make up,down,prune,ps,stats,sql,log
log:
	docker compose logs --follow
