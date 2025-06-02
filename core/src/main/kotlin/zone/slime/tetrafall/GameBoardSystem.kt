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
class GameBoardSystem : IteratingSystem(
    /* --- About: ASPECTS in ktx.artemis ---
     * -------------------------------------
     * The `IteratingSystem` constructor takes our `Aspect` to subscribe to.
     * An aspect is simply a set of rules for which components to match.
     * Any `Entity` in the `World` matching the `Aspect` rule will process here.
     *
     * `allOf`   (&&) -> the Entity must contain all listed components.
     * `oneOf`   (||) -> the Entity must contain at least one of the components.
     * `exclude` (!)  -> exclude entities containing any of the components.
     */
    allOf(GameBoardComponent::class)
) {
    companion object {
        /** Logger for the `GameBoardSystem`. */
        private val LOG by lazy { PrettyLogger<GameBoardSystem>() }
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
