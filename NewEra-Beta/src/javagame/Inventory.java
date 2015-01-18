package javagame;

public class Inventory {

    private static Inventory playerInvintory = null;

        // Player Class
    private int classID;


    public Inventory( ) {

    }

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

}
