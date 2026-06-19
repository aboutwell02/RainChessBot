package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface extends JPanel{
	static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
	static long WHITE_PIECES = 0L, BLACK_PIECES = 0L;
	static long universalWP = 0L, universalWN = 0L, universalWB = 0L, universalWR = 0L, universalWQ = 0L, universalWK = 0L, universalBP = 0L, universalBN = 0L, universalBB = 0L, universalBR = 0L, universalBQ = 0L, universalBK = 0L;
	static int humanIsWhite = 1;
	static int rating = 0;
	static int border = 10; // Amount of empty space around the frame
	static double squareSize = 64;
	static JFrame javaF = new JFrame("RainChess Bot created by ame");
	static UserInterface javaUI = new UserInterface(); // Must be declared as static so other classes can repaint
	static void main(String[] args) {
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

		/* TESTING BITBOARD TRAVERSAL AND NOTATION
		This traversal reads the bitboard right to left
		long traversal = 9223372036854775807L; // 1000000000000000000000000000000000000000000000000000000000000000
		for (int i = 0; i < 64; i++) {
			if (((traversal >> i) & 1) == 0) {
				System.out.println("1 is at index " + i);
			}
		}
		This traversal reads left to right
		long traversal = -9223372036854775808L;
		for (int i = 0; i < 64; i++) {
			if (((traversal << i) & 1) == 0) {
				System.out.println("1 is at index " + i);
			} else {
				System.out.println("0 is at index: " + i);
			}
		}
		long test = 922337203685477587L;
		long testWhitePawns = 65280L;
		int file1, file2;
		String letter1 = "", letter2 = "";
		for (int i = 0; i < 64; i++) {
			if (((testWhitePawns >> i)&1) == 1) {
				file1 = ((i % 8));
				letter1 = switch (file1) {
					case 0 -> "a";
					case 1 -> "b";
					case 2 -> "c";
					case 3 -> "d";
					case 4 -> "e";
					case 5 -> "f";
					case 6 -> "g";
					case 7 -> "h";
					default -> letter1;
				};
				file2 = ((i % 8));
				letter2 = switch (file2) {
					case 0 -> "a";
					case 1 -> "b";
					case 2 -> "c";
					case 3-> "d";
					case 4 -> "e";
					case 5 -> "f";
					case 6 -> "g";
					case 7 -> "h";
					default -> letter2;
				};

				System.out.println("Starting Position: " + letter1 + ", " + ((i % 8) + 1) + " | Ending Position: " + letter2 + ", " + ((i % 8) + 2));
			}

		}
		*/
		drawBorders(g);
		drawBoard(g);
		drawPieces(g);
		//testPNGDraw(g);
		//System.out.println("UI successfully generated");
	}
	public void testPNGDraw(Graphics g) {
		Image pieceImage;
		
		pieceImage = new ImageIcon("C:/Users/ajab0/git/RainChessBot/ChessBot/res/ChessPiecesArray.png").getImage();
		// Black Queen on a8
		g.drawImage(pieceImage, (border), (border), (int)(border+squareSize), (int)(border+squareSize), 0, 0, 60, 60, null);
		// Black Queen on d8
		g.drawImage(pieceImage, (int)((4)*squareSize)+border, border, (int)((4+1)*squareSize)+border, (int)((1)*squareSize)+border, 0, 0, 60, 60, null);
		//White Pawn on e2
		g.drawImage(pieceImage, (int)((4)*squareSize)+border, (int)((6)*squareSize)+border, (int)((4+1)*squareSize)+border, (int)((6+1)*squareSize)+border, (5*60), 60, ((5+1)*60), ((1+1)*60), null);
		// White King on e1
		g.drawImage(pieceImage, (int)((4)*squareSize)+border, (int)((7)*squareSize)+border, (int)((4+1)*squareSize)+border, (int)((7+1)*squareSize)+border, 60, 60, ((1+1)*60), ((1+1)*60), null);

	}
	public void drawBoard(Graphics g) {
		for (int i = 0; i < 64; i += 2) {
			g.setColor(new Color(255, 200, 100));
			g.fill3DRect((int) (((i%8)+(i/8)%2)*squareSize)+border, (int) ((i/8)*squareSize)+border, (int) (squareSize), (int) (squareSize), true);
			g.setColor(new Color(150, 50, 30));
			g.fill3DRect((int) (((i+1)%8-((i+1)/8)%2)*squareSize)+border, (int) (((i+1)/8)*squareSize)+border, (int) (squareSize), (int) (squareSize), true);
		}
	}
	public void drawPieces(Graphics g) {
		// Single PNG Method
		Image pieceImage;
		
		pieceImage = new ImageIcon("C:/Users/ajab0/git/RainChessBot/ChessBot/res/ChessPiecesArray.png").getImage();
		// Testing for CorrectBoard Generation
		//BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
		/* Bitwise Operations Debugger
		long test = 2L;
		System.out.println(test);
		if (((test>>1)& 1) == 1) {
			long res = test>>1;
			System.out.println("Success! " + res);
		}
		*/
		
		/* Visualizing Bitboards
		System.out.println(WP);
		System.out.println(WN);
		System.out.println(WB);
		System.out.println(WR);
		System.out.println(WQ);
		System.out.println(WK);
		System.out.println(BP);
		System.out.println(BN);
		System.out.println(BB);
		System.out.println(BR);
		System.out.println(BQ);
		System.out.println(BK);
		*/


		// Every time window size is manipulated, for loop is called twice
		for (int i = 0; i < 64; i++) {
			//System.out.println("Iteration " + (i+1));
			int j = -1, k = -1;
			
			if (((WP>>i)&1) == 1) {j = 5; k = humanIsWhite;}
			if (((BP>>i)&1) == 1) {j = 5; k = 1 - humanIsWhite;}
			if (((WN>>i)&1) == 1) {j = 3; k = humanIsWhite;}
			if (((BN>>i)&1) == 1) {j = 3; k = 1 - humanIsWhite;}
			if (((WB>>i)&1) == 1) {j = 4; k = humanIsWhite;}
			if (((BB>>i)&1) == 1) {j = 4; k = 1 - humanIsWhite;}
			if (((WR>>i)&1) == 1) {j = 2; k = humanIsWhite;}
			if (((BR>>i)&1) == 1) {j = 2; k = 1 - humanIsWhite;}
			if (((WQ>>i)&1) == 1) {j = 0; k = humanIsWhite;}
			if (((BQ>>i)&1) == 1) {j = 0; k = 1 - humanIsWhite;}
			if (((WK>>i)&1) == 1) {j = 1; k = humanIsWhite;}
			if (((BK>>i)&1) == 1) {j = 1; k = 1 - humanIsWhite;}
			
			//System.out.println(BP);
			if (j != -1 && k != -1) {
				/*
				 * g.drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer)
				 * d == destination, s == source image
				 * humanIsWhite == 1, black_source_height == 0 pixels, white_source_height == 60 pixels 
				 * PiecesIndex | Pawn == 300 Pixels | Knight == 180 pixels | Bishops == 240 pixels | Rooks == 120 pixels | Queens == 0 pixels | Kings == 60 pixels
				 * 
				*/
				g.drawImage(pieceImage, (int)((i%8)*squareSize)+border, (int)((i/8)*squareSize)+border, (int)((i%8+1)*squareSize)+border, (int)((i/8+1)*squareSize)+border, (j*60), (k*60), ((j+1)*60), ((k+1)*60), null);
			}
		}
		
	}
	public void drawBorders(Graphics g) {
		// g.fillRect3D(xPos, yPos, xSize, ySize, raised)
		// Brown
		g.setColor(new Color(100, 0, 0));
		g.fill3DRect(0, 0, border, (int) (8 * squareSize) + border * 2, true);
		g.fill3DRect((int) (8 * squareSize) + border, 0, border, (int) (8 * squareSize) + border * 2, true);
		g.fill3DRect(border, 0, (int) (8 * squareSize), border, true);
		g.fill3DRect(border, (int) (8 * squareSize) + border, (int) (8 * squareSize), border, true);
		/*
		 * Black
		 * g.setColor(Color.BLACK);
		 * g.fill3DRect((int) (8 * squareSize) + border, 0, border, border, true);
		 * g.fill3DRect((int) (8 * squareSize) + border, border, border, border, true);\
		 * g.fill3DRect(0, (int) (8 * squareSize) + border, border, border, true);
		 * g.fill3DRect((int) (8 * squareSize) + 2 * border + 200, 0, border, border, true);
		 * g.fill3DRect((int) (8 * squareSize) + 2 * border, + 200, (int) (8 * squareSize), border, border, true);
		 */
		// Green
		g.setColor(new Color(0, 100, 0));
		g.fill3DRect((int) (8 * squareSize) + 2 * border, 0, 200, border, true);
		g.fill3DRect((int) (8 * squareSize) + 2 * border + 200, 0, border, (int) (8 * squareSize) + border * 2, true);
		g.fill3DRect((int) (8 * squareSize) + 2 * border, (int) (8 * squareSize) + border, 200, border, true);
	}
	public static void newGame() {
		BoardGeneration.initiateStandardBoard();
		Moves.validMovesWhite("", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
		Moves.validMovesBlack("", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
	}
	
}
