package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

// 94, 96

public class Game extends BasicGameState {
        // Game State
    private static int gameState;
        // Player Class\
    private Player hunterTom;

    private TiledMap map;
    int mapCoordX = 0, mapCoordY = 0;
    int mapSkewX = 0, mapSkewY = 0;

    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        hunterTom = new Player( "HunterTom.png", "Tom" );

       // LargeMapGrasslands.tmx

        halfScreenHeight = gc.getHeight()/2;
        halfScreenWidth = gc.getWidth()/2;
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );

        hunterTom.drawPlayer( halfScreenWidth, halfScreenHeight );

    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 0 );
            hunterTom.incrementPlayerY();
            mapCoordY++;
        }
        else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 1 );
            hunterTom.incrementPlayerX();
            mapCoordX--;
        }
        else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 2 );
            hunterTom.decrementPlayerY();
            mapCoordY--;
        }
        else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
            hunterTom.startAnimation();
            hunterTom.setPlayerDirection( 3 );
            hunterTom.decrementPlayerX();
            mapCoordX++;
        }
        else {
            hunterTom.stopAnimation();
        }

        if( mapCoordX < 0 ) {
            mapCoordX = 1;
            mapSkewX++;
        }
        if( mapCoordX > 1 ) {
            mapCoordX = 0;
            mapSkewX--;
        }
        if( mapCoordY < 0 ) {
            mapCoordY = 1;
            mapSkewY++;
        }
        if( mapCoordY > 1 ) {
            mapCoordY = 0;
            mapSkewY--;
        }


    }

    public int getID( ) {
        return gameState;
    }
}
