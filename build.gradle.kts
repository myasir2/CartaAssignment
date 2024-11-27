plugins {
    kotlin("jvm") version "1.9.25"
}

group = "ca.myasir"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")
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
