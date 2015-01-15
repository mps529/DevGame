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

    private Map map;

    private boolean attacking = false;

    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        hunterTom = new Player( "HunterTom.png", "Tom" );
        hunterTom.setPlayerY(11);
        hunterTom.setPlayerX(10);

        map = new Map( "LargeMapGrasslands.tmx" );

        halfScreenHeight = gc.getHeight()/2;
        halfScreenWidth = gc.getWidth()/2;
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        map.drawMap();

        if( attacking ) {
            hunterTom.drawPlayerAttacking( halfScreenWidth, halfScreenHeight );
        }
        else {
            hunterTom.drawPlayer( halfScreenWidth, halfScreenHeight );
        }
        g.drawString("X: " + hunterTom.getPlayerX() + ", Y: " + hunterTom.getPlayerY(), 500, 100);

    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        if( !attacking ) {
            if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(0);
                if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() - 1) == 0) {
                    hunterTom.decrementPlayerY();
                    map.incrementMapCoordY();
                    map.updateMapSkewAndCoords();
                } else if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() - 1) == 2) {
                    System.out.println("New Map Time!!");
                }
            } else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(1);
                if (map.isSpaceTaken(hunterTom.getPlayerX() + 1, hunterTom.getPlayerY()) == 0) {
                    hunterTom.incrementPlayerX();
                    map.decrementMapCoordX();
                    map.updateMapSkewAndCoords();
                } else if (map.isSpaceTaken(hunterTom.getPlayerX() + 1, hunterTom.getPlayerY()) == 2) {
                    System.out.println("New Map Time!!");
                }
            } else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(2);
                if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() + 1) == 0) {
                    hunterTom.incrementPlayerY();
                    map.decrementMapCoordY();
                    map.updateMapSkewAndCoords();
                } else if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() + 1) == 2) {
                    System.out.println("New Map Time!!");
                }

            } else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(3);
                if (map.isSpaceTaken(hunterTom.getPlayerX() - 1, hunterTom.getPlayerY()) == 0) {
                    hunterTom.decrementPlayerX();
                    map.incrementMapCoordX();
                    map.updateMapSkewAndCoords();
                } else if (map.isSpaceTaken(hunterTom.getPlayerX() - 1, hunterTom.getPlayerY()) == 2) {
                    System.out.println("New Map Time!!");
                }
            } else {
                hunterTom.stopAnimationWalking();
            }

            if ((input.isKeyDown(Input.KEY_SPACE))) {
                hunterTom.startAnimationAttacking();
                attacking = true;
            } else {
                hunterTom.stopAnimationAttacking();
            }

        }

        if( hunterTom.isStopped() ) {
            attacking = false;
        }

    }

    public int getID( ) {
        return gameState;
    }
}
