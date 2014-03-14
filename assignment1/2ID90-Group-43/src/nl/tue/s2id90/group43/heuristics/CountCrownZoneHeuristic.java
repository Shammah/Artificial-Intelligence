package nl.tue.s2id90.group43.heuristics;

import java.util.ArrayList;
import nl.tue.s2id90.draughts.DraughtsState;

/**
 * Extends the CountCrownHeuristic with an additional score based on whether
 * pieces are on the sides of the board or on the middle.
 */
public class CountCrownZoneHeuristic extends CountCrownHeuristic {
    
    public CountCrownZoneHeuristic() {
        super();
    }
    
    public CountCrownZoneHeuristic(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public int evaluate(DraughtsState state) {
        return super.evaluate(state) + threeZones(state.getPieces());
    }

    public int threeZones(int[] pieces) {
        int sum = 0;

        // Side zones.
        ArrayList<Integer> sides = getZones(1);
        sides.addAll(getZones(3));
        int whitePieces = 0;
        int blackPieces = 0;
        for (int index : sides) {
            whitePieces += (pieces[index] == DraughtsState.WHITEPIECE) ? 1 : 0;
            blackPieces += (pieces[index] == DraughtsState.BLACKPIECE) ? 1 : 0;
        }

        if (_isWhite) {
            sum += 4 * (whitePieces - blackPieces);
        } else {
            sum += 4 * (blackPieces - whitePieces);
        }

        // Middle zone.
        whitePieces = 0;
        blackPieces = 0;
        for (int index : getZones(2)) {
            whitePieces += (pieces[index] == DraughtsState.WHITEPIECE) ? 1 : 0;
            blackPieces += (pieces[index] == DraughtsState.BLACKPIECE) ? 1 : 0;
        }

        if (_isWhite) {
            sum += 8 * (whitePieces - blackPieces);
        } else {
            sum += 8 * (blackPieces - whitePieces);
        }

        return sum;
    }

    /**
     * 1 = left 2 = middle 3 = right
     *
     * @param zone
     * @return
     */
    public ArrayList<Integer> getZones(int zone) {
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 1; i < 51; i++) {
            String intString = Integer.toString(i);
            char lastChar = intString.charAt(intString.length() - 1);

            if ((lastChar == '1' || lastChar == '6' || lastChar == '7') && zone == 1) {
                indices.add(i);
            } else if ((lastChar == '2' || lastChar == '8' || lastChar == '3' || lastChar == '9') && zone == 2) {
                indices.add(i);
            } else {
                indices.add(i);
            }
        }

        return indices;
    }
}
