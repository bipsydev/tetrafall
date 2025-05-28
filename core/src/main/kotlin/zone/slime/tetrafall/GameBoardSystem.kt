package zone.slime.tetrafall

import com.artemis.systems.IteratingSystem

/**
 *******************************************************************************
 * `GameBoardSystem` class (Artemis Entity-Iterating System)
 *******************************************************************************
 * Processes entities based on ID.
 */
class GameBoardSystem : IteratingSystem() {

    companion object {
        /** Logger for the `GameBoardSystem`. */
        private val LOG by lazy { PrettyLogger<GameScreen>() }
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
