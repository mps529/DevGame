package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class NPCMovement {

        // Npc Skin
    private String spriteSheetName;

    // This is the sheet that holds all the characters sprites
    private SpriteSheet playerSpriteSheet;

    private int[] durationSpeed = { 80,80,80,80,80,80,80,80 };
    private int[] durationHunterSpeedAttack = { 60,60,60,60,60,60,60,60,60,60,60,60,60 };
    private int[] durationWizardSpeedAttack = { 60,60,60,60,60,60};
    private int[] durationRougeSpeedAttack = { 60,60,60,60,60,60  };
    private int[] durationWarriorSpeedAttack = { 60,60,60,60,60,60,60,60 };

    private int[] durationSpeedDeath = { 80,80,80,80,80,80 };

    // Walking animations
    private Animation movingNPC, movingUp, movingRight, movingDown, movingLeft;
    // Fighting animations
    private Animation attackingNPC, attackingUp, attackingRight, attackingDown, attackingLeft;
    private int frameStop = 0;
    // Death
    private Animation npcDeath;

    // 0-Up, 1-Right, 2-Down, 3-Left
    private int direction;

    // Pixels to move
    private int npcSpeed = 2;

    private int npcClass;

        // This tel where the enemy was spawned at.
    private float spawnX;
    private float spawnY;

    // Players position in pixels of map
    private float npcX, npcY;

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


    public NPCMovement() {

    }

    public void setNPCClass( String sheetName, int classID ) {
        // Sets name and spriteSheet
        this.spriteSheetName = sheetName;

        this.npcClass = classID;

        // Creates new sprite sheet
        try {
            this.playerSpriteSheet = new SpriteSheet("NewEra-Beta/res/enemies/" + sheetName, 32, 32);

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
            this.frameStop = 12;
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
            this.frameStop = 7;
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
            this.frameStop = 5;
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
            this.frameStop = 5;
        }

        // Death Animation
        Image[] death = { playerSpriteSheet.getSubImage(0, 20), playerSpriteSheet.getSubImage(1, 20), playerSpriteSheet.getSubImage(2, 20),playerSpriteSheet.getSubImage(3, 20), playerSpriteSheet.getSubImage(4, 20), playerSpriteSheet.getSubImage(5, 20) };
        this.npcDeath = new Animation( death, durationSpeedDeath, true );

        // Setting walking animation
        setPlayerDirection( 2 );

        // Should not be able to shoot more then 8 projectiles at once
        this.projectiles = new Projectile[ 8 ];
        for( int x = 0; x < this.projectiles.length; x++ ){
            this.projectiles[ x ] = new Projectile();
        }

    }

    // 0-Up, 1-Right, 2-Down, 3-Left
    public void setPlayerDirection( int newDirection ) {
        this.direction = newDirection;
        switch ( newDirection ) {
            case 0:
                this.movingNPC = this.movingUp;
                this.attackingNPC = this.attackingUp;
                break;
            case 1:
                this.movingNPC = this.movingRight;
                this.attackingNPC = this.attackingRight;
                break;
            case 2:
                this.movingNPC = this.movingDown;
                this.attackingNPC = this.attackingDown;
                break;
            case 3:
                this.movingNPC = this.movingLeft;
                this.attackingNPC = this.attackingLeft;
                break;
            default:
                this.movingNPC = this.movingDown;
                this.attackingNPC = this.attackingDown;
        }
    }
    public int getDirection() { return this.direction; }

    public void setProjectileImage( Image[] projectileImage ) {
        this.projectileImage = projectileImage;
    }

    public Animation getMovingNPC(){
        return this.movingNPC;
    }
    public Animation getAttackingNPC() { return  this.attackingNPC; }
    public Animation getDieingNPC(){
        return this.npcDeath;
    }

    public void setSpawnX( float x ) { this.spawnX = x; this.npcX = x; }
    public void setSpawnY( float y ) { this.spawnY = y; this.npcY = y; }

    public float getSpawnX() { return this.spawnX; }
    public float getSpawnY() { return this.spawnY; }

    public float getNPCX() {
        return this.npcX;
    }
    public float getNPCXTile() {
        return this.npcX/32;
    }
    public float getNPCXForMap() { return ( this.npcX - 320 )*-1; }

    public void setNPCX( float x ) {
        this.npcX = x*32;
    }
    public void setNPCXinPixels( float x ) {
        this.npcX = x;
    }

    public String getSpriteSheetName() {return this.spriteSheetName;}

    public void incrementNPCX() {
        this.npcX += this.npcSpeed;
    }
    public void decrementNPCX() {
        this.npcX -= this.npcSpeed;
    }

    public float getNPCY() { return this.npcY; }
    public float getNPCYTile() {
        return this.npcY/32;
    }
    public float getNPCYForMap() { return ( this.npcY - 320 )*-1; }

    public void setNPCY( float y ) { this.npcY = y*32; }
    public void setNPCYinPixels( float y ) {
        this.npcY = y;
    }

    public void incrementNPCY() {
        this.npcY += this.npcSpeed;
    }
    public void decrementNPCY() {
        this.npcY -= this.npcSpeed;
    }

    public void startAnimationWalking() { this.movingNPC.start(); }
    public void stopAnimationWalking() { this.movingNPC.stop(); }

    public void startAnimationDeath() {
        this.npcDeath.start();
    }
    public void stopAnimationDeath() { this.npcDeath.stopAt(5); }

    public void startAnimationAttacking() {
        this.attackingNPC.restart();
        this.attackingNPC.start();
    }
    public void stopAnimationAttacking() {
        this.attackingNPC.stopAt( this.frameStop );
    }
    public boolean isStoppedAttacking() { return this.attackingNPC.isStopped(); }

    public boolean isStoppedDead() {
        return this.npcDeath.isStopped();
    }

    public void clearMoves() {
        for(  Projectile projectile: projectiles ) {
            projectile.killProjectile();
        }
    }

    public void renderProjectile(  GameContainer gc, Graphics g ) throws SlickException {
        for (Projectile p : this.projectiles) {
            p.render(gc, g, this.projectileImage);
        }
    }

    public void updateProjectile( int delta, boolean shot, Map map, int playerX, int playerY, boolean good  )  {

        // Increases time since last shot
        this.lastShot += delta;

        if( ( this.npcClass == 0 )  || (this.npcClass == 2 ) ) {

            // Checks if the time is good to shoot again and if the player shot an projectile
            if (this.lastShot > this.FIRE_RATE && shot) {
            /*
                This section checks which direction the player is looking then creates a new projectile in that direcetion.
                  The first vector is where the projectile will be drawn on the screen
                  The second vector is for holding the position of the player, so if they move the project will move to look like it is in the sam position
                  The third vector is for the projectiles position.  This will allow it to crash on collisions and detect people *( Future update to hurt others )
                  There needs to be two vectors passed in for the players position because of how the Vector2f works
            */

                if (this.attackingNPC == this.attackingUp) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(320, 300), new Vector2f(playerX, playerY), new Vector2f(getNPCX(), getNPCY()), 0,false);
                } else if (this.attackingNPC == this.attackingRight) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(340, 325), new Vector2f( playerX, playerY), new Vector2f(getNPCX(), getNPCY()), 1,false);
                } else if (this.attackingNPC == this.attackingDown) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(320, 338), new Vector2f(playerX, playerY), new Vector2f(getNPCX(), getNPCY()), 2,false);
                } else if (this.attackingNPC == this.attackingLeft) {
                    this.projectiles[this.currentIndex++] = new Projectile(new Vector2f(300, 325), new Vector2f(playerX, playerY), new Vector2f(getNPCX(), getNPCY()), 3, false);
                }

                if (this.currentIndex >= this.projectiles.length) {
                    this.currentIndex = 0;
                }
                this.lastShot = 0;
            }
        }
        for (Projectile p : this.projectiles) {
            p.update( delta, (int)this.npcX, (int)this.npcY,  map, good );
        }

    }
}
