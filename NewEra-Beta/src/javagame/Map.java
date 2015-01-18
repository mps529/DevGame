package javagame;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map {

        // This is the map
    private TiledMap map;

    private String mapName;

        // Size of map in pixels
    private int totalMapSizeX, getTotalMapSizeY;

    // Coords of the map  and the skew
    // These are for rendering the area around the player
    private float mapCoordX, mapCoordY;

        // This is the collision layer
    private int collisionsLayer;
        // This is the layer that will store passages to new maps
    private int doorLayer;

        // Map size in tiles
    private int mapHeight, mapWidth;

        // how many pixels to move
    private static int speed = 2;

        // 0-walkable area, 1-collisions, 2-doors
    private int[][] mapObjects;

    public Map(String mapName, int x, int y) throws SlickException {
            // Setting mapName
        this.mapName = mapName;
            // Defining Map
        this.map = new TiledMap( "NewEra-Beta/res/map/"+mapName );

            // Settign map size in pixels
        this.totalMapSizeX = map.getWidth() *32;
        this.totalMapSizeX = map.getHeight() *32;
            // Setting map size by tiles
        setMapWidth( map.getWidth() );
        setMapHeight( map.getHeight() );

            // Getting the different layers id's
        this.collisionsLayer = map.getLayerIndex( "collision" );
        this.doorLayer = map.getLayerIndex( "doorway" );

            // Setting 2D array for collisions
        this.mapObjects = new int[ getMapWidth() ][ getMapHeight() ];

            // This will be where the map will start rendering
        setMapCoordX( x * 32 );
        setMapCoordY( y * 32 );

            // Fills the 2D array with collisions
        fillMapObjects();
    }

    public void setMapName( String name ) {
        this.mapName = name;
    }

    public String getMapName() {
        return this.mapName;
    }

    public void setMapCoordX( float x ) { mapCoordX = x; }
    public float getMapCoordX() { return mapCoordX; }

    public void incrementMapCoordX( ) {
        mapCoordX += speed;
    }
    public void decrementMapCoordX( ) { mapCoordX -= speed; }

    public void setMapCoordY( float y ) { mapCoordY = y; }
    public float getMapCoordY( ) { return mapCoordY; }

    public void incrementMapCoordY( ) { mapCoordY += speed; }
    public void decrementMapCoordY( ) { mapCoordY -= speed; }

    public void setMapHeight( int height ) { mapHeight = height; }
    public int getMapHeight() { return mapHeight; }

    public void setMapWidth( int width ) { mapWidth = width; }
    public int getMapWidth() { return mapWidth; }


    public void drawMap( ) {
        //map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );
        map.render( (int)mapCoordX, (int)mapCoordY );
    }

    private void fillMapObjects() {
        for( int x = 0; x < getMapHeight(); x++ ) {
            for( int y = 0; y < getMapWidth(); y++ ) {
                if( map.getTileId( x, y, collisionsLayer) != 0 ) {
                    mapObjects[ x ][ y ] = 1;
                }
                else if( map.getTileId( x, y, doorLayer) != 0 ) {
                    mapObjects[ x ][ y ] = 2;
                }
                else {
                    mapObjects[ x ][ y ] = 0;
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
        return mapObjects[ (int)tileX ][ (int)tileY ];

    }


    public void isRunning() {
        this.speed = 3;
    }

    public void isNotRunning() {
        this.speed = 2;
    }
}
