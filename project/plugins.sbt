logLevel := sbt.Level.Info

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")
//addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.2")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0-M1")