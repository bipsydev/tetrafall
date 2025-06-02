package zone.slime.tetrafall

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class GameBoardRenderSystem(
    private val batch: SpriteBatch = TetraFall.BATCH,
    private val shapes: ShapeRenderer = TetraFall.SHAPES
) : BaseSystem() {

    // declare component mappers to get components from entities
    // this is initialized from the `World` during construction
    // or from `World.inject`
    private lateinit var map: ComponentMapper<GameBoardComponent>

    companion object {
        /** Logger for the `GameBoardRenderSystem`. */
        private val LOG by lazy { PrettyLogger<GameBoardRenderSystem>() }
    }

    override fun initialize() {
        LOG.debug { "initialize game board rendering system" }
    }


    override fun processSystem() {
        LOG.debug { "Begin game board rendering" }
        LOG.debug { "End game board rendering" }
    }

}
