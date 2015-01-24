package javagame;

import org.newdawn.slick.SlickException;

public class PlayerChoices {

        // Singleton
    private static PlayerChoices playerChoices = null;

        // Player classes
    private Hunter hunter = null;
    private Wizard wizard = null;
    private Warrior warrior = null;
    private Rouge rouge = null;

    private int playerClass;

    public PlayerChoices() {

    }

    public PlayerChoices( int playerClass, String sheetName, String name ) {
        try {
            switch (playerClass) {
                case 0:
                    hunter = new Hunter( sheetName, name );
                    break;
                case 1:
                    warrior = new Warrior( sheetName, name );
                    break;
                case 2:
                    wizard = new Wizard( sheetName, name );
                    break;
                case 3:
                    rouge = new Rouge( sheetName, name );
                    break;
            }
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }

        this.playerClass = playerClass;
    }

    public PlayerChoices getPlayerChoicesInstance( ) {
        return this.playerChoices;
    }

    public PlayerChoices setPlayerChoicesInstance( int playerClass, String sheetName, String name ) {
        this.playerChoices = new PlayerChoices( playerClass, sheetName, name );
        return this.playerChoices;
    }

        // Returns PlayerClass
    public Object getPlayerClass() {
        switch ( this.playerClass ) {
            case 0:
                return hunter;
            case 1:
                return warrior;
            case 2:
                return wizard;
            case 3:
                return rouge;
            default:
                return null;
        }
    }

}
