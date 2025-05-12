package zone.slime.tetrafall

import com.badlogic.gdx.graphics.Texture
import ktx.app.KtxScreen
import ktx.assets.*

import zone.slime.tetrafall.TetraFall.ASSETS

/**
 *******************************************************************************
 * GameScreen class
 *******************************************************************************
 * This `KtxScreen` subclass represents the main game screen view.
 * It controls all game-related variables contained within the view,
 * and uses `TetraFall` public parameters for drawing to the screen
 * and accessing other libGDX/KTX system objects.
 */
class GameScreen : KtxScreen {

    // store texture references here, load them with the global asset manager
    companion object {

        /** Logger for the `GameScreen`. */
        private val LOG by lazy { PrettyLogger<GameScreen>() }

        /** Helper function to build the asset string for fotonicbox#.png */
        private fun txStr(i: Int) = "PhotonicBox/fotonicbox$i.png"

        /**
         * TX_FOTONICBOX - Array of Asset wrappers holding Texture resource refs
         *----------------------------------------------------------------------
         * Store all images in this array of `Asset<Texture>`
         * `Asset` is a wrapper around the Texture resource,
         * useful for `Asset.finishLoading()` and other loading methods.
         */
        val TX_FOTONICBOX = Array(9) {
            val num: Int = it + 1
            LOG.debug { "\tLoading TX_FOTONICBOX${num}..." }
            // each element is initialized with this lambda, and `it` is index
            ASSETS.load<Texture>(txStr(num))
                .also {
                    LOG.debug { "\t\tTX_FOTONICBOX${num} Loaded." }
                }
        }

        // Images Source: https://el-chucho.itch.io/fotonicboxes
    }

    // scene active running time
    var time: Double = 0.0

    /**
     * GameScreen initialization block
     * --------------------------------
     * Loads texture resources and begins the game.
     */
    init {
        // finish loading all elements in this texture asset array
        TX_FOTONICBOX.forEach { it.finishLoading() }
    }

    override fun show() {

    }

    override fun render(delta: Float) {
    }

    override fun dispose() {
        super.dispose()
    }
}
