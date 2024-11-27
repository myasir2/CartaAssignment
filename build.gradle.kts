plugins {
    kotlin("jvm") version "1.9.25"
    application
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "ca.myasir"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")

    // Util dependencies
    implementation("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:12.1.1")
    implementation("commons-io:commons-io:2.18.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.25")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()

    jvmArgs(
        "--add-opens=java.base/java.time=ALL-UNNAMED",
    )
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

ktlint {
    ignoreFailures = true
    filter {
        exclude("**/test/**")
    }
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

application {
    mainClass.set("MainKt")
}

val programArgs = project.findProperty("args")?.toString()?.split(",") ?: emptyList()

tasks.named<JavaExec>("run") {
    args = programArgs
}

tasks.named("build") {
    dependsOn("ktlintFormat")
}

