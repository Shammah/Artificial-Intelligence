package nl.tue.s2id90.group43.heuristics;

/**
 * A heuristic is a class that has the functionality of evaluating a game-state.
 */
public abstract class Heuristic implements IHeuristic {
    protected boolean _isWhite;
    
    /**
     * Constructor.
     */
    public Heuristic() {
        _isWhite = true;
    }
    
    /**
     * Constructor. 
     * @param isWhite Whether the player that uses this heuristic is white.
     */
    public Heuristic(boolean isWhite) {
        _isWhite = isWhite;
    }
    
    /**
     * Sets whether the player is white or not.
     * @param isWhite  whether the player is white or not.
     */
    public void setWhite(boolean isWhite) {
        _isWhite = isWhite;
    }
    
    /**
     * 
     * @return Whether it is set if the player is white or not.
     */
    public boolean getWhite() {
        return _isWhite;
    }
}
