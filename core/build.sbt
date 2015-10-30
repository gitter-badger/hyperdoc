name := """hyperdoc-core"""

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value) // type erasure (TypeTag)
