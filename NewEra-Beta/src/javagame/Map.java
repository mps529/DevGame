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

    public Map( String mapName ) throws SlickException {
        map = new TiledMap( "NewEra-Beta/res/map/"+mapName );
    }

    public void setMapCoordX( int x ) { mapCoordX = x; }
    public int getMapCoordX() { return mapCoordX; }
    public void incrementMapCoordX( ) { mapCoordX++; }
    public void decrementMapCoordX() { mapCoordX--; }

    public void setMapCoordY( int y ) { mapCoordY = y; }
    public int getMapCoordY( ) { return mapCoordY; }
    public void incrementMapCoordY( ) { mapCoordY++; }
    public void decrementMapCoordY() { mapCoordY--; }

    public void


}
