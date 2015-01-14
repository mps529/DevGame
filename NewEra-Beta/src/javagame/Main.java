package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {

    public static final String gameName = "New Era Beta";
    public static final int game = 1;

    public Main( String name ) {
        super( name );

        this.addState( new Game( game ) );
    }

    public void initStatesList( GameContainer gc ) throws SlickException {
        // Inits the two screens
        this.getState( game ).init( gc, this );
        // Sets menu as start screen
        this.enterState( game );
    }

    public static void main( String[] args ) {
        AppGameContainer appgc;
        try{
            appgc = new AppGameContainer( new Main( gameName ) );
            appgc.setDisplayMode( 640, 640, false );
            // appgc.setShowFPS( false );
            appgc.start();
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }
    }

}
