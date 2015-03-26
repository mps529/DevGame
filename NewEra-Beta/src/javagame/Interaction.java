package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.Vector;


public class Interaction {

    Player player;
    private int menuX, menuY, menuHeight, menuWidth;

    public Interaction() {

        player = player.getInstance();
        menuHeight = 0;
        menuWidth = 80;
        menuX = 67;
        menuY = 200;
    }

    public NPC getLootableEnemy (float x, float y, int direction, Vector<NPC> enemies) {

        // 0-Up, 1-Right, 2-Down, 3-Left
        boolean isLooting = false;


        for( int i = 0; i < enemies.size(); i++ ) {
            if( !enemies.elementAt(i).getIsAlive() ) {
                    if ((enemies.elementAt(i).getNPCX() >= x - 24 && enemies.elementAt(i).getNPCX() <= x + 24) && (enemies.elementAt(i).getNPCY() >= y - 24 && enemies.elementAt(i).getNPCY() <= y + 24)) {
                        System.out.println("\n");
                        enemies.elementAt(i).getInventory().printInventory();
                        isLooting = true;
                        return enemies.elementAt(i);
                    }

            }
        }


        return null;
    }


    public int getMenuX() {return menuX;}
    public void setMenuX(int menuX) {this.menuX = menuX;}

    public int getMenuY() {return menuY;}
    public void setMenuY(int menuY) {this.menuY = menuY;}

    public int getMenuHeight() {return menuHeight;}
    public void setMenuHeight(int menuHeight) {this.menuHeight = menuHeight;}

    public int getMenuWidth() {return menuWidth;}
    public void setMenuWidth(int menuWidth) {this.menuWidth = menuWidth;}
}
