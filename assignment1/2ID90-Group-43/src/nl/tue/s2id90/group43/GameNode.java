package nl.tue.s2id90.group43;

import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * A GameNode is a node that represents a game-state, together with the best move
 * the player can do in that state, with its respective heuristic value.
 */
public class GameNode {
    
    private DraughtsState _gameState;
    private Move _bestMove;
    private int _value;
    
    /**
     * Constructor.
     * @param state The game-state of the node.
     */
    public GameNode(DraughtsState state) {
        _gameState = state;
    }
    
    /**
     * 
     * @return The game state of the node.
     */
    public DraughtsState getGameState() {
        return _gameState;
    }
    
    /**
     * 
     * @return The best move found for the game state of this node.
     */
    public Move getBestMove() {
        return _bestMove;
    }
    
    /**
     * Sets the new best move of the game-state.
     * @param moveThe new best move.
     */
    public void setBestMove(Move move) {
        _bestMove = move;
    }
    
    /**
     * 
     * @return The heuristic value of the best move found so far.
     */
    public int getValue() {
        return _value;
    }

    /**
     * Sets the heuristic value of the best move found for the game-state.
     * @param value The value of the best move.
     */
    public void setValue(int value) {
        _value = value;
    }
}
