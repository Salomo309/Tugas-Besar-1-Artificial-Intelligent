package main;

public class Bot {
    private static final int ROW = 8;
    private static final int COL = 8;
    private char[][] board;
    private BotAlgorithm algorithm;

    public Bot() {
        // Initialize the board with empty cells
        board = new char[ROW][COL];
    }

    public void setAlgorithm(BotAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setBoardState(char[][] newBoard) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                board[i][j] = newBoard[i][j];
            }
        }
    }

    public int[] getBestMove() {
        return algorithm.getBestMove(board);
    }

}
