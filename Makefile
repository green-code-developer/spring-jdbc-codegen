jar:
	cd generator && ../gradlew clean fatJar
	ls -l generator/build/libs/*.jar
