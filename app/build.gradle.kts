import java.util.Properties
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
	id("com.android.application")
	kotlin("android")
	kotlin("android.extensions")
	kotlin("kapt")
	id("io.fabric")
	id("com.github.ben-manes.versions")
}

extra.apply {
	set("min_sdk", 21)
	set("compile_sdk", 28)
	set("target_sdk", 28)
	set("version_code", 24)
	set("version_name", "0.2.1")
	set("annotations", "17.0.0")
	set("compat", "1.1.0-rc01")
	set("material", "1.1.0-alpha09")
	set("preference", "1.0.0")
	set("thirtyinch", "0.9.0")
	set("rxjava", "2.2.7")
	set("rxandroid", "2.1.1")
	set("ktx", "1.1.0-alpha05")
	set("koptional", "1.6.0")
	set("fuel", "2.0.1")
	set("gson", "2.8.5")
	set("room", "1.1.1")
	set("android_state", "1.4.1")
	set("zxing", "3.4.0")
	set("zxing_embedded", "3.6.0")
	set("glide", "4.9.0")
	set("shaped", "0.8.6")
	set("toasty", "1.4.2")
	set("bottom_navigation", "2.0.2")
	set("shortbread", "1.0.2")
	set("timber", "4.7.1")
	set("stetho", "1.5.0")
	set("colorpicker", "1.1.4")
	set("svg", "1.4")
	set("room", "2.1.0")
	set("crashlytics_version", "2.9.9")
	set("firebase", "16.0.8")
	set("kefirbb", "1.5")
}

fun readProperties(propertiesFile: File) = Properties().apply {
	propertiesFile.inputStream().use { fis ->
		load(fis)
	}
}

android {
	compileSdkVersion(extra["compile_sdk"] as Int)
	signingConfigs {
		create("release") {
			if (gradle.startParameter.taskNames.toString().contains("release")) {
				val buildProperties = readProperties(File(project.rootDir, "gradle.properties"))
				storeFile = file(buildProperties["RELEASE_STORE_FILE"] as String)
				keyAlias = buildProperties["RELEASE_KEY_ALIAS"] as String
				keyPassword = buildProperties["RELEASE_KEY_PASSWORD"] as String
				storePassword = buildProperties["RELEASE_STORE_PASSWORD"] as String
			}
		}
		getByName("debug") {
			val buildProperties = readProperties(File(project.rootDir, "gradle_debug.properties"))
			storeFile = file(buildProperties["RELEASE_STORE_FILE"] as String)
			keyAlias = buildProperties["RELEASE_KEY_ALIAS"] as String
			keyPassword = buildProperties["RELEASE_KEY_PASSWORD"] as String
			storePassword = buildProperties["RELEASE_STORE_PASSWORD"] as String
		}
	}
	defaultConfig {
		applicationId = "ru.fantlab.android"
		minSdkVersion(extra["min_sdk"] as Int)
		targetSdkVersion(extra["target_sdk"] as Int)
		versionCode = extra["version_code"] as Int
		versionName = extra["version_name"] as String
		base.archivesBaseName = "fantlab-${versionName}(${versionCode})"
		buildConfigField("String", "REST_URL", "\"https://api.fantlab.ru/\"")
		buildConfigField("String", "API_VERSION", "\"0.9.3\"")
		buildConfigField("String", "API_REVISION", "\"1\"")
		resValue("string", "VERSION_NAME", "$versionName ($versionCode)")
		javaCompileOptions {
			annotationProcessorOptions {
				includeCompileClasspath = false
			}
		}
		kapt {
			arguments {
				arg("room.schemaLocation", "$projectDir/schemas")
			}
		}
	}
	buildTypes {
		getByName("debug") {
			signingConfig = signingConfigs.getByName("debug")
			ext.set("alwaysUpdateBuildId", false)
		}
		getByName("release") {
			isMinifyEnabled = true
			isShrinkResources = true
			signingConfig = signingConfigs.getByName("release")
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
	}
	sourceSets {
		val main by getting
		main.java.srcDirs("src/main/kotlin")
		main.res.srcDirs("src/main/res/",
			"src/main/res/layouts/main_layouts",
			"src/main/res/layouts/row_layouts",
			"src/main/res/layouts/other_layouts")
	}
	lintOptions {
		isWarningsAsErrors = true
		isAbortOnError = true
		htmlReport = true
		disable("InvalidPackage")
	}
	packagingOptions {
		exclude("META-INF/NOTICE")
		exclude("META-INF/NOTICE.txt")
		exclude("META-INF/LICENSE")
		exclude("META-INF/LICENSE.txt")
		exclude("META-INF/rxjava.properties")
	}
	dexOptions {
		jumboMode = true
		javaMaxHeapSize = "4g"
	}
	androidExtensions {
		isExperimental = true
	}
}


tasks.withType<DependencyUpdatesTask> {
	revision = "release"
	outputFormatter = "json"
	outputDir = "build/dependencyUpdates"
}

dependencies {
	implementation("androidx.appcompat:appcompat:${project.extra["compat"]}")
	implementation("com.google.android.material:material:${project.extra["material"]}")
	implementation("androidx.legacy:legacy-preference-v14:${project.extra["preference"]}")
	implementation("net.grandcentrix.thirtyinch:thirtyinch:${project.extra["thirtyinch"]}")
	implementation("net.grandcentrix.thirtyinch:thirtyinch-rx2:${project.extra["thirtyinch"]}")
	implementation("io.reactivex.rxjava2:rxjava:${project.extra["rxjava"]}")
	implementation("io.reactivex.rxjava2:rxandroid:${project.extra["rxandroid"]}")
	implementation("androidx.core:core-ktx:${project.extra["ktx"]}")
	implementation("com.gojuno.koptional:koptional:${project.extra["koptional"]}")
	implementation("com.github.kittinunf.fuel:fuel-android:${project.extra["fuel"]}")
	implementation("com.github.kittinunf.fuel:fuel-rxjava:${project.extra["fuel"]}")
	implementation("com.github.kittinunf.fuel:fuel-gson:${project.extra["fuel"]}")
	implementation("androidx.room:room-runtime:${project.extra["room"]}")
	implementation("androidx.room:room-rxjava2:${project.extra["room"]}")
	kapt("androidx.room:room-compiler:${project.extra["room"]}")
	implementation("com.evernote:android-state:${project.extra["android_state"]}")
	kapt("com.evernote:android-state-processor:${project.extra["android_state"]}")
	implementation("com.github.bumptech.glide:glide:${project.extra["glide"]}")
	kapt("com.github.bumptech.glide:compiler:${project.extra["glide"]}")
	implementation("com.google.zxing:core:${project.extra["zxing"]}")
	implementation("com.journeyapps:zxing-android-embedded:${project.extra["zxing_embedded"]}@aar") { isTransitive = false }
	implementation("com.github.matthiasrobbers:shortbread:${project.extra["shortbread"]}")
	kapt("com.github.matthiasrobbers:shortbread-compiler:${project.extra["shortbread"]}")
	implementation("it.sephiroth.android.library.bottomnavigation:bottom-navigation:${project.extra["bottom_navigation"]}")
	implementation("com.github.GrenderG:Toasty:${project.extra["toasty"]}")
	implementation("cn.gavinliu:ShapedImageView:${project.extra["shaped"]}")
	implementation("com.jakewharton.timber:timber:${project.extra["timber"]}")
	implementation("com.crashlytics.sdk.android:crashlytics:${project.extra["crashlytics_version"]}@aar") { isTransitive = true }
	implementation("com.google.firebase:firebase-core:${project.extra["firebase"]}")
	implementation("com.facebook.stetho:stetho:${project.extra["stetho"]}")
	implementation("com.facebook.stetho:stetho-okhttp3:${project.extra["stetho"]}")
	implementation("petrov.kristiyan:colorpicker-library:${project.extra["colorpicker"]}")
	implementation("com.caverock:androidsvg-aar:${project.extra["svg"]}")
	implementation("org.kefirsf:kefirbb:${project.extra["kefirbb"]}")
}

apply(plugin = "com.google.gms.google-services")