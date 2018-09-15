package google.maps

import com.wadejensen.atlas.kotlinjs.require

object Color {
    // Util to convert RGB colors to hex
    val rgb2Hex = require("rgb-hex")
}

/**
 * A clever hack of data uris to create non-standard map marker pins to display on a Google map.
 * Each marker is roughly rectangular with a point and the centre bottom, and contains the price
 * variable input, prefixed by a dollar sign. It looks something like this:
 *  _____________________________________________
 * |                                             |
 * |                                             |
 * |     _    __             _           __`     |
 * |    | |  / /  _ __  _ __(_) ___ ___  \ \`    |
 * |   / __)| |  | '_ \| '__| |/ __/ _ \  | |`   |
 * |   \__ < <   | |_) | |  | | (_|  __/   > >`  |
 * |   (   /| |  | .__/|_|  |_|\___\___|  | |`   |
 * |    |_|  \_\ |_|                     /_/`    |
 * |   `                                         |
 * |                                             |
 * |                                             |
 * |____________________      ___________________|
 *                      \    /
 *                       \  /
 *                        \/
 * @param price
 */
fun generatePriceMarkerIcon(price: Int, fillRGB: RGB, outlineRGB: RGB): String {
    val fillHexColor = rgbToHex(fillRGB)
    val outlineHexColor = rgbToHex(outlineRGB)


    return (
        "data:image/svg+xml;charset=utf-8,%3Csvg%20width%3D%2250px%22%20height%3D%2226px%22%20viewBox%3D%220%200" +
        "%2050%2026%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%3Cpath%20stroke%3D%22%23${outlineHexColor}" +
        "%22%20d%3D%22M30.6568542%2C20%20L50%2C20%20L50%2C0%20L0%2C0%20L0%2C20%20L19.3431458%2C20%20L25%2C25.6568542" +
        "%20L30.6568542%2C20%20Z%22%20fill%3D%22%23${fillHexColor}%22%3E%3C%2Fpath%3E%3Ctext%20text-anchor%3D%22middle%22%20" +
        "font-family%3D%22%26%23x27%3BOpen%20Sans%26%23x27%3B%2C%20sans-serif%22%20font-size%3D%2214%22%20font-weight" +
        "%3D%22500%22%20fill%3D%22white%22%20x%3D%2225%22%20y%3D%2215%22%3E%24${price}%3C%2Ftext%3E%3C%2Fsvg%3E")
}

fun rgbToHex(rgb: RGB): String {
    return google.maps.Color.rgb2Hex(rgb.r, rgb.g, rgb.b)
}
