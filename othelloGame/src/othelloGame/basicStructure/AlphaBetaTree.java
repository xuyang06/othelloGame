package othelloGame.basicStructure;

import java.util.ArrayList;
import java.util.List;

//This class realizes alpha-beta algorithm and cut-off search algorithm
public class AlphaBetaTree {
	private int maximumDepth = 0;
	private int timesPruningMax = 0;
	private int timesPruningMin = 0;
	private int maximumValue = AlphaBetaNode.Minimum;
	private int nodesNumber = 0;
	private Board currentBoard;
	private List<DiscAction> nextActions = new ArrayList<DiscAction>();
	private List<Integer> valueCorrespondingToAction = new ArrayList<Integer>(); 
	private int differentLevel = 0;
	private int difficultyLevel = LevelEasy;
	public static final int LevelEasy = 1;
	public static final int LevelNormal = 2;
	public static final int LevelHard = 3;
	private static final int depthEasy = 4;
	private static final int depthNormal = 9;
	private static final int depthHard = 14;
	
	//get the maximum depth of this tree
	public int getMaximumDepth(){
		return this.maximumDepth;
	}
	
	// get number of times pruning occurred within the MAX-VALUE function
	public int getTimesPruningMax(){
		return this.timesPruningMax;
	}
	
	//get number of times pruning occurred within the MIN-VALUE function
	public int getTimesPruningMin(){
		return this.timesPruningMin;
	}
	
	//get maximum node value
	public int getMaximunValue(){
		return this.maximumValue;
	}
	
	//get total number of nodes generated
	public int getNodesNumber(){
		return this.nodesNumber;
	}
	
	//print alpha-beta tree information
	public void printAlphaBetaTreeInfo(){
		System.out.printf("For Alpha-Beta-Tree Info:\n");
		System.out.printf("Depth: "+ this.maximumDepth + ",TimesPruningMax: " + this.timesPruningMax + ",TimesPruningMin: " + this.timesPruningMin + ",NodesNumber: "+ this.nodesNumber + ",Value: "+ this.maximumValue + ".\n");
	}
	
	//the available actions for the root node
	public DiscAction getNextAction(){
		DiscAction nextAction = null;
		for(int i = 0; i < nextActions.size(); i++){
			if ( (valueCorrespondingToAction.get(i).intValue() == maximumValue) ){
				nextAction = nextActions.get(i);
				return nextAction;
			}
		}
		return nextAction;
	}
	
	// construct function
	public AlphaBetaTree(Board board){
		this.currentBoard = board;
		AlphaBetaSearch(this.currentBoard);
	}
	
	// construct function according to difficulty level
	public AlphaBetaTree(Board board, int level){
		this.currentBoard = board;
		this.differentLevel = 1;
		this.difficultyLevel = level;
		AlphaBetaSearch(this.currentBoard);
	}
	
	//realize function ALPHA-BETA-SEARCH(state) function in alpha-beta search algorithms
	private void AlphaBetaSearch(Board board){
		int alpha = AlphaBetaNode.Minimum;
		int beta = AlphaBetaNode.Maximum;
		int depth = 0;
		AlphaBetaNode alphaBetaNode = new AlphaBetaNode(board, alpha, beta, depth);
		this.nodesNumber = this.nodesNumber + 1;
		this.maximumValue = maxValue(alphaBetaNode);
	}
	
	//realize function MAX-VALUE(state, ¦Á, ¦Â) function in alpha-beta search algorithms
	private int maxValue(AlphaBetaNode node){
		Board board = node.getBoard();
		int alpha = node.getAlpha();
		int beta = node.getBeta();
		int depth = node.getDepth();
		if ( Board.terminalTest(board) ){
			return Board.utility(board);
		}
		if (this.differentLevel == 1){
			if (this.difficultyLevel == depth){
				return Board.heuristicUtility(board);
			}
		}
		int value = AlphaBetaNode.Minimum;
		List<DiscAction> discActions = board.actions(Board.White);
		if (discActions.size() == 0){
			//System.out.printf("i am here");
			depth = depth + 1;
			maximumDepth = Math.max(maximumDepth, depth);
			AlphaBetaNode newNode = new AlphaBetaNode(board, alpha, beta, depth);
			node.setChildrenNode(newNode, null);
			this.nodesNumber = this.nodesNumber + 1;
			int minValue = minValue(newNode);
			value = Math.max(value, minValue);
			if ( value >= beta){
				this.timesPruningMax = this.timesPruningMax + 1;
				return value;
			}
			alpha = Math.max(value, alpha);
			node.setAlpha(alpha);
		}else{
			//System.out.printf("here in fact\n");
			depth = depth + 1;
			maximumDepth = Math.max(maximumDepth, depth);
			for (int i = 0; i < discActions.size(); i++){
				DiscAction discAction = discActions.get(i);
				Board newboard = new Board(board);
				newboard.PutDisc(discAction.getxAxis(), discAction.getyAxis(), discAction.getColor());
				AlphaBetaNode newNode = new AlphaBetaNode(newboard, alpha, beta, depth);
				this.nodesNumber = this.nodesNumber + 1;
				node.setChildrenNode(newNode, discAction);
				int minValue = minValue(newNode);
				//System.out.printf("Max "+Integer.toString(depth)+"\n");
				if (depth == 1){
					//System.out.printf("here: minValue=" + minValue + "\n");
					this.nextActions.add(discAction);
					this.valueCorrespondingToAction.add(minValue);
				}
				value = Math.max(value, minValue);
				if ( value >= beta){
					this.timesPruningMax = this.timesPruningMax + 1;
					return value;
				}
				alpha = Math.max(value, alpha);
				node.setAlpha(alpha);
			}
		}
		return value;
	}
	
	//realize function MIN-VALUE(state, ¦Á, ¦Â) function in alpha-beta search algorithms
	private int minValue(AlphaBetaNode node){
		Board board = node.getBoard();
		int alpha = node.getAlpha();
		int beta = node.getBeta();
		int depth = node.getDepth();
		if ( Board.terminalTest(board) ){
			return Board.utility(board);
		}
		if (this.differentLevel == 1){
			if (this.difficultyLevel == depth){
				return Board.heuristicUtility(board);
			}
		}
		int value = AlphaBetaNode.Maximum;
		List<DiscAction> discActions = board.actions(Board.Black);
		if (discActions.size() == 0){
			depth = depth + 1;
			maximumDepth = Math.max(maximumDepth, depth);
			AlphaBetaNode newNode = new AlphaBetaNode(board, alpha, beta, depth);
			node.setChildrenNode(newNode, null);
			this.nodesNumber = this.nodesNumber + 1;
			value = Math.min(value, maxValue(newNode));
			if ( value <= alpha){
				this.timesPruningMin = this.timesPruningMin + 1;
				return value;
			}
			beta = Math.min(value, beta);
			node.setBeta(beta);
		}else{
			depth = depth + 1;
			maximumDepth = Math.max(maximumDepth, depth);
			for (int i = 0; i < discActions.size(); i++){
				DiscAction discAction = discActions.get(i);
				Board newboard = new Board(board);
				newboard.PutDisc(discAction.getxAxis(), discAction.getyAxis(), discAction.getColor());				
				AlphaBetaNode newNode = new AlphaBetaNode(newboard, alpha, beta, depth);
				this.nodesNumber = this.nodesNumber + 1;
				node.setChildrenNode(newNode, discAction);
				//System.out.printf("Min "+Integer.toString(depth)+"\n");
				value = Math.min(value, minValue(newNode));
				if ( value <= alpha){
					this.timesPruningMin = this.timesPruningMin + 1;
					return value;
				}
				beta = Math.min(value, beta);
				node.setBeta(beta);
			}
		}
		return value;
	}
	
	
}
