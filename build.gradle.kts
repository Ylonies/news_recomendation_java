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
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
