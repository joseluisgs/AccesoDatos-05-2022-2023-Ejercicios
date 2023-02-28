import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.3"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	// Serialization
	kotlin("plugin.serialization") version "1.7.20"
	// Dokka
	id("org.jetbrains.dokka") version "1.7.20"
}

group = "resa.mario"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	// Spring R2DBC
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	// Spring Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	// Spring Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	// Spring WebFlux Reactive
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	// Spring WebSocket
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	// Spring Cache
	implementation("org.springframework.boot:spring-boot-starter-cache")
	//implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	// Serializable Json Kotlin
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	// Kotlin
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	// Base de datos
	//runtimeOnly("com.h2database:h2")
	runtimeOnly("io.r2dbc:r2dbc-h2")
	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
	// Mockk
	testImplementation("com.ninja-squad:springmockk:4.0.0")
	// Logs
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
	// JWT
	implementation("com.auth0:java-jwt:4.2.1")
	// Dokka
	dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
