package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class InventoryScreen extends BasicGameState{

        // singelton for Inventory
    private Player player;

        //inventory object
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

        // This is to draw a square around the selected Item
    private int selectedX, selectedY;

        // What is selected
    private boolean equipedItemSelected;
    private boolean inventoryItemSelected;
    private boolean drop;

        // Button Images
    private Image dropItem;
    private Image equipItem;
    private Image unEquipItem;
    private Image confirm;
    private Image rename;

        // This holds the time the player has until the drop
        // option disappears
    private double timeOut;
    private double messageTimeOut;

        // These are the returns for equipped and unequipped
    private int unEquipSuccess;
    private int equipSuccess;

        // Text Field for new Name
    private TextField newName;
        // Boolean for if setting new name
    private boolean settingName;

        // This game state
    private static int gameState;

    public InventoryScreen( int gameState ) {
        this.gameState = gameState;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

            // Inventory
        this.player = this.player.getInstance();

        this.playerInventory = this.player.getInventory();

        // Inventory Background
        this.inventoryMap = new TiledMap( "NewEra-Beta/res/map/Inventory2.tmx" );

            // Setting inline textfield listener
        this.newName = new TextField( gc, gc.getDefaultFont(),352, 72, 256, 24, new ComponentListener()
        {
            public void componentActivated(AbstractComponent source) {
                displayItem.setName( newName.getText() );
                newName.setFocus(true);
            }

        });

            // Booleans for display
        this.settingName = false;
        this.equipedItemSelected = false;
        this.inventoryItemSelected = false;
        this.drop = false;

        this.unEquipSuccess = 0;
        this.equipSuccess = 0;

            // timeout for drop item
        this.timeOut = 0;
        this.messageTimeOut = 0;

            // Buttons
        this.dropItem = new Image( "NewEra-Beta/res/buttons/DropItem.png" );
        this.equipItem = new Image( "NewEra-Beta/res/buttons/EquipItem.png" );
        this.unEquipItem = new Image( "NewEra-Beta/res/buttons/unEquipItem.png" );
        this.confirm = new Image( "NewEra-Beta/res/buttons/confirm.png" );
        this.rename = new Image( "NewEra-Beta/res/buttons/rename.png" );

            /*
                This holds all the equiped items,
                ORDER: head, body, legs, feet, gloves, weapon, rings, necklace
            */
        this.equippedItems = new int[ 8 ];
            // This holds the order of what is in your inventory.
        this.inventoryItems = new int[ 21 ];
    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        this.inventoryMap.render( 0,0 );

        this.playerInventory.renderInventory( g, this.equippedItems, this.inventoryItems );

        g.setColor( Color.black );
        g.drawString( "Gold: " + this.playerInventory.getMoney(), 355, 40 );

            // If item is selected
        if( this.displayItem != null ) {
                // Name
            g.setColor( Color.lightGray );
            g.fillRoundRect(352, 72, 256, 24, 10);
            if( !this.settingName ) {
                g.setColor(this.displayItem.getItemRarityColorNoAlpha());
                g.drawString(this.displayItem.getName(), 355, 75);
            }
            g.setColor(Color.black );
                // Class
            int classID = this.displayItem.getClassID();
            if( classID == 0 ) {
                g.drawString("Class: Hunter", 360, 100);
            }
            else if( classID == 1 ) {
                g.drawString("Class: Warrior", 360, 100);
            }
            else if( classID == 2 ) {
                g.drawString("Class: Wizard", 360, 100);
            }
            else if( classID == 3 ) {
                g.drawString("Class: Rogue", 360, 100);
            }
            else if( classID == 4 ) {
                g.drawString("Class: Any", 360, 100);
            }

            g.drawString( "Attack Bonus:  " + this.displayItem.getAttackPower() , 360, 120);
            g.drawString( "Defence Bonus: " + this.displayItem.getDefencePower() , 360, 140);
            g.drawString( "Item Value: " + this.displayItem.getWorth() , 360, 160);
            // Add worth

            g.setColor( Color.white );
            g.drawRect( this.selectedX, this.selectedY, 32, 32 );
            this.unEquipSuccess = 0;
            this.equipSuccess = 0;
            this.messageTimeOut = 0;
        }
        else if ( unEquipSuccess == 1 ) {
            g.setColor(Color.black);
            g.drawString("Item Unequipped.", 355, 75);
        }
        else if( unEquipSuccess == 2 ) {
            g.setColor(Color.black );
            g.drawString( "Inventory is full, please remove item before unequipped", 355, 75);
        }
        else if( this.equipSuccess == 1 ) {
            g.setColor(Color.black);
            g.drawString("Item Equipped.", 355, 75);
        }
        else if( this.equipSuccess == 2 ) {
            g.setColor(Color.black );
            g.drawString( "Unable to equip item.", 355, 75);
        }

        if( this.equipedItemSelected ){
            this.unEquipItem.draw( 352, 256 );
            this.rename.draw( 448, 256 );
            if( !this.drop ){
                this.dropItem.draw( 544, 256 );
            }
            else {
                this.confirm.draw( 544, 256 );
            }
        }
        else if( this.inventoryItemSelected ) {
            this.equipItem.draw( 352, 256 );
            this.rename.draw( 448, 256 );
            if( !this.drop ){
                this.dropItem.draw( 544, 256 );
            }
            else {
                this.confirm.draw( 544, 256 );
            }
        }

        if( this.settingName ) {
            newName.render( gc, g );
            newName.setFocus( true );
        }

        //g.drawString( "X: " + this.mouseX + ", Y: " + this.mouseY, 500, 100 );
    }

    public void enter( GameContainer gc, StateBasedGame sbg ) {this.playerInventory = this.player.getInventory();}


    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {

        Input input = gc.getInput();

        boolean spotSelected =false;

            // Mouse X & Y
        this.mouseX = input.getMouseX();
        this.mouseY = input.getMouseY();

        if( !this.settingName ) {

            // 'I' will return to game
            if (input.isKeyPressed(Input.KEY_I) || input.isKeyPressed(Input.KEY_ESCAPE) ) {
                this.player.setOverallAttack( playerInventory.getPlayerOverallAttack() );
                this.player.setOverallDefence( playerInventory.getPlayerOverallDefence() );
                input.clearKeyPressedRecord();
                sbg.enterState(1);
            }
            // HEAD
            if ((this.mouseX >= 112 && this.mouseX <= 144) && (this.mouseY >= 80 && this.mouseY <= 112)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = playerInventory.getItemByID(this.equippedItems[0]);
                    this.selectedX = 112;
                    this.selectedY = 80;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // BODY
            if ((this.mouseX >= 112 && this.mouseX <= 144) && (this.mouseY >= 144 && this.mouseY <= 176)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = this.playerInventory.getItemByID(this.equippedItems[1]);
                    this.selectedX = 112;
                    this.selectedY = 144;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // LEGS
            if ((this.mouseX >= 112 && this.mouseX <= 144) && (this.mouseY >= 208 && this.mouseY <= 240)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = this.playerInventory.getItemByID(this.equippedItems[2]);
                    this.selectedX = 112;
                    this.selectedY = 208;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // BOOTIES
            else if ((this.mouseX >= 112 && this.mouseX <= 144) && (this.mouseY >= 272 && this.mouseY <= 304)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = this.playerInventory.getItemByID(this.equippedItems[3]);
                    this.selectedX = 112;
                    this.selectedY = 272;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // GLOVES
            else if ((this.mouseX >= 48 && this.mouseX <= 80) && (this.mouseY >= 144 && this.mouseY <= 176)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = playerInventory.getItemByID(this.equippedItems[4]);
                    this.selectedX = 48;
                    this.selectedY = 144;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // WEAPON
            else if ((this.mouseX >= 176 && this.mouseX <= 208) && (this.mouseY >= 144 && this.mouseY <= 176)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = this.playerInventory.getItemByID(this.equippedItems[5]);
                    this.selectedX = 176;
                    this.selectedY = 144;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // RING
            else if ((this.mouseX >= 208 && this.mouseX <= 240) && (this.mouseY >= 48 && this.mouseY <= 80)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = this.playerInventory.getItemByID(this.equippedItems[6]);
                    this.selectedX = 208;
                    this.selectedY = 48;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }
            // NECKLACE
            else if ((this.mouseX >= 272 && this.mouseX <= 304) && (this.mouseY >= 48 && this.mouseY <= 80)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = playerInventory.getItemByID(this.equippedItems[7]);
                    this.selectedX = 272;
                    this.selectedY = 48;
                    this.equipedItemSelected = true;
                    this.inventoryItemSelected = false;
                    spotSelected = true;
                }
            }

            // Checks what is in the inventory
            int xPos = 112, yPos = 464;
            for (int x = 0; x < this.playerInventory.getInventoryCount(); x++) {
                if ((this.mouseX >= xPos && this.mouseX <= xPos + 32) && (this.mouseY >= yPos && this.mouseY <= yPos + 32)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.displayItem = this.playerInventory.getItemByID(this.inventoryItems[x]);
                        x = this.inventoryItems.length;
                        this.selectedX = xPos;
                        this.selectedY = yPos;
                        this.equipedItemSelected = false;
                        this.inventoryItemSelected = true;
                        spotSelected = true;
                    }
                }
                xPos += 64;
                if (xPos > 496) {
                    xPos = 112;
                    yPos += 64;
                }
            }

            // If Item was selected
            if (this.equipedItemSelected) {
                if ((this.mouseX >= 352 && this.mouseX <= 416) && (this.mouseY >= 256 && this.mouseY <= 320)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        unEquipSuccess = this.playerInventory.unEquipItem(this.displayItem.getID());
                        this.equipedItemSelected = false;
                        this.displayItem = null;
                        spotSelected = true;
                    }
                } else if ((this.mouseX >= 544 && this.mouseX <= 608) && (this.mouseY >= 256 && this.mouseY <= 320) && !drop ) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.drop = true;
                        spotSelected = true;
                    }
                } else if ((this.mouseX >= 448 && this.mouseX <= 512) && (this.mouseY >= 256 && this.mouseY <= 320)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.settingName = true;
                        this.newName.setText( displayItem.getName() );
                        spotSelected = true;
                    }
                }

            } else if (this.inventoryItemSelected) {
                if ((this.mouseX >= 352 && this.mouseX <= 416) && (this.mouseY >= 256 && this.mouseY <= 320)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.equipSuccess = this.playerInventory.equipItem(displayItem.getID());
                        this.inventoryItemSelected = false;
                        this.displayItem = null;
                        spotSelected = true;
                    }
                }
                else if (((this.mouseX >= 544 && this.mouseX <= 608) && (this.mouseY >= 256 && this.mouseY <= 320)) && !drop) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.drop = true;
                        spotSelected = true;
                    }
                } else if ((this.mouseX >= 448 && this.mouseX <= 512) && (this.mouseY >= 256 && this.mouseY <= 320)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.settingName = true;
                        this.newName.setText( displayItem.getName() );
                        spotSelected = true;
                    }
                }
            }

            // Drop Time out
            if (this.timeOut > 500) {
                this.drop = false;
                this.timeOut = 0;
            }
            if( this.unEquipSuccess != 0 ||  this.equipSuccess != 0 ) {
                this.messageTimeOut += delta*.1f;
            }
            if( this.messageTimeOut > 500 ) {
                this.messageTimeOut = 0;
                this.unEquipSuccess = 0;
                this.equipSuccess = 0;
            }

            if (this.drop) {
                if ((this.mouseX >= 512 && this.mouseX <= 608) && (this.mouseY >= 256 && this.mouseY <= 320)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.playerInventory.dropItem(this.displayItem.getID());
                        this.equipedItemSelected = false;
                        this.inventoryItemSelected = false;
                        this.displayItem = null;
                        this.timeOut = 0;
                        this.drop = false;
                        spotSelected = true;
                    }
                }
                this.timeOut += delta * .1f;
            }
        }
        else {
            if( input.isKeyPressed( Input.KEY_ENTER ) ) {
                this.settingName = false;
                this.newName.setText("");
                this.newName.deactivate();
                spotSelected = true;
            }
        }

        if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) && !spotSelected ) {
            this.equipedItemSelected = false;
            this.inventoryItemSelected = false;
            this.displayItem = null;
            this.timeOut = 0;
            this.drop = false;
        }

    }

    public int getID( ) {
        return this.gameState;
    }

}
