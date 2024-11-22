package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Board extends JPanel {
    public int tileSize = 85;
    public int cols = 8;
    public int rows = 8;
    public Piece selectedPiece;
    public ArrayList<Piece> pieces = new ArrayList<Piece>();
    private Input input = new Input(this);
    public int enPassantCol = -1;
    public CheckScanner checkScanner = new CheckScanner(this);
    public boolean isWhiteTurn = true;
    public boolean isGameOver = false;
    private String gameOverMessage = "";

    public Board() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        addPiece();
    }

    public void addPiece() {
        for (int i = 0; i < cols; i++) {
            pieces.add(new Pawn(this, i, 1, false));
            pieces.add(new Pawn(this, i, 6, true));
        }
        pieces.add(new Knight(this, 1, 0, false));
        pieces.add(new Knight(this, 6, 0, false));
        pieces.add(new Knight(this, 1, 7, true));
        pieces.add(new Knight(this, 6, 7, true));

        pieces.add(new Rook(this, 0, 0, false));
        pieces.add(new Rook(this, 7, 0, false));
        pieces.add(new Rook(this, 0, 7, true));
        pieces.add(new Rook(this, 7, 7, true));

        pieces.add(new Bishop(this, 2, 0, false));
        pieces.add(new Bishop(this, 5, 0, false));
        pieces.add(new Bishop(this, 2, 7, true));
        pieces.add(new Bishop(this, 5, 7, true));

        pieces.add(new King(this, 4, 0, false));
        pieces.add(new King(this, 4, 7, true));

        pieces.add(new Queen(this, 3, 0, false));
        pieces.add(new Queen(this, 3, 7, true));

    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieces) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidMove(Move move) {
        if (isGameOver) {
            return false;
        }
        if (move.piece.isWhite != isWhiteTurn) {
            return false;
        }

        if (sameTeam(move.piece, move.capturedPiece)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if (checkScanner.isKingChecked(move)) {
            return false;
        }

        return true;
    }

    public void makeMove(Move move) {
        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.name.equals("King")) {
            moveKing(move);
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPosition = move.newCol * tileSize;
        move.piece.yPosition = move.newRow * tileSize;

        move.piece.isFirstMove = false;
        capture(move.capturedPiece);

        isWhiteTurn = !isWhiteTurn;
        updateGameState();
    }

    private void moveKing(Move move) {
        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.newCol == 6) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPosition = rook.col * tileSize;
        }
    }

    private void movePawn(Move move) {
        int colorIndex = move.piece.isWhite ? 1 : -1;
        if (getTileNum(move.newCol, move.newRow) == enPassantCol) {
            move.capturedPiece = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.newRow - move.piece.row) == 2) {
            enPassantCol = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantCol = -1;
        }
        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex) {
            promotePawn(move);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPosition = move.newCol * tileSize;
        move.piece.yPosition = move.newRow * tileSize;

        move.piece.isFirstMove = false;
        capture(move.capturedPiece);
    }

    private void promotePawn(Move move) {
        pieces.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        pieces.remove(piece);
    }

    public boolean sameTeam(Piece piece1, Piece piece2) {
        if (piece1 == null || piece2 == null) {
            return false;
        }
        return piece1.isWhite == piece2.isWhite;
    }

    public Piece findKing(boolean isWhite) {
        for (Piece piece : pieces) {
            if (piece.name.equals("King") && isWhite == piece.isWhite) {
                return piece;
            }
        }
        return null;
    }

    private void updateGameState() {
        Piece king = findKing(isWhiteTurn);
        if (checkScanner.isGameOver(king)) {
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                System.out.println(isWhiteTurn ? "Black wins" : "White wins");
                gameOverMessage = isWhiteTurn ? "Black wins" : "White wins";
            } else {
                System.out.println("Stalemate");
                gameOverMessage = "Stalemate";
            }
            isGameOver = true;
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            System.out.println("Draw");
            isGameOver = true;
        }
    }

    private boolean insufficientMaterial(boolean isWhite) {
        ArrayList<String> pieceNames = pieces.stream()
                .filter(piece -> piece.isWhite == isWhite)
                .map(piece -> piece.name)
                .collect(Collectors.toCollection(ArrayList::new));
        if (pieceNames.contains("Queen") || pieceNames.contains("Rook") || pieceNames.contains("Pawn")) {
            return false;
        }
        return pieceNames.size() <= 2;
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                graphics2d.setColor((col + row) % 2 == 0 ? new Color(227, 198, 181) : new Color(107, 105, 53));
                graphics.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
        if (this.selectedPiece != null) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (isValidMove(new Move(this, this.selectedPiece, col, row))) {
                        graphics2d.setColor(new Color(0, 255, 0, 60));
                        graphics2d.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                    }
                }
            }
            if (checkScanner.isKingChecked(new Move(this, this.selectedPiece, 0, 0))) {
                Piece king = findKing(selectedPiece.isWhite);
                graphics2d.setColor(new Color(255, 0, 0, 60));
                graphics2d.fillRect(king.col * tileSize, king.row * tileSize, tileSize, tileSize);
            }
        }
        for (Piece piece : pieces) {
            piece.paint(graphics2d);
        }
        if (isGameOver && !gameOverMessage.isEmpty()) {
            graphics2d.setColor(new Color(0, 0, 0, 150));
            graphics2d.fillRect(0, 0, cols * tileSize, rows * tileSize);
            graphics2d.setColor(Color.GREEN);
            graphics2d.setFont(graphics2d.getFont().deriveFont(30f));
            graphics2d.drawString(gameOverMessage, cols * tileSize / 2 - 50, rows * tileSize / 2);
        }
    }
}
