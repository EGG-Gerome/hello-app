plugins {
    java // 保留Java基础插件（子模块可继承）
    // 关键：SpringBoot和依赖管理插件「apply false」——父工程不用，子模块可继承
    id("org.springframework.boot") version "3.0.2" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

// 保留原有项目的group/version/description（子模块会默认继承）
group = "cloud.tangyuan"
version = "0.0.1-SNAPSHOT"
description = "hello-app"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    // 增加阿里云Maven镜像（优先用国内源）
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    mavenCentral() // 保留官方源兜底
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
