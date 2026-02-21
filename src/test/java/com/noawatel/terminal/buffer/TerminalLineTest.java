package com.noawatel.terminal.buffer;

import com.noawatel.terminal.color.TerminalColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalLineTest {

    @Test
    void testLineInitialization() {
        TerminalLine line = new TerminalLine(5);
        assertEquals(5, line.getWidth());
        for (int i = 0; i < 5; i++) {
            assertEquals(' ', line.getCell(i).getCharacter());
            assertEquals(TerminalColor.DEFAULT, line.getCell(i).getAttributes().getForeground());
        }
    }

    @Test
    void testSetAndGetCell() {
        TerminalLine line = new TerminalLine(3);
        TextAttributes attr = new TextAttributes(TerminalColor.RED, TerminalColor.BLACK, true, false, false);
        TerminalCell cell = new TerminalCell('X', attr);
        line.setCell(1, cell);
        assertEquals('X', line.getCell(1).getCharacter());
        assertTrue(line.getCell(1).getAttributes().isBold());
    }

    @Test
    void testFillLine() {
        TerminalLine line = new TerminalLine(4);
        TextAttributes attr = new TextAttributes(TerminalColor.GREEN, TerminalColor.DEFAULT, false, true, false);
        line.fillLine('-', attr);

        for (int i = 0; i < 4; i++) {
            assertEquals('-', line.getCell(i).getCharacter());
            assertTrue(line.getCell(i).getAttributes().isItalic());
        }
        assertEquals("----", line.getLineAsString());
    }
}