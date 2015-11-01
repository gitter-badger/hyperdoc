import sbt._

object HyperdocBuild extends Build {
  // Versions
  lazy val akkaVersion = "2.4.0"

  lazy val root = Project(id = "hyperdoc",
    base = file(".")) aggregate(core, model, journal, server)

  lazy val core = Project(id = "hyperdoc-core", base = file("core"))

  lazy val model = Project(id = "hyperdoc-model", base = file("model")) dependsOn (
    core % "test -> test;compile -> compile")

  lazy val journal = Project(id = "hyperdoc-journal", base = file("journal")) dependsOn (
    core % "test -> test;compile -> compile")

  lazy val server = Project(id = "hyperdoc-server", base = file("server")) dependsOn(
    core % "test -> test;compile -> compile",
    model % "test -> test;compile -> compile",
    journal)
}
