package javagame;


import org.newdawn.slick.*;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.Random;
import java.util.Vector;

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


    private static int npcCount = 0;
    private int id;

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

    private boolean isAttacking = false;

        // If NPC is alive;
    private boolean isAlive;
                         // 1,000,000
    private long deadTime = 1000000;

        // If should bge rendered
    private boolean willRender;

    private boolean good;

    private boolean hasAttacked = false;

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

        // Path Finding
    private AStarPathFinder pathFinder;
    private Path path;

        // -1 for player, 0 to whatever for index
    private int opponentsArrayLocation;

    private int sinceLastTurn = 0;
    private int TIME_TO_TURN = 1700;

    private int timeInBattle = 1000;

    public NPC () {
        super();
            // Set player to be dead
        this.isAlive = false;
        this.willRender = false;
    }

    public NPC(NPC other) {
        //minimal copy constructor only used when looting
        this.inventory = new Inventory(other.getInventory(), false );
        this.currentIndex = other.currentIndex;
        this.isAlive = other.isAlive;
        this.id = other.id;


    }
        // NPC Enemy with race
    public NPC( int race, boolean good ) {
        super();

        this.id = this.npcCount++;
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
        this.npcClass = random.nextInt( 2 );
        if( this.npcClass == 0 ) {
            this.npcClass = 1 ;
        }
        else {
            this.npcClass = 3;
        }
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
        this.inventory.setClassID(this.npcClass);
        this.inventory.setBaseAttack(this.BASE_ATTACK);
        this.inventory.setBaseDefence(this.BASE_DEFENCE);
        this.inventory.addEnemyNPCArmor( this.npcLevel );

            // Setting attack and defence
        this.OVERALL_ATTACK = this.inventory.getPlayerOverallAttack();
        this.OVERALL_DEFENCE = this.inventory.getPlayerOverallDefence();

        this.good = good;


        this.isAlive = true;
        this.willRender = false;


    }

    public void setImage( boolean villager ) {

        if( good ) {

            if( villager ) {
                setNPCClass("villager.png", 3);
            }
            else {
                setNPCClass("guard.png", 1);
            }

        }
        else {

            setNPCClass("fancyOrk.png", this.npcClass);
        }

        this.stopAnimationWalking();
        this.setPlayerDirection( 0 );

    }

        // NPC with race and class
    public NPC( int race, int npcClass, boolean good  ) {

        super();

        this.id = this.npcCount++;

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

        this.good = good;

        if( !good ) {
            setNPCClass("fancyOrk.png", 3);
        }
        else {
            setNPCClass("villager1.png", 2);
        }
        this.isAlive = true;
        this.willRender = false;

        this.stopAnimationWalking();
        this.setPlayerDirection(3);

    }

    public void setMapPath( Map map ) {
        pathFinder = new AStarPathFinder( map, 11, false );
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

                setProjectileImage( projectileImage );

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

                setProjectileImage( projectileImage );

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

    public void takeDamage( int direction ) {
        Random rand = new Random();

        int defence  = getOverallDefence();

        if( this.player.getOverallDefence() + 20 > defence ) {
            defence = getOverallDefence() + 20;
        }

        this.health -= (  (this.player.getInventory().getPlayerOverallAttack() * ( rand.nextInt( 20 ) + 1 )   ) / defence  ) + this.player.getDamageOfCurrentAttack() ;

        switch ( direction ) {
            case 0 :
                this.setPlayerDirection( 2 );
                break;
            case 1 :
                this.setPlayerDirection( 3 );
                break;
            case 2 :
                this.setPlayerDirection( 0 );
                break;
            case 3:
                this.setPlayerDirection( 1 );
                break;
        }

        this.inCombat = true;

        if ( checkDeath() ) {
            this.getInventory().unEquipAllItems();
            startAnimationDeath();
            stopAnimationDeath();
        }
    }

    public void decreaseTimeLeftOnEarth ( int delta ) {
        this.deadTime -= delta;
    }

    public long getDeathTime( ) { return this.deadTime; }
    public void setDeadTime(long time) { this.deadTime = time;}

    public int getId() {return this.id;}

    public boolean getIsAttacking() { return this.isAttacking; }

    public void facePlayer () {
        int x = findCurrentTile( getNPCX() );
        int y = findCurrentTile( getNPCY() );

        int playerX = findCurrentTile( this.player.getPlayerX()  );
        int playerY = findCurrentTile( this.player.getPlayerY()  );

        if( x < playerX && y == playerY ) {
            setPlayerDirection( 1 );
        }
        else if( x > playerX && y == playerY ) {
            setPlayerDirection( 3 );
        }
        else if( x == playerX && y < playerY ) {
            setPlayerDirection( 0 );
        }
        else if( x == playerX && y > playerY ) {
            setPlayerDirection( 2 );
        }
        else if( x < playerX ) {
            setPlayerDirection( 1 );
        }
        else if( x > playerX ) {
            setPlayerDirection( 3 );
        }
        else if(  y < playerY ) {
            setPlayerDirection( 0 );
        }
        else if( y > playerY ) {
            setPlayerDirection( 2 );
        }

    }

    public boolean isGoodInSight( int[][] mapObjects, Vector<NPC> npcs ) {
        int x = findCurrentTile( getNPCX() );
        int y = findCurrentTile( getNPCY() );
        int direction = getDirection();
        int viewSpan;
        int blockCheck;

        int playerX = findCurrentTile( this.player.getPlayerX() );
        int playerY = findCurrentTile( this.player.getPlayerY() );

        if( !this.good ) {
            if (direction == 0) {
                for (int i = y; i > y - 6; i--) {
                    if (i >= y - 2) {
                        viewSpan = 7;
                    } else if (i == y) {
                        viewSpan = 3;
                    } else {
                        viewSpan = 5;
                    }

                    blockCheck = x - viewSpan / 2;
                    while (viewSpan >= 0) {
                        if (blockCheck == playerX && i == playerY) {
                            this.inCombat = true;
                            this.opponentsArrayLocation = -1;
                            return true;
                        }
                        viewSpan--;
                        blockCheck++;
                    }
                }
            } else if (direction == 1) {
                for (int i = x; i < x + 6; i++) {
                    if (i >= x + 2) {
                        viewSpan = 7;
                    } else if (i == x) {
                        viewSpan = 3;
                    } else {
                        viewSpan = 5;
                    }

                    blockCheck = y - viewSpan / 2;
                    while (viewSpan >= 0) {
                        if (i == playerX && blockCheck == playerY) {
                            this.inCombat = true;
                            this.opponentsArrayLocation = -1;
                            return true;
                        }
                        viewSpan--;
                        blockCheck++;
                    }
                }
            } else if (direction == 2) {

                for (int i = y; i < y + 6; i++) {
                    if (i >= y + 2) {
                        viewSpan = 7;
                    } else if (i == y) {
                        viewSpan = 3;
                    } else {
                        viewSpan = 5;
                    }

                    blockCheck = x - viewSpan / 2;
                    while (viewSpan >= 0) {
                        if (blockCheck == playerX && i == playerY) {
                            this.inCombat = true;
                            this.opponentsArrayLocation = -1;
                            return true;
                        }
                        viewSpan--;
                        blockCheck++;
                    }
                }

            } else if (direction == 3) {
                for (int i = x; i > x - 6; i--) {
                    if (i <= x - 2) {
                        viewSpan = 7;
                    } else if (i == x) {
                        viewSpan = 3;
                    } else {
                        viewSpan = 5;
                    }

                    blockCheck = y - viewSpan / 2;
                    while (viewSpan >= 0) {
                        if (i == playerX && blockCheck == playerY) {
                            this.inCombat = true;
                            this.opponentsArrayLocation = -1;
                            return true;
                        }
                        viewSpan--;
                        blockCheck++;
                    }
                }
            }
        }
        else if( npcs != null ) {
            for( int iterator = 0; iterator < npcs.size(); iterator++ ) {

                if( npcs.elementAt(iterator).getIsAlive() == true ) {
                    playerX = findCurrentTile( npcs.elementAt(iterator).getNPCX());
                    playerY = findCurrentTile( npcs.elementAt(iterator).getNPCY());

                    if ((x - playerX < 10 && x - playerX > -10) && (y - playerY < 10 || y - playerY > -10)) {
                        if (direction == 0) {
                            for (int i = y; i > y - 6; i--) {
                                if (i >= y - 2) {
                                    viewSpan = 7;
                                } else if (i == y) {
                                    viewSpan = 3;
                                } else {
                                    viewSpan = 5;
                                }

                                blockCheck = x - viewSpan / 2;
                                while (viewSpan >= 0) {
                                    if (blockCheck == playerX && i == playerY) {
                                        this.inCombat = true;
                                        this.timeInBattle = 1000;
                                        this.opponentsArrayLocation = iterator;
                                        return true;
                                    }
                                    viewSpan--;
                                    blockCheck++;
                                }
                            }
                        } else if (direction == 1) {
                            for (int i = x; i < x + 6; i++) {
                                if (i >= x + 2) {
                                    viewSpan = 7;
                                } else if (i == x) {
                                    viewSpan = 3;
                                } else {
                                    viewSpan = 5;
                                }

                                blockCheck = y - viewSpan / 2;
                                while (viewSpan >= 0) {
                                    if (i == playerX && blockCheck == playerY) {
                                        this.inCombat = true;
                                        this.timeInBattle = 1000;
                                        this.opponentsArrayLocation = iterator;
                                        return true;
                                    }
                                    viewSpan--;
                                    blockCheck++;
                                }
                            }
                        } else if (direction == 2) {

                            for (int i = y; i < y + 6; i++) {
                                if (i >= y + 2) {
                                    viewSpan = 7;
                                } else if (i == y) {
                                    viewSpan = 3;
                                } else {
                                    viewSpan = 5;
                                }

                                blockCheck = x - viewSpan / 2;
                                while (viewSpan >= 0) {
                                    if (blockCheck == playerX && i == playerY) {
                                        this.inCombat = true;
                                        this.timeInBattle = 1000;
                                        this.opponentsArrayLocation = iterator;
                                        return true;
                                    }
                                    viewSpan--;
                                    blockCheck++;
                                }
                            }

                        } else if (direction == 3) {
                            for (int i = x; i > x - 6; i--) {
                                if (i <= x - 2) {
                                    viewSpan = 7;
                                } else if (i == x) {
                                    viewSpan = 3;
                                } else {
                                    viewSpan = 5;
                                }

                                blockCheck = y - viewSpan / 2;
                                while (viewSpan >= 0) {
                                    if (i == playerX && blockCheck == playerY) {
                                        this.inCombat = true;
                                        this.timeInBattle = 1000;
                                        this.opponentsArrayLocation = iterator;
                                        return true;
                                    }
                                    viewSpan--;
                                    blockCheck++;
                                }
                            }
                        }
                    }
                }
            }
        }
        if( this.inCombat = true && this.timeInBattle > 0 ) {
            this.timeInBattle -= 2;
        }
        else if( this.inCombat = true ) {
            this.timeInBattle = 1000;
            this.inCombat = false;
        }

        return false;
    }

    public int getEnemy() { return this.opponentsArrayLocation; }

    public void goToGood( int enemyX, int enemyY ) {

        int npcX = findCurrentTile( getNPCX() );
        int npcY = findCurrentTile( getNPCY() );

        this.path = this.pathFinder.findPath( null, npcX, npcY, findCurrentTile( enemyX ), findCurrentTile( enemyY ) );


        if(  this.path != null && this.path.getLength() > 1  ) {



            int x = this.path.getX(1);
            int y = this.path.getY(1);

           // System.out.println( "NPC X: " + npcX + ", NPC Y: " + npcY + " Dest x: " + x + "Dest y: " + y + "\n" );

            this.startAnimationWalking();

            if( x != npcX )  {
                if( x < npcX ) {
                    this.decrementNPCX();
                    this.setPlayerDirection( 3 );
                }
                else {
                    this.incrementNPCX();
                    this.setPlayerDirection( 1 );
                }
             }
            else if( y != npcY ) {
                if( y < npcY ) {
                    this.decrementNPCY();
                    this.setPlayerDirection( 0 );
                }
                else {
                    this.incrementNPCY();
                    this.setPlayerDirection( 2 );
                }
            }
            else {
                this.stopAnimationWalking();
            }
        }
        else {
            int playerX = enemyX;
            int playerY = enemyY;

            if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY - 24 && npcY <= playerY)) {
                this.setPlayerDirection( 0 );
            }
            else if ((npcX >= playerX && npcX <= playerX + 24) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                this.setPlayerDirection( 1 );
            }
            else if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY && npcY <= playerY + 24)) {
                this.setPlayerDirection( 2 );
            }
            else if ((npcX >= playerX - 24 && npcX <= playerX ) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                this.setPlayerDirection( 3 );
            }
            else {
                npcX = (int)getNPCX();
                npcY = (int)getNPCY();

                int directionX = npcX - playerX;
                int directionY = npcY - playerY;

                if( directionX > 24 ) {
                    this.decrementNPCX();
                    this.setPlayerDirection( 3 );
                }
                else if( directionX < -24 ) {
                    this.incrementNPCX();
                    this.setPlayerDirection( 1 );
                }
                else if( directionY > 24 ) {
                    this.decrementNPCY();
                    this.setPlayerDirection( 0 );
                }
                else if( directionY < -24 ) {
                    this.incrementNPCY();
                    this.setPlayerDirection( 2 );
                }
                else {

                }
            }

        }

    }

    public void lookAround( int delta ) {
        Random rand = new Random();

        this.sinceLastTurn += delta * .5f;

        if( this.sinceLastTurn > this.TIME_TO_TURN * (rand.nextInt(1) +  1 ) ) {
            this.setPlayerDirection(rand.nextInt(4));
            this.sinceLastTurn = 0;
        }
    }

    public boolean isGood(  ) { return this.good; }

    public int getNpcClass() { return this.npcClass; }
    public boolean getHasAttacked() { return this.hasAttacked; }
    public void setHasAttacked( boolean hasAttacked) { this.hasAttacked = hasAttacked; }

    public boolean closeEnoughToAttack( int x, int y ) {

        float playerX = x;
        float playerY = y;
        float npcX = getNPCX();
        float npcY = getNPCY();

            // Hunter attacks
        if( this.npcClass == 0 ||  this.npcClass == 2  ) {
            if( !this.isAttacking ) {
                if (this.getDirection() == 0) {
                    if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY  && npcY <= playerY + 216 )) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                } else if (this.getDirection() == 1) {
                    if ((npcX >= playerX -216 && npcX <= playerX ) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                } else if (this.getDirection() == 2) {
                    if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY - 216 && npcY <= playerY )) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                } else if (this.getDirection() == 3) {
                    if ((npcX >= playerX  && npcX <= playerX + 216 ) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                }
                this.stopAnimationAttacking();
            }
            else {
                this.hasAttacked = true;
                this.isAttacking = false;

            }
            return false;
        }
        else {

            if (!this.isAttacking) {

                if (this.getDirection() == 0) {
                    if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY && npcY <= playerY + 24)) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                } else if (this.getDirection() == 1) {
                    if ((npcX >= playerX - 24 && npcX <= playerX) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                } else if (this.getDirection() == 2) {
                    if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY - 24 && npcY <= playerY)) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                } else if (this.getDirection() == 3) {
                    if ((npcX >= playerX && npcX <= playerX + 24) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                        this.startAnimationAttacking();
                        this.stopAnimationAttacking();
                        this.isAttacking = true;
                        return true;
                    }
                }
                this.stopAnimationAttacking();
            } else {
                if ( isStoppedAttacking()) {
                    if (this.getDirection() == 0) {
                        if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY && npcY <= playerY + 24)) {
                            if (opponentsArrayLocation == -1) {
                                this.player.takeDamage(this.OVERALL_ATTACK, this.attackDamage);
                            }
                            this.isAttacking = false;
                            return true;
                        }
                    } else if (this.getDirection() == 1) {
                        if ((npcX >= playerX - 24 && npcX <= playerX) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                            if (opponentsArrayLocation == -1) {
                                this.player.takeDamage(this.OVERALL_ATTACK, this.attackDamage);
                            }
                            this.isAttacking = false;
                            return true;
                        }
                    } else if (this.getDirection() == 2) {
                        if ((npcX >= playerX - 24 && npcX <= playerX + 24) && (npcY >= playerY - 24 && npcY <= playerY)) {
                            if (opponentsArrayLocation == -1) {
                                this.player.takeDamage(this.OVERALL_ATTACK, this.attackDamage);
                            }
                            this.isAttacking = false;
                            return true;
                        }
                    } else if (this.getDirection() == 3) {
                        if ((npcX >= playerX && npcX <= playerX + 24) && (npcY >= playerY - 24 && npcY <= playerY + 24)) {
                            if (opponentsArrayLocation == -1) {
                                this.player.takeDamage(this.OVERALL_ATTACK, this.attackDamage);
                            }
                            this.isAttacking = false;
                            return true;
                        }

                    }
                    this.isAttacking = false;
                }
            }
        }
        return false;
    }

    public int findCurrentTile( float coord ) {
        float tile = coord/32;

        float decimalCoord = tile - (int)Math.floor( tile );

        if( decimalCoord >= 0.5 ) {
            tile++;
        }

        return (int)tile;
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

                if( this.inCombat ) {
                    double healthPercent = getHealth() / MAX_HEALTH;
                    g.setColor(red);

                    g.fillRect(328 + x, 319 + y, 16 * (float) healthPercent, 6);
                    this.healthBar.draw(328 + x, 319 + y);
                }
                if (this.isAlive) {
                    if( this.stunned > 0 ) {
                        this.stunnedAnimation.start();
                        this.stunnedAnimation.draw( 320 + x, 310 + y );
                    }
                    if( this.isAttacking ) {
                        getAttackingNPC().draw(320 + x, 320 + y);
                    }
                    else {
                        getMovingNPC().draw(320 + x, 320 + y);
                    }
                } else {
                    getDieingNPC().draw(320 + x, 320 + y);

                }
            }
        }
    }
   // public void drawPlayerDieing( float x, float y ) { npcDeath.draw( x, y );  }
    public boolean equals(NPC other) {
        if(other.getId() == this.getId()) {
            return true;
        }
        else {
            return false;
        }
    }
}


