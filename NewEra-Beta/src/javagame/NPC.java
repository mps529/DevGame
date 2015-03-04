package javagame;


import org.newdawn.slick.*;

import java.util.Random;

public class NPC extends NPCMovement {

        // Max Health
    private double MAX_HEALTH;
        // Max Stamina
    private double MAX_STAMINA;

        // Base Attack and Defend
    private int BASE_ATTACK ;
    private int BASE_DEFENCE;

        // overall attack and defence
    private int OVERALL_ATTACK;
    private int OVERALL_DEFENCE;

        // Attack Stamina and Damage
    private int attackStamina;
    private int attackDamage;

        // Player Health
    private double health;
        // Player Stamina
    private double stamina;
        // Player level
    private int npcLevel;

        // NPC's Inventory
    private Inventory inventory;

        // How long arrow will live for.
    private int FIRE_RATE = 250;
        // Index of the projectile array
    private int currentIndex = 0;
        // Time since last shot
    private int lastShot = 0;

        // If the player is in combat
    private boolean inCombat;

        // If NPC is alive;
    private boolean isAlive;
                         // 1,000,000
    private long deadTime = 1000000;

        // If should bge rendered
    private boolean willRender;

    private boolean good;

        // This is what skin the character has
    private int npcRace;

        //  npc stunned
    private int stunned = 0;
    private Animation stunnedAnimation;

        // This is the class that the npc is
        // 0=hunter 1=warrior 2=wizard 3=rogue
    private int npcClass;

    private Image[] projectileImage;

    private Color red;
    private Image healthBar;
        /*
        The player class is necessary to determine the players
        location for attacking/rendering/level/location
         */
    private Player player;

    public NPC () {
        super();
            // Set player to be dead
        this.isAlive = false;
        this.willRender = false;
    }

        // NPC Enemy with race
    public NPC( int race  ) {
        super();

            // Set the race
        this.npcRace = race;
            // Get player class
        this.player = this.player.getInstance();

        Random random = new Random();
        int playerLevel = this.player.getLevel();

        // Sets NPC level from a range of two above and two below.
        this.npcLevel = random.nextInt(  ( ( playerLevel + 2 ) - ( playerLevel - 2 ) ) + 1 ) + ( playerLevel - 2 );

        if( this.npcLevel <= 0 ) {
            this.npcLevel = 1;
        }

        // Setting the max health and stamina based on level
        this.MAX_HEALTH = 90 + ( this.npcLevel * 5 );
        this.MAX_STAMINA = 90 + ( this.npcLevel * 5 );

        // Setting the current npc health and stamina
        this.health = this.MAX_HEALTH;
        this.stamina = this.MAX_STAMINA;

            // Setting the class randomly
        this.npcClass = random.nextInt( 4 );
            // Setting stats and image
        try {
            setClassStat();
            setImages( race );
        }
        catch ( SlickException e ) {
            e.printStackTrace();
        }

            // Setting up the NPC Inventory
        this.inventory = new Inventory();
        this.inventory.setClassID( this.npcClass );
        this.inventory.setBaseAttack( this.BASE_ATTACK );
        this.inventory.setBaseDefence(this.BASE_DEFENCE);
        this.inventory.addEnemyNPCArmor( this.npcLevel );

            // Setting attack and defence
        this.OVERALL_ATTACK = this.inventory.getPlayerOverallAttack();
        this.OVERALL_DEFENCE = this.inventory.getPlayerOverallDefence();

        good = false;

        setNPCClass( "fancyOrk.png", 3 );

        this.isAlive = true;
        this.willRender = false;

        setNPCX( 89 );
        setNPCY( 99 );

        this.setPlayerDirection( 0 );
    }

        // NPC Enemy with race and class
    public NPC( int race, int npcClass  ) {

        super();
            // Set the race
        this.npcRace = race;

            // Get player class
        this.player = this.player.getInstance();

        Random random = new Random();
        int playerLevel = this.player.getLevel();

            // Sets NPC level from a range of two above and two below.
        this.npcLevel = random.nextInt(  ( ( playerLevel + 2 ) - ( playerLevel - 2 ) ) + 1 ) + ( playerLevel - 2 );

        if( this.npcLevel <= 0 ) {
            this.npcLevel = 1;
        }

            // Setting the max health and stamina based on level
        this.MAX_HEALTH = 90 + ( this.npcLevel * 5 );
        this.MAX_STAMINA = 90 + ( this.npcLevel * 5 );

            // Setting the current npc health and stamina
        this.health = this.MAX_HEALTH;
        this.stamina = this.MAX_STAMINA;

        this.npcClass = npcClass;


            // Setting stats and image
        try {
            setClassStat();
            setImages( race );
        }
        catch ( SlickException e ) {
            e.printStackTrace();
        }

            // Setting up the NPC Inventory
        this.inventory = new Inventory();
        this.inventory.setClassID( npcClass );
        this.inventory.setBaseAttack( this.BASE_ATTACK );
        this.inventory.setBaseDefence(this.BASE_DEFENCE);
        this.inventory.addEnemyNPCArmor( this.npcLevel );


            // Setting attack and defence
        this.OVERALL_ATTACK = this.inventory.getPlayerOverallAttack();
        this.OVERALL_DEFENCE = this.inventory.getPlayerOverallDefence();

        this.good = false;

        setNPCClass( "fancyOrk.png", 3 );

        this.isAlive = true;
        this.willRender = false;

        setNPCX( 80 );
        setNPCY( 99 );

        this.setPlayerDirection(3);

    }
        // Class set stats
    private void setClassStat() throws SlickException{
        switch ( this.npcClass ) {
                // Hunter
            case 0:
                projectileImage = new Image[4];
                projectileImage[0] = new Image("NewEra-Beta/res/projectiles/Arrow-Up.png");
                projectileImage[1] = new Image("NewEra-Beta/res/projectiles/Arrow-Right.png");
                projectileImage[2] = new Image("NewEra-Beta/res/projectiles/Arrow-Down.png");
                projectileImage[3] = new Image("NewEra-Beta/res/projectiles/Arrow-Left.png");

                this.BASE_ATTACK = 10;
                this.BASE_DEFENCE = 3;

                break;
                // Warrior
            case 1:
                this.BASE_ATTACK = 7;
                this.BASE_DEFENCE = 10;

                break;
                // Wizard
            case 2:
                projectileImage = new Image[4];
                projectileImage[0] = new Image("NewEra-Beta/res/projectiles/FireBall-Up.png");
                projectileImage[1] = new Image("NewEra-Beta/res/projectiles/FireBall-Right.png");
                projectileImage[2] = new Image("NewEra-Beta/res/projectiles/FireBall-Down.png");
                projectileImage[3] = new Image("NewEra-Beta/res/projectiles/FireBall-Left.png");

                this.BASE_ATTACK = 13;
                this.BASE_DEFENCE = 2;

                break;
                // Rouge
            case 3:
                this.BASE_ATTACK = 9;
                this.BASE_DEFENCE = 7;
        }
    }

    public boolean getIsAlive() { return this.isAlive; }

    private void setImages( int race ) throws SlickException {

        this.healthBar = new Image( "NewEra-Beta/res/enemies/EnemyHealth.png" );

        Image[] stunnedImages = new Image[2];
        stunnedImages[0] =  new Image( "NewEra-Beta/res/moves/stun.png" );
        stunnedImages[1] =  new Image( "NewEra-Beta/res/moves/stunFlipped.png" );
        int[] duration = { 400, 400 };
        this.stunnedAnimation = new Animation( stunnedImages, duration, true );

        this.red = new Color( 225, 0, 0, .7f );
    }
        // Inventory
    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory( Inventory inventory) {
        this.inventory = inventory;
    }

        // Combat
    public boolean getInCombat() { return this.inCombat; }
    public void setInCombat( boolean inCombat ) { this.inCombat = inCombat; }

        // Max health and stamina
    public double getMaxHealth() { return this.MAX_HEALTH; }
    public double getMaxStamina() { return this.MAX_STAMINA; }

        // Attack and defence
    public int getOverallAttack() { return this.OVERALL_ATTACK; }
    public int getOverallDefence() { return this.OVERALL_DEFENCE; }

        // Base attack and defence
    public int getBaseAttack() { return this.BASE_ATTACK; }
    public int getBaseDefence() { return this.BASE_DEFENCE; }

        // Health
    public double getHealth() {
        return this.health;
    }
    public void decreaseHealth( double damage ) {
        this.health -= damage;
    }
    public boolean checkDeath() {
        if( getHealth() <= 0 ) {
            this.health = 0;
            this.isAlive = false;
            Random rand  = new Random();
            this.player.increaseExp( ( this.npcLevel * 3 ) + rand.nextInt( 10 ) + 1 );
            return true;
        }
        return false;
    }

        // Stamina
    public double getStamina() {
        return this.stamina;
    }
    public void decreaseStamina( ) { this.stamina -= 10;  }

    public int getStunned() { return this.stunned; }
    public void setStunned() {
        this.stunned = 100;
    }
    public void setStunned( int stunned ) {
        this.stunned = stunned;
    }
    public void decreaseStunned( int delta ) {
        this.stunned -= delta;
        if( this.stunned < 0 ) {
            this.stunned = 0;
        }
    }

        // Exp
    public int getExp() {
        Random rand = new Random();
        return ( ( this.npcLevel + 3 ) * ( rand.nextInt( 3 ) + 1 ) );
    }

        // Used Potion
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

    public void takeDamage() {
        Random rand = new Random();

        int defence  = getOverallDefence();

        if( this.player.getOverallDefence() + 20 > defence ) {
            defence = getOverallDefence() + 20;
        }

        this.health -= (  (this.player.getOverallAttack() * ( rand.nextInt( 20 ) + 1 )   ) / defence  ) + this.player.getDamageOfCurrentAttack();

        if ( checkDeath() ) {
            startAnimationDeath();
            stopAnimationDeath();
        }
    }

    public void decreaseTimeLeftOnEarth ( int delta ) {
        this.deadTime -= delta;
    }

    public long getDeathTime( ) { return this.deadTime; }

    public boolean isGoodInSight( int[][] mapObjects ) {
        int x = (int)getNPCX()/32;
        int y = (int)getNPCY()/32;
        int direction = getDirection();
        int viewSpan;
        int blockCheck;

        int playerX = (int)this.player.getPlayerX()/32;
        int playerY = (int)this.player.getPlayerY()/32;

        if( direction == 0 ) {
            for( int i = y; i > y-6; i-- ) {
                if( i >= y-2) {
                    viewSpan = 7;
                }
                else if( i == y ) {
                    viewSpan = 3;
                }
                else {
                    viewSpan = 5;
                }

                blockCheck = x - viewSpan/2;
                while( viewSpan >= 0 ) {
                    if ( blockCheck == playerX && i == playerY ) {
                        return true;
                    }
                    viewSpan--;
                    blockCheck++;
                }
            }
        }
        else if( direction == 1 ) {
            for( int i = x; i < x+5; i++ ) {
                if( i >= x+2) {
                    viewSpan = 7;
                }
                else if( i == x ) {
                    viewSpan = 3;
                }
                else {
                    viewSpan = 5;
                }

                blockCheck = y - viewSpan/2;
                while( viewSpan >= 0 ) {
                    if ( i == playerX && blockCheck == playerY ) {
                        //System.out.println("Found");
                        return true;
                    }
                    viewSpan--;
                    blockCheck++;
                }
            }
        }
        else if( direction == 2 ) {

            for( int i = y; i < y+5; i++ ) {
                if( i >= y+2) {
                    viewSpan = 7;
                }
                else if( i == y ) {
                    viewSpan = 3;
                }
                else {
                    viewSpan = 5;
                }

                blockCheck = x - viewSpan/2;
                while( viewSpan >= 0 ) {
                    if ( blockCheck == playerX && i == playerY ) {
                        //System.out.println("Found");
                        return true;
                    }
                    viewSpan--;
                    blockCheck++;
                }
            }

        }
        else if( direction == 3 ) {
            for( int i = x; i > x-5; i-- ) {
                if( i <= x-2) {
                    viewSpan = 7;
                }
                else if( i == x ) {
                    viewSpan = 3;
                }
                else {
                    viewSpan = 5;
                }

                blockCheck = y - viewSpan/2;
                while( viewSpan >= 0 ) {
                    if ( i == playerX && blockCheck == playerY ) {
                        //System.out.println("Found");
                        return true;
                    }
                    viewSpan--;
                    blockCheck++;
                }
            }
        }

        return false;
    }

    public void drawNPC( Graphics g ) {

        if( this.deadTime > 0 ) {
            int npcX = (int)getNPCX();
            int npcY = (int)getNPCY();
            int playerX = (int)this.player.getPlayerX();
            int playerY = (int)this.player.getPlayerY();
            int x, y;

            x = npcX - playerX;
            y = npcY - playerY;

            if( ( ( 352 + x ) >= 0 && (352 + x ) <= 672 ) &&
                    ( ( 352 + y ) >= 0 && (352 + y ) <= 672 ) ) {

                double healthPercent = getHealth() / MAX_HEALTH;
                g.setColor(red);

                g.fillRect(328 + x, 319 + y, 16 * (float) healthPercent, 6);
                this.healthBar.draw(328 + x, 319 + y);

                if (this.isAlive) {
                    if( this.stunned > 0 ) {
                        this.stunnedAnimation.start();
                        this.stunnedAnimation.draw( 320 + x, 310 + y );
                    }
                    getMovingNPC().draw(320 + x, 320 + y);
                } else {
                    getDieingNPC().draw(320 + x, 320 + y);

                }
            }
        }
    }
   // public void drawPlayerDieing( float x, float y ) { npcDeath.draw( x, y );  }
}


