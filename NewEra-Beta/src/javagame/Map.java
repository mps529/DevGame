package javagame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.util.Random;
import java.util.Vector;

public class Map implements TileBasedMap{

        // This is the map
    private TiledMap map;
        // Name of this map
    private String mapName;

    private Player player;

    // Coords of the map  and the skew
    // These are for rendering the area around the player
    private float mapCoordX, mapCoordY;

    private float x, y;
    private float mapSkewX, mapSkewY;

        // This is the collision layer
    private int collisionsLayer;
        // This is the layer that will store passages to new maps
    private int doorLayer;
        // Above Layer
    private int aboveLayer;

        // Layer for enemies to spawn
    private int enemySpawn;
        // Layer for good to spawn
    private int villagerSpawn;
        // Layer for guards to spawn
    private int guardSpawn;


        // Map size in tiles
    private int mapHeight, mapWidth;

        // Number of object groups
    private int objectGroupCount;
        // Number of objects
    private int objectCount;

    private int objectX;
    private int objectY;

        // How many pixels to move
    private static int speed = 2;

        // enemies
    private Vector<NPC> enemies;
        // Good
    private Vector<NPC> allies;

        // 0-walkable area, 1-collisions, 2-doors
    private int[][] mapObjects;

    public Map(String mapName, int x, int y) throws SlickException {
            // Setting mapName
        setMapName( mapName );
            // Defining Map
        this.map = new TiledMap( "NewEra-Beta/res/map/"+mapName );

            // We will only have one object group
        this.objectGroupCount = this.map.getObjectGroupCount();
        this.objectGroupCount--;
            // the number of groups in our objects layer
        this.objectCount = this.map.getObjectCount( objectGroupCount );
            // Setting map size by tiles
        setMapWidth(map.getWidth());
        setMapHeight( map.getHeight() );

            // Getting the different layers id's
        this.collisionsLayer = map.getLayerIndex( "collision" );
        this.doorLayer = map.getLayerIndex( "doorway" );
        this.aboveLayer = map.getLayerIndex( "above" );
            // Enemies
        this.enemySpawn = map.getLayerIndex("spawn");
            // Goods
        this.villagerSpawn = map.getLayerIndex( "villagerSpawn" );
        this.guardSpawn = map.getLayerIndex( "guardSpawn" );

            // Setting 2D array for collisions
        this.mapObjects = new int[ getMapWidth() ][ getMapHeight() ];

            // This will be where the map will start rendering
        setMapCoordX(x);
        setMapCoordY(y);
        this.mapSkewX = Math.abs( x );
        this.mapSkewY = Math.abs( y );
        this.x=0;
        this.y=0;

        this.enemies = new Vector<NPC>();
        this.allies = new Vector<NPC>();


            // Fills the 2D array with collisions
        fillMapObjects();

     }

    public void setMapName( String name ) {
        this.mapName = name;
    }
    public String getMapName() {
        return this.mapName;
    }

    public void printSizes() {
        System.out.println("Enemies: " + this.enemies.size() + ", Allies: " + this.allies.size());
    }

    public void addEnemies() {

        if( this.enemySpawn != -1 ) {
            Random rand = new Random();

            for (int x = 0; x < getMapHeight(); x++) {
                for (int y = 0; y < getMapWidth(); y++) {

                    if (this.map.getTileId(x, y, this.enemySpawn) != 0) {
                        NPC temp = new NPC(rand.nextInt(2), false);
                        this.enemies.add(temp);
                        this.enemies.lastElement().setImage(false);
                        this.enemies.lastElement().setSpawnX(x * 32);
                        this.enemies.lastElement().setSpawnY(y * 32);
                        this.enemies.lastElement().setMapPath(this);
                    }

                }
            }
        }
    }
    public void addGood() {

        if (this.villagerSpawn != -1 && this.guardSpawn != -1) {

            Random rand = new Random();

            for (int x = 0; x < getMapHeight(); x++) {
                for (int y = 0; y < getMapWidth(); y++) {

                    if (this.map.getTileId(x, y, this.villagerSpawn) != 0) {
                        NPC temp = new NPC(rand.nextInt(2), true);
                        this.allies.add(temp);
                        this.allies.lastElement().setImage(true);
                        this.allies.lastElement().setSpawnX(x * 32);
                        this.allies.lastElement().setSpawnY(y * 32);
                        this.allies.lastElement().setMapPath(this);
                    } else if (this.map.getTileId(x, y, this.guardSpawn) != 0) {
                        NPC temp = new NPC(rand.nextInt(2), true);
                        this.allies.add(temp);
                        this.allies.lastElement().setImage(false);
                        this.allies.lastElement().setSpawnX(x * 32);
                        this.allies.lastElement().setSpawnY(y * 32);
                        this.allies.lastElement().setMapPath(this);
                    }
                }
            }
        }
    }

    public void addGood( int x, int y ) {
        if( this.mapObjects[(x/32)][y/32] != 1 ) {
            NPC temp = new NPC( 1, true );
            this.allies.add( temp );
            this.allies.lastElement().setSummon();
            this.allies.lastElement().setSpawnX(x );
            this.allies.lastElement().setSpawnY(y);
            this.allies.lastElement().setMapPath(this);
            this.allies.lastElement().setSummoned( true );
        }
    }

    public boolean checkIfSummonedAlive() {
        for( int x = 0; x < this.allies.size(); x++ ) {
            if( this.allies.elementAt(x).getSummoned() ) {
                return true;
            }
        }
        return false;
    }

    public void removedSummoned() {
        for( int x = 0; x < this.allies.size(); x++ ) {
            if( this.allies.elementAt(x).getSummoned() ) {
                this.allies.removeElementAt( x );
                return;
            }
        }
    }

     public void clearNPCList() {
         this.enemies.clear();
         this.allies.clear();
     }

        // Send in TileX
    public void setMapCoordX( float x ) { mapCoordX = x*32; }
    public void setMapCoordXInPixels( float x ) { mapCoordX = x; }
    public float getMapCoordX() { return mapCoordX; }

    public void incrementMapCoordX( ) {
        this.mapCoordX += this.speed;

        int skewX =  (int)this.mapSkewX * 32;

        this.x = skewX + (int)this.mapCoordX;

        if( this.x >= 32 ) {
            this.mapSkewX--;
            this.x = 0;
        }

    }
    public void decrementMapCoordX( ) {
        this.mapCoordX -= this.speed;

        int skewX = (int)this.mapSkewX * 32;

        this.x = skewX + (int)this.mapCoordX;

        if( this.x <= 0 ) {
            this.mapSkewX++;
            this.x = 32;
        }
    }

        // Send in TileY
    public void setMapCoordY( float y ) { mapCoordY = y*32; }
    public void setMapCoordYInPixels( float y ) { mapCoordY = y; }
    public float getMapCoordY( ) { return this.mapCoordY; }

    public void incrementMapCoordY( ) {
        this.mapCoordY += this.speed;
        int skewY =  (int)this.mapSkewY * 32;

        this.y = skewY + (int)this.mapCoordY;

        if( this.y >= 32 ) {
            this.mapSkewY--;
            this.y = 0;
        }
    }
    public void decrementMapCoordY( ) {
        this.mapCoordY -= this.speed;

        int skewY = (int)this.mapSkewY * 32;

        this.y = skewY + (int)this.mapCoordY;

        if( this.y <= 0 ) {
            this.mapSkewY++;
            this.y = 32;
        }
    }

    public void resetSkewAndCoords() {
        this.mapSkewX = Math.abs( this.mapCoordX ) / 32;
        this.mapSkewY = Math.abs( this.mapCoordY ) / 32;
        this.x = 0;
        this.y = 0;
    }

    public void setMapHeight( int height ) { this.mapHeight = height; }
    public int getMapHeight() { return this.mapHeight; }

    public void setMapWidth( int width ) { this.mapWidth = width; }
    public int getMapWidth() { return this.mapWidth; }

    public void drawMap( Graphics g ) {
            // This is rendering portions but is glitchy, maybe will get to
        //map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );
        //map.render( (int)mapCoordX, (int)mapCoordY, -84, -86, 20 , 20);
        map.render( (int)this.x-32, (int)this.y-32, (int)this.mapSkewX-1, (int)this.mapSkewY-1 , 21, 21);
        //map.render( (int)mapCoordX, (int)mapCoordY );

        for( int x =0; x < this.enemies.size(); x++ ) {
            this.enemies.elementAt(x).drawNPC(g);
        }

        for( int x =0; x < this.allies.size(); x++ ) {
            this.allies.elementAt(x).drawNPC(g);
        }
    }
    public void drawMapAbove() {
        this.map.render( (int)mapCoordX, (int)mapCoordY, this.aboveLayer );
        //this.map.render( (int)this.mapSkewX*32, (int)this.mapSkewY*32, this.aboveLayer );
    }

    private void fillMapObjects() {
            // Setting the bounds in a 2D array
        for( int x = 0; x < getMapHeight(); x++ ) {
            for( int y = 0; y < getMapWidth(); y++ ) {
                if( this.map.getTileId( x, y, this.collisionsLayer) != 0 ) {
                    this.mapObjects[ x ][ y ] = 1;
                }
                else {
                    this.mapObjects[ x ][ y ] = 0;
                }
            }
        }
    }

    public int[][] getCollisions() {
        return mapObjects;
    }

    // 0-open, 1-collision, 2-door
    public int isSpaceTaken( float x, float y ) {

        float tileX = x/32;
        float tileY = y/32;

        float decimalX = tileX - (int)Math.floor( tileX );
        float decimalY = tileY - (int)Math.floor( tileY );

        if( decimalX >= 0.5 ) {
            tileX++;
        }
        if( decimalY >= 0.5 ) {
            tileY++;
        }
        return this.mapObjects[ (int)tileX ][ (int)tileY ];

    }

    public void updateNPCAttacks( int delta, int x, int y  ) {

        for( int i = 0; i < this.enemies.size(); i++ ) {
            if( ( this.enemies.elementAt( i ).getNpcClass() == 0 || this.enemies.elementAt( i ).getNpcClass() == 2 )  ) {
                this.enemies.elementAt( i ).updateProjectile(delta, this.enemies.elementAt(i).getHasAttacked(), this, x, y, enemies.elementAt(i).isGood());
                if( this.enemies.elementAt( i ).getHasAttacked() ) {
                    this.enemies.elementAt( i ).setHasAttacked( false );
                }
            }
        }

    }


    public boolean isSpaceEnemy( float x, float y, int direction ) {

        for( NPC enemy : enemies ) {
            if( enemy.getIsAlive() ) {
                if ((enemy.getNPCX() >= x - 20 && enemy.getNPCX() <= x + 20) && (enemy.getNPCY() >= y - 20 && enemy.getNPCY() <= y + 20)) {

                    if( direction == -1 ) {
                        enemy.takeDamage( enemy.getDirection() );
                    }
                    else {
                        enemy.takeDamage( direction );
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSpaceAlly( float x, float y, int direction ) {

        for( NPC ally : allies ) {
            if( ally.getIsAlive() ) {
                if ((ally.getNPCX() >= x - 12 && ally.getNPCX() <= x + 12) && (ally.getNPCY() >= y - 12 && ally.getNPCY() <= y + 12)) {
                    ally.takeDamage( direction );
                    return true;
                }
            }
        }
        return false;
    }

    public Vector getEnemies() { return this.enemies; }

    public void setEnemies(Vector<NPC> enemies) {this.enemies = enemies;}

    public void enemyMove( int delta, int x, int y, Player player, boolean hidden, boolean invisible ) {

        boolean playerFound = false;

        boolean isHidden = false;

        if( hidden || invisible ) {
            isHidden = true;
        }


        for( int i = 0; i < enemies.size(); i++ ) {
            enemies.elementAt(i).stopAnimationWalking();
            int opponent = enemies.elementAt(i).getEnemy();

            int opponentX;
            int opponentY;

            if( opponent == -1 ) {
                opponentX = x;
                opponentY = y;
            }
            else {
                opponentX = (int) allies.elementAt(opponent).getNPCX();
                opponentY = (int) allies.elementAt(opponent).getNPCY();

            }
            if( enemies.elementAt(i).getStunned() > 0 ) {
                enemies.elementAt(i).decreaseStunned(delta);
            }
            if( !enemies.elementAt(i).getIsAlive() && enemies.elementAt(i).getDeathTime() > 0 ) {
                enemies.elementAt(i).decreaseTimeLeftOnEarth(delta);
            }
            else if(  enemies.elementAt(i).getStunned() <=0 && enemies.elementAt(i).getIsAlive() ) {
                if( !enemies.elementAt(i).getIsAttacking() ) {
                    if ( enemies.elementAt(i).isGoodInSight(this.mapObjects, allies, isHidden )) {
                        if( enemies.elementAt(i).getEnemy() == -1 ) {
                            playerFound = true;
                        }
                        if (!enemies.elementAt(i).closeEnoughToAttack(opponentX, opponentY)) {
                            enemies.elementAt(i).goToGood(opponentX, opponentY);
                        }
                    }
                    else if( enemies.elementAt(i).getInCombat() ) {
                        enemies.elementAt(i).facePlayer();
                        if( enemies.elementAt(i).getEnemy() == -1 ) {
                            playerFound = true;
                        }
                    }
                    else {
                        enemies.elementAt(i).lookAround(delta);
                    }
                }
                else {

                    if( opponent == -1  && !enemies.elementAt(i).getIsAttacking() ) {
                        enemies.elementAt(i).goToGood(x, y);
                        playerFound = true;
                    }
                    else if ( !enemies.elementAt(i).getIsAttacking() ){
                        enemies.elementAt(i).goToGood((int) enemies.elementAt(opponent).getNPCX(), (int) enemies.elementAt(opponent).getNPCY());
                    }
                    if( enemies.elementAt(i).closeEnoughToAttack(opponentX, opponentY) && opponent != -1 ) {
                        allies.elementAt(opponent).takeDamage( enemies.elementAt(i).getDirection() );
                    }
                }
            }

        }

        if( playerFound == false ) {
            player.setInCombat( false );
        }

    }

    public Vector<NPC> getAllies() {return allies;}
    public void setAllies(Vector<NPC> allies) {this.allies = allies;}


    public void alliesMove( int delta, int x, int y ) {

        for(int i = 0; i < allies.size(); i++ ) {
            allies.elementAt(i).stopAnimationWalking();
            int opponent = allies.elementAt(i).getEnemy();
            int opponentX;
            int opponentY;
            if( opponent == -1 ) {
                opponentX = x;
                opponentY = y;
            }
            else {
                opponentX = (int)enemies.elementAt(opponent).getNPCX();
                opponentY = (int)enemies.elementAt(opponent).getNPCY();
            }
            if( allies.elementAt(i).getStunned() > 0 ) {
                allies.elementAt(i).decreaseStunned(delta);
            }
            if( !allies.elementAt(i).getIsAlive() && allies.elementAt(i).getDeathTime() > 0 ) {
                allies.elementAt(i).decreaseTimeLeftOnEarth( delta );
            }
            else if(  allies.elementAt(i).getStunned() <=0 && allies.elementAt(i).getIsAlive() ) {
                if( !allies.elementAt(i).getIsAttacking() ) {
                    if (allies.elementAt(i).isGoodInSight(this.mapObjects, enemies, false )) {
                        if (!allies.elementAt(i).closeEnoughToAttack(opponentX, opponentY)) {
                            allies.elementAt(i).goToGood(opponentX, opponentY);
                        }
                    }
                    else if( allies.elementAt(i).getInCombat() ) {
                        allies.elementAt(i).facePlayer();
                    }
                    else {
                        allies.elementAt(i).lookAround(delta);
                    }
                }
                else {
                    if( allies.elementAt(i).closeEnoughToAttack(opponentX, opponentY) && opponent != -1 ) {
                        enemies.elementAt(opponent).takeDamage(allies.elementAt(i).getDirection());
                    }
                }
            }

        }

    }

    public void renderProjectile( GameContainer gc, Graphics g )  {

        try {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.elementAt(i).renderProjectile(gc, g);
            }
            for (int i = 0; i < allies.size(); i++) {
                allies.elementAt(i).renderProjectile(gc, g);
            }
        }
        catch( SlickException e ) {
            e.printStackTrace();
        }
    }

    public float getPlayerXInPixels() { return (Math.abs( getMapCoordX() ) + 320); }
    public float getPlayerYInPixels() { return (Math.abs( getMapCoordY() ) + 320); }

    public void setObjectX( int x ) {
        this.objectX = x;
    }
    public void setObjectX( String x ) {
        this.objectX = Integer.parseInt( x );
    }
    public int getObjectX() { return this.objectX; }

    public void setObjectY( int y ) {
        this.objectY = y;
    }
    public void setObjectY( String y ) {
        this.objectY = Integer.parseInt( y );
    }
    public int getObjectY() { return this.objectY; }

    public String isOnObjectLayer( float x, float y ) {
        String objName;
        int objectX = 0;
        int objectY = 0;
        int objectWidth = 0;
        int objectHeight = 0;

        for( int i = 0; i < this.objectCount; i++ ) {
            objectX = this.map.getObjectX( this.objectGroupCount, i );
            objectY = this.map.getObjectY(this.objectGroupCount, i);
            objectWidth = this.map.getObjectWidth(this.objectGroupCount, i );
            objectHeight = this.map.getObjectHeight(this.objectGroupCount, i);
            objName = this.map.getObjectProperty( this.objectGroupCount, i, "mapName", null );

            if( ((objectX-12) <= x) && ((objectX+objectWidth+12)) >= x  ) {
                if( (objectY >= y) && ((objectY - objectHeight)) <= y  ) {
                    setObjectX( this.map.getObjectProperty( this.objectGroupCount, i, "destX", "-1" ) );
                    setObjectY( this.map.getObjectProperty( this.objectGroupCount, i, "destY", "-1" ) );
                    return objName;
                }
            }
        }

        return null;
    }

    public void isRunning() {
        this.speed = 3;
    }
    public void isNotRunning() {
        this.speed = 2;
    }

    public void setX(float newX) {this.x = newX;}
    public float getX() {return this.x;}

    public boolean despawnNpc (int id) {
        for(int i=0;i <this.enemies.size(); i++) {
            if(this.enemies.elementAt(i).getId() == id) {
                this.enemies.elementAt(i).setAlive(false);
                return true;
            }
        }
        return false;
    }
    //used when looting to replace inventory with looted inventory
    public boolean setNewInventory(int id) {
        this.player = this.player.getInstance();

        for(int i=0; i<this.enemies.size();i++) {
            if(this.enemies.elementAt(i).getId() == player.getLootingId()) {
                this.enemies.elementAt(i).setInventory(player.getLootingInventory());
                return true;
            }
        }
        return false;
    }
    public boolean checkIfCanLoot(int id) {
        for(int i=0;i <this.enemies.size(); i++) {
            if (this.enemies.elementAt(i).getId() == id) {
                if(this.enemies.elementAt(i).getDeathTime() == 0) {
                    return false;
                }

            }
        }
        return true;
    }

    public void setY(float newY) {this.y = newY;}
    public float getY() {return this.y;}

    public float getMapSkewX() {return this.mapSkewX;}
    public void setMapSkewX(float msx) {this.mapSkewX = msx;}

    public float getMapSkewY() {return this.mapSkewY;}
    public void setMapSkewY(float msy) {this.mapSkewY = msy;}

    @Override
    public boolean blocked(PathFindingContext ctx, int x, int y) {
        if( this.mapObjects[ x ][ y ] == 1 ) {
            return true;
        }
        for( NPC enemy : enemies ) {
            if( enemy.getNPCXTile() == x && enemy.getNPCYTile() == y ) {
                return true;
            }
        }
        for( NPC ally : allies ) {
            if( ally.getNPCXTile() == x && ally.getNPCYTile() == y ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public float getCost(PathFindingContext ctx, int x, int y) {
        return 1.0f;
    }

    @Override
    public int getHeightInTiles() {
        return this.mapHeight;
    }

    @Override
    public int getWidthInTiles() {
        return this.mapWidth;
    }

    @Override
    public void pathFinderVisited(int x, int y) {}

}
