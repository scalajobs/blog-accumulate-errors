ThisBuild / scalaVersion     := "3.2.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.scalajobs"
ThisBuild / organizationName := "ScalaJobs"

lazy val root = (project in file("."))
  .settings(
    name := "accumulate-errors",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats"  % "2.9.0",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
    )
  )

