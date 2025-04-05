import java.util.*;

public class GameEngine {

    static Player player = null;
    static Scanner scanner = new Scanner(System.in);

    public static void start() {
        System.out.printf("Welcome to my first self-written Game Engine in Java in %s!\n"
                , Runtime.version().feature());
        createPlayer();
        if (player.characterClass == CharacterClass.Doctor) {
            player.health += 50;
            player.inventory.put("Health Potion", 3);
            System.out.println("You gained 50 HP and 3 Health Potions!");
        }
        mainLoop();
    }

    private static void createPlayer() {
        System.out.println("What would you like your Username to be?");
        String username = scanner.nextLine();

        System.out.printf("\nWelcome %s! Choose one of the given classes: \n", username);

        CharacterClass[] classes = CharacterClass.values();
        for (int i = 0; i < classes.length; i++) {
            System.out.print(classes[i]);
            if (i < classes.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(); //moving to the next line after printing

        CharacterClass chosenClass = null;

        while (chosenClass == null) {
            String input = scanner.nextLine().trim();

            //try to match input (case-insensitive)
            for (CharacterClass cl : CharacterClass.values()) {
                if (cl.name().equalsIgnoreCase(input)) {
                    chosenClass = cl;
                    break;
                }
            }

            if (chosenClass == null) {
                System.out.println("Invalid class. Please try again.");
            }
        }

        System.out.println("You chose to be a " + chosenClass);

        player = new Player(username, chosenClass, 100, 0, new HashMap<String, Integer>());
    }

    private static void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        String[] actions = {"explore", "status", "heal", "quit"};

        while (true) {
            System.out.println("\nWhat action would you like to take?");
            for (String str : actions) System.out.println(" - " + str);

            String action = scanner.next().toLowerCase(); // make it case-insensitive

            switch (action) {
                case "explore" -> explore();
                case "status" -> status();
                case "heal" -> player.heal();
                case "quit" -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid action, please try again.");
            }
        }
    }

    public static void explore() {

        enum Zone {
            Dungeon, Swamp, Town, Mountains, Forest
        }

        //assigns the enemies to the proper zones and their corresponding health values and gold drops
        Map<Zone, Map<String, EnemyData>> zoneEnemies = new HashMap<>();

        //assigning enemies with their health and gold drops to each zone
        Map<String, EnemyData> swampEnemies = new HashMap<>();
        swampEnemies.put("Goblin", new EnemyData(30, 10));
        swampEnemies.put("Worm", new EnemyData(25, 5));
        zoneEnemies.put(Zone.Swamp, swampEnemies);

        Map<String, EnemyData> mountainsEnemies = new HashMap<>();
        mountainsEnemies.put("Yeti", new EnemyData(50, 20));
        mountainsEnemies.put("Rock Golem", new EnemyData(45, 15));
        zoneEnemies.put(Zone.Mountains, mountainsEnemies);

        Map<String, EnemyData> townEnemies = new HashMap<>();
        townEnemies.put("Bandit", new EnemyData(40, 12));
        townEnemies.put("Zombie", new EnemyData(35, 8));
        zoneEnemies.put(Zone.Town, townEnemies);

        Map<String, EnemyData> dungeonEnemies = new HashMap<>();
        dungeonEnemies.put("Skeleton", new EnemyData(20, 7));
        dungeonEnemies.put("Dark Mage", new EnemyData(60, 25));
        zoneEnemies.put(Zone.Dungeon, dungeonEnemies);

        Map<String, EnemyData> forestEnemies = new HashMap<>();
        forestEnemies.put("Entangled Spirit", new EnemyData(25, 10));
        forestEnemies.put("Goblin Scout", new EnemyData(30, 5));
        zoneEnemies.put(Zone.Forest, forestEnemies);

        //choose a random zone
        Zone[] zones = Zone.values();
        Random rand = new Random();
        Zone randomZone = zones[rand.nextInt(zones.length)];

        //get the enemies in the chosen zone
        Map<String, EnemyData> enemiesInZone = zoneEnemies.get(randomZone);

        //pick a random enemy and create an Enemy instance with its corresponding health and gold
        String enemyName = (String) enemiesInZone.keySet().toArray()[rand.nextInt(enemiesInZone.size())];
        EnemyData enemyData = enemiesInZone.get(enemyName);
        Enemy enemy = new Enemy(enemyName, enemyData.getHealth());

        //display the encounter
        System.out.println("You explore the " + randomZone + " and encounter a " + enemyName + "!");
        System.out.println(enemy);

        //player variables: health and damage
        int playerHealth = player.health;
        Random damage = new Random();

        //fight loop
        while (enemy.isAlive() && playerHealth > 0) {

            //player attacks
            int playerAttack;

            //if is a warrior, get a better sword (more attack damage)
            if (player.characterClass == CharacterClass.Warrior) {
                playerAttack = damage.nextInt(20) + 1;
            } else {
                playerAttack = damage.nextInt(10) + 1;
            }

            enemy.takeDamage(playerAttack);
            System.out.println("You hit the " + enemy.name + " for " + playerAttack + " damage.");

            //if enemy is defeated, break the loop
            if (!enemy.isAlive()) break;

            //enemy attacks back
            int enemyAttack = enemy.attack();
            playerHealth -= enemyAttack;
            System.out.println("The " + enemy.name + " hits you for " + enemyAttack + " damage.");
            System.out.println("You have " + playerHealth + " HP left.");
        }

        //outcome of the fight
        if (playerHealth <= 0) {
            System.out.println("You have been defeated by the " + enemy.name + "...");
        } else {
            //on defeat, the enemy drops gold
            int goldDropped = enemy.getGoldDrop();
            System.out.println("You defeated the " + enemy.name + "!");
            System.out.println("You have stolen " + goldDropped + " gold coins!");
            player.loot();
            player.showInventory();
        }
    }

    static class EnemyData {
        int health;
        int goldDrop;

        public EnemyData(int health, int goldDrop) {
            this.health = health;
            this.goldDrop = goldDrop;
        }

        public int getHealth() {
            return health;
        }
    }

    public static void status() {
        System.out.printf("\nAlive: %b | Player health: %d%n", player.isAlive(), player.health);
    }
}
