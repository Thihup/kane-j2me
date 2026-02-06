package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class TiledLayer extends Layer {
    private int[][] cellMatrix;
    private int tileWidth, tileHeight;
    private Image tileSet;
    private int columns, rows;

    public TiledLayer(int columns, int rows, Image image, int tileWidth, int tileHeight) {
        super(0, 0, columns * tileWidth, rows * tileHeight, true);
        this.columns = columns;
        this.rows = rows;
        this.tileSet = image;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.cellMatrix = new int[rows][columns];
    }

    public void setCell(int col, int row, int tileIndex) {
        cellMatrix[row][col] = tileIndex;
    }

    public int getCell(int col, int row) {
        return cellMatrix[row][col];
    }

    public int createAnimatedTile(int staticTileIndex) {
        return 0; // Stub
    }

    public void setAnimatedTile(int animatedTileIndex, int staticTileIndex) {
        // Stub
    }

    public int getAnimatedTile(int animatedTileIndex) {
        return 0; // Stub
    }

    @Override
    public void paint(Graphics g) {
        if (isVisible()) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    int tileIndex = cellMatrix[r][c];
                    if (tileIndex > 0) {
                        // Very simplified tile drawing
                        g.drawRegion(tileSet, (tileIndex - 1) * tileWidth, 0, tileWidth, tileHeight, 
                                     Sprite.TRANS_NONE, getX() + c * tileWidth, getY() + r * tileHeight, 
                                     Graphics.TOP | Graphics.LEFT);
                    }
                }
            }
        }
    }
}
