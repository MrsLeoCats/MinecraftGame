package de.tum.in.ase.eist;

public class Item {

    private static final int WS = 1;
    private static final int SS = 3;
    private static final int IS = 5;
    private static final int DS = 7;

    private static final Item[] ITEMS = new Item[]{
            new Item("wooden_sword.png", WS),
            new Item("stone_sword.png", SS),
            new Item("iron_sword.png", IS),
            new Item("diamond_sword.png", DS)
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
