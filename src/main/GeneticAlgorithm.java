package main;

import java.util.Arrays;
import java.util.Random;

class GeneticAlgorithm {
    public int[][] generateInitialPopulation(int rounds) {
        int[][] initialPopulation = new int[4][rounds];
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < rounds; j++) {
                int position = random.nextInt(64) + 1;
                initialPopulation[i][j] = position;
            }
        }

        return initialPopulation;
    }

    public int countMarks(char[][] board, char mark) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == mark) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getFitnessValue(int[] individual) {
        char[][] board = new char[8][8];
        int currentPlayer = 1;

        for (int i = 0; i < individual.length; i++) {
            int position = individual[i];
            int row = (position - 1) / 8;
            int col = (position - 1) % 8;

            if (currentPlayer == 1) {
                board[row][col] = 'X';
            } else {
                board[row][col] = 'O';
            }

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = row + dr;
                    int newCol = col + dc;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        char neighborMark = board[newRow][newCol];
                        if (neighborMark != ' ' && neighborMark != board[row][col]) {
                            board[newRow][newCol] = board[row][col];
                        }
                    }
                }
            }

            currentPlayer = 3 - currentPlayer;
        }

        int botCount = countMarks(board, 'X');
        int playerCount = countMarks(board, 'O');
        int fitnessValue = botCount - playerCount;

        return fitnessValue;
    }



    public int[][] selection(int rounds) {
        int[][] population = this.generateInitialPopulation(rounds);
        Random random = new Random();
        int[][] parents = new int[4][];

        int total = this.getFitnessValue(population[0]) + this.getFitnessValue(population[1]) + this.getFitnessValue(population[2]) + this.getFitnessValue(population[3]);

        int rangeA = this.getFitnessValue(population[0]) * 100 / total;
        int rangeB = rangeA + this.getFitnessValue(population[1]) * 100 / total;
        int rangeC = rangeB + this.getFitnessValue(population[2]) * 100 / total;
        // int rangeD = rangeC + this.getFitnessValue(population[3]);

        for (int i = 0; i < 4; i++) {
            int randomValue = random.nextInt(101);

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

    public int[][] crossover(int[] parent1, int[] parent2) {
        int crossoverPoint = parent1.length / 2;
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent1.length];

        for (int i = 0; i < crossoverPoint; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
        }

        for (int i = crossoverPoint; i < parent1.length; i++) {
            child1[i] = parent2[i];
            child2[i] = parent1[i];
        }
        int[][] children = {child1, child2};

        return children;
    }

    public int[] mutate(int[] individual) {
        Random random = new Random();
        int mutationIndex = random.nextInt(individual.length);
        int mutationValue = random.nextInt(64) + 1;
        int[] mutatedIndividual = Arrays.copyOf(individual, individual.length);
        mutatedIndividual[mutationIndex] = mutationValue;

        return mutatedIndividual;
    }

    public static void main(String[] args) {
        // GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        // int rounds = 28; //

        // int[][] initialPopulation = geneticAlgorithm.generateInitialPopulation(rounds);

        // // Menampilkan populasi awal
        // System.out.println("Initial Population:");
        // for (int i = 0; i < initialPopulation.length; i++) {
        //     System.out.println("Individual " + (i + 1) + ": " + Arrays.toString(initialPopulation[i]));
        // }

        // int rounds = 28; // Ganti dengan jumlah ronde yang sesuai

        // int[][] initialPopulation = geneticAlgorithm.generateInitialPopulation(rounds);

        // // Menampilkan populasi awal
        // System.out.println("Initial Population:");
        // for (int i = 0; i < 4; i++) {
        //     System.out.println("Individual " + (i + 1) + ": " + Arrays.toString(initialPopulation[i]));
        // }

        // // Menghitung nilai fitness untuk setiap individu dalam populasi awal
        // for (int i = 0; i < 4; i++) {
        //     int fitnessValue = geneticAlgorithm.getFitnessValue(initialPopulation[i]);
        //     System.out.println("Fitness Value for Individual " + (i + 1) + ": " + fitnessValue);
        // }
    }
}
