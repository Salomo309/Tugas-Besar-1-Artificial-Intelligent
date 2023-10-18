package main;

import java.util.Arrays;
import java.util.Random;

class GeneticAlgorithm implements BotAlgorithm {
    private int generations;
    private int rounds;

    public GeneticAlgorithm(int generations, int rounds) {
        this.generations = generations;
        this.rounds = rounds;
    }

    public int[][] generateInitialPopulation(int rounds) {
        int[][] initialPopulation = new int[16][rounds];
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
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

    public int getFitnessValue(int[] individual, char[][] boardGame) {
        char[][] board = boardGame;
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

    public int[][] selection(int[][] population, char[][] boardGame) {
        Random random = new Random();
        int[][] parents = new int[16][];

        int total = 0;

        for (int i = 0; i < population.length; i++) {
            total += this.getFitnessValue(population[i], boardGame);
        }

        double[] range = new double[16];
        range[0] = (double) this.getFitnessValue(population[0], boardGame) * 100 / total;
        // System.out.println(range[0]);
        for (int i = 1; i < 16; i++) {
            range[i] = range[i - 1] + (double) this.getFitnessValue(population[i], boardGame) * 100 / total;
        }

        for (int i = 0; i < 16; i++) {
            double randomValue = random.nextDouble() * 100;

            for (int j = 0; j < 16; j++) {
                if (randomValue > (j > 0 ? range[j - 1] : 0) && randomValue <= range[j]) {
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

    public int[] getBestMove(int generations, int rounds, char[][] boardGame) {
        int[][] currentPopulation = generateInitialPopulation(rounds);

        for (int generation = 0; generation < generations; generation++) {
            // System.out.println(generation);

            // Selection
            int[][] selectedParents = selection(currentPopulation, boardGame);

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
            int fitness = getFitnessValue(currentPopulation[i], boardGame);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestMove = currentPopulation[i];
            }
        }

        return bestMove;
    }

    public int[] getBestMove(char[][] board) {
        char[][] boardGame = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardGame[i][j] = board[i][j];
            }
        }
        int[] result =  getBestMove(this.generations, this.rounds, boardGame);
        this.rounds--;
        return result;
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
