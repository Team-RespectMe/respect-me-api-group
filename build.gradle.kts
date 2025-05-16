import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.bmuschko.docker-remote-api") version "9.3.1"
}

group = "kr.respect-me"
version = "0.5.5"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }

}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("com.github.Team-RespectMe:respect-me-api-common:v0.0.1")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
//    implementation("org.postgresql:postgresql:42.5.4")
    runtimeOnly("org.postgresql:postgresql:42.5.4")
    runtimeOnly("com.h2database:h2:2.1.214")
    implementation("org.flywaydb:flyway-core:9.16.3")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // B3 Propagation 구현 라이브러리
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    // query dsl
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    implementation("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:2.1.214")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("querydsl.jpa", true)
    }
}

tasks.register<DockerBuildImage>("buildProdImage") {
    dependsOn("bootJar")
    inputDir.set(file(".")) // Dockerfile이 위치한 경로 (프로젝트 root 경로)
    images.add("elensar92/respect-me-group-api:${version}-prod")
    group = "docker"
}

tasks.register<DockerPushImage>("pushProdImage") {
    dependsOn("buildProdImage")
    images.add("elensar92/respect-me-group-api:${version}-prod")
    group = "docker"
}


tasks.register<DockerBuildImage>("buildTestImage") {
    dependsOn("bootJar")
    inputDir.set(file(".")) // Dockerfile이 위치한 경로 (프로젝트 root 경로)
    images.add("elensar92/respect-me-group-api:${version}-test")
    group = "docker"
}

tasks.register<DockerPushImage>("pushTestImage") {
    dependsOn("buildTestImage")
    images.add("elensar92/respect-me-group-api:${version}-test")
    group = "docker"
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("${rootProject.group}.group-api")
    archiveVersion.set("latest")
}

tasks {
    bootRun {
        jvmArgs = listOf("-Dspring.profiles.active=local")
    }
}

tasks.test {
    useJUnitPlatform()
}

