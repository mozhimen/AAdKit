package com.mozhimen.adk.topon.test.utils

import android.text.TextUtils
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.kotlin.utilk.kotlin.UtilKStrAsset
import org.json.JSONObject

/**
 * @ClassName PlacementIdUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/21
 * @Version 1.0
 */
object PlacementIdUtil : IUtilK {
    private const val placementIdJson = "placementId.json"
    private var splashPlacements: Map<String, String>? = null
    private var nativePlacements: Map<String, String>? = null
    private var nativeExpressPlacements: Map<String, String>? = null
    private var bannerPlacements: Map<String, String>? = null
    private var interstitialPlacements: Map<String, String>? = null
    private var rewardedVideoPlacements: Map<String, String>? = null

    private var placementInfoObject: JSONObject? = null

    var MODE = ""

    private fun getPlacementJSONObject(): JSONObject? {
        if (placementInfoObject == null) {
            val jsonString: String = UtilKStrAsset.strAssetName2str_use_ofBufferedReader(placementIdJson) ?: ""
            try {
                placementInfoObject = JSONObject(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return placementInfoObject
    }

    fun getListPlacementId(): String {
        try {
            val jsonObject = getPlacementJSONObject()
            return jsonObject!!.getString("list_placement_id")
        } catch (e: Throwable) {
        }
        return ""
    }

    private fun getPlacementIdMap(placementIdJsonFileName: String, format: String): Map<String, String> {
        val result: MutableMap<String, String> = LinkedHashMap()
        var jsonObject: JSONObject? = null
        try {
            jsonObject = getPlacementJSONObject() ?: return emptyMap()
            val placementJsonObject = jsonObject.optJSONObject(format) ?: return emptyMap()
            val placements = placementJsonObject.keys()
            var placementName: String
            var placementId: String
            while (placements.hasNext()) {
                placementName = placements.next()
                placementId = placementJsonObject[placementName] as String
                result[placementName] = placementId
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            UtilKLogWrapper.e(TAG, "getPlacementIdMap: parse json error -> $format")
        }
        return result
    }

    //////////////////////////////////////////////////////////////////////////////

    @JvmStatic
    fun getSplashPlacements(): Map<String, String> {
        if (splashPlacements == null) {
            splashPlacements = getPlacementIdMap(placementIdJson, "splash")
        }
        return (splashPlacements ?: emptyMap()).also { UtilKLogWrapper.d(TAG, "getSplashPlacements: $it") }
    }

    @JvmStatic
    fun getNativeSelfrenderPlacements(): Map<String, String> {
        if (nativePlacements == null) {
            nativePlacements = getPlacementIdMap(placementIdJson, "native-selfrender")
        }
        return nativePlacements ?: emptyMap()
    }

    @JvmStatic
    fun getNativeExpressPlacements(): Map<String, String> {
        if (nativeExpressPlacements == null) {
            nativeExpressPlacements = getPlacementIdMap(placementIdJson, "native-express")
        }
        return nativeExpressPlacements ?: emptyMap()
    }

    @JvmStatic
    fun getBannerPlacements(): Map<String, String> {
        if (bannerPlacements == null) {
            bannerPlacements = getPlacementIdMap(placementIdJson, "banner")
        }
        return bannerPlacements ?: emptyMap()
    }

    @JvmStatic
    fun getInterstitialPlacements(): Map<String, String> {
        if (interstitialPlacements == null) {
            interstitialPlacements = getPlacementIdMap(placementIdJson, "interstitial")
        }
        return interstitialPlacements ?: emptyMap()
    }

    @JvmStatic
    fun getRewardedVideoPlacements(): Map<String, String> {
        if (rewardedVideoPlacements == null) {
            rewardedVideoPlacements = getPlacementIdMap(placementIdJson, "rewarded_video")
        }
        return rewardedVideoPlacements ?: emptyMap()
    }

    //////////////////////////////////////////////////////////////////////////////

    private var fullScreenPlacements: Map<String, String>? = null
    private var drawPlacements: Map<String, String>? = null
    private var patchPlacements: Map<String, String>? = null

    @JvmStatic
    fun getFullScreenPlacements(): Map<String, String> {
        if (fullScreenPlacements == null) {
            fullScreenPlacements = getPlacementIdMap(placementIdJson, "fullscreen")
        }
        return fullScreenPlacements ?: emptyMap()
    }

    @JvmStatic
    fun getDrawPlacements(): Map<String, String> {
        if (drawPlacements == null) {
            drawPlacements = getPlacementIdMap(placementIdJson, "draw")
        }
        return drawPlacements ?: emptyMap()
    }

    @JvmStatic
    fun getPatchPlacements(): Map<String, String> {
        if (patchPlacements == null) {
            patchPlacements = getPlacementIdMap(placementIdJson, "patch")
        }
        return patchPlacements ?: emptyMap()
    }

    //////////////////////////////////////////////////////////////////////////////

    private var appId: String? = null
    private var appKey: String? = null

    @JvmStatic
    fun getAppId(): String {
        if (TextUtils.isEmpty(appId)) {
            try {
                appId = getPlacementJSONObject()?.getString("app_id")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return appId ?: ""
    }

    @JvmStatic
    fun getAppKey(): String {
        if (TextUtils.isEmpty(appKey)) {
            try {
                appKey = getPlacementJSONObject()?.getString("app_key")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return appKey ?: ""
    }
}