package nl.tue.s2id90.group43.heuristics;

import nl.tue.s2id90.draughts.DraughtsState;

/**
 * A heuristic is a class that has the functionality of evaluating a game-state.
 */
public interface IHeuristic {
    
    /**
     * Evaluates the heuristic for a given game-state.
     * @param state The game-state that has to be evaluated.
     * @return The score that the heuristic has assigned to the given state.
     */
    public int evaluate(DraughtsState state);
}
