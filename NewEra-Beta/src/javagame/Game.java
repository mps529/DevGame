package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

// 94, 96

public class Game extends BasicGameState {
        // Game State
    private static int gameState;
        // Player Class\
    private Player hunterTom;

    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        hunterTom = new Player( "HunterTom.png", "Tom" );

        halfScreenHeight = gc.getHeight()/2;
        halfScreenWidth = gc.getWidth()/2;
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        hunterTom.getMovingPlayer().draw( halfScreenWidth, halfScreenHeight );

    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 0 );
        }
        else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 1 );
        }
        else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 2 );
        }
        else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 3 );
        }
        else {
            hunterTom.stopAnimation();
        }

    }

    public int getID( ) {
        return gameState;
    }
}
