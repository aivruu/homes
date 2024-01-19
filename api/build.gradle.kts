plugins {
  id("homes.common-conventions")
}

dependencies {
  compileOnlyApi(libs.paper)
  compileOnlyApi(libs.configurate.gson)
  compileOnlyApi(libs.mongo)
}

tasks {
  compileJava {
    options.encoding = "UTF-8"
    options.release = 17
  }
}