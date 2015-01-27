package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class PlayerClass extends Player {

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
    private int attackOne;
    private int attackTwo;
    private int attackThree;
    private int attackFour;

    /*
        0 - attackOne
        1- attackTwo
        2- attackThree
        3 - attackFour
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

    // projectile Animations
     private Image[] projectileImage;

    // Currently Selected Attack
    private Image currentAttack;
    // Players Inventory
    private Inventory inventory;

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

    // If the player is in combat
    private boolean inCombat;

    private static PlayerClass playerClass = null;

    public PlayerClass( String sheetName, String name, int classID ) throws SlickException {

        // Call Player constructor
        super(  sheetName, name, classID );

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
        black = new Color( 0,0,0, .5f );
        grey = new Color( 0, 0, 0, .3f );

        // Set Player starting attributes
        setLevel( 1 );
        calculateExpToLevelUp();
        setHealth( 80 );
        setStamina( MAX_STAMINA );
        setExp( expToLevelUp - 10 );

        this.moveSelected = 0;

        // Set up player Inventory/ give default items
        inventory = new Inventory( );
        inventory.setBaseAttack( this.BASE_ATTACK );
        inventory.setBaseDefence( this.BASE_DEFENCE );
        inventory.setClassID( classID );

        this.emptyHealth = new Image( "NewEra-Beta/res/dash/EmptyBar.png" );
        this.emptyExpBar = new Image( "NewEra-Beta/res/dash/EmptyBarLong.png" );
    }

    public static PlayerClass getInstance() {
        return playerClass;
    }
    public static PlayerClass createInstance( String sheetName, String name, int classID ) throws SlickException  {
        if( playerClass == null ) {
            playerClass = new PlayerClass( sheetName, name, classID );
        }
        return playerClass;
    }

    private void setHunter() throws SlickException {
        // Setting arrow animations
        projectileImage = new Image[4];
        projectileImage[0] = new Image("NewEra-Beta/res/projectiles/Arrow-Up.png");
        projectileImage[1] = new Image("NewEra-Beta/res/projectiles/Arrow-Right.png");
        projectileImage[2] = new Image("NewEra-Beta/res/projectiles/Arrow-Down.png");
        projectileImage[3] = new Image("NewEra-Beta/res/projectiles/Arrow-Left.png");
        // sets the projectile
        setProjectileImage( projectileImage );

        // This sets the display image for which attack is chosen
        currentAttack = projectileImage[1];

        this.attackImages[0] =  projectileImage[1];
        this.attackImages[1] = new Image( "NEwEra-Beta/res/moves/trap.png" );
        this.attackImages[2] = new Image( "NEwEra-Beta/res/moves/bush.png" );
        this.attackImages[3 ]= new Image( "NEwEra-Beta/res/projectiles/Double-Arrow-Right.png" );

        this.attacksKnown[0] = 1;
        this.attacksKnown[1] = -1;
        this.attacksKnown[2] = -1;
        this.attacksKnown[3] = -1;

        this.MAX_HEALTH = 100;
        this.MAX_STAMINA = 120;
        this.BASE_ATTACK = 13;
        this.BASE_DEFENCE = 5;

        this.attackOne = 10;
        this.attackTwo = 20;
        this.attackThree = 30;
        this.attackFour = 40;

    }
    private void setWarrior() throws SlickException {
            // This sets the display image for which attack is chosen
        currentAttack = new Image("NewEra-Beta/res/items/spear.png");
        this.MAX_HEALTH = 120;
        this.MAX_STAMINA = 110;
        this.BASE_ATTACK = 7;
        this.BASE_DEFENCE = 10;

        this.attackOne = 10;
        this.attackTwo = 20;
        this.attackThree = 30;
        this.attackFour = 40;
    }
    private void setWizard() throws SlickException {
        // Setting projectileImage animations
        projectileImage = new Image[4];
        projectileImage[0] = new Image("NewEra-Beta/res/projectiles/FireBall-Up.png");
        projectileImage[1] = new Image("NewEra-Beta/res/projectiles/FireBall-Right.png");
        projectileImage[2] = new Image("NewEra-Beta/res/projectiles/FireBall-Down.png");
        projectileImage[3] = new Image("NewEra-Beta/res/projectiles/FireBall-Left.png");
        // sets the projectile
        setProjectileImage( projectileImage );

        // This sets the display image for which attack is chosen
        currentAttack = projectileImage[1];

        this.MAX_HEALTH = 90;
        this.MAX_STAMINA = 110;
        this.BASE_ATTACK = 15;
        this.BASE_DEFENCE = 5;

        this.attackOne = 10;
        this.attackTwo = 20;
        this.attackThree = 30;
        this.attackFour = 40;

    }
    private void setRouge() throws SlickException {
            // This sets the display image for which attack is chosen
        currentAttack = new Image("NewEra-Beta/res/items/dagger.png");

        this.MAX_HEALTH = 100;
        this.MAX_STAMINA = 100;
        this.BASE_ATTACK = 11;
        this.BASE_DEFENCE = 9;

        this.attackOne = 10;
        this.attackTwo = 20;
        this.attackThree = 30;
        this.attackFour = 40;
    }

    public Image[] getAttackImages() { return this.attackImages; }
    public int[] getAttacksKnown() { return this.attacksKnown; }

    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory( Inventory inventory) {
        this.inventory = inventory;
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

    public int getOverallDefence() { return this.OVERALL_DEFENCE; }
    public void setOverallDefence( int defence ) { this.OVERALL_DEFENCE = defence; }

    public void setBaseAttack( int attack ) { this.BASE_ATTACK = attack; }
    public void increaseBaseAttack( int increase ) { this.BASE_ATTACK += increase; }
    public int getBaseAttack() { return this.BASE_ATTACK; }

    public void setBaseDefence( int attack ) { this.BASE_DEFENCE = attack; }
    public void increaseBaseDefence( int increase ) { this.BASE_DEFENCE += increase; }
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
    public void increaseMaxHealth( ) {
        Random random = new Random();
        MAX_HEALTH += random.nextInt( 20 ) + 5;
    }

    public void setMoveSelected( int move ) { this.moveSelected = move; }
    public int getMoveSelected( ) { return this.moveSelected; }

    public void setStamina( double stamina ) {
        this.stamina = stamina;
    }
    public double getStamina() {
        return this.stamina;
    }
    public int getAttackStamina() {
        if( this.getMoveSelected() == 0 ) {
            return this.attackOne;
        }
        else if( this.getMoveSelected() == 1 ) {
            return  this.attackTwo;
        }
        else if( this.getMoveSelected() == 2 ) {
            return this.attackThree;
        }
        else if( this.getMoveSelected() == 3 ) {
            return this.attackFour;
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
        if( getHealth() <= 0 ) {
            return true;
        }
        return false;
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
        this.perkPoints = 2;
        this.movePoints = 1;
        calculateExpToLevelUp();
        increaseMaxHealth();
        increaseMaxStamina();
    }

    public int getPerkPoints() { return this.perkPoints; }
    public int getMovePoints() { return this.movePoints; }

    public boolean isWeaponEqiupped () {
        return this.inventory.isWeaponEquipped();
    }

    public void updateAttack( int delta, boolean attacked, Map map ) {
        // Update projectiles position
        updateProjectile( delta, attacked, map );
    }

    public void drawPlayerInfo( Graphics g ) {

        // Calculate Percents
        double healthPercent = getHealth() / MAX_HEALTH;
        double staminaPercent = getStamina() / MAX_STAMINA;
        double experiencePercent = getExp() / getExpToLevelUp();

        g.setColor( grey );
        g.fillRect( 30, 16, 32, 32 );
        currentAttack.draw( 32, 16 );


        g.setColor( red );
        g.fillRoundRect(111, 558, 191 * (float)healthPercent, 10, 10 );
        this.emptyHealth.draw( 104, 550 );

        g.setColor( green );
        g.fillRoundRect(328, 558, 191 * (float)staminaPercent, 10, 10 );
        this.emptyHealth.draw( 320, 550 );



        g.setColor( Color.blue );
        g.fillRoundRect( 110, 623, 423 * (float)experiencePercent, 4, 5 );
        this.emptyExpBar.draw( 104, 620 );
    }
}
