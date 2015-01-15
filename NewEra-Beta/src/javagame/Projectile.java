package javagame;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Projectile {
        // Position of projectile
    private Vector2f pos;
        // Speed of projectile
    private Vector2f speed;
        // How long the projectile has been alive for
    private int lived = 0;

        // 0-Up, 1-Right, 2-Down, 3-Left
    private int direction;

    private boolean active = true;

        // Lifespan of projectile
    private static int MAX_LIFETIME = 2000;

    public Projectile( Vector2f pos, Vector2f speed, int direction ) {
        this.pos = pos;
        this.speed = speed;
        this.direction = direction;
    }

    public Projectile( ) {
        active = false;
    }
    public void update( int t ) {

        if( active ) {
            Vector2f realSpeed = speed.copy();
            realSpeed.scale( ( t/10000.0f ) );
/*
            switch(direction) {
                case 0:
                    pos.add(realSpeed);
                    break;
                case 1:
                    pos.add(realSpeed);
                    break;
                case 2:
                    pos.add(realSpeed);
                    break;
                case 3:
                    pos.add( realSpeed );
                    break;
            }
*/
            pos.add( realSpeed );

            lived += t;

            if( lived > MAX_LIFETIME ) {
                active = false;
            }

        }

    }

    public void render( GameContainer gc, Graphics g ) throws SlickException {
        if( active ) {

                g.setColor(Color.red);
                g.fillOval(pos.getX()*32, pos.getY()*32, 8, 8);
        }
    }

    public boolean isActive() {
        return active;
    }

}
