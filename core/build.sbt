name := """hyperdoc-core"""

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value, // type erasure (TypeTag)
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "io.argonaut" %% "argonaut" % "6.0.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.12" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test")
