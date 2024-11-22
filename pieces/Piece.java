package pieces;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Board;

public abstract class Piece {
    public int col;
    public int row;
    public int xPosition;
    public int yPosition;

    public boolean isWhite;
    public String name;
    protected int value;
    public boolean isFirstMove = true;
    protected Image sprite;
    protected Board board;
    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(new File("res/pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected int sheetScale = sheet.getWidth() / 6;

    public Piece(Board board) {
        this.board = board;
    }

    public abstract boolean isValidMovement(int col, int row);

    public abstract boolean moveCollidesWithPiece(int col, int row);

    public void paint(Graphics2D graphics2d) {
        graphics2d.drawImage(sprite, xPosition, yPosition, null);
    }
}
