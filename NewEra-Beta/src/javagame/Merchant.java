package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by mattslavin on 4/9/15.
 */
public class Merchant extends BasicGameState {

    private static int gameState;

    public Merchant( int state ) {
        this.gameState = state;
    }

    public int getID( ) {
        return gameState;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = gc.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();



    }
}
