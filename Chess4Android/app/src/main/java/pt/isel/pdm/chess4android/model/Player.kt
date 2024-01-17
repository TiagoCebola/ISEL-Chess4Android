package pt.isel.pdm.chess4android.model

/**
 * Enumeration type used to represent the game's players.
 */
enum class Player {

    WHITE, BLACK;

    companion object {
        val firstToMove: Player = WHITE
    }

    /**
     * The other player
     */
    val other: Player
        get() = if (this == WHITE) BLACK else WHITE
}

fun stringToPlayer(turn : String?) : Player? {
    if(turn == "WHITE") return Player.WHITE
    else if(turn == "BLACK")  return Player.BLACK
    return null
}
