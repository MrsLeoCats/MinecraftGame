package de.tum.in.ase.eist;

public class Item {

    private static final int[] N = {1, 3, 5, 7};

    private static final Item[] ITEMS = new Item[]{
            new Item("wooden_sword.png", N[0]),
            new Item("stone_sword.png", N[1]),
            new Item("iron_sword.png", N[2]),
            new Item("diamond_sword.png", N[3])
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
