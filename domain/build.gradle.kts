plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.javax.inject)
}

kotlin {
    jvmToolchain(11)
}
