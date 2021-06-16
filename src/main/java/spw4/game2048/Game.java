package spw4.game2048;

import java.util.LinkedList;

public class Game {
    public static final int HIGHEST_VALUE = 2048;
    private final Board board;
    private int score = 0;
    private int moves = 0;

    public Game() {
        this(new Board());
    }

    public Game(Board board) {
        this.board = board;
    }

    public int getScore() {
        return score;
    }

    public boolean isOver() {
        return !movesAvailable() || isWon();
    }

    public boolean isWon() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                if(board.getTileValue(row, column) == HIGHEST_VALUE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Moves: " + moves + "\t" + "Score: " + score + "\n"
                + board;
    }

    public void initialize() {
        score = 0;
        moves = 0;
        board.initialize();
    }

    public void move(Direction direction) {
        boolean horizontal = direction == Direction.left || direction == Direction.right;
        boolean upLeft = direction == Direction.left || direction == Direction.up;
        for (int i = 0; i < Board.SIZE; i++) {
            LinkedList<Integer> elements = new LinkedList<>();
            for (int j = 0; j < Board.SIZE; j++) {
                final int row = horizontal ? i : j;
                final int column = horizontal ? j : i;
                final int tileValue = board.getTileValue(row, column);
                if (tileValue != 0) {
                    if (upLeft) {
                        elements.addLast(tileValue);
                    } else {
                        elements.addFirst(tileValue);
                    }
                }
            }

            LinkedList<Integer> mergedElements = new LinkedList<>();
            while (elements.size() > 1) {
                final int first = elements.remove();
                final int second = elements.getFirst();
                if (first == second) {
                    final int res = first + second;
                    score += res;
                    mergedElements.add(res);
                    elements.remove();
                } else {
                    mergedElements.add(first);
                }
            }

            mergedElements.addAll(elements);

            int j = upLeft ? 0 : Board.SIZE - 1;
            while (upLeft && j < Board.SIZE || !upLeft && j >= 0) {
                final int row = horizontal ? i : j;
                final int column = horizontal ? j : i;
                if (mergedElements.isEmpty()) {
                    board.setTileValue(row, column, 0);
                } else {
                    board.setTileValue(row, column, mergedElements.remove());
                }
                j += upLeft ? +1 : -1;
            }
        }
        board.spawnRandomTiles();
        moves++;
    }

    public boolean movesAvailable() {
        for (int row = 0; row < Board.SIZE - 1; row++) {
            for (int column = 0; column < Board.SIZE - 1; column++) {
                if (board.getTileValue(row, column) == board.getTileValue(row + 1, column)
                        || board.getTileValue(row, column) == board.getTileValue(row, column + 1)) {
                    return true;
                }
            }
        }
        return !board.isFull();
    }

    public int getValueAt(int x, int y) {return board.getTileValue(x, y);}

    public int getMoves() {
        return moves;
    }
}
