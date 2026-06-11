package main;

import java.util.Arrays;

public class Moves {
    // Edges of board for logic reasons
    static long FILE_A = 72340172838076673L; //  1000000010000000100000001000000010000000100000001000000010000000
    static long FILE_H = 0x8080808080808080L; //  0000000100000001000000010000000100000001000000010000000100000001
    static long FILE_AB = 217020518514230019L; // 1100000011000000110000001100000011000000110000001100000011000000
    static long FILE_GH = 4557430888798830400L; // 0000001100000011000000110000001100000011000000110000001100000011

   // Pawn Promotions, Two Steps, and En Passant
    static long RANK_1 = 72057594037927936L; // 1111111100000000000000000000000000000000000000000000000000000000
    static long RANK_4 = 1095216660480L; // 0000000000000000000000001111111100000000000000000000000000000000
    static long RANK_5 = 4278190080L; // 0000000000000000000000000000000011111111000000000000000000000000
    static long RANK_8 = 255L; // 0000000000000000000000000000000000000000000000000000000011111111

    // Engine Evaluation
    static long CENTER = 103481868288L;
    static long EXTENDED_CENTER = 66229406269440L;
    static long KING_SIDE = 1085102592571150096L;
    static long QUEEN_SIDE = 1085102592571150095L;

    // Testing Pieces
    static long KING_B7 = 460039L;
    static long KNIGHT_C6 = 43234889994L;

    // Chess Rules
    static long INVALID_WHITE_CAPTURES;
    static long BLACK_PIECES;
    static long EMPTY;

    // Bitmasks
    static long FileMasks[] =  {0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L, 0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L};
    static long RankMasks[] = {0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L};

    public static String validMovesWhite(String history, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        INVALID_WHITE_CAPTURES =~ (WP | WN | WB | WR | WQ | WK | BK); // Add Black king to avoid King's from being captured
        BLACK_PIECES = (BP | BN | BB | BR | BQ);
        EMPTY =~ (WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK);
        timeExperiment(history, WP, BP);
        String list = possiblePawnWhite(history, WP, BP);
        /*
        + possibleKnightWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleBishopWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleRookWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleQueenWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleKingWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        */
        return list;
    }

    public static String possiblePawnWhite(String history, long WP, long BP) {
        // OPTIMIZED METHOD FOR VALID MOVE SEARCH (~4 times faster than previous method)
        int index;
        String list = "";

        // PAWN REGULAR MOVEMENT
        long PAWN_MOVES = (WP >> 7) & BLACK_PIECES & ~RANK_8 & ~FILE_A; // Pawn captures to the RIGHT
        long possibility = PAWN_MOVES & -PAWN_MOVES;
        //System.out.println("Before RIGHT CAPTURES Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + ((index % 8) - 1) + ((index / 8) + 1) + (index % 8) + (index / 8); // x1y1 = 1 left & 1 down
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES & -PAWN_MOVES;
        }
        //System.out.println("After RIGHT CAPTURE Loop: " + possibility);
        // TODO: LEFT CAPTURES NOT BEING SEEN
        PAWN_MOVES = (WP >> 9) & BLACK_PIECES & ~RANK_8 & ~FILE_H; // Pawn captures to the LEFT
        possibility = PAWN_MOVES & -PAWN_MOVES;
        //System.out.println("Debugging test");
        //System.out.println("Before LEFT CAPTURES Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + ((index % 8) + 1) + ((index / 8) + 1) + (index % 8) + (index / 8); // x1y1 = 1 right & 1 down
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES & -PAWN_MOVES;
        }
        //System.out.println("After LEFT CAPTURE Loop: " + possibility);
        PAWN_MOVES = (WP >> 8) & EMPTY & ~RANK_8; // Pawn moves FORWARD ONE square
        possibility = PAWN_MOVES & -PAWN_MOVES;
        //System.out.println("Before FORWARD STEP Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + ((index % 8)) + ((index / 8) + 1) + (index % 8) + (index / 8); // x1y1 = same & 1 down
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES;
        }
        //System.out.println("After FORWARD STEP Loop: " + possibility);
        PAWN_MOVES = (WP >> 16) & EMPTY & (EMPTY >> 8) & RANK_4; // Pawn moves FORWARD TWO squares as its first move
        possibility = PAWN_MOVES & -PAWN_MOVES;
        //System.out.println("Before TWO STEP Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + (index % 8) + ((index / 8) + 2) + (index % 8) + (index / 8); // x1y1 = same & 2 down
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES & -PAWN_MOVES;
        }
        //System.out.println("After TWO STEP Loop: " + possibility);

        // PAWN PROMOTIONS
        // x1x2(PROMOTION)P
        PAWN_MOVES = (WP >> 7) & BLACK_PIECES & RANK_8 & ~FILE_A; // Pawn captures RIGHT and PROMOTES
        possibility = PAWN_MOVES;
        //System.out.println("Before RIGHT PROMOTION Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + ((index % 8) - 1) + (index % 8) + "QP" + ((index % 8) - 1) + (index % 8) + "RP" + ((index % 8) - 1) + (index % 8) + "BP" + ((index % 8) - 1) + (index % 8) + "NP"; // x1 = 1 left
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES & -PAWN_MOVES;
        }
        //System.out.println("After RIGHT PROMOTION Loop: " + possibility);
        PAWN_MOVES = (WP >> 9) & BLACK_PIECES & RANK_8 & ~FILE_H; // Pawn captures LEFT and PROMOTES
        possibility = PAWN_MOVES;
        //System.out.println("Before LEFT PROMOTION Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + ((index % 8) + 1) + (index % 8) + "QP" + ((index % 8) + 1) + (index % 8) + "RP" + ((index % 8) + 1) + (index % 8) + "BP" + ((index % 8) + 1) + (index % 8) + "NP"; // x1 = 1 right
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES & -PAWN_MOVES;
        }
        //System.out.println("After LEFT PROMOTION Loop: " + possibility);
        PAWN_MOVES = (WP >> 8) & EMPTY & RANK_8; // Pawn moves FORWARD one square and PROMOTES
        possibility = PAWN_MOVES;
        //System.out.println("Before FORWARD PROMOTION Loop: " + possibility);
        while (possibility != 0) {
            //drawBitboard(possibility);
            index = Long.numberOfTrailingZeros(possibility);
            list += "" + (index % 8) + (index % 8) + "QP" + (index % 8) + (index % 8) + "RP" + (index % 8) + (index % 8) + "BP" + (index % 8) + (index % 8) + "NP"; // x1 = same
            PAWN_MOVES &= ~(possibility);
            possibility = PAWN_MOVES & -PAWN_MOVES;}
        //System.out.println("After FORWARD PROMOTION Loop: " + possibility);
        //System.out.println(list);


        return list;
    }
    public static void drawBitboard (long bitboard) {
        String chessBoard[][] = new String[8][8];
        for (int i = 0; i < 64; i++) {
            chessBoard[i / 8][i % 8] = "";
        }
        for (int i = 0; i < 64; i++) {
            if (((bitboard >>> i) & 1) == 1) {chessBoard[i / 8][i % 8] = "p";}
            if ("".equals(chessBoard[i / 8][i % 8])) {chessBoard[i / 8][i % 8] = " ";}
        }
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
    public static void timeExperiment(String history, long WP, long BP) {
        int loopLength = 1;
        long startTime = System.currentTimeMillis();
       // System.out.println("That took " + (endTime - startTime) + " milliseconds for the Method A.");
        methodB(loopLength, history, WP, BP);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds for Method B.");
    }
    public static void methodA(int loopLength, String history, long WP) {
        for (int loop = 0; loop < loopLength; loop++) {
            String list = "";
            long PAWN_MOVES = (WP >> 7) & BLACK_PIECES &~ RANK_8 &~ FILE_A; // capture RIGHT not on rank 8
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 - Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((PAWN_MOVES >> i) & 1) == 1) {list += "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);}
            }
            PAWN_MOVES = (WP >> 9) & BLACK_PIECES &~ RANK_8 &~ FILE_H; // capture LEFT not on rank 8
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 - Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((PAWN_MOVES >> i) & 1) == 1) {list += "" + ((i / 8) + 1) + ((i % 8) + 1) + (i / 8) + (i % 8);}
            }
            PAWN_MOVES = (WP >> 8) & EMPTY &~ RANK_8; // Pawn moves forward 1 and not on rank 8
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((PAWN_MOVES >> i) & 1) == 1) {list += "" + ((i / 8) + 1) + ((i % 8) + 1) + (i / 8) + (i % 8);}
            }
            PAWN_MOVES = (WP >> 16) & EMPTY & (EMPTY >> 8) & RANK_4; // Pawn moves two steps from starting position only onto rank 4
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((PAWN_MOVES >> i) & 1) == 1) {list += "" + ((i / 8) + 2) + (i % 8) + (i / 8) + (i % 8);}
            }
            PAWN_MOVES = (WP >> 7) & BLACK_PIECES & RANK_8 &~ FILE_A; // Pawn capturing RIGHT and PROMOTING
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((WP >> i) & 1) == 1) {list += "" + ((i % 8) - 1) + (i % 8) + "QP" + ((i % 8) - 1) + (i % 8) + "RP" + ((i % 8) - 1) + (i % 8) + "BP" + ((i % 8) - 1) + (i % 8) + "NP";}
            }
            PAWN_MOVES = (WP >> 9) & BLACK_PIECES & RANK_8 &~ FILE_H; // Pawn capturing LEFT and PROMOTING
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 - Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((WP >> i) & 1) == 1) {list += "" + (i % 8 + 1) + (i % 8) + "QP" + (i % 8 + 1) + (i % 8) + "RP" + (i % 8 + 1) + (i % 8) + "BP" + (i % 8 + 1) + (i % 8) + "NP";}
            }
            PAWN_MOVES = (WP >> 8) & EMPTY & RANK_8; // Pawn moves FORWARD one onto empty Rank 8 square
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 - Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((WP >> i) & 1) == 1) {list += "" + (i % 8) + (i % 8) + "QP" + (i % 8) + (i % 8) + "RP" + (i % 8) + (i % 8) + "BP" + (i % 8) + (i % 8) + "NP";}
            }
            System.out.println(list);
        }
    }
    public static void methodB(int loopLength, String history, long WP, long BP) {
        int index;
        String list = "";
        for (int loop = 0; loop < loopLength; loop++) {
            long PAWN_MOVES = (WP >> 7) & BLACK_PIECES &~ RANK_8 &~ FILE_A; // Pawn captures to the RIGHT
            long possibility = PAWN_MOVES & -PAWN_MOVES;
            //System.out.println("Before RIGHT CAPTURES Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" + ((index % 8) - 1) + ((index / 8) + 1) + (index % 8) + (index / 8); // x1y1 = 1 left & 1 down
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES & -PAWN_MOVES;
            }
            //System.out.println("After RIGHT CAPTURE Loop: " + possibility);
            PAWN_MOVES = (WP >> 9) & BLACK_PIECES &~ RANK_8 &~ 0x8080808080808080L; // Pawn captures to the LEFT
            possibility = PAWN_MOVES & -PAWN_MOVES;
            //System.out.println("Before LEFT CAPTURES Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" +((index % 8) + 1) + ((index / 8) + 1) + (index % 8) + (index / 8); // x1y1 = 1 right & 1 down
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES & -PAWN_MOVES;
            }
            //System.out.println("After LEFT CAPTURE Loop: " + possibility);
            PAWN_MOVES = (WP >> 8) & EMPTY &~ RANK_8; // Pawn moves FORWARD ONE square
            possibility = PAWN_MOVES &- PAWN_MOVES;
            System.out.println("Before FORWARD STEP Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                System.out.println(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" + (index % 8) + ((index / 8) + 1) + (index % 8) + ((index / 8)); // x1y1 = same & 1 down
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES;
            }
            //System.out.println("After FORWARD STEP Loop: " + possibility);
            PAWN_MOVES = (WP >> 16) & EMPTY & (EMPTY >> 8) & RANK_4; // Pawn moves FORWARD TWO squares as its first move
            possibility = PAWN_MOVES & -PAWN_MOVES;
            //System.out.println("Before TWO STEP Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" + (index % 8) + ((index / 8) + 2) + (index % 8) + (index / 8); // x1y1 = same & 2 down
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES & -PAWN_MOVES;
            }
            //System.out.println("After TWO STEP Loop: " + possibility);
            PAWN_MOVES = (WP >> 7) & BLACK_PIECES & RANK_8 &~ FILE_A; // Pawn captures RIGHT and PROMOTES
            possibility = PAWN_MOVES;
            //System.out.println("Before RIGHT PROMOTION Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" + ((index % 8) - 1) + (index % 8) + "QP" + ((index % 8) - 1) + (index % 8) + "RP" + ((index % 8) - 1) + (index % 8) + "BP" + ((index % 8) - 1) + (index % 8) + "NP"; // x1 = 1 left
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES & -PAWN_MOVES;
            }
            //System.out.println("After RIGHT PROMOTION Loop: " + possibility);
            PAWN_MOVES = (WP >> 9) & BLACK_PIECES & RANK_8 &~ FILE_H; // Pawn captures LEFT and PROMOTES
            possibility = PAWN_MOVES;
            //System.out.println("Before LEFT PROMOTION Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" + (index % 8 + 1) + (index % 8) + "QP" + (index % 8 + 1) + (index % 8) + "RP" + (index % 8 + 1) + (index % 8) + "BP" + (index % 8 + 1) + (index % 8) + "NP"; // x1 = 1 right
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES & -PAWN_MOVES;
            }
            //System.out.println("After LEFT PROMOTION Loop: " + possibility);
            PAWN_MOVES = (WP >> 8) & EMPTY & RANK_8; // Pawn moves FORWARD one square and PROMOTES
            possibility = PAWN_MOVES;
            //System.out.println("Before FORWARD PROMOTION Loop: " + possibility);
            while (possibility != 0) {
                //drawBitboard(possibility);
                index = Long.numberOfTrailingZeros(possibility);
                list += "" + (index % 8) + (index % 8) + "QP" + (index % 8) + (index % 8) + "RP" + (index % 8) + (index % 8) + "BP" + (index % 8) + (index % 8) + "NP"; // x1 = same
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES & -PAWN_MOVES;
            }
            //System.out.println("After FORWARD PROMOTION Loop: " + possibility);
            //System.out.println(list);
            // TODO: Add En Passant Rules
            // EN PASSANT "x1x2 SPACE E"
            if (history.length() >= 4) {
                System.out.println("History.charAt(history.length()) - 1) == " + history.charAt(history.length() - 1));
                System.out.println("history.charAt(history.length() - 3) == " + history.charAt(history.length() - 3));
                if (history.charAt(history.length() - 1) == history.charAt(history.length() - 3) && Math.abs(history.charAt(history.length() - 2) - history.charAt(history.length() - 4)) == 2) {

                    int file = history.charAt(history.length() - 1) - '0';
                    possibility = (WP << 1) & BP & RANK_5 &~ FILE_A & FileMasks[file]; // En passant RIGHT
                    System.out.println("Right capture en passant at: " + possibility);
                    if (possibility != 0) {
                        index = Long.numberOfTrailingZeros(possibility);
                        list += "" + ((index % 8) - 1) + (index % 8) + "E"; // x1 = 1 left
                    }
                    possibility = (WP >> 1) & BP & RANK_5 &~ FILE_H & FileMasks[file]; // En passant LEFT
                    System.out.println("Left capture en passant at: " + possibility);
                    if (possibility != 0) {
                        index = Long.numberOfTrailingZeros(possibility);
                        list += "" + ((index % 8) + 1) + (index % 8) + "E"; // x1 = 1 right
                    }
                }
            }
        }
        System.out.println(list);
    }

}
