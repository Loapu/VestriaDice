import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    idea
    id("com.github.johnrengelman.shadow") version "7.0.0"
}
group = project.property("pluginGroup") as String
version = project.property("pluginVersion") as String
repositories {
    mavenCentral()
    maven(uri("https://papermc.io/repo/repository/maven-public/"))
    maven(uri("https://jitpack.io"))
    maven(uri("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/"))
    maven(uri("https://repo.codemc.org/repository/maven-public/"))
    maven(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
    flatDir {
        dirs("libs")
    }
}
dependencies {
    implementation("io.papermc.paper:paper-api:" + project.property("versionPaper") as String)
    implementation("dev.jorel.CommandAPI:commandapi-core:" + project.property("versionCommandApi") as String)
    implementation("dev.jorel.CommandAPI:commandapi-annotations:" + project.property("versionCommandApi") as String)
    implementation("org.jetbrains:annotations:19.0.0")
    annotationProcessor("dev.jorel.CommandAPI:commandapi-annotations:" + project.property("versionCommandApi") as String)
    implementation("net.kyori:adventure-text-minimessage:" + project.property("versionMiniMessage") as String)
    implementation(name, "VentureChat-3.0.4")
}
sourceSets {
    main {
        java.srcDirs("src/main/java")
        resources.srcDirs("src/main/resources")
    }
}
tasks {
    register("bumpBuild") {
        doLast {
            val pluginBuildNumber = project.property("pluginBuildNumber") as String
            val build = Integer.parseInt(pluginBuildNumber) + 1
            val token = "pluginBuildNumber=$pluginBuildNumber"
            val value = "pluginBuildNumber=$build"
            ant.withGroovyBuilder {
                "replace"("file" to "gradle.properties", "token" to token, "value" to value)
            }
        }
    }
    compileJava {
        options.encoding = project.property("projectEncoding") as String
        options.release.set(Integer.parseInt(project.property("versionJava") as String))
    }
    shadowJar {
        dependencies {
            include(dependency("net.kyori:adventure-text-minimessage:" + project.property("versionMiniMessage") as String))
        }
        archiveBaseName.set(project.property("pluginName") as String)
        if (project.property("pluginVersion").toString().contains('-'))
        {
            archiveVersion.set("${project.version}+B" + project.property("pluginBuildNumber") as String)
        }
        archiveClassifier.set("")
    }
    processResources {
        outputs.upToDateWhen { false }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        val tokens = mapOf(
                "group" to project.property("pluginGroup") as String,
                "name" to project.property("pluginName") as String,
                "prefix" to project.property("pluginPrefix") as String,
                "ver" to project.property("pluginVersion") as String,
                "apiVer" to project.property("pluginApiVersion") as String,
                "author" to project.property("pluginAuthor") as String,
                "description" to project.property("pluginDescription") as String,
                "homepage" to project.property("pluginHomepage") as String
        )
        inputs.properties(tokens)
        from(sourceSets.main.get().resources.srcDirs) {
            filter<ReplaceTokens>("tokens" to tokens)
        }
    }
}
