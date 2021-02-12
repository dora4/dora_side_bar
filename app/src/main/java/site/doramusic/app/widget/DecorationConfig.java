package site.doramusic.app.widget;

public final class DecorationConfig {

    private int lineHeight;
    private int lineColor;

    private int selectedBgColorR;
    private int selectedBgColorG;
    private int selectedBgColorB;

    private int unSelectBgColorR;
    private int unSelectBgColorG;
    private int unSelectBgColorB;

    private int selectedTextColorR;
    private int selectedTextColorG;
    private int selectedTextColorB;

    private int unSelectTextColorR;
    private int unSelectTextColorG;
    private int unSelectTextColorB;

    private float textXOffset;
    private float textSize;

    private int height;

    public int getLineHeight() {
        return lineHeight;
    }

    public int getLineColor() {
        return lineColor;
    }

    public int getSelectedBgColorR() {
        return selectedBgColorR;
    }

    public int getSelectedBgColorG() {
        return selectedBgColorG;
    }

    public int getSelectedBgColorB() {
        return selectedBgColorB;
    }

    public int getUnSelectBgColorR() {
        return unSelectBgColorR;
    }

    public int getUnSelectBgColorG() {
        return unSelectBgColorG;
    }

    public int getUnSelectBgColorB() {
        return unSelectBgColorB;
    }

    public int getSelectedTextColorR() {
        return selectedTextColorR;
    }

    public int getSelectedTextColorG() {
        return selectedTextColorG;
    }

    public int getSelectedTextColorB() {
        return selectedTextColorB;
    }

    public int getUnSelectTextColorR() {
        return unSelectTextColorR;
    }

    public int getUnSelectTextColorG() {
        return unSelectTextColorG;
    }

    public int getUnSelectTextColorB() {
        return unSelectTextColorB;
    }

    public float getTextXOffset() {
        return textXOffset;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getHeight() {
        return height;
    }

    public static class Builder {

        private DecorationConfig config;

        public Builder() {
            config = new DecorationConfig();
        }

        public Builder setLine(int height, int color) {
            config.lineHeight = height;
            config.lineColor = color;
            return this;
        }

        public Builder setSelectedBgColor(int r, int g, int b) {
            config.selectedBgColorR = r;
            config.selectedBgColorG = g;
            config.selectedBgColorB = b;
            return this;
        }

        public Builder setUnSelectBgColor(int r, int g, int b) {
            config.unSelectBgColorR = r;
            config.unSelectBgColorG = g;
            config.unSelectBgColorB = b;
            return this;
        }

        public Builder setSelectedTextColor(int r, int g, int b) {
            config.selectedTextColorR = r;
            config.selectedTextColorG = g;
            config.selectedTextColorB = b;
            return this;
        }

        public Builder setUnSelectTextColor(int r, int g, int b) {
            config.unSelectTextColorR = r;
            config.unSelectTextColorG = g;
            config.unSelectTextColorB = b;
            return this;
        }

        public Builder setTextXOffset(float textXOffset) {
            config.textXOffset = textXOffset;
            return this;
        }

        public Builder setTextSize(float textSize) {
            config.textSize = textSize;
            return this;
        }

        public Builder setHeight(int height) {
            config.height = height;
            return this;
        }

        public DecorationConfig build() {
            return config;
        }
    }
}