package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;
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

    public NPC getLootableEnemy (float x, float y, Vector<NPC> enemies) {

        // 0-Up, 1-Right, 2-Down, 3-Left
        boolean isLooting = false;

        Random rand = new Random();

        for( int i = 0; i < enemies.size(); i++ ) {
            if( !enemies.elementAt(i).getIsAlive() ) {
                    if ((enemies.elementAt(i).getNPCX() >= x - 24 && enemies.elementAt(i).getNPCX() <= x + 24) && (enemies.elementAt(i).getNPCY() >= y - 24 && enemies.elementAt(i).getNPCY() <= y + 24)) {
                        isLooting = true;
                        this.player.setLootingId(enemies.elementAt(i).getId());
                        if(!enemies.elementAt(i).getInventory().isHasBeenLooted()) {
                            enemies.elementAt(i).getInventory().addMoney((rand.nextInt(player.getLevel() + 3) +1) * (rand.nextInt(3) + 1));
                            enemies.elementAt(i).getInventory().setHasBeenLooted(true);
                        }
                        return enemies.elementAt(i);
                    }

            }
        }
        return null;
    }
    public boolean findMerchant (float x, float y, Vector<NPC> allies) {




        for( int i = 0; i < allies.size(); i++ ) {
            if( allies.elementAt(i).isMerchant() ) {
                if ((allies.elementAt(i).getNPCX() >= x - 100 && allies.elementAt(i).getNPCX() <= x + 100) &&
                        (allies.elementAt(i).getNPCY() >= y - 100 && allies.elementAt(i).getNPCY() <= y + 100)) {
                    return true;
                }

            }
        }
        return false;
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
