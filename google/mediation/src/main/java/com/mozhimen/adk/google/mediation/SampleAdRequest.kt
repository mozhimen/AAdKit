package com.mozhimen.adk.google.mediation

/**
 * @ClassName SampleAdRequest
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * A sample ad request used to load an ad. This is an example of some targeting options an ad
 * network may provide.
 */
open class SampleAdRequest
/**
 * Creates a new [SampleAdRequest].
 */
{
    /**
     * Sets keywords for targeting purposes.
     *
     * @param keywords A set of keywords.
     */
    fun setKeywords(keywords: Set<String>) {
        // Normally we'd save the keywords. But since this is a sample network, we'll do nothing.
    }

    /**
     * Designates a request for test mode.
     *
     * @param useTesting `true` to enable test mode.
     */
    fun setTestMode(useTesting: Boolean) {
        // Normally we'd save this flag. But since this is a sample network, we'll do nothing.
    }

    fun setShouldAddAwesomeSauce(shouldAddAwesomeSauce: Boolean) {
        // Normally we'd save this flag but since this is a sample network, we'll do nothing.
    }

    fun setIncome(income: Int) {
        // Normally we'd save this value but since this is a sample network, we'll do nothing.
    }

    companion object {
        @JvmStatic
        fun getSDKVersion(): String =
            BuildConfig.SDK_VERSION
    }
}
