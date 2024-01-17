package pt.isel.pdm.chess4android.common

import pt.isel.pdm.chess4android.daily.DayPuzzleDTO

private var solution : List<String> = mutableListOf<String>()
private var solutionIndex : Int = 0
private var movingPiece : String? = null
private var playIndex : Int = 0
private var playVerifier : Int = 0
private var rightPieceSelected : Boolean = false
private var originCordinateX : Int = 0
private var originCordinateY : Int = 0


/**
 * Correspondes a Piece to a Char
 */
private fun PieceToCharMove(piece: Pair<model.Army, model.Piece>?) : String? {
    return when(piece?.second?.name) {
        "KNIGHT" -> "N"
        "BISHOP" -> "B"
        "ROOK" -> "R"
        "QUEEN" -> "Q"
        "KING" -> "K"
        "PAWN" -> ""
        else -> null
    }
}

/**
 * Updates with the information of the piece that the user wants to move (ONLY LOCAL USE)
 */
fun selectPiece(piece: Pair<model.Army, model.Piece>?, row: Int, column: Int) {
    movingPiece = PieceToCharMove(piece)
    originCordinateX = row
    originCordinateY = column
    playVerifier = 0
}

/**
 * Function that swaps the KING info to a ROQUE info (ONLY LOCAL USE)
 */
fun roque(piece: String?) {
    movingPiece = piece
}

/**
 * Returns the current piece
 */
fun getPiece() : String? {
    return movingPiece
}

/**
 * Returns the status of the move (SUCESS, CHECK, ERROR, END GAME)
 */
fun getPlayVerifier() : Int {
    return playVerifier
}

/**
 * Returns the ARMY of the current move (ONLY API USE)
 */
fun getPlayingArmy() : String {
    if(playIndex % 2 == 0) return "white"
    return "black"
}

/**
 * Create a representation of the board with the data from the API
 */
fun createModel(d: DayPuzzleDTO) : BoardState {
    val board : BoardState = BoardState()

    // Reseting variables
    playVerifier = 0
    solutionIndex = 0
    movingPiece = null
    rightPieceSelected = false


    // Get the number of total plays
    playIndex = d.puzzle.initialPly.toString().toInt()-1

    // Splits the solution movements from API with spaces
    solution = d.puzzle.solution.toString().replace("\"", "")
                                            .replace("[", "")
                                            .replace("]", "")
                                            .replace(",", "")
                                            .split(" ")

    // Splits the game movements from API with spaces
    var pgn = d.game.pgn.replace("\"", "")
                        .replace("+", "")
                        .split(" ")

    // Make all the plays from the API
    var currentPlay = 0
    pgn.forEach {
        board.doMove(it, currentPlay);
        currentPlay++
    }

    return board
}

/**
 * Updates the board with a player move (ONLY USE API)
 */
fun updateModel(board: BoardState, row: Int, column: Int, piece: Pair<model.Army, model.Piece>?): BoardState {
    var playerArmy : Int = playIndex

    // Checks if there is any more plays
    if (solutionIndex < solution.size) {

        // The current piece location
        val originY = charToIntMove(solution.get(solutionIndex).get(0))
        val originX = charToIntMove(solution.get(solutionIndex).get(1))

        // The destiny piece location
        val destinationY = solution.get(solutionIndex).get(2)
        val destinationX = solution.get(solutionIndex).get(3)

        // First click
        if( movingPiece == null ) {
            // Check if the player clicked the right piece
            if(originY == column && originX == row) {
                rightPieceSelected = true
            }

            if( getPlayingArmy().equals(piece?.first?.name,true) )
                movingPiece = PieceToCharMove(piece)
        }

        // Second click
        else {
            // Checks if the destination coordinates corresponds to the tile clicked for the correct piece
            if(charToIntMove(destinationY) == column && charToIntMove(destinationX) == row && rightPieceSelected) {

                // Makes the move passing the corresponding piece with its coordinates and the Army type
                board.doMove( (movingPiece?.plus(destinationY.toString()).plus(destinationX.toString())), playerArmy)

                // Go to the next solution play
                solutionIndex++
                playerArmy++

                // After the player move, the next play is simulated by the machine
                if (solutionIndex < solution.size) {
                    playVerifier = 1

                    // Gets the initial position of the machine piece
                    val rivalOriginY = solution.get(solutionIndex).get(0)
                    val rivalOriginX = solution.get(solutionIndex).get(1)

                    // Gets the destiny position of the machine piece
                    val rivalDestY = solution.get(solutionIndex).get(2).toString()
                    val rivalDestX = solution.get(solutionIndex).get(3).toString()

                    // Makes the move passing the corresponding piece fetched on the tile with the initials position, the destiny's and the color
                    board.doMove(
                        (PieceToCharMove(board.getBoard()[charToIntMove(rivalOriginX)][charToIntMove(rivalOriginY)])
                                     ?.plus(rivalOriginY.toString()).plus(rivalDestY).plus(rivalDestX)), playerArmy)

                    solutionIndex++
                    playerArmy++
                }
                else  playVerifier = -1
            }
            // The player clicked on the wrong destination, resetting the play
            else playVerifier = 0
            movingPiece = null
            rightPieceSelected = false
        }
    }

    playIndex = playerArmy
    return board
}

/**
 * Function that sees if the game ended or if the king is in check
 */
fun isGameEndOrKingCheck(boardState: BoardState, playerArmy: Int, row: Int, column: Int) {

    // Defines which king is checked
    var pieceId : Pair<model.Army, model.Piece>
    if(playerArmy == 1) pieceId = Pair(model.Army.WHITE, model.Piece.KING)
    else pieceId = Pair(model.Army.BLACK, model.Piece.KING)

    var kingPositionX = 0
    var kingPositionY = 0

    var status = -1
    var boardNumber = 0


    // Go to every array<Pair<model.Army, model.Piece>> and check if the opponents KING exists
    boardState.getBoard().iterator().forEach { pairElements ->
        // Check if the king is on the current row
        if(pairElements.indexOf(pieceId) >= 0 && pairElements.indexOf(pieceId) <= 7) {
            // Row and column where the king is located
            kingPositionX = boardState.getBoard().indexOf(pairElements)
            kingPositionY = pairElements.indexOf(pieceId)
            status = 1
        }
        boardNumber++
    }

    // Verify if it the opponents KING is in check
    if(status == 1) {
        var initialBoard = boardStateToStringArray(boardState)
        var checkBoard = stringArrayToBoardState(initialBoard) // Creates an fictional board to not affect the real board
        var playerMove = movingPiece.plus(column).plus(row).plus(kingPositionY).plus(kingPositionX)

        checkBoard.doMoveLocal(playerMove, playerArmy)
        var resultBoard = boardStateToStringArray(checkBoard)
        // Checks if the KING was ate
        if(!initialBoard.contentEquals(resultBoard)) {
            status = 3
        }
    }
    // Modify the current game status for the activity
    playVerifier = status
}

/**
 * Updates the board with a player move (ONLY USE LOCAL)
 */
fun updateModelLocalMultiplayer(board: BoardState, row: Int, column: Int, playerArmy : Int) : BoardState {
    // Resets the move status
    playVerifier = 0
    var initialBoard = boardStateToStringArray(board)

    // Gets the piece and makes his own move
    var currentPiece = movingPiece.plus(originCordinateY).plus(originCordinateX).plus(column).plus(row)
    board.doMoveLocal(currentPiece, playerArmy)

    // Gets the board updated after the move
    var resultBoard = boardStateToStringArray(board)

    // Checks if the board had a successful move
    if(!initialBoard.contentEquals(resultBoard)) {
        // Changes to the next player and if this move was a check or end game
        playVerifier = 1
        isGameEndOrKingCheck(board, playerArmy, row ,column)
    }

    // Sets a bad movement and "deselects" a piece selected for the next play
    else playVerifier = 2
    movingPiece = null

    return board
}

