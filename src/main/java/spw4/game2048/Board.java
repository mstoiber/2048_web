package spw4.game2048;

import java.util.Arrays;
import java.util.Random;

public class Board {
    static final int SIZE = 4;
    public static final double VALUE_PROBABILITY = 0.9;
    public static final int START_VALUE2 = 4;
    public static final int START_VALUE1 = 2;
    private final Random random;
    private final int[][] board;

    public Board(Random random) {
        this.random = random;
        board = new int[SIZE][SIZE];
    }

    public Board() {
        this(new Random());
    }

    public void setTileValue(int row, int column, int value) {
        throwIfValueInvalid(value);
        throwIfPositionIsInvalid(row, column);
        board[row][column] = value;
    }

    public int getTileValue(int row, int col) {
        throwIfPositionIsInvalid(row, col);
        return board[row][col];
    }

    public void initialize() {
        // Clear Board
        for (var row : board) {
            Arrays.fill(row, 0);
        }

        spawnRandomTiles();
    }

    public void spawnRandomTiles() {
        for (int i = 0; i < 2; i++) {
            final int value = random.nextDouble() > VALUE_PROBABILITY ? START_VALUE2 : START_VALUE1;
            int row, column;
            do {
                row = random.nextInt(SIZE);
                column = random.nextInt(SIZE);
            } while (getTileValue(row, column) != 0);
            setTileValue(row, column, value);
        }
    }

    @Override
    public String toString() {
        for (int[] row : board) {
            for (int value : row) {
                System.out.print((value > 0 ? value : ".") + "\t");
            }
            System.out.println();
            System.out.println();
        }
        return super.toString();
    }

    public boolean isFull() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                if(getTileValue(row, column) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void throwIfPositionIsInvalid(int row, int column) {
        if (row < 0 || column < 0 || row >= board.length || column >= board.length) {
            throw new IllegalArgumentException("row or column are out of bounds");
        }
    }

    private void throwIfValueInvalid(int value) {
        if (value == 1 || (value & value - 1) != 0) {
            throw new IllegalArgumentException("Value has to be a power of two");
        }
    }
}
