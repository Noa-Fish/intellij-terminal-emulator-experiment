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

    public TerminalCell getCellAt(int index) {
        if (index < 0 || index >= cells.length) throw new IndexOutOfBoundsException();
        return cells[index];
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

    public void fillLine(char ch, TextAttributes attributes, boolean userTyped) {
        for (TerminalCell cell : cells) {
            cell.setCharacter(ch, userTyped);
            cell.setAttributes(attributes);
        }
    }

    public String getUserContent() {
        int lastUserCharIndex = -1;
        for (int i = 0; i < getWidth(); i++) {
            if (cells[i].isUserTyped()) {
                lastUserCharIndex = i;
            }
        }
        if (lastUserCharIndex == -1) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= lastUserCharIndex; i++) {
            sb.append(cells[i].getCharacter());
        }
        return sb.toString();
    }
}