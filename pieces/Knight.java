package pieces;

import java.awt.image.BufferedImage;

import main.Board;

public class Knight extends Piece {
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        super.col = col;
        super.row = row;
        super.xPosition = col * board.tileSize;
        super.yPosition = row * board.tileSize;
        super.isWhite = isWhite;
        super.name = "Knight";
        super.sprite = sheet.getSubimage(3 * sheetScale, (isWhite ? 0 : 1) * sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        //

        return false;
    }
}
