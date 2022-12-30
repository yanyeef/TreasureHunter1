/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Hunter
{
    //Keeps the items in the kit separate
    private static final String KIT_DELIMITER = ";";

    //instance variables
    private String hunterName;
    private String kit;
    private String treasureCollection = "";
    private int gold;

    //Constructor
    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     */
    public Hunter(String hunterName, int startingGold)
    {
        this.hunterName = hunterName;
        kit = "";
        gold = startingGold;
    }

    //Accessors
    public String getHunterName()
    {
        return hunterName;
    }

    public String getTreasureCollection(){
        return treasureCollection;
    }

    public String getKit()
    {
        return kit;
    }

    public int getGold()
    {
        return gold;
    }

    public void changeGold(int modifier)
    {
        gold += modifier;
        if (gold < 0)
        {
            gold = 0;
        }
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem  the cost of the item
     *
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem)
    {
        if (costOfItem == 0 || gold < costOfItem || hasItemInKit(item, kit))
        {
            return false;
        }

        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice  the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice)
    {
        if (buyBackPrice <= 0 || !hasItemInKit(item, kit))
        {
            return false;
        }

        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     *  Removes an item from the kit.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item)
    {
        int itmIdx = kit.indexOf(item);

        // if item is found
        if (itmIdx >= 0)
        {
            String tmpKit = kit.substring(0, itmIdx);
            int endIdx = kit.indexOf(KIT_DELIMITER, itmIdx);
            tmpKit += kit.substring(endIdx + 1);

            // update kit
            kit = tmpKit;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it adds an item to the end of the String representing the hunter's kit.<br /><br />
     * A KIT_DELIMITER character is added to the end of the of String.
     *
     * @param item The item to be added to the kit.
     * @returns true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item)
    {
        String itemUpper;
        if (!hasItemInKit(item, kit))
        {
            itemUpper = (item.substring(0,1)).toUpperCase() + item.substring(1);
            kit += itemUpper + KIT_DELIMITER;
            return true;
        }

        return false;
    }
    /**
     * Checks to make sure that the treasure has not already been collected
     * If not, it adds the treasure to the end of the String representing the hunter's treausre collection.<br /><br />
     * A KIT_DELIMITER character is added to the end of the of String.
     *
     * @param treasure the treasure to be collected
     * @returns true if the item is not in the kit and has been added.
     */
    public boolean collectTreasure(Treasure treasure){
        String item = treasure.getType();
        if (!hasItemInKit(item, treasureCollection)){
            treasureCollection += item + KIT_DELIMITER;
            return true;
        }
        return false;
    }

    /**
     * Searches the kit String for a specified item.
     *
     * @param item The search item
     * @param inventory The items in the inventory
     *
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item, String inventory)
    {
        int placeholder = 0;

        while (placeholder < inventory.length() - 1)
        {
            int endOfItem = inventory.indexOf(KIT_DELIMITER, placeholder);
            String tmpItem = inventory.substring(placeholder, endOfItem);
            placeholder = endOfItem + 1;
            if (tmpItem.equals(item))
            {
                // early return
                return true;
            }
        }
        return false;
    }

    /** Returns a printable representation of the inventory, which
     *  is a list of the items in kit, with the KIT_DELIMITER replaced with a space
     *
     * @return  The printable String representation of the inventory
     */
    public String getInventory()
    {
        String printableTreasure = replaceDelimiter(kit);
        return printableTreasure;
    }

    public String getTreasures(){
        String printableTreasure  = replaceDelimiter(treasureCollection);
        return printableTreasure;
    }
    private String replaceDelimiter(String str){
        String updatedStr = str;
        String space = " ";
        int index =0;

        while (updatedStr.indexOf(KIT_DELIMITER) != -1){
            index = updatedStr.indexOf(KIT_DELIMITER);
            updatedStr = updatedStr.substring(0, index) + space + updatedStr.substring(index +1);
        }
        return updatedStr;
    }

    /**
     * @return A string representation of the hunter.
     */
    public String toString()
    {
        String str = hunterName + " has " + gold + " gold";
        if (!kit.equals(""))
        {
            str += " and " + getInventory();
        }

        str += "\nTreasures collected: ";
        if(!treasureCollection.equals("")){
            str += getTreasures();
        } else {
            str += "none";
        }
        return str;
    }
}
