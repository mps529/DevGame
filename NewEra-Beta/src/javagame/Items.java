package javagame;


import org.newdawn.slick.Color;
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

    private String name;

        // How rare the item is
        // 0 - Common, 1 - Uncommon, 2 - Rare, 3 - Legendary, 4 - God
    private int rarity;

        // Grey - Common, Orange - Uncommon, Blue - Rare, Purple - Legendary, Yellow - God
    Color itemRarityColor;

        // Pass -1 in if you don't want to set it
    public Items( int itemId, int classID, int rarity  ) {

        Random randomNumber = new Random();

        if( itemId != -1 ) {
            this.itemID = randomNumber.nextInt( 13 );
        }
        else {
            this.itemID = itemId;
        }
        if( this.itemID != 0 && this.itemID != 1 ) {

            if (classID != -1) {
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

            if (rarity != -1) {
                assignRarity(randomNumber);
                assignColor();
            } else {
                this.rarity = rarity;
                assignColor();
            }
        }
        nameItem();
    }

    public Items() {
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
    }

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
                break;
                // Uncommon
            case 1:
                this.itemRarityColor = new Color( 255,165,0, .5f );
                break;
                // Rare
            case 2:
                this.itemRarityColor = new Color( 0,0,255, .5f );
                break;
                // Legendary
            case 3:
                this.itemRarityColor = new Color( 255,0,255, .5f );
                break;
                // God
            case 4:
                this.itemRarityColor = new Color( 255,255,0, .5f );
                break;
        }

    }

    public void nameItem() {
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
                    this.name += "Necklaces ";
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

    public void nameItem( String name ) {
        this.name = name;
    }

    public void increaseAndAssignID() {
        this.classID = ItemsCount;
    }
}


