package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InventoryScreen extends BasicGameState{

    Inventory playerInventory;

    private static int gameState;

    public InventoryScreen( int gameState ) {
        this.gameState = gameState;
    }


    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        playerInventory = playerInventory.getPlayerInvintory();

    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {



    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        if( input.isKeyPressed( Input.KEY_ESCAPE ) ) {
            sbg.enterState( 1 );
        }

    }

    public int getID( ) {
        return gameState;
    }

}
