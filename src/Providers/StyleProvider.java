package Providers;

import javafx.scene.paint.Color;

public class StyleProvider {
    private static StyleProvider instance;
    private Color highlightedColor;
    private int higlightedLineWidth;

    private StyleProvider() {}

    public static synchronized StyleProvider getInstance() {
        if (instance==null)
            instance = new StyleProvider();
        return instance;
    }

    public Color getHighlightedColor() {
        return highlightedColor;
    }

    public void setHighlightedColor(Color highlightedColor) {
        this.highlightedColor = highlightedColor;
    }

    public int getHiglightedLineWidth() {
        return higlightedLineWidth;
    }

    public void setHiglightedLineWidth(int higlightedLineWidth) {
        this.higlightedLineWidth = higlightedLineWidth;
    }
}
