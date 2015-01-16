package javagame;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map2 {

    // This is the map
    private TiledMap map;

    private int totalMapSizeX = 0, getTotalMapSizeY = 0;

    // Coords of the map  and the skew
    // These are for rendering the area around the player
    private float mapCoordX, mapCoordY;
    private float mapSkewX, mapSkewY;

    private int collisionsLayer;

    private int doorLayer;

    private int mapHeight, mapWidth;

    int speed = 2;

    // 0-walkable area, 1-collisions, 2-doors
    private int[][] mapObjects;

    public Map2( String mapName ) throws SlickException {
        map = new TiledMap( "NewEra-Beta/res/map/"+mapName );

        totalMapSizeX = map.getWidth() *32;
        totalMapSizeX = map.getHeight() *32;

        collisionsLayer = map.getLayerIndex( "collision" );
        doorLayer = map.getLayerIndex( "doorway" );

        setMapCoordX( 1 );
        setMapCoordY( 1 );

        setMapSkewX( 0 );
        setMapCoordY( 0 );

        setMapHeight( map.getHeight() );
        setMapWidth( map.getWidth() );

        mapObjects = new int[ map.getWidth() ][ map.getHeight() ];

        fillMapObjects();
    }

    public void setMapCoordX( int x ) { mapCoordX = x; }
    public float getMapCoordX() { return mapCoordX; }

    public void incrementMapCoordX( int delta ) {
        mapCoordX+= speed;
    }
    public void decrementMapCoordX( int delta ) { mapCoordX-= speed; }

    public void setMapCoordY( int y ) { mapCoordY = y; }
    public float getMapCoordY( ) { return mapCoordY; }

    public void incrementMapCoordY( int delta ) { mapCoordY+=speed; }
    public void decrementMapCoordY( int delta ) { mapCoordY-= speed; }

    public float getMapSkewX() { return mapSkewX; }
    public void setMapSkewX( int x ) { mapSkewX = x; }

    public float getMapSkewY() { return mapSkewY; }
    public void setMapSkewY( int y ) { mapSkewY = y; }

    public void setMapHeight( int height ) { mapHeight = height; }
    public float getMapHeight() { return mapHeight; }

    public void setMapWidth( int width ) { mapWidth = width; }
    public float getMapWidth() { return mapWidth; }

    public void updateMapSkewAndCoords( ) {
        if( mapCoordX < 32 ) {
            mapCoordX = 32;
            mapSkewX+=2;
        }
        if( mapCoordX > 32 ) {
            mapCoordX = 0;
            mapSkewX-=2;
        }
        if( mapCoordY < 32 ) {
            mapCoordY = 32;
            mapSkewY+=2;
        }
        if( mapCoordY > 32 ) {
            mapCoordY = 0;
            mapSkewY-=2;
        }

    }

    public void drawMap( ) {
        //map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );
        map.render( (int)mapCoordX, (int)mapCoordY );
    }

    private void fillMapObjects() {
        for( int x = 0; x < mapHeight; x++ ) {
            for( int y = 0; y < mapWidth; y++ ) {
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

}
