package javagame;

import com.thoughtworks.xstream.XStream;
import org.lwjgl.Sys;
import org.newdawn.slick.SlickException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class SaveGame {

    private String saveName;
    private int classID;
    private String sheetName;
    private int playerLvl;
    private Player playerObj;
    private Inventory inv;
    private static int itemscount; //only variable needed from Items class
    private int currentMap;
    private int slot;
    //private Map map;


    private static SaveGame saveGameClass = null;

    public SaveGame() {}

    public void saveGameCopy(SaveGame other) {
        this.saveName = other.saveName;
        this.playerObj = new Player();
        this.playerObj.playerCopy( other.playerObj );
        this.classID = other.classID;
        this.sheetName = other.sheetName;
        this.inv = new Inventory(other.inv);
        this.itemscount = other.itemscount;
        this.playerLvl = other.playerLvl;
        this.currentMap = other.currentMap;
        this.slot = other.slot;

    }



    public static SaveGame getInstance() {
        if( saveGameClass == null ) {
            saveGameClass = new SaveGame();
        }
        return saveGameClass;
    }



    public boolean save( int cMap, int saveSlot ) {
        XStream xstream = new XStream();
        Items items = new Items();

        Player player = Player.getInstance();
        FileOutputStream fop = null;
        File save;

        this.slot = saveSlot;
        items = new Items();
        this.itemscount = items.getItemsCount();
        this.setPlayer();
        this.playerLvl = player.getLevel();
        this.sheetName = player.getSpriteSheetName();
        this.classID = player.getCharacterClassChosen();
        inv = new Inventory(player.getInventory());
        //map = m;
        this.currentMap = cMap;

        this.saveName = player.getPlayerName();
        //xstream.alias("save"+slot, SaveGame.class);

        String xml = xstream.toXML(saveGameClass);
        //System.out.println(xml);

        try {

            save = new File("NewEra-Beta/res/saves/save_"+slot+".xml");

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

    public boolean load(int slot) {

        XStream xstream = new XStream();
        Player player;
        boolean foundSave = false;


        int slotNo, slotIndex;
        File folder = new File("NewEra-Beta/res/saves/");
        File[] listOfFiles = folder.listFiles();

        folder = new File("NewEra-Beta/res/saves/");
        listOfFiles = folder.listFiles();


        /*parses slot numbers out of save files. Will get the index of the save slot needed to load
          and store in slotIndex
        */
        for(int i=0;i<listOfFiles.length;i++) {
            //System.out.println(listOfFiles[i].getName());
            if(listOfFiles[i].getName().endsWith(".xml")) {
                String[] parsedName = listOfFiles[i].getName().split("_");
                slotNo = Math.abs(Character.getNumericValue(parsedName[1].charAt(0)));
                System.out.println(slotNo);
                if (slotNo == slot) {
                    //found save
                    slotIndex = slotNo;

                    //load values back into variables

                    saveGameClass = saveGameClass.getInstance();
                    saveGameClass.saveGameCopy((SaveGame) xstream.fromXML(listOfFiles[i]));

                    try {
                        player = Player.getInstance();

                        player.setUpLoadInstance(saveGameClass.sheetName, saveGameClass.saveName, saveGameClass.classID);
                        //player.getInventory().clearInventory();
                        player.playerCopy(saveGameClass.playerObj);
                        player.setInventory( saveGameClass.getInv() );
                        player.setSaveSlot(slotIndex);
                        foundSave = true;
                    } catch (SlickException e) {
                        e.printStackTrace();
                    }



                }
            }
        }
        if(foundSave) {
            return true;
        } else {
            return false;
        }
    }

    public String getSaveName() {return this.saveName;}
    public void setSaveName(String sn) {this.saveName = sn;}
    
    public int getSlot() {return this.slot;}
    public void setSlot(int slotNo) {this.slot = slotNo;}

    public Player getPlayer() {return this.playerObj;}
    public void setPlayer() {this.playerObj = this.playerObj.getInstance();}

    public Inventory getInv() {return this.inv;}
    public void setInv(Inventory i) {this.inv = new Inventory(i);}

    public int getItemscount() {return this.itemscount;}
    public void setItemscount(int ic) {this.itemscount = ic;}

    public int getCurrentMap() {return this.currentMap;}
    public void setCurrentMap(int cm) {this.currentMap = cm;}

    public int getClassID() {return this.classID;}

    public int getPlayerLvl() {return this.playerLvl;}











}
