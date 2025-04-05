import java.util.Random;

public class Enemy {

    String name;
    int health;

    Enemy(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public int attack() {
        int maxDamage = 15;
        Random random = new Random();
        return random.nextInt(1, maxDamage) + 1;
    }

    public void takeDamage(int damageTaken) {
        health -= damageTaken;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getGoldDrop() {
        Random rand = new Random();
        return rand.nextInt(10) + 5; // Drop between 5 and 15 gold
    }

    @Override
    public String toString() {
        return String.format("%s has %d HP", name, health);
    }
}
