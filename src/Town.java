/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private Treasure Treasure;

    //set to 0 initially; 1 == win & gameover; 2 == lose & game over
    private int winCondition;

    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);

        Treasure = new Treasure ();
        winCondition=0;
    }

    public int getWinCondition(){
        return winCondition;
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int)(Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                if(hunter.getGold() - goldDiff<0){
                    printMessage += "What?! You don't have enough to pay up? Guess your time is up!";
                    winCondition = 2;
                } else{
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                    hunter.changeGold(-1 * goldDiff);
                }
            }
        }
    }

    public void lookForTroubleEasy()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int)(Math.random() * 15) + 5;
            if (Math.random() > 0.1)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  (goldDiff + 8) + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                if(hunter.getGold() - goldDiff<0){
                    printMessage += "What?! You don't have enough to pay up? Guess your time is up!";
                    winCondition = 2;
                } else{
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                    hunter.changeGold(-1 * goldDiff);
                }
            }
        }
    }

    public void huntForTreasure ()
    {
        String treasureStr = Treasure.getType();
        printMessage += ("You wander around the town looking for loot, and magically find...\n" + treasureStr + " on the ground!");

        if (treasureStr.equals(Treasure.NOTHING)) {
            printMessage += ("\nHow unlucky.");
        } else {
            if (hunter.collectTreasure(Treasure)){
                printMessage+= ("\nYou don't have this in your collection yet. Pick it up and add it.");
                if(Treasure.collectedAllTreasures(hunter.getTreasureCollection())){
                    winCondition = 1;
                }
            } else {
                printMessage += ("You already collected that treasure.");
            }
        }
    }


    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .17)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .34)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .50)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .67)
        {
            return new Terrain("Desert", "Water");
        }
        else if (rnd < .84)
        {
            return new Terrain("Jungle", "Machete");
        }
        else
        {
            return new Terrain("Marsh", "Boot");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
//        double rand = Math.random();
        double rand = 0.4;
        return (rand < 0.5);
    }



}
