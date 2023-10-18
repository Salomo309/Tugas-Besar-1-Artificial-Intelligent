package main;

public class BoardIterator {
    private char[][] board;
    private int row;
    private int col;
    private char player;

    public BoardIterator(char[][] board, char currentPlayer) {
        this.board = board;
        row = 0;
        col = 0;
        player = currentPlayer;
    }

    public char[][] next() {
        char[][] result = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result[i][j] = board[i][j];
            }
        }
        if (row > 7) {
            return null;
        }
        while (board[row][col] != '-') {
            advanceCell();
            if (row > 7) {
                return null;
            }
        }
        result[row][col] = player;
        if (row - 1 >= 0 && result[row - 1][col] != '-') {
            result[row - 1][col] = player;
        }
        if (row + 1 < 8 && result[row + 1][col] != '-') {
            result[row + 1][col] = player;
        }
        if (col - 1 >= 0 && result[row][col - 1] != '-') {
            result[row][col - 1] = player;
        }
        if (col + 1 < 8 && result[row][col + 1] != '-') {
            result[row][col + 1] = player;
        }
        
        player = (player == 'X') ? 'O' : 'X';
        advanceCell();
        return result;
    }

    private void advanceCell() {
        if (col >= 7) {
            col = 0;
            row++;
        } else {
            col++;
        }
    }

    public int[] getMove() {
        assert row != 0 && col != 0: "Jangan panggil fungsi ini sebelum next()";

        int resRow = row;
        int resCol = col;
        if (resCol == 0) {
            resRow--;
            resCol = 7;
        } else {
            resCol--;
        }

        return new int[] {resRow, resCol};
    }
}
