package javagame;

import com.thoughtworks.xstream.XStream;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.io.File;
import java.util.Random;


public class MainMenu extends BasicGameState {

    private int gameState;

    //music
    private Music theme;

    //buttons and menu items
    private Image title;
    private Image newGame, newGamePressed;
    private Image loadGame, loadGamePressed;
    private Image playGame, playGamePressed;
    private Image deleteSave, deleteSavePressed;
    private Image goBack;

    private Image warriorSprite, warrior2Sprite, warrior3Sprite, warrior4Sprite;
    private Image wizardSprite, wizard2Sprite, wizard3Sprite, wizard4Sprite;
    private Image rogueSprite, rogue2Sprite, rogue3Sprite, rogue4Sprite;
    private Image hunterSprite, hunter2Sprite, hunter3Sprite, hunter4Sprite;

    private SpriteSheet hunter, warrior, rogue, wizard;
    private TextField characterName;

    /////// player save and save slot
    private SaveGame sg;
    private int slotNo;

    private boolean slot1DidLoad, slot2DidLoad, slot3DidLoad, slot4DidLoad;

    private int slot1ClassId, slot2ClassId, slot3ClassId, slot4ClassId;
    private int slot1Level, slot2Level, slot3Level, slot4Level;
    private String slot1Name, slot2Name, slot3Name, slot4Name;
    //////

    private String playerName;
    private Player player;

    //map information
    private TiledMap map, newMenu, loadMenu;
    private float mapX, mapY;

    //map scroll values
    private boolean moveUp, moveDown, moveLeft, moveRight;
    private Random randNum;
    private double scrollSpeedX, scrollSpeedY;

    //menu logic
    private boolean newGameStarted, loadGameStarted, gameStarted, deletingSave;
    private boolean loadPressed, newPressed, playPressed, deletePressed;
    private boolean editingName;
    private boolean warriorSelected, wizardSelected, rogueSelected, hunterSelected, classSelected;
    private boolean sprite1Selected, sprite2Selected, sprite3Selected, sprite4Selected, spriteSelected;
    private boolean loadSlot1Selected, loadSlot2Selected, loadSlot3Selected, loadSlot4Selected, loadSlotSelected;
    private int selectedX, selectedY;
    private int spriteSelectedX, spriteSelectedY;
    private int slotSelectedX, slotSelectedY, slotWidth, slotHeight;

    private boolean firstTimeRunning;

    public MainMenu( int state ) {
        this.gameState = state;
    }

    public int getID( ) {
        return gameState;
    }

    public void enter ( GameContainer gc, StateBasedGame sbg ) {

        theme.loop();


            this.player.getInstance();
            newGameStarted = false;
            loadGameStarted = false;
            classSelected = false;
            loadSlotSelected = false;
            spriteSelected = false;
            warriorSelected = false;
            wizardSelected = false;
            rogueSelected = false;
            hunterSelected = false;

            sprite1Selected = false;
            sprite2Selected = false;
            sprite3Selected = false;
            sprite4Selected = false;
            characterName.setText("");
            editingName = false;
            gameStarted = false;

            loadSlot1Selected = false;
            loadSlot2Selected = false;
            loadSlot3Selected = false;
            loadSlot4Selected = false;


    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {


        this.loadGames();
        theme = new Music("NewEra-Beta/res/sounds/title_loop.ogg");
        theme.setVolume(0.02f);
        theme.loop();

        this.firstTimeRunning = true;

        //init title image
        this.title = new Image("NewEra-Beta/res/title/NEW-ERA.png");

        //init new game button
        this.newGame = new Image("NewEra-Beta/res/buttons/NewButton.png");
        this.newGamePressed = new Image("NewEra-Beta/res/buttons/NewButtonPressed.png");
        this.newPressed = false;

        //init load game button
        this.loadGame = new Image("NewEra-Beta/res/buttons/LoadButton.png");
        this.loadGamePressed = new Image("NewEra-Beta/res/buttons/LoadButtonPressed.png");
        this.loadPressed = false;

        // init delete button in load menu
        this.deleteSave = new Image("NewEra-Beta/res/buttons/DeleteButton.png");
        this.deleteSavePressed = new Image("NewEra-Beta/res/buttons/DeleteButtonPressed.png");
        this.deletePressed = false;

        //init play game button
        this.playGame = new Image("NewEra-Beta/res/buttons/PlayButton.png");
        this.playGamePressed = new Image("NewEra-Beta/res/buttons/PlayButtonPressed.png");
        this.playPressed = false;

        //init map
        this.map = new TiledMap("NewEra-Beta/res/map/LargeMapGrasslands.tmx");
        this.mapX = -2688;
        this.mapY = -2752;

        //init slot width and height
        this.slotWidth = 320;
        this.slotHeight = 64;

        //init menus
        this.newMenu = new TiledMap("NewEra-Beta/res/map/ClassSelectMenu.tmx");
        this.loadMenu = new TiledMap("NewEra-Beta/res/map/loadGameMenu.tmx");

        //return button
        this.goBack = new Image("NewEra-Beta/res/buttons/BackButton.png");

        //booleans for map animation
        this.moveUp = true;
        this.moveDown = false;
        this.moveLeft = true;
        this.moveRight = false;

        //numbers for map animation speed
        this.randNum = new Random();
        this.scrollSpeedX = .1f;
        this.scrollSpeedY = .101f;

        //booleans for beginning new game, loading previous save, and deleting a save
        this.newGameStarted = false;
        this.loadGameStarted = false;
        this.deletingSave = false;

        //booleans for new game menu items
        this.editingName = false;
        this.warriorSelected = false;
        this.wizardSelected = false;
        this.hunterSelected = false;
        this.rogueSelected = false;

        this.sprite1Selected = false;
        this.sprite2Selected = false;
        this.sprite3Selected = false;
        this.sprite4Selected = false;

        this.loadSlot1Selected = false;
        this.loadSlot2Selected = false;
        this.loadSlot3Selected = false;
        this.loadSlot4Selected = false;
        this.loadSlotSelected = false;

        //logic to allow gameplay
        this.classSelected = false;
        this.spriteSelected = false;
        this.playerName = "Hunter Tom";

        //init images for character select
        this.warrior = new SpriteSheet("NewEra-Beta/res/players/warrior.png", 32 ,32);
        warriorSprite = this.warrior.getSprite(1,2);
        this.warrior = new SpriteSheet("NewEra-Beta/res/players/warrior2.png", 32 ,32);
        warrior2Sprite = this.warrior.getSprite(1,2);
        this.warrior = new SpriteSheet("NewEra-Beta/res/players/warrior3.png", 32 ,32);
        warrior3Sprite = this.warrior.getSprite(1,2);
        this.warrior = new SpriteSheet("NewEra-Beta/res/players/warrior4.png", 32 ,32);
        warrior4Sprite = this.warrior.getSprite(1,2);
        this.warrior = new SpriteSheet("NewEra-Beta/res/players/warrior.png", 32 ,32);


        this.wizard = new SpriteSheet("NewEra-Beta/res/players/wizard.png", 32 ,32);
        wizardSprite = this.wizard.getSprite(1,2);
        this.wizard = new SpriteSheet("NewEra-Beta/res/players/wizard2.png", 32 ,32);
        wizard2Sprite = this.wizard.getSprite(1,2);
        this.wizard = new SpriteSheet("NewEra-Beta/res/players/wizard3.png", 32 ,32);
        wizard3Sprite = this.wizard.getSprite(1,2);
        this.wizard = new SpriteSheet("NewEra-Beta/res/players/wizard4.png", 32 ,32);
        wizard4Sprite = this.wizard.getSprite(1,2);
        this.wizard = new SpriteSheet("NewEra-Beta/res/players/wizard.png", 32 ,32);

        this.rogue = new SpriteSheet("NewEra-Beta/res/players/rouge.png", 32 ,32);
        rogueSprite = this.rogue.getSprite(1,2);
        this.rogue = new SpriteSheet("NewEra-Beta/res/players/rogue2.png", 32 ,32);
        rogue2Sprite = this.rogue.getSprite(1,2);
        this.rogue = new SpriteSheet("NewEra-Beta/res/players/rogue3.png", 32 ,32);
        rogue3Sprite = this.rogue.getSprite(1,2);
        this.rogue = new SpriteSheet("NewEra-Beta/res/players/rogue4.png", 32 ,32);
        rogue4Sprite = this.rogue.getSprite(1,2);
        this.rogue = new SpriteSheet("NewEra-Beta/res/players/rouge.png", 32 ,32);

        this.hunter = new SpriteSheet("NewEra-Beta/res/players/hunter.png", 32 ,32);
        hunterSprite = this.hunter.getSprite(1,2);
        this.hunter = new SpriteSheet("NewEra-Beta/res/players/hunter2.png", 32 ,32);
        hunter2Sprite = this.hunter.getSprite(1,2);
        this.hunter = new SpriteSheet("NewEra-Beta/res/players/hunter3.png", 32 ,32);
        hunter3Sprite = this.hunter.getSprite(1,2);
        this.hunter = new SpriteSheet("NewEra-Beta/res/players/hunter4.png", 32 ,32);
        hunter4Sprite = this.hunter.getSprite(1,2);
        this.hunter = new SpriteSheet("NewEra-Beta/res/players/hunter.png", 32 ,32);

        //text field init
        this.characterName =new TextField( gc, gc.getDefaultFont(),352, 72, 256, 24, new ComponentListener()
        {
            public void componentActivated(AbstractComponent source) {

                characterName.setFocus(true);
            }

        });

        this.player = this.player.getInstance();

    }

    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {

        //load initial main menu
        if(!newGameStarted && !loadGameStarted) {
            this.map.render((int) mapX, (int) mapY);

            player.getInstance();

            this.title.draw(66, 100);

            if (!newPressed) {
                this.newGame.draw(96, 300);
            } else {
                this.newGamePressed.draw(96, 300);
            }

            if (!loadPressed) {
                this.loadGame.draw(352, 300);
            } else {
                this.loadGamePressed.draw(352, 300);
            }
        }
        else if(newGameStarted && !loadGameStarted) {

            this.newMenu.render(0, 0);

            this.goBack.draw(10,10);



            //save slots
            g.setColor(Color.black);
            g.drawString("Select Save Slot", 40, 480);
            if(!slot1DidLoad) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.red);
            }
            g.fillRect(64, 512, 32, 32);
            g.setColor(Color.white);
            g.drawString("1", 76, 520);


            if(!slot2DidLoad) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.red);
            }
            g.fillRect(96, 512, 32, 32);
            g.setColor(Color.white);
            g.drawString("2", 108, 520);

            if(!slot3DidLoad) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.red);
            }
            g.fillRect(64, 544, 32, 32);
            g.setColor(Color.white);
            g.drawString("3", 76, 552);

            if(!slot4DidLoad) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.red);
            }
            g.fillRect(96, 544, 32, 32);
            g.setColor(Color.white);
            g.drawString("4", 108, 552);
            g.setColor(Color.white);
            if(loadSlot1Selected) {
                g.drawRect(64, 512, 32, 32);
            }
            if(loadSlot2Selected) {
                g.drawRect(96, 512, 32, 32);
            }
            if(loadSlot3Selected) {
                g.drawRect(64, 544, 32, 32);
            }
            if(loadSlot4Selected) {
                g.drawRect(96, 544, 32, 32);
            }



            //draw play button
            if ( playPressed && classSelected && spriteSelected) {
                this.playGamePressed.draw(225, 470);
            } else {
                this.playGame.draw(225, 470);
            }


            g.setColor(Color.white);
            characterName.render( gc, g );
            if(!editingName) {
                characterName.setFocus(false);
            }
            else {
                characterName.setFocus(true);
            }

            g.setColor(Color.black);
            g.drawString("Player Name:", 388, 36);
            g.drawString("Select Sprite", 260, 330);

            if(classSelected) {
                g.setColor(new Color(0, 0, 0, .3f));
                g.fillRect(selectedX, selectedY, 32, 32);
            }
            if(warriorSelected){
                g.setColor(Color.black);
                g.drawString("Warrior",298, 100 );
                g.drawString("Sword fighter with anger issues to match. Don't mix with beer.", 40, 130);
                g.drawString("- Melee Fighter", 60, 150);
                g.drawString("- Bonus to Base Defense ", 60, 170);
                g.drawString("- Stronger warriors can enter a Beserk state", 60, 190);
                g.setColor(Color.transparent);
                warriorSprite.draw(112, 400);
                warrior2Sprite.draw(240, 400);
                warrior3Sprite.draw(368, 400);
                warrior4Sprite.draw(496, 400);
                g.setColor(new Color(0, 0, 0, .3f));
                if(spriteSelected) {
                    g.fillRect(spriteSelectedX, spriteSelectedY, 32, 32);
                }

            }
            else if (wizardSelected) {
                g.setColor(Color.black);
                g.drawString("Wizard", 300, 100);
                g.drawString("Master of the Arcane Arts. Grows great beards.", 40, 130);
                g.drawString("- Ranged Spell-Caster", 60, 150);
                g.drawString("- Bonus to Base Attack", 60, 170);
                g.drawString("- Mighty wizards can make it rain...fire.", 60, 190);
                g.setColor(Color.transparent);
                wizardSprite.draw(112, 400);
                wizard2Sprite.draw(240, 400);
                wizard3Sprite.draw(368, 400);
                wizard4Sprite.draw(496, 400);
                g.setColor(new Color(0, 0, 0, .3f));
                if(spriteSelected) {
                    g.fillRect(spriteSelectedX, spriteSelectedY, 32, 32);
                }
            }
            else if (rogueSelected) {
                g.setColor(Color.black);
                g.drawString("Rogue",302, 100 );
                g.drawString("Expert Thief and Assassin. Check your pockets...", 40, 130);
                g.drawString("- Melee Assassin", 60, 150);
                g.drawString("- Dagger uses less Stamina at a cost to Base Damage", 60, 170);
                g.drawString("- More adept Rogues can utilize Invisibility", 60, 190);
                g.setColor(Color.transparent);
                rogueSprite.draw(112, 400);
                rogue2Sprite.draw(240, 400);
                rogue3Sprite.draw(368, 400);
                rogue4Sprite.draw(496, 400);
                g.setColor(new Color(0, 0, 0, .3f));
                if(spriteSelected) {
                    g.fillRect(spriteSelectedX, spriteSelectedY, 32, 32);
                }
            }
            else if (hunterSelected) {
                g.setColor(Color.black);
                g.drawString("Hunter",301, 100 );
                g.drawString("Skilled woodsman. Occasionally forgets deodorant.", 40, 130);
                g.drawString("- Ranged Bowman", 60, 150);
                g.drawString("- Bonus to Base Damage", 60, 170);
                g.drawString("- Capable of hiding in any environment...", 60, 190);
                g.setColor(Color.transparent);
                hunterSprite.draw(112, 400);
                hunter2Sprite.draw(240, 400);
                hunter3Sprite.draw(368, 400);
                hunter4Sprite.draw(496, 400);
                g.setColor(new Color(0, 0, 0, .3f));
                if(spriteSelected) {
                    g.fillRect(spriteSelectedX, spriteSelectedY, 32, 32);
                }

            } else {
                g.setColor(Color.black);
                g.drawString("Select a Class",270, 100 );
            }

            this.warrior.getSprite(1,5).draw(208, 240);
            this.wizard.getSprite(4, 14).draw(272, 240);
            this.rogue.getSprite(2, 20).draw(336, 240);
            this.hunter.getSprite(7,17).draw(400, 240);


        } else if (!newGameStarted && loadGameStarted) {


           // this.sg.load(1);
           // sbg.enterState(1);
            //this.sg.getInstance();
            int slotToLoad = 0; // no slot selected

            int slotNo, slotIndex;


            this.loadMenu.render(0,0);
            this.goBack.draw(10,10);
            if(!deletePressed) {
                this.deleteSave.draw(416, 192);
            } else {
                this.deleteSavePressed.draw(416, 192);
            }
            if (!loadPressed) {
                this.loadGame.draw(416, 300);
            } else {
                this.loadGamePressed.draw(416, 300);
            }

            if(deletingSave) {

                sg.getInstance();
                sg.deleteSave(this.slotNo);
                this.loadGames();
                deletingSave = false;

            }


            g.setColor(Color.black);
            g.drawString("Slot 1",165, 72);
            g.drawString("Slot 2", 165, 200);
            g.drawString("Slot 3", 165, 328);
            g.drawString("Slot 4", 165, 456);

            //slot 1 display information
            if(this.slot1DidLoad) {
                if(this.slot1Name.equals("") || this.slot1Name.equals(null)) {
                    g.drawString("Name: [No Name Set]", 40, 100);
                } else {
                    g.drawString("Name: "+this.slot1Name, 40, 100);
                }

                if( this.slot1ClassId == 0 ) {
                    g.drawString("Class: Hunter", 40, 120);
                } else if( this.slot1ClassId == 1 ) {
                    g.drawString("Class: Warrior", 40, 120);
                } else if( this.slot1ClassId == 2 ) {
                    g.drawString("Class: Wizard", 40, 120);
                } else if( this.slot1ClassId == 3 ) {
                    g.drawString("Class: Rogue", 40, 120);
                }
                g.drawString("Level: "+this.slot1Level, 40, 140);

            } else {
                g.drawString("[Empty Save Slot]", 115, 118);
            }

            //slot 2 display information
            if(this.slot2DidLoad) {
                if(this.slot2Name.equals("") || this.slot2Name.equals(null)) {
                    g.drawString("Name: [No Name Set]", 40, 228);
                } else {
                    g.drawString("Name: "+this.sg.getSaveName(), 40, 228);
                }
                if( this.slot2ClassId == 0 ) {
                    g.drawString("Class: Hunter", 40, 248);
                } else if( this.slot2ClassId == 1 ) {
                    g.drawString("Class: Warrior", 40, 248);
                } else if( this.slot2ClassId == 2 ) {
                    g.drawString("Class: Wizard", 40, 248);
                } else if( this.slot2ClassId == 3 ) {
                    g.drawString("Class: Rogue", 40, 248);
                }
                g.drawString("Level: "+this.slot2Level, 40, 268);
            } else {
                g.drawString("[Empty Save Slot]", 115, 246);
            }

            //slot 3 display information
            if(this.slot3DidLoad) {
                if(this.slot3Name.equals("") || this.slot3Name.equals(null)) {
                    g.drawString("Name: [No Name Set]", 40, 356);
                } else {
                    g.drawString("Name: "+this.slot3Name, 40, 356);
                }
                if( this.slot3ClassId == 0 ) {
                    g.drawString("Class: Hunter", 40, 376);
                } else if( this.slot3ClassId == 1 ) {
                    g.drawString("Class: Warrior", 40, 376);
                } else if( this.slot3ClassId == 2 ) {
                    g.drawString("Class: Wizard", 40, 376);
                } else if( this.slot3ClassId == 3 ) {
                    g.drawString("Class: Rogue", 40, 376);
                }
                g.drawString("Level: "+this.slot3Level, 40, 396);
            } else {
                g.drawString("[Empty Save Slot]", 115, 374);
            }

            //slot 4 display information
            if(this.slot4DidLoad) {
                if(this.slot4Name.equals("") || this.slot4Name.equals(null)) {
                    g.drawString("Name: [No Name Set]", 40, 484);
                } else {
                    g.drawString("Name: "+this.slot4Name, 40, 484);
                }
                if( this.slot4ClassId == 0 ) {
                    g.drawString("Class: Hunter", 40, 504);
                } else if( this.slot4ClassId == 1 ) {
                    g.drawString("Class: Warrior", 40, 504);
                } else if( this.slot4ClassId == 2 ) {
                    g.drawString("Class: Wizard", 40, 504);
                } else if( this.slot4ClassId == 3 ) {
                    g.drawString("Class: Rogue", 40, 504);
                }
                g.drawString("Level: "+this.slot4Level, 40, 524);
            } else {
                g.drawString("[Empty Save Slot]", 115, 502);
            }


            //slot one name coordinates

            if(loadSlot1Selected) {
                g.setColor(new Color(0, 0, 0, .3f));
                g.fillRect(slotSelectedX, slotSelectedY, slotWidth, slotHeight);

            } else if(loadSlot2Selected) {
                g.setColor(new Color(0, 0, 0, .3f));
                g.fillRect(slotSelectedX, slotSelectedY, slotWidth, slotHeight);

            } else if(loadSlot3Selected) {
                g.setColor(new Color(0, 0, 0, .3f));
                g.fillRect(slotSelectedX, slotSelectedY, slotWidth, slotHeight);

            } else if(loadSlot4Selected) {
                g.setColor(new Color(0, 0, 0, .3f));
                g.fillRect(slotSelectedX, slotSelectedY, slotWidth, slotHeight);
            }

        }
        if( gameStarted ) {
            //PLAYER STARTED GAME
            this.theme.stop();
            if(newGameStarted) {
                playerName = characterName.getText();


                player.getInstance();
                this.player.setIsNewGame(true);

                if (warriorSelected) {

                    if(sprite1Selected) {
                        this.player.setUpInstance("warrior.png", playerName, 1);
                    } else if(sprite2Selected) {
                        this.player.setUpInstance("warrior2.png", playerName, 1);
                    } else if(sprite3Selected) {
                        this.player.setUpInstance("warrior3.png", playerName, 1);
                    } else if(sprite4Selected) {
                        this.player.setUpInstance("warrior4.png", playerName, 1);
                    }


                } else if (wizardSelected) {

                    if (sprite1Selected) {
                        this.player.setUpInstance("wizard.png", playerName, 2);
                    } else if(sprite2Selected) {
                        this.player.setUpInstance("wizard2.png", playerName, 2);
                    } else if(sprite3Selected) {
                        this.player.setUpInstance("wizard3.png", playerName, 2);
                    } else if(sprite4Selected) {
                        this.player.setUpInstance("wizard4.png", playerName, 2);
                    }

                } else if (rogueSelected) {

                    if (sprite1Selected) {
                        this.player.setUpInstance("rouge.png", playerName, 3);
                    } else if(sprite2Selected) {
                        this.player.setUpInstance("rogue2.png", playerName, 3);
                    } else if(sprite3Selected) {
                        this.player.setUpInstance("rogue3.png", playerName, 3);
                    } else if(sprite4Selected) {
                        this.player.setUpInstance("rogue4.png", playerName, 3);
                    }

                } else if (hunterSelected) {

                    if (sprite1Selected) {
                        this.player.setUpInstance("hunter.png", playerName, 0);
                    } else if(sprite2Selected) {
                        this.player.setUpInstance("hunter2.png", playerName, 0);
                    } else if(sprite3Selected) {
                        this.player.setUpInstance("hunter3.png", playerName, 0);
                    } else if(sprite4Selected) {
                        this.player.setUpInstance("hunter4.png", playerName, 0);
                    }

                }
                player.setSaveSlot(slotNo);
                newGameStarted = false;
                classSelected = false;
                loadSlotSelected = false;
                spriteSelected = false;
                warriorSelected = false;
                wizardSelected = false;
                rogueSelected = false;
                hunterSelected = false;

                sprite1Selected = false;
                sprite2Selected = false;
                sprite3Selected = false;
                sprite4Selected = false;
                characterName.setText("");
                editingName = false;
                gameStarted = false;

                loadSlot1Selected = false;
                loadSlot2Selected = false;
                loadSlot3Selected = false;
                loadSlot4Selected = false;
                firstTimeRunning = false;
                sbg.enterState(1);

            } else if(loadGameStarted) {

                if(this.slotNo == 1) {
                    sg.load(1);
                } else if( slotNo == 2) {
                    sg.load(2);
                } else if( slotNo == 3) {
                    sg.load(3);
                } else {
                    sg.load(4);
                }
                newGameStarted = false;
                classSelected = false;
                loadSlotSelected = false;
                spriteSelected = false;
                warriorSelected = false;
                wizardSelected = false;
                rogueSelected = false;
                hunterSelected = false;

                sprite1Selected = false;
                sprite2Selected = false;
                sprite3Selected = false;
                sprite4Selected = false;
                characterName.setText("");
                editingName = false;
                gameStarted = false;

                loadSlot1Selected = false;
                loadSlot2Selected = false;
                loadSlot3Selected = false;
                loadSlot4Selected = false;
                firstTimeRunning = false;
                sbg.enterState(1);


            }

        }


    }
    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = gc.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        if(!newGameStarted && !loadGameStarted) {
            //title screen functionality

            if (moveUp) {
                if (mapY <= -5) {
                    mapY += delta * scrollSpeedY;
                } else {
                    moveUp = false;
                    moveDown = true;

                    int rand = randNum.nextInt(3);
                    if (rand == 0) {
                        scrollSpeedY = .09f;
                    } else if (rand == 1) {
                        scrollSpeedY = .1f;
                    } else {
                        scrollSpeedY = .101f;
                    }

                }

            }
            if (moveDown) {
                if (mapY >= -2800) {
                    mapY -= delta * scrollSpeedY;
                } else {
                    moveUp = true;
                    moveDown = false;

                    int rand = randNum.nextInt(3);
                    if (rand == 0) {
                        scrollSpeedY = .09f;
                    } else if (rand == 1) {
                        scrollSpeedY = .1f;
                    } else {
                        scrollSpeedY = .101f;
                    }
                }
            }
            if (moveRight) {
                if (mapX >= -2800) {
                    mapX -= delta * scrollSpeedX;
                } else {
                    moveRight = false;
                    moveLeft = true;

                    int rand = randNum.nextInt(3);
                    if (rand == 0) {
                        scrollSpeedX = .09f;
                    } else if (rand == 1) {
                        scrollSpeedX = .1f;
                    } else {
                        scrollSpeedX = .101f;
                    }
                }
            }
            if (moveLeft) {
                if (mapX <= -5) {
                    mapX += delta * scrollSpeedX;
                } else {
                    moveLeft = false;
                    moveRight = true;

                    int rand = randNum.nextInt(3);
                    if (rand == 0) {
                        scrollSpeedX = .09f;
                    } else if (rand == 1) {
                        scrollSpeedX = .1f;
                    } else {
                        scrollSpeedX = .101f;
                    }

                }
            }


            //New Game button operations
            if ((mouseX >= 96 && mouseX <= 290) && (mouseY >= 300 && mouseY <= 364)) {
                newPressed = true;
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    newGameStarted = true;

                }
            }
            else {
                newPressed = false;
            }


            //Load Game button operations
            if ((mouseX >= 352 && mouseX <= 546) && (mouseY >= 300 && mouseY <= 364)) {
                loadPressed = true;
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    loadGameStarted = true;

                }
            }
            else {
                loadPressed = false;
            }




        }
        else if(newGameStarted && !loadGameStarted) {
            //new game menu functionality

            //MOUSE OPTIONS


            //play game button toggle
            if ((mouseX >= 225 && mouseX <= 417) && (mouseY >= 470 && mouseY <= 598)) {
                playPressed = true;
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && classSelected && spriteSelected && loadSlotSelected) {
                    input.clearKeyPressedRecord();
                    gameStarted = true;
                }
            }
            else {
                playPressed = false;
            }



            if ((mouseX >= 10 && mouseX <= 106) && (mouseY >= 10 && mouseY <= 74)) {
                //clicking on the back button
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    newGameStarted = false;
                    classSelected = false;
                    loadSlotSelected = false;
                    spriteSelected = false;
                    warriorSelected = false;
                    wizardSelected = false;
                    rogueSelected = false;
                    hunterSelected = false;

                    sprite1Selected = false;
                    sprite2Selected = false;
                    sprite3Selected = false;
                    sprite4Selected = false;
                    characterName.setText("");
                    editingName = false;
                    gameStarted = false;

                    loadSlot1Selected = false;
                    loadSlot2Selected = false;
                    loadSlot3Selected = false;
                    loadSlot4Selected = false;





                }
            } else if ((mouseX >= 352 && mouseX <= 608) && (mouseY >= 72 && mouseY <= 96)) {
                //clicking on the text field
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    editingName = true;

                }

            } else if((mouseX >= 208 && mouseX <= 240) && (mouseY >= 240 && mouseY <= 272)) {
                //warrior class selected
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    selectedX = 208;
                    selectedY = 240;
                    warriorSelected = true;
                    wizardSelected = false;
                    rogueSelected = false;
                    hunterSelected = false;
                    classSelected = true;
                }

            } else if((mouseX >= 272 && mouseX <= 304) && (mouseY >= 240 && mouseY <= 272)) {
                // wizard class selected
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    selectedX = 272;
                    selectedY = 240;
                    wizardSelected = true;
                    warriorSelected = false;
                    rogueSelected = false;
                    hunterSelected = false;
                    classSelected = true;
                }

            } else if((mouseX >= 336 && mouseX <= 368) && (mouseY >= 240 && mouseY <= 272)) {
                //rogue class selected
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    selectedX = 336;
                    selectedY = 240;
                    rogueSelected = true;
                    warriorSelected = false;
                    wizardSelected = false;
                    hunterSelected = false;
                    classSelected = true;
                }

            } else if((mouseX >= 400 && mouseX <= 432) && (mouseY >= 240 && mouseY <= 272)) {
                //hunter class selected
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    selectedX = 400;
                    selectedY = 240;
                    hunterSelected = true;
                    warriorSelected = false;
                    wizardSelected = false;
                    rogueSelected = false;
                    classSelected = true;
                }

            //SPRITE OPTIONS
            } else if((mouseX >= 112 && mouseX <= 142) && (mouseY >= 400 && mouseY <= 432) && classSelected) {
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    spriteSelectedX = 112;
                    spriteSelectedY = 400;
                    sprite1Selected = true;
                    sprite2Selected = false;
                    sprite3Selected = false;
                    sprite4Selected = false;
                    spriteSelected = true;
                }
            } else if((mouseX >= 240 && mouseX <= 272) && (mouseY >= 400 && mouseY <= 432) && classSelected) {
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    spriteSelectedX = 240;
                    spriteSelectedY = 400;
                    sprite2Selected = true;
                    sprite1Selected = false;
                    sprite3Selected = false;
                    sprite4Selected = false;
                    spriteSelected = true;
                }
            } else if((mouseX >= 368 && mouseX <= 400) && (mouseY >= 400 && mouseY <= 432) && classSelected) {
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    spriteSelectedX = 368;
                    spriteSelectedY = 400;
                    sprite3Selected = true;
                    sprite1Selected = false;
                    sprite2Selected = false;
                    sprite4Selected = false;
                    spriteSelected = true;
                }
            } else if((mouseX >= 496 && mouseX <= 528) && (mouseY >= 400 && mouseY <= 432) && classSelected) {
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    spriteSelectedX = 496;
                    spriteSelectedY = 400;
                    sprite4Selected = true;
                    sprite1Selected = false;
                    sprite2Selected = false;
                    sprite3Selected = false;
                    spriteSelected = true;
                }
            } else if((mouseX >= 64 && mouseX <= 96) && (mouseY >= 512 && mouseY <= 544)) {
                //slot 1
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    loadSlot1Selected = true;
                    loadSlot2Selected = false;
                    loadSlot3Selected = false;
                    loadSlot4Selected = false;
                    this.loadSlotSelected = true;
                    this.slotNo = 1;
                }
            } else if((mouseX >= 96 && mouseX <= 128) && (mouseY >= 512 && mouseY <= 544)) {
                //slot 2
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    loadSlot1Selected = false;
                    loadSlot2Selected = true;
                    loadSlot3Selected = false;
                    loadSlot4Selected = false;
                    this.loadSlotSelected = true;
                    this.slotNo = 2;
                }
            } else if((mouseX >= 64 && mouseX <= 96) && (mouseY >= 544 && mouseY <= 576)) {
                //slot 3
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    loadSlot1Selected = false;
                    loadSlot2Selected = false;
                    loadSlot3Selected = true;
                    loadSlot4Selected = false;
                    this.loadSlotSelected = true;
                    this.slotNo = 3;
                }
            } else if((mouseX >= 96 && mouseX <= 128) && (mouseY >= 544 && mouseY <= 576)) {
                //slot 4
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    loadSlot1Selected = false;
                    loadSlot2Selected = false;
                    loadSlot3Selected = false;
                    loadSlot4Selected = true;
                    this.loadSlotSelected = true;
                    this.slotNo = 4;
                }
            } else { //clicking anywhere else on the screen
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    editingName = false;
                }
            }



        } else if (!newGameStarted && loadGameStarted) {
            //Load Game menu functionality

            //MOUSE OPTIONS

            if ((mouseX >= 10 && mouseX <= 106) && (mouseY >= 10 && mouseY <= 74)) {
                //clicking on the back button
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    loadGameStarted = false;
                    gameStarted = false;
                    loadSlot1Selected = false;
                    loadSlot2Selected = false;
                    loadSlot3Selected = false;
                    loadSlot4Selected = false;
                    loadSlotSelected = false;


                }
            }
            if ((mouseX >= 416 && mouseX <= 608) && (mouseY >= 192 && mouseY <= 256) && loadSlotSelected) {
                this.deletePressed = true;
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && loadSlotSelected) {
                    input.clearKeyPressedRecord();
                    this.deletingSave = true;
                }
            } else {
                this.deletePressed = false;
            }

            if ((mouseX >= 416 && mouseX <= 610) && (mouseY >= 300 && mouseY <= 364) && loadSlotSelected) {
                loadPressed = true;
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && loadSlotSelected) {
                    input.clearKeyPressedRecord();
                    this.gameStarted = true;
                }
            }
            else {
                loadPressed = false;
            }


            if ((mouseX >= 32 && mouseX <= 336) && (mouseY >= 96 && mouseY <= 160)) {
                //click on slot 1
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    slotSelectedX = 32;
                    slotSelectedY = 96;

                    this.slotNo = 1;
                    if(slot1DidLoad) {
                        loadSlotSelected = true;
                    } else {
                        loadSlotSelected = false;
                    }
                    loadSlot1Selected = true;
                    loadSlot2Selected = false;
                    loadSlot3Selected = false;
                    loadSlot4Selected = false;


                }
            }else if ((mouseX >= 32 && mouseX <= 336) && (mouseY >= 224 && mouseY <= 288)) {
                //click on slot 2
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    slotSelectedX = 32;
                    slotSelectedY = 224;

                    this.slotNo = 2;
                    if(slot2DidLoad) {
                        loadSlotSelected = true;
                    } else {
                        loadSlotSelected = false;
                    }
                    loadSlot2Selected = true;
                    loadSlot1Selected = false;
                    loadSlot3Selected = false;
                    loadSlot4Selected = false;

                }
            } else if ((mouseX >= 32 && mouseX <= 336) && (mouseY >= 352 && mouseY <= 416)) {
                //click on slot 3
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    slotSelectedX = 32;
                    slotSelectedY = 352;

                    this.slotNo = 3;
                    if(slot3DidLoad) {
                        loadSlotSelected = true;
                    } else {
                        loadSlotSelected = false;
                    }
                    loadSlot3Selected = true;
                    loadSlot1Selected = false;
                    loadSlot2Selected = false;
                    loadSlot4Selected = false;

                }
            } else if ((mouseX >= 32 && mouseX <= 336) && (mouseY >= 480 && mouseY <= 544)) {
                //click on slot 4
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    slotSelectedX = 32;
                    slotSelectedY = 480;

                    this.slotNo = 4;
                    if(slot1DidLoad) {
                        loadSlotSelected = true;
                    } else {
                        loadSlotSelected = false;
                    }
                    loadSlot4Selected = true;
                    loadSlot1Selected = false;
                    loadSlot2Selected = false;
                    loadSlot3Selected = false;

                }
            }
        }

    }
    public void loadGames() {
        //load files from save folder
        sg = SaveGame.getInstance();

        slot1DidLoad = sg.load(1);
        if(slot1DidLoad) {
            slot1Name = sg.getSaveName();
            slot1ClassId = sg.getClassID();
            slot1Level = sg.getPlayerLvl();
        }
        slot2DidLoad = sg.load(2);
        if(slot2DidLoad) {
            slot2Name = sg.getSaveName();
            slot2ClassId = sg.getClassID();
            slot2Level = sg.getPlayerLvl();
        }
        slot3DidLoad = sg.load(3);
        if(slot3DidLoad) {
            slot3Name = sg.getSaveName();
            slot3ClassId = sg.getClassID();
            slot3Level = sg.getPlayerLvl();
        }
        slot4DidLoad = sg.load(4);
        if(slot4DidLoad) {
            slot4Name = sg.getSaveName();
            slot4ClassId = sg.getClassID();
            slot4Level = sg.getPlayerLvl();
        }
        player = null;
    }
}
