package javagame;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Sound;
import java.util.Random;

public class Player extends Movement {

    // Max Health for hunter
    private static double MAX_HEALTH;
    // Max Stamina
    private static double MAX_STAMINA ;
    // Max level
    private static final int MAX_LEVEL = 20;

    // Base Attack and Defend
    private static int BASE_ATTACK ;
    private static int BASE_DEFENCE;

    // overall attack and defence
    private static int OVERALL_ATTACK;
    private static int OVERALL_DEFENCE;

    private int minRunningStamina = 10;

    // The amount of stamina it takes to attack
    private int attackOneStamina;
    private int attackTwoStamina;
    private int attackThreeStamina;
    private int attackFourStamina;

    // The amount of stamina it takes to attack
    private int attackOneDamage;
    private int attackTwoDamage;
    private int attackThreeDamage;
    private int attackFourDamage;

    private Sound attack1, attack2, attack3, attack4;

    /*
        0 - attackOneStamina
        1- attackTwoStamina
        2- attackThreeStamina
        3 - attackFourStamina
    */
    private int moveSelected;

    private Image[] attackImages;
    private int[] attacksKnown;

    // Status Bar colors
    private Color red, blue, green, black, grey;

    // Experence needed to level up
    private double expToLevelUp;
    // Amount of experience player has
    private double exp;

    // Player Health
    private double health;
    // Player Stamina
    private double stamina;
    // Player level
    private int level;

    private int perkPoints;
    private int movePoints;

    private boolean inBeserkMode = false;

    // projectile Animations
    private Image[] projectileImageOne = null;
    private Image[] projectileImageTwo = null;

    // Currently Selected Attack
    private Image currentAttack;
    // Players Inventory
    private Inventory inventory;

    private boolean playerDead = false;

    // enemy inventory player is looting
        // used only when interacting with enemy bodies
    private Inventory lootingInventory;
    //enemy id that is being looted
    private int lootingId;

    // How long arrow will live for.
    private static int FIRE_RATE = 250;
    // Index of the projectile array
    private int currentIndex = 0;
    // Time since last shot
    private int lastShot = 0;

        // Health and stamina bar
    private Image emptyHealth;
        // Exp bar
    private Image emptyExpBar;
        // Movement Moves
    private TiledMap playerMoves;

        // Health Potion
    private Image healthPotion;
        // Stamina Potion
    private Image staminaPotion;

        // If the player is in combat
    private boolean inCombat;

    private int saveSlot;

    //Interaction class to handle player actions
    private Interaction action;

    // needed to set the map to player location when game loads
    private float mapX, mapY;
    private float skewX, skewY;
    private boolean isNewGame;

    // 0=hunter 1=warrior 2=wizard 3=rogue
    private int characterClassChosen;

    private static Player playerClass = null;

    public Player() {
        super();
    }

    public void playerCopy(Player other) {


        this.minRunningStamina = 10;
        this.expToLevelUp = other.expToLevelUp;
        this.exp = other.exp;
        this.health = other.health;
        this.stamina = other.stamina;
        this.level = other.level;
        this.perkPoints = other.perkPoints;
        this.movePoints = other.movePoints;
        this.inventory = new Inventory(other.getInventory(), false );
        //this.lootingInventory = new Inventory(other.getLootingInventory(), false);
        this.FIRE_RATE = other.FIRE_RATE;
        this.currentIndex = other.currentIndex;
        this.lastShot = other.lastShot;
        this.inCombat = other.inCombat;
        this.saveSlot = other.saveSlot;
        this.mapX = other.mapX;
        this.mapY = other.mapY;
        this.skewX = other.skewX;
        this.skewY = other.skewY;
        this.isNewGame = other.isNewGame;
        this.action = new Interaction();

        // Movement
        setCurrentMapIndex(other.getCurrentMapIndex() );
        setPlayerDirection(other.getDirection() );
        setPlayerXinPixels( other.getPlayerX() );
        setPlayerYinPixels( other.getPlayerY() );

    }


    public void setUpInstance( String sheetName, String name, int classID ) throws SlickException {
        // Call Movement constructor
        setPlayerClass(sheetName, name, classID);
        this.isNewGame = true;

        this.action = new Interaction();

        this.attackImages = new Image[4];
        this.attacksKnown = new int[4];

        if( classID == 0 ) {
            setHunter();
        }
        else if( classID == 1 ){
            setWarrior();
        }
        else if( classID == 2 ) {
            setWizard();
            //attack1 = new Sound("")
        }
        else if( classID == 3 ) {
            setRouge();
        }

        // Set the color
        red = new Color( 225, 0, 0, .7f );
        green = new Color( 0,128,0, .7f );
        blue = new Color( 0,206,209 );
        black = new Color( 0,0,0, .7f );
        grey = new Color( 0, 0, 0, .3f );

        // Set Movement starting attributes
        setLevel( 1 );
        calculateExpToLevelUp();
        setHealth( MAX_HEALTH );
        setStamina( MAX_STAMINA );
        setExp( 0 );

        this.moveSelected = 0;

        // Set up player Inventory/ give default items

            this.inventory = new Inventory();
            this.inventory.setBaseAttack(this.BASE_ATTACK);
            this.inventory.setBaseDefence(this.BASE_DEFENCE);
            this.inventory.setClassID(classID);


        this.playerMoves = new TiledMap( "NewEra-Beta/res/map/itemSlots.tmx" );

        this.emptyHealth = new Image( "NewEra-Beta/res/dash/EmptyBar.png" );
        this.emptyExpBar = new Image( "NewEra-Beta/res/dash/EmptyBarLong.png" );

        this.healthPotion = new Image( "NewEra-Beta/res/items/health.png" );
        this.staminaPotion = new Image( "NewEra-Beta/res/items/stamina.png" );

    }

    public void setUpLoadInstance( String sheetName, String name, int classID ) throws SlickException {
        // Call Movement constructor
        setPlayerClass(sheetName, name, classID);

        this.action = new Interaction();

        this.attackImages = new Image[4];
        this.attacksKnown = new int[4];

        if( classID == 0 ) {
            setHunter();
        }
        else if( classID == 1 ){
            setWarrior();
        }
        else if( classID == 2 ) {
            setWizard();
        }
        else if( classID == 3 ) {
            setRouge();
        }

        // Set the color
        red = new Color( 225, 0, 0, .7f );
        green = new Color( 0,128,0, .7f );
        blue = new Color( 0,206,209 );
        black = new Color( 0,0,0, .7f );
        grey = new Color( 0, 0, 0, .3f );

        // Set Movement starting attributes
        //setLevel( 1 );
       // calculateExpToLevelUp();
       // setHealth( 80 );
       // setStamina( MAX_STAMINA );
       // setExp( 0 );

        this.moveSelected = 0;

           /* this.inventory = new Inventory();
            this.inventory.setBaseAttack(this.BASE_ATTACK);
            this.inventory.setBaseDefence(this.BASE_DEFENCE);
            this.inventory.setClassID(classID);*/

        this.OVERALL_ATTACK = this.inventory.getPlayerOverallAttack();
        this.OVERALL_DEFENCE = this.inventory.getPlayerOverallDefence();

        this.playerMoves = new TiledMap( "NewEra-Beta/res/map/itemSlots.tmx" );

        this.emptyHealth = new Image( "NewEra-Beta/res/dash/EmptyBar.png" );
        this.emptyExpBar = new Image( "NewEra-Beta/res/dash/EmptyBarLong.png" );

        this.healthPotion = new Image( "NewEra-Beta/res/items/health.png" );
        this.staminaPotion = new Image( "NewEra-Beta/res/items/stamina.png" );

    }

    public static Player getInstance() {
        if( playerClass == null ) {
            playerClass = new Player();
        }
        return playerClass;
    }

    private void setHunter() throws SlickException {
        this.characterClassChosen = 0;
        // Setting arrow animations
        projectileImageOne = new Image[4];
        projectileImageOne[0] = new Image("NewEra-Beta/res/projectiles/Arrow-Up.png");
        projectileImageOne[1] = new Image("NewEra-Beta/res/projectiles/Arrow-Right.png");
        projectileImageOne[2] = new Image("NewEra-Beta/res/projectiles/Arrow-Down.png");
        projectileImageOne[3] = new Image("NewEra-Beta/res/projectiles/Arrow-Left.png");

        projectileImageTwo = new Image[4];
        projectileImageTwo[0] = new Image("NewEra-Beta/res/projectiles/Double-Arrow-Up.png");
        projectileImageTwo[1] = new Image("NewEra-Beta/res/projectiles/Double-Arrow-Right.png");
        projectileImageTwo[2] = new Image("NewEra-Beta/res/projectiles/Double-Arrow-Down.png");
        projectileImageTwo[3] = new Image("NewEra-Beta/res/projectiles/Double-Arrow-Left.png");

        // sets the projectile
        setProjectileImage( projectileImageOne );

        // This sets the display image for which attack is chosen
        currentAttack = projectileImageOne[1];

        this.attackImages[0] =  projectileImageOne[1];
        this.attackImages[1] = new Image( "NewEra-Beta/res/moves/trap.png" );
        this.attackImages[2] = new Image( "NewEra-Beta/res/moves/bush.png" );
        this.attackImages[3 ]= new Image( "NewEra-Beta/res/projectiles/Double-Arrow-Right.png" );

        this.attacksKnown[0] = 1;
        this.attacksKnown[1] = 1;
        this.attacksKnown[2] = 1;
        this.attacksKnown[3] = 1;

        this.MAX_HEALTH = 100;
        this.MAX_STAMINA = 120;
        this.BASE_ATTACK = 13;
        this.BASE_DEFENCE = 5;

        this.attackOneStamina = 5;
        this.attackTwoStamina = 10;
        this.attackThreeStamina = 2;
        this.attackFourStamina = 20;

        this.attackOneDamage = 10;
        this.attackTwoDamage = 20;
        this.attackThreeDamage = 20;
        this.attackFourDamage = 30;

    }
    private void setWarrior() throws SlickException {
        this.characterClassChosen = 1;

        projectileImageOne = new Image[4];
        projectileImageOne[0] = new Image("NewEra-Beta/res/moves/BoulderThrow.png");
        projectileImageOne[1] = new Image("NewEra-Beta/res/moves/BoulderThrow.png");
        projectileImageOne[2] = new Image("NewEra-Beta/res/moves/BoulderThrow.png");
        projectileImageOne[3] = new Image("NewEra-Beta/res/moves/BoulderThrow.png");
        // sets the projectile
        setProjectileImage( projectileImageOne );

            // This sets the display image for which attack is chosen
        this.attackImages[0] =  new Image("NewEra-Beta/res/items/spear.png");
        this.attackImages[1] = new Image( "NEwEra-Beta/res/moves/BoulderThrow.png" );
        this.attackImages[2] = new Image( "NEwEra-Beta/res/moves/stun.png" );
        this.attackImages[3 ]= new Image( "NEwEra-Beta/res/moves/berserk.png" );

        currentAttack =this.attackImages[0];

        this.attacksKnown[0] = 1;
        this.attacksKnown[1] = 1;
        this.attacksKnown[2] = 1;
        this.attacksKnown[3] = 1;

        this.MAX_HEALTH = 120;
        this.MAX_STAMINA = 110;
        this.BASE_ATTACK = 7;
        this.BASE_DEFENCE = 10;

        this.attackOneStamina = 5;
        this.attackTwoStamina = 10;
        this.attackThreeStamina = 2;
        this.attackFourStamina = 20;

        this.attackOneDamage = 10;
        this.attackTwoDamage = 20;
        this.attackThreeDamage = 30;
        this.attackFourDamage = 40;
    }
    private void setWizard() throws SlickException {
        this.characterClassChosen = 2;

        // Setting projectileImage animations
        projectileImageOne = new Image[4];
        projectileImageOne[0] = new Image("NewEra-Beta/res/projectiles/FireBall-Up.png");
        projectileImageOne[1] = new Image("NewEra-Beta/res/projectiles/FireBall-Right.png");
        projectileImageOne[2] = new Image("NewEra-Beta/res/projectiles/FireBall-Down.png");
        projectileImageOne[3] = new Image("NewEra-Beta/res/projectiles/FireBall-Left.png");
        // sets the projectile
        setProjectileImage( projectileImageOne );

        this.attackImages[0] =  projectileImageOne[1];
        this.attackImages[1] = new Image( "NewEra-Beta/res/moves/transmute.png" );
        this.attackImages[2] = new Image( "NewEra-Beta/res/moves/summon.png" );
        this.attackImages[3 ]= new Image( "NewEra-Beta/res/moves/immulate.png" );

        this.attacksKnown[0] = 1;
        this.attacksKnown[1] = 1;
        this.attacksKnown[2] = 1;
        this.attacksKnown[3] = 1;

        this.MAX_HEALTH = 90;
        this.MAX_STAMINA = 110;
        this.BASE_ATTACK = 15;
        this.BASE_DEFENCE = 5;

        this.attackOneStamina = 5;
        this.attackTwoStamina = 10;
        this.attackThreeStamina = 15;
        this.attackFourStamina = 20;

        this.attackOneDamage = 10;
        this.attackTwoDamage = 20;
        this.attackFourDamage = 2;

    }
    private void setRouge() throws SlickException {
        this.characterClassChosen = 3;

        projectileImageOne = new Image[4];
        projectileImageOne[0] = new Image("NewEra-Beta/res/moves/ninjaStar.png");
        projectileImageOne[1] = new Image("NewEra-Beta/res/moves/ninjaStar.png");
        projectileImageOne[2] = new Image("NewEra-Beta/res/moves/ninjaStar.png");
        projectileImageOne[3] = new Image("NewEra-Beta/res/moves/ninjaStar.png");
        // sets the projectile
        setProjectileImage( projectileImageOne );

        this.attackImages[0] = new Image("NewEra-Beta/res/items/dagger.png");
        this.attackImages[1] = new Image( "NewEra-Beta/res/moves/ninjaStar.png" );
        this.attackImages[2] = new Image( "NewEra-Beta/res/moves/invisible.png" );
        this.attackImages[3]= new Image( "NewEra-Beta/res/moves/poisonDagger.png" );

        this.attacksKnown[0] = 1;
        this.attacksKnown[1] = 1;
        this.attacksKnown[2] = 1;
        this.attacksKnown[3] = 1;

        this.MAX_HEALTH = 100;
        this.MAX_STAMINA = 100;
        this.BASE_ATTACK = 11;
        this.BASE_DEFENCE = 9;

        this.attackOneStamina = 5;
        this.attackTwoStamina = 10;
        this.attackThreeStamina = 2;
        this.attackFourStamina = 20;

        this.attackOneDamage = 10;
        this.attackTwoDamage = 20;
        this.attackThreeDamage = 30;
        this.attackFourDamage = 40;
    }

    public Image[] getAttackImages() { return this.attackImages; }
    public int[] getAttacksKnown() { return this.attacksKnown; }
    public Image getAttackImage( int index ) { return this.attackImages[ index ]; }

    public Inventory getInventory() {
        return this.inventory;
    }
    public void setInventory( Inventory inventory) {this.inventory = new Inventory(inventory, false);}
    public void setInventoryLoad(Inventory inventory) {this.inventory = new Inventory(inventory, true);}

    public Inventory getLootingInventory() {return lootingInventory;}
    public void setLootingInventory(Inventory lootingInventory) {
        if(lootingInventory != null) {
            this.lootingInventory = new Inventory(lootingInventory, false);
        } else {
            this.lootingInventory = null;
        }
    }

    public int getLootingId() {return lootingId;}
    public void setLootingId(int lootingId) {this.lootingId = lootingId;}

    public void setInBeserkMode( boolean beserkMode ) { this.inBeserkMode = beserkMode; }
    public boolean getInBeserkMode() { return this.inBeserkMode; }

    public int getAttackOneDamage() { return this.attackOneDamage; }
    public void incrementAttackOneDamage() { this.attackOneDamage += 2; }

    public int getAttackTwoDamage() { return this.attackTwoDamage; }
    public void incrementAttackTwoDamage() { this.attackTwoDamage += 2; }

    public int getAttackThreeDamage() { return this.attackThreeDamage; }
    public void incrementAttackThreeDamage() { this.attackThreeDamage += 2; }

    public int getAttackFourDamage() { return this.attackFourDamage; }
    public void incrementAttackFourDamage() { this.attackFourDamage += 2; }

    public int getDamageOfCurrentAttack() {
        switch ( this.moveSelected ) {
            case 0:
                return this.attackOneDamage;
            case 1:
                return this.attackTwoDamage;
            case 2:
                return this.attackThreeDamage;
            case 3:
                return this.attackFourDamage;
            default:
                return -110;
        }
    }

    public boolean getInCombat() { return this.inCombat; }
    public void setInCombat( boolean inCombat ) { this.inCombat = inCombat; }

    public double getMaxHealth() { return this.MAX_HEALTH; }
    public void setMaxHealth( int health ) { this.MAX_HEALTH = health; }
    public void incrementMaxHealth( ) { this.MAX_HEALTH += 10; }

    public double getMaxStamina() { return this.MAX_STAMINA; }
    public void setMaxStamina( int stamina ) { this.MAX_STAMINA = stamina; }
    public void incrementMaxStamina( ) { this.MAX_STAMINA += 10; }

    public int getOverallAttack() { return this.OVERALL_ATTACK; }
    public void setOverallAttack( int attack ) { this.OVERALL_ATTACK = attack; }

    public int getOverallDefence() {
        if( this.inBeserkMode ) {
            return ( this.OVERALL_DEFENCE  + ( this.level * 2 ) );
        }

        return this.OVERALL_DEFENCE;
    }
    public void setOverallDefence( int defence ) { this.OVERALL_DEFENCE = defence; }

    public void setBaseAttack( int attack ) { this.BASE_ATTACK = attack; }
    public void increaseBaseAttack( int increase ) {
        this.BASE_ATTACK += increase;
        this.OVERALL_ATTACK += increase;
    }
    public int getBaseAttack() { return this.BASE_ATTACK; }

    public void setBaseDefence( int attack ) { this.BASE_DEFENCE = attack; }
    public void increaseBaseDefence( int increase ) {
        this.BASE_DEFENCE += increase;
        this.OVERALL_ATTACK += increase;
    }
    public int getBaseDefence() { return this.BASE_DEFENCE; }

    public void setHealth( double health ) {
        this.health = health;
    }
    public double getHealth() {
        return this.health;
    }
    public void decreaseHealth( double damage ) {
        this.health -= damage;
    }
    public void increaseHealth( double heal ) {
        this.health += heal;
        if( this.health >= MAX_HEALTH ) {
            this.health = MAX_HEALTH;
        }

    }
    public void takeDamage( int attack, int movePower ) {
        Random rand = new Random();

        int defence  = this.inventory.getPlayerOverallDefence() + 1;
        if( this.inBeserkMode ) {
            defence += this.level * 2;
        }

        this.health -= (  ( attack * ( rand.nextInt( 7 ) + 1 )   ) / defence  ) + movePower;

        if( this.health < 0 ) {
            this.playerDead = true;

        }

    }

    public void setMoveSelected( int move ) {
        if( getPlayerClass() == 0 ) {
            if( move == 0 ) {
                setProjectileImage( this.projectileImageOne );
            }
            else if( move == 3 ) {
                setProjectileImage( this.projectileImageTwo );
            }
        }
        else if( getPlayerClass() == 4 ) {
            if( move == 0 ) {
                setProjectileImage( this.projectileImageOne );
            }
        }
        this.moveSelected = move;
    }
    public int getMoveSelected( ) { return this.moveSelected; }
    public boolean isMoveKnown( int move ) {
        if( this.attacksKnown[ move ] == 1 ) {
            return true;
        }
        return false;
    }

    public void setStamina( double stamina ) {
        this.stamina = stamina;
    }
    public double getStamina() {
        return this.stamina;
    }
    public int getAttackStamina() {
        if( this.getMoveSelected() == 0 ) {
            return this.attackOneStamina;
        }
        else if( this.getMoveSelected() == 1 ) {
            return  this.attackTwoStamina;
        }
        else if( this.getMoveSelected() == 2 ) {
            return this.attackThreeStamina;
        }
        else if( this.getMoveSelected() == 3 ) {
            return this.attackFourStamina;
        }
        else {
            return (int)this.MAX_STAMINA;
        }
    }
    public void decreaseStamina( double energy ) { this.stamina -= energy;  }
    public void decreaseStamina( ) {
        this.stamina -= getAttackStamina();
    }
    public void increaseStamina( double energy ) {
        this.stamina += energy;
        if( this.stamina > MAX_STAMINA ) {
            this.stamina = MAX_STAMINA;
        }
    }
    public void increaseMaxStamina( ) {
        Random random = new Random();
        MAX_STAMINA += random.nextInt( 20 ) + 5;
    }
    public void setMinRunningStamina( int newMin ) { this.minRunningStamina = newMin; }
    public int getMinRunningStamina() { return this.minRunningStamina; }

    public boolean checkDeath() {
        if( this.playerDead ) {
            this.health = 0;
        }
        return this.playerDead;
    }

    public void setExpToLevelUp( double exp ) { this.expToLevelUp = exp; }
    public double getExpToLevelUp() { return this.expToLevelUp; }
    public void calculateExpToLevelUp() {
        Random random = new Random();
        // Next level Experience
        this.expToLevelUp = getLevel() * ( random.nextInt(40) + 20 );

    }

    public void setExp( double exp ) {
        this.exp = exp;
    }
    public double getExp() {
        return this.exp;
    }
    public void increaseExp( double exp ) {
        this.exp += exp;
        checkLevelUp();
    }
    private void checkLevelUp() {
        // Check for level up
        if( getExp() > getExpToLevelUp() ) {
            // Collect exp to continue it to next level
            double overFlowExp = getExp() - getExpToLevelUp();

            setExp( overFlowExp );
            levelUp();
        }
        else if( getExp() == getExpToLevelUp() ) {
            setExp( 0 );
            levelUp();
        }
    }

    public void setLevel( int level ) { this.level = level; }
    public int getLevel() { return this.level; }
    public void levelUp() {
        this.level++;
        this.perkPoints += 2;
        this.movePoints += 1;
        calculateExpToLevelUp();
    }

    public int getPerkPoints() { return this.perkPoints; }
    public void decrementPerkPoints() { this.perkPoints--; }

    public int getCharacterClassChosen() {return this.characterClassChosen;}

    public int getMovePoints() { return this.movePoints; }
    public void decrementMovePoints() { this.movePoints--; }

    public int getSaveSlot() { return this.saveSlot; }
    public void setSaveSlot( int slot ) { this.saveSlot = slot; }

    public float getMapX() {return this.mapX;}
    public void setMapX(float x) {this.mapX = x;}

    public float getMapY() {return this.mapY;}
    public void setMapY(float y) {this.mapY = y;}

    public float getSkewX() {return this.skewX;}
    public void setSkewX(float sx) {this.skewX = sx;}

    public Interaction getAction() {return action;}

    public float getSkewY() {return this.skewY;}
    public void setSkewY(float sy) {this.skewY = sy;}

    public boolean getIsNewGame() {return this.isNewGame;}
    public void setIsNewGame(boolean ing) {this.isNewGame = ing;}

    public boolean isWeaponEqiupped () {
        return this.inventory.isWeaponEquipped();
    }

    public void updateAttack( int delta, boolean attacked, Map map ) {
        // Update projectiles position
        updateProjectile(delta, attacked, map, getMoveSelected());
        updateTrap(delta, attacked, map, getMoveSelected());
    }

    public void usedHealthPotion() {
        if( this.inventory.getHealthPotions() > 0 ) {
            this.health += 30;
            this.inventory.useHealthPotion();
        }
    }

    public void usedStaminaPotion() {
        if( this.inventory.getStaminaPotions() > 0 ) {
            this.stamina += 30;
            this.inventory.useStaminaPotion();
        }
    }

    public void drawPlayerInfo( Graphics g ) {

        // Calculate Percents
        double healthPercent = getHealth() / MAX_HEALTH;
        double staminaPercent = getStamina() / MAX_STAMINA;
        double experiencePercent = getExp() / getExpToLevelUp();

        this.playerMoves.render( 0, 538 );

        g.setColor( red );
        g.fillRoundRect(121, 558, 191 * (float)healthPercent, 10, 10 );
        this.emptyHealth.draw( 114, 550 );

        g.setColor( green );
        g.fillRoundRect(328, 558, 191 * (float)staminaPercent, 10, 10 );
        this.emptyHealth.draw( 320, 550 );

        g.setColor( Color.blue );
        g.fillRoundRect( 110, 631, 423 * (float)experiencePercent, 4, 5 );
        this.emptyExpBar.draw( 104, 628 );

        int xPos = 144;
        g.setColor( grey );

        for( int x = 0; x < 4; x++ ) {
            if( this.attacksKnown[ x ] == 1 ) {
                g.setColor( grey );
                g.fillRect(xPos, 586, 32, 32);
                attackImages[x].draw(xPos, 586);
                if( moveSelected == x ) {
                    g.setColor( Color.white );
                    g.drawRect( xPos, 586, 32, 32 );
                }
            }
            else {
                g.setColor( black );
                attackImages[x].draw(xPos, 586);
                g.fillRect(xPos,586, 32,32  );
            }
            xPos+=64;
        }

        g.setColor( grey );
        g.fillRect(xPos, 586, 32, 32);
        this.healthPotion.draw( xPos, 586 );
        g.setColor( Color.white );

        g.drawString( ""+this.inventory.getHealthPotions(), xPos + 32, 580 );

        g.setColor( grey );
        xPos += 64;
        g.fillRect(xPos, 586, 32, 32);
        this.staminaPotion.draw( xPos, 586 );
        g.setColor( Color.white );
        g.drawString( ""+this.inventory.getStaminaPotions(), xPos + 32, 580 );

    }

    public void drawPlayerInfoPlayerScreen( Graphics g ) {

        // Calculate Percents
        double healthPercent = getHealth() / MAX_HEALTH;
        double staminaPercent = getStamina() / MAX_STAMINA;
        double experiencePercent = getExp() / getExpToLevelUp();

        g.setColor( red );
        g.fillRoundRect(67, 128, 191 * (float)healthPercent, 10, 10 );
        this.emptyHealth.draw( 60, 120 );

        g.setColor(green);
        g.fillRoundRect(67, 188, 191 * (float)staminaPercent, 10, 10 );
        this.emptyHealth.draw( 60, 180 );

        g.setColor( Color.blue );
        g.fillRoundRect( 67, 263, 423 * (float)experiencePercent, 5, 5 );
        this.emptyExpBar.draw( 60, 260 );


        g.setColor( Color.black );
        g.drawString( "" + (int)getHealth() + "/" + (int)this.MAX_HEALTH, 270, 123 );
        g.drawString( "" + (int)getStamina() + "/" + (int)this.MAX_STAMINA, 270, 183 );
        g.drawString( "" + (int)getExp() + "/" + (int)getExpToLevelUp(), 490, 255 );


    }

    public int findCurrentTile( float coord ) {
        float tile = coord/32;

        float decimalCoord = tile - (int)Math.floor( tile );

        if( decimalCoord >= 0.5 ) {
            tile++;
        }

        return (int)tile;
    }
}
