import com.typesafe.config.ConfigFactory
import com.typesafe.sbt.packager.docker.Cmd

enablePlugins(JavaAppPackaging, DockerPlugin)

Global / onChangedBuildSource := ReloadOnSourceChanges

val conf = ConfigFactory.parseFile(new File("src/main/resources/application.conf")).resolve()

name := "protokollitaja-backend"

version := "0.1.7"

scalaVersion := "2.13.18"

libraryDependencies ++= {
  val pekkoVersion = "1.4.+"
  val pekkoHttpVersion = "1.3.+"

  Seq(
    "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion,
    "org.apache.pekko" %% "pekko-http" % pekkoHttpVersion,
    "org.apache.pekko" %% "pekko-stream" % pekkoVersion,
    //  "org.apache.pekko" %% "pekko-testkit" % "2.6.+" % Test,
    "org.scalatest" %% "scalatest" % "3.2.+" % Test,
    "org.apache.pekko" %% "pekko-actor-testkit-typed" % pekkoVersion % Test,
    "org.apache.pekko" %% "pekko-stream-testkit" % pekkoVersion % Test,
    "org.apache.pekko" %% "pekko-http-testkit" % pekkoHttpVersion % Test,
    "ch.qos.logback" % "logback-classic" % "1.5.23",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.6",
    "org.json4s" %% "json4s-jackson" % "4.0.7",
//    "org.json4s" %% "json4s-native" % "3.6.+",
    "org.json4s" %% "json4s-mongo" % "4.0.7" exclude("org.mongodb", "mongo-java-driver"),
//    "org.apache.kafka" %% "kafka" % "2.4.+",
    "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0",
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
//    "org.mongodb.scala" %% "mongo-scala-bson" % "2.8.0",
//    "com.lightbend.akka" %% "akka-stream-alpakka-mongodb" % "1.1.2"
    "org.mongodb.scala" %% "mongo-scala-driver" % "5.6.2"
  )
}

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

parallelExecution := false

mainClass in Compile := Some("ee.zone.web.protokollitaja.backend.server.ServerMain")
mainClass in (Compile, run) := Some("ee.zone.web.protokollitaja.backend.server.ServerMain")
mainClass in (Compile, packageBin) := Some("ee.zone.web.protokollitaja.backend.server.ServerMain")

dockerBaseImage := "eclipse-temurin:17-jre"
dockerExposedPorts := Seq(conf.getInt("server.httpsPort"), conf.getInt("server.httpPort"))
dockerExposedVolumes := Seq("/data")
dockerUpdateLatest := true
//dockerCommands += Cmd("USER", "protokollitaja")
//dockerCommands += Cmd("RUN", "useradd --no-log-init --uid 1001 protokollitaja")
//dockerCommands += Cmd("RUN", "chown 1001:1001 /opt/docker/bin/*")
//dockerCommands += Cmd("RUN", "chown 1001:root /data")
daemonUser in Docker := "protokollitaja"
//dockerCommands += Cmd("USER", (daemonUser in Docker).value)
//dockerCommands := Seq()
//dockerCommands ++= Seq(
    //  Cmd("FROM", "openjdk:8"),
    //  Cmd("LABEL", s"""MAINTAINER="${maintainer.value}""""),
//    Cmd("CONTAINER_NAME", "protokollitaja-backend")
//)
