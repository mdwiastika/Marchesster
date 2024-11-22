package main;

import javax.swing.JFrame;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Marchesster");
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);

        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);

    }
}