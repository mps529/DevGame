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

        // This is the collision layer
    private int collisionsLayer;
        // This is the layer that will store passages to new maps
    private int doorLayer;
        // Above Layer
    private int aboveLayer;

        // Map size in tiles
    private int mapHeight, mapWidth;

        // How many pixels to move
    private static int speed = 2;

        // 0-walkable area, 1-collisions, 2-doors
    private int[][] mapObjects;

    public Map(String mapName, int x, int y) throws SlickException {
            // Setting mapName
        setMapName( mapName );
            // Defining Map
        this.map = new TiledMap( "NewEra-Beta/res/map/"+mapName );

            // Setting map size by tiles
        setMapWidth( map.getWidth() );
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
    public float getMapCoordX() { return mapCoordX; }

    public void incrementMapCoordX( ) {
        mapCoordX += speed;
    }
    public void decrementMapCoordX( ) { mapCoordX -= speed; }

        // Send in TileY
    public void setMapCoordY( float y ) { mapCoordY = y*32; }
    public float getMapCoordY( ) { return mapCoordY; }
    public void incrementMapCoordY( ) { mapCoordY += speed; }
    public void decrementMapCoordY( ) { mapCoordY -= speed; }

    public void setMapHeight( int height ) { mapHeight = height; }
    public int getMapHeight() { return mapHeight; }

    public void setMapWidth( int width ) { mapWidth = width; }
    public int getMapWidth() { return mapWidth; }

    public void drawMap( ) {
            // This is rendering portions but is glitchy, maybe will get to
        //map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );
        map.render( (int)mapCoordX, (int)mapCoordY );
    }
    public void drawMapAbove() {
        this.map.render( (int)mapCoordX, (int)mapCoordY, this.aboveLayer );
    }

    private void fillMapObjects() {
            // Setting the bounds in a 2D array
        for( int x = 0; x < getMapHeight(); x++ ) {
            for( int y = 0; y < getMapWidth(); y++ ) {
                if( this.map.getTileId( x, y, this.collisionsLayer) != 0 ) {
                    this.mapObjects[ x ][ y ] = 1;
                }
                else if( this.map.getTileId( x, y, this.doorLayer) != 0 ) {
                    this.mapObjects[ x ][ y ] = 2;
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
        float decimalY = tileY - (int)Math.floor( tileY );;

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

    public void isRunning() {
        this.speed = 3;
    }
    public void isNotRunning() {
        this.speed = 2;
    }
}
