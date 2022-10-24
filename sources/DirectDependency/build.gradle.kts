plugins {
    kotlin("multiplatform") version "1.7.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    ios()
    iosSimulatorArm64()

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val iosMain by getting
        val iosTest by getting

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}
