package zone.slime.tetrafall

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
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

import zone.slime.tetrafall.TetraFall as TF

/**
 *******************************************************************************
 * `GameScreen` class
 *******************************************************************************
 * This `KtxScreen` subclass represents the main game screen view.
 * It controls all game-related variables contained within the view,
 * and uses `TetraFall` public system objects for drawing to the screen
 * and accessing other libGDX/KTX system objects.
 */
class GameScreen : KtxScreen {

    // --- companion object ---
    // store texture references here, load them with the global asset manager
    companion object {

        /** Logger for the `GameScreen`. */
        private val LOG by lazy { PrettyLogger<GameScreen>() }

        /** Helper function to build the asset string for fotonicbox#.png */
        private fun txStr(i: Int) = "PhotonicBox/fotonicbox$i.png"

        /**
         * `TX_FOTONICBOX` - Array of Texture references
         *-----------------------------------------------
         * Store all images in this array of `Asset<Texture>`
         * Assets are loaded with `AssetStorage` instance `TetraFall.ASSETS`
         * and references to the resources are stored in this array.
         * This way, the screen can access these resources globally/statically.
         */
        lateinit var TX_FOTONICBOX: Array<Texture>

        /**
         * This flag will be set to `true` when
         * all `TX_FOTONICBOX` elements are loaded.
         */
        var TX_FOTONICBOX_LOADED: Boolean = false

        // Images Source: https://el-chucho.itch.io/fotonicboxes
    }
    // --- end of companion object ---

    /** scene active running time */
    private var time: Double = 0.0

    /** scene active frame `render` count */
    private var frame: Int = -1 // increments at beginning of `render`, so -1

    /** the current FPS calculated from given `delta`. */
    private var fps: Double = 0.0

    /** The viewport's minimum/desired dimensions. */
    private var dims: Vector2 = Vector2(0f, 0f)
    /** The minimum of either `dims.x` or `dims.y`. */
    private var dimMin: Float = 0f
    /** Position to draw the floating square + frame number text. */
    private var rectPos: Vector2 = Vector2(0f, 0f)

    /* Using artemis-odb with libKTX via `ktx.artemis`
     * basic usage is like this:
     * create a World (having Systems) that will hold Entities
     * Entities are a container of Components
     * Components are pure data classes for specific logical elements
     * Systems process the Components of each Entity during World execution
     */


    /*
     * What Systems do we need?
     * GameBoardSystem - process inputs, update player controlled piece
     * UISystem - updates score & interface display
     *
     */

    /** The World, contains Systems which process Components in Entities. */
    val world: World = World(   // World constructor takes a WorldConfiguration
        WorldConfigurationBuilder()
            //TODO: Exception in thread "main" java.lang.NullPointerException: Aspect was null and no aspect annotations set on system (@All); to use systems which do not subscribe to entities, extend BaseSystem directly.
            .with(GameBoardSystem())
            .build()
    )

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
     * `render` method override (Main rendering loop)
     *------------------------------------------------
     * Called every time the game needs a frame update.
     * This is where the game should react to events, update game logic,
     * and then render to the screen.
     *
     * @param delta - A `Float`, the time in seconds since the last frame.
     *                Passed in by libGDX for us to use for the frame.
     */
    override fun render(delta: Float) {

        // update time, FPS & frame count
        updateFrameTime(delta)

        // update game logic
        update(delta)

        // show "loading..." text while loading textures
        if (!TX_FOTONICBOX_LOADED)
        {
            drawLoading()
        }
        else
        {
            //drawRenderingTest()
            //world.draw()
            LOG.debug { "World.draw called (kinda)" }
        }

        LOG.debug { "--- End of `render` frame #$frame ---\n" }

    }

    /**
     * `updateFrameTime` private method
     *----------------------------------
     * Updates FPS, time, and other frame-time data.
     *
     * @param delta - the time in seconds since last frame, passed by `render`.
     */
    private fun updateFrameTime(delta: Float) {
        // increment frame count (starts at -1, so 0 for first frame)
        frame++

        // no time or frame increase on first frame
        if (frame > 0) {
            time += delta   // update scene time
        }

        // update FPS calculation with new delta
        //TODO add smoothing
        val smoothingFactor: Double = 0.9
        fps = (1.0 / delta.toDouble()).takeIf { it.isFinite() } ?: 0.0
        // calculates 1/delta, sets to 0 if result is either infinity
    }

    /**
     * `update` private method
     *-------------------------
     * Called in `render` after `updateFrameData` before any rendering is done.
     * Updates game logic based on events & input.
     *
     * @param delta - the time in seconds since last frame, passed by `render`.
     */
    private fun update(delta: Float) {

        // log this sometimes
        if (frame < 25
            || (frame % 25 == 0 && frame <= 1000)
            || frame % 1000 == 0) {
            LOG.debug {
                ("frame # %3d: rendering GameScreen with delta = %.4f " +
                    "(%.2f FPS), time = %.2f")
                        .format(frame, delta, fps, time)
            }
        }

        // if we have our textures loaded...
        if (TX_FOTONICBOX_LOADED) {
//
//            // calculate some things into private properties (render data)
//            dims = Vector2(TF.VIEWPORT.minWorldWidth,
//                           TF.VIEWPORT.minWorldHeight)
//            dimMin = min(dims.x, dims.y)
//            rectPos = Vector2(frame/4.5f, frame/8f)
//
//            // pan camera based on delta mouse pos (movement)
//            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//                TF.CAMERA.translate(
//                    -Gdx.input.deltaX.toFloat(),
//                    Gdx.input.deltaY.toFloat()
//                )
//            }

            LOG.debug { "world.process() being called here..." }
            world.setDelta(delta)
            world.process()
        }

    }

    /**
     * `drawLoading` private method
     *--------------------------------
     * This is what is rendered while we wait for resources to load.
     * Just some simple text for now.
     */
    private fun drawLoading() {
        // display text
        TF.BATCH.use {
            TF.FONT.data.setScale(4f)   // make larger
            TF.FONT.color = Color.WHITE
            //TODO position in middle
            TF.FONT.draw(TF.BATCH, "LOADING...", 32.0f, 128.0f)
            TF.FONT.data.setScale(2f)   // make smaller (revert)
        }
    }

    private fun drawRenderingTest() {
        // ======================== DRAW FILLED SHAPES =========================
        TF.SHAPES.use(ShapeRenderer.ShapeType.Filled) {
            // draw a green rectangle on entire screen
            TF.SHAPES.color = Color.GREEN
            TF.SHAPES.rect(0f, 0f, dims.x, dims.y)
            // draw circles in top-right corner
            TF.SHAPES.color = Color.RED
            TF.SHAPES.circle(dims.x, dims.y, dimMin / 4f)
            TF.SHAPES.color = Color.MAGENTA
            TF.SHAPES.circle(dims.x, dims.y, dimMin / 8f)

            // Draw moving white box with red dot, moves with frame count
            TF.SHAPES.color = Color.WHITE
            TF.SHAPES.rect(rectPos.x - 16f, rectPos.y - 16f,
                32f, 32f)
            TF.SHAPES.color = Color.RED
            TF.SHAPES.circle(rectPos, 3f)
        }
        // ========================= DRAW TEXT (World) =========================
        TF.BATCH.use {
            //TODO use a smarter multi-line text rendering structure
            TF.FONT.data.setScale(1.25f)
            TF.FONT.color = Color.BLACK
            TF.FONT.draw(
                TF.BATCH,
                "FRAME: $frame",
                rectPos.x, rectPos.y
            )
        }
        // ======================== DRAW TEXT (Screen) =========================
        TF.setProjectionsToScreen()
        TF.BATCH.use {
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
        }
        TF.setProjectionsToCamera()
    }

    /**
     * `dispose` method override
     */
    override fun dispose() {
        LOG.debug { "Disposing of GameScreen." }
    }
}
