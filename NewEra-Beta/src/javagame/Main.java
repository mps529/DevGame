package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {

    public static final String gameName = "New Era Beta";
    public static final int mainMenu = 0;
    public static final int game = 1;
    public static final int inventoryScreen = 2;

    public Main( String name ) {
        super( name );

        this.addState( new MainMenu( mainMenu ) );
        this.addState( new Game( game ) );
        this.addState( new InventoryScreen( inventoryScreen) );
    }

    public void initStatesList( GameContainer gc ) throws SlickException {
        // Inits the three screens
        this.getState( mainMenu ).init( gc, this );
        this.getState( game ).init( gc, this );
        this.getState( inventoryScreen ).init( gc, this );
        // Sets menu as start screen
        this.enterState( mainMenu );
    }

    public static void main( String[] args ) {
        AppGameContainer appgc;
        try{
            appgc = new AppGameContainer( new Main( gameName ) );
            appgc.setDisplayMode( 640, 640, false );
            appgc.setVSync( true );
            appgc.setTargetFrameRate( 60 );
            appgc.setSmoothDeltas( true );
            appgc.setShowFPS(false);
            appgc.start();
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }
    }

}
