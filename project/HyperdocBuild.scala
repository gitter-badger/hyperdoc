import sbt._

object HyperdocBuild extends Build {
  lazy val root = Project(id = "hyperdoc",
    base = file(".")) aggregate(core, commit_log, backend)

  lazy val core = Project(id = "hyperdoc-core",
    base = file("core"))

  lazy val commit_log = Project(id = "hyperdoc-commit-log",
    base = file("commit-log")) dependsOn core

  lazy val backend = Project(id = "hyperdoc-backend",
    base = file("backend")) dependsOn core
}
