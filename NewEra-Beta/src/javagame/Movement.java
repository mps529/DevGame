package javagame;

import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class Movement {

    // This will be the name of the sprite sheet the user choose
    private String spriteSheetName;
    // Movement Name
    private String playerName;

    // This is the sheet that holds all the characters sprites
    private SpriteSheet playerSpriteSheet;

    private int[] durationSpeed = { 80,80,80,80,80,80,80,80 };
    private int[] durationHunterSpeedAttack = { 50,50,50,50,50,50,50,50,50,50,50,50,50 };
    private int[] durationWizardSpeedAttack = { 60,60,60,60,60,60 };
    private int[] durationRougeSpeedAttack = { 60,60,60,60,60,60  };
    private int[] durationWarriorSpeedAttack = { 50,50,50,50,50,50,50,50 };

    private int[] durationSpeedDeath = { 120,120,120,120,120,120 };

        // Walking animations
    private Animation movingPlayer, movingUp, movingRight, movingDown, movingLeft;
        // Fighting animations
    private Animation attackingPlayer, attackingUp, attackingRight, attackingDown, attackingLeft;
        // Death
    private Animation playingDeath;

    private int direction;

        // Pixels to move
    private int playerSpeed = 2;

    private int playerClass;

        // Players position in pixels of map
    private float playerX, playerY;

        // For Projectiles if player has them
    private Projectile[] projectiles;
        // How long till another arrow can be shot
    private static int FIRE_RATE = 250;
        // Index of the projectile array
    private int currentIndex = 0;
        // Time since last shot
    private int lastShot = 0;

    // projectile Animations
    private Image[] projectileImage;

    private Image trap;

    private Trap[] traps;
        // How long the trap will stay for
    private static int TRAP_RATE = 250;
        // Time since last shot
    private int lastLayed = 0;
        //
    private int currentTrapIndex = 0;

        // This is for storing what map you are in
    private int currentMapIndex;

    public Movement() {

    }

    public void setPlayerClass( String sheetName, String name, int classID ) {
        // Sets name and spriteSheet
        this.spriteSheetName = sheetName;
        this.playerName = name;

        this.playerClass = classID;

        // Creates new sprite sheet
        try {
            this.playerSpriteSheet = new SpriteSheet("NewEra-Beta/res/players/" + sheetName, 32, 32);
            this.trap = new Image("NewEra-Beta/res/moves/trap.png");

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
            // Moves One and Four
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
            Image[] upAttack = {playerSpriteSheet.getSubImage(0, 12), playerSpriteSheet.getSubImage(1, 12), playerSpriteSheet.getSubImage(2, 12), playerSpriteSheet.getSubImage(3, 12), playerSpriteSheet.getSubImage(4, 12), playerSpriteSheet.getSubImage(5, 12) };
            Image[] leftAttack = {playerSpriteSheet.getSubImage(0, 13), playerSpriteSheet.getSubImage(1, 13), playerSpriteSheet.getSubImage(2, 13), playerSpriteSheet.getSubImage(3, 13), playerSpriteSheet.getSubImage(4, 13), playerSpriteSheet.getSubImage(5, 13) };
            Image[] downAttack = {playerSpriteSheet.getSubImage(0, 14), playerSpriteSheet.getSubImage(1, 14), playerSpriteSheet.getSubImage(2, 14), playerSpriteSheet.getSubImage(3, 14), playerSpriteSheet.getSubImage(4, 14), playerSpriteSheet.getSubImage(5, 14) };
            Image[] rightAttack = {playerSpriteSheet.getSubImage(0, 15), playerSpriteSheet.getSubImage(1, 15), playerSpriteSheet.getSubImage(2, 15), playerSpriteSheet.getSubImage(3, 15), playerSpriteSheet.getSubImage(4, 15), playerSpriteSheet.getSubImage(5, 15) };
            // Assigning the Images to the animations
            this.attackingUp = new Animation(upAttack, durationWizardSpeedAttack, true);
            this.attackingRight = new Animation(rightAttack, durationWizardSpeedAttack, true);
            this.attackingDown = new Animation(downAttack, durationWizardSpeedAttack, true);
            this.attackingLeft = new Animation(leftAttack, durationWizardSpeedAttack, true);
        }
        else if( classID == 3 ) {
            // Sets images for attacking animations
            Image[] upAttack = {playerSpriteSheet.getSubImage(0, 12), playerSpriteSheet.getSubImage(1, 12), playerSpriteSheet.getSubImage(2, 12), playerSpriteSheet.getSubImage(3, 12), playerSpriteSheet.getSubImage(4, 12), playerSpriteSheet.getSubImage(5, 12) };
            Image[] leftAttack = {playerSpriteSheet.getSubImage(0, 13), playerSpriteSheet.getSubImage(1, 13), playerSpriteSheet.getSubImage(2, 13), playerSpriteSheet.getSubImage(3, 13), playerSpriteSheet.getSubImage(4, 13), playerSpriteSheet.getSubImage(5, 13)};
            Image[] downAttack = {playerSpriteSheet.getSubImage(0, 14), playerSpriteSheet.getSubImage(1, 14), playerSpriteSheet.getSubImage(2, 14), playerSpriteSheet.getSubImage(3, 14), playerSpriteSheet.getSubImage(4, 14), playerSpriteSheet.getSubImage(5, 14) };
            Image[] rightAttack = {playerSpriteSheet.getSubImage(0, 15), playerSpriteSheet.getSubImage(1, 15), playerSpriteSheet.getSubImage(2, 15), playerSpriteSheet.getSubImage(3, 15), playerSpriteSheet.getSubImage(4, 15), playerSpriteSheet.getSubImage(5, 15) };
            // Assigning the Images to the animations
            this.attackingUp = new Animation(upAttack, durationRougeSpeedAttack, true);
            this.attackingRight = new Animation(rightAttack, durationRougeSpeedAttack, true);
            this.attackingDown = new Animation(downAttack, durationRougeSpeedAttack, true);
            this.attackingLeft = new Animation(leftAttack, durationRougeSpeedAttack, true);
        }

        // Death Animation
        Image[] death = { playerSpriteSheet.getSubImage(0, 20), playerSpriteSheet.getSubImage(1, 20), playerSpriteSheet.getSubImage(2, 20),playerSpriteSheet.getSubImage(3, 20), playerSpriteSheet.getSubImage(4, 20), playerSpriteSheet.getSubImage(5, 20) };
        this.playingDeath = new Animation( death, durationSpeedDeath, true );

        // Setting walking animation
        setPlayerDirection( 2 );

        // Should not be able to shoot more then 8 projectiles at once
        this.projectiles = new Projectile[ 8 ];
        for( int x = 0; x < this.projectiles.length; x++ ){
            this.projectiles[ x ] = new Projectile();
        }

        this.traps = new Trap[ 3 ];
        for( int x = 0; x < this.traps.length; x++ ){
            this.traps[ x ] = new Trap();
        }
    }

    public void setProjectileImage( Image[] projectileImage ) {
        this.projectileImage = projectileImage;
    }

    public Animation getMovingPlayer(){
        return this.movingPlayer;
    }

    public SpriteSheet getPlayerSpriteSheet() { return this.playerSpriteSheet; }

    public void drawPlayer( float x, float y ) { this.movingPlayer.draw( x, y );  }
    public void drawPlayerDieing( float x, float y ) { this.playingDeath.draw(x, y);  }


    public String getPlayerName() { return this.playerName; }

    public int getDirection() { return this.direction; }

    public void setCurrentMapIndex( int index ) {
        this.currentMapIndex = index;
    }
    public int getCurrentMapIndex() {
        return this.currentMapIndex;
    }

    // 0-Up, 1-Right, 2-Down, 3-Left
    public void setPlayerDirection( int newDirection ) {
        this.direction = newDirection;
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

    public int getPlayerClass() { return this.playerClass; }

    public float getPlayerX() {
        return this.playerX;
    }
    public float getPlayerXForMap() { return ( this.playerX - 320 )*-1; }

    public void setPlayerX( float x ) {
        this.playerX = x*32;
    }
    public void setPlayerXinPixels( float x ) {
        this.playerX = x;
    }

    public String getSpriteSheetName() {return this.spriteSheetName;}

    public void incrementPlayerX() {
        this.playerX += this.playerSpeed;
    }
    public void decrementPlayerX() {
        this.playerX -= this.playerSpeed;
    }

    public float getPlayerY() { return this.playerY; }
    public float getPlayerYForMap() { return ( this.playerY - 320 )*-1; }
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

    public void startAnimationDeath() {
        this.playingDeath.start();
    }
    public void stopAnimationDeath() { this.playingDeath.stopAt(5); }
    
    public boolean isStoppedDead() {
        return this.playingDeath.isStopped();
    }

    public void clearMoves() {
        for(  Trap trap: traps ) {
            trap.killTrap();
        }

        for(  Projectile projectile: projectiles ) {
            projectile.killProjectile();
        }
    }

    public void renderProjectile(  GameContainer gc, Graphics g ) throws SlickException {
        for (Projectile p : this.projectiles) {
            p.render(gc, g, this.projectileImage);
        }
    }

    public void updateProjectile( int delta, boolean shot, Map map, int move  )  {

            // Increases time since last shot
        this.lastShot += delta;

        boolean spin = false;

        if( ( this.playerClass == 0 && ( move == 0 || move == 3 ) )  ||
                (this.playerClass == 1 && move == 1) ||
                (this.playerClass == 2 && move == 0) ||
                (this.playerClass == 3 && move == 1) ) {

            if( this.playerClass == 3 ) {
                spin = true;
            }

            // Checks if the time is good to shoot again and if the player shot an projectile
            if (this.lastShot > this.FIRE_RATE && shot) {
            /*
                This section checks which direction the player is looking then creates a new projectile in that direction.
                  The first vector is where the projectile will be drawn on the screen
                  The second vector is for holding the position of the player, so if they move the project will move to look like it is in the sam position
                  The third vector is for the projectiles position.  This will allow it to crash on collisions and detect people *( Future update to hurt others )
                  There needs to be two vectors passed in for the players position because of how the Vector2f works
            */

                if (this.attackingPlayer == this.attackingUp) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(320, 300), new Vector2f(getPlayerX(), getPlayerY()), new Vector2f(getPlayerX(), getPlayerY()), 0, spin);
                } else if (this.attackingPlayer == this.attackingRight) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(340, 325), new Vector2f(getPlayerX(), getPlayerY()), new Vector2f(getPlayerX(), getPlayerY()), 1, spin);
                } else if (this.attackingPlayer == this.attackingDown) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(320, 338), new Vector2f(getPlayerX(), getPlayerY()), new Vector2f(getPlayerX(), getPlayerY()), 2, spin);
                } else if (this.attackingPlayer == this.attackingLeft) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(300, 325), new Vector2f(getPlayerX(), getPlayerY()), new Vector2f(getPlayerX(), getPlayerY()), 3, spin);
                }

                if (this.currentIndex >= this.projectiles.length) {
                    this.currentIndex = 0;
                }
                this.lastShot = 0;
            }
        }
        for (Projectile p : this.projectiles) {
            p.update( delta, (int)getPlayerX(), (int)getPlayerY(), map, true );
        }

    }

    public void renderTraps( GameContainer gc, Graphics g ) throws SlickException {
        for ( Trap t : this.traps ) {
            t.render( gc, g );
        }
    }

    public void updateTrap( int delta, boolean layed, Map map, int move ) {
        // Increases time since last shot
        this.lastLayed += delta;

        if( this.playerClass == 0 && move == 1 ) {
            if(  layed ) {
                this.traps[ this.currentTrapIndex++ ] = new Trap(  new Vector2f( 320, 320 ),  new Vector2f( getPlayerX(), getPlayerY() ), new Vector2f( getPlayerX(), getPlayerY() ) );
            }
            if (this.currentTrapIndex >= this.traps.length) {
                this.currentTrapIndex = 0;
            }
            this.lastLayed = 0;
        }

        for (Trap t : this.traps) {
            t.update( delta, (int)getPlayerX(), (int)getPlayerY(), map  );
        }
    }
    
    public void isRunning() {
        this.playerSpeed = 3;
    }
    public void isNotRunning() {
        this.playerSpeed = 2;
    }

}
