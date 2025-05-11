package zone.slime.tetrafall

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.async.KtxAsync
import ktx.graphics.LetterboxingViewport
import ktx.graphics.use

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
object TetraFall : KtxGame<KtxScreen>() {

    /** Sprite batch for drawing textures to the screen. */
    val batch by lazy { SpriteBatch() }

    /** A renderer for primitive shapes. */
    val shapes by lazy { ShapeRenderer() }

    /** The global viewport that maintains an aspect ratio and PPI density. */
    val viewport by lazy { LetterboxingViewport(96f, 96f,
                                                16f / 9f) }

    /**
     * create override function
     *--------------------------
     * Called when the game is first created, for initialization purposes.
     * libGDX/KTX is available along with OpenGL, so textures can be allocated,
     * but NO OpenGL Rendering Context is available yet at this point.
     */
    override fun create() {
        KtxAsync.initiate() // needed to initialize ktx.async coroutines.

        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}


/**=============================================================================
 * FirstScreen test screen class
 *==============================================================================
 * a default screen generated with the libKTX template.
 * Just loads and renders a texture.
 */
class FirstScreen : KtxScreen {
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
