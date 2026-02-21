package com.noawatel.terminal.buffer;

import com.noawatel.terminal.color.TerminalColor;

public final class TextAttributes {

    private final TerminalColor foreground;
    private final TerminalColor background;
    private final boolean bold;
    private final boolean italic;
    private final boolean underline;

    public TextAttributes() {
        this(TerminalColor.DEFAULT, TerminalColor.DEFAULT, false, false, false);
    }

    public TextAttributes(TerminalColor foreground, TerminalColor background, boolean bold, boolean italic, boolean underline) {
        this.foreground = foreground;
        this.background = background;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    public TerminalColor getForeground() { return foreground; }
    public TerminalColor getBackground() { return background; }
    public boolean isBold() { return bold; }
    public boolean isItalic() { return italic; }
    public boolean isUnderline() { return underline; }

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