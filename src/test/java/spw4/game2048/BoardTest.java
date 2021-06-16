package spw4.game2048;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BoardTest {

    @Test
    @DisplayName("Board.setTileValue when Output is Input")
    void setTileValueReturnsInputValue() {
        //given
        Board board = new Board();
        var expected = 2;
        var input = 2;
        //when
        board.setTileValue(1, 1, input);
        //then
        assertEquals(expected, board.getTileValue(1, 1));
    }

    @DisplayName("Board.setTileValue when Invalid Value Throws Illegal Argument Exception")
    @ParameterizedTest(name = "value = {0}")
    @CsvSource({"1", "-1", "3"})
    void setTileValue_whenInvalidValueThrowsIllegalArgumentException(int value) {
        //given
        var board = new Board();

        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> board.setTileValue(0, 0, value));
    }

    @DisplayName("Board.setTileValue when Invalid Position Throws Illegal Argument Exception")
    @ParameterizedTest(name = "row = {0}, col = {1}")
    @CsvSource({
            "1,    -1",
            "-1,    1",
            "10000, 0",
            "0,     10000"
    })
    void setTileValue_whenInvalidPositionThrowsIllegalArgumentException(int row, int column) {
        //given
        var board = new Board();
        var value = 2;

        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> board.setTileValue(row, column, value));
    }

    @DisplayName("Board.getTileValue when Invalid Position Throws Illegal Argument Exception")
    @ParameterizedTest(name = "row = {0}, col = {1}")
    @CsvSource({
            "1,    -1",
            "-1,    1",
            "10000, 0",
            "0,     10000"
    })
    void getTileValue_whenInvalidPositionThrowsIllegalArgumentException(int row, int column) {
        //given
        var board = new Board();

        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> board.getTileValue(row, column));
    }

    @Test
    @DisplayName("Board.initialize sets the values at two random positions")
    void initializeSetsTwoTwosAtRandomPositions() {
        //given
        Random random = mock(Random.class);
        when(random.nextInt(Board.SIZE))
                .thenReturn(0, 0, 3, 3);
        when(random.nextDouble())
                .thenReturn(0.1, 0.99);

        Board board = new Board(random);

        //when
        board.initialize();

        //then
        assertAll(
                () -> assertEquals(2, board.getTileValue(0, 0)),
                () -> assertEquals(4, board.getTileValue(3, 3))
        );
    }

    @Test
    @DisplayName("Board.spawnRandomTiles sets the values at two random positions")
    void spawnRandomTilesSetsTwoTwosAtRandomPositions() {
        //given
        Random random = mock(Random.class);
        when(random.nextInt(Board.SIZE))
                .thenReturn(0, 0, 3, 3);
        when(random.nextDouble())
                .thenReturn(0.1, 0.99);

        Board board = new Board(random);

        //when
        board.spawnRandomTiles();

        //then
        assertAll(
                () -> assertEquals(2, board.getTileValue(0, 0)),
                () -> assertEquals(4, board.getTileValue(3, 3))
        );
    }

    @Test
    @DisplayName("Board.spawnRandomTiles does not override the value of a tile at a random position")
    void spawnRandomTiles_doesNotOverrideTheValueOfATileAtARandomPosition() {
        //given
        Random random = mock(Random.class);
        when(random.nextInt(Board.SIZE))
                .thenReturn(
                        0, 0,
                        3, 3,
                        0, 0,
                        1, 1,
                        2, 2
                );
        when(random.nextDouble())
                .thenReturn(0.99, 0.99, 0.2, 0.2);

        Board board = new Board(random);
        board.spawnRandomTiles();

        //when
        board.spawnRandomTiles();

        //then
        assertAll(
                () -> assertEquals(4, board.getTileValue(0, 0)),
                () -> assertEquals(4, board.getTileValue(3, 3)),
                () -> assertEquals(2, board.getTileValue(1, 1)),
                () -> assertEquals(2, board.getTileValue(2, 2))
        );
    }

    @Test
    @DisplayName("Board.isFull returns false when empty")
    void isFullReturnsFalseWhenEmpty() {
        //given
        var board = new Board();

        //when
        var result = board.isFull();
        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("Board.isFull returns true when not empty")
    void isFullReturnsTrueWhenNotEmpty() {
        //given
        var board = new Board();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                board.setTileValue(row, column, 2);
            }
        }

        //when
        var result = board.isFull();
        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("Board.isFull returns false when one tile zero")
    void isFullReturnsFalseWhenOneTileZero() {
        //given
        var board = new Board();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                board.setTileValue(row, column, 2);
            }
        }
        board.setTileValue(0, 0, 0);

        //when
        var result = board.isFull();
        //then
        assertFalse(result);
    }
}