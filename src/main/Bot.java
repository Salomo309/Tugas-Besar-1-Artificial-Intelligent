package main;

public class Bot {
    private static final int ROW = 8;
    private static final int COL = 8;
    private String[][] board;

    public Bot() {
        // Initialize the board with empty cells
        board = new String[ROW][COL];
    }

    public void setBoardState(String[][] newBoard) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                board[i][j] = newBoard[i][j];
            }
        }
    }

    // public void setBoardValue(int row, int col, String val) {
    // board[row][col] = val;
    // }

    public int[] move() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }

        System.out.println();

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (isEmptyCell(i, j)) {
                    makeMove(i, j, "O"); // Bot's move

                    // call minimax
                    int score = minimax(3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                    undoMove(i, j);

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    public int minimax(int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0) {
            return evaluate();
        }

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (isEmptyCell(i, j)) {
                        makeMove(i, j, "O");
                        int score = minimax(depth - 1, alpha, beta, false);
                        undoMove(i, j);
                        maxScore = Math.max(maxScore, score);
                        alpha = Math.max(alpha, score);
                        if (beta <= alpha) {
                            break; // Prune the tree
                        }
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (isEmptyCell(i, j)) {
                        makeMove(i, j, "X");
                        int score = minimax(depth - 1, alpha, beta, true);
                        undoMove(i, j);
                        minScore = Math.min(minScore, score);
                        beta = Math.min(beta, score);
                        if (beta <= alpha) {
                            break; // Prune the tree
                        }
                    }
                }
            }
            return minScore;
        }
    }

    private boolean isEmptyCell(int row, int col) {
        return board[row][col] == "-";
    }

    private void makeMove(int row, int col, String player) {
        board[row][col] = player;
        // Update the board state according to your game rules
    }

    private void undoMove(int row, int col) {
        board[row][col] = "-";
        // Restore the board state to the previous state
    }

    private int evaluate() {
        int botScore = 0;
        int opponentScore = 0;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board[i][j].equals("O")) {
                    botScore++;
                } else if (board[i][j].equals("X")) {
                    opponentScore++;
                }
            }
        }

        return botScore - opponentScore;
    }
}
