@file:Suppress("UnstableApiUsage")

rootProject.name = "homes"

sequenceOf("api", "infrastructure", "mongodb", "json", "plugin").forEach {
  val kerbalProject = ":${rootProject.name}-$it"
  include(kerbalProject)
  if (it.equals("mongodb") || it.equals("json")) {
    project(kerbalProject).projectDir = file("infrastructure/$it")
  } else {
    project(kerbalProject).projectDir = file(it)
  }
}
