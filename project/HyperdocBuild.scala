import sbt._

object HyperdocBuild extends Build {
  // Versions
  lazy val akkaVersion = "2.4.0"

  lazy val root = Project(id = "hyperdoc",
    base = file(".")) aggregate(core, model, journal, search, graph, server)

  lazy val core = Project(id = "hyperdoc-core", base = file("core"))

  lazy val model = Project(id = "hyperdoc-model", base = file("model")) dependsOn (
    core % "test -> test;compile -> compile")

  lazy val journal = Project(id = "hyperdoc-journal", base = file("journal")) dependsOn (
    core % "test -> test;compile -> compile")

  lazy val search = Project(id = "hyperdoc-search", base = file("search")) dependsOn (
    core % "test -> test;compile -> compile",
    journal % "test -> test;compile -> compile")

  lazy val graph = Project(id = "hyperdoc-graph", base = file("graph")) dependsOn (
    core % "test -> test;compile -> compile",
    journal % "test -> test;compile -> compile")

  lazy val server = Project(id = "hyperdoc-server", base = file("server")) dependsOn(
    core % "test -> test;compile -> compile",
    model % "test -> test;compile -> compile",
    journal, graph, search)
}
