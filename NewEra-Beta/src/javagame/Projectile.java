package javagame;


import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class Projectile {
        // Position of projectile
    private Vector2f pos;
        // Speed of projectile
    private Vector2f playerPos;
        // Location of where trhe projectile is in relation to the map
    private Vector2f projectilePos;
        // How long the projectile has been alive for
    private int lived = 0;

        // 0-Up, 1-Right, 2-Down, 3-Left
    private int direction;

        // If the projectile is still active
    private boolean active = true;

        // This is for the arrow color
    Color brown;

        // Lifespan of projectile
    private static int MAX_LIFETIME = 1500;

    public Projectile( Vector2f pos, Vector2f playerPos, Vector2f projectilePos, int direction ) {
            // Setting Screen position
        this.pos = pos;
            // Current player position
        this.playerPos = playerPos;
            // Current projectile position
        this.projectilePos = projectilePos;
            // Direction
        this.direction = direction;
            // Setting the rgb of the color brown
        brown = new Color( 139, 89, 46 );
    }

    public Projectile( ) {
        active = false;
    }

    public void update( int t, int x, int y, Map2 map ) {

            // Checks for the change in position
        int changeX = 0;
        int changeY = 0;

        if( active ) {
                // Checking if projectile hit something
            if(  map.isSpaceTaken( projectilePos.getX(), projectilePos.getY() ) != 0 ) {
                active = false;
            }
                // Calculation if player changed position since projectile shot
            changeX = (int)playerPos.getX() - x;
            changeY = (int)playerPos.getY() - y;
                // If there is a change adjust
            if( changeX != 0 ) {
                pos.set( pos.getX() + changeX, pos.getY() );
                playerPos.set( x, y );
            }
            if( changeY != 0 ) {
                pos.set( pos.getX(), pos.getY() + changeY );
                playerPos.set( x, y );
            }

                // Increasing projectile position and position on screen
            switch(direction) {
                case 0:
                    pos.set( pos.getX(), pos.getY() - 3 );
                    projectilePos.set( projectilePos.getX(), projectilePos.getY() - 3 );
                    break;
                case 1:
                    pos.set(pos.getX() + 3, pos.getY() );
                    projectilePos.set( projectilePos.getX()+3, projectilePos.getY() );
                    break;
                case 2:
                    pos.set( pos.getX(), pos.getY() + 3);
                    projectilePos.set( projectilePos.getX(), projectilePos.getY() + 3 );
                    break;
                case 3:
                    pos.set(pos.getX() - 3, pos.getY());
                    projectilePos.set( projectilePos.getX() - 3, projectilePos.getY() );
                    break;
            }
                // Increase time alive
            lived += t;
                // Check if proectile has live past its time
            if( lived > MAX_LIFETIME ) {
                active = false;
            }
        }
    }


    public void render( GameContainer gc, Graphics g, Image[] projectile ) throws SlickException {
        if( active ) {
            projectile[ direction ].draw( pos.getX(), pos.getY() );
        }
    }

    public boolean isActive() {
        return active;
    }

}
