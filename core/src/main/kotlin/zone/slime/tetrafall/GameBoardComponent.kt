package zone.slime.tetrafall

import com.artemis.Component
import ktx.collections.GdxIntArray

/**
 *******************************************************************************
 * `GameBoardComponent` data class (Artemis Component)
 *******************************************************************************
 * This is a subclass of `com.artemis.Component`
 */
class GameBoardComponent(
    /** Width of the board. */
    val width: Int = 10,
    /** Height of the board. */
    val height: Int = 20,
    /** Resizable array of Entity IDs for each Tetromino on the board. */
    val tetrominoes: GdxIntArray = GdxIntArray(0),
    /** The Entity ID of the current player-controlled Tetromino. -1 if none. */
    var player: Int = -1
) : Component()
