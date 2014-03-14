package nl.tue.s2id90.group43.heuristics;

import java.util.Random;
import nl.tue.s2id90.draughts.DraughtsState;

/**
 * A class that gives states a random heuristic value.
 */
public class RandomHeuristic implements IHeuristic {

    @Override
    public int evaluate(DraughtsState state) {
        Random random = new Random();
        return random.nextInt();
    }

}
