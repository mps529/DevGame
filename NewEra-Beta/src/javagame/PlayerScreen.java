package javagame;


import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class PlayerScreen extends BasicGameState {

        // This game state
    private static int gameState;
        // Player class
    private PlayerClass player;
        // Player class background
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
        this.background.render( 0,0 );

            // Name
        g.setColor(Color.black );
        g.drawString(this.player.getPlayerName(), 45, 75 );

        g.setColor(Color.black );
            // Base Stats
        g.drawString( "Base Attack: " + this.player.getBaseAttack(), 345, 95 );
        g.drawString( "Base Defence: " + this.player.getBaseDefence(), 345, 115 );
        g.drawString( "Total Attack: " + this.player.getOverallAttack(), 345, 135 );
        g.drawString( "Total Defence: " + this.player.getOverallDefence(), 345, 155 );
        g.drawString( "Total Health: " + (int)this.player.getMaxHealth(), 345, 175 );
        g.drawString( "Total Stamina: " + (int)this.player.getMaxStamina(), 345, 195 );

        if( this.player.getPerkPoints() != 0 ) {
            this.plus.draw( 525, 95 );
            this.plus.draw( 525, 115 );
            this.plus.draw( 525, 175 );
            this.plus.draw( 525, 195);
       }
        if( this.player.getMovePoints() != 0 ) {
            this.plus.draw( 118, 560 );
            this.plus.draw( 248, 560 );
            this.plus.draw( 374, 560 );
            this.plus.draw( 502, 560);
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

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException{

        Input input = gc.getInput();

        this.playersAttacks = this.player.getAttackImages();
        this.knownAttacks = this.player.getAttacksKnown();

        this.player.setOverallAttack( this.player.getInventory().getPlayerOverallAttack()  );
        this.player.setOverallDefence(this.player.getInventory().getPlayerOverallDefence() );

        if( input.isKeyPressed( Input.KEY_P ) ) {
            input.clearKeyPressedRecord();
            sbg.enterState( 1 );
        }

    }

    public int getID( ) {
        return this.gameState;
    }

}
