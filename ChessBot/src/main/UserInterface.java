package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface extends JPanel{
	static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
	static long universalWP = 0L, universalWN = 0L, universalWB = 0L, universalWR = 0L, universalWQ = 0L, universalWK = 0L, universalBP = 0L, universalBN = 0L, universalBB = 0L, universalBR = 0L, universalBQ = 0L, universalBK = 0L;
	static int humanIsWhite = 1;
	static int rating = 0;
	static int border = 10; // Amount of empty space around the frame
	static double squareSize = 64;
	static JFrame javaF = new JFrame("RainChess Bot created by ame");
	static UserInterface javaUI = new UserInterface(); // Must be declared as static so other classes can repaint
	public static void main(String[] args) {
		javaF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		javaF.add(javaUI);
		javaF.setSize(757, 570);
		javaF.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-javaF.getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height-javaF.getHeight()) / 2);
		javaF.setVisible(true);
		newGame();
		javaF.repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(new Color(200, 100, 0));
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				squareSize = (double) (Math.min(getHeight(),  getWidth() - 200 - border) - 2 * border) / 8;
			}
		});
		drawBorders(g);
		drawBoard(g);
		drawPieces(g);
	}
	public void drawBoard(Graphics g) {
		for (int i = 0; i < 64; i += 2) {
			g.setColor(new Color(255, 200, 100));
		}
	}
	public void drawPieces(Graphics g) {
		
	}
	public void drawBorders(Graphics g) {
		
	}
	public static void newGame() {
		BoardGeneration.initiateStandardBoard();
	}
	
}
