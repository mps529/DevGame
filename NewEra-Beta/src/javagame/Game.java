package javagame;

import org.lwjgl.Sys;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Vector;

public class Game extends BasicGameState {

        // Game State
    private static int gameState;
        
        // Player Class
    private PlayerClass player;

    private Vector<Map> maps;

        // Is Attacking animation playing
    private boolean attacking = false;
        // Display fps and coords
    private boolean showInfo = false;
        // if player is DED
    private boolean dead = false;
        // Starting of death animaiton
    private boolean startDed = false;
        // Current map
    private int currentMap;
        // Temp map name to see if there needs to be a map change
    private String mapName;

    
        // Is player Running
    private boolean running = false;
        // Screen sizes cut in half for character rendering
    int halfScreenWidth, halfScreenHeight;

    public Game( int state ) {
        this.gameState = state;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        //this.player = this.player.createInstance( "hunter.png", "Tom" , 0 );
        //this.player = this.player.createInstance( "warrior.png", "Tom", 1 );
        //this.player = this.player.createInstance( "wizard.png", "Tom" , 2 );
        //this.player = this.player.createInstance( "rouge.png", "Tom" , 3 );

        this.player = this.player.getInstance();

            // Starting tile
        this.player.setPlayerX(94);
        this.player.setPlayerY(96);


        maps = new Vector<Map>();

            // Maps **Starting tiles for map are 10 under
            // PlayerX and PlayerY then negative
        maps.addElement( new Map( "LargeMapGrasslands.tmx", -84, -86 )  );
        maps.addElement( new Map( "devGrasslandsDungeon.tmx", -15, -38 )  );
        maps.addElement( new Map( "devGrasslandsHome.tmx", 0, -8 )  );

            // Setting starting map
        for( int x = 0; x < maps.size(); x++) {
            if( maps.elementAt(x).getMapName() == "LargeMapGrasslands.tmx" ) {
                currentMap = x;
                break;
            }
        }
            // Getting the screen Sizes
        this.halfScreenHeight = gc.getHeight()/2;
        this.halfScreenWidth = gc.getWidth()/2;
     }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
            // render current map
        this.maps.elementAt(this.currentMap).drawMap();

            // Switches to attack animation if true
        if( this.attacking ) {
            this.player.drawPlayerAttacking(this.halfScreenWidth, this.halfScreenHeight);
        }
            // If dead
        else if( this.dead ) {
            this.player.drawPlayerDieing( this.halfScreenWidth, this.halfScreenHeight );
        }
        else {
            this.player.drawPlayer( this.halfScreenWidth, this.halfScreenHeight );
        }

            // Render the layer the player will walk under
        this.maps.elementAt(this.currentMap).drawMapAbove();

            // Drawing arrows if he has them
        this.player.renderProjectile( gc, g );
            // Drawing Health/Stamina etc.
        this.player.drawPlayerInfo( g );

            // Debugging information
        if( this.showInfo ) {
            g.setColor( Color.white );
            g.drawString("X: " + this.player.getPlayerX() + ", Y: " + this.player.getPlayerY(), 300, 10);
            g.drawString("X: " + this.maps.elementAt(this.currentMap).getMapCoordX() + ", Y: " +  this.maps.elementAt(this.currentMap).getMapCoordY() , 300, 30);
            g.drawString("Running: " + this.running , 300, 50);
        }
            // FPS show, also for debugging
        gc.setShowFPS( this.showInfo );
    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        // Checks if the plaer is dead
        dead = player.checkDeath();

        // If player is ded
        if (!dead) {
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
            if (!attacking) {
                if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
                    this.player.startAnimationWalking();
                    this.player.setPlayerDirection(0);
                    if (this.maps.elementAt(this.currentMap).isSpaceTaken((int) this.player.getPlayerX(), (int) this.player.getPlayerY() - 12) == 0) {
                        this.player.decrementPlayerY();
                        this.maps.elementAt(this.currentMap).incrementMapCoordY();
                            // Checking for map switch
                        this.mapName = this.maps.elementAt(this.currentMap).isOnObjectLayer((int) this.player.getPlayerX(), (int) this.player.getPlayerY());
                        if (this.mapName != null) {
                            changeMap(this.mapName);
                        }
                        if (this.running) {
                            this.player.decreaseStamina(delta * .003f);
                            movedWhileRunning = true;
                        }
                    }
                } else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                    this.player.startAnimationWalking();
                    this.player.setPlayerDirection(1);
                    if (this.maps.elementAt(this.currentMap).isSpaceTaken(this.player.getPlayerX() + 12, this.player.getPlayerY()) == 0) {
                        this.player.incrementPlayerX();
                        this.maps.elementAt(this.currentMap).decrementMapCoordX();
                            // Checking for map switch
                        this.mapName = this.maps.elementAt(this.currentMap).isOnObjectLayer((int) this.player.getPlayerX(), (int) this.player.getPlayerY());
                        if (this.mapName != null) {
                            changeMap(this.mapName);
                        }

                        if (this.running) {
                            this.player.decreaseStamina(delta * .003f);
                            movedWhileRunning = true;
                        }
                    }

                } else if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
                    this.player.startAnimationWalking();
                    this.player.setPlayerDirection(2);
                    if (this.maps.elementAt(this.currentMap).isSpaceTaken(this.player.getPlayerX(), this.player.getPlayerY() + 24) == 0) {
                        this.player.incrementPlayerY();
                        this.maps.elementAt(this.currentMap).decrementMapCoordY();
                            // Checking for map switch
                        this.mapName = this.maps.elementAt(this.currentMap).isOnObjectLayer((int) this.player.getPlayerX(), (int) this.player.getPlayerY());
                        if (this.mapName != null) {
                            changeMap(this.mapName);
                        }
                        if (this.running) {
                            this.player.decreaseStamina(delta * .003f);
                            movedWhileRunning = true;
                        }
                    }
                } else if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                    this.player.startAnimationWalking();
                    this.player.setPlayerDirection(3);
                    if (this.maps.elementAt(this.currentMap).isSpaceTaken(this.player.getPlayerX() - 12, this.player.getPlayerY()) == 0) {
                        this.player.decrementPlayerX();
                        this.maps.elementAt(this.currentMap).incrementMapCoordX();
                            // Checking for map switch
                        this.mapName = this.maps.elementAt(this.currentMap).isOnObjectLayer((int) this.player.getPlayerX(), (int) this.player.getPlayerY());
                        if (this.mapName != null) {
                            changeMap(this.mapName);
                        }
                        if (running) {
                            this.player.decreaseStamina(delta * .003f);
                            movedWhileRunning = true;
                        }
                    }
                }
                // If the player did not move, stop playing walking animation
                else {
                    this.player.stopAnimationWalking();
                }

                if( input.isKeyDown( Input.KEY_1 ) ) {
                    if( this.player.isMoveKnown( 0 ) ) {
                        this.player.setMoveSelected( 0 );
                    }
                }
                if( input.isKeyDown( Input.KEY_2 ) ) {
                    if( this.player.isMoveKnown( 1 ) ) {
                        this.player.setMoveSelected( 1 );
                    }
                }
                if( input.isKeyDown( Input.KEY_3 ) ) {
                    if( this.player.isMoveKnown( 2 ) ) {
                        this.player.setMoveSelected( 2 );
                    }
                }
                if( input.isKeyDown( Input.KEY_4 ) ) {
                    if( this.player.isMoveKnown( 3 ) ) {
                        this.player.setMoveSelected( 3 );
                    }
                }
                if( input.isKeyDown( Input.KEY_V ) ) {
                    this.player.increaseExp( 10 );
                }

                // If player attacks and the player has stamina to attack
                if (input.isKeyDown(Input.KEY_SPACE) && this.player.getStamina() >= this.player.getAttackStamina() && player.isWeaponEqiupped()) {
                    this.player.startAnimationAttacking();
                    this.attacking = true;
                    this.player.decreaseStamina();
                } else {
                    this.player.stopAnimationAttacking();
                }

                // If the player moves and did not run, he gains stamina
                if (!movedWhileRunning) {
                    this.player.increaseStamina(delta * .003f);
                }
            } // End of not attacking

            // Enter Inventory
            if (input.isKeyPressed(Input.KEY_I)) {
                input.clearKeyPressedRecord();
                sbg.enterState( 2 );
            }
            // Enter Player Screen
            if( input.isKeyPressed( Input.KEY_P ) ) {
                input.clearKeyPressedRecord();
                sbg.enterState( 3 );
            }

            // Start Running
            if (input.isKeyPressed(Input.KEY_LSHIFT) && this.player.getStamina() > this.player.getMinRunningStamina()) {
                this.running = !this.running;
            }

            // If player has stamina to run
            if (this.player.getStamina() <= this.player.getMinRunningStamina()) {
                this.running = false;
            }

            // if running is true, speed up player animation
            if (this.running) {
                this.player.isRunning();
                this.maps.elementAt(this.currentMap).isRunning();
            }
            // Slow it down
            else {
                this.player.isNotRunning();
                this.maps.elementAt(this.currentMap).isNotRunning();
            }

            // This is when the players animation has stopped and he began to fire
            if (player.isStopped() && attacking) {
                this.attacking = false;
                attacked = true;
            }

            // Bring up debugging
            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                this.showInfo = !showInfo;
            }

            // If he is not in combat increase health
            if (!player.getInCombat()) {
                this.player.increaseHealth(delta * .003f);
            }
            // Update projectiles position
            this.player.updateAttack(delta, attacked, maps.elementAt(this.currentMap));
        } else {
            if (!this.startDed) {
                this.player.startAnimationDeath();
                this.player.stopAnimationDeath();
                this.startDed = true;
            } else if (this.player.isStoppedDead()) {
                // Switch State to main menu =)
            }
        }
    }


    public void changeMap( String newName ) {
            // Checking all maps for similar name
        for( int x = 0; x < this.maps.size(); x++ ) {
            if( this.maps.elementAt( x ).getMapName().equals(newName) ) {
                    // Setting new coords
                player.setPlayerXinPixels( Math.abs( this.maps.elementAt(this.currentMap).getObjectX()*32 )+320 );
                player.setPlayerYinPixels(Math.abs(this.maps.elementAt(this.currentMap).getObjectY()*32 )+320 );
                this.maps.elementAt( x ).setMapCoordX( this.maps.elementAt(this.currentMap).getObjectX() );
                this.maps.elementAt( x ).setMapCoordY( this.maps.elementAt(this.currentMap).getObjectY() );
                // Setting the current map
                this.currentMap = x;

                return;
            }
        }
    }

    public int getID( ) {
        return gameState;
    }
}