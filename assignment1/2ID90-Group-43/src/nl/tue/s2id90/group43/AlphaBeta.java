package nl.tue.s2id90.group43;

import java.util.ArrayList;
import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.group43.heuristics.IHeuristic;
import org10x10.dam.game.Move;

/**
 *
 * This class will hold all functions concerning the Alpha-Beta algorithm.
 * 
 * It holds an instance that represents a heuristic in order to determine the
 * 'value' or 'score' of a particular game-state. Additionally, you can stop 
 * the algorithm from running. By doing so, the algorithm will throw an
 * AIStoppedException if it has not stopped yet.
 * 
 * It should also be noted that the algorithm should not be ran without a
 * heuristic. A heuristic is not required upon class instantiation, but that is
 * only so that a heuristic can be picked later on before running the algorithm.
 * Running the algorithm without a heuristic will throw a NullPointerException.
 */
public class AlphaBeta {
    
    private IHeuristic              _heuristic;
    private boolean                 _stopped;
    private boolean                 _isRunning;
    
    /**
     * Constructor.
     */
    public AlphaBeta() {
        _heuristic      = null;
        _stopped        = false;
    }
    
    /**
     * Constructor.
     * @param heuristic The heuristic evaluator for game-states.
     */
    public AlphaBeta(IHeuristic heuristic) {
        _heuristic      = heuristic;
        _stopped        = false;
    }

    /**
     * 
     * @return The currently used heuristic to evaluate game-states.
     */
    public IHeuristic getHeuristic() {
        return _heuristic;
    }

    /**
     * Sets the heuristic to be used to evaluate game-states.
     * 
     * This can be null, but this is not recommended. By doing so, alphaBeta()
     * will give a NullPointerException when invoked.
     * 
     * @param heuristic The heuristic to be used.
     */
    public void setHeuristic(IHeuristic heuristic) {
        _heuristic = heuristic;
    }
    
    /**
     * Stops the algorithm from running if it is running.
     */
    public void stop() {
        if (_isRunning) {
            _stopped = true;
        }
    }
    
    /**
     * Runs the alpha-beta algorithm on a particular GameNode.
     * 
     * @param node The GameNode to evaluate the best move for.
     * @param a The best score for MAX found so far.
     * @param b The best score for MIN found so far.
     * @param d The (relative) depth at which nodes get evaluated.
     * @param max Whether the player is MAX.
     * @return The score of the best move found.
     * @throws AIStoppedException 
     */
    public int alphaBeta(GameNode node, int a, int b, int d, boolean max)
            throws AIStoppedException {
        _isRunning              = true;
        DraughtsState state     = node.getGameState();
        
        // Evaluate the node with the heuristic if the bottom of the game-state
        // tree has been reached.
        if (d == 0 || state.isEndState()) {
            return getHeuristic().evaluate(node.getGameState());
        }
 
        // Fetch all possible moves from this state and set the best move to the
        // first possible move.
        List<Move> moves        = new ArrayList<>(state.getMoves());
        Move bestMove           = moves.get(0);
        
        for (Move move : moves) {
            
            // The stop button has been pressed, thus we have to get out of the recursion.
            if (_stopped) {
                _isRunning = false;
                _stopped   = false;               
                throw new AIStoppedException();
            }
            
            // Apply the move, get its state and then find the best move for that state.
            // Then undo it.
            state.doMove(move);
            GameNode newNode    = new GameNode(state);
            int childValue      = alphaBeta(newNode, a, b, d - 1, !max);
            state.undoMove(move);
            
            // I am max.
            if (max) {
                if (childValue > a) {
                    a           = childValue;
                    bestMove    = move;
                }
            }
            
            // I am min.
            else {
                if (childValue < b) {
                    b           = childValue;
                    bestMove    = move;
                }
            }
            
            if (a >= b) {
                return (max) ? b : a;
            }
        }
        
        // Update the best move found for the GameNode where this algorithm
        // was applied for.
        node.setBestMove(bestMove);
        
        if (max) {
            node.setValue(a);
            return a;
        } else {
            node.setValue(b);
            return b;
        }
    }
}
