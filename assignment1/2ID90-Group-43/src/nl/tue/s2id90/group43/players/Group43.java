package nl.tue.s2id90.group43.players;

import nl.tue.s2id90.group43.heuristics.CCCDepthHeuristic;

public class Group43 extends AlphaBetaPlayer {

    public Group43() {
        super(new CCCDepthHeuristic());
    }
}
