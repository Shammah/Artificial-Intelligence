package nl.tue.s2id90.group43.heuristics;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * A heuristic that not only bases its score on the number of pieces on the board,
 * but also based on the presence of the so-called 'Crown Piece'.
 */
public class CountCrownHeuristic extends SimpleCountHeuristic {

    public CountCrownHeuristic() {
        super();
    }
    
    public CountCrownHeuristic(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public int evaluate(DraughtsState state) {
        return super.evaluate(state) + crownPiece(state.getPieces());
    }

    public int crownPiece(int[] pieces) {
        int sum = 0;

        if (_isWhite) {
            if (pieces[48] == DraughtsState.WHITEPIECE) {
                sum += 5;
            }

            if (pieces[3] == DraughtsState.BLACKPIECE) {
                sum -= 5;
            }
        } else {
            if (pieces[3] == DraughtsState.BLACKPIECE) {
                sum += 5;
            }

            if (pieces[48] == DraughtsState.WHITEPIECE) {
                sum -= 5;
            }
        }

        return sum;
    }
}
