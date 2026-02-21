package com.noawatel.terminal.buffer;

public class TerminalLine {

    private final TerminalCell[] cells;

    public TerminalLine(int width) {
        if (width <= 0) throw new IllegalArgumentException("Width must be positive");
        this.cells = new TerminalCell[width];
        for (int i = 0; i < width; i++) {
            cells[i] = new TerminalCell();
        }
    }

    public int getWidth() {
        return cells.length;
    }

    public TerminalCell getCell(int column) {
        if (column < 0 || column >= cells.length) {
            throw new IndexOutOfBoundsException("Column out of bounds: " + column);
        }
        return cells[column];
    }

    public void setCell(int column, TerminalCell cell) {
        if (column < 0 || column >= cells.length) {
            throw new IndexOutOfBoundsException("Column out of bounds: " + column);
        }
        cells[column] = cell;
    }

    public void fillLine(char ch, TextAttributes attributes) {
        for (TerminalCell cell : cells) {
            cell.setCharacter(ch);
            cell.setAttributes(attributes);
        }
    }

    public String getLineAsString() {
        StringBuilder sb = new StringBuilder(cells.length);
        for (TerminalCell cell : cells) {
            sb.append(cell.getCharacter());
        }
        return sb.toString();
    }
}