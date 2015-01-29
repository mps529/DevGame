package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.Vector;

public class Inventory {

        /*
            Since there is no data able to be passed
             between states, I created a singleton. This
             makes data transfer between states very easy
         */
   // private static Inventory playerInvintory = null;

        // This is the max, although 2 spots are for potions
    private static int MAX_ITEMS_IN_INVENTORY = 21;
    private int currentInventoryCount;

        // Currency for game
    private int money;

    private static int baseAttack;
    private static int baseDefence;

        // Class
    private int playerLevel;

    private int playerOverallAttack;
    private int playerOverallDefence;

        // Equipped items **( assigned -1 if he is not wearing anything )
    private int playerHelmet;
    private int playerBody;
    private int playerGloves;
    private int playerPants;
    private int playerBoots;
    private int playerRing;
    private int playerNecklaces;
    private int playerWeapon;

        // Count of potions
    private int healthPotions;
    private int staminaPotions;

        // Movement Class
        // 0-Hunter, 1-Warrior, 2-Mage, 3-Rouge
    private int classID;

    private static Vector<Items> itemList;

    public Inventory( ) {

        this.itemList = new Vector<Items>();
        this.currentInventoryCount = 0;
        this.money = 0;
        this.playerHelmet = -1;
        this.playerBody = -1;
        this.playerGloves = -1;
        this.playerPants = -1;
        this.playerBoots = -1;
        this.playerRing = -1;
        this.playerNecklaces = -1;
        this.playerWeapon = -1;

    }
        /*
            Call this function to get a copy of the class between states.
            Do NOT make a new Inventory, this function handles if it needs
            to be created or not.

    public static Inventory getPlayerInvintory( ) {
        if( playerInvintory == null ) {
            playerInvintory = new Inventory( );
        }
        return playerInvintory;
    }
    */


    public void setBaseAttack( int attack ) { this.baseAttack = attack;}
    public int getPlayerOverallAttack() { return this.playerOverallAttack; }

    public void setBaseDefence( int defence ) { this.baseDefence = defence; }
    public int getPlayerOverallDefence() { return this.playerOverallDefence; }

    private void calculateAttackAndDefence(  ) {
            // Setting to base
        this.playerOverallAttack = this.baseAttack;
        this.playerOverallDefence = this.baseDefence;

        for( Items item : this.itemList ) {

            if (item.getID() == this.playerHelmet) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerBody) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerPants) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerGloves) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerWeapon) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerBoots) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerRing) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
            else if (item.getID() == this.playerNecklaces) {
                this.playerOverallAttack += item.getAttackPower();
                this.playerOverallDefence += item.getDefencePower();
            }
        }
    }

    public void setClassID( int classID ) {
        this.classID = classID;
        if( this.itemList.isEmpty() ) {
            this.addStartingItems();
        }
    }
    public int getClassID() {
        return this.classID;
    }

    public int getItemCount() { return this.currentInventoryCount; }

    public int getInventoryCount() {
        int inventory = this.currentInventoryCount;

        if( this.playerHelmet != -1 ) {
            inventory--;
        }
        if( this.playerWeapon != -1 ) {
            inventory--;
        }
        if( this.playerBoots != -1 ) {
            inventory--;
        }
        if( this.playerRing != -1 ) {
            inventory--;
        }
        if( this.playerNecklaces != -1 ) {
            inventory--;
        }
        if( this.playerBody != -1 ) {
            inventory--;
        }
        if( this.playerGloves != -1 ) {
            inventory--;
        }
        if( this.playerPants != -1 ) {
            inventory--;
        }
        return inventory;
    }

    public void addMoney( int money ) { this.money += money; }
    public void subMoney( int money ) { this.money -= money; }
    public int getMoney() { return this.money; }

        // Returns True if added, false if not
    public boolean addItem( Items item  ) {

            // Potions don't actually get added, they just increment the counter
        if( item.getItemID() == 0  ) {
            this.healthPotions++;
            return true;
        }
        else if( item.getItemID() == 1 ) {
            this.staminaPotions++;
            return true;
        }
            // Checks for space in inventory
        if( getInventoryCount() < MAX_ITEMS_IN_INVENTORY ) {
            this.currentInventoryCount++;
            this.itemList.addElement( item );
            return true;
        }
        else {
            return false;
        }
    }

    public boolean dropItem( int ID ) {
        for( int x = 0; x < getItemCount(); x++ ) {
            if( this.itemList.elementAt( x ).getID() == ID ) {
                this.itemList.removeElementAt( x );
                this.currentInventoryCount--;
                return true;
            }
        }
        return false;
    }

    public void addStartingItems( ) {
            // Holds Items
        Items[] basic = new Items[8];

            // Assigning basic Items
        basic[ 0 ] = new Items( 2, this.classID, 0, 1 );
        basic[ 1 ] = new Items( 3, this.classID, 0, 1 );
        basic[ 2 ] = new Items( 4, this.classID, 0, 1 );
        basic[ 3 ] = new Items( 5, this.classID, 0, 1 );
        basic[ 4 ] = new Items( 6, this.classID, 0, 1 );

            // Check class to give correct weapon
        if( this.classID == 0 ) {
            basic[ 5 ] = new Items( 10, this.classID, 0, 1 );
        }
        else if( this.classID == 1 ) {
            basic[ 5 ] = new Items( 9, this.classID, 0, 1 );
        }
        else if( this.classID == 2 ) {
            basic[ 5 ] = new Items( 12, this.classID, 0, 1 );
        }
        else if( this.classID == 3 ) {
            basic[ 5 ] = new Items( 11, this.classID, 0, 1 );
        }

        basic[ 6 ] = new Items( 7, this.classID, 0, 1 );
        basic[ 7 ] = new Items( 8, this.classID, 0, 1 );

            // Adding to inventory and equipping item
        for( int x=0; x < basic.length; x++ ) {
            addItem( basic[x] );
            equipItem( basic[x].getID() );
        }

    }
    // Return 1 for successful equip, 2 for unable to equip
    public int equipItem ( int ID ) {

            // Equipping items to user
        for( int x=0; x < this.itemList.size(); x++ ) {
            if( this.itemList.elementAt( x ).getID() == ID ) {

                    // Checking if it is the correct class
                switch (this.itemList.elementAt(x).getItemID()) {
                    case 2:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4) {
                            this.playerHelmet = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 3:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4) {
                            this.playerBody = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 4:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4) {
                            this.playerPants = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 5:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4 ) {
                            this.playerGloves = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 6:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4 ) {
                            this.playerBoots = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 7:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4 ) {
                            this.playerRing = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 8:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4 ) {
                            this.playerNecklaces = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 9:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4) {
                            this.playerWeapon = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 10:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4 ) {
                            this.playerWeapon = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                    case 11:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4) {
                            this.playerWeapon = ID;
                            calculateAttackAndDefence();
                            return 1;

                        }
                        break;
                    case 12:
                        if (this.itemList.elementAt(x).getClassID() == this.classID || this.itemList.elementAt(x).getClassID() == 4) {
                            this.playerWeapon = ID;
                            calculateAttackAndDefence();
                            return 1;
                        }
                        break;
                }
            }
        }
        return 2;
    }

        // Return 1 for successful unequip, 2 for unable to unequip
    public int unEquipItem( int ID ) {

        if( getInventoryCount() < this.MAX_ITEMS_IN_INVENTORY ) {
            if (this.playerHelmet == ID) {
                this.playerHelmet = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerBoots == ID) {
                this.playerBoots = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerPants == ID) {
                this.playerPants = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerBody == ID) {
                this.playerBody = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerGloves == ID) {
                this.playerGloves = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerWeapon == ID) {
                this.playerWeapon = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerRing == ID) {
                this.playerRing = -1;
                calculateAttackAndDefence();
                return 1;
            } else if (this.playerNecklaces == ID) {
                this.playerNecklaces = -1;
                calculateAttackAndDefence();
                return 1;
            } else {
                return 2;
            }
        }
        else {
            return 2;
        }
    }
        // Returns null if not found
    public Items getItemByID( int ID ) {
        for ( Items item : this.itemList ) {
            if( item.getID() == ID ) {
                return item;
            }
        }
        return null;
    }

    public boolean isWeaponEquipped() {
        if( this.playerWeapon == -1 ) {
            return false;
        }
        return true;
    }

    public void renderInventory( Graphics g, int[] equipped, int[] inventory ) {

        int x = 112 , y=464;
        g.setColor(Color.black);

        int inventoryCounter = 0;

        for( Items item : this.itemList ) {

            if( item.getID() == this.playerHelmet ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(112, 80, 32, 32);
                item.drawImageItem( 112,80 );
                equipped[0] = this.playerHelmet;
            }
            else if( item.getID() == this.playerBody ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(112, 144, 32, 32);
                item.drawImageItem( 112,144 );
                equipped[1] = this.playerBody;
            }
            else if( item.getID() == this.playerPants ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(112, 208, 32, 32);
                item.drawImageItem( 112, 208 );
                equipped[2] = this.playerPants;
            }
            else if( item.getID() == this.playerGloves ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(48, 144, 32, 32);
                item.drawImageItem( 48,144 );
                equipped[4] = this.playerGloves;
            }
            else if( item.getID() == this.playerWeapon ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(176, 144, 32, 32);
                item.drawImageItem( 176, 144 );
                equipped[5] = this.playerWeapon;
            }
            else if( item.getID() == this.playerBoots ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(112, 272, 32, 32);
                item.drawImageItem( 112, 272 );
                equipped[3] = this.playerBoots;
            }
            else if( item.getID() == this.playerRing ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(208, 48, 32, 32);
                item.drawImageItem( 208,48 );
                equipped[6] = this.playerRing;
            }
            else if( item.getID() == this.playerNecklaces ) {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(272, 48, 32, 32);
                item.drawImageItem( 272,48 );
                equipped[7] = this.playerNecklaces;
            }

            else if( item.getItemID() == 0 || item.getItemID() == 1  ) {

            }
            else {
                g.setColor( item.getItemRarityColor() );
                g.fillRect(x, y, 32, 32);
                item.drawImageItem( x,y );
                inventory[inventoryCounter++] = item.getID();

                x += 64;
                if( x > 496 ) {
                    x = 112;
                    y += 64;
                }
            }
        }

        try {
            g.setColor(Color.black);
            Image healthPotion = new Image("NewEra-Beta/res/items/health.png");
            healthPotion.draw(431, 368);
            g.drawString("" + this.healthPotions, 460, 360);

            Image staminaPotion = new Image("NewEra-Beta/res/items/stamina.png");
            staminaPotion.draw(496, 368);
            g.drawString("" + this.staminaPotions, 525, 360);
        }
        catch( SlickException e ){
            e.printStackTrace();
        }
    }

    public void printInventory() {
        String Test;
        for( int x=0; x < this.itemList.size(); x++ ) {
            System.out.println("ID: " + this.itemList.elementAt( x ).getID() + ", Class: " + this.itemList.elementAt( x ).getClassID() + ", ItemID: " + this.itemList.elementAt( x ).getItemID() );
        }
    }

    public void printEquippedItems() {
        System.out.println("Helmet: " + this.playerHelmet);
        System.out.println("Torso: " + this.playerBody);
        System.out.println("Pants: " + this.playerPants);
        System.out.println("Gloves: " + this.playerGloves);
        System.out.println("Boots: " + this.playerBoots);
        System.out.println("Weapon: " + this.playerWeapon);
        System.out.println("Ring: " + this.playerRing);
        System.out.println("Necklaces: " + this.playerNecklaces);

    }
}

