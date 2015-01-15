package javagame;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map {
        // This is the map
    private TiledMap map;

        // Coords of the map  and the skew
        // These are for rendering the area around the player
    private int mapCoordX, mapCoordY;
    private int mapSkewX, mapSkewY;

    private int collisionsLayer;

    private int doorLayer;

    private int mapHeight, mapWidth;

        // 0-walkable area, 1-collisions, 2-doors
    private int[][] mapObjects;

    public Map( String mapName ) throws SlickException {
        map = new TiledMap( "NewEra-Beta/res/map/"+mapName );

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
    public int getMapCoordX() { return mapCoordX; }
    public void incrementMapCoordX( ) { mapCoordX++; }
    public void decrementMapCoordX() { mapCoordX--; }

    public void setMapCoordY( int y ) { mapCoordY = y; }
    public int getMapCoordY( ) { return mapCoordY; }
    public void incrementMapCoordY( ) { mapCoordY++; }
    public void decrementMapCoordY() { mapCoordY--; }

    public int getMapSkewX() { return mapSkewX; }
    public void setMapSkewX( int x ) { mapSkewX = x; }

    public int getMapSkewY() { return mapSkewY; }
    public void setMapSkewY( int y ) { mapSkewY = y; }

    public void setMapHeight( int height ) { mapHeight = height; }
    public int getMapHeight() { return mapHeight; }

    public void setMapWidth( int width ) { mapWidth = width; }
    public int getMapWidth() { return mapWidth; }

    public void updateMapSkewAndCoords( ) {
       if( mapCoordX < 0 ) {
            mapCoordX = 1;
            mapSkewX+=2;
        }
        if( mapCoordX > 1 ) {
            mapCoordX = 0;
            mapSkewX-=2;
        }
        if( mapCoordY < 0 ) {
            mapCoordY = 1;
            mapSkewY+=2;
        }
        if( mapCoordY > 1 ) {
            mapCoordY = 0;
            mapSkewY-=2;
        }

    }

    public void drawMap( ) {
        map.render( (mapCoordX-1)*32, (mapCoordY-1)*32, mapSkewX, mapSkewY, mapSkewX+25, mapSkewY+25 );
        //map.render( mapCoordX*32, mapCoordY*32 );
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

        for( int x = 0; x < mapWidth; x++ ) {
            for( int y = 0; y < mapHeight; y++ ) {
                if(map.getTileId( y, x, collisionsLayer  ) == 0 ) {
                    System.out.print( "-" );
                }
                else {
                    System.out.print( "1" );
                }
            }
            System.out.print("\n");
        }

    }

        // 0-open, 1-collision, 2-door
    public int isSpaceTaken( int x, int y ) { return mapObjects[ x ][ y ]; }

}
