package nl.tue.s2id90.group43.heuristics;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * Extends the CountCrownHeuristic so that adds an additional score based on
 * the clustering of pieces.
 */
public class CCClusterHeuristic extends CountCrownHeuristic {
    
    public CCClusterHeuristic() {
        super();
    }
    
    public CCClusterHeuristic(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public int evaluate(DraughtsState state) {
        return super.evaluate(state) + clusterValue(state);
    }

    public int clusterValue(DraughtsState state) {
        int sum = 0;
        
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (state.getPiece(i, j) == DraughtsState.EMPTY ||
                    state.getPiece(i, j) == DraughtsState.WHITEFIELD ||
                    state.getPiece(i, j) == DraughtsState.WHITEKING ||
                    state.getPiece(i, j) == DraughtsState.BLACKKING) {
                    continue;
                }
                
                int multiplier = 0;
                
                if (_isWhite && state.getPiece(i, j) == DraughtsState.WHITEPIECE ||
                    !_isWhite && state.getPiece(i, j) == DraughtsState.BLACKPIECE) {
                    multiplier = 1;
                } else {
                     multiplier = -1;
                }
                         
                if (state.getPiece(i, j) == state.getPiece(i + 1, j) ||
                    state.getPiece(i, j) == state.getPiece(i - 1, j) ||
                    state.getPiece(i, j) == state.getPiece(i, j + 1) ||
                    state.getPiece(i, j) == state.getPiece(i, j - 1))
                {
                    sum += multiplier * 5;
                }
            }
        }
        
        return sum;
    }
}
