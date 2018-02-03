name := """my-first-app"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file("."))
.settings(
        EclipseKeys.skipParents in ThisBuild := false,      // Needed ofr subprojects to behave with eclipse
        EclipseKeys.preTasks := Seq(),                      // Do not trigger a compilation before running eclipse
        Keys.javaOptions in (Test) +=                       // Ensures that the JVM launched for testing allows debugging.
        "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9999",
        doc in Compile <<= target.map(_ / "none"),
        javacOptions ++= Seq("-g")
    )
.enablePlugins(PlayJava)
.aggregate(common)
.dependsOn(common % "test->test;compile->compile")

scalaVersion := "2.11.7"

libraryDependencies ++= Seq()


lazy val common = RootProject(file("../common"))