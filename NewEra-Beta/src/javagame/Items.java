package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class Items {

        /*
            This is to determine what kind of item this is
            0 - Health Potion, 1 - Stamina/Magic Potion  **( These do not have rarity or buffs )
            2 - helmet, 3 - Body, 4 - Legs, 5 - Hands, 6 - Feet
            7 - Ring, 8 - Necklaces
            9 - Sword, 10 - Bow, 11 - Dagger, 12 - Wand
        */
    private int itemID;

    private static int ItemsCount = 0;

    private int ID;

        // This specifies which class can use it
        // 0 - Hunter, 1 - Warrior, 2 - Mage, 3 - Rouge, 4 - Any
    private int classID;

        // Buffs
    private int attackPower;
    private int defencePower;

        // Sprite image
    private Image imageItem;

        // Name of item
    private String name;

        // How rare the item is
        // 0 - Common, 1 - Uncommon, 2 - Rare, 3 - Legendary, 4 - God
    private int rarity;

        // Grey - Common, Green - Uncommon, Blue - Rare, Purple - Legendary, Yellow - God
    Color itemRarityColor;
    Color itemRarityColorNoAlpha;

        // Pass -1 in if you don't want to set it
    public Items( int itemId, int classID, int rarity, int level  ) {

        Random randomNumber = new Random();

        if( itemId == -1 ) {
            this.itemID = randomNumber.nextInt( 13 );
        }
        else {
            this.itemID = itemId;
        }
        if( this.itemID != 0 && this.itemID != 1 ) {

            if (classID == -1) {
                switch (this.itemID) {
                    case 9:
                        this.classID = 1;
                        break;
                    case 10:
                        this.classID = 0;
                        break;
                    case 11:
                        this.classID = 3;
                        break;
                    case 12:
                        this.classID = 2;
                        break;
                    default:
                        this.classID = randomNumber.nextInt(5);
                }
            } else {
                this.classID = classID;
            }

            if (rarity == -1) {
                assignRarity(randomNumber);
                assignColor();
            } else {
                this.rarity = rarity;
                assignColor();
            }
        }
        setName();
        setImage();
        if( getClassID() == 0 ) {
            assignStatsForHunter(level);
        }
        increaseAndAssignID();
    }

    public Items( int level ) {
        Random randomNumber = new Random();

        this.itemID = randomNumber.nextInt( 13 );

        if( this.itemID != 0 && this.itemID != 1 ) {
            switch (this.itemID) {
                case 9:
                    this.classID = 1;
                    break;
                case 10:
                    this.classID = 0;
                    break;
                case 11:
                    this.classID = 3;
                    break;
                case 12:
                    this.classID = 2;
                    break;
                default:
                    this.classID = randomNumber.nextInt(5);
            }


            assignRarity(randomNumber);
            assignColor();
        }
        setName();
        setImage();
        if( getClassID() == 0 ) {
            assignStatsForHunter(level);
        }
        increaseAndAssignID();
    }

    public int getAttackPower() { return  this.attackPower; }
    public int getDefencePower() { return this.defencePower; }

    private void assignRarity(  Random randomNumber ) {
        int rarity = randomNumber.nextInt(101);

        if (rarity <= 60) {
            this.rarity = 0;
        } else if (rarity <= 80) {
            this.rarity = 1;
        } else if (rarity == 81) {
            this.rarity = 4;
        } else if (rarity <= 92) {
            this.rarity = 2;
        } else if (rarity <= 100) {
            this.rarity = 100;
        }
    }
    private void assignColor() {

        switch ( this.rarity ) {
                // Common
            case 0:
                this.itemRarityColor = new Color( 0, 0, 0, .3f );
                this.itemRarityColorNoAlpha =  new Color( 0, 0, 0 );
                break;
                // Uncommon
            case 1:
                this.itemRarityColor = new Color( 0, 255 ,0, .5f );
                this.itemRarityColorNoAlpha = new Color( 0, 255 ,0);
                break;
                // Rare
            case 2:
                this.itemRarityColor = new Color( 0,0,255, .5f );
                this.itemRarityColorNoAlpha = new Color( 0,0,255 );
                break;
                // Legendary
            case 3:
                this.itemRarityColor = new Color( 255,0,255, .5f );
                this.itemRarityColorNoAlpha = new Color( 255,0,255 );
                break;
                // God
            case 4:
                this.itemRarityColor = new Color( 255,255,0, .5f );
                this.itemRarityColorNoAlpha = new Color( 55,255,0 );
                break;
        }

    }

    public void setName() {
        if( this.itemID == 0 ) {
            this.name = "Health Potion";
        }
        else if( this.itemID == 1 ) {
            this.name = "Stamina Potion";
        }
        else {
            switch (this.rarity) {
                // Common
                case 0:
                    this.name = "The Common ";
                    break;
                // Uncommon
                case 1:
                    this.name = "The Uncommon ";
                    break;
                // Rare
                case 2:
                    this.name = "The Rare ";
                    break;
                // Legendary
                case 3:
                    this.name = "The Legendary ";
                    break;
                // God
                case 4:
                    this.name = "The God ";
                    break;
            }
            switch (this.classID) {
                // Hunters
                case 0:
                    this.name += "Hunters ";
                    break;
                // Warrior
                case 1:
                    this.name += "Warriors ";
                    break;
                // Mage
                case 2:
                    this.name += "Mages ";
                    break;
                // Rough
                case 3:
                    this.name += "Rouges ";
                    break;
            }
            switch (this.itemID) {

                // Hunters
                case 2:
                    this.name += "Helmet ";
                    break;
                // Warrior
                case 3:
                    this.name += "Cuirass ";
                    break;
                // Mage
                case 4:
                    this.name += "Greave ";
                    break;
                // Rough
                case 5:
                    this.name += "Gloves ";
                    break;
                case 6:
                    this.name += "Boots ";
                    break;
                case 7:
                    this.name += "Ring ";
                    break;
                case 8:
                    this.name += "Necklace ";
                    break;
                case 9:
                    this.name += "Sword ";
                    break;
                case 10:
                    this.name += "Bow ";
                    break;
                case 11:
                    this.name += "Dagger ";
                    break;
                case 12:
                    this.name += "Wand ";
                    break;
            }
        }
    }
    public void setName( String name ) {
        this.name = name;
    }
    public String getName() { return this.name; }

    private void increaseAndAssignID() {
        this.ID = ItemsCount;
        ItemsCount++;
    }

    public int getID() {
        return this.ID;
    }
    public int getItemID() {
        return this.itemID;
    }
    public int getClassID() {
        return this.classID;
    }
    public int getRarity() { return this.rarity; }

    public void assignStatsForHunter( int level ) {
        Random randomNumber = new Random();
        int stat;

        this.attackPower = 0;
        this.defencePower = 0;

            // This is armor stat
        if( getItemID() <= 6 && getItemID() >= 2 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(3);
                stat *= level;
                this.defencePower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(6) + 2;
                stat *= level;
                this.defencePower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(12) + 6;
                stat *= level;
                this.defencePower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(18) + 10;
                stat *= level;
                this.defencePower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 24 ) + 20;
                stat *= level;
                this.defencePower = stat;
            }
        }
            // This is weapon stat
        else if( getItemID() >= 9 && getItemID() <= 12 ) {
                if( getRarity() == 0 ) {
                    stat = randomNumber.nextInt(6);
                    stat *= level;
                    this.attackPower = stat;
                }
                else  if( getRarity() == 1 ) {
                    stat = randomNumber.nextInt(10) + 3;
                    stat *= level;
                    this.attackPower = stat;
                }
                else  if( getRarity() == 2 ) {
                    stat = randomNumber.nextInt(15) + 7;
                    stat *= level;
                    this.attackPower = stat;
                }
                else if( getRarity() == 3 ) {
                    stat = randomNumber.nextInt(20) + 12;
                    stat *= level;
                    this.attackPower = stat;
                }
                else if( getRarity() == 4 ) {
                    stat = randomNumber.nextInt( 30 ) + 20;
                    stat *= level;
                    this.attackPower = stat;
                }
            }
            // Ring or necklace
        else if( getItemID() == 7 || getItemID() == 8 ) {
            int whatToBoost = randomNumber.nextInt(3);
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(2);
                stat *= level;
                if( whatToBoost == 0 ) {
                    this.attackPower = stat;
                }
                else if( whatToBoost == 1 ) {
                    this.defencePower = stat;
                }
                else {
                    this.attackPower = stat;
                    this.defencePower = stat;
                }
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(4) + 2;
                stat *= level;
                if( whatToBoost == 0 ) {
                    this.attackPower = stat;
                }
                else if( whatToBoost == 1 ) {
                    this.defencePower = stat;
                }
                else {
                    this.attackPower = stat;
                    this.defencePower = stat;
                }
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(10) + 4;
                stat *= level;
                if( whatToBoost == 0 ) {
                    this.attackPower = stat;
                }
                else if( whatToBoost == 1 ) {
                    this.defencePower = stat;
                }
                else {
                    this.attackPower = stat;
                    this.defencePower = stat;
                }
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(14) + 9;
                stat *= level;
                if( whatToBoost == 0 ) {
                    this.attackPower = stat;
                }
                else if( whatToBoost == 1 ) {
                    this.defencePower = stat;
                }
                else {
                    this.attackPower = stat;
                    this.defencePower = stat;
                }
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 20 ) + 12;
                stat *= level;
                if( whatToBoost == 0 ) {
                    this.attackPower = stat;
                }
                else if( whatToBoost == 1 ) {
                    this.defencePower = stat;
                }
                else {
                    this.attackPower = stat;
                    this.defencePower = stat;
                }
            }
        }



    }

    public Color getItemRarityColorNoAlpha() {
        return this.itemRarityColorNoAlpha;
    }
    public Color getItemRarityColor() {
        return this.itemRarityColor;
    }
    private void setImage()  {
       try {
            switch ( getItemID() ) {
                case 0:
                    this.imageItem = null;
                    break;
                case 1:
                    this.imageItem = null;
                    break;
                case 2:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 3:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 4:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 5:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 6:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 7:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 8:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 9:
                    this.imageItem = new Image("NewEra-Beta/res/items/sword.png");
                    break;
                case 10:
                    this.imageItem = new Image("NewEra-Beta/res/items/bow.png");
                    break;
                case 11:
                    this.imageItem = new Image("NewEra-Beta/res/items/dagger.png");
                    break;
                case 12:
                    this.imageItem = new Image("NewEra-Beta/res/items/wand.png");
                    break;
            }
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }

    }

    public Image getImageItem() {
        return this.imageItem;
    }
    public void drawImageItem( int x, int y ) {
        this.imageItem.draw(x,y);
    }

}


