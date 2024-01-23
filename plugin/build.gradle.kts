plugins {
  id("homes.common-conventions")
  alias(libs.plugins.shadow)
  alias(libs.plugins.blossom)
}

val release = property("version") as String

dependencies {
  implementation(project(":api"))
  compileOnly(libs.paper)
  compileOnly(libs.command)
}

tasks {
  // We'll establish the encoding to UTF-8 and Java version release to 17, the minimum supported for this project.
  compileJava {
    options.encoding = "UTF-8"
    options.release = 17
  }

  // We'll replace the ${version} type with the 'release' value inside the resource.
  processResources {
    filesMatching("paper-plugin.yml") {
      expand("version" to release)
    }
  }

  shadowJar {
    archiveFileName.set("Homes-v$release.jar")
    destinationDirectory.set(file("$rootDir/bin/"))
  }
}

// We'll replace the {version} type inside the route expected.
blossom {
  val tokenRoute = "src/main/java/com/aivruu/homes/Constants.java"
  replaceToken("{version}", release, tokenRoute)
}