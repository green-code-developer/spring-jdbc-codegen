.PHONY: jar docker

jar:
	cd generator && ../gradlew clean fatJar
	ls -l generator/build/libs/*.jar

docker:
	cd docker_spring_jdbc_codegen && $(MAKE) docker
