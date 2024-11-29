plugins {
    id("java")
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

    implementation("com.microsoft.onnxruntime:onnxruntime:1.15.0")
    implementation("ai.djl.huggingface:tokenizers:0.30.0")
}

tasks.test {
    useJUnitPlatform()
}