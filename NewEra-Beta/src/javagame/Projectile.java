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
    private Vector2f playerPos;

    private Vector2f projectilePos;
        // How long the projectile has been alive for
    private int lived = 0;

        // 0-Up, 1-Right, 2-Down, 3-Left
    private int direction;

    private boolean active = true;

    Color brown;

        // Lifespan of projectile
    private static int MAX_LIFETIME = 2000;

    public Projectile( Vector2f pos, Vector2f playerPos, Vector2f projectilePos, int direction ) {
        this.pos = pos;
        this.playerPos = playerPos;
        this.projectilePos = projectilePos;

        this.direction = direction;
        brown = new Color( 139, 89, 46 );
    }

    public Projectile( ) {
        active = false;
        brown = new Color( 139, 89, 46 );
    }
    public void update( int t, int x, int y, Map2 map ) {

        int changeX = 0;
        int changeY = 0;

        if( active ) {

            if(  map.isSpaceTaken( projectilePos.getX(), projectilePos.getY() ) != 0 ) {
                active = false;
            }
            System.out.println("X: " + projectilePos.getX() / 32 + ", Y: " + projectilePos.getY()/32 + " collision: " + map.isSpaceTaken(projectilePos.getX(), projectilePos.getY()) );


            changeX = (int)playerPos.getX() - x;
            changeY = (int)playerPos.getY() - y;

            if( changeX != 0 ) {
                pos.set( pos.getX() + changeX, pos.getY() );
                playerPos.set( x, y );
            }
            if( changeY != 0 ) {
                pos.set( pos.getX(), pos.getY() + changeY );
                playerPos.set( x, y );
            }

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


            lived += t;

            if( lived > MAX_LIFETIME ) {
                active = false;
            }

        }

    }


    public void render( GameContainer gc, Graphics g ) throws SlickException {
        if( active ) {

            g.setColor( brown );

            if( direction == 1 || direction == 3 ) {
                g.drawRoundRect(pos.getX(), pos.getY(), 16, 1, 10);
            }
            else {
                g.drawRoundRect(pos.getX(), pos.getY(), 1, 16, 10);
            }

        }
    }

    public boolean isActive() {
        return active;
    }

}
