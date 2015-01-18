package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class InventoryScreen extends BasicGameState{

    Inventory playerInventory;

    private TiledMap inventoryMap;

    private static int gameState;

    public InventoryScreen( int gameState ) {
        this.gameState = gameState;
    }


    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        playerInventory = playerInventory.getPlayerInvintory();
        inventoryMap = new TiledMap( "NewEra-Beta/res/map/Inventory.tmx" );

    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        inventoryMap.render( 0,0 );


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
