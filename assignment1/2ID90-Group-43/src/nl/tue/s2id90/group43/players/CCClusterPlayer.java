package nl.tue.s2id90.group43.players;

import nl.tue.s2id90.group43.heuristics.CCClusterHeuristic;

public class CCClusterPlayer extends AlphaBetaPlayer {

    public CCClusterPlayer() {
        super(new CCClusterHeuristic());
    }
}
