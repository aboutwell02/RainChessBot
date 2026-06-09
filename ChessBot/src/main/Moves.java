package main;

import java.util.Arrays;

public class Moves {
    // Edges of board for logic reasons
    static long FILE_A = 72340172838076673L; //  1000000010000000100000001000000010000000100000001000000010000000
    static long FILE_H = 9187201950435737472L; //  0000000100000001000000010000000100000001000000010000000100000001
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

    public static String validMovesWhite(String history, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        INVALID_WHITE_CAPTURES =~ (WP | WN | WB | WR | WQ | WK | BK); // Add Black king to avoid King's from being captured
        BLACK_PIECES = (BP | BN | BB | BR | BQ);
        EMPTY =~ (WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK);
        timeExperiment(WP);
        String list = possiblePawnWhite(history, WP);
        /*
        + possibleKnightWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleBishopWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleRookWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleQueenWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleKingWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        */
        return list;
    }

    public static String possiblePawnWhite(String history, long WP) {
        String list = "";
        // x1y1x2y2 format
        /* Movement Visualization
        00000000000000000000000000000000000000000000|e3|0000000|e2|00000000000
        0 0 0 0 0 0 0 0
        0 0 0 0 0 0 0 0
        0 0 0 0 0 0 0 0
        0 0 0 0 0 0 0 0
        0 0 0 0 0 0 0 0
        0 0 0 0 e3 0 0 0
        0 0 0 0 e2 0 0 0
        0 0 0 0 0 0 0 0
        e2 Pawn Moves forward 1 space to e3
        i = 12
        e2: x1: 4, y1 = 2 | ((12  % 8) + 1) = 5 FILE E = 5 | ((12 / 8) + 1) RANK 2 = 2
        e3: x2 = 4 y2 = 3 | (12 % 8 + 1) = 5 FILE E = 5 | (12 / 8) + 2) RANK 3 = 3
         */
        long PAWN_MOVES = (WP >> 7) & BLACK_PIECES &~ RANK_8 *~ FILE_A; // Pawn captures to the right and not on rank 8 and not on file A
        for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
            if (((PAWN_MOVES >> i) & 1) == 1) {list += "" + ((i / 8) + 1) + ((i % 8) - 1) + (i / 8) + (i % 8);} // Adds x1(1 Rank Down)y1(1 File Left)x2(Destination x)y2(Destination y)
        }

        PAWN_MOVES = (WP >> 9) & BLACK_PIECES &~ RANK_8 *~ FILE_H; // Pawn captures to the left and not on rank 8 and not on file H
        for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
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

        // Pawn Promotion Cases
        PAWN_MOVES = (WP >> 7) & BLACK_PIECES & RANK_8 &~ FILE_A; // Pawn Captures black piece to the RIGHT on rank 8 and not file A and PROMOTES
        for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
            if (((PAWN_MOVES >> i) & 1) == 1) {
                /* VISUALISATION
                0000000|h8|000000|g7|0000000000000000000000000000000000000000000000000
                a8 0 0 d8 0 0 0 h8
                0 b7 0 d7 0 0 g7 0
                0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0
                g7 White Pawn Takes Black Piece on h8 and Promotes to Queen
                i = 63 (h8)
                g7: x1 = ((63 % 8) - 1 = 7 - 1 = 6
                h8: x2 = (63 & 8) = 7
                PRODUCT: x1x2(PROMOTION)P 67QP
                b7 white pawn takes black piece on a8 and promotes to knight
                i = 56
                b7: x1 = (56 % 8) = 0 + 1 = 1
                a8: x2 = (56 % 8) = 0
                PRODUCT: 10NP
                pawn moves from d7 to d8 and promotes to rook
                i = 51
                d7: x1 = (51 % 8) = 3
                d8: x2 = (51 % 8) = 3
                PRODUCT: 33RP
                 */

                list += "" + ((i % 8) - 1) + (i % 8) + "QP" + ((i % 8) - 1) + (i % 8) + "RP" + ((i % 8) - 1) + (i % 8) + "BP" + ((i % 8) - 1) + (i % 8) + "NP";
            }
        }
        PAWN_MOVES = (WP >> 9) & BLACK_PIECES & RANK_8 &~ FILE_H; // Pawn Captures black piece to the LEFT on rank 8, not file H, and PROMOTES
        for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 - Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
            if (((PAWN_MOVES >> i) & 1 ) == 1) {list += "" + (i % 8 + 1) + (i % 8) + "QP" + (i % 8 + 1) + (i % 8) + "RP" + (i % 8 + 1) + (i % 8) + "BP" + (i % 8 + 1) + (i % 8) + "NP";}
        }
        PAWN_MOVES = (WP >> 8) & EMPTY & RANK_8; // Pawn Moves forward to last rank and promotes
        for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 - Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
            if (((PAWN_MOVES >> i) & 1) == 1) {list += "" + (i % 8) + (i % 8) + "QP" + (i % 8) + (i % 8) + "RP" + (i % 8) + (i % 8) + "BP" + (i % 8) + (i % 8) + "NP";}
        }
        // TODO: Add En Passant Rules
        // x1x2 SPACE (E)
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
    public static void timeExperiment(long WP) {
        int loopLength = 1000;
        long startTime = System.currentTimeMillis();
        methodA(loopLength, WP);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds for the Method A.");
        startTime = System.currentTimeMillis();
        methodB(loopLength, WP);
        endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds for Method B.");
    }
    public static void methodA(int loopLength, long WP) {
        for (int loop = 0; loop < loopLength; loop++) {
            long PAWN_MOVES = (WP >> 7) & BLACK_PIECES &~ RANK_8 &~ FILE_A; // capture RIGHT not on rank 8
            String list = "";
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

            PAWN_MOVES = (WP >> 7) & BLACK_PIECES * RANK_8 *~ FILE_A;
            for (int i = Long.numberOfTrailingZeros(PAWN_MOVES); i < 64 -  Long.numberOfTrailingZeros(PAWN_MOVES); i++) {
                if (((WP >> i) & 1) == 1) {

                }
            }
        }
    }
    public static void methodB(int loopLength, long WP) {
        for (int loop = 0; loop < loopLength; loop++) {
            long PAWN_MOVES = (WP >> 7) & BLACK_PIECES & RANK_8 &~ FILE_A;
            String list = "";
            long possibility = PAWN_MOVES & -PAWN_MOVES;
            while (possibility != 0) {
                drawBitboard(possibility);
                int index = Long.numberOfTrailingZeros(possibility);
                list += "" + (index / 8 + 1) + (index & 8) + (index % 8);
                PAWN_MOVES &=~ (possibility);
                possibility = PAWN_MOVES &~ (PAWN_MOVES - 1);
            }
        }
    }

}
