package javagame;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map {

        // This is the map
    private TiledMap map;
        // Name of this map
    private String mapName;

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

            // Setting 2D array for collisions
        this.mapObjects = new int[ getMapWidth() ][ getMapHeight() ];

            // This will be where the map will start rendering
        setMapCoordX( x );
        setMapCoordY( y );
        this.mapSkewX = Math.abs( x );
        this.mapSkewY = Math.abs( y );
        this.x=0;
        this.y=0;

            // Fills the 2D array with collisions
        fillMapObjects();
    }

    public void setMapName( String name ) {
        this.mapName = name;
    }
    public String getMapName() {
        return this.mapName;
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

    public void setMapHeight( int height ) { this.mapHeight = height; }
    public int getMapHeight() { return this.mapHeight; }

    public void setMapWidth( int width ) { this.mapWidth = width; }
    public int getMapWidth() { return this.mapWidth; }

    public void drawMap( ) {
            // This is rendering portions but is glitchy, maybe will get to
        //map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );
        //map.render( (int)mapCoordX, (int)mapCoordY, -84, -86, 20 , 20);
        map.render( (int)this.x-32, (int)this.y-32, (int)this.mapSkewX-1, (int)this.mapSkewY-1 , 21, 21);
        //map.render( (int)mapCoordX, (int)mapCoordY );
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
}
