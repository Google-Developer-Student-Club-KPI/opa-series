import java.util.Arrays;
import java.util.Random;

public class KnapsackProblem {
    private final boolean[][] chromosoms;

    private final int size;
    private final int[][] itemsCharacteristics;
    private final int amountOfSpecies;

    private final Species[] species;

    private final double CROSS_INDEX = 70;
    private final int MUTATION_CHANCE = 10;

    public KnapsackProblem(int size, int[][] items, int species){
        this.size = size;
        this.itemsCharacteristics = items;
        this.amountOfSpecies = species;

        this.chromosoms = new boolean[species][items.length];
        this.onStartPolulation();

        this.species = new Species[species];
        this.recalculateValues();
    }

    public void startGeneticAlgorithm(int iterations){
        Random rand = new Random();
        int record = 0;
        int numOfSteps = 0;

        for (int i = 0; i < iterations; i++) {
            // crossbreeding

            int random = rand.nextInt(chromosoms.length);
            int best = species[amountOfSpecies-1].id;

            boolean[] child = crossbreeding(chromosoms[best], chromosoms[random]);

            if(!isAlive(child))
                continue;


            //mutation
            if(rand.nextInt(100) < MUTATION_CHANCE){
                boolean[] mutChild = mutation(child);
                if(isAlive(mutChild))
                    child = mutChild;
            }

            int toKillId = species[0].id;
            chromosoms[toKillId] = child;

            recalculateValues();

            // output block
            if(species[amountOfSpecies-1].value > record) {
                record = species[amountOfSpecies-1].value;
                numOfSteps = i;
                System.out.println("Step: " + i + ": " + record);
            }
        }
    }

    private boolean[] mutation(boolean[] child){
        boolean[] newChild = child.clone();
        Random random = new Random();

        int randomGenId = random.nextInt(child.length);
        newChild[randomGenId] = !newChild[randomGenId];

        return newChild;
    }

    private boolean isAlive(boolean[] i){
        int size = 0;
        boolean empty = true;

        for (int j = 0; j < i.length; j++) {
            if(i[j]) {
                size += itemsCharacteristics[j][1];
                empty = false;
            }
        }

        return (!empty && size <= this.size);
    }

    public boolean[] crossbreeding(boolean[] first, boolean[] second){
        boolean[] child = new boolean[first.length];
        int th1 = (int)(CROSS_INDEX/100 * first.length);
        int th2 = (int)((100-CROSS_INDEX)/100 * second.length);

        System.arraycopy(first, 0, child, 0, th1);
        System.arraycopy(second, th1, child, th1, th2);

        return child;
    }

    private void onStartPolulation(){
        Random random = new Random();

        for (int i = 0; i < chromosoms.length; i++) {
            chromosoms[i][random.nextInt(chromosoms[i].length)] = random.nextBoolean();
        }
    }

    // 0 0 0 1 0 0 1
    // 1 0 1 0 1 0 1

    private void recalculateValues(){
        Arrays.fill(species, null);

        for (int i = 0; i < chromosoms.length; i++) {
            int value = 0;
            int size = 0;
            for (int j = 0; j < chromosoms[i].length; j++) {
                if(chromosoms[i][j]){
                    value+=itemsCharacteristics[j][0];
                    size+=itemsCharacteristics[j][1];
                }
            }
            species[i] = new Species(i, value, size );
        }
        Arrays.sort(this.species);
    }


    private static class Species implements Comparable<Species>{
        int value;
        int id;
        int size;

        public Species(int id, int value, int size){
            this.value = value;
            this.id = id;
            this.size = size;
        }

        @Override
        public int compareTo(Species o) {
            return Integer.compare(this.value, o.value);
        }

        @Override
        public String toString(){
            return "Species: " + this.id + " " + this.value;
        }
    }
}
