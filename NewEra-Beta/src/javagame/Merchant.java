package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.Timer;


public class Merchant extends BasicGameState {

    private static int gameState;

    private Player player;

    private Inventory shopStock;

    private Items displayItem;
    private int itemVal;

    private int selectedX, selectedY;

    private TiledMap shopMenuBuy, shopMenuSell;

    private Image increaseBtn, decreaseBtn, buyBtn, sellBtn, switchModeBtn;
    private Image increaseBtnPressed, decreaseBtnPressed, buyBtnPressed, sellBtnPressed, switchModeBtnPressed;

    private Image healthPotion, staminaPotion;

    private Sound menuSwap;

    private int quantity;
    private int mouseX, mouseY;

    //gui booleans
    private boolean overPlusBtn, overMinusBtn, confirmBtn, switchMenu;
    private boolean shopItemSelected, inventoryItemSelected, potionSelected;
    private boolean healthPotionSelected, staminaPotionSelected;
    private boolean purchaseSuccessful, purchaseUnsuccessful;
    private boolean isBuying;

    private int[] shopItems;

    private boolean settingName;


    public Merchant( int state ) {
        this.gameState = state;
    }

    public int getID( ) {
        return gameState;
    }

    public void enter( GameContainer gc, StateBasedGame sbg) {
        this.player = this.player.getInstance();
        this.shopStock = new Inventory();
        this.shopStock.addShopItems(this.player.getLevel(), this.player.getPlayerClass());
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {


        this.shopMenuBuy = new TiledMap("NewEra-Beta/res/map/MerchantMenu.tmx");
        this.shopMenuSell = new TiledMap("NewEra-Beta/res/map/MerchantMenuSell.tmx");

        //buttons
        this.increaseBtn = new Image( "NewEra-Beta/res/buttons/plus_white.png" );
        this.increaseBtnPressed = new Image( "NewEra-Beta/res/buttons/plus_orange.png" );
        this.decreaseBtn = new Image( "NewEra-Beta/res/buttons/minus_white.png" );
        this.decreaseBtnPressed = new Image( "NewEra-Beta/res/buttons/minus_orange.png" );
        this.buyBtn = new Image( "NewEra-Beta/res/buttons/BuyButton.png" );
        this.buyBtnPressed = new Image( "NewEra-Beta/res/buttons/BuyButtonPressed.png");
        this.sellBtn = new Image( "NewEra-Beta/res/buttons/SellButton.png" );
        this.sellBtnPressed = new Image( "NewEra-Beta/res/buttons/SellButtonPressed.png" );
        this.switchModeBtn = new Image( "NewEra-Beta/res/buttons/BuySellSwitch.png" );
        this.switchModeBtnPressed = new Image( "NewEra-Beta/res/buttons/BuySellSwitchPressed.png" );

        //potions for sell menu
        this.healthPotion = new Image( "NewEra-Beta/res/items/health.png" );
        this.staminaPotion = new Image( "NewEra-Beta/res/items/stamina.png" );

        menuSwap = new Sound("NewEra-Beta/res/sounds/switchBuySell.ogg");


        //quantity being purchased
        this.quantity = 0;

        //boolean to flip between buy and sell menus
        this.isBuying = true;

        //quantity booleans
        this.overPlusBtn = false;
        this.overMinusBtn = false;

        this.quantity = 0;

        this.shopItems = new int[ 21 ];
        this.shopStock = new Inventory();

        this.settingName = false;
        this.healthPotionSelected = false;
        this.staminaPotionSelected = false;
        this.purchaseSuccessful = false;
        this.purchaseUnsuccessful = false;




    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {



        if(isBuying) { //buy mode
            this.shopMenuBuy.render( 0, 0 );

            if(!switchMenu) {
                this.switchModeBtn.draw(480, 32, 96, 64);
            } else {
                this.switchModeBtnPressed.draw(480, 32, 96, 64);
            }

            if(!confirmBtn) {
                this.buyBtn.draw(448, 352, 96, 64);
            } else {
                this.buyBtnPressed.draw(448, 352, 96, 64);
            }
            g.setColor(Color.black);
            g.drawString("Gold: " + this.player.getInventory().getMoney(), 128, 40);
            g.drawString("(R)Buy Item", 96, 432);

            if(!overPlusBtn) {
                this.increaseBtn.draw(320, 352);
            } else {
                this.increaseBtnPressed.draw(320, 352);
            }
            if(!overMinusBtn) {
                this.decreaseBtn.draw(288, 352);
            } else {
                this.decreaseBtnPressed.draw(288, 352);
            }


            this.healthPotion.draw(112, 464);
            this.staminaPotion.draw(176, 464);
            this.shopStock.renderShopStock(g, shopItems);

            // If item is selected
            if( this.displayItem != null ) {

                g.setColor(Color.black);
                g.drawString("" + quantity, 312, 410);
                if(!potionSelected) {
                    g.setColor(new Color( 0, 0, 0, .3f ));
                    g.fillRect(304, 400, 32, 32);
                }
                if(this.displayItem.getID() == 0 || this.displayItem.getID() == 1) {
                    g.setColor(Color.black);

                    if(this.displayItem.getID() == 0) {
                        g.drawString("Health Potion", 160, 104);
                        g.drawString("Item Value: " + this.itemVal, 160, 120);
                    } else if(this.displayItem.getID() ==1) {
                        g.drawString("Health Potion", 160, 104);
                        g.drawString("Item Value: " + this.itemVal, 160, 120);
                    }

                }else {
                    g.setColor(Color.lightGray);
                    g.fillRoundRect(160, 96, 256, 24, 10);
                    if (!this.settingName) {
                        g.setColor(this.displayItem.getItemRarityColorNoAlpha());
                        g.drawString(this.displayItem.getName(), 160, 104);
                    }
                    g.setColor(Color.black);
                    // Class
                    int classID = this.displayItem.getClassID();
                    if (classID == 0) {
                        g.drawString("Class: Hunter", 160, 120);
                    } else if (classID == 1) {
                        g.drawString("Class: Warrior", 160, 120);
                    } else if (classID == 2) {
                        g.drawString("Class: Wizard", 160, 120);
                    } else if (classID == 3) {
                        g.drawString("Class: Rogue", 160, 120);
                    } else if (classID == 4) {
                        g.drawString("Class: Any", 160, 120);
                    }

                    g.drawString("Attack Bonus:  " + this.displayItem.getAttackPower(), 160, 136);
                    g.drawString("Defence Bonus: " + this.displayItem.getDefencePower(), 160, 152);
                    g.drawString("Item Value: " + this.itemVal, 160, 168);
                    // Add worth
                }

                g.setColor(Color.white);
                g.drawRect( this.selectedX, this.selectedY, 32, 32 );

                if (purchaseSuccessful) {
                    g.setColor(Color.green);
                    g.drawString("Purchase Successful", 144, 304);
                } else if (purchaseUnsuccessful) {
                    g.setColor(Color.red);
                    g.drawString("Not enough gold/inventory space", 144, 304);
                }


            }



        } else { //sell mode
            this.shopMenuSell.render(0, 0);

            if(!switchMenu) {
                this.switchModeBtn.draw(480, 32, 96, 64);
            } else {
                this.switchModeBtnPressed.draw(480, 32, 96, 64);
            }

            if(!confirmBtn) {
                this.sellBtn.draw(448, 352, 96, 64);
            } else {
                this.sellBtnPressed.draw(448, 352, 96, 64);
            }

            g.setColor(Color.black);
            g.drawString("Gold: " + this.player.getInventory().getMoney(), 128, 40);
            g.drawString("(R)Sell Item", 96, 432);

            this.player.getInventory().renderSellMenu(g, shopItems);

            if( this.displayItem != null ) {


                g.setColor(Color.lightGray);
                g.fillRoundRect(160, 96, 256, 24, 10);
                if (!this.settingName) {
                    g.setColor(this.displayItem.getItemRarityColorNoAlpha());
                    g.drawString(this.displayItem.getName(), 160, 104);
                }
                g.setColor(Color.black);
                // Class
                int classID = this.displayItem.getClassID();
                if (classID == 0) {
                    g.drawString("Class: Hunter", 160, 120);
                } else if (classID == 1) {
                    g.drawString("Class: Warrior", 160, 120);
                } else if (classID == 2) {
                    g.drawString("Class: Wizard", 160, 120);
                } else if (classID == 3) {
                    g.drawString("Class: Rogue", 160, 120);
                } else if (classID == 4) {
                    g.drawString("Class: Any", 160, 120);
                }
                g.drawString("Attack Bonus:  " + this.displayItem.getAttackPower(), 160, 136);
                g.drawString("Defence Bonus: " + this.displayItem.getDefencePower(), 160, 152);
                g.drawString("Item Value: " + this.itemVal, 160, 168);
                g.setColor(Color.white);
                g.drawRect(this.selectedX, this.selectedY, 32, 32);
            }




        }



    }

    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = gc.getInput();
        this.mouseX = input.getMouseX();
        this.mouseY = input.getMouseY();
        int xPos, yPos;

        boolean spotSelected = false;

        if (input.isKeyPressed(Input.KEY_COMMA) || input.isKeyPressed(Input.KEY_E)) {
            input.clearKeyPressedRecord();
            this.shopItemSelected = false;
            this.displayItem = null;
            this.potionSelected = false;
            this.itemVal = 0;
            this.healthPotionSelected = false;
            this.staminaPotionSelected = false;
            this.purchaseUnsuccessful = false;
            this.purchaseSuccessful = false;
            this.isBuying = true;
            sbg.enterState(1);
        }
        //mouse over sell/buy mode button
        if ((this.mouseX >= 480 && this.mouseX <= 480 + 96) && (this.mouseY >= 32 && this.mouseY <= 32 + 64)) {
            this.switchMenu = true;
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
                this.shopItemSelected = false;
                this.displayItem = null;
                this.potionSelected = false;
                this.itemVal = 0;
                this.quantity = 1;
                if(isBuying) {
                    this.isBuying = false;
                } else {
                    this.isBuying = true;
                }
                this.menuSwap.play();
            }
        }  else {
            this.switchMenu = false;
        }


        if ((this.mouseX >= 288 && this.mouseX <= 288 + 32) && (this.mouseY >= 352 && this.mouseY <= 352 + 32)) {
            if(potionSelected)
                this.overMinusBtn = true;
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && potionSelected) {
                if (this.quantity > 0) {
                    this.quantity--;
                    this.itemVal -= this.displayItem.getWorth();
                }
            }
        }  else {
            this.overMinusBtn = false;
        }

        if ((this.mouseX >= 320 && this.mouseX <= 320 + 32) && (this.mouseY >= 352 && this.mouseY <= 352 + 32)) {
            if(potionSelected)
                this.overPlusBtn = true;
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && potionSelected) {
                this.quantity++;
                this.itemVal+= this.displayItem.getWorth();
            }
        }  else {
            this.overPlusBtn = false;
        }

        if(isBuying) { //buy mode

            //complete transaction
            if ((this.mouseX >= 448 && this.mouseX <= 448 + 96) && (this.mouseY >= 352 && this.mouseY <= 352 + 64)) {
                this.confirmBtn = true;
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
                    //calculate transaction total
                    int total = displayItem.getWorth() * this.quantity;

                    if(player.getInventory().getMoney() - total >= 0) {
                        if(!potionSelected) {
                            if(player.getInventory().addItem(displayItem)) {
                                this.purchaseSuccessful = true;
                                player.getInventory().subMoney(total);
                            }
                        } else {
                            player.getInventory().subMoney(total);
                            if(healthPotionSelected) {
                                player.getInventory().addHealthPotions(quantity);
                            }else if(staminaPotionSelected) {
                                player.getInventory().addStaminaPotions(quantity);
                            }
                            this.purchaseSuccessful = true;
                        }

                    }
                    else {
                        this.purchaseUnsuccessful = true;
                    }

                }
            }  else {
                this.confirmBtn = false;
            }
            //complete transaction shortcut
            if (input.isKeyPressed(Input.KEY_R)) {
                if (displayItem != null) {
                    //calculate transaction total
                    int total = displayItem.getWorth() * this.quantity;

                    if(player.getInventory().getMoney() - total >= 0) {
                        if(!potionSelected) {
                            player.getInventory().subMoney(total);
                            player.getInventory().addItem(displayItem);
                        } else {
                            player.getInventory().subMoney(total);
                            if(healthPotionSelected) {
                                player.getInventory().addHealthPotions(quantity);
                            }else if(staminaPotionSelected) {
                                player.getInventory().addStaminaPotions(quantity);
                            }

                        }
                        this.purchaseSuccessful = true;
                    }
                    else {
                        this.purchaseUnsuccessful = true;
                    }
                }
            }


            //select health potion
            if ((this.mouseX >= 112 && this.mouseX <= 112 + 32) && (this.mouseY >= 464 && this.mouseY <= 464 + 32)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = new Items();
                    this.displayItem.setWorth(5);
                    this.selectedX = 112;
                    this.selectedY = 464;
                    this.shopItemSelected = true;
                    this.potionSelected = true;
                    this.healthPotionSelected = true;
                    this.staminaPotionSelected = false;
                    this.quantity = 1;
                    this.itemVal = this.displayItem.getWorth();
                    spotSelected = true;

                }
            }//select stamina potion
            else if ((this.mouseX >= 176 && this.mouseX <= 176 + 32) && (this.mouseY >= 464 && this.mouseY <= 464 + 32)) {
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    this.displayItem = new Items();
                    this.displayItem.setWorth(5);
                    this.selectedX = 176;
                    this.selectedY = 464;
                    this.shopItemSelected = true;
                    this.potionSelected = true;
                    this.healthPotionSelected = false;
                    this.staminaPotionSelected = true;
                    this.quantity = 1;
                    this.itemVal = this.displayItem.getWorth();
                    spotSelected = true;
                }
            }


            xPos = 240;
            yPos = 464;
            for (int x = 0; x < this.shopStock.getInventoryCount(); x++) {
                if ((this.mouseX >= xPos && this.mouseX <= xPos + 32) && (this.mouseY >= yPos && this.mouseY <= yPos + 32)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.displayItem = shopStock.getItemByID(this.shopItems[x]);
                        x = this.shopItems.length;
                        this.quantity = 1;
                        this.selectedX = xPos;
                        this.selectedY = yPos;
                        this.shopItemSelected = true;
                        this.potionSelected = false;
                        this.purchaseSuccessful = false;
                        this.purchaseUnsuccessful = false;
                        this.itemVal = this.displayItem.getWorth();
                        spotSelected = true;
                        break;
                    }
                }
                xPos += 64;
                if (xPos > 496) {
                    xPos = 112;
                    yPos += 64;
                }
            }
            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) && !spotSelected ) {
                this.shopItemSelected = false;
                this.displayItem = null;
                this.potionSelected = false;
                this.itemVal = 0;
            }

        } else { //sell mode



            //complete transaction
            if ((this.mouseX >= 448 && this.mouseX <= 448 + 96) && (this.mouseY >= 352 && this.mouseY <= 352 + 64)) {
                this.confirmBtn = true;
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
                    //calculate transaction total
                    int total = displayItem.getWorth();
                    player.getInventory().addMoney(total);
                    player.getInventory().dropItem(displayItem.getID());
                    this.displayItem = null;
                }
            }  else {
                this.confirmBtn = false;
            }
            //complete transaction shortcut
            if (input.isKeyPressed(Input.KEY_R)) {
                if(displayItem != null) {
                    input.clearKeyPressedRecord();
                    int total = displayItem.getWorth();
                    player.getInventory().addMoney(total);
                    player.getInventory().dropItem(displayItem.getID());
                    this.displayItem = null;
                }

            }

            xPos = 112;
            yPos = 464;
            for (int x = 0; x < this.player.getInventory().getInventoryCount(); x++) {
                if ((this.mouseX >= xPos && this.mouseX <= xPos + 32) && (this.mouseY >= yPos && this.mouseY <= yPos + 32)) {
                    if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                        this.displayItem = player.getInventory().getItemByID(this.shopItems[x]);
                        x = this.shopItems.length;
                        this.quantity = 1;
                        this.selectedX = xPos;
                        this.selectedY = yPos;
                        this.shopItemSelected = true;
                        this.potionSelected = false;
                        this.itemVal = this.displayItem.getWorth();
                        spotSelected = true;
                        break;
                    }
                }
                xPos += 64;
                if (xPos > 496) {
                    xPos = 112;
                    yPos += 64;
                }
            }

            if( input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) && !spotSelected ) {
                this.shopItemSelected = false;
                this.displayItem = null;
                this.potionSelected = false;
                this.itemVal = 0;
            }
        }


    }
}
