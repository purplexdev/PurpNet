package net.purplex.purpnet.utils;

import java.util.HashSet;

public class IDUtils {

    private static HashSet<Integer> takenIDS = new HashSet<Integer>();

    /**
     * Generates a random integer ID
     * @return
     */
    public static int generateRandomId() {
        int random = generateNumberInRange(0, Integer.MAX_VALUE);
        if(takenIDS.contains(random)) {
            return generateRandomId();
        }
        takenIDS.add(random);
        return random;
    }

    public static void removeID(int id) {
        takenIDS.remove(id);
    }

    private static int generateNumberInRange(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
}
