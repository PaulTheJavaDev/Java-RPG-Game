import java.util.HashMap;
import java.util.Random;

public class Player {

    static Random random = new Random();

    String name;
    CharacterClass characterClass;
    int health;
    int level;
    HashMap<String, Integer> inventory;

    Player(String name, CharacterClass characterClass, int health, int level, HashMap<String, Integer> inventory) {
        this.name = name;
        this.characterClass = characterClass;
        this.health = health;
        this.level = level;
        this.inventory = inventory;
    }

    //checks if the player is alive
    public boolean isAlive() {
        return health > 0;
    }

    //adds a random amount of health to the player
    public void heal() {
        //check if the player has a Health Potion in the inventory
        if (inventory.containsKey("Health Potion") && inventory.get("Health Potion") > 0) {
            //consume one Health Potion
            inventory.put("Health Potion", inventory.get("Health Potion") - 1);

            //heal the player (random healing between 1 and maxHealthGained)
            int maxHealthGained = 15;
            int healthGained = random.nextInt(maxHealthGained) + 1;
            health += healthGained;

            System.out.println("You used a Health Potion and healed for " + healthGained + " HP.");
        } else {
            System.out.println("You don't have any Health Potions in your inventory!");
        }
    }

    /*
    public void useShield() {
        if (inventory.containsKey("Shield") && inventory.get("Shield") > 0) {
            inventory.put("Shield", inventory.get("Shield") - 1);

            int defense = random.nextInt(3, 6) + 1;
        }
    }
     */


    // Method to simulate looting after defeating an enemy or finding treasure
    public void loot() {
        // Define possible loot items and their drop probabilities (weights)
        String[] lootItems = {"Sword", "Shield", "Health Potion", "Gold", "Mana Potion"};

        // Choose a random item from the loot items
        String item = lootItems[random.nextInt(lootItems.length)];

        // If the item is already in the inventory, increase its quantity
        if (inventory.containsKey(item)) {
            inventory.put(item, inventory.get(item) + 1);
        } else {
            // If the item isn't in the inventory, add it with a quantity of 1
            inventory.put(item, 1);
        }

        // Optionally, print out a message to let the player know what they found
        System.out.println("You found a " + item + "!");
    }

    // Display the player's inventory (for testing)
    public void showInventory() {
        System.out.println("Inventory: ");
        for (String item : inventory.keySet()) {
            System.out.println(item + ": " + inventory.get(item));
        }
    }
}
