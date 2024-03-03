plugins {
    id("java")
    id("dev.architectury.loom") version("1.0-SNAPSHOT")
    id("architectury-plugin") version("3.4-SNAPSHOT")
    kotlin("jvm") version ("1.7.10")
}

group = "io.github.adainish"
version = "1.1-SNAPSHOT"

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    silentMojangMappingsLicense()

    mixin {
        defaultRefmapName.set("mixins.${project.name}.refmap.json")
    }
}

repositories {
    mavenCentral()
    maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.impactdev.net/repository/development/")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots1"
        mavenContent { snapshotsOnly() }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.15.6")

    //modImplementation(fabricApi.module("fabric-command-api-v2", "0.92.0+1.19.2"))
    modImplementation("dev.architectury", "architectury-fabric", "9.2.14")

    modImplementation("ca.landonjw.gooeylibs:api:3.0.0-1.20.1-SNAPSHOT")
    modImplementation("com.cobblemon:mod:1.4.1+1.20.1-SNAPSHOT")
    implementation("net.impactdev.impactor.api:economy:5.1.1-SNAPSHOT")
    implementation("net.impactdev.impactor.api:core:5.1.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.14.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}