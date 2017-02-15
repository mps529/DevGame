package javagame;

import com.thoughtworks.xstream.XStream;
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
    private static int itemscount;
    private int currentMap;
    private int slot;


    private static SaveGame saveGameClass = null;

    public SaveGame() {}

    public void saveGameCopy(SaveGame other) {
        this.saveName = other.saveName;
        this.playerObj = playerObj.getInstance();
        this.playerObj.playerCopy( other.playerObj );
        //this.playerObj = playerObj.getInstance();
        this.classID = other.classID;
        this.sheetName = other.sheetName;
        this.inv = new Inventory(other.inv, false);
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


        playerObj = playerObj.getInstance();
        FileOutputStream fop = null;
        File save;

        this.slot = saveSlot;
        items = new Items();
        this.itemscount = items.getItemsCount(); //will have to change later
        this.playerLvl = playerObj.getLevel();
        this.sheetName = playerObj.getSpriteSheetName();
        this.classID = playerObj.getCharacterClassChosen();
        playerObj.getInventory().printInventory();
        this.inv = new Inventory(playerObj.getInventory(), true);



        //map = m;
        this.currentMap = cMap;

        this.saveName = playerObj.getPlayerName();

        String xml = xstream.toXML(this);

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
        File folder;
        File[] listOfFiles;

        folder = new File("NewEra-Beta/res/saves/");
        listOfFiles = folder.listFiles();


        /*parses slot numbers out of save files. Will get the index of the save slot needed to load
          and store in slotIndex
        */
        for(int i=0;i<listOfFiles.length;i++) {
            if(listOfFiles[i].getName().endsWith(".xml")) {
                String[] parsedName = listOfFiles[i].getName().split("_");
                slotNo = Math.abs(Character.getNumericValue(parsedName[1].charAt(0)));
                if (slotNo == slot) {
                    //found save
                    slotIndex = slotNo;

                    //load values back into variables

                    saveGameClass = saveGameClass.getInstance();
                    saveGameClass.saveGameCopy((SaveGame) xstream.fromXML(listOfFiles[i]));

                    try {
                        player = Player.getInstance();

                        player.setUpLoadInstance(saveGameClass.sheetName, saveGameClass.saveName, saveGameClass.classID);
                        player.playerCopy(saveGameClass.playerObj);
                        player.setInventoryLoad( saveGameClass.getInv() );
                        for(Items item: player.getInventory().getItemList()) {
                            player.getInventory().equipItemsOnLoad(item.getID());
                        }
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

    public boolean loadSlots(int slot) {

        XStream xstream = new XStream();
        boolean foundSave = false;


        int slotNo, slotIndex;
        File folder;
        File[] listOfFiles;

        folder = new File("NewEra-Beta/res/saves/");
        listOfFiles = folder.listFiles();

        /*parses slot numbers out of save files. Will get the index of the save slot needed to load
          and store in slotIndex
        */
        for(int i=0;i<listOfFiles.length;i++) {
            if(listOfFiles[i].getName().endsWith(".xml")) {
                String[] parsedName = listOfFiles[i].getName().split("_");
                slotNo = Math.abs(Character.getNumericValue(parsedName[1].charAt(0)));
                if (slotNo == slot) {
                    //found save
                    slotIndex = slotNo;

                    foundSave = true;
                    //load values back into variables

                    saveGameClass = saveGameClass.getInstance();
                    saveGameClass.saveGameCopy((SaveGame) xstream.fromXML(listOfFiles[i]));

                    this.saveName = saveGameClass.getSaveName();
                    this.playerLvl = saveGameClass.getPlayerLvl();
                    this.classID = saveGameClass.getClassID();
                }
            }
        }
        if(foundSave) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSave(int slot) {
        XStream xstream = new XStream();
        Player player;
        boolean foundSave = false;


        int slotNo;
        File folder = new File("NewEra-Beta/res/saves/");
        File[] listOfFiles = folder.listFiles();

        folder = new File("NewEra-Beta/res/saves/");
        listOfFiles = folder.listFiles();


        /*parses slot numbers out of save files. Will get the index of the save slot needed to load
          and store in slotIndex
        */
        for(int i=0;i<listOfFiles.length;i++) {
            if (listOfFiles[i].getName().endsWith(".xml")) {
                String[] parsedName = listOfFiles[i].getName().split("_");
                slotNo = Math.abs(Character.getNumericValue(parsedName[1].charAt(0)));
                if (slotNo == slot) {
                    //found save
                    listOfFiles[i].delete();
                    return true;
                }
            }
        }
        return false;
    }

    public String getSaveName() {return this.saveName;}
    public void setSaveName(String sn) {this.saveName = sn;}
    
    public int getSlot() {return this.slot;}
    public void setSlot(int slotNo) {this.slot = slotNo;}

    public Player getPlayer() {return this.playerObj;}
    public void setPlayer() {this.playerObj = this.playerObj.getInstance();}

    public Inventory getInv() {return this.inv;}
    public void setInv(Inventory i) {this.inv = new Inventory(i, false);}

    public int getItemscount() {return this.itemscount;}
    public void setItemscount(int ic) {this.itemscount = ic;}

    public int getCurrentMap() {return this.currentMap;}
    public void setCurrentMap(int cm) {this.currentMap = cm;}

    public int getClassID() {return this.classID;}

    public int getPlayerLvl() {return this.playerLvl;}

}
