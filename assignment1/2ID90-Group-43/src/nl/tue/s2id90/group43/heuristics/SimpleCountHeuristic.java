package nl.tue.s2id90.group43.heuristics;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * A heuristic that bases the game-state value on the amount of
 * black and white pieces on the board.
 */
public class SimpleCountHeuristic extends Heuristic {

    public SimpleCountHeuristic() {
        super();
    }
    
    public SimpleCountHeuristic(boolean isWhite) {
        super(isWhite);
    }
    
    @Override
    public int evaluate(DraughtsState state) {
        return countPieces(state.getPieces());
    }
    
    public int countPieces(int[] pieces) {
        int sum = 0;

        for (int piece : pieces) {
            switch (piece) {
                case DraughtsState.WHITEPIECE:
                    sum += (_isWhite) ? 25 : -25;
                    break;
                case DraughtsState.BLACKPIECE:
                    sum += (_isWhite) ? -25 : 25;
                    break;
                case DraughtsState.WHITEKING:
                    sum += (_isWhite) ? 75 : -75;
                    break;
                case DraughtsState.BLACKKING:
                    sum += (_isWhite) ? -75 : 75;
            }
        }

        return sum;
    }

}
