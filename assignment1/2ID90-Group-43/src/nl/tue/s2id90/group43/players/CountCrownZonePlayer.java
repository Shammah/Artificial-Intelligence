package nl.tue.s2id90.group43.players;

import nl.tue.s2id90.group43.heuristics.CountCrownZoneHeuristic;

public class CountCrownZonePlayer extends AlphaBetaPlayer {

    public CountCrownZonePlayer() {
        super(new CountCrownZoneHeuristic());
    }
}
