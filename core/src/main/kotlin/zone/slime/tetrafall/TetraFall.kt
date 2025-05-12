package zone.slime.tetrafall

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.graphics.LetterboxingViewport

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
    val ASSETS by lazy {    // ASSETS is lazy-initialized...
        AssetManager().also {   // ... with `AssetManager()`, and also...
            LOG.debug { "Initialized global AssetManager Tetrafall.ASSETS." }
        }
    }

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

    /** The global viewport that maintains an aspect ratio and PPI density. */
    val VIEWPORT by lazy {
        LetterboxingViewport(96f, 96f,
                             16f / 9f).also {
            LOG.debug { "Initialized global LetterboxingViewport TetraFall.VIEWPORT." }
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

        // instantiate a GameScreen and add it to the screen cache
        addScreen(GameScreen())
        // set it as the active running screen for the game
        setScreen<GameScreen>()
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
