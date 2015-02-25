name := "crbclientdetails"

version := "1.0"

lazy val `crbclientdetails` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc ,
  javaEbean ,
  cache ,
  javaWs,
  "javax.activation" % "activation" % "1.1.1",
  "javax.mail" % "mail" % "1.4.7",
  "commons-codec" % "commons-codec" % "1.7",
  "mysql" % "mysql-connector-java" % "5.1.21",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "joda-time" % "joda-time" % "2.3",
  "commons-io" % "commons-io" % "2.3",
  "org.apache.commons" % "commons-io" % "1.3.2"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  