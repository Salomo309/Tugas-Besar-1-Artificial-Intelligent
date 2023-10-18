package main;

public class HillClimbing implements BotAlgorithm {
    private static final int ROW = 8;
    private static final int COL = 8;
    
    @Override
    public int[] getBestMove(char[][] board, char mark) {
        char markBot = mark;
        char markEnemy = mark == 'X' ? 'O' : 'X';
        // create successor
        int maxScore = -1000;
        int[] move = new int[2];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                if (board[i][j] == '-') {
                    int score = evaluate(i, j, board, markBot, markEnemy);
                    if (score > maxScore) {
                        maxScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        // System.out.println(maxScore);
        return move;
    }

    // evaluate successor
    public int evaluate(int i, int j, char[][] board, char markBot, char markEnemy) {
        int score = 1;
        if (i - 1 >= 0 && (board[i - 1][j] == 'X' 
            || board[i - 1][j] == 'O')) {
            score++;
        }
        if (i + 1 < ROW && (board[i + 1][j] == 'X'
            || board[i + 1][j] == 'O')) {
            score++;
        }
        if (j - 1 >= 0 && (board[i][j - 1] == 'X'
            || board[i][j - 1] == 'O')) {
            score++;
        }
        if (j + 1 < COL && (board[i][j + 1] == 'X'
            || board[i][j + 1] == 'O')) {
            score++;
        }
        // looping through all board
        for (int k = 0; k < ROW; k++) {
            for (int l = 0; l < COL; l++) {
                if (!((k == i && l == j - 1) || (k == i && l != j + 1) || (k == i + 1 && l == j) || (k == i + 1 && l == j))) {
                    if (board[k][l] == markBot) {
                        score++;
                    }
                    else if (board[k][l] == markEnemy) {
                        score--;
                    }
                }
            }
        }
        return score;
    }
}
