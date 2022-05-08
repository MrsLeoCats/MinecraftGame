package de.tum.in.ase.eist;

public class Item {

    private static final Item[] ITEMS = new Item[]{
            new Item("wooden_sword.png", 2),
            new Item("stone_sword.png", 3),
            new Item("iron_sword.png", 4),
            new Item("diamond_sword.png", 5)
    };

    private static int counter = 0;

    public static void reset() {
        counter = 0;
    }

    public static Item next() {
        Item item = ITEMS[counter++];
        if (counter == ITEMS.length) {
            counter--;
        }
        return item;
    }

    private final String imageLocation;
    private final int damage;

    public Item(String image, int damage) {
        this.imageLocation = image;
        this.damage = damage;
    }

    public String image() {
        return imageLocation;
    }

    public int damage() {
        return damage;
    }

}
