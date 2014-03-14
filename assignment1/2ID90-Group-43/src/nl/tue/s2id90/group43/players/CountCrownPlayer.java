package nl.tue.s2id90.group43.players;

import nl.tue.s2id90.group43.heuristics.CountCrownHeuristic;

public class CountCrownPlayer extends AlphaBetaPlayer {

    public CountCrownPlayer() {
        super(new CountCrownHeuristic());
    }
}
