package othelloGame.basicStructure;
import java.io.*;
import java.util.ArrayList;
import java.util.List;



//This class describes the condition of the board and its corresponding operations:
public class Board {
	private int [][] Discs = new int[4][4];
	public static final int Black = 1;
	public static final int White = 2;
	private static final int None = -1;
	
	// what is the color of this position 
	public int getColorFromPoint(int x, int y){
		return Discs[x-1][y-1];
	}
	
	// construct function
	public Board(){
		for (int i = 0; i < 4; i ++){
			for (int j = 0; j < 4; j ++){
				this.Discs[i][j] = this.None;
			}
		}
		Discs[1][1] = this.White;
		Discs[2][2] = this.White;
		Discs[1][2] = this.Black;
		Discs[2][1] = this.Black;
	}
	
	// construct from another Board class, deep replication
	public Board(Board otherBoard){
		for (int i = 0; i < 4; i ++){
			for (int j = 0; j < 4; j ++){
				this.Discs[i][j] = otherBoard.Discs[i][j];
			}
		}
	}
	
	//whether this disc is out of range
	private boolean DiscExist(int xAxis, int yAxis){
		if ( (xAxis < 0) || (xAxis > 3) || (yAxis < 0) || (yAxis > 3) ){
			return false;
		}else{
			return true;
		}
	}
	
	// whether the disc put here would result in the outflanking of the other color discs
	public boolean checkPutDisc(int xAxis, int yAxis, int color){
		int xMatrixAxis = xAxis - 1;
		int yMatrixAxis = yAxis - 1;
		int competitorColor = -1;
		if ( color == this.White){
			competitorColor = this.Black;
		}else{
			competitorColor = this.White;
		}
		if ( DiscExist(xMatrixAxis, yMatrixAxis) ){
			if ( Discs[xMatrixAxis][yMatrixAxis] != None){
				return false;
			}else{
				// check every possible direction
				int[][] directs = {{-1, 0}, {-1, -1}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
				for(int[] direct: directs){
					int xTestAxis = xMatrixAxis;
					int yTestAxis = yMatrixAxis;
					xTestAxis = xTestAxis + direct[0];
					yTestAxis = yTestAxis + direct[1];
					int haveCompetitorColor = 0;
					while( DiscExist(xTestAxis, yTestAxis) && (Discs[xTestAxis][yTestAxis] == competitorColor) ){
						xTestAxis = xTestAxis + direct[0];
						yTestAxis = yTestAxis + direct[1];
						haveCompetitorColor = 1;
					}
					if ( (haveCompetitorColor == 1) && DiscExist(xTestAxis, yTestAxis) && (Discs[xTestAxis][yTestAxis] == color) ){
						return true;
					}
				}
				return false;
			}
			
		}else{
			return false;
		}
	}
	
	//whether the user using this color disc has valid move
	public boolean checkPutDiscForColor(int color){
		for (int i = 1; i <= 4; i++){
			for (int j = 1; j <= 4; j++){
				if ( checkPutDisc(i, j, color) ){
					return true;
				}
			}
		}
		return false;
	}
	
	//put disc in that position of the board
	public void PutDisc(int xAxis, int yAxis, int color){
		int xMatrixAxis = xAxis - 1;
		int yMatrixAxis = yAxis - 1;
		int competitorColor = -1;
		if ( color == this.White){
			competitorColor = this.Black;
		}else{
			competitorColor = this.White;
		}
		int[][] directs = {{-1, 0}, {-1, -1}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
		for(int[] direct: directs){
			int haveCompetitorColor = 0;
			int xTestAxis = xMatrixAxis, yTestAxis = yMatrixAxis, xMin = xMatrixAxis, xMax = xMatrixAxis, yMin = yMatrixAxis, yMax = yMatrixAxis;
			xTestAxis = xTestAxis + direct[0];
			yTestAxis = yTestAxis + direct[1];
			while( DiscExist(xTestAxis, yTestAxis) && (Discs[xTestAxis][yTestAxis] == competitorColor) ){
				xTestAxis = xTestAxis + direct[0];
				yTestAxis = yTestAxis + direct[1];
				xMin = Math.min(xMin, xTestAxis);
				xMax = Math.max(xMax, xTestAxis);
				yMin = Math.min(yMin, yTestAxis);
				yMax = Math.max(yMax, yTestAxis);
				haveCompetitorColor = 1;
			}
			if ( (haveCompetitorColor == 1) && DiscExist(xTestAxis, yTestAxis) && (Discs[xTestAxis][yTestAxis] == color) ){
				for(int i = xMatrixAxis, j = yMatrixAxis; i <= xMax && i >= xMin && j <= yMax && j >= yMin;){
					Discs[i][j] = color;
					i += direct[0];
					j += direct[1];
				}
			}
		}
	}
	
	//get the number of disc from the color
	private int getNumberFromColor(int color){
		int number = 0;
		for (int i = 0; i < 4; i ++){
			for (int j = 0; j < 4; j ++){
				if(this.Discs[i][j] == color){
					number++;
				}
			}
		}
		return number;
	}
	
	// all possible actions for the user using this color disc
	public List<DiscAction> actions(int color){
		List<DiscAction> actions = new ArrayList<DiscAction>();
		for (int i = 1; i <= 4; i++){
			for (int j = 1; j <= 4; j++){
				if ( checkPutDisc(i, j, color) ){
					DiscAction action = new DiscAction(i, j, color);
					actions.add(action);
				}
			}
		}
		return actions;
	}
	
	//whether the game ends
	public static boolean terminalTest(Board board){
		if ((!board.checkPutDiscForColor(Board.Black)) && (!board.checkPutDiscForColor(Board.White))){
			return true;
		}
		return false;
	}
	
	// the utility of the board 
	public static int utility(Board board){
		return board.getNumberFromColor(Board.White) - board.getNumberFromColor(Board.Black);
	}
	
	// the heuristic utility of the board 
	public static int heuristicUtility(Board board){
		return heuristicUtilityForColor(board, Board.White) - heuristicUtilityForColor(board, Board.Black);
	}
	
	//the heuristic utility for the user using this color disc 
	private static int heuristicUtilityForColor(Board board, int color){
		int utility = 0;
		for (int i = 0; i < 4; i ++){
			for (int j = 0; j < 4; j ++){
				if ( ( (i==0)&&((j==0) || (j==3)) ) || ( (i==0)&&((j==0) || (j==3)) )){
					if (board.Discs[i][j] == color){
						utility += 16;
					}
				}else if ( (i==0) || (i==3) || (j==0) || (j==3)){
					if (board.Discs[i][j] == color){
						utility += 4;
					}
				}else{
					if (board.Discs[i][j] == color){
						utility += 1;
					}
				}
			}
		}
		return utility;
	}
	
}
