package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Projectile {

        // Position of projectile
    private Vector2f pos;
        // Position of player
    private Vector2f playerPos;
        // Location of where trhe projectile is in relation to the map
    private Vector2f projectilePos;
        // How long the projectile has been alive for
    private int lived = 0;
        // 0-Up, 1-Right, 2-Down, 3-Left
    private int direction;
        // If the projectile is still active
    private boolean active = true;
        // Lifespan of projectile
    private static int MAX_LIFETIME = 890;

    private int speed = 5 ;

    private boolean spin;

    public Projectile( Vector2f pos, Vector2f playerPos, Vector2f projectilePos, int direction, boolean spin ) {
            // Setting Screen position
        this.pos = pos;
            // Current player position
        this.playerPos = playerPos;
            // Current projectile position
        this.projectilePos = projectilePos;
            // Direction
        this.direction = direction;
            //Spin
        this.spin = spin;
    }

    public Projectile( ) {
        this.active = false;
    }

    public void update( int delta, int x, int y, Map map ) {

            // Checks for the change in position
        int changeX = 0;
        int changeY = 0;

        if( this.active ) {
                // Checking if projectile hit something
            if(  map.isSpaceTaken( this.projectilePos.getX(), this.projectilePos.getY() ) != 0 ) {
                this.active = false;
            }
                // Calculation if player changed position since projectile shot
            changeX = (int)this.playerPos.getX() - x;
            changeY = (int)this.playerPos.getY() - y;
                // If there is a change adjust
            if( changeX != 0 ) {
                this.pos.set( this.pos.getX() + changeX, this.pos.getY() );
                this.playerPos.set( x, y );
            }
            if( changeY != 0 ) {
                this.pos.set( this.pos.getX(), this.pos.getY() + changeY );
                this.playerPos.set( x, y );
            }

                // Increasing projectile position and position on screen
            switch(this.direction) {
                case 0:
                    this.pos.set( pos.getX(), this.pos.getY() - this.speed );
                    this.projectilePos.set( this.projectilePos.getX(), this.projectilePos.getY() - this.speed );
                    break;
                case 1:
                    this.pos.set(pos.getX() + this.speed, this.pos.getY() );
                    this.projectilePos.set(this.projectilePos.getX()+ this.speed, this.projectilePos.getY() );
                    break;
                case 2:
                    this.pos.set( this.pos.getX(), this.pos.getY() + this.speed);
                    this.projectilePos.set( this.projectilePos.getX(), this.projectilePos.getY() + this.speed );
                    break;
                case 3:
                    this.pos.set(pos.getX() - this.speed, this.pos.getY());
                    this.projectilePos.set( this.projectilePos.getX() - this.speed, this.projectilePos.getY() );
                    break;
            }
                // Increase time alive
            this.lived += delta;
                // Check if proectile has live past its time
            if( this.lived > this.MAX_LIFETIME ) {
                this.active = false;
            }
        }
    }


    public void render( GameContainer gc, Graphics g, Image[] projectile ) throws SlickException {
        if( this.active ) {
            if( this.spin ) {
                projectile[ this.direction ].rotate( 110 );
            }
            projectile[ this.direction ].draw( this.pos.getX(), this.pos.getY() );
        }
    }

    public boolean isActive() {
        return this.active;
    }

}
