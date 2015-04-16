package javagame;


import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class PlayerScreen extends BasicGameState {

        // This game state
    private static int gameState;
        // Movement class
    private Player player;
        // Movement class background
    private TiledMap background;
        // Increasing stats
    private Image plus;

    private Image[] playersAttacks;
    private int[] knownAttacks;

    Color greyLight;
    Color greyDark;

    public PlayerScreen( int gameState ) {
        this.gameState = gameState;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        this.player = this.player.getInstance();
        this.background = new TiledMap( "NewEra-Beta/res/map/PlayerMenu.tmx" );

        this.plus = new Image( "NewEra-Beta/res/buttons/plus.png" );

        this.playersAttacks = this.player.getAttackImages();
        this.knownAttacks = this.player.getAttacksKnown();

        this.greyLight = new Color( 0, 0, 0, .3f );
        this.greyDark = new Color( 0, 0, 0, .7f );
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        this.background.render(0, 0);

            // Name
        g.setColor(Color.black );
        g.drawString(this.player.getPlayerName(), 45, 75);

        g.setColor(Color.black);

        g.drawString("Player Points: " + this.player.getPerkPoints(), 455, 40);
        g.drawString( "Move Points: " + this.player.getMovePoints(), 75, 425  );
            // Base Stats
        g.drawString( "Base Attack: " + this.player.getBaseAttack(), 360, 95 );
        g.drawString( "Base Defence: " + this.player.getBaseDefence(), 360, 115 );

        g.drawString( "Total Attack: " + this.player.getOverallAttack(), 360, 135 );
        g.drawString( "Total Defence: " + this.player.getOverallDefence(), 360, 155 );

        g.drawString( "Total Health: " + (int)this.player.getMaxHealth(), 360, 175 );
        g.drawString( "Total Stamina: " + (int)this.player.getMaxStamina(), 360, 195 );

        this.player.drawPlayerInfoPlayerScreen( g );


        if( this.player.getPerkPoints() != 0 ) {
            this.plus.draw( 540, 95 );
            this.plus.draw( 540, 115 );
            this.plus.draw( 540, 175 );
            this.plus.draw( 540, 195);
       }
        if( this.player.getMovePoints() != 0 ) {
            if( this.player.isMoveKnown( 0 ) ) {
                this.plus.draw(118, 560);
            }
            if( this.player.isMoveKnown( 1 ) || this.player.getLevel() >= 4 ) {
                this.plus.draw(248, 560);
            }
            if( this.player.isMoveKnown( 2 ) || this.player.getLevel() >= 8 ) {
                this.plus.draw(374, 560);
            }
            if( this.player.isMoveKnown( 3 ) || this.player.getLevel() >= 12 ) {
                this.plus.draw(502, 560);
            }
        }

        int startX = 112;
        int startY = 496;
        for( int x = 0; x < 4; x++ ) {

            if( this.knownAttacks[x] == 1 ) {
                g.setColor( this.greyLight );
                g.fillRect(startX, startY, 32, 32);
                this.playersAttacks[x].draw( startX, startY  );
            }
            else {
                g.setColor( this.greyDark );
                this.playersAttacks[x].draw( startX, startY  );
                g.fillRect(startX, startY, 32, 32);
            }
            startX += 128;
        }


    }

    public void enter( GameContainer gc, StateBasedGame sbg ) {
        this.playersAttacks = this.player.getAttackImages();
        this.knownAttacks = this.player.getAttacksKnown();

        this.player.setOverallAttack(this.player.getInventory().getPlayerOverallAttack());
        this.player.setOverallDefence(this.player.getInventory().getPlayerOverallDefence());
    }

    public void leave( GameContainer gc, StateBasedGame sbg ) {
        this.player.getInventory().setBaseAttack( this.player.getBaseAttack() );
        this.player.getInventory().setBaseDefence( this.player.getBaseDefence() );
    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException{

        Input input = gc.getInput();


        if( input.isKeyPressed( Input.KEY_P ) || input.isKeyPressed(Input.KEY_ESCAPE) ) {
            input.clearKeyPressedRecord();
            sbg.enterState( 1 );
        }

        if( this.player.getPerkPoints() != 0 ) {

            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                int mouseX = input.getMouseX();
                int mouseY = input.getMouseY();

                if( ( mouseX >= 540 && mouseX <= 556 ) && ( mouseY >= 95 && mouseY <= 111 ) ) {
                    this.player.increaseBaseAttack(5);
                    this.player.decrementPerkPoints();
                    input.clearMousePressedRecord();
                }
                else if( ( mouseX >= 540 && mouseX <= 556 ) && ( mouseY >= 115 && mouseY <= 131 ) ) {
                    this.player.increaseBaseDefence(5);
                    this.player.decrementPerkPoints();
                    input.clearMousePressedRecord();
                }
                else if( ( mouseX >= 540 && mouseX <= 556 ) && ( mouseY >= 175 && mouseY <= 191 ) ) {
                    this.player.incrementMaxHealth();
                    this.player.decrementPerkPoints();
                    input.clearMousePressedRecord();
                }
                else if( ( mouseX >= 540 && mouseX <= 556 ) && ( mouseY >= 195 && mouseY <= 211 ) ) {
                    this.player.incrementMaxStamina();
                    this.player.decrementPerkPoints();
                    input.clearMousePressedRecord();
                }
            }

        }

        if( this.player.getMovePoints() != 0 ) {

            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                int mouseX = input.getMouseX();
                int mouseY = input.getMouseY();

                if( ( mouseX >= 118 && mouseX <= 134 ) && ( mouseY >= 560 && mouseY <= 576 ) ) {
                    this.player.incrementAttackOneDamage();
                    this.player.decrementMovePoints();
                    input.clearMousePressedRecord();
                }
                else if( this.player.getLevel() >= 4 &&  (( mouseX >= 248 && mouseX <= 264 ) && ( mouseY >= 560 && mouseY <= 576 ) ) ) {
                    if( this.player.isMoveKnown( 1 ) ) {
                        this.player.incrementAttackTwoDamage();
                        this.player.decrementMovePoints();
                        input.clearMousePressedRecord();
                    }
                    else {
                        this.player.unlockMove( 1 );
                        input.clearMousePressedRecord();
                    }
                }
                else if( this.player.getLevel() >= 8 &&  ( ( mouseX >= 374 && mouseX <= 390 ) && ( mouseY >= 560 && mouseY <= 576 ) ) ) {
                    if( this.player.isMoveKnown( 2 ) ) {
                        this.player.incrementAttackThreeDamage();
                        this.player.decrementMovePoints();
                        input.clearMousePressedRecord();
                    }
                    else {
                        this.player.unlockMove( 2 );
                        input.clearMousePressedRecord();
                    }
                }
                else if( this.player.getLevel() >= 12 &&  ( ( mouseX >= 502 && mouseX <= 518 ) && ( mouseY >= 560 && mouseY <= 576 ) ) ) {
                    if( this.player.isMoveKnown( 3 ) ) {
                        this.player.incrementAttackFourDamage();
                        this.player.decrementMovePoints();
                        input.clearMousePressedRecord();
                    }else {
                        this.player.unlockMove( 3 );
                        input.clearMousePressedRecord();
                    }

                }


            }

        }

    }

    public int getID( ) {
        return this.gameState;
    }

}
