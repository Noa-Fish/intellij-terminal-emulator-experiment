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
}