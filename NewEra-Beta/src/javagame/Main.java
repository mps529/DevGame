package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {

    public static final String gameName = "New Era Beta";
    public static final int mainMenu = 0;
    public static final int game = 1;
    public static final int inventoryScreen = 2;
    public static final int playerScreen = 3;
    public static final int lootScreen = 4;
    public static final int merchantMenu = 5;

    public Main( String name ) {
        super( name );

        this.addState(new MainMenu(mainMenu));
        this.addState( new Game( game ) );
        this.addState( new InventoryScreen( inventoryScreen) );
        this.addState( new PlayerScreen( playerScreen ) );
        this.addState( new LootMenu( lootScreen ) );
        this.addState( new Merchant( merchantMenu ));
    }

    public void initStatesList( GameContainer gc ) throws SlickException {
            // Inits the two screens
        this.getState( game ).init( gc, this );
        this.getState( inventoryScreen ).init( gc, this );
        this.getState( playerScreen ).init( gc, this );
        this.getState( lootScreen ).init( gc, this );
        this.getState( merchantMenu ).init( gc, this );
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
            appgc.setShowFPS(false);
            appgc.setSmoothDeltas( true );
            appgc.start();
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }
    }

}
