package test;

public class Tile {
	
	public final int score;
	public final char letter;

	private Tile(char l, int s) {
		score = s;
		letter = l;
	}
	
	public boolean equals(Tile t) {
		if(this.letter==t.letter) {
			return true;
		}
		return false;	
	}
	
	public int hashCode() {
		return this.letter;
	}
	
	public static class Bag{
		private int letterAmounts[] = new int[26];
		private Tile tiles[] = new Tile[26];
		private int maxes[] = new int[26];
		private int sum = 98;
		
		public Bag() {
			letterAmounts[0] = 9;
			letterAmounts[1] = 2;
			letterAmounts[2] = 2;
			letterAmounts[3] = 4;
			letterAmounts[4] = 12;
			letterAmounts[5] = 2;
			letterAmounts[6] = 3;
			letterAmounts[7] = 2;
			letterAmounts[8] = 9;
			letterAmounts[9] = 1;
			letterAmounts[10] = 1;
			letterAmounts[11] = 4;
			letterAmounts[12] = 2;
			letterAmounts[13] = 6;
			letterAmounts[14] = 8;
			letterAmounts[15] = 2;
			letterAmounts[16] = 1;
			letterAmounts[17] = 6;
			letterAmounts[18] = 4;
			letterAmounts[19] = 6;
			letterAmounts[20] = 4;
			letterAmounts[21] = 2;
			letterAmounts[22] = 2;
			letterAmounts[23] = 1;
			letterAmounts[24] = 2;
			letterAmounts[25] = 1;
			
			maxes[0] = 9;
			maxes[1] = 2;
			maxes[2] = 2;
			maxes[3] = 4;
			maxes[4] = 12;
			maxes[5] = 2;
			maxes[6] = 3;
			maxes[7] = 2;
			maxes[8] = 9;
			maxes[9] = 1;
			maxes[10] = 1;
			maxes[11] = 4;
			maxes[12] = 2;
			maxes[13] = 6;
			maxes[14] = 8;
			maxes[15] = 2;
			maxes[16] = 1;
			maxes[17] = 6;
			maxes[18] = 4;
			maxes[19] = 6;
			maxes[20] = 4;
			maxes[21] = 2;
			maxes[22] = 2;
			maxes[23] = 1;
			maxes[24] = 2;
			maxes[25] = 1;
			
			tiles[0] = new Tile('A', 1);
			tiles[1] = new Tile('B', 3);
			tiles[2] = new Tile('C', 3);
			tiles[3] = new Tile('D', 2);
			tiles[4] = new Tile('E', 1);
			tiles[5] = new Tile('F', 4);
			tiles[6] = new Tile('G', 2);
			tiles[7] = new Tile('H', 4);
			tiles[8] = new Tile('I', 1);
			tiles[9] = new Tile('J', 8);
			tiles[10] = new Tile('K', 5);
			tiles[11] = new Tile('L', 1);
			tiles[12] = new Tile('M', 3);
			tiles[13] = new Tile('N', 1);
			tiles[14] = new Tile('O', 1);
			tiles[15] = new Tile('P', 3);
			tiles[16] = new Tile('Q', 10);
			tiles[17] = new Tile('R', 1);
			tiles[18] = new Tile('S', 1);
			tiles[19] = new Tile('T', 1);
			tiles[20] = new Tile('U', 1);
			tiles[21] = new Tile('V', 4);
			tiles[22] = new Tile('W', 4);
			tiles[23] = new Tile('X', 8);
			tiles[24] = new Tile('Y', 4);
			tiles[25] = new Tile('Z', 10);
			
			
		}
		
		public Tile getRand() {
			if(sum==0) {
				return null;
			}
			int randN = (int) (Math.random()*26);
			if(letterAmounts[randN]>0) {
				letterAmounts[randN]--;
				sum--;
				return tiles[randN];
			}
			return getRand();
			
		}
		
		public Tile getTile(char c) {
			for(int i = 0; i<tiles.length; i++) {
				if(tiles[i].letter==c && letterAmounts[i]>0) {
					letterAmounts[i]--;
					sum--;
					return tiles[i];
				}
			}
			//System.out.println("Missing tile");
			return null;
		}
		
		public void put(Tile t) {
			if(letterAmounts[t.letter-65]<maxes[t.letter-65]) {
				sum++;
				letterAmounts[t.letter-65]++;
			}
		}
		
		public int size() {
			return sum;
		}
		
		public int[] getQuantities() {
			int arr[] = new int[26];
			for(int i = 0; i<arr.length; i++) {
				arr[i] = letterAmounts[i];
			}
			return arr;
		}

		public static Bag getBag() {
			Bag b = new Bag();
			return b;
		}
	}
	
}
