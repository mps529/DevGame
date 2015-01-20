package javagame;


import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends BasicGameState {

        // Game State
    private static int gameState;
        // Player Class
    private Hunter hunterTom;
        // Map Class
    private Map map;
        // Is Attacking animation playing
    private boolean attacking = false;
        // Display fps and coords
    private boolean showInfo = false;

        // Is player Running
    private boolean running = false;
        // Screen sizes cut in half for character rendering
    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        this.gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        this.hunterTom = new Hunter( "HunterTom.png", "Tom" );

            // Starting tile
        this.hunterTom.setPlayerX( 94 );
        this.hunterTom.setPlayerY( 96 );

            // Map **Starting tiles for map are 10 under
            // PlayerX and PlayerY then negative
        this.map = new Map( "LargeMapGrasslands.tmx", -84, -86 );

            // Getting the screen Sizes
        this.halfScreenHeight = gc.getHeight()/2;
        this.halfScreenWidth = gc.getWidth()/2;
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        this.map.drawMap();

            // Switches to attack animation if true
        if( this.attacking ) {
            this.hunterTom.drawPlayerAttacking( this.halfScreenWidth, this.halfScreenHeight );
        }
        else {
            this.hunterTom.drawPlayer( this.halfScreenWidth, this.halfScreenHeight );
        }

            // Drawing arrows if he has them
        this.hunterTom.renderProjectile( gc, g );
            // Drawing Health/Stamina etc.
        this.hunterTom.drawPlayerInfo( g );

            // Debugging information
        if( this.showInfo ) {
            g.setColor( Color.white );
            g.drawString("X: " + this.hunterTom.getPlayerX() + ", Y: " + this.hunterTom.getPlayerY(), 300, 10);
            g.drawString("X: " + this.map.getMapCoordX() + ", Y: " +  this.map.getMapCoordY() , 300, 30);
            g.drawString("Running: " + this.running , 300, 50);
        }
            // FPS show, also for debugging
        gc.setShowFPS( this.showInfo );
    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        // Did player attack
        boolean attacked = false;

            // Checking if the player has moved with running, will change if the player does
        boolean movedWhileRunning = false;
            /*
                This block of code does roughly the following for each key detection
                  1) Starts the animation, and puts the player facing the correct direction
                  2) Checks the next tile
                  3) If space is not a collision move the hunter and the map
                  4) If the player is running, decrease stamina.
            */
        if( !attacking ) {
            if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
                this.hunterTom.startAnimationWalking();
                this.hunterTom.setPlayerDirection(0);
                if (this.map.isSpaceTaken((int) this.hunterTom.getPlayerX(), (int) this.hunterTom.getPlayerY() - 12) == 0) {
                    this.hunterTom.decrementPlayerY();
                    this.map.incrementMapCoordY();
                    if( this.running) {
                        this.hunterTom.decreaseStamina( delta*.003f );
                        movedWhileRunning = true;
                    }
                }
            } else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                this.hunterTom.startAnimationWalking();
                this.hunterTom.setPlayerDirection(1);
                if (this.map.isSpaceTaken(this.hunterTom.getPlayerX() + 12, this.hunterTom.getPlayerY()) == 0) {
                    this.hunterTom.incrementPlayerX();
                    this.map.decrementMapCoordX();
                    if( this.running ) {
                        this.hunterTom.decreaseStamina( delta*.003f );
                        movedWhileRunning = true;
                    }
                }

            } else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
                this.hunterTom.startAnimationWalking();
                this.hunterTom.setPlayerDirection(2);
                if (this.map.isSpaceTaken(this.hunterTom.getPlayerX(), this.hunterTom.getPlayerY() + 24) == 0) {
                    this.hunterTom.incrementPlayerY();
                    this.map.decrementMapCoordY();
                    if( this.running) {
                        this.hunterTom.decreaseStamina( delta*.003f );
                        movedWhileRunning = true;
                    }
                }
            } else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                this.hunterTom.startAnimationWalking();
                this.hunterTom.setPlayerDirection(3);
                if (this.map.isSpaceTaken(this.hunterTom.getPlayerX() - 12,this.hunterTom.getPlayerY()) == 0) {
                    this.hunterTom.decrementPlayerX();
                    this.map.incrementMapCoordX();
                    if( running) {
                        this.hunterTom.decreaseStamina( delta*.003f );
                        movedWhileRunning = true;
                    }
                }
            }
                // If the player did not move, stop playing walking animation
            else {
                this.hunterTom.stopAnimationWalking();
            }

                // If player attacks and the player has stamina to attack
            if ( input.isKeyDown(Input.KEY_SPACE) && this.hunterTom.getStamina() > this.hunterTom.getMoveSelected() ) {
                this.hunterTom.startAnimationAttacking();
                this.attacking = true;
                this.hunterTom.decreaseStamina();
            } else {
                this.hunterTom.stopAnimationAttacking();
            }

                // If the player moves and did not run, he gains stanima
            if( !movedWhileRunning ) {
                this.hunterTom.increaseStamina(delta * .003f);
            }
        } // End of not attacking

            // Enter Inventory
        if( input.isKeyPressed( Input.KEY_I ) ) {
            sbg.enterState( 2 );
        }

            // Start Running
        if( input.isKeyPressed( Input.KEY_LSHIFT ) && this.hunterTom.getStamina() > this.hunterTom.getMinRunningStamina() ) {
            this.running = !this.running;
        }

            // If player has stamina to run
        if(  this.hunterTom.getStamina() <= this.hunterTom.getMinRunningStamina() ) {   // ****************( Need to add variable to get running stamina min  )
            this.running = false;
        }

            // if rinnign is true, speed up player animation
        if( this.running ) {
            this.hunterTom.isRunning();
            this.map.isRunning();
        }
            // Slow it down
        else {
            this.hunterTom.isNotRunning();
            this.map.isNotRunning();
        }

            // This is when the players animation has stopped and he began to fire
        if( hunterTom.isStopped() && attacking ) {
            this.attacking = false;
            attacked = true;
        }

            // Bring up debugging
        if( input.isKeyPressed(Input.KEY_ESCAPE) ) {
            this.showInfo = !showInfo;
        }

            // If he is not in combat increase health
        if( !hunterTom.getInCombat() ) {
            this.hunterTom.increaseHealth( delta*.003f );
        }

            // Update projectiles position
        this.hunterTom.updateAttack(delta, attacked, map);
    }

    public int getID( ) {
        return gameState;
    }
}