package main;

import java.util.Arrays;

public class BoardGeneration {

	public static void initiateStandardBoard() {
		/*
		Capitals determines color:
		Pawns = p (White) / P (Black)
		Knights = n(White) / N (Black)
		Bishops = b (White) / B (Black)
		Rooks = r (white) / R (Black)
		Queens = q (White) / Q (Black)
		Kings = k (white) / K (Black)
		*/

		long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
		/*
		MAP REPRESENTATION
		A8 B8 C8 D8 E8 F8 G8 H8
		A7 B7 C7 D7 E7 F7 G7 H7
		A6 B6 C6 D6 E6 F6 G6 H6
		A5 B5 C5 D5 E5 F5 G5 H5
		A4 B4 C4 D4 E4 F4 G4 H4
		A3 B3 C3 D3 E3 F3 G3 H3
		A2 B2 C2 D2 E2 F2 G2 H2
		A1 B1 C1 D1 E1 F1 G1 H1

		BOARD COORDINATES
		(0, 0) (1, 0) (2, 0) (3, 0) (4, 0) (5, 0) (6, 0) (7, 0)
		(0, 1) (1, 1) (2, 1) (3, 1) (4, 1) (5, 1) (6, 1) (7, 1)
		(0, 2) (1, 2) (2, 2) (3, 2) (4, 2) (5, 2) (6, 2) (7, 2)
		(0, 3) (1, 3) (2, 3) (3, 3) (4, 3) (5, 3) (6, 3) (7, 3)
		(0, 4) (1, 4) (2, 4) (3, 4) (4, 4) (5, 4) (6, 4) (7, 4)
		(0, 5) (1, 5) (2, 5) (3, 5) (4, 5) (5, 5) (6, 5) (7, 5)
		(0, 6) (1, 6) (2, 6) (3, 6) (4, 6) (5, 6) (6, 6) (7, 6)
		(0, 7) (1, 7) (2, 7) (3, 7) (4, 7) (5, 7) (6, 7) (7, 7)

		BOARD INDEX
		00 01 02 03 04 05 06 07
		08 09 10 11 12 13 14 15
		16 17 18 19 20 21 22 23
		24 25 26 27 28 29 30 31
		32 33 34 35 36 37 38 39
		40 41 42 43 44 45 46 47
		48 49 50 51 52 53 54 55
		56 57 58 59 60 61 62 63

		x formula: index % 8 | eg. 28 % 8 = 4, x = 4 | eg. 11 % 8 = 3, x = 3 | eg 63 % 8 = 7, x = 7
		y formula: index / 8 | eg. 28 / 8 = 3, y = 3 | eg. 11 / 8 = 1, y = 1 | eg 63 / 8 = 7, y = 7
		 */

		/*
		// LONDON SYSTEM (DEBUGGING BITBOARDS)
		String chessBoard[][] = {
				{"R", " ", " ", " ", " ", "R", "K", " "},
				{"P", "P", "P", " ", " ", "P", "P", "P"},
				{" ", " ", "N", " ", "Q", "N", " ", " "},
				{" ", " ", " ", "P", "P", " ", " ", " "},
				{" ", "B", " ", "p", " ", "b", "B", " "},
				{"p", " ", "p", " ", "p", "n", " ", "p"},
				{" ", "p", " ", "n", " ", "p", "p", " "},
				{"r", " ", " ", "q", " ", "r", "k", " "}
		};
		// EMPTY BOARD
		String chessBoard[][] = {
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "}
		};
		*/
		// DEBUGGING BOARD
		String chessBoard[][] = {
				{"Q", "P", " ", "Q", "P", " ", "Q", "P"},
				{" ", "p", " ", " ", "p", " ", " ", "p"},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "}
		};
		/*
		// Game Starting Position
		String chessBoard[][] = {
				{"R", "N", "B", "Q", "K", "B", "N", "R"},
				{"P", "P", "P", "P", "P", "P", "P", "P"},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{"p", "p", "p", "p", "p", "p", "p", "p"},
				{"r", "n", "b", "q", "k", "b", "n", "r"}
		};
		*/
		/* Bitboard examples:
		 Black Rooks: "1000000100000000000000000000000000000000000000000000000000000000"
		 White Pawns: "0000000000000000000000000000000000000000000000001111111100000000"
		 
		 Convert the string into binary
		*/
		arrayToBitboards(chessBoard, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
	}
	
	public static void initiateChess960() {
		long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
		String chessBoard[][] = {
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{"P", "P", "P", "P", "P", "P", "P", "P"},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{"p", "p", "p", "p", "p", "p", "p", "p"},
				{" ", " ", " ", " ", " ", " ", " ", " "}
		};
		// Step 1:
		int random1 = (int) (Math.random() * 8);
		chessBoard[0][random1] = "B";
		chessBoard[7][random1] = "b";
		// Step 2;
		int random2 = (int) (Math.random() * 8);
		while (random2 % 2 == random1 % 2) {
			random2 = (int) (Math.random() * 8);
		}
		chessBoard[0][random2] = "B";
		chessBoard[7][random2] = "b";
		// Step 3:
		int random3 = (int) (Math.random() * 8);
		while (random3 == random1 || random3 == random2) {
			random3 = (int) (Math.random() * 8);
		}
		chessBoard[0][random3] = "Q";
		chessBoard[7][random3] = "q";
		// Step 4:
		int random4a = (int) (Math.random() * 5); // 3 squares have been filled
		int counter = 0;
		int loop = 0;
		while (counter - 1 < random4a) {
			if (" ".equals(chessBoard[0][loop])) {counter++;}
			loop++;
		}
		chessBoard[0][loop-  1] = "N";
		chessBoard[7][loop - 1] = "n";
		int random4b = (int) (Math.random() * 4); // 4 squares have been filled
		counter = 0;
		loop = 0;
		while (counter - 1 < random4b) {
			if (" ".equals(chessBoard[0][loop])) {counter++;}
			loop++;
		}
		chessBoard[0][loop - 1] = "N";
		chessBoard[7][loop - 1] = "n";
		// Step 5: King needs to be in between rooks
		counter = 0;
		while (!" ".equals(chessBoard[0][counter])) {
			counter++;
		}
		chessBoard[0][counter] = "R";
		chessBoard[7][counter] = "r";
		while (!" ".equals(chessBoard[0][counter])) {
			counter++;
		}
		chessBoard[0][counter] = "K";
		chessBoard[7][counter] = "k";
		while(!" ".equals(chessBoard[0][counter])) {
			counter++;
		}
		chessBoard[0][counter] = "R";
		chessBoard[7][counter] = "r";
		arrayToBitboards(chessBoard, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
	}
	
	public static void arrayToBitboards(String[][] chessBoard, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
		/*
		//UNOPTIMIZED BITBOARD CONVERSION
		String binary;
		for (int i = 0; i < 64; i++) {
			binary = "0000000000000000000000000000000000000000000000000000000000000000";
			binary = binary.substring(i + 1) + "1" + binary.substring(0, i);
			switch (chessBoard[i / 8][i % 8]) {
				case "p": WP += convertStringToBitboard(binary);
					break;
				case "n": WN += convertStringToBitboard(binary);
					break;
				case "b": WB += convertStringToBitboard(binary);
					break;
				case "r": WR += convertStringToBitboard(binary);
					break;
				case "q": WQ += convertStringToBitboard(binary);
					break;
				case "k": WK += convertStringToBitboard(binary);
					break;
				case "P": BP += convertStringToBitboard(binary);
					break;
				case "N": BN += convertStringToBitboard(binary);
					break;
				case "B": BB += convertStringToBitboard(binary);
					break;
				case "R": BR += convertStringToBitboard(binary);
					break;
				case "Q": BQ += convertStringToBitboard(binary);
					break;
				case "K": BK += convertStringToBitboard(binary);
					break;
			}
		}
		drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
		*/
		
		
		long binary = 0b01L; // this is 1 in binary
		for (int i = 0; i < 64; i++) {
			switch (chessBoard[i / 8][i % 8]) {
			case "p": WP += binary; break;
			case "n": WN += binary; break;
			case "b": WB += binary; break;
			case "r": WR += binary; break;
			case "q": WQ += binary; break;
			case "k": WK += binary; break;
			case "P": BP += binary; break;
			case "N": BN += binary; break;
			case "B": BB += binary; break;
			case "R": BR += binary; break;
			case "Q": BQ += binary; break;
			case "K": BK += binary; break;
			}
			binary = binary<<1;
		}
		drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
		UserInterface.WP = WP;
		UserInterface.WN = WN;
		UserInterface.WB = WB;
		UserInterface.WR = WR;
		UserInterface.WQ = WQ;
		UserInterface.WK = WK;
		UserInterface.BP = BP;
		UserInterface.BN = BN;
		UserInterface.BB = BB;
		UserInterface.BR = BR;
		UserInterface.BQ = BQ;
		UserInterface.BK = BK;
	}
	
	public static long convertStringToBitboard(String binary) {
		if (binary.charAt(0) == '0') { // Not a negative number
			return Long.parseLong(binary, 2);
		} else { // If negative
			return Long.parseLong("1"+binary.substring(2), 2) * 2;
		}
	}
	public static void drawArray(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
		String chessBoard[][] = new String[8][8];
		for (int i = 0; i < 64; i++) {
			chessBoard[i / 8][i % 8] = " ";
		}
		for (int i = 0; i < 64; i++) {
			if (((WP>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "p";}
			if (((WN>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "n";}
			if (((WB>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "b";}
			if (((WR>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "r";}
			if (((WQ>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "q";}
			if (((WK>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "k";}
			if (((BP>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "P";}
			if (((BN>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "N";}
			if (((BB>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "B";}
			if (((BR>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "R";}
			if (((BQ>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "Q";}
			if (((BK>>i) & 1) == 1) {chessBoard[i / 8][i % 8] = "K";}
		}
		for (int i = 0; i < 8; i++) {
			System.out.println(Arrays.toString(chessBoard[i]));
		}
	}
}
