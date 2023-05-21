package test;

import java.util.Arrays;

public class Word {

	private Tile tiles[];
	private int row;
	private int col;
	private boolean vertical;
	
	public Word(Tile t[], int r, int c, boolean v) {
		tiles=Arrays.copyOf(t, t.length);
		row=r;
		col=c;
		vertical=v;
		
	}
	
	public Tile[] getTiles() {
		return Arrays.copyOf(this.tiles, this.tiles.length);
	}
	public int getRow() {
		return this.row;
	}
	public int getCol() {
		return this.col;
	}
	public boolean getVertical() {
		return this.vertical;
	}
	public boolean equals(Word w) {
		if(this.tiles.length!=w.getTiles().length) {
			return false;
		}
		for(int i = 0; i<w.getTiles().length; i++) {
			if (!this.tiles[i].equals(w.getTiles()[i])){
				return false;
			}
		}
		return true;
	}

	public void setTile(int i, Tile tile) {
		tiles[i] = tile;
		
	}

}
