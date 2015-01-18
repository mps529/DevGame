package javagame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Hunter extends Player {

        // Max Health for hunter
    private static final double MAX_HEALTH = 100.0;
        // Max Stamina
    private static final double MAX_STAMINA = 100.0;
        // Max level
    private static final int MAX_LEVEL = 20;
        // Base Attack and Defend
    private static final int BASE_ATTACK = 8;
    private static final int BASE_DEFENCE = 3;

    private int attackStamina  =10;

        // Status Bar colors
    private Color red, blue, green, black, grey;

        // Experence needed to level up
    private double expToLevelUp;
        // Amount of experience
    private double exp;

        // 0-100 scale
    private double health;
        // 0-100 scale
    private double stamina;
        // Player level
    private int level;

    // Arrow Animations
    private Image[] arrows;

    private Image currentAttack;

    private Inventory inventory;


    private boolean inCombat;

    public Hunter( String sheetName, String name ) throws SlickException {

        super(  sheetName, name  );

        arrows = new Image[4];

        arrows[0] = new Image("NewEra-Beta/res/projectiles/Arrow-Up.png");
        arrows[1] = new Image("NewEra-Beta/res/projectiles/Arrow-Right.png");
        arrows[2] = new Image("NewEra-Beta/res/projectiles/Arrow-Down.png");
        arrows[3] = new Image("NewEra-Beta/res/projectiles/Arrow-Left.png");

        setProjectileImage( arrows );

        currentAttack = arrows[1];

        inventory = inventory.getPlayerInvintory();



        red = new Color( 225, 0, 0, .7f );
        green = new Color( 0,255,0, .7f );
        blue = new Color( 0,206,209, .7f );
        black = new Color( 0,0,0, .5f );
        grey = new Color( 0, 0, 0, .3f );

        setLevel( 1 );
        calculateExpToLevelUp();
        setHealth( 20 );
        setStamina( 20 );
        setExp( 0 );
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

    public void setStamina( double stamina ) {
        this.stamina = stamina;
    }
    public double getStamina() {
        return this.stamina;
    }
    public void decreaseStamina( double energy ) { this.stamina -= energy;  }
    public void decreaseStamina( ) { this.stamina -= this.attackStamina;
    }
    public void increaseStamina( double energy ) {
        this.stamina += energy;
        if( this.stamina > MAX_STAMINA ) {
            this.stamina = MAX_STAMINA;
        }
    }

    public void setExpToLevelUp( double exp ) { this.expToLevelUp = exp; }
    public double getExpToLevelUp() { return this.expToLevelUp; }
    public void calculateExpToLevelUp() {
        this.expToLevelUp = getLevel() * 20;
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
        if( getExp() >= getExpToLevelUp() ) {
                // Collect exp to continue it to next level
            double overFlowExp = getExp() - getExpToLevelUp();

            setExp( overFlowExp );
            calculateExpToLevelUp();
            levelUp();
        }
    }

    public void setLevel( int level ) { this.level = level; }
    public int getLevel() { return this.level; }
    public void levelUp() { this.level++; }

    public void drawPlayerInfo( Graphics g ) {

            // Calculate Percents
        double healthPercent = getHealth() / MAX_HEALTH;
        double staminaPercent = getStamina() / MAX_STAMINA;
        double experiencePercent = getExp() / getExpToLevelUp();

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
