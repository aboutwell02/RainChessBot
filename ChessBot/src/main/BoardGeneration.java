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
		
		/* LONDON SYSTEM (DEBUGGING BITBOARDS)
		String chessBoard[][] = {
				{"R", " ", " ", "Q", " ", "R", "K", " "},
				{"P", "P", "P", " ", " ", "P", "P", "P"},
				{" ", " ", "N", " ", "P", "N", " ", " "},
				{" ", " ", " ", "P", " ", " ", " ", " "},
				{" ", "B", " ", "p", " ", "b", "B", " "},
				{" ", " ", "p", "b", "p", "n", " ", " "},
				{"p", "p", " ", "n", " ", "p", "p", "p"},
				{"r", " ", " ", "q", "k", " ", " ", "r"}
		};
		*/
		
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
		while (counter - 1< random4a) {
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
