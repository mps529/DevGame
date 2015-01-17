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
    private int[] durationSpeedAttack = { 80,80,80,80,80,80,80,80,80,80,80,80,80 };
        // Walking animations
    private Animation movingPlayer, movingUp, movingRight, movingDown, movingLeft;
        // Fighting animations
    private Animation attackingPlayer, attackingUp, attackingRight, attackingDown, attackingLeft;

    private int playerSpeed = 0;

    private int playerX, playerY;

        // For Projectiles
    private Projectile[] projectiles;
    private static int FIRE_RATE = 250;
    private int currentIndex = 0;
    private int lastShot = 0;


    public Player( String sheetName, String name ) {
            // Sets name and spriteSheet
        spriteSheetName = sheetName;
        playerName = name;

            // Creates new sprite sheet
        try {
            playerSpriteSheet = new SpriteSheet( "NewEra-Beta/res/players/"+spriteSheetName, 32, 32 );
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
        movingUp = new Animation( up, durationSpeed, true );
        movingRight = new Animation( right, durationSpeed, true );
        movingDown = new Animation( down, durationSpeed, true );
        movingLeft = new Animation( left, durationSpeed, true );

            // Sets images for attacking animations
        Image[] upAttack = { playerSpriteSheet.getSubImage(0,16), playerSpriteSheet.getSubImage(1,16), playerSpriteSheet.getSubImage(2,16) , playerSpriteSheet.getSubImage(3,16), playerSpriteSheet.getSubImage(4,16), playerSpriteSheet.getSubImage(5,16), playerSpriteSheet.getSubImage(6,16), playerSpriteSheet.getSubImage(7,16), playerSpriteSheet.getSubImage(8,16), playerSpriteSheet.getSubImage(9,16), playerSpriteSheet.getSubImage(10,16), playerSpriteSheet.getSubImage(11,16) , playerSpriteSheet.getSubImage(12,16) };
        Image[] leftAttack = { playerSpriteSheet.getSubImage(0,17), playerSpriteSheet.getSubImage(1,17), playerSpriteSheet.getSubImage(2,17) , playerSpriteSheet.getSubImage(3,17), playerSpriteSheet.getSubImage(4,17), playerSpriteSheet.getSubImage(5,17), playerSpriteSheet.getSubImage(6,17), playerSpriteSheet.getSubImage(7,17), playerSpriteSheet.getSubImage(8,17), playerSpriteSheet.getSubImage(9,17), playerSpriteSheet.getSubImage(10,17), playerSpriteSheet.getSubImage(11,17) , playerSpriteSheet.getSubImage(12,17) };
        Image[] downAttack = { playerSpriteSheet.getSubImage(0,18), playerSpriteSheet.getSubImage(1,18), playerSpriteSheet.getSubImage(2,18) , playerSpriteSheet.getSubImage(3,18), playerSpriteSheet.getSubImage(4,18), playerSpriteSheet.getSubImage(5,18), playerSpriteSheet.getSubImage(6,18), playerSpriteSheet.getSubImage(7,18), playerSpriteSheet.getSubImage(8,18), playerSpriteSheet.getSubImage(9,18), playerSpriteSheet.getSubImage(10,18), playerSpriteSheet.getSubImage(11,18) , playerSpriteSheet.getSubImage(12,18) };
        Image[] rightAttack = { playerSpriteSheet.getSubImage(0,19), playerSpriteSheet.getSubImage(1,19), playerSpriteSheet.getSubImage(2,19) , playerSpriteSheet.getSubImage(3,19), playerSpriteSheet.getSubImage(4,19), playerSpriteSheet.getSubImage(5,19), playerSpriteSheet.getSubImage(6,19), playerSpriteSheet.getSubImage(7,19), playerSpriteSheet.getSubImage(8,19), playerSpriteSheet.getSubImage(9,19), playerSpriteSheet.getSubImage(10,19), playerSpriteSheet.getSubImage(11,19) , playerSpriteSheet.getSubImage(12,19) };
        // Assigning the Images to the animations
        attackingUp = new Animation( upAttack, durationSpeedAttack, true );
        attackingRight = new Animation( rightAttack, durationSpeedAttack, true );
        attackingDown = new Animation( downAttack, durationSpeedAttack, true );
        attackingLeft = new Animation( leftAttack, durationSpeedAttack, true );


            // Setting walking animation
        setPlayerDirection( 2 );

            // Setting player coords;
        setPlayerX( 0 );
        setPlayerY( 0 );

        projectiles = new Projectile[ 8 ];
        for( int x = 0; x < projectiles.length; x++ ){
            projectiles[ x ] = new Projectile();
        }
    }

    public Animation getMovingPlayer(){
        return movingPlayer;
    }

    public void drawPlayer( int x, int y ) { movingPlayer.draw( x, y );  }
    public void drawPlayerAttacking( int x, int y ) { attackingPlayer.draw( x, y );  }

        // 0-Up, 1-Right, 2-Down, 3-Left
    public void setPlayerDirection( int newDirection ) {
        switch ( newDirection ) {
            case 0:
                movingPlayer = movingUp;
                attackingPlayer = attackingUp;
                break;
            case 1:
                movingPlayer = movingRight;
                attackingPlayer = attackingRight;
                break;
            case 2:
                movingPlayer = movingDown;
                attackingPlayer = attackingDown;
                break;
            case 3:
                movingPlayer = movingLeft;
                attackingPlayer = attackingLeft;
                break;
            default:
                movingPlayer = movingDown;
                attackingPlayer = attackingDown;
        }
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX( int x ) {
        playerX = x;
    }

    public void incrementPlayerX() {
        try {
            Thread.sleep( playerSpeed );
            playerX++;
        }
        catch ( InterruptedException e ) {
            System.out.println("Failed to sleep in incrementPlayerX");
            e.printStackTrace();
        }
    }

    public void decrementPlayerX() {
        try {
            Thread.sleep( playerSpeed );
            playerX--;
        }
        catch ( InterruptedException e ) {
            System.out.println("Failed to sleep in decrementPlayerX");
            e.printStackTrace();
        }
    }

    public int getPlayerY() { return playerY; }

    public void setPlayerY( int y ) { playerY = y; }

    public void incrementPlayerY() {
        try {
            Thread.sleep( playerSpeed );
            playerY++;
        }
        catch ( InterruptedException e ) {
            System.out.println("Failed to sleep in incrementPlayerY");
            e.printStackTrace();
        }
    }

    public void decrementPlayerY() {
        try {
            Thread.sleep( playerSpeed );
            playerY--;
        }
        catch ( InterruptedException e ) {
            System.out.println("Failed to sleep in decrementPlayerY");
            e.printStackTrace();
        }
    }

    public void startAnimationWalking() { movingPlayer.start(); }
    public void stopAnimationWalking() { movingPlayer.stop(); }

    public void startAnimationAttacking() {
        attackingPlayer.restart();
        attackingPlayer.start();
    }
    public void stopAnimationAttacking() { attackingPlayer.stopAt(12); }

    public boolean isStopped() {
        return attackingPlayer.isStopped();
    }

    public void renderProjectile(  GameContainer gc, Graphics g ) throws SlickException {

            for( Projectile p : projectiles ) {
               //p.render( gc, g );
            }
    }
    public void updateProjectile( int delta, boolean shot, Map2 map  )  {

        lastShot += delta;
        if( lastShot > FIRE_RATE && shot) {

            if( attackingPlayer == attackingUp ) {
                projectiles[ currentIndex++ ] = new Projectile( new Vector2f( getPlayerX() ,getPlayerY() ), new Vector2f(100, -500), new Vector2f( getPlayerX(), getPlayerY() ), 0 );
            }
            else if( attackingPlayer == attackingRight ) {
                projectiles[ currentIndex++ ] = new Projectile( new Vector2f( getPlayerX() ,getPlayerY() ), new Vector2f(100, 0), new Vector2f( getPlayerX(), getPlayerY() ), 1 );
            }
            else if( attackingPlayer == attackingDown ) {
                projectiles[ currentIndex++ ] = new Projectile( new Vector2f( getPlayerX() ,getPlayerY() ), new Vector2f(100, 1), new Vector2f( getPlayerX(), getPlayerY() ), 2 );
            }
            else if( attackingPlayer == attackingLeft ) {
                projectiles[ currentIndex++ ] = new Projectile( new Vector2f( getPlayerX() ,getPlayerY() ), new Vector2f(100, 5000), new Vector2f( getPlayerX(), getPlayerY() ), 3 );
            }

            if( currentIndex >= projectiles.length ) {
                currentIndex = 0;
            }
            lastShot = 0;
        }


        for (Projectile p : projectiles) {
            //p.update( delta, getPlayerX(), getPlayerY(), map );
        }

    }

}
