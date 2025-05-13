package zone.slime.tetrafall

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.graphics.circle
import ktx.graphics.use

import kotlinx.coroutines.launch
import kotlin.math.min

import zone.slime.tetrafall.TetraFall as TF

/**
 *******************************************************************************
 * GameScreen class
 *******************************************************************************
 * This `KtxScreen` subclass represents the main game screen view.
 * It controls all game-related variables contained within the view,
 * and uses `TetraFall` public system objects for drawing to the screen
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
         * `TX_FOTONICBOX` - Array of Asset wrappers holding Texture resource refs
         *-------------------------------------------------------------------------
         * Store all images in this array of `Asset<Texture>`
         * `Asset` is a wrapper around the Texture resource,
         * useful for `Asset.finishLoading()` and other loading methods.
         */
        lateinit var TX_FOTONICBOX: Array<Texture>

        /** This flag will be set to `true` when all `TX_FOTONICBOX` elements are loaded.*/
        var TX_FOTONICBOX_LOADED: Boolean = false

        // Images Source: https://el-chucho.itch.io/fotonicboxes
    }
    // end of companion object

    // scene active running time
    private var time: Double = 0.0

    // scene active frame `render` count
    private var frame: Int = 0

    // the current FPS calculated from given `delta`.
    private var fps: Double = 0.0

    /**
     * GameScreen initialization block
     * --------------------------------
     * Loads texture resources on a coroutine and begins the game.
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
                // `load` returns a delegate, and
                // requests the asset to be loaded immediately.
                TF.ASSETS.load<Texture>(txStr(num)).also {
                    LOG.debug { "\t\tTX_FOTONICBOX${num} Loaded." }
                }
            }
            LOG.debug { "TX_FOTONICBOX loading coroutine over." }
            TX_FOTONICBOX_LOADED = true
        }
        LOG.debug { "GameScreen successfully initialized." }
    }

    /**
     * `show` method override
     *--------------------------
     * Called when the scene is set as the active screen for the game
     * with `TetraFall.setScreen`.
     */
    override fun show() {
        LOG.debug { "GameScreen `show()` called" }
    }

    /**
     * `render` method override
     *--------------------------
     * Called every time the game needs a frame update.
     * This is where the game should react to events, update game logic,
     * and then render to the screen.
     */
    override fun render(delta: Float) {
        fps = 1.0 / delta.toDouble()    // update FPS calculation with new delta
        if (fps == Double.NEGATIVE_INFINITY || fps == Double.POSITIVE_INFINITY) {
            fps = 0.0
        }

        // log this sometimes
        if (frame < 25
            || (frame % 25 == 0 && frame <= 1000)
            || frame % 1000 == 0) {
            LOG.debug {
                "frame # %3d: rendering GameScreen with delta = %.4f (%.2f FPS), time = %.2f"
                    .format(frame, delta, fps, time)
            }
        }

        // show "loading..." text while loading textures
        if (!TX_FOTONICBOX_LOADED)
        {
            // display text
            TF.BATCH.use {
                TF.FONT.data.setScale(4f)
                TF.FONT.color = Color.WHITE
                TF.FONT.draw(TF.BATCH, "LOADING...", 32.0f, 128.0f)
                TF.FONT.data.setScale(2f)
            }
        }
        else
        {
            val dims = Vector2(TF.VIEWPORT.minWorldWidth, TF.VIEWPORT.minWorldHeight)
            val dimMin = min(dims.x, dims.y)
            val rectPos = Vector2(frame/4.5f, frame/8f)

            // draw filled
            TF.SHAPES.use(ShapeRenderer.ShapeType.Filled) {
                // draw a green rectangle on entire screen
                TF.SHAPES.color = Color.GREEN
                //SHAPES.rect(0f, 0f, vdims.x, vdims.y)
                TF.SHAPES.rect(0f, 0f, dims.x, dims.y)
                // draw circles in top-right corner
                TF.SHAPES.color = Color.RED
                /*
                LOG.debug { "SHAPES.circle(${vdims.x}, ${vdims.y}, ${ppiMinScale / 4f})" }
                TF.SHAPES.circle(vdims.x, vdims.y, vdimsMin / 4f)
                */
                TF.SHAPES.circle(dims.x, dims.y, dimMin / 4f)
                TF.SHAPES.color = Color.MAGENTA
                //SHAPES.circle(vdims.x, vdims.y, vdimsMin / 8f)
                TF.SHAPES.circle(dims.x, dims.y, dimMin / 8f)

                // Draw moving white box with red dot, moves with frame count
                TF.SHAPES.color = Color.WHITE
                TF.SHAPES.rect(rectPos.x - 16f, rectPos.y - 16f,
                    32f, 32f)
                TF.SHAPES.color = Color.RED
                TF.SHAPES.circle(rectPos, 3f)
            }
            TF.BATCH.use {
                TF.FONT.data.setScale(1.25f)
                TF.FONT.color = Color.BLACK
                TF.FONT.draw(TF.BATCH,
                    "FRAME: $frame",
                    rectPos.x, rectPos.y)

                TF.FONT.data.setScale(2f)
                TF.FONT.color = Color.RED
                TF.FONT.draw(TF.BATCH,
                    "Viewport Screen: ${TF.VIEWPORT.screenWidth} x ${TF.VIEWPORT.screenHeight}",
                    32f, 132f)

                TF.FONT.color = Color.BLUE
                TF.FONT.draw(TF.BATCH,
                    "Viewport World: ${TF.VIEWPORT.worldWidth} x ${TF.VIEWPORT.worldHeight}",
                    32f, 164f)

                TF.FONT.draw(TF.BATCH,
                    "dims (min): ${dims.x}, ${dims.y}",
                    32f, 196f)

                TF.FONT.color = Color.WHITE
                TF.FONT.draw(TF.BATCH,
                    "Graphics Density: ${Gdx.graphics.density}",
                    32f, 228f)

                TF.FONT.draw(TF.BATCH,
                    "PPI X & Y: ${Gdx.graphics.ppiX}, ${Gdx.graphics.ppiY}",
                    32f, 260f)

                /*
                TF.FONT.draw(TF.BATCH,
                    "PPI X & Y / target PPI: ${ppiXScale}, ${ppiYScale}",
                    32f, 260f)
                TF.FONT.draw(TF.BATCH,
                    "vdims: ${TF.VIEWPORT.worldWidth * ppiMinScale}, ${TF.VIEWPORT.worldHeight * ppiMinScale}",
                    32f, 292f)
                 */
            }
        }

        time += delta   // update scene time
        frame++         // increase frame count
    }

    /**
     * `dispose` method override
     */
    override fun dispose() {
        LOG.debug { "Disposing of GameScreen." }
    }
}
