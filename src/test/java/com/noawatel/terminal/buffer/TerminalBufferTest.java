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
    void testScroll() {
        TerminalBuffer buffer = new TerminalBuffer(5, 2, 3);
        buffer.writeText("ABCDEFGH");

        assertEquals(1, buffer.getCursorRow());
        assertEquals(3, buffer.getCursorCol());

        assertEquals("ABCDE", buffer.getScreenLine(0).getUserContent());
        assertEquals("FGH", buffer.getScreenLine(1).getUserContent());
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
        assertEquals(4, buffer.getCursorCol());
    }

    @Test
    void testFillAndClear() {
        TerminalBuffer buffer = new TerminalBuffer(3, 2, 5);
        buffer.fillLine('X');

        TerminalLine line = buffer.getScreenLine(buffer.getCursorRow());
        for (int i = 0; i < line.getWidth(); i++) {
            assertEquals('X', line.getCellAt(i).getCharacter());
            assertTrue(line.getCellAt(i).isUserTyped());
        }

        buffer.clearScreen();
        for (TerminalLine l : buffer.getScreenLines()) {
            for (int i = 0; i < l.getWidth(); i++) {
                assertEquals(' ', l.getCellAt(i).getCharacter());
                assertFalse(l.getCellAt(i).isUserTyped());
            }
        }

        buffer.clearScreenAndScrollback();
        assertEquals(0, buffer.getScrollbackLines().size());
    }

    @Test
    void testInsertEmptyLine() {
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
    void testResizeKeepsContent() {
        TerminalBuffer buffer = new TerminalBuffer(5, 5, 5);
        buffer.writeText("ABCDE12345");

        buffer.resize(3, 3);

        List<TerminalLine> screen = buffer.getScreenLines();

        assertEquals("DE1", screen.get(0).getUserContent());
        assertEquals("234", screen.get(1).getUserContent());
        assertEquals("5", screen.get(2).getUserContent());
    }

    @Test
    void testCursorPositionAfterResize() {
        TerminalBuffer buffer = new TerminalBuffer(5, 5, 10);
        buffer.writeText("A  B ");

        buffer.resize(3, 2);
        assertEquals(1, buffer.getCursorRow());
        assertEquals(2, buffer.getCursorCol());

        buffer.resize(1, 3);
        assertEquals(3, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());
    }
}