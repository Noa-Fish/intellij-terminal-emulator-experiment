package com.noawatel.terminal.buffer;

import java.util.LinkedList;
import java.util.List;

public class TerminalBuffer {

    private final int width;
    private final int height;
    private final int maxScrollback;

    private final List<TerminalLine> scrollback;
    private final List<TerminalLine> screen;

    private int cursorRow;
    private int cursorCol;

    private TextAttributes currentAttributes;

    public TerminalBuffer(int width, int height, int maxScrollback) {
        if (width <= 0 || height <= 0 || maxScrollback < 0) {
            throw new IllegalArgumentException("Invalid dimensions");
        }
        this.width = width;
        this.height = height;
        this.maxScrollback = maxScrollback;

        this.scrollback = new LinkedList<>();
        this.screen = new LinkedList<>();

        for (int i = 0; i < height; i++) {
            screen.add(new TerminalLine(width));
        }

        this.cursorRow = 0;
        this.cursorCol = 0;
        this.currentAttributes = new TextAttributes();
    }

    public int getCursorRow() { return cursorRow; }
    public int getCursorCol() { return cursorCol; }

    public void setCursor(int row, int col) {
        cursorRow = Math.max(0, Math.min(row, height - 1));
        cursorCol = Math.max(0, Math.min(col, width - 1));
    }

    public void setCurrentAttributes(TextAttributes attributes) {
        this.currentAttributes = attributes;
    }

    public void writeChar(char c) {
        TerminalLine line = screen.get(cursorRow);
        line.getCell(cursorCol).setCharacter(c);
        line.getCell(cursorCol).setAttributes(currentAttributes);

        cursorCol++;
        if (cursorCol >= width) {
            cursorCol = 0;
            cursorRow++;
            if (cursorRow >= height) {
                scrollUp();
                cursorRow = height - 1;
            }
        }
    }

    private void scrollUp() {
        TerminalLine removed = screen.removeFirst();
        if (scrollback.size() >= maxScrollback) {
            scrollback.removeFirst();
        }
        scrollback.add(removed);
        screen.add(new TerminalLine(width));
    }

    public String getScreenAsString() {
        StringBuilder sb = new StringBuilder();
        for (TerminalLine line : screen) {
            sb.append(line.getLineAsString()).append("\n");
        }
        return sb.toString();
    }

    // TODO: Once full buffer manipulation methods are implemented (insert, overwrite, fill, clear, etc.), remove this method.
    //  This method is temporary for testing multiple characters quickly.
    public void writeString(String s) {
        for (char c : s.toCharArray()) {
            writeChar(c);
        }
    }
}