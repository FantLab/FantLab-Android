buildscript {
	repositories {
		google()
		jcenter()
		maven { url = uri("https://maven.fabric.io/public") }
		maven { url = uri("https://jitpack.io") }
	}
	dependencies {
		classpath("com.android.tools.build:gradle:3.5.0")
		classpath("io.fabric.tools:gradle:1.26.1")
		classpath("com.google.gms:google-services:4.3.1")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
		classpath("com.github.ben-manes:gradle-versions-plugin:0.20.0")
		classpath("com.novoda:gradle-build-properties-plugin:0.4.1")
	}
}

allprojects {
	repositories {
		google()
		jcenter()
		maven { url = uri("https://maven.fabric.io/public") }
		maven { url = uri("https://jitpack.io") }
	}
}

tasks.register("clean",Delete::class){
	delete(rootProject.buildDir)
}