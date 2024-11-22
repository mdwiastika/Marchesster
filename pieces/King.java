package pieces;

import java.awt.image.BufferedImage;

import main.Board;
import main.Move;

public class King extends Piece {
    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        super.col = col;
        super.row = row;
        super.xPosition = col * board.tileSize;
        super.yPosition = row * board.tileSize;
        super.isWhite = isWhite;
        super.name = "King";
        super.sprite = sheet.getSubimage(0 * sheetScale, (isWhite ? 0 : 1) * sheetScale, sheetScale, sheetScale)
                .getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int col, int row) {
        return Math.abs(col - this.col) <= 1 && row <= 8 && Math.abs(row - this.row) <= 1 || canCastle(col, row);
    }

    public boolean moveCollidesWithPiece(int col, int row) {
        return false;
    }

    private boolean canCastle(int col, int row) {
        if (this.row == row) {
            if (col == 6) {
                Piece rook = board.getPiece(7, row);
                if (rook != null && rook.isFirstMove && this.isFirstMove) {
                    return board.getPiece(5, row) == null &&
                            board.getPiece(6, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 5, row));
                }
            } else if (col == 2) {
                Piece rook = board.getPiece(0, row);
                if (rook != null && rook.isFirstMove && this.isFirstMove) {
                    return board.getPiece(1, row) == null &&
                            board.getPiece(2, row) == null &&
                            board.getPiece(3, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
                }
            }
        }
        return false;
    }

}
