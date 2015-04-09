package javagame;

import org.lwjgl.Sys;
import org.newdawn.slick.*;

import java.util.Vector;

public class PlayerAttack {

        // Getting Player
    private static Player player;

    private SpriteSheet playerSpriteSheet;

    //warrior
    private Sound spearAttack, stun, berserk;
    //wizard
    private Sound fireball, transmute, summonSpell, immolate;
    //hunter
    private Sound bowShot, trap, bushStealth, doubleShot;
    //rogue
    private Sound daggerSwing, shurikenThrow, cloak, poison;
        // Did the player Attack
    private boolean isAttacking;
    private boolean isSneaking = false;
    private boolean isBeserk = false;
    private boolean renderFire = false;

    private Animation currentAttack;
    private Animation spellAttackUp, spellAttackDown, spellAttackLeft, spellAttackRight;
    private Animation arrowAttackUp, arrowAttackDown, arrowAttackLeft, arrowAttackRight;
    private Animation thrustAttackUp, thrustAttackDown, thrustAttackLeft, thrustAttackRight;
    private Animation swipeAttackUp, swipeAttackDown, swipeAttackLeft, swipeAttackRight;

    private Animation fire;

    public Animation bush;

    public Animation shadow;

    boolean isHidding = false;

        // Indicates which frame to stop at
    private int frameStop = 0;

    private int[] durationArrowSpeed = { 60,60,60,60,60,60,60,60,60,60,60,60,60 };
    private int[] durationSpellSpeed = { 60,60,60,60,60,60,60 };
    private int[] durationSwipeSpeed = { 60,60,60,60,60,60  };
    private int[] durationThrustSpeed = { 60,60,60,60,60,60,60,60 };
    private int[] durationSpeedDeath = { 80,80,80,80,80,80 };
    private int[] durationfire = { 150, 150 };
    private int[] durationBush = { 500, 500 };


        // time a summed is to be alive for
    private int summonedTime = 8000;
    private boolean summonedAlive = false;

    // Constructor
    public PlayerAttack( ) {
            // Gets the player Class
        this.player = this.player.getInstance();
            // Sets the player to be attacking
        this.loadSounds();
        this.isAttacking = false;


    }

    public void setPlayerSpriteSheet( SpriteSheet playerSpriteSheet ) { this.playerSpriteSheet = playerSpriteSheet; }

    public void setAttackSprite() {
        // Sets images for attacking animations
        Image[] upAttackArrow = { playerSpriteSheet.getSubImage(0, 16), playerSpriteSheet.getSubImage(1, 16), playerSpriteSheet.getSubImage(2, 16), playerSpriteSheet.getSubImage(3, 16), playerSpriteSheet.getSubImage(4, 16), playerSpriteSheet.getSubImage(5, 16), playerSpriteSheet.getSubImage(6, 16), playerSpriteSheet.getSubImage(7, 16), playerSpriteSheet.getSubImage(8, 16), playerSpriteSheet.getSubImage(9, 16), playerSpriteSheet.getSubImage(10, 16), playerSpriteSheet.getSubImage(11, 16), playerSpriteSheet.getSubImage(12, 16)};
        Image[] leftAttackArrow = { playerSpriteSheet.getSubImage(0, 17), playerSpriteSheet.getSubImage(1, 17), playerSpriteSheet.getSubImage(2, 17), playerSpriteSheet.getSubImage(3, 17), playerSpriteSheet.getSubImage(4, 17), playerSpriteSheet.getSubImage(5, 17), playerSpriteSheet.getSubImage(6, 17), playerSpriteSheet.getSubImage(7, 17), playerSpriteSheet.getSubImage(8, 17), playerSpriteSheet.getSubImage(9, 17), playerSpriteSheet.getSubImage(10, 17), playerSpriteSheet.getSubImage(11, 17), playerSpriteSheet.getSubImage(12, 17)};
        Image[] downAttackArrow = { playerSpriteSheet.getSubImage(0, 18), playerSpriteSheet.getSubImage(1, 18), playerSpriteSheet.getSubImage(2, 18), playerSpriteSheet.getSubImage(3, 18), playerSpriteSheet.getSubImage(4, 18), playerSpriteSheet.getSubImage(5, 18), playerSpriteSheet.getSubImage(6, 18), playerSpriteSheet.getSubImage(7, 18), playerSpriteSheet.getSubImage(8, 18), playerSpriteSheet.getSubImage(9, 18), playerSpriteSheet.getSubImage(10, 18), playerSpriteSheet.getSubImage(11, 18), playerSpriteSheet.getSubImage(12, 18)};
        Image[] rightAttackArrow = { playerSpriteSheet.getSubImage(0, 19), playerSpriteSheet.getSubImage(1, 19), playerSpriteSheet.getSubImage(2, 19), playerSpriteSheet.getSubImage(3, 19), playerSpriteSheet.getSubImage(4, 19), playerSpriteSheet.getSubImage(5, 19), playerSpriteSheet.getSubImage(6, 19), playerSpriteSheet.getSubImage(7, 19), playerSpriteSheet.getSubImage(8, 19), playerSpriteSheet.getSubImage(9, 19), playerSpriteSheet.getSubImage(10, 19), playerSpriteSheet.getSubImage(11, 19), playerSpriteSheet.getSubImage(12, 19)};
        // Assigning the Images to the animations
        this.arrowAttackUp = new Animation(upAttackArrow, durationArrowSpeed, true);
        this.arrowAttackRight = new Animation(rightAttackArrow, durationArrowSpeed, true);
        this.arrowAttackDown = new Animation(downAttackArrow, durationArrowSpeed, true);
        this.arrowAttackLeft = new Animation(leftAttackArrow, durationArrowSpeed, true);

        // Sets images for attacking animations
        Image[] upAttackThrust = {playerSpriteSheet.getSubImage(0, 4), playerSpriteSheet.getSubImage(1, 4), playerSpriteSheet.getSubImage(2, 4), playerSpriteSheet.getSubImage(3, 4), playerSpriteSheet.getSubImage(4, 4), playerSpriteSheet.getSubImage(5, 4), playerSpriteSheet.getSubImage(6, 4), playerSpriteSheet.getSubImage(7, 4)   };
        Image[] leftAttackThrust = {playerSpriteSheet.getSubImage(0, 5), playerSpriteSheet.getSubImage(1, 5), playerSpriteSheet.getSubImage(2, 5), playerSpriteSheet.getSubImage(3, 5), playerSpriteSheet.getSubImage(4, 5), playerSpriteSheet.getSubImage(5, 5), playerSpriteSheet.getSubImage(6, 5), playerSpriteSheet.getSubImage(7, 5)  };
        Image[] downAttackThrust = {playerSpriteSheet.getSubImage(0, 6), playerSpriteSheet.getSubImage(1, 6), playerSpriteSheet.getSubImage(2, 6), playerSpriteSheet.getSubImage(3, 6), playerSpriteSheet.getSubImage(4, 6), playerSpriteSheet.getSubImage(5, 6), playerSpriteSheet.getSubImage(6, 6) , playerSpriteSheet.getSubImage(7, 6) };
        Image[] rightAttackThrust = {playerSpriteSheet.getSubImage(0, 7), playerSpriteSheet.getSubImage(1, 7), playerSpriteSheet.getSubImage(2, 7), playerSpriteSheet.getSubImage(3, 7), playerSpriteSheet.getSubImage(4, 7), playerSpriteSheet.getSubImage(5, 7), playerSpriteSheet.getSubImage(6, 7), playerSpriteSheet.getSubImage(7, 7)  };
        // Assigning the Images to the animations
        this.thrustAttackUp = new Animation(upAttackThrust, durationThrustSpeed, true);
        this.thrustAttackRight = new Animation(rightAttackThrust, durationThrustSpeed, true);
        this.thrustAttackDown = new Animation(downAttackThrust, durationThrustSpeed, true);
        this.thrustAttackLeft = new Animation(leftAttackThrust, durationThrustSpeed, true);


        // Sets images for attacking animations
        Image[] upAttackSwipe = {playerSpriteSheet.getSubImage(0, 12), playerSpriteSheet.getSubImage(1, 12), playerSpriteSheet.getSubImage(2, 12), playerSpriteSheet.getSubImage(3, 12), playerSpriteSheet.getSubImage(4, 12), playerSpriteSheet.getSubImage(5, 12) };
        Image[] leftAttackSwipe = {playerSpriteSheet.getSubImage(0, 13), playerSpriteSheet.getSubImage(1, 13), playerSpriteSheet.getSubImage(2, 13), playerSpriteSheet.getSubImage(3, 13), playerSpriteSheet.getSubImage(4, 13), playerSpriteSheet.getSubImage(5, 13) };
        Image[] downAttackSwipe = {playerSpriteSheet.getSubImage(0, 14), playerSpriteSheet.getSubImage(1, 14), playerSpriteSheet.getSubImage(2, 14), playerSpriteSheet.getSubImage(3, 14), playerSpriteSheet.getSubImage(4, 14), playerSpriteSheet.getSubImage(5, 14) };
        Image[] rightAttackSwipe = {playerSpriteSheet.getSubImage(0, 15), playerSpriteSheet.getSubImage(1, 15), playerSpriteSheet.getSubImage(2, 15), playerSpriteSheet.getSubImage(3, 15), playerSpriteSheet.getSubImage(4, 15), playerSpriteSheet.getSubImage(5, 15) };
        // Assigning the Images to the animations
        this.swipeAttackUp = new Animation(upAttackSwipe, durationSwipeSpeed, true);
        this.swipeAttackRight = new Animation(rightAttackSwipe, durationSwipeSpeed, true);
        this.swipeAttackDown = new Animation(downAttackSwipe, durationSwipeSpeed, true);
        this.swipeAttackLeft = new Animation(leftAttackSwipe, durationSwipeSpeed, true);

        // Sets images for attacking animations
        Image[] upAttackSpell = {playerSpriteSheet.getSubImage(0, 0), playerSpriteSheet.getSubImage(1, 0), playerSpriteSheet.getSubImage(2, 0), playerSpriteSheet.getSubImage(3, 0), playerSpriteSheet.getSubImage(4, 0), playerSpriteSheet.getSubImage(5, 0), playerSpriteSheet.getSubImage(6, 0) };
        Image[] leftAttackSpell = {playerSpriteSheet.getSubImage(0, 1), playerSpriteSheet.getSubImage(1, 1), playerSpriteSheet.getSubImage(2, 1), playerSpriteSheet.getSubImage(3, 1), playerSpriteSheet.getSubImage(4, 1), playerSpriteSheet.getSubImage(5, 1), playerSpriteSheet.getSubImage(6, 1)};
        Image[] downAttackSpell = {playerSpriteSheet.getSubImage(0, 2), playerSpriteSheet.getSubImage(1, 2), playerSpriteSheet.getSubImage(2, 2), playerSpriteSheet.getSubImage(3, 2), playerSpriteSheet.getSubImage(4, 2), playerSpriteSheet.getSubImage(5, 2), playerSpriteSheet.getSubImage(6, 2) };
        Image[] rightAttackSpell = {playerSpriteSheet.getSubImage(0, 3), playerSpriteSheet.getSubImage(1, 3), playerSpriteSheet.getSubImage(2, 3), playerSpriteSheet.getSubImage(3, 3), playerSpriteSheet.getSubImage(4, 3), playerSpriteSheet.getSubImage(5, 3), playerSpriteSheet.getSubImage(6, 3) };
        // Assigning the Images to the animations
        this.spellAttackUp = new Animation( upAttackSpell, durationSpellSpeed, true );
        this.spellAttackRight = new Animation( rightAttackSpell, durationSpellSpeed, true );
        this.spellAttackDown = new Animation( downAttackSpell, durationSpellSpeed, true );
        this.spellAttackLeft = new Animation( leftAttackSpell, durationSpellSpeed, true );

        try {
            Image[] bushImage = {new Image("NewEra-Beta/res/moves/bush.png"), new Image("NewEra-Beta/res/moves/bushLeft.png")};
            Image[] shadowImage = { new Image( "NewEra-Beta/res/moves/shadow.png" ),  new Image( "NewEra-Beta/res/moves/shadow.png" ) };
            this.bush = new Animation( bushImage, durationBush, true );
            this.shadow = new Animation( shadowImage, durationBush, true );

            Image[] fireImage = {new Image("NewEra-Beta/res/moves/fireOne.png"), new Image("NewEra-Beta/res/moves/fireTwo.png")};
            this.fire = new Animation( fireImage, durationfire, true );
        }
        catch ( SlickException e ) {
            e.printStackTrace();
        }

        this.currentAttack = this.swipeAttackDown;
    }
    public boolean getIsAttacking() { return this.isAttacking; }
    public void setIsAttacking( boolean attacking ) { this.isAttacking = attacking; }

    public boolean renderFire() { return this.renderFire; }
    public boolean isSneaking() { return this.isSneaking; }
    public boolean isBeserk() { return this.isBeserk; }
    public void setBeserk( boolean beserk ) { this.isBeserk = beserk; }

    public boolean isDoneAttacking( Input input, int delta, Vector<NPC> enemies, Map map  ) {

        if( this.player.getPlayerClass() == 0 ) {
            return isHunterDone( input, delta );
        }
        else if( this.player.getPlayerClass() == 1 ) {
            return isWarriorDone(input, delta, enemies);
        }
        else if( this.player.getPlayerClass() == 2 ) {
            return isWizardDone(input, delta, enemies, map );
        }
        else if( this.player.getPlayerClass() == 3 ) {
            return isRougeDone(input, delta,enemies);
        }

        return false;
    }

    public void attack(  ) {
        setIsAttacking(true);

        if( !this.isBeserk ) {
            this.player.decreaseStamina();
        }

        if( this.player.getPlayerClass() == 0 ) {
            hunterAttacks( );
        }
        else if( this.player.getPlayerClass() == 1 ) {
            warriorAttacks(  );
        }
        else if( this.player.getPlayerClass() == 2 ) {
            wizardAttacks( );
        }
        else if( this.player.getPlayerClass() == 3 ) {
            rogueAttacks(  );
        }

        //startAnimationAttacking();
    }
    private void hunterAttacks(  ) {
        int moveSelected = this.player.getMoveSelected();
        // 0-Up, 1-Right, 2-Down, 3-Left
        int direction = this.player.getDirection();

            // Arrow
        if( moveSelected == 0 ) {
            if( direction == 0 ) {
                currentAttack = arrowAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = arrowAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = arrowAttackDown;
            }
            else {
                currentAttack = arrowAttackLeft;
            }
            this.frameStop = 12;
            bowShot.play();
        }
            // Trap
        else if( moveSelected == 1 ) {
            if( direction == 0 ) {
                currentAttack = thrustAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = thrustAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = thrustAttackDown;
            }
            else {
                currentAttack = thrustAttackLeft;
            }
            this.frameStop = 7;
        }
            // hide in bush
        else if( moveSelected == 2 ) {
            currentAttack = this.bush;
            this.frameStop = 2;
        }
            // Double arrow
        else if( moveSelected == 3 ) {
            // set projectile to double arrow 2.5x damage
            if( direction == 0 ) {
                currentAttack = arrowAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = arrowAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = arrowAttackDown;
            }
            else {
                currentAttack = arrowAttackLeft;
            }
            this.frameStop = 12;
        }
    }
    private void warriorAttacks(  ) {
        int moveSelected = this.player.getMoveSelected();
        // 0-Up, 1-Right, 2-Down, 3-Left
        int direction = this.player.getDirection();

        // Basic Thrust
        if( moveSelected == 0 ) {
            if( direction == 0 ) {
                currentAttack = thrustAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = thrustAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = thrustAttackDown;
            }
            else {
                currentAttack = thrustAttackLeft;
            }
            this.frameStop = 7;
            spearAttack.play();
        }
        // Battle Cry
        else if( moveSelected == 1 ) {
            // Battle Cry
            if( direction == 0 ) {
                currentAttack = spellAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = spellAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = spellAttackDown;
            }
            else {
                currentAttack = spellAttackLeft;
            }
            this.frameStop = 6;
        }
        // Stun
        else if( moveSelected == 2 ) {
            if( direction == 0 ) {
                currentAttack = swipeAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = swipeAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = swipeAttackDown;
            }
            else {
                currentAttack = swipeAttackLeft;
            }
            this.frameStop = 5;
        }
        // Berserk
        else if( moveSelected == 3 ) {
            if( direction == 0 ) {
                currentAttack = spellAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = spellAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = spellAttackDown;
            }
            else {
                currentAttack = spellAttackLeft;
            }
            this.frameStop = 6;
            berserk.play();
        }
    }
    private void wizardAttacks( ) {
        int moveSelected = this.player.getMoveSelected();
        // 0-Up, 1-Right, 2-Down, 3-Left
        int direction = this.player.getDirection();

        // Basic Spell
        if( moveSelected == 0 ) {
            if( direction == 0 ) {
                currentAttack = swipeAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = swipeAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = swipeAttackDown;
            }
            else {
                currentAttack = swipeAttackLeft;
            }
            this.frameStop = 5;
            fireball.play();
        }
        // Transmute
        else if( moveSelected == 1 ) {
            if( direction == 0 ) {
                currentAttack = spellAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = spellAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = spellAttackDown;
            }
            else {
                currentAttack = spellAttackLeft;
            }
            this.frameStop = 6;
            transmute.play();
        }
        // Summon
        else if( moveSelected == 2 ) {
            if( direction == 0 ) {
                currentAttack = swipeAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = swipeAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = swipeAttackDown;
            }
            else {
                currentAttack = swipeAttackLeft;
            }
            this.frameStop = 5;
            summonSpell.play();
        }
        // Immolate
        else if( moveSelected == 3 ) {
            if( direction == 0 ) {
                currentAttack = spellAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = spellAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = spellAttackDown;
            }
            else {
                currentAttack = spellAttackLeft;
            }
            this.frameStop = 6;
            immolate.play();
        }
    }
    private void rogueAttacks( ) {
        int moveSelected = this.player.getMoveSelected();
        // 0-Up, 1-Right, 2-Down, 3-Left
        int direction = this.player.getDirection();

        // swipe
        if( moveSelected == 0 ) {
            if( direction == 0 ) {
                currentAttack = swipeAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = swipeAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = swipeAttackDown;
            }
            else {
                currentAttack = swipeAttackLeft;
            }
            this.frameStop = 5;
            daggerSwing.play();
        }
        // Ninja Star
        else if( moveSelected == 1 ) {
            if( direction == 0 ) {
                currentAttack = thrustAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = thrustAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = thrustAttackDown;
            }
            else {
                currentAttack = thrustAttackLeft;
            }
            this.frameStop = 7;
            shurikenThrow.play();
        }
            // Invisible
        else if( moveSelected == 2 ) {
            if( direction == 0 ) {
                currentAttack = spellAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = spellAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = spellAttackDown;
            }
            else {
                currentAttack = spellAttackLeft;
            }
            this.frameStop = 6;
            bushStealth.play();
        }
            // Poison
        else if( moveSelected == 3 ) {
            if( direction == 0 ) {
                currentAttack = swipeAttackUp;
            }
            else if( direction == 1 ) {
                currentAttack = swipeAttackRight;
            }
            else if( direction == 2 ) {
                currentAttack = swipeAttackDown;
            }
            else {
                currentAttack = swipeAttackLeft;
            }
            this.frameStop = 5;
        }
    }

    private void checkSpaceAhead( Vector<NPC> enemies ) {

        float x = this.player.getPlayerX();
        float y = this.player.getPlayerY();

        boolean stun = false;
        if( this.player.getCharacterClassChosen() == 1 && this.player.getMoveSelected() == 2 ) {
            stun = true;
        }

        for( int i = 0; i < enemies.size(); i++ ) {
            if( enemies.elementAt(i).getIsAlive() ) {
                if( this.player.getDirection() == 0 ) {
                    if ((enemies.elementAt(i).getNPCX() >= x - 24 && enemies.elementAt(i).getNPCX() <= x + 24) && (enemies.elementAt(i).getNPCY() >= y - 24 && enemies.elementAt(i).getNPCY() <= y )) {
                        enemies.elementAt(i).takeDamage(this.player.getDirection());
                        if( stun ) {
                            enemies.elementAt(i).setStunned(5000);
                        }
                        break;
                    }
                }
                else if( this.player.getDirection() == 1 ) {
                    if ((enemies.elementAt(i).getNPCX() >= x  && enemies.elementAt(i).getNPCX() <= x + 24) && (enemies.elementAt(i).getNPCY() >= y - 24 && enemies.elementAt(i).getNPCY() <= y + 24 )) {
                        enemies.elementAt(i).takeDamage(this.player.getDirection() );
                        if( stun ) {
                            enemies.elementAt(i).setStunned(5000);
                        }
                        break;
                    }
                }
                else if( this.player.getDirection() == 2 ) {
                    if ((enemies.elementAt(i).getNPCX() >= x - 24 && enemies.elementAt(i).getNPCX() <= x + 24) && (enemies.elementAt(i).getNPCY() >= y && enemies.elementAt(i).getNPCY() <= y + 24 )) {
                        enemies.elementAt(i).takeDamage(this.player.getDirection());
                        if( stun ) {
                            enemies.elementAt(i).setStunned( 5000 );
                        }
                        break;
                    }
                }
                else if( this.player.getDirection() == 3 ) {
                    if ((enemies.elementAt(i).getNPCX() >= x - 24  && enemies.elementAt(i).getNPCX() <= x) && (enemies.elementAt(i).getNPCY() >= y - 24 && enemies.elementAt(i).getNPCY() <= y + 24 )) {
                        enemies.elementAt(i).takeDamage(this.player.getDirection());
                        if( stun ) {
                            enemies.elementAt(i).setStunned( 5000 );
                        }
                        break;
                    }
                }
            }
        }
    }

    private void checkSpaceAround( Vector<NPC> enemies ) {

        float x = this.player.getPlayerX();
        float y = this.player.getPlayerY();

        for( int i = 0; i < enemies.size(); i++ ) {
            if( enemies.elementAt(i).getIsAlive() ) {
                if ((enemies.elementAt(i).getNPCX() >= x - 44 && enemies.elementAt(i).getNPCX() <= x + 44) && (enemies.elementAt(i).getNPCY() >= y - 44 && enemies.elementAt(i).getNPCY() <= y + 44)) {
                    enemies.elementAt(i).takeDamage(this.player.getDirection());
                    enemies.elementAt(i).setStunned();
                }
            }
        }
    }

    public boolean getIsHidding() { return this.isHidding; }

    private boolean isHunterDone( Input input, int delta ) {
        int moveSelected = this.player.getMoveSelected();

        if( moveSelected == 0 ) {
            if( this.currentAttack.isStopped() ) {
                return true;
            }
        }
        else if( moveSelected == 1 ) {
            if( this.currentAttack.isStopped() ) {
                return true;
            }
        }
        else if( moveSelected == 2 ) {
            if (!input.isKeyDown(Input.KEY_SPACE) ) {
                this.isHidding = false;
                return true;
            }
            else if( this.player.getStamina() >= 2 ) {
                this.player.decreaseStamina(delta * .01f);
                this.isHidding = true;
                return false;
            }
            else {
                this.isHidding = false;
                return true;
            }
        }
        else if( moveSelected == 3 ) {
            if( this.currentAttack.isStopped() ) {
                return true;
            }
        }
        return false;

    }
    private boolean isWarriorDone( Input input, int delta, Vector<NPC> enemies ) {
        int moveSelected = this.player.getMoveSelected();

        if( moveSelected == 0 ) {
            if( this.currentAttack.isStopped() ) {
                checkSpaceAhead( enemies );
                return true;
            }
        }
        else if( moveSelected == 1 ) {
            if( this.currentAttack.isStopped() ) {
                return true;
            }
        }
        else if( moveSelected == 2 ) {
            if( this.currentAttack.isStopped() ) {
                checkSpaceAhead( enemies );
                return true;
            }
        }
        else if( moveSelected == 3 ) {
            if( this.isBeserk == true ) {
                if( input.isKeyDown( Input.KEY_SPACE ) || this.player.getStamina() <= 2 ) {
                    this.isBeserk = false;
                    return true;
                }
            }
            else if( this.currentAttack.isStopped() ) {
                this.isBeserk = true;
                input.clearKeyPressedRecord();
                return true;
            }
        }
        return false;

    }

    public boolean getSummonedAlive() { return this.summonedAlive; }
    public void setSummonedAlive( boolean alive ) { this.summonedAlive = alive; }

    public void decreaseSummonedLife( int delta, Map map ) {
        this.summonedTime -= delta * .1f;
        System.out.println( this.summonedTime );
        if( this.summonedTime <= 0 ) {
            this.summonedAlive = false;
            map.removedSummoned();
        }
    }

    private boolean isWizardDone( Input input, int delta, Vector<NPC> enemies, Map map ) {
        int moveSelected = this.player.getMoveSelected();

        if( moveSelected == 0 ) {
            if( this.currentAttack.isStopped() ) {
                return true;
            }
        }
        else if( moveSelected == 1 ) {
            if( input.isKeyDown( Input.KEY_SPACE ) && this.player.getStamina() >= 2 && this.player.getHealth() < this.player.getMaxHealth() ) {
                this.currentAttack.stopAt( 4 );
                this.player.decreaseStamina(delta * .03f);
                this.player.increaseHealth( delta * .03f );
                return false;
            }
            else if( this.currentAttack.isStopped() ) {
                this.renderFire = false;
                return true;
            }
            else {

                return false;
            }
        }
        else if( moveSelected == 2 ) {
            if( this.currentAttack.isStopped() && this.summonedAlive == false ) {
                System.out.println( "Summon!" );
                map.addGood( (int)this.player.getPlayerX(), (int)this.player.getPlayerY() );
                this.summonedAlive = true;
                this.summonedTime =  8000;
                return true;
            }
            else if( this.currentAttack.isStopped() ) {
                return true;
            }
            else {
                return false;
            }
        }
        else if( moveSelected == 3 ) {

            if( input.isKeyDown( Input.KEY_SPACE ) && this.player.getStamina() >= 2 ) {
                this.currentAttack.stopAt( 4 );
                this.player.decreaseStamina(delta * .03f);
                this.renderFire = true;
                this.fire.start();
                checkSpaceAround(enemies);
                return false;
            }
            else if( this.currentAttack.isStopped() ) {
                this.renderFire = false;
                return true;
            }
            else {
                return false;
            }

         }
        return false;

    }
    private boolean isRougeDone( Input input, int delta, Vector<NPC> enemies ) {
        int moveSelected = this.player.getMoveSelected();

        if( moveSelected == 0 ) {
            if( this.currentAttack.isStopped() ) {
                checkSpaceAhead( enemies );
                return true;
            }
        }
        else if( moveSelected == 1 ) {
            if( this.currentAttack.isStopped() ) {
                return true;
            }
        }
        else if( moveSelected == 2 ) {
            if( this.isSneaking == true ) {
                if( input.isKeyDown( Input.KEY_SPACE ) || this.player.getStamina() <= 2 ) {
                    this.isSneaking = false;
                    return true;
                }
                else {
                    this.player.decreaseStamina(delta * .01f);
                    return false;
                }
            }
            else if( this.currentAttack.isStopped() ) {
                this.currentAttack = this.shadow;
                this.isSneaking = true;
                input.clearKeyPressedRecord();
                return false;
            }

        }
        else if( moveSelected == 3 ) {
            if( this.currentAttack.isStopped() ) {
                checkSpaceAhead( enemies );
                return true;
            }
        }
        return false;

    }

    public void drawFire( int x, int y) {
        this.fire.draw( x+32, y+32 );
        this.fire.draw( x, y+32 );
        this.fire.draw( x-32, y+32 );
        this.fire.draw( x+32, y );
        this.fire.draw( x-32, y );
        this.fire.draw( x+32, y-32 );
        this.fire.draw( x, y-32 );
        this.fire.draw( x-32, y-32 );
    }

    public void startAnimationAttacking() {
        this.currentAttack.restart();
        this.currentAttack.start();
    }
    public void stopAnimationAttacking() {
        this.currentAttack.stopAt( this.frameStop );
    }
    public boolean isAttackingStopped() {
        return this.currentAttack.isStopped();
    }
    public void drawPlayerAttacking( float x, float y ) { this.currentAttack.draw( x, y );  }

    private void loadSounds() {
        try{
            fireball =      new Sound("NewEra-Beta/res/sounds/fireball.ogg");
            immolate =      new Sound("NewEra-Beta/res/sounds/immolate.ogg");
            berserk =       new Sound("NewEra-Beta/res/sounds/rage.ogg");
            bowShot =       new Sound("NewEra-Beta/res/sounds/bow.ogg");
            daggerSwing =   new Sound("NewEra-Beta/res/sounds/dagger.ogg");
            shurikenThrow = new Sound("NewEra-Beta/res/sounds/shuriken.ogg");
            spearAttack =   new Sound("NewEra-Beta/res/sounds/dagger.ogg");
            transmute =     new Sound("NewEra-Beta/res/sounds/magic1.ogg");
            summonSpell =   new Sound("NewEra-Beta/res/sounds/spell.ogg");
            cloak =         new Sound("NewEra-Beta/res/sounds/cloak.ogg");
            bushStealth =   new Sound("NewEra-Beta/res/sounds/hide.ogg");
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }
}
