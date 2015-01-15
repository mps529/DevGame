package javagame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player {
        // This will be the name of the sprite sheet the user choose
    private String spriteSheetName;
        // Player Name
    private String playerName;

        // This is the sheet that holds all the characters sprites
    private SpriteSheet playerSpriteSheet;

    private int[] durationSpeed = { 80,80,80,80,80,80,80,80 };
        // Walking animations
    private Animation movingPlayer, movingUp, movingRight, movingDown, movingLeft;
        // Fighting animations

    private int playerSpeed = 100;

    private int playerX, playerY;


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
            // Setting walking animation
        setPlayerDirection( 2 );

            // Setting player coords;
        setPlayerX( 0 );
        setPlayerY( 0 );
    }

    public Animation getMovingPlayer(){
        return movingPlayer;
    }

    public void drawPlayer( int x, int y ) { movingPlayer.draw( x, y );  }

        // 0-Up, 1-Right, 2-Down, 3-Left
    public void setPlayerDirection( int newDirection ) {
        switch ( newDirection ) {
            case 0:
                movingPlayer = movingUp;
                break;
            case 1:
                movingPlayer = movingRight;
                break;
            case 2:
                movingPlayer = movingDown;
                break;
            case 3:
                movingPlayer = movingLeft;
                break;
            default:
                movingPlayer = movingDown;
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

    public void startAnimation() { movingPlayer.start(); }

    public void stopAnimation() { movingPlayer.stop(); }
}
