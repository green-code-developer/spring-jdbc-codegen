.PHONY: jar docker verify golden-update

jar:
	cd generator && ../gradlew clean fatJar
	ls -l generator/build/libs/*.jar

docker:
	cd docker_spring_jdbc_codegen && $(MAKE) docker

# 生成結果がgolden と一致するか検証する (要 make docker)
verify:
	cd generator && ../gradlew test
	cd test-app && ../gradlew test --rerun-tasks

# golden を現在の生成結果で更新する。差分は必ずレビューすること
#   DDL を変更した場合は先に make docker でDB を作り直すこと
golden-update:
	# 実体クラスは生成時に削除されないため、出力先を先に消す
	rm -rf generator/build/golden-actual
	cd generator && ../gradlew generateGolden
	rm -rf generator/src/test/resources/golden/expected
	cp -R generator/build/golden-actual generator/src/test/resources/golden/expected
	git diff --stat generator/src/test/resources/golden/expected
