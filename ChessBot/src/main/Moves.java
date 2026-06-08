package main;

import java.util.Arrays;

public class Moves {
    // Edges of board for logic reasons
    static long FILE_A = 9187201950435737472L; //  1000000010000000100000001000000010000000100000001000000010000000
    static long FILE_H = 72340172838076673L; //  0000000100000001000000010000000100000001000000010000000100000001
    static long FILE_AB = 4557430888798830400L; // 1100000011000000110000001100000011000000110000001100000011000000
    static long FILE_GH = 217020518514230019L; // 0000001100000011000000110000001100000011000000110000001100000011

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

    public static String possibleMovesWhite(String history, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        INVALID_WHITE_CAPTURES =~ (WP | WN | WB | WR | WQ | WK | BK); // Add Black king to avoid King's from being captured
        BLACK_PIECES = (BP | BN | BB | BR | BQ);
        EMPTY =~ (WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK);
        //timeExperiment(WP);
        String list =  "temp";// possiblePawnWhite(history, WP);
        /*
        + possibleKnightWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleBishopWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleRookWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleQueenWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        + possibleKingWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        */
        return list;
    }




}
