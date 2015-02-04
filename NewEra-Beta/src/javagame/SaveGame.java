package javagame;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;


public class SaveGame {

    private Player player;
    private Inventory inv;
    private static int itemscount; //only variable needed from Items class
    private int currentMap;
    //private Map map;


    private static SaveGame saveGameClass = null;



    public static SaveGame getInstance() {
        if( saveGameClass == null ) {
            saveGameClass = new SaveGame();
        }
        return saveGameClass;
    }

    public void save( Map m, int cMap ) {
        XStream xstream = new XStream();
        Items items = new Items();
        xstream.alias("save", SaveGame.class);

        FileOutputStream fop = null;
        File file;

        items = new Items();
        this.itemscount = items.getItemsCount();
        this.player = this.player.getInstance();
        inv = player.getInventory();
        //map = m;
        //this.currentMap = cMap;




        String xml = xstream.toXML(this);
        System.out.println(xml);

        try {
            file = new File("NewEra-Beta/res/saves/save.txt");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = xml.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }











}
