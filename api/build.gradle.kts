plugins {
  id("homes.common-conventions")
}

dependencies {
  compileOnly(libs.paper)
  compileOnly(libs.configurate.gson)
  compileOnly(libs.mongo)
}

tasks {
  compileJava {
    options.encoding = "UTF-8"
    options.release = 17
  }
}