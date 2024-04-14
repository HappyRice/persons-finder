import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	id("org.springframework.boot") version "2.7.0"
	id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.persons.finder"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

openApi {
	apiDocsUrl.set("http://localhost:8080/v2/api-docs")
	outputDir.set(file("$projectDir"))
	outputFileName.set("swagger.json")
	waitTimeInSeconds.set(10)
}

configure<SourceSetContainer> {
	named("main") {
		java.srcDir("src/main/kotlin")
	}
	named("test") {
		java.srcDir("src/test/kotlin")
	}
}

configurations {
	all {
		exclude("org.springframework.boot", "spring-boot-starter-logging")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("com.h2database:h2:2.1.212")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.springfox:springfox-swagger2:2.9.2")
	implementation("io.springfox:springfox-swagger-ui:2.9.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}