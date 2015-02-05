package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Trap {

        // Position of the trap
    private Vector2f pos;
        // Position of player
    private Vector2f playerPos;
        // How long the projectile has been alive for
    private int lived = 0;
        // If the trap is active
    boolean active = true;

    private Image trap;

        // 20 seconds
    private static int MAX_LIFETIME = 20000;


    public Trap(  Vector2f pos, Vector2f playerPos ) {
            // Setting Position
        this.pos = pos;
            // Setting Player position
        this.playerPos = playerPos;

        try {
            trap = new Image("NewEra-Beta/res/moves/trap.png");
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }
    }

    public Trap() {
        this.active = false;
    }

    public void update( int delta, int x, int y ) {

        if( this.active ) {
                // Checks for the change in position
            int changeX = (int) this.playerPos.getX() - x;
            int changeY = (int) this.playerPos.getY() - y;

            if (changeX != 0) {
                this.pos.set(this.pos.getX() + changeX, this.pos.getY());
                this.playerPos.set(x, y);
            }
            if (changeY != 0) {
                this.pos.set(this.pos.getX(), this.pos.getY() + changeY);
                this.playerPos.set(x, y);
            }

                // Increase time alive
            this.lived += delta;
                // Check if trap has live past its time
            if( this.lived > this.MAX_LIFETIME ) {
                this.active = false;
            }
        }
    }

    public void render( GameContainer gc, Graphics g ) throws SlickException {
        if( this.active ) {
            this.trap.draw( this.pos.getX() , this.pos.getY());
        }
    }

}
