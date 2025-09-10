package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    private static final char[] SYMBOLS_UNICODE = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
    private static final char[] SYMBOLS_ASCII = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        int normalizedColor = Math.max(0, Math.min(color, 255));

        int index = (int) ((normalizedColor / 255.0) * (SYMBOLS_ASCII.length - 1));

        return SYMBOLS_ASCII[SYMBOLS_ASCII.length - 1 - index];
    }
}
