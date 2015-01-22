package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class Player {

    // This will be the name of the sprite sheet the user choose
    private String spriteSheetName;
    // Player Name
    private String playerName;

    // This is the sheet that holds all the characters sprites
    private SpriteSheet playerSpriteSheet;

    private int[] durationSpeed = { 80,80,80,80,80,80,80,80 };
    private int[] durationHunterSpeedAttack = { 60,60,60,60,60,60,60,60,60,60,60,60,60 };
    private int[] durationWizardSpeedAttack = { 60,60,60,60,60,60,60 };
    private int[] durationWarriorSpeedAttack = { 60,60,60,60,60,60,60,60 };

    private int[] durationSpeedDeath = { 80,80,80,80,80,80 };

        // Walking animations
    private Animation movingPlayer, movingUp, movingRight, movingDown, movingLeft;
        // Fighting animations
    private Animation attackingPlayer, attackingUp, attackingRight, attackingDown, attackingLeft;
        // Death
    private Animation playingDeath;

        // Pixels to move
    private int playerSpeed = 2;

    private int playerClass;

        // Players position in pixels of map
    private float playerX, playerY;

        // For Projectiles if player has them
    private Projectile[] projectiles;
        // How long arrow will live for.
    private static int FIRE_RATE = 250;
        // Index of the projectile array
    private int currentIndex = 0;
        // Time since last shot
    private int lastShot = 0;

    // projectile Animations
    private Image[] projectileImage;

    public Player(String sheetName, String name, int classID ) {
            // Sets name and spriteSheet
        this.spriteSheetName = sheetName;
        this.playerName = name;

        this.playerClass = classID;

             // Creates new sprite sheet
        try {
            this.playerSpriteSheet = new SpriteSheet("NewEra-Beta/res/players/" + sheetName, 32, 32);

        }
        catch ( SlickException e ){
            System.out.println( "Spritesheet load fail." );
            e.printStackTrace();
        }

        // Sets images for walking animations
        Image[] up = { playerSpriteSheet.getSubImage(1,8), playerSpriteSheet.getSubImage(2,8) , playerSpriteSheet.getSubImage(3,8), playerSpriteSheet.getSubImage(4,8), playerSpriteSheet.getSubImage(5,8), playerSpriteSheet.getSubImage(6,8), playerSpriteSheet.getSubImage(7,8), playerSpriteSheet.getSubImage(8,8) };
        Image[] left = { playerSpriteSheet.getSubImage(1,9), playerSpriteSheet.getSubImage(2,9) , playerSpriteSheet.getSubImage(3,9), playerSpriteSheet.getSubImage(4,9), playerSpriteSheet.getSubImage(5,9), playerSpriteSheet.getSubImage(6,9), playerSpriteSheet.getSubImage(7,9), playerSpriteSheet.getSubImage(8,9) };
        Image[] down = { playerSpriteSheet.getSubImage(1,10), playerSpriteSheet.getSubImage(2,10) , playerSpriteSheet.getSubImage(3,10), playerSpriteSheet.getSubImage(4,10), playerSpriteSheet.getSubImage(5,10), playerSpriteSheet.getSubImage(6,10), playerSpriteSheet.getSubImage(7,10), playerSpriteSheet.getSubImage(8,10) };
        Image[] right = { playerSpriteSheet.getSubImage(1,11), playerSpriteSheet.getSubImage(2,11) , playerSpriteSheet.getSubImage(3,11), playerSpriteSheet.getSubImage(4,11), playerSpriteSheet.getSubImage(5,11), playerSpriteSheet.getSubImage(6,11), playerSpriteSheet.getSubImage(7,11), playerSpriteSheet.getSubImage(8,11) };
        // Assigning the Images to the animations
        this.movingUp = new Animation( up, durationSpeed, true );
        this.movingRight = new Animation( right, durationSpeed, true );
        this.movingDown = new Animation( down, durationSpeed, true );
        this.movingLeft = new Animation( left, durationSpeed, true );

            // Hunter
        if( classID == 0 ) {

            // Sets images for attacking animations
            Image[] upAttack = {playerSpriteSheet.getSubImage(0, 16), playerSpriteSheet.getSubImage(1, 16), playerSpriteSheet.getSubImage(2, 16), playerSpriteSheet.getSubImage(3, 16), playerSpriteSheet.getSubImage(4, 16), playerSpriteSheet.getSubImage(5, 16), playerSpriteSheet.getSubImage(6, 16), playerSpriteSheet.getSubImage(7, 16), playerSpriteSheet.getSubImage(8, 16), playerSpriteSheet.getSubImage(9, 16), playerSpriteSheet.getSubImage(10, 16), playerSpriteSheet.getSubImage(11, 16), playerSpriteSheet.getSubImage(12, 16)};
            Image[] leftAttack = {playerSpriteSheet.getSubImage(0, 17), playerSpriteSheet.getSubImage(1, 17), playerSpriteSheet.getSubImage(2, 17), playerSpriteSheet.getSubImage(3, 17), playerSpriteSheet.getSubImage(4, 17), playerSpriteSheet.getSubImage(5, 17), playerSpriteSheet.getSubImage(6, 17), playerSpriteSheet.getSubImage(7, 17), playerSpriteSheet.getSubImage(8, 17), playerSpriteSheet.getSubImage(9, 17), playerSpriteSheet.getSubImage(10, 17), playerSpriteSheet.getSubImage(11, 17), playerSpriteSheet.getSubImage(12, 17)};
            Image[] downAttack = {playerSpriteSheet.getSubImage(0, 18), playerSpriteSheet.getSubImage(1, 18), playerSpriteSheet.getSubImage(2, 18), playerSpriteSheet.getSubImage(3, 18), playerSpriteSheet.getSubImage(4, 18), playerSpriteSheet.getSubImage(5, 18), playerSpriteSheet.getSubImage(6, 18), playerSpriteSheet.getSubImage(7, 18), playerSpriteSheet.getSubImage(8, 18), playerSpriteSheet.getSubImage(9, 18), playerSpriteSheet.getSubImage(10, 18), playerSpriteSheet.getSubImage(11, 18), playerSpriteSheet.getSubImage(12, 18)};
            Image[] rightAttack = {playerSpriteSheet.getSubImage(0, 19), playerSpriteSheet.getSubImage(1, 19), playerSpriteSheet.getSubImage(2, 19), playerSpriteSheet.getSubImage(3, 19), playerSpriteSheet.getSubImage(4, 19), playerSpriteSheet.getSubImage(5, 19), playerSpriteSheet.getSubImage(6, 19), playerSpriteSheet.getSubImage(7, 19), playerSpriteSheet.getSubImage(8, 19), playerSpriteSheet.getSubImage(9, 19), playerSpriteSheet.getSubImage(10, 19), playerSpriteSheet.getSubImage(11, 19), playerSpriteSheet.getSubImage(12, 19)};
            // Assigning the Images to the animations
            this.attackingUp = new Animation(upAttack, durationHunterSpeedAttack, true);
            this.attackingRight = new Animation(rightAttack, durationHunterSpeedAttack, true);
            this.attackingDown = new Animation(downAttack, durationHunterSpeedAttack, true);
            this.attackingLeft = new Animation(leftAttack, durationHunterSpeedAttack, true);

        }
        else if( classID == 1 ) {
            // Sets images for attacking animations
            Image[] upAttack = {playerSpriteSheet.getSubImage(0, 4), playerSpriteSheet.getSubImage(1, 4), playerSpriteSheet.getSubImage(2, 4), playerSpriteSheet.getSubImage(3, 4), playerSpriteSheet.getSubImage(4, 4), playerSpriteSheet.getSubImage(5, 4), playerSpriteSheet.getSubImage(6, 4), playerSpriteSheet.getSubImage(7, 4)   };
            Image[] leftAttack = {playerSpriteSheet.getSubImage(0, 5), playerSpriteSheet.getSubImage(1, 5), playerSpriteSheet.getSubImage(2, 5), playerSpriteSheet.getSubImage(3, 5), playerSpriteSheet.getSubImage(4, 5), playerSpriteSheet.getSubImage(5, 5), playerSpriteSheet.getSubImage(6, 5), playerSpriteSheet.getSubImage(7, 5)  };
            Image[] downAttack = {playerSpriteSheet.getSubImage(0, 6), playerSpriteSheet.getSubImage(1, 6), playerSpriteSheet.getSubImage(2, 6), playerSpriteSheet.getSubImage(3, 6), playerSpriteSheet.getSubImage(4, 6), playerSpriteSheet.getSubImage(5, 6), playerSpriteSheet.getSubImage(6, 6) , playerSpriteSheet.getSubImage(7, 6) };
            Image[] rightAttack = {playerSpriteSheet.getSubImage(0, 7), playerSpriteSheet.getSubImage(1, 7), playerSpriteSheet.getSubImage(2, 7), playerSpriteSheet.getSubImage(3, 7), playerSpriteSheet.getSubImage(4, 7), playerSpriteSheet.getSubImage(5, 7), playerSpriteSheet.getSubImage(6, 7), playerSpriteSheet.getSubImage(7, 7)  };
            // Assigning the Images to the animations
            this.attackingUp = new Animation(upAttack, durationWarriorSpeedAttack, true);
            this.attackingRight = new Animation(rightAttack, durationWarriorSpeedAttack, true);
            this.attackingDown = new Animation(downAttack, durationWarriorSpeedAttack, true);
            this.attackingLeft = new Animation(leftAttack, durationWarriorSpeedAttack, true);
        }
            // Wizard
        else if( classID == 2 ) {
            // Sets images for attacking animations
            Image[] upAttack = {playerSpriteSheet.getSubImage(0, 0), playerSpriteSheet.getSubImage(1, 0), playerSpriteSheet.getSubImage(2, 0), playerSpriteSheet.getSubImage(3, 0), playerSpriteSheet.getSubImage(4, 0), playerSpriteSheet.getSubImage(5, 0), playerSpriteSheet.getSubImage(6, 0) };
            Image[] leftAttack = {playerSpriteSheet.getSubImage(0, 1), playerSpriteSheet.getSubImage(1, 1), playerSpriteSheet.getSubImage(2, 1), playerSpriteSheet.getSubImage(3, 1), playerSpriteSheet.getSubImage(4, 1), playerSpriteSheet.getSubImage(5, 1), playerSpriteSheet.getSubImage(6, 1) };
            Image[] downAttack = {playerSpriteSheet.getSubImage(0, 2), playerSpriteSheet.getSubImage(1, 2), playerSpriteSheet.getSubImage(2, 2), playerSpriteSheet.getSubImage(3, 2), playerSpriteSheet.getSubImage(4, 2), playerSpriteSheet.getSubImage(5, 2), playerSpriteSheet.getSubImage(6, 2) };
            Image[] rightAttack = {playerSpriteSheet.getSubImage(0, 3), playerSpriteSheet.getSubImage(1, 3), playerSpriteSheet.getSubImage(2, 3), playerSpriteSheet.getSubImage(3, 3), playerSpriteSheet.getSubImage(4, 3), playerSpriteSheet.getSubImage(5, 3), playerSpriteSheet.getSubImage(6, 3) };
            // Assigning the Images to the animations
            this.attackingUp = new Animation(upAttack, durationWizardSpeedAttack, true);
            this.attackingRight = new Animation(rightAttack, durationWizardSpeedAttack, true);
            this.attackingDown = new Animation(downAttack, durationWizardSpeedAttack, true);
            this.attackingLeft = new Animation(leftAttack, durationWizardSpeedAttack, true);
        }

        // Death Animation
        Image[] death = { playerSpriteSheet.getSubImage(0, 20), playerSpriteSheet.getSubImage(1, 20), playerSpriteSheet.getSubImage(2, 20),playerSpriteSheet.getSubImage(3, 20), playerSpriteSheet.getSubImage(4, 20), playerSpriteSheet.getSubImage(5, 20) };
        this.playingDeath = new Animation( death, durationSpeedDeath, true );

        // Setting walking animation
        setPlayerDirection( 2 );

        // Setting player coords;
        setPlayerX( 0 );
        setPlayerY( 0 );

        // Should not be able to shoot more then 8 projectiles at once
        this.projectiles = new Projectile[ 8 ];
        for( int x = 0; x < this.projectiles.length; x++ ){
            this.projectiles[ x ] = new Projectile();
        }
    }

    public void setProjectileImage( Image[] projectileImage ) {
        this.projectileImage = projectileImage;
    }

    public Animation getMovingPlayer(){
        return this.movingPlayer;
    }

    public void drawPlayer( float x, float y ) { this.movingPlayer.draw( x, y );  }
    public void drawPlayerAttacking( float x, float y ) { this.attackingPlayer.draw( x, y );  }
    public void drawPlayerDieing( float x, float y ) { this.playingDeath.draw(x, y);  }

    // 0-Up, 1-Right, 2-Down, 3-Left
    public void setPlayerDirection( int newDirection ) {
        switch ( newDirection ) {
            case 0:
                this.movingPlayer = this.movingUp;
                this.attackingPlayer = this.attackingUp;
                break;
            case 1:
                this.movingPlayer = this.movingRight;
                this.attackingPlayer = this.attackingRight;
                break;
            case 2:
                this.movingPlayer = this.movingDown;
                this.attackingPlayer = this.attackingDown;
                break;
            case 3:
                this.movingPlayer = this.movingLeft;
                this.attackingPlayer = this.attackingLeft;
                break;
            default:
                this.movingPlayer = this.movingDown;
                this.attackingPlayer = this.attackingDown;
        }
    }

    public float getPlayerX() {
        return this.playerX;
    }
    public void setPlayerX( float x ) {
        this.playerX = x*32;
    }
    public void setPlayerXinPixels( float x ) {
        this.playerX = x;
    }

    public void incrementPlayerX() {
        this.playerX += this.playerSpeed;
    }
    public void decrementPlayerX() {
        this.playerX -= this.playerSpeed;
    }

    public float getPlayerY() { return this.playerY; }
    public void setPlayerY( float y ) { this.playerY = y*32; }
    public void setPlayerYinPixels( float y ) {
        this.playerY = y;
    }

    public void incrementPlayerY() {
        this.playerY += this.playerSpeed;
    }
    public void decrementPlayerY() {
        this.playerY -= this.playerSpeed;
    }

    public void startAnimationWalking() { this.movingPlayer.start(); }
    public void stopAnimationWalking() { this.movingPlayer.stop(); }

    public void startAnimationAttacking() {
        this.attackingPlayer.restart();
        this.attackingPlayer.start();
    }
    public void stopAnimationAttacking() {
        if (this.playerClass == 0) {
            this.attackingPlayer.stopAt(12);
        }
        else if( this.playerClass == 2 ) {
            this.attackingPlayer.stopAt( 6 );
        }
        else if( this.playerClass == 1 ) {
            this.attackingPlayer.stopAt( 7 );
        }
    }

    public void startAnimationDeath() {
        this.playingDeath.start();
    }
    public void stopAnimationDeath() { this.playingDeath.stopAt(5); }

    public boolean isStopped() {
        return this.attackingPlayer.isStopped();
    }
    public boolean isStoppedDead() {
        return this.playingDeath.isStopped();
    }

    public void renderProjectile(  GameContainer gc, Graphics g ) throws SlickException {
       if( this.playerClass == 0 || this.playerClass == 2 ) {
           for (Projectile p : this.projectiles) {
               p.render(gc, g, this.projectileImage);
           }
       }
    }
    public void updateProjectile( int delta, boolean shot, Map map  )  {

            // Increases time since last shot
        this.lastShot += delta;
            // Checks if the time is good to shoot again and if the player shot an projectile
        if( this.lastShot > this.FIRE_RATE && shot) {

            /*
                This section checks which direction the player is looking then creates a new projectile in that direcetion.
                  The first vector is where the projectile will be drawn on the screen
                  The second vector is for holding the position of the player, so if they move the project will move to look like it is in the sam position
                  The third vector is for the projectiles position.  This will allow it to crash on collisions and detect people *( Future update to hurt others )
                  There needs to be two vectors passed in for the players position because of how the Vector2f works
            */

            if( this.attackingPlayer == this.attackingUp ) {
                this.projectiles[ this.currentIndex++ ] = new Projectile( new Vector2f( 320 ,300 ), new Vector2f( getPlayerX(), getPlayerY() ), new Vector2f( getPlayerX(), getPlayerY() ), 0 );
            }
            else if( this.attackingPlayer == this.attackingRight ) {
                this.projectiles[ this.currentIndex++ ] = new Projectile( new Vector2f( 340, 325 ), new Vector2f( getPlayerX(), getPlayerY() ), new Vector2f( getPlayerX(), getPlayerY() ), 1 );
            }
            else if( this.attackingPlayer == this.attackingDown ) {
                this.projectiles[ this.currentIndex++ ] = new Projectile( new Vector2f( 320 , 338 ), new Vector2f( getPlayerX(), getPlayerY() ), new Vector2f( getPlayerX(), getPlayerY() ), 2 );
            }
            else if( this.attackingPlayer == this.attackingLeft ) {
                this.projectiles[ this.currentIndex++ ] = new Projectile( new Vector2f( 300 , 325 ), new Vector2f( getPlayerX(), getPlayerY() ), new Vector2f( getPlayerX(), getPlayerY() ), 3 );
            }

            if( this.currentIndex >= this.projectiles.length ) {
                this.currentIndex = 0;
            }
            this.lastShot = 0;
        }

        for (Projectile p : this.projectiles) {
            p.update( delta, (int)getPlayerX(), (int)getPlayerY(), map );
        }

    }

    public void isRunning() {
        this.playerSpeed = 3;
    }
    public void isNotRunning() {
        this.playerSpeed = 2;
    }

}
