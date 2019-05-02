plugins {
  id("org.jetbrains.kotlin.jvm").version("1.3.21")
  application
}

repositories {
  jcenter()
}

dependencies {
  implementation("io.ktor:ktor-server-core:1.1.4")
  implementation("io.ktor:ktor-server-netty:1.1.4")
  implementation("io.ktor:ktor-jackson:1.1.4")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
  mainClassName = "io.github.fajran.kudupiye.Main"
}
