object Configs {
    const val applicationId = "com.tomasfp.pokedex"
    const val buildToolVersion = "30.0.3"
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val versionCode = versionCode()
    val versionName = versionName()
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0

    private fun versionCode(): Int = versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100

    private fun versionName(): String = "${versionMajor}.${versionMinor}.${versionPatch}"
}