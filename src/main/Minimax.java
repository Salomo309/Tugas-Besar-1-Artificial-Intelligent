package main;

public class Minimax implements BotAlgorithm {
    private static final int ROW = 8;
    private static final int COL = 8;

    @Override
    public int[] getBestMove(char[][] board, char mark) {
        char markBot = mark;
        char markEnemy = mark == 'X' ? 'O' : 'X';

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

        BoardIterator it = new BoardIterator(board, mark);
        char[][] state;
        while ((state = it.next()) != null) {
            int score = minimax(state, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false, markBot, markEnemy);
            // System.out.printf("Score for move (%d, %d): %d\n", it.getMove()[0], it.getMove()[1], score);
            if (score > bestScore) {
                bestScore = score;
                bestMove = it.getMove();
            }
        }
        return bestMove;
    }

    public int minimax(char[][] board, int depth, int alpha, int beta, boolean isMaximizing, char markBot, char markEnemy) {
        if (depth == 0) {
            return evaluate(board, markBot, markEnemy);
        }

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            BoardIterator it = new BoardIterator(board, markBot);
            char[][] state;
            while((state = it.next()) != null) {
                int score = minimax(state, depth - 1, alpha, beta, false, markBot, markEnemy);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            BoardIterator it = new BoardIterator(board, markEnemy);
            char[][] state;
            while((state = it.next()) != null) {
                int score = minimax(state, depth - 1, alpha, beta, true, markBot, markEnemy);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }

    private int evaluate(char[][] board, char markBot, char markEnemy) {
        int botScore = 0;
        int opponentScore = 0;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board[i][j] == markBot) {
                    botScore++;
                } else if (board[i][j] == markEnemy) {
                    opponentScore++;
                }
            }
        }

        return botScore - opponentScore;
    }

    
}