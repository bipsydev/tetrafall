package zone.slime.tetrafall

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync

/**
 *******************************************************************************
 * TetraFall Game Singleton (`object`)
 *******************************************************************************
 * The main cross-platform entrypoint for the game,
 * when the libGDX/KTX context is available.
 * Contains the main loop logic as well as global game objects.
 * Delegates most game loop logic to `KtxScreen` subclass instances.
 * Each represents a view of the game and loads their own resources.
 * The `KtxScreen`s are stored in a cache with `TetraFall.addScreen()`
 * and can be set to the currently active running view with `setScreen()`.
 */
object TetraFall : KtxGame<KtxScreen>()
{

    /**
     * Asset Manager for loading and freeing assets.
     * KTX provides a `load` extension function that can be used as a delegate.
     */
    lateinit var ASSETS: AssetStorage

    /** Sprite batch for drawing textures to the screen. */
    val BATCH by lazy {
        SpriteBatch().also {
            LOG.debug { "Initialized global SpriteBatch TetraFall.BATCH." }
        }
    }

    /** A renderer for primitive shapes. */
    val SHAPES by lazy {
        ShapeRenderer().also {
            LOG.debug { "Initialized global ShapeRenderer TetraFall.SHAPES." }
        }
    }

    /**
     * The world camera, for panning & zooming around the game world.
     * Works with the VIEWPORT to send game world projections to the screen.
     * Anything that uses a projection matrix to draw things to the screen
     * will need to copy this camera's `combined` projection matrix
     * whenever it's updated.
     */
    val CAMERA by lazy {
        OrthographicCamera().also {
            LOG.debug { "Initialized global OrthographicCamera TetraFall.CAMERA." }
        }
    }

    /**
     * The global viewport that maintains a minimum virtual world size,
     * and extends the world view in one dimension (both directions) when
     * aspect ratio is not the same as desired minimum.
     * Needs to be `update`d on window resize.
     */
    val VIEWPORT by lazy {
        ExtendViewport(1280f, 720f, CAMERA).also {
            // The camera will initiate
            CAMERA.translate(it.minWorldWidth / 2f, it.minWorldHeight / 2f)
            LOG.debug { "Initialized global ExtendViewport TetraFall.VIEWPORT." }
        }
    }

    val FONT by lazy {
        BitmapFont().apply {
            data.scale(2f)  // double the size of the font
            LOG.debug { "Initialized global BitmapFont TetraFall.FONT." }
        }
    }

    private val LOG by lazy {
        Gdx.app.logLevel = LOG_DEBUG    // set DEBUG logging level (everything)
        PrettyLogger<TetraFall>().also {
            it.debug { "Initialized private TetraFall.LOG. Gdx.app.logLevel = ${Gdx.app.logLevel}" }
            it.info { "info tag example" }
            it.error { "error tag example" }
        }
    }

    /**
     * create override function
     *--------------------------
     * Called when the game is first created, for initialization purposes.
     * libGDX/KTX is available along with OpenGL, so textures can be allocated,
     * but NO OpenGL Rendering Context is available yet at this point.
     */
    override fun create() {
        LOG.debug { "TetraFall.create() entered..." }
        KtxAsync.initiate() // needed to initialize ktx.async coroutines.

        ASSETS = AssetStorage()
        LOG.debug { "Initialized global AssetManager Tetrafall.ASSETS." }

        // instantiate a GameScreen and add it to the screen cache
        addScreen(GameScreen())
        // set it as the active running screen for the game
        setScreen<GameScreen>()
    }

    override fun render() {
        // update camera projection matrices based on camera position/zoom/etc
        CAMERA.update()

        // use camera's combined projection matrix as the projection matrix
        // of our rendering objects
        BATCH.projectionMatrix = CAMERA.combined
        SHAPES.projectionMatrix = CAMERA.combined

        // renders the current screen
        super.render()
    }

    override fun resize(width: Int, height: Int) {
        // update the viewport and camera
        // do not set camera to center of world dimensions
        VIEWPORT.update(width, height, false)

        // update rendering objects' project matrices
        BATCH.projectionMatrix = CAMERA.combined
        SHAPES.projectionMatrix = CAMERA.combined

        super.resize(width, height) // call `resize` for current screen
    }

    override fun dispose() {
        // dispose of all screens stored in our cache
        super.dispose()

        // dispose of ASSETS manager (on current thread, blocking)
        ASSETS.dispose()
        LOG.debug { "AssetStorage disposed of successfully." }
        LOG.debug { "Exiting game..." }
    }
}


/*
/**=============================================================================
 * FirstScreen test screen class
 *==============================================================================
 * a default screen generated with the libKTX template.
 * Just loads and renders a texture.
 */
class FirstScreen : KtxScreen
{
    /** A default texture, a libKTX logo.  */
    private val image = Texture(
        // use KTX's `String?.toInternalFile` function
        "logo.png".toInternalFile(), true)
        // use kotlin's scope function `apply` to change the object in same line
        .apply {
            // `this` context set to the new Texture `image` within this block
            setFilter(Linear, Linear)
            // `this` is implicitly returned to `apply`
        }

    /** A sprite batch for the screen. */
    private val batch = SpriteBatch()

    /** render override function
     *---------------------------
     * Called every frame to update the game and render the screen.
     * OpenGL rendering context is available here for drawing to the screen.
     */
    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        // libKTX's `Batch.use` will call `begin()` and `end()` for us!
        // give it a lambda, and it will call them around it.
        batch.use {
            // Access the batch with `it` variable name
            // (Kotlin's default name for a single implicit parameter in lambda)
            it.draw(image, 100f, 160f)
        }
    }

    /**
     * dispose override function
     *---------------------------
     * Explicitly free up memory we allocated for the screen.
     */
    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}
*/
