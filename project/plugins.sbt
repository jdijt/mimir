// Compile/assembly/etc helpers.
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.16.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.2.0")
addSbtPlugin("com.github.sbt"     % "sbt-native-packager"      % "1.10.0")
addSbtPlugin("com.github.sbt"     % "sbt-web"                  % "1.5.3")
addSbtPlugin("com.vmunier"        % "sbt-web-scalajs"          % "1.3.0")

// Dev helpers
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// Quality
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
