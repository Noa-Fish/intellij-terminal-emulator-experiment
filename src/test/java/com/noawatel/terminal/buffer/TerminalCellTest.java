package com.noawatel.terminal.buffer;

import com.noawatel.terminal.color.TerminalColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TerminalCellTest {

    @Test
    void testDefaultCell() {
        TerminalCell cell = new TerminalCell();
        assertEquals(' ', cell.getCharacter());
        assertEquals(TerminalColor.DEFAULT, cell.getAttributes().foreground());
        assertEquals(TerminalColor.DEFAULT, cell.getAttributes().background());
        assertFalse(cell.getAttributes().bold());
    }

    @Test
    void testCustomCell() {
        TextAttributes attr = new TextAttributes(TerminalColor.RED, TerminalColor.BLACK, true, false, true);
        TerminalCell cell = new TerminalCell('A', attr);
        assertEquals('A', cell.getCharacter());
        assertTrue(cell.getAttributes().bold());
        assertTrue(cell.getAttributes().underline());
    }
}