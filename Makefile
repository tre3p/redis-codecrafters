test:
	mvn clean test

lint:
	mvn clean verify -DskipTests

report:
	mvn jacoco:report