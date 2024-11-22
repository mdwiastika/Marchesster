package pieces;

import java.awt.image.BufferedImage;

import main.Board;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board);
        super.col = col;
        super.row = row;
        super.xPosition = col * board.tileSize;
        super.yPosition = row * board.tileSize;
        super.isWhite = isWhite;
        super.name = "Pawn";
        super.sprite = sheet.getSubimage(5 * sheetScale, (isWhite ? 0 : 1) * sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        int colorIndex = isWhite ? 1 : -1;
        if (this.col == col && row == this.row - colorIndex && board.getPiece(col, row) == null)
            return true;

        if (super.isFirstMove && this.col == col && row == this.row - colorIndex * 2 && board.getPiece(col, row) == null
                && board.getPiece(col, row + colorIndex) == null)
            return true;

        if (col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row) != null)
            return true;

        if (col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row) != null)
            return true;

        if (board.getTileNum(col, row) == board.enPassantCol && col == this.col - 1 && row == this.row - colorIndex
                && board.getPiece(col, row + colorIndex) != null) {
            return true;
        }
        if (board.getTileNum(col, row) == board.enPassantCol && col == this.col + 1 && row == this.row - colorIndex
                && board.getPiece(col, row + colorIndex) != null) {
            return true;
        }

        return false;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        return false;
    }

}
