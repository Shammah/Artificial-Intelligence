package nl.tue.s2id90.group43.heuristics;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * Extends the CCCluster heuristic so that it adds an additional score based
 * on how 'deep' the pieces of the player are on the board.
 */
public class CCCDepthHeuristic extends CCClusterHeuristic {

    public CCCDepthHeuristic() {
        super();
    }
    
    public CCCDepthHeuristic(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public int evaluate(DraughtsState state) {
        return super.evaluate(state) + depthValue(state);
    }
    
    public int depthValue(DraughtsState state) {
        int sum         = 0;
        int whiteCount  = 0;
        int blackCount  = 0;
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (_isWhite && state.getPiece(i, j) == DraughtsState.WHITEPIECE ||
                    !_isWhite && state.getPiece(i, j) == DraughtsState.BLACKPIECE) {
                    sum += (_isWhite) ? 9 - i : i;
                }
                
                if (_isWhite) {
                    whiteCount++;
                } else {
                    blackCount++;
                }
            }
        }
        
        return sum;
    }
}
