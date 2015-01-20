package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.opengl.ImageData;

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
        // Current amount of items in Bag
    private static int ItemsCount = 0;
        // ID of current Item
    private int ID;

        // This specifies which class can use it
        // 0 - Hunter, 1 - Warrior, 2 - Mage, 3 - Rouge, 4 - Any
    private int classID;

        // Gold worth of Item
    private int worth;

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
            // If not a potion
        if( this.itemID != 0 && this.itemID != 1 ) {
                // Set for which class it is for **( Applies for weapons only, default is all others )
            if (classID == -1) {
                switch ( this.itemID ) {
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
        else if( getClassID() == 1 ) {
            assignStatsForWarrior(level);
        }
        else if( getClassID() == 2 ) {
            assignStatsForMage(level);
        }
        else if( getClassID() == 3 ) {
            assignStatsForRouge(level);
        }
        else {
            assignStatsForAll( level );
        }
        setWorth();
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
        }
        assignRarity(randomNumber);
        assignColor();

        setName();
        setImage();
        if( getClassID() == 0 ) {
            assignStatsForHunter(level);
        }
        else if( getClassID() == 1 ) {
            assignStatsForWarrior( level );
        }
        else if( getClassID() == 2 ) {
            assignStatsForMage(level);
        }
        else if(getClassID() == 3 ) {
            assignStatsForRouge( level );
        }
        else {
            assignStatsForAll( level );
        }

        setWorth();
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
            this.rarity = 3;
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

    public void setWorth() {
        Random random = new Random();

        this.worth = 0;

        this.worth += getAttackPower() * random.nextInt( 8 ) + 2;
        this.worth += getDefencePower() * random.nextInt( 8 ) + 2;

        this.worth += getRarity() * random.nextInt( 8 ) + 3;
    }
    public void setWorth( int worth ) { this.worth = worth; }
    public int getWorth() { return this.worth; }

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
                stat = randomNumber.nextInt(3) + 1;
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
                    stat = randomNumber.nextInt(6) + 1;
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
    public void assignStatsForWarrior( int level ) {
        Random randomNumber = new Random();
        int stat;

        this.attackPower = 0;
        this.defencePower = 0;

        // This is weapon stat
        if( getItemID() <= 12 && getItemID() >= 9 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(3) + 1;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(6) + 2;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(12) + 6;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(18) + 10;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 24 ) + 20;
                stat *= level;
                this.attackPower = stat;
            }
        }
        // This is armor stat
        else if( getItemID() >= 2 && getItemID() <= 6 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(6) + 1;
                stat *= level;
                this.defencePower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(10) + 3;
                stat *= level;
                this.defencePower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(15) + 7;
                stat *= level;
                this.defencePower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(20) + 12;
                stat *= level;
                this.defencePower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 30 ) + 20;
                stat *= level;
                this.defencePower = stat;
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
    public void assignStatsForMage( int level ) {
        Random randomNumber = new Random();
        int stat;

        this.attackPower = 0;
        this.defencePower = 0;

        // This is armor stat
        if( getItemID() <= 6 && getItemID() >= 2 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(3) + 1;
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
                stat = randomNumber.nextInt(6) + 1;
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
    public void assignStatsForRouge( int level ) {
        Random randomNumber = new Random();
        int stat;

        this.attackPower = 0;
        this.defencePower = 0;

        // This is weapon stat
        if( getItemID() <= 12 && getItemID() >= 9 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(3) + 1;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(6) + 2;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(12) + 6;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(18) + 10;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 24 ) + 20;
                stat *= level;
                this.attackPower = stat;
            }
        }
        // This is armor stat
        else if( getItemID() >= 2 && getItemID() <= 6 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(6) + 1;
                stat *= level;
                this.defencePower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(10) + 3;
                stat *= level;
                this.defencePower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(15) + 7;
                stat *= level;
                this.defencePower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(20) + 12;
                stat *= level;
                this.defencePower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 30 ) + 20;
                stat *= level;
                this.defencePower = stat;
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
    public void assignStatsForAll( int level ) {
        Random randomNumber = new Random();
        int stat;

        this.attackPower = 0;
        this.defencePower = 0;

        // This is weapon stat
        if( getItemID() <= 12 && getItemID() >= 9 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(3) + 1;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(6) + 2;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(12) + 6;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(18) + 10;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 24 ) + 20;
                stat *= level;
                this.attackPower = stat;
            }
        }
        // This is armor stat
        else if ( getItemID() <= 12 && getItemID() >= 9 ) {
            if( getRarity() == 0 ) {
                stat = randomNumber.nextInt(3) + 1;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 1 ) {
                stat = randomNumber.nextInt(6) + 2;
                stat *= level;
                this.attackPower = stat;
            }
            else  if( getRarity() == 2 ) {
                stat = randomNumber.nextInt(12) + 6;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 3 ) {
                stat = randomNumber.nextInt(18) + 10;
                stat *= level;
                this.attackPower = stat;
            }
            else if( getRarity() == 4 ) {
                stat = randomNumber.nextInt( 24 ) + 20;
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
        Random randomPicture = new Random();
            // Holds Class armor specifics in sprite sheet
        int[] classID = { 3, 5, 0, 9 };
            
        try {
            SpriteSheet armor = new SpriteSheet("NewEra-Beta/res/items/armorSprite.png", 32, 32);
            SpriteSheet ringAndNecklace = new SpriteSheet("NewEra-Beta/res/items/RingAndNeck.png", 32, 32);

            switch ( getItemID() ) {
                case 0:
                    this.imageItem = null;
                    break;
                case 1:
                    this.imageItem = null;
                    break;
                    // Helmet
                case 2:
                    this.imageItem = armor.getSubImage( randomPicture.nextInt(10) , 1).copy();

                    break;
                    // Body
                case 3:
                    if( this.classID == 0 ) {
                        armor.startUse();
                        this.imageItem = armor.getSubImage( randomPicture.nextInt(10), 3).copy();
                        armor.endUse();
                    }
                    else if( this.classID == 1 ) {
                        armor.startUse();
                        this.imageItem = armor.getSubImage(randomPicture.nextInt(10), 5).copy();
                        armor.endUse();
                    }
                    else if( this.classID == 2 ) {
                        armor.startUse();
                        this.imageItem = armor.getSubImage(randomPicture.nextInt(9), 0).copy();
                        armor.endUse();
                    }
                    else if( this.classID == 3 ) {
                        armor.startUse();
                        this.imageItem = armor.getSubImage( randomPicture.nextInt(9), 4).copy();
                        armor.endUse();
                    }
                    else if( this.classID == 4 ) {
                        armor.startUse();
                        this.imageItem = armor.getSubImage( randomPicture.nextInt(9), classID[randomPicture.nextInt(3)] ).copy();
                        armor.endUse();
                    }
                    break;
                case 4:
                    this.imageItem = new Image("NewEra-Beta/res/items/template.png");
                    break;
                case 5:
                    armor.startUse();
                    this.imageItem = armor.getSubImage( randomPicture.nextInt(6), 6 ).copy();
                    armor.endUse();
                    break;
                case 6:
                    armor.startUse();
                    this.imageItem = armor.getSubImage(randomPicture.nextInt(5), 2).copy();
                    armor.endUse();
                    break;
                case 7:
                    this.imageItem = ringAndNecklace.getSubImage( randomPicture.nextInt( 8 ), 0 ).copy();
                    break;
                case 8:
                    this.imageItem = ringAndNecklace.getSubImage( randomPicture.nextInt( 8 ), 1 ).copy();
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


