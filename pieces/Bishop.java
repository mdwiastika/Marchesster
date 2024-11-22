package pieces;

import java.awt.image.BufferedImage;

import main.Board;

public class Bishop extends Piece {
    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board);
        super.col = col;
        super.row = row;
        super.xPosition = col * board.tileSize;
        super.yPosition = row * board.tileSize;
        super.isWhite = isWhite;
        super.name = "Bishop";
        super.sprite = sheet.getSubimage(2 * sheetScale, (isWhite ? 0 : 1) * sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        return Math.abs(col - this.col) == Math.abs(row - this.row);
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        if (col < this.col && row < this.row) {
            for (int i = 1; i < Math.abs(col - this.col); i++) {
                if (board.getPiece(this.col - i, this.row - i) != null) {
                    return true;
                }
            }
        }
        if (col < this.col && row > this.row) {
            for (int i = 1; i < Math.abs(col - this.col); i++) {
                if (board.getPiece(this.col - i, this.row + i) != null) {
                    return true;
                }
            }
        }
        if (col > this.col && row < this.row) {
            for (int i = 1; i < Math.abs(col - this.col); i++) {
                if (board.getPiece(this.col + i, this.row - i) != null) {
                    return true;
                }
            }
        }
        if (col > this.col && row > this.row) {
            for (int i = 1; i < Math.abs(col - this.col); i++) {
                if (board.getPiece(this.col + i, this.row + i) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
