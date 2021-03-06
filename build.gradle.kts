import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options

plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.7.2"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.0"
    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    java
}

val javaVersion = 17
group = "me.obsilabor"
version = "1.0.2+mc1.19"

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    // fabric
    minecraft("com.mojang:minecraft:1.19")
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.19+build.1:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.7")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.55.3+1.19")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.0+kotlin.1.7.0")
    // modmenu & clothconfig
    modApi("com.terraformersmc:modmenu:4.0.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:7.0.65") {
        exclude("net.fabricmc.fabric-api")
    }
}


kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

tasks {
    processResources {
        val props = mapOf(
            "version" to project.version,
            "mcVersion" to "${project.version.toString().split("mc")[1]}"
        )

        inputs.properties(props)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
    compileJava {
        options.release.set(javaVersion)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "$javaVersion"
    }
    named("curseforge") {
        onlyIf {
            System.getenv("CURSEFORGE_TOKEN") != null
        }
        dependsOn(remapJar)
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("VP0RHYaP")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    gameVersions.addAll(listOf("${project.version.toString().split("mc")[1]}"))
    loaders.add("fabric")
    loaders.add("quilt")
    dependencies {
        required.project("Ha28R6CL") // fabric language kotlin
        required.project("9s6osm5g") // cloth config
        optional.project("mOgUt4GM") // mod menu
    }

    uploadFile.set(tasks.remapJar.get())
}

curseforge {
    project(closureOf<CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        mainArtifact(tasks.remapJar.get())

        id = "632589"
        releaseType = "release"
        addGameVersion("${project.version.toString().split("mc")[1]}")
        addGameVersion("Java 17")
        addGameVersion("Fabric")
        addGameVersion("Quilt")

        relations(closureOf<CurseRelation> {
            requiredDependency("cloth-config")
            requiredDependency("fabric-language-kotlin")
            optionalDependency("modmenu")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}