plugins {
  java
  alias(libs.plugins.spotless)
  alias(libs.plugins.indra)
  alias(libs.plugins.shadow)
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "net.kyori.indra")
  apply(plugin = "io.github.goooler.shadow")

  repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
  }
    
  indra {
    javaVersions {
      target(21)
      minimumToolchain(21)
    }
  }

  spotless {
    java {
      licenseHeaderFile("$rootDir/header/header.txt")
      trimTrailingWhitespace()
      indentWithSpaces(2)
    }
    kotlinGradle {
      trimTrailingWhitespace()
      indentWithSpaces(2)
    }
  }

  tasks {
    compileJava {
      dependsOn("spotlessApply")
      options.compilerArgs.add("-parameters")
    }
    
    shadowJar {
      archiveBaseName.set(project.name)
    }
  }
}
