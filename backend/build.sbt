name := """hyperdoc-backend"""

libraryDependencies ++= Seq(
  "io.hyperdoc" %% "hyperdoc-core" % "1.0.0-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test")