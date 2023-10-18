package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class GeneticAlgorithm implements BotAlgorithm {
    private int generations;
    private int rounds;
    private boolean isBotFirst;

    public GeneticAlgorithm(int generations, int rounds, boolean isBotFirst) {
        this.generations = generations;
        this.rounds = rounds;
        this.isBotFirst = isBotFirst;
    }

    public int[][] generateInitialPopulation(int rounds) {
        int[][] initialPopulation = new int[16][rounds];
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
        Set<Integer> uniquePositions = new HashSet<>();

        for (int j = 0; j < rounds; j++) {
            int position;

            // ini ngecek supaya ga duplikat
            do {
                position = random.nextInt(64) + 1;
            } while (uniquePositions.contains(position));

            uniquePositions.add(position);
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

    public int getFitnessValue(int[] individual, char[][] boardGame, boolean isBotFirst) {
        char[][] board = new char[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = boardGame[i][j];
            }
        }
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

        int xCount = countMarks(board, 'X');
        int oCount = countMarks(board, 'O');

        int fitnessValue = 0;

        if (isBotFirst) {
            fitnessValue = xCount - oCount;
        } else {
            fitnessValue = oCount - xCount;
        }

        return fitnessValue;
    }

    public int[][] selection(int[][] population, char[][] boardGame, boolean isBotFirst) {
        Random random = new Random();
        int[][] parents = new int[16][];

        int total = 0;

        for (int i = 0; i < population.length; i++) {
            total += this.getFitnessValue(population[i], boardGame, isBotFirst);
        }

        double[] range = new double[16];
        range[0] = (double) this.getFitnessValue(population[0], boardGame, isBotFirst) * 100 / total;
        // System.out.println(range[0]);
        for (int i = 1; i < 16; i++) {
            if (range[i - 1] + (double) this.getFitnessValue(population[i], boardGame, isBotFirst) * 100 / total < 0) {
                range[i] = 0;
            } else {

                range[i] = range[i - 1] + (double) this.getFitnessValue(population[i], boardGame, isBotFirst) * 100 / total;
            }
        }
        range[15] = 100;

        for (int i = 0; i < 16; i++) {
            double randomValue = random.nextDouble(100);

            for (int j = 0; j < 16; j++) {
                if (randomValue <= range[j]) {
                    parents[i] = population[j];
                    break;
                }
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

        // Check and fix duplicates in child1
        for (int i = 0; i < child1.length; i++) {
            int count = 0;
            for (int j = 0; j < child1.length; j++) {
                if (child1[i] == child1[j]) {
                    count++;
                    if (count > 1) {
                        int newUniqueValue = generateUniqueValue(child1);
                        child1[i] = newUniqueValue;
                        i = -1; // Start over to recheck from the beginning
                        break;
                    }
                }
            }
        }

        // Check and fix duplicates in child2
        for (int i = 0; i < child2.length; i++) {
            int count = 0;
            for (int j = 0; j < child2.length; j++) {
                if (child2[i] == child2[j]) {
                    count++;
                    if (count > 1) {
                        int newUniqueValue = generateUniqueValue(child2);
                        child2[i] = newUniqueValue;
                        i = -1; // Start over to recheck from the beginning
                        break;
                    }
                }
            }
        }

        int[][] children = {child1, child2};

        return children;
    }

    // bantuin ngecek crossover
    private int generateUniqueValue(int[] array) {
        Random random = new Random();
        int value = 0;
        boolean isUnique = false;
        while (!isUnique) {
            value = (int) (random.nextInt(64) + 1);
            isUnique = true;
            for (int i : array) {
                if (i == value) {
                    isUnique = false;
                    break;
                }
            }
        }
        return value;
    }

    public int[] mutate(int[] individual) {
        Random random = new Random();
        Set<Integer> usedPositions = new HashSet<>();

        int mutationIndex;
        int mutationValue;

        // sama kaya tadi, ngecek supaya ga duplikat
        do {
            mutationIndex = random.nextInt(individual.length);
            mutationValue = random.nextInt(64) + 1;
        } while (!usedPositions.add(mutationValue));

        int[] mutatedIndividual = Arrays.copyOf(individual, individual.length);
        mutatedIndividual[mutationIndex] = mutationValue;

        return mutatedIndividual;
    }

    // public void printIndividual(int[] individual) {
    //     System.out.print("Individual Chromosome: [");
    //     for (int i = 0; i < individual.length; i++) {
    //         System.out.print(individual[i]);
    //         if (i < individual.length - 1) {
    //             System.out.print(", ");
    //         }
    //     }
    //     System.out.println("]");
    // }

    // public void printParents(int[][] parents) {
    //     for (int i = 0; i < parents.length; i++) {
    //         System.out.print("Parent " + (i + 1) + ": ");
    //         printIndividual(parents[i]);
    //     }
    // }

    public int[] getBestMove(int generations, int rounds, char[][] boardGame, boolean isBotFirst) {
        int[][] currentPopulation = generateInitialPopulation(rounds);
        // printParents(currentPopulation);

        for (int generation = 0; generation < generations; generation++) {

            // Selection
            int[][] selectedParents = selection(currentPopulation, boardGame, isBotFirst);
            // printParents(selectedParents);

            // Crossover
            int[][] newPopulation = new int[16][rounds];
            for (int i = 1; i < 16; i += 2) {
                int[] parent1 = selectedParents[i - 1];
                int[] parent2 = selectedParents[i];
                int[][] children = crossover(parent1, parent2);
                newPopulation[i - 1] = children[0];
                newPopulation[i] = children[1];
            }

            // Mutation
            for (int i = 0; i < newPopulation.length; i++) {
                newPopulation[i] = mutate(newPopulation[i]);
            }

            currentPopulation = newPopulation;
        }

        int bestFitness = Integer.MIN_VALUE;
        int[] bestMove = null;
        for (int i = 0; i < currentPopulation.length; i++) {
            int fitness = getFitnessValue(currentPopulation[i], boardGame, isBotFirst);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestMove = currentPopulation[i];
            }
        }

        return bestMove;
    }

    public int[] getBestMove(char[][] board, char mark) {
        // TODO: implements getBestMove with mark parameter
        char[][] boardGame = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardGame[i][j] = board[i][j];
            }
        }
        int[] result =  getBestMove(this.generations, this.rounds, boardGame, this.isBotFirst);
        this.rounds--;

        int position = result[0];
        int row = (position - 1) / 8;  // Menghitung baris
        int col = (position - 1) % 8;  // Menghitung kolom

        int[] resultPosition = {row, col};

        return resultPosition;
    }

    public static void main(String[] args) {
        // GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        // System.out.println(geneticAlgorithm.getBestMove(500, 28, boardGame));

        // // Buat populasi awal
        // int rounds = 28;
        // int[][] initialPopulation = geneticAlgorithm.generateInitialPopulation(rounds);

        // // Menampilkan populasi awal
        // System.out.println("Initial Population:");
        // for (int i = 0; i < 16; i++) {
        //     System.out.println("Individual " + (i + 1) + ": " + Arrays.toString(initialPopulation[i]));
        // }

        // // Selection
        // int[][] selectedParents = geneticAlgorithm.selection(initialPopulation);
        // System.out.println("\nSelected Parents:");
        // for (int i = 0; i < 16; i++) {
        //     System.out.println("Parent " + (i + 1) + ": " + Arrays.toString(selectedParents[i]));
        // }

        // // Crossover
        // int[][] newPopulation = new int[16][rounds];
        // for (int i = 1; i < 16; i += 2) {
        //     int[] parent1 = selectedParents[i - 1];
        //     int[] parent2 = selectedParents[i];
        //     int[][] children = geneticAlgorithm.crossover(parent1, parent2);
        //     newPopulation[i - 1] = children[0];
        //     newPopulation[i] = children[1];
        // }
        // System.out.println("\nNew Population After Crossover:");
        // for (int i = 0; i < 16; i++) {
        //     System.out.println("Individual " + (i + 1) + ": " + Arrays.toString(newPopulation[i]));
        // }

        // // Mutate
        // for (int i = 0; i < newPopulation.length; i++) {
        //     newPopulation[i] = geneticAlgorithm.mutate(newPopulation[i]);
        // }
        // System.out.println("\nNew Population After Mutation:");
        // for (int i = 0; i < 16; i++) {
        //     System.out.println("Individual " + (i + 1) + ": " + Arrays.toString(newPopulation[i]));
        // }
    }
}
