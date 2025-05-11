package zone.slime.tetrafall

import com.badlogic.gdx.graphics.Texture
import ktx.app.KtxScreen
import ktx.assets.*

import zone.slime.tetrafall.TetraFall.ASSETS

class GameScreen : KtxScreen {

    // store texture references here, load them with the global asset manager
    companion object {
        /** Helper function to build the asset string for fotonicbox#.png */
        private fun txStr(i: Int) = "PhotonicBox/fotonicbox$i.png"

        // store all images in this array
        val TX_FOTONICBOX = Array(9) {
            // each element is initialized with this lambda, and `it` is index
            ASSETS.load<Texture>(txStr(it + 1))
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
