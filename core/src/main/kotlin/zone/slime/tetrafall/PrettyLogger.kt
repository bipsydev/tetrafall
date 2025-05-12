package zone.slime.tetrafall

import ktx.log.DEBUG
import ktx.log.ERROR
import ktx.log.INFO
import ktx.log.Logger


/**
 *******************************************************************************
 * PrettyLogger logging class (subclass of `ktx.log.Logger`
 *******************************************************************************
 * Overrides the `buildMessage` method to do some pretty formatting.
 * Tabs in a message will be prepended before the log level tag.
 * The name/class of the logger will be abbreviated to exclude package path.
 */
class PrettyLogger(
    override var name: String,
    override var debugTag: String = DEBUG,
    override var infoTag: String = INFO,
    override var errorTag: String = ERROR
)
    : Logger(name,
             "${ANSI.BLACK_BRIGHT}${ANSI.ITALIC}$debugTag${ANSI.RESET}",
             "${ANSI.WHITE_BOLD_BRIGHT}$infoTag${ANSI.RESET}",
             "${ANSI.RED_BOLD_BRIGHT}${ANSI.UNDERLINE}$errorTag${ANSI.RESET}")
{

    init {
        debugTag = super.debugTag
        infoTag = super.infoTag
        errorTag = super.errorTag
    }

    /**
     * buildMessage override method
     *------------------------------
     * This needs to return the message string to the logger when a message
     * is to be logged. The returned string will be appended after the
     * appropriate log-level tag.
     * This method also does some manipulation of the tag to place
     * leading tabs ("`\t`") before the log-level tag.
     * This creates an indented, tree-like logging appearance.
     */
    override fun buildMessage(message: String): String {
        // get the index of first non-tab
        val first_non_tab = message.indexOfFirst { it != '\t' }
            // should be 0 if not found
            .let { if (it == -1) 0 else it }

        // substring of only tabs from beginning
        val tabs = message.substring(0, first_non_tab)

        // leading tabs are stripped away
        val message_stripped = message.substring(first_non_tab)

        // will show up like this:
        // "[tabs][tag] [returned_message]"
        return "$tabs${ANSI.ITALIC}<${ANSI.RESET}$name${ANSI.ITALIC}>${ANSI.RESET} $message_stripped"
    }
}

/**
 * Factory function for PrettyLogger<T>
 *--------------------------------------
 * Same as ktx.log.logger, except it removes the package name from
 * `T::class.java.name` and initializes a `PrettyLogger` with only class name.
 */
inline fun <reified T : Any> PrettyLogger(): PrettyLogger {
    val full_name = T::class.java.name
    val short_name = full_name.substring(full_name.indexOfLast { it == '.' } + 1)
    return PrettyLogger("${ANSI.GREEN_UNDERLINED}${ANSI.ITALIC}$short_name${ANSI.RESET}")
}
