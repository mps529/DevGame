package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;
import java.util.Random;

/**
 * Created by mattslavin on 1/26/15.
 */
public class MainMenu extends BasicGameState {

    private int gameState;
    private Image title;
    private Image newGame, newGamePressed;
    private Image loadGame, loadGamePressed;
    private TiledMap map;
    private float mapX, mapY;
    private boolean loadPressed, newPressed;
    private Random randInt;


    public MainMenu( int state ) {
        this.gameState = state;
    }

    public int getID( ) {
        return gameState;
    }

    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {

        randInt = new Random();

        //init title image
        this.title = new Image("NewEra-Beta/res/title/NEW-ERA.png");

        //init new game button
        this.newGame = new Image("NewEra-Beta/res/buttons/NewButton.png");
        this.newGamePressed = new Image("NewEra-Beta/res/buttons/NewButtonPressed.png");
        this.newPressed = false;

        //init load game button
        this.loadGame = new Image("NewEra-Beta/res/buttons/LoadButton.png");
        this.loadGamePressed = new Image("NewEra-Beta/res/buttons/LoadButtonPressed.png");
        this.loadPressed = false;

        //init map
        this.map = new TiledMap("NewEra-Beta/res/map/LargeMapGrasslands.tmx");
        this.mapX = -2688;
        this.mapY = -2752;

    }
    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {


        this.map.render((int) mapX, (int) mapY);

        this.title.draw(66, 100);

        if(!newPressed) { this.newGame.draw(96, 300); }
        else { this.newGamePressed.draw(96, 300); }

        if(!loadPressed) { this.loadGame.draw(352, 300); }
        else { this.loadGamePressed.draw(352, 300); }

    }
    public void update( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = gc.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

       // if(randInt.nextInt(2)== 0) {
        if(mapX >=-3200) {
            mapX += delta * .2f;
            mapY += delta * .2f;
        }
        if(mapX <= 0) {
            mapX -= delta * .2f;
            mapY += delta * .2f;
        }


        //New Game button operations
        if((mouseX >= 96 && mouseX <= 290) && (mouseY >= 300 && mouseY <= 364)) {
            newPressed = true;
        }
        else{
            newPressed = false;
        }

        //Load Game button operations
        if((mouseX >= 352 && mouseX <= 546) && (mouseY >= 300 && mouseY <= 364)) {
            loadPressed = true;
        }
        else{
            loadPressed = false;
        }








    }
}
