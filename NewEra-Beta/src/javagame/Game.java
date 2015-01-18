package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;



// 94, 96

public class Game extends BasicGameState {
        // Game State
    private static int gameState;
        // Player Class
    private Hunter hunterTom;
        // Map Class
    private Map map;
        // Is Attacking animation playing
    private boolean attacking = false;
        // Diasplay fps and coords
    private boolean showInfo = false;
        // Did player shoot
    private boolean shot = false;

    private boolean running = false;


    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        hunterTom = new Hunter( "HunterTom.png", "Tom" );

            // Starting tile
        hunterTom.setPlayerX( 94 );
        hunterTom.setPlayerY( 96 );

            // Map **Starting tiles for map are 10 under
            // PlayerX and PlayerY then negative
        map = new Map( "LargeMapGrasslands.tmx", -84, -86 );

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


        hunterTom.drawPlayerInfo( g );


        if( showInfo ) {
            g.setColor( Color.white );
            g.drawString("X: " + hunterTom.getPlayerX()/32 + ", Y: " + hunterTom.getPlayerY()/32, 300, 10);
            g.drawString("X: " + map.getMapCoordX()/32 + ", Y: " +  map.getMapCoordY()/32 , 300, 30);
            g.drawString("Running: " + running , 300, 50);
        }
        gc.setShowFPS( showInfo );
    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        boolean moved = false;

        if( !attacking ) {
            if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(0);
                if (map.isSpaceTaken((int) hunterTom.getPlayerX(), (int) hunterTom.getPlayerY() - 12) == 0) {
                    hunterTom.decrementPlayerY(delta);
                    map.incrementMapCoordY();
                    if( running) {
                        hunterTom.decreaseStamina( delta*.003f );
                        moved = true;
                    }
                }
            } else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(1);
                if (map.isSpaceTaken(hunterTom.getPlayerX() + 12, hunterTom.getPlayerY()) == 0) {
                    hunterTom.incrementPlayerX(delta);
                    map.decrementMapCoordX();
                    if( running ) {
                        hunterTom.decreaseStamina( delta*.003f );
                        moved = true;
                    }
                }

            } else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(2);
                if (map.isSpaceTaken(hunterTom.getPlayerX(), hunterTom.getPlayerY() + 24) == 0) {
                    hunterTom.incrementPlayerY(delta);
                    map.decrementMapCoordY();
                    if( running) {
                        hunterTom.decreaseStamina( delta*.003f );
                        moved = true;
                    }
                }

            } else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                hunterTom.startAnimationWalking();
                hunterTom.setPlayerDirection(3);
                if (map.isSpaceTaken(hunterTom.getPlayerX() - 12, hunterTom.getPlayerY()) == 0) {
                    hunterTom.decrementPlayerX(delta);
                    map.incrementMapCoordX();
                    if( running) {
                        hunterTom.decreaseStamina( delta*.003f );
                        moved = true;
                    }
                }
            } else {
                hunterTom.stopAnimationWalking();

            }

            if ( input.isKeyDown(Input.KEY_SPACE) && hunterTom.getStamina() > 10 ) {
                hunterTom.startAnimationAttacking();
                attacking = true;
                hunterTom.decreaseStamina();
            } else {
                hunterTom.stopAnimationAttacking();
            }

            if( !moved ) {
                hunterTom.increaseStamina(delta * .003f);
            }

        }

        if( input.isKeyPressed( Input.KEY_F ) ) {
            sbg.enterState( 2 );
        }

        if( input.isKeyPressed( Input.KEY_LSHIFT ) && hunterTom.getStamina() > 10 ) {
            running = !running;
        }

        if(  hunterTom.getStamina() <= 10 ) {
            running = false;
        }

        if( running ) {
            hunterTom.isRunning();
            map.isRunning();
        }
        else {
            hunterTom.isNotRunning();
            map.isNotRunning();
        }

        if( hunterTom.isStopped() && attacking ) {
            attacking = false;
            shot = true;
        }

        if( input.isKeyPressed(Input.KEY_ESCAPE) ) {
            showInfo = !showInfo;
        }

        if( !hunterTom.getInCombat() ) {
            hunterTom.increaseHealth( delta*.003f );
        }

        hunterTom.updateProjectile( delta, shot, map );
        shot = false;

    }

    public int getID( ) {
        return gameState;
    }
}