package zone.slime.tetrafall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.graphics.use

import zone.slime.tetrafall.TetraFall.ASSETS
import zone.slime.tetrafall.TetraFall.BATCH
import zone.slime.tetrafall.TetraFall.FONT
import zone.slime.tetrafall.TetraFall.SHAPES

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
        lateinit var TX_FOTONICBOX: Array<Texture>
        var TX_FOTONICBOX_LOADED: Boolean = false

        // Images Source: https://el-chucho.itch.io/fotonicboxes
    }

    // scene active running time
    var time: Double = 0.0

    // scene active frame `render` count
    var frame: Int = 0

    // the current FPS calculated from given `delta`.
    var fps: Double = 0.0

    /**
     * GameScreen initialization block
     * --------------------------------
     * Loads texture resources and begins the game.
     */
    init {
        LOG.debug { "GameScreen initializing..." }

        // begin coroutine to load assets
        KtxAsync.launch {
            // define the texture array
            TX_FOTONICBOX = Array(9) {
                // `it` is index
                val num: Int = it + 1
                LOG.debug { "\tLoading TX_FOTONICBOX${num}..." }
                // each element is initialized with this lambda result
                ASSETS.load<Texture>(txStr(num)).also {
                    LOG.debug { "\t\tTX_FOTONICBOX${num} Loaded." }
                }
            }
            LOG.debug { "TX_FOTONICBOX loading coroutine over." }
            TX_FOTONICBOX_LOADED = true
        }
        LOG.debug { "GameScreen successfully initialized." }
    }

    override fun show() {
        LOG.debug { "GameScreen `show()` called" }
    }

    override fun render(delta: Float) {
        fps = 1.0 / delta.toDouble()    // update FPS calculation with new delta

        // log this sometimes
        if (frame < 25
            || (frame % 25 == 0 && frame <= 1000)
            || frame % 1000 == 0) {
            LOG.debug {
                "frame # %d: rendering GameScreen with delta = %.4f (%.2f FPS), time = %.2f"
                    .format(frame, delta, fps, time)
            }
        }

        // show "loading..." text while loading textures
        if (!TX_FOTONICBOX_LOADED)
        {
            // display text
            BATCH.use {
                FONT.color = Color.WHITE
                FONT.draw(BATCH, "LOADING...", 32.0f, 32.0f)
            }
        }
        else
        {
            SHAPES.use(ShapeRenderer.ShapeType.Filled) {
                SHAPES.color = Color.GREEN
                SHAPES.rect(32.0f, 32.0f, 256.0f, 256.0f)
            }
        }

        time += delta   // update scene time
        frame++         // increase frame count
    }

    override fun dispose() {
        LOG.debug { "Disposing of GameScreen." }
    }
}
