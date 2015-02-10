package javagame;

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

    private PlayerAttack playerAttack;

        // Movement Class
    private Player player;

    //Save class
    private SaveGame save;

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


        this.player = this.player.getInstance();
        this.playerAttack = new PlayerAttack();

            // Starting tile
        this.player.setPlayerX(94);
        this.player.setPlayerY(96);
        this.save = this.save.getInstance();

        maps = new Vector<Map>();

            // Maps **Starting tiles for map are 10 under
            // PlayerX and PlayerY then negative
        maps.addElement( new Map( "LargeMapGrasslands.tmx", -84, -86 )  );
        maps.addElement( new Map( "devGrasslandsDungeon.tmx", -15, -38 )  );
        maps.addElement( new Map( "devGrasslandsHome.tmx", 0, -8 )  );
        maps.addElement( new Map( "house1.tmx", -3, -8 ) );



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

    public void enter( GameContainer gc, StateBasedGame sbg ) {
        this.playerAttack.setPlayerSpriteSheet( this.player.getPlayerSpriteSheet() );
        this.playerAttack.setAttackSprite();
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
            // render current map
        this.maps.elementAt(this.currentMap).drawMap();

        // Drawing traps if set
        this.player.renderTraps( gc, g );

            // Switches to attack animation if true
        if( this.playerAttack.getIsAttacking() ) {
            this.playerAttack.drawPlayerAttacking(this.halfScreenWidth, this.halfScreenHeight);
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

        // Checks if the player is dead
        dead = player.checkDeath();


        // If player is ded
        if (!dead) {
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
            if ( !this.playerAttack.getIsAttacking() || this.playerAttack.isSneaking() ) {

                // Enter Inventory
                if (input.isKeyPressed(Input.KEY_I)) {
                    input.clearKeyPressedRecord();
                    sbg.enterState(2);
                }
                // Enter Movement Screen
                else if (input.isKeyPressed(Input.KEY_P)) {
                    input.clearKeyPressedRecord();
                    sbg.enterState(3);
                } else if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
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

                // If player attacks and the player has stamina to attack
                if (input.isKeyPressed(Input.KEY_SPACE) && !this.playerAttack.isSneaking() && this.player.getStamina() >= this.player.getAttackStamina() && player.isWeaponEqiupped() ) {
                    this.playerAttack.attack();
                    this.playerAttack.startAnimationAttacking();
                    this.playerAttack.stopAnimationAttacking();
                }

                if (!this.playerAttack.isSneaking() ) {
                    if (input.isKeyDown(Input.KEY_1)) {
                        if (this.player.isMoveKnown(0)) {
                            this.player.setMoveSelected(0);
                        }
                    } else if (input.isKeyDown(Input.KEY_2)) {
                        if (this.player.isMoveKnown(1)) {
                            this.player.setMoveSelected(1);
                        }
                    } else if (input.isKeyDown(Input.KEY_3)) {
                        if (this.player.isMoveKnown(2)) {
                            this.player.setMoveSelected(2);
                        }
                    } else if (input.isKeyDown(Input.KEY_4)) {
                        if (this.player.isMoveKnown(3)) {
                            this.player.setMoveSelected(3);
                        }
                    }
                }

                if (input.isKeyPressed( Input.KEY_9 ) ) {
                    input.clearKeyPressedRecord();
                    this.player.usedHealthPotion();
                }
                else if( input.isKeyPressed( Input.KEY_0 ) ) {
                    input.clearKeyPressedRecord();
                    this.player.usedStaminaPotion();
                }

                if ( input.isKeyPressed( Input.KEY_V ) ) {
                    this.player.increaseExp( 10 );
                }

                    // If the player moves and did not run, he gains stamina
                if (!movedWhileRunning  && !this.playerAttack.isSneaking() ) {
                    this.player.increaseStamina(delta * .003f);
                }
            } // End of not attacking

            if( this.playerAttack.getIsAttacking() && this.playerAttack.isDoneAttacking( input, delta )  ) {
                attacked = true;
                this.playerAttack.setIsAttacking( false );
                input.clearKeyPressedRecord();
            }

            // Start Running
            if (input.isKeyPressed(Input.KEY_LSHIFT) && this.player.getStamina() > this.player.getMinRunningStamina()) {
                this.running = !this.running;
            }
            // If player has stamina to run
           else if (this.player.getStamina() <= this.player.getMinRunningStamina()) {
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


            // Bring up debugging
            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                this.showInfo = !showInfo;
            }

            if(input.isKeyPressed(input.KEY_COMMA)) {
                this.save.save(this.maps.elementAt(this.currentMap), this.currentMap, 1);
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
                sbg.enterState( 0 );
            }
        }

        input.clearKeyPressedRecord();
    }


    public void changeMap( String newName ) {
            // Checking all maps for similar name
        for( int x = 0; x < this.maps.size(); x++ ) {
            if( this.maps.elementAt( x ).getMapName().equals(newName) ) {
                this.player.clearMoves();
                    // Setting new coords
                this.player.setPlayerXinPixels( Math.abs( this.maps.elementAt(this.currentMap).getObjectX()*32 )+320 );
                this.player.setPlayerYinPixels(Math.abs(this.maps.elementAt(this.currentMap).getObjectY()*32 )+320 );
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
