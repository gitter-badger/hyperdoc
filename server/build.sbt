name := """hyperdoc-server"""

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "io.argonaut" %% "argonaut" % "6.0.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.12" % "test")
