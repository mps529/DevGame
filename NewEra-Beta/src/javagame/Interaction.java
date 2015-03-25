package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.Vector;


public class Interaction {

    Player player;

    public Interaction() {
        player = player.getInstance();
    }

    public Vector<NPC> getLootableEnemies (float x, float y, int direction, Vector<NPC> enemies) {

        // 0-Up, 1-Right, 2-Down, 3-Left

        Vector<NPC> lootableEnemies = new Vector<NPC>();

        for( int i = 0; i < enemies.size(); i++ ) {
            if( !enemies.elementAt(i).getIsAlive() ) {
                    if ((enemies.elementAt(i).getNPCX() >= x - 24 && enemies.elementAt(i).getNPCX() <= x + 24) && (enemies.elementAt(i).getNPCY() >= y - 24 && enemies.elementAt(i).getNPCY() <= y + 24)) {
                        lootableEnemies.add(enemies.elementAt(i));
                        System.out.println("\n");
                        enemies.elementAt(i).getInventory().printInventory();
                    }

            }
        }

        return lootableEnemies;
    }

    public void selectOptions(Graphics g, Vector<NPC> enemies) {




    }

}
