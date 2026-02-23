package com.noawatel.terminal.buffer;

public class TerminalCell {
    private char character;
    private TextAttributes attributes;
    private boolean userTyped;

    public TerminalCell() {
        this(' ', new TextAttributes(), false);
    }

    public TerminalCell(char character, TextAttributes attributes, boolean userTyped) {
        this.character = character;
        this.attributes = attributes;
        this.userTyped = userTyped;
    }

    public char getCharacter() { return character; }
    public void setCharacter(char c, boolean userTyped) {
        this.character = c;
        this.userTyped = userTyped;
    }

    public TextAttributes getAttributes() { return attributes; }
    public boolean isUserTyped() { return userTyped; }
    public void setAttributes(TextAttributes attr) { this.attributes = attr; }

    public void copyFrom(TerminalCell other) {
        this.character = other.character;
        this.attributes = other.attributes;
        this.userTyped = other.userTyped;
    }
}