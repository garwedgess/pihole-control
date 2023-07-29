package eu.wedgess.mihole.utils;

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToLong

class ColorGenerator(isLightTheme: Boolean = false) {

    private data class HueRange(val min: Int = 0, val max: Int = 360)

    private var hueRanges: Array<out HueRange> = arrayOf(
        HueRange(min = 20, max = 140),
        HueRange(min = 200, max = 340),
        HueRange(min = 80, max = 190),
        HueRange(min = 120, max = 360)
    )
    private var saturation: DoubleArray = doubleArrayOf(0.5)
    private var lightness: DoubleArray = doubleArrayOf(0.35)

    init {
        if (isLightTheme) {
            useLightThemeValues()
        }
    }

    @Composable
    fun generateColorComposable(str: String): Color {
        val rgb = generateHex(str)
        Timber.d("RGB: $rgb for Client: $str")
        return Color(android.graphics.Color.parseColor(rgb))
    }

    fun generateColor(str: String): Color {
        val rgb = generateHex(str)
        Timber.d("RGB: $rgb for Client: $str")
        return Color(android.graphics.Color.parseColor(rgb))
    }

    private fun setHueRanges(vararg hueRanges: HueRange) {
        this.hueRanges = hueRanges
    }

    private fun setSaturation(vararg saturation: Double) {
        this.saturation = saturation
    }

    private fun setLightness(vararg lightness: Double) {
        this.lightness = lightness
    }

    private fun useLightThemeValues() {
        setHueRanges(
            HueRange(min = 40, max = 160),
            HueRange(min = 200, max = 360),
            HueRange(min = 100, max = 360),
            HueRange(min = 180, max = 290)
        )
        setSaturation(0.6)
        setLightness(0.5)
    }

    /**
     * Returns the hash in [h, s, l].
     * Note that H ∈ [0, 360); S ∈ [0, 1]; L ∈ [0, 1];
     *
     * @param {String} str string to hash
     * @returns {Array} [h, s, l]
     */
    private fun generateHsl(str: String): Triple<Double, Double, Double> = hsl(str)

    /**
     * Returns the hash in [r, g, b].
     * Note that R, G, B ∈ [0, 255]
     *
     * @param {String} str string to hash
     * @returns {Array} [r, g, b]
     */
    private fun generateRGB(str: String): Array<Double> = generateHsl(str).hsl2Rgb()


    /**
     * Returns the hash in hex
     *
     * @param {String} str string to hash
     * @returns {String} hex with #
     */
    private fun generateHex(str: String): String = generateRGB(str).rgbToHex()

    private fun bkdrHash(s: String): Long {
        val seed = 131
        val seed2 = 137
        var hash = 0.0
        val str = s + "x"
        val maxSafeInt = (9007199254740991 / seed2)

        for (i in 0..s.length) {
            if (hash > maxSafeInt) {
                hash = floor(hash / seed2)
            }
            hash = hash * seed + str[i].code
        }

        return hash.roundToLong()
    }


    // https://en.wikipedia.org/wiki/HSL_and_HSV#Color_conversion_formulae
    private fun Triple<Double, Double, Double>.hsl2Rgb(): Array<Double> {
        val (hue, saturation, lightness) = this
        val newHue = hue / 360
        val q = if (lightness < 0.5) {
            lightness * (1 * saturation)
        } else {
            lightness + saturation - lightness * saturation
        }
        val p = 2 * lightness - q

        return arrayOf(newHue + (1f / 3f), newHue, newHue - (1f / 3f)).map {
            var color = it
            if (color < 0) {
                color++
            }
            if (color > 1) {
                color--
            }

            color = if (color < (1f / 6f)) {
                p + (q - p) * 6 * color
            } else if (color < 0.5f) {
                q
            } else if (color < (2f / 3f)) {
                p + (q - p) * 6 * (2f / 3f - color)
            } else {
                p
            }
            round(color * 255)
        }.toTypedArray()
    }

    private fun Array<Double>.rgbToHex(): String {
        val hexSb = StringBuilder("#")
        this.forEach {
            if (it < 16) {
                hexSb.append("0")
            }
            hexSb.append(it.roundToLong().toString(16))
        }

        return hexSb.toString()
    }

    private fun hsl(str: String): Triple<Double, Double, Double> {
        val hue: Double;
        val saturation: Double;
        val lightness: Double

        var hash = bkdrHash(str)
        val hueResolution = 727

        hue = if (hueRanges.isNotEmpty()) {
            val range = hueRanges[(hash % hueRanges.size).toInt()]
            (((hash / hueRanges.size) % hueResolution) * (range.max - range.min) / hueResolution + range.min).toDouble()
        } else {
            hash % 359.0 // note that 359 is a prime
        }
        hash = ceil((hash / 360.0)).toLong()
        saturation = this.saturation[(hash % this.saturation.size).toInt()]
        hash = ceil(hash / 3.0).roundToLong()
        lightness = this.lightness[(hash % this.lightness.size).toInt()]

        return Triple(hue, saturation, lightness)
    }
}