import sbt._

object HyperdocBuild extends Build {
  lazy val root = Project(id = "hyperdoc",
    base = file(".")) aggregate(core, model, persistence, server)

  lazy val core = Project(id = "hyperdoc-core", base = file("core"))

  lazy val model = Project(id = "hyperdoc-model", base = file("model")) dependsOn (
    core % "test -> test;compile -> compile")

  lazy val persistence = Project(id = "hyperdoc-persistence", base = file("persistence")) dependsOn (
    core % "test -> test;compile -> compile")

  lazy val server = Project(id = "hyperdoc-server", base = file("server")) dependsOn(
    core % "test -> test;compile -> compile",
    model % "test -> test;compile -> compile",
    persistence)
}
