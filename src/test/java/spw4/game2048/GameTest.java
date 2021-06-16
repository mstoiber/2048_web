package spw4.game2048;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    @Test
    @DisplayName("Game.getScore Returns zero At Start")
    void getScoreReturnsZeroAtStart() {
        //given
        int expected = 0;
        //when
        var game = new Game();

        //then
        assertEquals(expected, game.getScore());

    }

    @Test
    @DisplayName("Game.initialize uses the method board.initialize")
    void initializeInitializesBoard() {
        //given
        var spy = spy(new Board());
        var game = new Game(spy);

        //when
        game.initialize();

        //then
        verify(spy, times(1)).initialize();
    }

    @Test
    @DisplayName("Game.initialize resets score")
    void initializeResetsScore() {
        //given
        var board = new Board();
        var game = new Game();
        board.setTileValue(0, 0, 2);
        board.setTileValue(0, 1, 2);
        game.move(Direction.left);

        //when
        game.initialize();

        //then
        assertEquals(0, game.getScore());
    }

    @DisplayName("Game.move gets called with horizontal Movement")
    @ParameterizedTest(name = "direction = {0}, row = {1}, column = {2}")
    @CsvSource({
            "left, 0, 0",
            "right, 0, 3"
    })
    void moveWithHorizontalMovement(Direction direction, int row, int column) {
        //given
        var board = new Board();
        var game = new Game(board);
        board.setTileValue(0, 0, 2);
        board.setTileValue(0, 1, 2);

        //when
        game.move(direction);

        //then
        assertEquals(4, board.getTileValue(row, column));
    }

    @DisplayName("Game.move gets called with vertical Movement")
    @ParameterizedTest(name = "direction = {0}, row = {1}, column = {2}")
    @CsvSource({
            "up, 0, 0",
            "down, 3, 0"
    })
    void moveWithVerticalMovement(Direction direction, int row, int column) {
        //given
        var board = new Board();
        var game = new Game(board);
        board.setTileValue(0, 0, 2);
        board.setTileValue(1, 0, 2);

        //when
        game.move(direction);

        //then
        assertEquals(4, board.getTileValue(row, column));
    }

    @DisplayName("Game.move does not merge different tiles")
    @Test
    void moveDoesNotMergeDifferentTiles() {
        //given
        var board = new Board();
        var game = new Game(board);
        board.setTileValue(0, 0, 2);
        board.setTileValue(0, 1, 4);
        board.setTileValue(0, 3, 8);

        //when
        game.move(Direction.left);

        //then
        assertAll(
                () -> assertEquals(2, board.getTileValue(0, 0)),
                () -> assertEquals(4, board.getTileValue(0, 1)),
                () -> assertEquals(8, board.getTileValue(0, 2))
        );
    }


    @Test()
    @DisplayName("Game.move increases score")
    void moveIncreasesScore() {
        //given
        var board = spy(new Board());
        doNothing()
                .when(board)
                .spawnRandomTiles();

        var game = new Game(board);
        board.setTileValue(0, 0, 2);
        board.setTileValue(0, 1, 2);
        board.setTileValue(1, 0, 4);
        board.setTileValue(0, 3, 8);


        //when
        game.move(Direction.left);
        game.move(Direction.up);
        game.move(Direction.right);

        //then
        assertEquals(28, game.getScore());
    }

    @Test
    @DisplayName("Game.move uses Board.spawnRandomTiles")
    void moveUsesBoardSpawnRandomTiles() {
        //given
        var board = spy(new Board());
        var game = new Game(board);

        //when
        game.move(Direction.left);

        //then
        verify(board, times(1)).spawnRandomTiles();
    }

    @Test
    @DisplayName("Game.movesAvailable returns true when tiles can be moved")
    void movesAvailableReturnsTrueWhenTilesCanBeMoved() {
        //given
        var board = new Board();
        var game = new Game(board);
        board.setTileValue(0,0, 2);

        //when
        var result = game.movesAvailable();

        //then
        assertTrue(result);

    }

    @Test
    @DisplayName("Game.movesAvailable returns false when no tiles available")
    void movesAvailableReturnsFalseWhenNoTilesAvailable() {
        //given
        var board = new Board();
        var game = new Game(board);
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board.setTileValue(i, j, (i + j) % 2 == 0 ? 2 : 4);
            }
        }

        //when
        var result = game.movesAvailable();

        //then
        assertFalse(result);

    }

    @Test
    @DisplayName("Game.movesAvailable returns true when one tile Available")
    void movesAvailableReturnsTrueWhenOneTileAvailable() {
        //given
        var board = new Board();
        var game = new Game(board);
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board.setTileValue(i, j, (i + j) % 2 == 0 ? 2 : 4);
            }
        }
        board.setTileValue(0, 0, 0);

        //when
        var result = game.movesAvailable();

        //then
        assertTrue(result);

    }

    @Test
    @DisplayName("Game.isOver returns false when moves available")
    void isOverReturnsFalseWhenMovesAvailable () {
        //given
        var game = new Game();
        game.initialize();

        //when
        var result = game.isOver();

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("Game.isOver returns true when no moves available")
    void isOverReturnsTrueWhenNoMovesAvailable () {
        //given
        var board = new Board();
        var game = new Game(board);
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                board.setTileValue(i, j, (i + j) % 2 == 0 ? 2 : 4);
            }
        }

        //when
        var result = game.isOver();

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("Game.isOver returns true when game is won")
    void isOverReturnsTrueWhenGameIsWon() {
        //given
        var board = new Board();
        var game = new Game(board);
        board.setTileValue(0, 0, 2048);

        //when
        var result = game.isOver();

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("Game.isWon returns true when game is won")
    void isWonReturnsTrueWhenGameIsWon() {
        //given
        var board = new Board();
        var game = new Game(board);
        board.setTileValue(0, 0, 2048);

        //when
        var result = game.isWon();

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("Game.isWon returns false when game is not won")
    void isWonReturnsFalseWhenGameIsNotWon() {
        //given
        var board = new Board();
        var game = new Game(board);
        game.initialize();

        //when
        var result = game.isWon();

        //then
        assertFalse(result);
    }

}