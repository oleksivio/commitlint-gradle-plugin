val GITHUB = "https://github.com/oleksivio/commitlint-gradle-plugin"
val GROUP = "io.github.oleksivio"
val PROJECT_NAME = "commitlint"

plugins {
    val nebulaPluginVersion = "10.0.1"
    id("com.gradle.plugin-publish") version "0.10.1"
    kotlin("jvm") version ("1.3.30")
    `java-gradle-plugin`
    id("nebula.maven-publish") version nebulaPluginVersion
    id("nebula.javadoc-jar") version nebulaPluginVersion
    id("nebula.source-jar") version nebulaPluginVersion
    id("nebula.release") version nebulaPluginVersion
}

group = GROUP

repositories {
    jcenter()
}

gradlePlugin {
    plugins {
        create("commitlintPlugin") {
            id = "$GROUP.$PROJECT_NAME"
            implementationClass = "$GROUP.$PROJECT_NAME.CommitlintPlugin"
        }
    }
}

pluginBundle {
    website = GITHUB
    vcsUrl = GITHUB

    description = "Commitlint "

    (plugins) {
        "commitlintPlugin" {
            displayName = "Commitlint plugin"
            tags = listOf("git", "commit", "commitlint", "conventions")
            version = project.version.toString()
        }

    }
    mavenCoordinates {
        groupId = GROUP
        artifactId = PROJECT_NAME
        version = project.version.toString()
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.3.0.201903130848-r")
}
