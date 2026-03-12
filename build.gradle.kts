plugins {
    java
    id("com.gradleup.shadow") version "9.3.0" //ShadowJar
}

group = "me.hearlov"
version = "1.1.2-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("../libs/powernukkitx.jar"))
}

tasks {

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}