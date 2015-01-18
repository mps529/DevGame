package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class InventoryScreen extends BasicGameState{

        // singelton for Inventory
    private Inventory playerInventory;
        // Background
    private TiledMap inventoryMap;
        // Where the mouse is at
    private int mouseX, mouseY;
        // ID of locations
    private int[] equippedItems;
    private int[] inventoryItems;
        // This will hold the item that the user is displaying
    private Items displayItem;

        // This game state
    private static int gameState;

    public InventoryScreen( int gameState ) {
        this.gameState = gameState;
    }


    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        playerInventory = playerInventory.getPlayerInvintory();

        inventoryMap = new TiledMap( "NewEra-Beta/res/map/Inventory2.tmx" );

            /*
                This holds all the equiped items,
                ORDER: head, body, legs, feet, gloves, weapon, rings, necklace
            */
        equippedItems = new int[ 8 ];
            // This holds the order of what is in your inventory.
        inventoryItems = new int[ 21 ];


    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        inventoryMap.render( 0,0 );
        playerInventory.renderInventory( g, equippedItems, inventoryItems );

        g.setColor( Color.black );
        g.drawString( playerInventory.getPlayerName(), 360, 80 );
        g.drawString( "Attack:  "+ playerInventory.getPlayerOverallAttack(), 360, 100 );
        g.drawString( "Defence: " + playerInventory.getPlayerOverallDefence(), 360, 120 );
        g.drawString( "Money: " + playerInventory.getMoney(), 360, 140 );

        if( displayItem != null ) {
                // Name
            g.setColor( displayItem.getItemRarityColorNoAlpha() );
            g.drawString( displayItem.getName() , 360, 180);
            g.setColor(Color.black );
                // Class
            int classID = displayItem.getClassID();
            if( classID == 0 ) {
                g.drawString("Class: Hunter", 360, 200);
            }
            else if( classID == 1 ) {
                g.drawString("Class: Warrior", 360, 200);
            }
            else if( classID == 2 ) {
                g.drawString("Class: Mage", 360, 200);
            }
            else if( classID == 3 ) {
                g.drawString("Class: Rouge", 360, 200);
            }
            else if( classID == 4 ) {
                g.drawString("Class: Any", 360, 200);
            }

            g.drawString( "Attack Bonus:  " + displayItem.getAttackPower() , 360, 220);
            g.drawString( "Defence Bonus: " + displayItem.getDefencePower() , 360, 240);

        }

        g.drawString("X: " + mouseX + ", Y: " + mouseY, 410, 50);

    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        mouseX = input.getMouseX();
        mouseY = input.getMouseY();

        if( input.isKeyPressed( Input.KEY_F ) ) {
            sbg.enterState( 1 );
        }
            // HEAD
        if( (mouseX >= 112 && mouseX <= 144) && ( mouseY >= 80 && mouseY <= 112 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[0] );
            }
        }
            // BODY
        if( (mouseX >= 112 && mouseX <= 144) && ( mouseY >= 144 && mouseY <= 176 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[1] );
            }
        }
            // LEGS
        if( (mouseX >= 112 && mouseX <= 144) && ( mouseY >= 208 && mouseY <= 240 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[2] );
            }
        }
            // BOOTIES
        if( (mouseX >= 112 && mouseX <= 144) && ( mouseY >= 272 && mouseY <= 304 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[3] );
            }
        }
            // GLOVES
        if( (mouseX >= 48 && mouseX <= 80) && ( mouseY >= 144 && mouseY <= 176 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[4] );
            }
        }
            // WEAPON
        if( (mouseX >= 176 && mouseX <= 208) && ( mouseY >= 144 && mouseY <= 176 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[5] );
            }
        }
            // RING
        if( (mouseX >= 208 && mouseX <= 240) && ( mouseY >= 48 && mouseY <= 80 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[6] );
            }
        }
            // NECKLACE
        if( (mouseX >= 272 && mouseX <= 304) && ( mouseY >= 48 && mouseY <= 80 ) ) {
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) {
                displayItem = playerInventory.getItemByID( equippedItems[7] );
            }
        }

        int xPos, yPos;
        for( int x = 0; x < inventoryItems.length; x++ ) {

        }


    }

    public int getID( ) {
        return gameState;
    }

}
