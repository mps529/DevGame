package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.Random;


public class MainMenu extends BasicGameState {

    private int gameState;

    //buttons and menu items
    private Image title;
    private Image newGame, newGamePressed;
    private Image loadGame, loadGamePressed;
    private Image playGame, playGamePressed;
    private Image goBack;

    private Image warriorSprite, warrior2Sprite, warrior3Sprite, warrior4Sprite;
    private Image wizardSprite, wizard2Sprite, wizard3Sprite, wizard4Sprite;
    private Image rogueSprite, rogue2Sprite, rogue3Sprite, rogue4Sprite;
    private Image hunterSprite, hunter2Sprite, hunter3Sprite, hunter4Sprite;

    private SpriteSheet hunter, warrior, rogue, wizard;
    private TextField characterName;

    /////// player save and save slot
    private SaveGame sg = new SaveGame();
    private int slot;
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
    private boolean newGameStarted, loadGameStarted, gameStarted;
    private boolean loadPressed, newPressed, playPressed;
    private boolean editingName;
    private boolean warriorSelected, wizardSelected, rogueSelected, hunterSelected, classSelected;
    private boolean sprite1Selected, sprite2Selected, sprite3Selected, sprite4Selected, spriteSelected;
    private int selectedX, selectedY;
    private int spriteSelectedX, spriteSelectedY;

    public MainMenu( int state ) {
        this.gameState = state;
    }

    public int getID( ) {
        return gameState;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {


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

        //init play game button
        this.playGame = new Image("NewEra-Beta/res/buttons/PlayButton.png");
        this.playGamePressed = new Image("NewEra-Beta/res/buttons/PlayButtonPressed.png");
        this.playPressed = false;

        //init map
        this.map = new TiledMap("NewEra-Beta/res/map/LargeMapGrasslands.tmx");
        this.mapX = -2688;
        this.mapY = -2752;

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

        //booleans for beginning new game or loading previous save
        this.newGameStarted = false;
        this.loadGameStarted = false;

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
        //this.rogue = new SpriteSheet("NewEra-Beta/res/players/rogue4.png", 32 ,32);
        //rogue4Sprite = this.rogue.getSprite(1,2);
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
                //rogue4Sprite.draw(496, 400);
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

            if( gameStarted ) {
                //PLAYER STARTED GAME
                if(newGameStarted) {
                    playerName = characterName.getText();

                    if (warriorSelected) {

                        if(sprite1Selected) {
                            this.player.setUpInstance("warrior.png", playerName, 1);
                            sbg.enterState(1);
                        } else if(sprite2Selected) {
                            this.player.setUpInstance("warrior2.png", playerName, 1);
                            sbg.enterState(1);
                        } else if(sprite3Selected) {
                            this.player.setUpInstance("warrior3.png", playerName, 1);
                            sbg.enterState(1);
                        } else if(sprite4Selected) {
                            this.player.setUpInstance("warrior4.png", playerName, 1);
                            sbg.enterState(1);
                        }


                    } else if (wizardSelected) {

                        if (sprite1Selected) {
                            this.player.setUpInstance("wizard.png", playerName, 2);
                            sbg.enterState(1);
                        } else if(sprite2Selected) {
                            this.player.setUpInstance("wizard2.png", playerName, 2);
                            sbg.enterState(1);
                        } else if(sprite3Selected) {
                            this.player.setUpInstance("wizard3.png", playerName, 2);
                            sbg.enterState(1);
                        } else if(sprite4Selected) {
                            this.player.setUpInstance("wizard4.png", playerName, 2);
                            sbg.enterState(1);
                        }

                    } else if (rogueSelected) {

                        if (sprite1Selected) {
                            this.player.setUpInstance("rouge.png", playerName, 3);
                            sbg.enterState(1);
                        } else if(sprite2Selected) {
                            this.player.setUpInstance("rogue2.png", playerName, 3);
                            sbg.enterState(1);
                        } else if(sprite3Selected) {
                            this.player.setUpInstance("rogue3.png", playerName, 3);
                            sbg.enterState(1);
                        } else if(sprite4Selected) {
                            //this.player.setUpInstance("rogue4.png", playerName, 3);
                            //sbg.enterState(1);
                        }

                    } else if (hunterSelected) {

                        if (sprite1Selected) {
                            this.player.setUpInstance("hunter.png", playerName, 0);
                            sbg.enterState(1);
                        } else if(sprite2Selected) {
                            this.player.setUpInstance("hunter2.png", playerName, 0);
                            sbg.enterState(1);
                        } else if(sprite3Selected) {
                            this.player.setUpInstance("hunter3.png", playerName, 0);
                            sbg.enterState(1);
                        } else if(sprite4Selected) {
                            this.player.setUpInstance("hunter4.png", playerName, 0);
                            sbg.enterState(1);
                        }

                    }
                } else if(loadGameStarted && !newGameStarted) {


                }

            }
        } else if (!newGameStarted && loadGameStarted) {

            this.loadMenu.render(0,0);

            this.goBack.draw(10,10);
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
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && classSelected && spriteSelected) {
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
            }
            else { //clicking anywhere else on the screen
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

                }
            }
        }

    }
}
