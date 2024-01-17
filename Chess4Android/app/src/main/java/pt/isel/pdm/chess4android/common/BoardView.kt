package pt.isel.pdm.chess4android.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.GridLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.common.model.Piece
import pt.isel.pdm.chess4android.common.model.Army
import pt.isel.pdm.chess4android.common.Tile.Type

typealias TileTouchListener = (tile: Tile, row: Int, column: Int) -> Unit

/**
 * Custom view that implements a chess board.
 */
@SuppressLint("ClickableViewAccessibility")
class BoardView(private val ctx: Context, attrs: AttributeSet?) : GridLayout(ctx, attrs) {

    private val side = 8

    var model: BoardState = BoardState()
        set(value) {
            field = value
            // Remove board view to create a new one
            removeAllViews()
            rowCount = side
            columnCount = side

            repeat(side * side) {
                var row = it / side
                var column = it % side

                val tile = Tile(ctx, if((row + column) % 2 == 0) Type.WHITE else Type.BLACK, side,
                    mapOf( Pair(model.getBoard()[row][column], piecesImages.get(model.getBoard()[row][column]) )) )

                tile.piece = model.getBoard()[row][column]
                tile.setOnClickListener { tileClickedListener?.invoke(tile, row, column) }
                addView(tile)
            }
        }

    var tileClickedListener: TileTouchListener? = null

    private val brush = Paint().apply {
        ctx.resources.getColor(R.color.chess_board_black, null)
        style = Paint.Style.STROKE
        strokeWidth = 10F
    }

    private fun createImageEntry(army: Army, piece: Piece, imageId: Int) =
        Pair(Pair(army, piece), VectorDrawableCompat.create(ctx.resources, imageId, null))

    private val piecesImages = mapOf(
        createImageEntry(Army.WHITE, Piece.PAWN, R.drawable.ic_white_pawn),
        createImageEntry(Army.WHITE, Piece.KNIGHT, R.drawable.ic_white_knight),
        createImageEntry(Army.WHITE, Piece.BISHOP, R.drawable.ic_white_bishop),
        createImageEntry(Army.WHITE, Piece.ROOK, R.drawable.ic_white_rook),
        createImageEntry(Army.WHITE, Piece.QUEEN, R.drawable.ic_white_queen),
        createImageEntry(Army.WHITE, Piece.KING, R.drawable.ic_white_king),
        createImageEntry(Army.BLACK, Piece.PAWN, R.drawable.ic_black_pawn),
        createImageEntry(Army.BLACK, Piece.KNIGHT, R.drawable.ic_black_knight),
        createImageEntry(Army.BLACK, Piece.BISHOP, R.drawable.ic_black_bishop),
        createImageEntry(Army.BLACK, Piece.ROOK, R.drawable.ic_black_rook),
        createImageEntry(Army.BLACK, Piece.QUEEN, R.drawable.ic_black_queen),
        createImageEntry(Army.BLACK, Piece.KING, R.drawable.ic_black_king),
    )

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
            canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
            canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
            canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
        }
        else {
            canvas.drawLine(0f, 0f, height.toFloat(), 0f, brush)
            canvas.drawLine(0f, height.toFloat(), height.toFloat(), height.toFloat(), brush)
            canvas.drawLine(0f, 0f, 0f, width.toFloat(), brush)
            canvas.drawLine(height.toFloat(), 0f, height.toFloat(), width.toFloat(), brush)
        }

    }

}