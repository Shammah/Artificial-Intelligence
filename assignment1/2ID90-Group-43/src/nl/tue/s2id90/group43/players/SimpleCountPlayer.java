package nl.tue.s2id90.group43.players;

import nl.tue.s2id90.group43.heuristics.SimpleCountHeuristic;

public class SimpleCountPlayer extends AlphaBetaPlayer {

    public SimpleCountPlayer() {
        super(new SimpleCountHeuristic());
    }
}
