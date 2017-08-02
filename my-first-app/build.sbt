name := """my-first-app"""

version := "1.0-SNAPSHOT"

lazy val common = RootProject(file("../system-common"))

lazy val root = (project in file(".")).enablePlugins(PlayJava)
.aggregate(common)
.dependsOn(common)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  javaWs,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "mysql" % "mysql-connector-java" % "5.1.18"
)

PlayKeys.externalizeResources := false


