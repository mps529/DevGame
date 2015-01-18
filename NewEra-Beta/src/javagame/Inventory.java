package javagame;

import java.util.Vector;

public class Inventory {

        /*
            Since there is no data able to be passed
             between states, I created a singleton. This
             makes data transfer between states very easy
         */
    private static Inventory playerInvintory = null;

    private static int MAX_ITEMS_IN_INVENTORY = 20;

    private int money;

    /*
        Add equiped items by ID
    */


        // Player Class
        // 0-Hunter, 1-Warrior, 2-Mage, 3-Rouge
    private int classID;

    Vector<Items> itemList;


    public Inventory( ) {

        this.itemList = new Vector<Items>();
        this.money = 0;

    }
        /*
            Call this function to get a copy of the class between states.
            Do NOT make a new Inventory, this function handles if it needs
            to be created or not.
        */
    public static Inventory getPlayerInvintory( ) {
        if( playerInvintory == null ) {
            playerInvintory = new Inventory( );
        }
        return playerInvintory;
    }

    public void setClassID( int classID ) {
        this.classID = classID;
    }
    public int getClassID() {
        return this.classID;
    }

    public int getItemCount() { return this.itemList.capacity(); }

    public void addMoney( int money ) { this.money += money; }
    public void subMoney( int money ) { this.money -= money; }
    public int getMoney() { return this.money; }

    public void addItem( Items item  ) {
        this.itemList.addElement( item );
    }

    public void addStartingItems( int classID ) {
        Items[] basic = new Items[6];

        basic[ 0 ] = new Items( 2, classID, 0 );
        basic[ 1 ] = new Items( 3, classID, 0 );
        basic[ 2 ] = new Items( 4, classID, 0 );
        basic[ 3 ] = new Items( 5, classID, 0 );
        basic[ 4 ] = new Items( 6, classID, 0 );

        if( classID == 0 ) {
            basic[ 5 ] = new Items( 10, classID, 0 );
        }
        else if( classID == 1 ) {
            basic[ 5 ] = new Items( 9, classID, 0 );
        }
        else if( classID == 2 ) {
            basic[ 5 ] = new Items( 12, classID, 0 );
        }
        else if( classID == 3 ) {
            basic[ 5 ] = new Items( 11, classID, 0 );
        }

    }

}
