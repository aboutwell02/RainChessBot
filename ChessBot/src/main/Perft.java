package main;

public class Perft {

    static int perftTotalMoveCount = 0;
    static int perftMoveCounter = 0;
    static int perftMaxDepth = 5;

    public static String movementAlgebra(String move) {
        String moveString = "";
        moveString += "" + (char)(move.charAt(0) + 49);
        moveString += "" + ('8' - move.charAt(1));
        moveString += "" + (char)(move.charAt(2) + 49);
        moveString += "" + ('8' - move.charAt(3));
        return moveString;
    }
    public static void perft(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EN_PASSANT, boolean CQSW, boolean CKSW, boolean CQSB, boolean CKSB, boolean WHITE_TURN, int depth) {
        if (depth < perftMaxDepth) {
            String moves;
            if (WHITE_TURN) {
                moves = Moves.validMovesWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EN_PASSANT, CQSW, CKSW, CQSB, CKSB);
            } else {
                moves = Moves.validMovesBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EN_PASSANT, CQSW, CKSW, CQSB, CKSB);
            }
            for (int i = 0; i < moves.length(); i += 4) {
                long WPt = Moves.makeMove(WP, moves.substring(i, (i + 4)), 'p'), WNt = Moves.makeMove(WN, moves.substring(i, (i + 4)), 'n'),
                        WBt = Moves.makeMove(WB, moves.substring(i, (i + 4)), 'b'), WRt = Moves.makeMove(WR, moves.substring(i, (i + 4)), 'r'),
                        WQt = Moves.makeMove(WQ, moves.substring(i, (i + 4)), 'q'), WKt = Moves.makeMove(WK, moves.substring(i, (i + 4)), 'k'),
                        BPt = Moves.makeMove(BP, moves.substring(i, (i + 4)), 'P'), BNt = Moves.makeMove(BN, moves.substring(i, (i + 4)), 'N'),
                        BBt = Moves.makeMove(BB, moves.substring(i, (i + 4)), 'B'), BRt = Moves.makeMove(BR, moves.substring(i, (i + 4)), 'R'),
                        BQt = Moves.makeMove(BQ, moves.substring(i, (i + 4)), 'Q'), BKt = Moves.makeMove(BK, moves.substring(i, (i + 4)), 'K'),
                        EN_PASSANTt = Moves.makeMoveTwoStep(WP | BP, moves.substring(i, (i + 4)));
                boolean CQSWt = CQSW, CKSWt = CKSW, CQSBt = CQSB, CKSBt = CKSB;
                if (Character.isDigit(moves.charAt(3))) {
                    int start = (Character.getNumericValue(moves.charAt(i))) + (Character.getNumericValue(moves.charAt(i + 1)) * 8); // x1y1 index
                    if (((1L << start) & WK) != 0) {CQSWt = false; CKSWt = false;}
                    if (((1L << start) & BK) != 0) {CQSBt = false; CKSBt = false;}
                    if (((1L << start) & WR & (1L << 63)) != 0) {CKSWt = false;}
                    if (((1L << start) & WR & (1L << 56)) != 0) {CQSWt = false;}
                    if (((1L << start) & BR & (1L << 7)) != 0) {CKSBt = false;}
                    if (((1L << start) & BR & 1L) != 0) {CQSBt = false;}
                }
                if (((WK & Moves.inSightsBlack(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && WHITE_TURN) || ((BK & Moves.inSightsWhite(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0) & !WHITE_TURN) {
                    if (depth +1 == perftMaxDepth) {perftMoveCounter++;}
                    perft(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EN_PASSANT, CQSW, CKSW, CQSB, CKSB, !WHITE_TURN, depth++);
                }
            }
        }
    }
}
