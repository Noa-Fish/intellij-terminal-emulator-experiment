package com.noawatel.terminal.buffer;

import java.util.LinkedList;
import java.util.List;

public class TerminalBuffer {

    private int width;
    private int height;
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

    public List<TerminalLine> getScreenLines() {
        return List.copyOf(screen);
    }

    public List<TerminalLine> getScrollbackLines() {
        return List.copyOf(scrollback);
    }

    public TerminalLine getScreenLine(int row) {
        if (row < 0 || row >= screen.size()) throw new IndexOutOfBoundsException();
        return screen.get(row);
    }

    public void setCursor(int row, int col) {
        cursorRow = Math.max(0, Math.min(row, height - 1));
        cursorCol = Math.max(0, Math.min(col, width - 1));
    }

    public void setCurrentAttributes(TextAttributes attributes) {
        this.currentAttributes = attributes;
    }

    public void writeChar(char c) {
        if (cursorRow >= height) {
            scrollUp();
            cursorRow = height - 1;
        }
        TerminalLine line = screen.get(cursorRow);
        line.getCell(cursorCol).setCharacter(c, true);
        line.getCell(cursorCol).setAttributes(currentAttributes);

        cursorCol++;
        if (cursorCol == width) {
            cursorCol = 0;
            cursorRow++;
        }
    }

    public void insertText(String text) {
        for (char c : text.toCharArray()) {
            insertCharShift(c);
        }
    }

    private void insertCharShift(char c) {
        int r = cursorRow;
        int col = cursorCol;

        char carry = c;
        boolean cursorMoved = false;

        while (carry != 0) {
            TerminalLine line = screen.get(r);

            char nextCarry = line.getCell(col).getCharacter();
            TextAttributes nextAttr = line.getCell(col).getAttributes();

            line.getCell(col).setCharacter(carry, true);
            line.getCell(col).setAttributes(currentAttributes);

            if (!cursorMoved) {
                cursorCol++;
                cursorRow = r;
                cursorMoved = true;
            }

            carry = nextCarry;
            currentAttributes = nextAttr;

            col++;
            if (col >= width) {
                col = 0;
                r++;
            }

            if (r >= height) {
                scrollUp();
                r = height - 1;
            }

            if (carry == ' ') {
                carry = 0;
            }
        }
    }

    private TerminalLine getLineByGlobalRow(int globalRow) {
        int totalLines = scrollback.size() + screen.size();

        if (globalRow < 0 || globalRow >= totalLines) {
            throw new IndexOutOfBoundsException();
        }

        if (globalRow < scrollback.size()) {
            return scrollback.get(globalRow);
        }

        return screen.get(globalRow - scrollback.size());
    }

    public char getCharAt(int globalRow, int col) {
        TerminalLine line = getLineByGlobalRow(globalRow);
        return line.getCell(col).getCharacter();
    }

    public TextAttributes getAttributesAt(int globalRow, int col) {
        TerminalLine line = getLineByGlobalRow(globalRow);
        return line.getCell(col).getAttributes();
    }

    public String getLineAsString(int globalRow) {
        return getLineByGlobalRow(globalRow).getUserContent();
    }

    public String getEntireBufferContent() {
        StringBuilder sb = new StringBuilder();

        for (TerminalLine line : scrollback) {
            sb.append(line.getUserContent()).append("\n");
        }

        for (TerminalLine line : screen) {
            sb.append(line.getUserContent()).append("\n");
        }

        return sb.toString();
    }

    private void scrollUp() {
        TerminalLine removed = screen.removeFirst();
        if (scrollback.size() >= maxScrollback) {
            scrollback.removeFirst();
        }
        scrollback.add(removed);
        screen.add(new TerminalLine(width));
    }

    public void writeText(String text) {
        for (char c : text.toCharArray()) {
            writeChar(c);
        }
    }

    public void moveCursorUp(int n) {
        cursorRow = Math.max(0, cursorRow - n);
    }

    public void moveCursorDown(int n) {
        cursorRow = Math.min(height - 1, cursorRow + n);
    }

    public void moveCursorLeft(int n) {
        cursorCol = Math.max(0, cursorCol - n);
    }

    public void moveCursorRight(int n) {
        cursorCol = Math.min(width - 1, cursorCol + n);
    }

    public void fillLine(char ch) {
        screen.get(cursorRow).fillLine(ch, currentAttributes, true);
        cursorCol = width;
    }

    public void clearScreen() {
        for (TerminalLine line : screen) {
            line.fillLine(' ', new TextAttributes(), false);
        }
        setCursor(0, 0);
    }

    public void clearScreenAndScrollback() {
        scrollback.clear();
        clearScreen();
    }

    public void insertEmptyLineAtBottom() {
        if (screen.size() >= height) {
            scrollUp();
        }
        screen.set(screen.size() - 1, new TerminalLine(width));
    }

    public void resize(int newWidth, int newHeight) {
        if (newWidth <= 0 || newHeight <= 0)
            throw new IllegalArgumentException("Invalid dimensions");

        List<String> linesContent = new LinkedList<>();
        for (TerminalLine line : screen) {
            linesContent.add(line.getUserContent());
        }

        this.width = newWidth;
        this.height = newHeight;
        screen.clear();
        for (int i = 0; i < newHeight; i++)
            screen.add(new TerminalLine(newWidth));

        cursorRow = 0;
        cursorCol = 0;

        for (String lineStr : linesContent) {
            for (char c : lineStr.toCharArray()) {
                writeChar(c);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int lastLineWithContent = -1;
        for (int i = 0; i < screen.size(); i++) {
            if (!screen.get(i).getUserContent().isEmpty()) {
                lastLineWithContent = i;
            }
        }
        for (int i = 0; i <= lastLineWithContent; i++) {
            sb.append(screen.get(i).getUserContent()).append("\n");
        }
        return sb.toString();
    }
}