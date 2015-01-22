package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Random;

public class Hunter extends Player {

        // Max Health for hunter
    private static double MAX_HEALTH = 100.0;
        // Max Stamina
    private static double MAX_STAMINA = 100.0;
        // Max level
    private static final int MAX_LEVEL = 20;

        // Base Attack and Defend
    private static final int BASE_ATTACK = 8;
    private static final int BASE_DEFENCE = 3;

    private int minRunningStamina = 10;

        // The amount of stamina it takes to attack
    private int basicAttackStamina  = 10;
    private int powerAttackStamina  = 30;
    private int otherStamina  = 40;
    private int killMoveAttackStamina  = (int)MAX_STAMINA-20;

    /********************     Move Names are not final, this is just place holder     ********************/
    /*
        0 - Basic
        1- Power
        2- Other
        3 - Kill
    */
    private int moveSelected;

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

        // Arrow Animations
    private Image[] arrows;

        // Currently Selected Attack
    private Image currentAttack;
        // Players Inventory
    private Inventory inventory;

        // If the player is in combat
    private boolean inCombat;

    public Hunter( String sheetName, String name ) throws SlickException {
            // Call Player constructor
        super(  sheetName, name  );

            // Settign arrow animations
        arrows = new Image[4];
        arrows[0] = new Image("NewEra-Beta/res/projectiles/Arrow-Up.png");
        arrows[1] = new Image("NewEra-Beta/res/projectiles/Arrow-Right.png");
        arrows[2] = new Image("NewEra-Beta/res/projectiles/Arrow-Down.png");
        arrows[3] = new Image("NewEra-Beta/res/projectiles/Arrow-Left.png");
            // sets the projectile
        setProjectileImage( arrows );

            // This sets the display image for which attack is chosen
        currentAttack = arrows[1];

            // Set the color
        red = new Color( 225, 0, 0, .7f );
        green = new Color( 0,255,0, .7f );
        blue = new Color( 0,206,209, .7f );
        black = new Color( 0,0,0, .5f );
        grey = new Color( 0, 0, 0, .3f );

            // Set Player starting attributes
        setLevel( 1 );
        calculateExpToLevelUp();
        setHealth( MAX_HEALTH );
        setStamina( MAX_STAMINA );
        setExp( 0 );

        this.moveSelected = 0;

             // Set up player Inventory/ give default items
        inventory = inventory.getPlayerInvintory();
        inventory.setClassID( 0 );
        inventory.setBaseAttack( this.BASE_ATTACK );
        inventory.setBaseDefence( this.BASE_DEFENCE );
        inventory.setPlayerName( name );
    }

    public boolean getInCombat() { return this.inCombat; }
    public void setInCombat( boolean inCombat ) { this.inCombat = inCombat; }

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
            return this.basicAttackStamina;
        }
        else if( this.getMoveSelected() == 1 ) {
            return  this.powerAttackStamina;
        }
        else if( this.getMoveSelected() == 2 ) {
            return this.otherStamina;
        }
        else if( this.getMoveSelected() == 3 ) {
            return this.killMoveAttackStamina;
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
        calculateExpToLevelUp();
        increaseMaxHealth();
        increaseMaxStamina();
    }

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

            /*
                Shitty done Health System
             */
        g.setColor( grey );
        g.fillRect( 30, 16, 32, 32 );
        currentAttack.draw( 32, 16 );

        g.setColor( black );
        g.drawRoundRect(68, 23, 198, 10, 10);
        g.drawRoundRect(68, 38, 198, 10, 10 );
        g.drawRoundRect(32, 54, 235, 5, 10 );

        g.setColor( red );
        g.fillRoundRect(68, 23, 198 * (float)healthPercent, 10, 10 );
        g.setColor( green );
        g.fillRoundRect(68, 38, 198 * (float) staminaPercent, 10, 10);
        g.setColor( blue );
        g.fillRoundRect(32, 54, 235 * (float)experiencePercent, 5, 10 );


    }
}
