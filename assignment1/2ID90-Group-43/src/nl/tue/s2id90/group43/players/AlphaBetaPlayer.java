package nl.tue.s2id90.group43.players;

import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.group43.AIStoppedException;
import nl.tue.s2id90.group43.AlphaBeta;
import nl.tue.s2id90.group43.GameNode;
import nl.tue.s2id90.group43.heuristics.Heuristic;
import nl.tue.s2id90.group43.heuristics.IHeuristic;
import org10x10.dam.game.Move;

/**
 * A draughts player that uses AlphaBeta pruning with a given heuristic.
 */
public abstract class AlphaBetaPlayer extends DraughtsPlayer {
    
    GameNode    _bestNode   = null;
    int         _value      = 0;
    AlphaBeta   _ab         = new AlphaBeta();

    public AlphaBetaPlayer(IHeuristic heuristic) {
        super(AlphaBetaPlayer.class.getResource("resources/ab.jpg"));
        _ab.setHeuristic(heuristic);
    }
    
    @Override
    public Move getMove(DraughtsState s) {
        GameNode node       = new GameNode(s);
        IHeuristic h        = _ab.getHeuristic();
        if (h instanceof Heuristic) {
            ((Heuristic) h).setWhite(s.isWhiteToMove());
        }
        
        try {
            // Use iterative deepening.
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                _ab.alphaBeta(node, Integer.MIN_VALUE, Integer.MAX_VALUE, i, true);
            }
        } catch (AIStoppedException ex) {
            
        }

        _bestNode = node;
        return node.getBestMove();
    }
    
    @Override
    public void stop() {
        _ab.stop();
    }
    
    /**
     * 
     * @return The best move the player can do since the last evaluation.
     */
    public Move getBestMove() {
        return _bestNode.getBestMove();
    }

    @Override
    public Integer getValue() {
        if (_bestNode == null) {
            return 0;
        }
        
        return _bestNode.getValue();
    }
}
