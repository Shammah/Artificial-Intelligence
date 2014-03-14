package nl.tue.s2id90.group43;

import nl.tue.s2id90.group43.players.UninformedPlayer;
import nl.tue.s2id90.group43.players.OptimisticPlayer;
import nl.tue.s2id90.draughts.DraughtsPlugin;
import nl.tue.s2id90.group43.players.Group43;
import nl.tue.s2id90.group43.players.CCClusterPlayer;
import nl.tue.s2id90.group43.players.CountCrownPlayer;
import nl.tue.s2id90.group43.players.SimpleCountPlayer;

/**
 *
 * @author huub
 */
public class MyDraughtsPlugin extends DraughtsPlugin {
    public MyDraughtsPlugin() {
        // make two players available to the AICompetition tool
        // During the final competition you should make only your 
        // best player available. For testing it might be handy
        // to make more than one player available.
        super(  new Group43() );
    }
}