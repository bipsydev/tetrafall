package zone.slime.tetrafall

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx

/**
 *******************************************************************************
 * `ANSI` singleton - libGDX logging-friendly ANSI coloring codes.
 *******************************************************************************
 * Designed to be used with `PrettyLogger`. Defines ANSI color & formatting
 * escape strings ONLY on desktop (`lwjgl3:*`). Otherwise defines empty strings,
 * so that no formatting is applied on other platforms (Android, web).
 *
 * Used as reference:
 * [W3Schools' ANSI Color Codes - Java](https://www.w3schools.blog/ansi-colors-java)
 */
object ANSI {
    /** Helper to determine if code should be defined or not. */
    private val isDesktop get() = Gdx.app.type == ApplicationType.Desktop

    /** Helper to generate a code for a property. */
    private fun code(s: String) = if (isDesktop) "\u001b[$s" else ""

    val RESET = code("0m")
    val BOLD = code("1m")
    val ITALIC = code("3m")
    val UNDERLINE = code("4m")

    val BLACK = code("0;30m")
    val RED = code("0;31m")
    val GREEN = code("0;32m")
    val YELLOW = code("0;33m")
    val BLUE = code("0;34m")
    val PURPLE = code("0;35m")
    val CYAN = code("0;36m")
    val WHITE = code("0;37m")

    val BLACK_BOLD = code("1;30m")
    val RED_BOLD = code("1;31m")
    val GREEN_BOLD = code("1;32m")
    val YELLOW_BOLD = code("1;33m")
    val BLUE_BOLD = code("1;34m")
    val PURPLE_BOLD = code("1;35m")
    val CYAN_BOLD = code("1;36m")
    val WHITE_BOLD = code("1;37m")

    val BLACK_UNDERLINED = code("4;30m")
    val RED_UNDERLINED = code("4;31m")
    val GREEN_UNDERLINED = code("4;32m")
    val YELLOW_UNDERLINED = code("4;33m")
    val BLUE_UNDERLINED = code("4;34m")
    val PURPLE_UNDERLINED = code("4;35m")
    val CYAN_UNDERLINED = code("4;36m")
    val WHITE_UNDERLINED = code("4;37m")

    val BLACK_BACKGROUND = code("40m")
    val RED_BACKGROUND = code("41m")
    val GREEN_BACKGROUND = code("42m")
    val YELLOW_BACKGROUND = code("43m")
    val BLUE_BACKGROUND = code("44m")
    val PURPLE_BACKGROUND = code("45m")
    val CYAN_BACKGROUND = code("46m")
    val WHITE_BACKGROUND = code("47m")

    val BLACK_BRIGHT = code("0;90m")
    val RED_BRIGHT = code("0;91m")
    val GREEN_BRIGHT = code("0;92m")
    val YELLOW_BRIGHT = code("0;93m")
    val BLUE_BRIGHT = code("0;94m")
    val PURPLE_BRIGHT = code("0;95m")
    val CYAN_BRIGHT = code("0;96m")
    val WHITE_BRIGHT = code("0;97m")

    val BLACK_BOLD_BRIGHT = code("1;90m")
    val RED_BOLD_BRIGHT = code("1;91m")
    val GREEN_BOLD_BRIGHT = code("1;92m")
    val YELLOW_BOLD_BRIGHT = code("1;93m")
    val BLUE_BOLD_BRIGHT = code("1;94m")
    val PURPLE_BOLD_BRIGHT = code("1;95m")
    val CYAN_BOLD_BRIGHT = code("1;96m")
    val WHITE_BOLD_BRIGHT = code("1;97m")

    val BLACK_BACKGROUND_BRIGHT = code("0;100m")
    val RED_BACKGROUND_BRIGHT = code("0;101m")
    val GREEN_BACKGROUND_BRIGHT = code("0;102m")
    val YELLOW_BACKGROUND_BRIGHT = code("0;103m")
    val BLUE_BACKGROUND_BRIGHT = code("0;104m")
    val PURPLE_BACKGROUND_BRIGHT = code("0;105m")
    val CYAN_BACKGROUND_BRIGHT = code("0;106m")
    val WHITE_BACKGROUND_BRIGHT = code("0;107m")
}
