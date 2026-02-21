package com.noawatel.terminal.buffer;

import com.noawatel.terminal.color.TerminalColor;

public record TextAttributes(TerminalColor foreground, TerminalColor background, boolean bold, boolean italic, boolean underline) {

    public TextAttributes() {
        this(TerminalColor.DEFAULT, TerminalColor.DEFAULT, false, false, false);
    }

    public TextAttributes withForeground(TerminalColor color) {
        return new TextAttributes(color, background, bold, italic, underline);
    }

    public TextAttributes withBackground(TerminalColor color) {
        return new TextAttributes(foreground, color, bold, italic, underline);
    }

    public TextAttributes withBold(boolean bold) {
        return new TextAttributes(foreground, background, bold, italic, underline);
    }

    public TextAttributes withItalic(boolean italic) {
        return new TextAttributes(foreground, background, bold, italic, underline);
    }

    public TextAttributes withUnderline(boolean underline) {
        return new TextAttributes(foreground, background, bold, italic, underline);
    }
}