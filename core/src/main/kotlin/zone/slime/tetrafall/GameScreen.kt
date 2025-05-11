package zone.slime.tetrafall

import com.badlogic.gdx.graphics.Texture
import ktx.app.KtxScreen
import ktx.assets.toInternalFile

class GameScreen : KtxScreen {

    companion object {
        private fun txStr(i: Int) = "PhotonicBox/fotonicbox$i.png".toInternalFile()

        // Images Source: https://el-chucho.itch.io/fotonicboxes
        val TX_FOTONICBOX1 by lazy { Texture(txStr(1)) }
        val TX_FOTONICBOX2 by lazy { Texture(txStr(2)) }
        val TX_FOTONICBOX3 by lazy { Texture(txStr(3)) }
        val TX_FOTONICBOX4 by lazy { Texture(txStr(4)) }
        val TX_FOTONICBOX5 by lazy { Texture(txStr(5)) }
        val TX_FOTONICBOX6 by lazy { Texture(txStr(6)) }
        val TX_FOTONICBOX7 by lazy { Texture(txStr(7)) }
        val TX_FOTONICBOX8 by lazy { Texture(txStr(8)) }
        val TX_FOTONICBOX9 by lazy { Texture(txStr(9)) }
    }

    var time: Double = 0.0

    /**
     * GameScreen initialization block
     * --------------------------------
     * Loads texture resources and begins the game.
     */
    init {
        // allocation of textures should happen here

    }

    override fun show() {
    }

    override fun render(delta: Float) {
    }

    override fun dispose() {
        super.dispose()
    }
}
