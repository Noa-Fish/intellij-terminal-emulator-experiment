package com.noawatel.terminal.buffer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalBufferTest {

    @Test
    void testWriteCharAndScreen() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);
        buffer.writeString("ABCDE");
        String[] lines = buffer.getScreenAsString().split("\n");
        assertEquals("ABCDE", lines[0].trim());
    }

    @Test
    void testScroll() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 3);
        buffer.writeString("ABCDEFGH");
        assertEquals(1, buffer.getCursorRow());
        assertEquals(3, buffer.getCursorCol());
    }

    @Test
    void testCursorMovement() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);
        buffer.setCursor(1, 2);
        buffer.moveCursorUp(1);
        assertEquals(0, buffer.getCursorRow());
        buffer.moveCursorDown(2);
        assertEquals(2, buffer.getCursorRow());
        buffer.moveCursorLeft(1);
        assertEquals(1, buffer.getCursorCol());
        buffer.moveCursorRight(3);
        assertEquals(4, buffer.getCursorCol()); // cannot exceed width-1
    }

    @Test
    void testFillAndClear() {
        TerminalBuffer buffer = new TerminalBuffer(3, 2, 5);
        buffer.fillLine('X');

        TerminalLine line = buffer.getScreenLine(buffer.getCursorRow());
        for (int i = 0; i < line.getWidth(); i++) {
            assertEquals('X', line.getCellAt(i).getCharacter());
        }

        buffer.clearScreen();
        for (TerminalLine l : buffer.getScreenLines()) {
            for (int i = 0; i < l.getWidth(); i++) {
                assertEquals(' ', l.getCellAt(i).getCharacter());
            }
        }

        buffer.clearScreenAndScrollback();
        assertEquals(0, buffer.getScrollbackLines().size());
    }

    @Test
    void testInsertEmptyLine() {
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2);
        buffer.writeString("ABCD"); // fill screen
        buffer.insertEmptyLineAtBottom();
        String[] lines = buffer.getScreenAsString().split("\n");
        assertEquals("  ", lines[1]); // bottom line is empty
    }
}