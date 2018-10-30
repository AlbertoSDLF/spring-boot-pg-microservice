# spring-boot-pg-microservice
Spring Boot PostgreSQL microservice

Spring Data JPA, JaCoCo, SonarQube, Embedded H2, Swagger, Cucumber

SonarQube configuration in Maven settings.xml

	<pluginGroup>org.sonarsource.scanner.maven</pluginGroup>


	<profile>
        <id>sonar</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <sonar.host.url>
                http://localhost:9000
            </sonar.host.url>
			<sonar.language>java</sonar.language>
			<sonar.sources>src/main</sonar.sources>
			<sonar.tests>src/test</sonar.tests>
			<sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
			<sonar.dynamicAnalysis>reuseReports</sonar.dynamicAnalysis>
			<sonar.jacoco.reportPaths>${project.build.directory}/jacoco.exec</sonar.jacoco.reportPaths>
			<sonar.surefire.reportsPath>${project.build.directory}/surefire-reports</sonar.surefire.reportsPath>
			<sonar.junit.reportPaths>${project.build.directory}/surefire-reports</sonar.junit.reportPaths>
			<sonar.issue.ignore.allfile>r1</sonar.issue.ignore.allfile>
			<sonar.issue.ignore.allfile.r1.fileRegexp>@lombok\.Generated</sonar.issue.ignore.allfile.r1.fileRegexp>
			<!-- <sonar.exclusions></sonar.exclusions> -->
        </properties>
    </profile>
