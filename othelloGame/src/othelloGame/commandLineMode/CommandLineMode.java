package othelloGame.commandLineMode;
import othelloGame.basicStructure.*;
import java.io.*;

//realize the command line mode of the game
public class CommandLineMode {
	
	// main function, realize the function of command line mode here
	public static void main(String[] args){
		System.out.printf("Welcome to Othello Game!\n");
		System.out.printf("'B' means Black Disc (Human), 'W' means White Disc (Computer), 'O' means No Disc there.\n");
		Board board = new Board();
		commandPrintBoard(board);
		int PositionTurn = 0;
		System.out.printf("Do you want to go first? Y(yes) or N(no):\n");
		BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));
		try{
			String str = buf.readLine();
			if (str.equalsIgnoreCase("Y")){
				PositionTurn = 0;
			}else{
				PositionTurn = 1;
			}
			System.out.printf("Do you want to select diffculty level? (default: The hardest level) Y(yes) or N(no):\n");
			int differentLevel = 0;
			int difficultyLevel = AlphaBetaTree.LevelEasy;
			str = buf.readLine();
			if (str.equalsIgnoreCase("Y")){
				differentLevel = 1;
				System.out.printf("Select diffculty level: E(Easy), N(Normal) or H(Hard):\n");
				str = buf.readLine();
				if (str.equalsIgnoreCase("E")){
					difficultyLevel = AlphaBetaTree.LevelEasy;
				}else if (str.equalsIgnoreCase("N")){
					difficultyLevel = AlphaBetaTree.LevelNormal;
				}else{
					difficultyLevel = AlphaBetaTree.LevelHard;
				}
			}
			
			while ( !Board.terminalTest(board)){
				if ( PositionTurn == 0 ){
					System.out.printf("Now For Human:\n");
					if ( board.checkPutDiscForColor(Board.Black)){
						System.out.printf("Please specify xAxis of the Disc:\n");
						int xAxis = Integer.parseInt(buf.readLine());
						System.out.printf("Please specify yAxis of the Disc:\n");
						int yAxis = Integer.parseInt(buf.readLine());
						while ( (xAxis<1) || (xAxis>4) || (yAxis<1) || (yAxis>4) || !board.checkPutDisc(xAxis, yAxis, Board.Black)){
							System.out.printf("Input point wrong! Please input again!\n");
							System.out.printf("Please specify xAxis of the Disc:\n");
							xAxis = Integer.parseInt(buf.readLine());
							System.out.printf("Please specify yAxis of the Disc:\n");
							yAxis = Integer.parseInt(buf.readLine());
						}
						board.PutDisc(xAxis, yAxis, Board.Black);
						System.out.printf("Input succesfully!\n");
						commandPrintBoard(board);
					}else{
						System.out.printf("No move available for Human:\n");
					}
					PositionTurn = 1;
				}else{
					System.out.printf("Now For Computer:\n");
					if ( board.checkPutDiscForColor(Board.White)){
						AlphaBetaTree alphaBetaTree = null;
						if ( differentLevel == 0 ){
							alphaBetaTree = new AlphaBetaTree(board);
						}else{
							alphaBetaTree = new AlphaBetaTree(board, difficultyLevel);
						}
						alphaBetaTree.printAlphaBetaTreeInfo();
						DiscAction discAction = alphaBetaTree.getNextAction();
						board.PutDisc(discAction.getxAxis(), discAction.getyAxis(), Board.White);
						commandPrintBoard(board);
					}else{
						System.out.printf("No move available for Computer:\n");
					}
					PositionTurn = 0;
				}
			}
			System.out.printf("Game End!\n");
			if ( Board.utility(board) > 0){
				System.out.printf("You Lose!\n");
			}else if ( Board.utility(board) < 0){
				System.out.printf("You Win!\n");
			}else{
				System.out.printf("Equal!\n");
			}
		}catch( Exception ex){
			ex.printStackTrace();
		}
	}
	
	//get symbol from color, and use it to print the board
	public static String getCommandSymbolFromColor(int color){
		if (color == Board.Black){
			return "B";
		}
		else if (color == Board.White){
			return "W";
		}
		else{
			return "O";
		}
	}
	
	//print the current board condition
	public static void commandPrintBoard(Board board){
		System.out.printf("Begin to print current board situation .....\n");
		System.out.printf("  1 2 3 4\n");
		for (int i = 1; i <= 4; i++){
			System.out.printf(i + " " + getCommandSymbolFromColor(board.getColorFromPoint(i, 1)) + " " + getCommandSymbolFromColor(board.getColorFromPoint(i, 2)) + " " + getCommandSymbolFromColor(board.getColorFromPoint(i, 3)) + " " + getCommandSymbolFromColor(board.getColorFromPoint(i, 4)) + "\n");
		}
		System.out.printf("End ......\n");
	}
}
