package javagame;


import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class SaveGame {

    private String saveName;
    private Player player;
    private Inventory inv;
    private static int itemscount; //only variable needed from Items class
    private int currentMap;
    private int slot;
    //private Map map;


    private static SaveGame saveGameClass = null;



    public static SaveGame getInstance() {
        if( saveGameClass == null ) {
            saveGameClass = new SaveGame();
        }
        return saveGameClass;
    }

    public boolean save( Map m, int cMap, int saveSlot ) {
        XStream xstream = new XStream();
        Items items = new Items();
        xstream.alias(this.saveName, SaveGame.class);

        FileOutputStream fop = null;
        File save;

        this.slot = saveSlot;
        items = new Items();
        this.itemscount = items.getItemsCount();
        this.player = this.player.getInstance();
        inv = player.getInventory();
        //map = m;
        //this.currentMap = cMap;

        this.saveName = this.player.getPlayerName();

        String xml = xstream.toXML(this);
        System.out.println(xml);

        try {
            if(this.saveName == "" || this.saveName == null) {
                save = new File("NewEra-Beta/res/saves/save"+slot);
            } else {
                save = new File("NewEra-Beta/res/saves/"+this.saveName);
            }
            fop = new FileOutputStream(save);

            // if file doesnt exists, then create it
            if (!save.exists()) {
                save.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = xml.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }











}
