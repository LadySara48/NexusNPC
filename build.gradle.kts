plugins {
    java
    id("com.gradleup.shadow") version "9.3.0" //ShadowJar
}

group = "me.hearlov"
version = "1.0-DEV"

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