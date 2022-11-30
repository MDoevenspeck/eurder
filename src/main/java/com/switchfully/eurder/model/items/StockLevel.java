package com.switchfully.eurder.model.items;

public enum StockLevel {
    STOCK_LOW(5), STOCK_MEDIUM(10), STOCK_HIGH(0);

    private final int level;

    StockLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
