plugins {
    id("java")
    application
    id("pmd")
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("com.microsoft.onnxruntime:onnxruntime:1.15.0")
    implementation("ai.djl.huggingface:tokenizers:0.30.0")
}

pmd {
    isConsoleOutput = true
    toolVersion = "7.0.0"
    rulesMinimumPriority = 5
    ruleSets = listOf("category/java/errorprone.xml", "category/java/bestpractices.xml")
    threads = 6
}

tasks.test {
    useJUnitPlatform()
}
