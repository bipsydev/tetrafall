package zone.slime.tetrafall

import com.artemis.systems.IteratingSystem
import ktx.artemis.allOf

/**
 *******************************************************************************
 * `GameBoardSystem` class (Artemis Entity-Iterating System)
 *******************************************************************************
 * Processes entities based on ID.
 * Subscribes to an `Aspect` that matches just `GameBoardComponent`.
 */
class GameBoardSystem : IteratingSystem(ASPECT) {
    companion object {
        /** Logger for the `GameBoardSystem`. */
        private val LOG by lazy { PrettyLogger<GameBoardSystem>() }

        private val ASPECT = allOf(GameBoardComponent::class)
    }

    override fun initialize() {
        LOG.debug { "GameBoardSystem.initialize called" }
    }

    override fun begin() {
        LOG.debug { "GameBoardSystem.begin called" }
    }

    override fun process(entityId: Int) {
        LOG.debug { "GameBoardSystem.process($entityId) called" }
    }

    override fun end() {
        LOG.debug { "GameBoardSystem.end called" }
    }

    override fun inserted(entityId: Int) {
        LOG.debug { "GameBoardSystem.inserted($entityId) called" }
    }

    override fun removed(entityId: Int) {
        LOG.debug { "GameBoardSystem.removed($entityId) called" }
    }
}
