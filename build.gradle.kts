// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.Gradle.plugin)
        classpath(Plugins.Kotlin.plugin)
        classpath(Plugins.Hilt.plugin)
        classpath(Plugins.Navigation.safeArgs)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task("clean") {
    delete(rootProject.buildDir)
}