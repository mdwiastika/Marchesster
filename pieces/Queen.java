package pieces;

import java.awt.image.BufferedImage;

import main.Board;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board);
        super.col = col;
        super.row = row;
        super.xPosition = col * board.tileSize;
        super.yPosition = row * board.tileSize;
        super.isWhite = isWhite;
        super.name = "Queen";
        super.sprite = sheet.getSubimage(1 * sheetScale, (isWhite ? 0 : 1) * sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row || Math.abs(col - this.col) == Math.abs(row - this.row);
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        if (this.col == col || this.row == row) {
            if (this.col > col) {
                for (int colIndex = this.col - 1; colIndex > col; colIndex--) {
                    if (board.getPiece(colIndex, this.row) != null) {
                        return true;
                    }
                }
            }
            if (this.col < col) {
                for (int colIndex = this.col + 1; colIndex < col; colIndex++) {
                    if (board.getPiece(colIndex, this.row) != null) {
                        return true;
                    }
                }
            }
            if (this.row > row) {
                for (int rowIndex = this.row - 1; rowIndex > row; rowIndex--) {
                    if (board.getPiece(this.col, rowIndex) != null) {
                        return true;
                    }
                }
            }
            if (this.row < row) {
                for (int rowIndex = this.row + 1; rowIndex < row; rowIndex++) {
                    if (board.getPiece(this.col, rowIndex) != null) {
                        return true;
                    }
                }
            }
        } else {
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
        }
        return false;
    }
}
