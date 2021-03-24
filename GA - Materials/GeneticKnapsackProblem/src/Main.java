import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final int amountOfThings = 5;
        final int maxItemValue = 100;
        final int maxItemSize = 100;
        final int knapsackSize = 300;
        final int amountOfSpecies = 10;
        final int iterations = 100000;

        int[][] items = new int[amountOfThings][2];
        generateItems(items, maxItemValue, maxItemSize);

        KnapsackProblem knapsackProblem = new KnapsackProblem(knapsackSize, items, amountOfSpecies);
        knapsackProblem.startGeneticAlgorithm(iterations);
    }

    public static void generateItems(int[][] items, int maxValue, int maxSize){
        Random random = new Random();
        for (int i = 0; i < items.length; i++) {
            int random_value = random.nextInt(maxValue);
            int random_size = random.nextInt(maxSize);

            items[i][0] = random_value;
            items[i][1] = random_size;
        }
    }
}
