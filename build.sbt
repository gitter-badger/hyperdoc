name := """hyperdoc"""

organization in ThisBuild := "io.hyperdoc"

scalaVersion in ThisBuild := "2.11.7"

version in ThisBuild := "1.0.0-SNAPSHOT"

libraryDependencies in ThisBuild ++= Seq(
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test")