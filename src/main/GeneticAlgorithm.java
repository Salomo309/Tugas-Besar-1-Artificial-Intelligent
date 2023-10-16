package main;

import java.util.Random;

class GeneticAlgorithm {
    public String[] generateInitialPopulation() {
        String[] initialPopulation = new String[4];

        for (int i = 0; i < 4; i++) {
            StringBuilder individual = new StringBuilder(64);
            Random random = new Random();
            for (int j = 0; j < 64; j++) {
                individual.append(random.nextInt(8) + 1);;
            }
            initialPopulation[i] = individual.toString();
        }

        return initialPopulation;
    }

    public int getFitnessValue(String individu) {
        // TODO
        return 1;
    }

    public String[] selection() {
        String[] population = this.generateInitialPopulation();
        Random random = new Random();
        String[] parents = new String[4];

        for (int i = 0; i < 4; i++) {
            int randomValue = random.nextInt(101);

            int rangeA = this.getFitnessValue(population[0]);
            int rangeB = rangeA + this.getFitnessValue(population[1]);
            int rangeC = rangeB + this.getFitnessValue(population[2]);
            // int rangeD = rangeC + this.getFitnessValue(population[3]);

            if (randomValue <= rangeA) {
                parents[i] = population[0];
            } else if (randomValue <= rangeB && randomValue > rangeA) {
                parents[i] = population[1];
            } else if (randomValue <= rangeC && randomValue > rangeB) {
                parents[i] = population[2];
            } else {
                parents[i] = population[3];
            }
        }

        return parents;
    }

    public String[] crossOver(String parent1, String parent2) {
        int crossoverPoint = parent1.length() / 2;

        String child1 = parent1.substring(0, crossoverPoint) + parent2.substring(crossoverPoint);
        String child2 = parent2.substring(0, crossoverPoint) + parent1.substring(crossoverPoint);

        String[] children = {child1, child2};
        return children;
    }

    public String mutate(String individual) {
        Random random = new Random();
        int mutationValue = random.nextInt(10);

        StringBuilder mutatedIndividual = new StringBuilder(individual);
        mutatedIndividual.setCharAt(2, (char) ('0' + mutationValue));

        return mutatedIndividual.toString();
    }
}
