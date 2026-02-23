package com.noawatel.terminal.buffer;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerminalBufferTest {

    @Test
    void testWriteCharAndScreen() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);
        buffer.writeText("ABCDE");
        assertEquals("ABCDE", buffer.getScreenLine(0).getUserContent());
    }

    @Test
    void testWriteMultipleLinesAndScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 3);
        buffer.writeText("ABCDEFGH");

        assertEquals("ABCDE", buffer.getScreenLine(0).getUserContent());
        assertEquals("FGH", buffer.getScreenLine(1).getUserContent());

        assertEquals(1, buffer.getCursorRow());
        assertEquals(3, buffer.getCursorCol());

        assertEquals(0, buffer.getScrollbackLines().size());
    }

    @Test
    void testCursorMovementAndClamping() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);
        buffer.setCursor(1, 2);

        buffer.moveCursorUp(1);
        assertEquals(0, buffer.getCursorRow());

        buffer.moveCursorDown(2);
        assertEquals(2, buffer.getCursorRow());

        buffer.moveCursorLeft(1);
        assertEquals(1, buffer.getCursorCol());

        buffer.moveCursorRight(3);
        assertEquals(4, buffer.getCursorCol());

        buffer.moveCursorDown(10);
        buffer.moveCursorRight(10);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(4, buffer.getCursorCol());
    }

    @Test
    void testInsertTextAndShift() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 10);
        buffer.writeText("HELLO");
        buffer.setCursor(0, 2);
        buffer.insertText("ZZ");

        assertEquals("HEZZL", buffer.getScreenLine(0).getUserContent());
        assertEquals("LO", buffer.getScreenLine(1).getUserContent());

        assertTrue(buffer.getScreenLine(0).getCellAt(2).isUserTyped());
        assertTrue(buffer.getScreenLine(0).getCellAt(3).isUserTyped());
    }

    @Test
    void testFillLine() {
        TerminalBuffer buffer = new TerminalBuffer(3, 2, 5);
        buffer.fillLine('X');

        TerminalLine line = buffer.getScreenLine(buffer.getCursorRow());
        for (int i = 0; i < line.getWidth(); i++) {
            assertEquals('X', line.getCellAt(i).getCharacter());
            assertTrue(line.getCellAt(i).isUserTyped());
        }
    }

    @Test
    void testInsertEmptyLineAtBottom() {
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2);
        buffer.writeText("ABCD");
        buffer.insertEmptyLineAtBottom();

        TerminalLine bottom = buffer.getScreenLine(1);
        for (int i = 0; i < bottom.getWidth(); i++) {
            assertEquals(' ', bottom.getCellAt(i).getCharacter());
            assertFalse(bottom.getCellAt(i).isUserTyped());
        }
    }

    @Test
    void testClearScreenAndScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(3, 2, 5);
        buffer.writeText("ABC");
        buffer.writeText("DEF");
        buffer.clearScreen();

        for (TerminalLine line : buffer.getScreenLines()) {
            for (int i = 0; i < line.getWidth(); i++) {
                assertEquals(' ', line.getCellAt(i).getCharacter());
                assertFalse(line.getCellAt(i).isUserTyped());
            }
        }

        buffer.writeText("AAA");
        buffer.writeText("BBB");
        buffer.clearScreenAndScrollback();

        assertEquals(0, buffer.getScrollbackLines().size());
        for (TerminalLine line : buffer.getScreenLines()) {
            for (int i = 0; i < line.getWidth(); i++) {
                assertEquals(' ', line.getCellAt(i).getCharacter());
            }
        }
    }

    @Test
    void testResizePreservesContentAndCursor() {
        TerminalBuffer buffer = new TerminalBuffer(5, 5, 10);
        buffer.writeText("ABCDE12345");

        buffer.resize(3, 3);
        List<TerminalLine> screen = buffer.getScreenLines();

        assertEquals("DE1", screen.get(0).getUserContent());
        assertEquals("234", screen.get(1).getUserContent());
        assertEquals("5", screen.get(2).getUserContent());

        assertTrue(buffer.getCursorRow() < 3);
        assertTrue(buffer.getCursorCol() < 3);
    }

    @Test
    void testGlobalAccessMethods() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 5);
        buffer.writeText("ABCDE");
        buffer.writeText("FGHIJ");
        buffer.writeText("KLMNO");

        assertEquals("ABCDE", buffer.getLineAsString(0));
        assertEquals('B', buffer.getCharAt(0, 1));
        assertEquals("FGHIJ", buffer.getLineAsString(1));
        assertEquals("KLMNO", buffer.getLineAsString(2));
        assertEquals(3, buffer.getScreenLines().size());
    }

    @Test
    void testScrollbackBehavior() {
        TerminalBuffer buffer = new TerminalBuffer(3, 2, 2);
        buffer.writeText("111222333444");

        assertEquals(2, buffer.getScrollbackLines().size());
        assertEquals("111", buffer.getScrollbackLines().get(0).getUserContent());
    }
}