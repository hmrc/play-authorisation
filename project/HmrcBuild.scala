import sbt.Keys._
import sbt._
import uk.gov.hmrc.{SbtArtifactory, SbtAutoBuildPlugin}
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

object HmrcBuild extends Build {

  import BuildDependencies._

  val appName = "play-authorisation"

  resolvers += Resolver.bintrayRepo("jcenter", "releases")
  resolvers += Resolver.bintrayRepo("hmrc", "releases")

  lazy val playAuthorisation = (project in file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
    .settings(
      name := appName,
      scalaVersion := "2.11.12",
      libraryDependencies ++= Seq(
        Compile.playFramework,
        Compile.httpVerbs,
        Compile.ficus,
        Test.scalaTest,
        Test.pegdown,
        Test.playTest,
        Test.mockito
      ),
      Developers()
    )
    .settings(majorVersion := 5)
    .settings(resolvers += Resolver.bintrayRepo("hmrc", "releases"),
      resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
    )
}

private object BuildDependencies {

  import play.core.PlayVersion

  object Compile {
    val playFramework = "com.typesafe.play" %% "play" % PlayVersion.current % "provided"
    val httpVerbs = "uk.gov.hmrc" %% "http-verbs" % "8.10.0-play-25"
    val ficus = "net.ceedubs" %% "ficus" % "1.1.1"
  }

  sealed abstract class Test(scope: String) {
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.5.0" % scope
    val playTest = "com.typesafe.play" %% "play-test" % PlayVersion.current % scope
    val mockito = "org.mockito" % "mockito-core" % "1.9.0" % scope
  }

  object Test extends Test("test")

}

object Developers {

  def apply() = developers := List[Developer]()
}
