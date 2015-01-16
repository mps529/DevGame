package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Vector;


// 94, 96

public class Game extends BasicGameState {
        // Game State
    private static int gameState;
        // Player Class
    private Player2 hunterTom;
        // Map Class
    private Map2 map;
        // Is Attacking animation playing
    private boolean attacking = false;
        // Diasplay fps and coords
    boolean showInfo = false;
        // Did player shoot
    boolean shot = false;


    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        hunterTom = new Player2( "HunterTom.png", "Tom" );

            // Starting tile
        hunterTom.setPlayerX( 94 );
        hunterTom.setPlayerY( 96 );

            // Map **Starting tiles for map are 10 under
            // PlayerX and PlayerY then negative
        map = new Map2( "LargeMapGrasslands.tmx", -84, -86 );

            // Getting the screen Sizes
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

        hunterTom.renderProjectile( gc, g );

        if( showInfo ) {
            g.setColor( Color.white );
            g.drawString("X: " + hunterTom.getPlayerX()/32 + ", Y: " + hunterTom.getPlayerY()/32, 300, 10);
            g.drawString("X: " + map.getMapCoordX()/32 + ", Y: " +  map.getMapCoordY()/32 , 300, 30);
        }
        gc.setShowFPS( showInfo );
    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        if( !attacking ) {
            if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(0);
                if (map.isSpaceTaken((int) hunterTom.getPlayerX(), (int) hunterTom.getPlayerY() - 12) == 0) {
                    hunterTom.decrementPlayerY(delta);
                    map.incrementMapCoordY();
                }
            } else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(1);
                if (map.isSpaceTaken(hunterTom.getPlayerX() + 12, hunterTom.getPlayerY()) == 0) {
                    hunterTom.incrementPlayerX(delta);
                    map.decrementMapCoordX();
                }

            } else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(2);
                if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() + 24) == 0) {
                    hunterTom.incrementPlayerY(delta);
                    map.decrementMapCoordY();
                }

            } else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(3);
                if (map.isSpaceTaken(hunterTom.getPlayerX() - 12, hunterTom.getPlayerY()) == 0) {
                    hunterTom.decrementPlayerX(delta);
                    map.incrementMapCoordX();
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

        if( hunterTom.isStopped() && attacking ) {
            attacking = false;
            shot = true;
        }

        if( input.isKeyPressed(Input.KEY_ESCAPE) ) {
            showInfo = !showInfo;
        }

        hunterTom.updateProjectile( delta, shot, map );
        shot = false;

    }

    public int getID( ) {
        return gameState;
    }
}













/*
if( !attacking ) {
            if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(0);
                if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() - 1) == 0) {
                    y -= delta*.009f;
                    if( y < -1 )  {
                        hunterTom.decrementPlayerY();
                        map.incrementMapCoordY();
                        map.updateMapSkewAndCoords();
                        y=0;
                    }

                } else if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() - 1) == 2) {
                    System.out.println("New Map Time!!");
                }
            } else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(1);
                if (map.isSpaceTaken(hunterTom.getPlayerX() + 1, hunterTom.getPlayerY()) == 0) {
                    x += delta*.009f;
                    if( x > 1 )  {
                        hunterTom.incrementPlayerX();
                        map.decrementMapCoordX();
                        map.updateMapSkewAndCoords();
                        x=0;
                    }
                } else if (map.isSpaceTaken(hunterTom.getPlayerX() + 1, hunterTom.getPlayerY()) == 2) {
                    System.out.println("New Map Time!!");
                }
            } else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(2);
                if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() + 1) == 0) {
                    y += delta*.009f;
                    if( y > 1 )  {
                        hunterTom.incrementPlayerY();
                        map.decrementMapCoordY();
                        map.updateMapSkewAndCoords();
                        y=0;
                    }
                } else if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() + 1) == 2) {
                    System.out.println("New Map Time!!");
                }

            } else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(3);
                if (map.isSpaceTaken(hunterTom.getPlayerX() - 1, hunterTom.getPlayerY()) == 0) {
                    x -= delta*.009f;
                    if( x < -1 )  {
                        hunterTom.decrementPlayerX();
                        map.incrementMapCoordX();
                        map.updateMapSkewAndCoords();
                        x=0;
                    }
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

        if( hunterTom.isStopped() && attacking ) {
            attacking = false;
            shot = true;
        }

        if( input.isKeyPressed(Input.KEY_ESCAPE) ) {
            showInfo = !showInfo;
        }

        hunterTom.updateProjectile( delta, shot );
        shot = false;

 */