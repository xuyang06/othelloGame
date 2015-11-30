package othelloGame.basicStructure;

import java.util.ArrayList;
import java.util.List;


//This class describes the tree node in the alpha-beta algorithms and the corresponding operations
public class AlphaBetaNode {
	private Board board;
	private int alpha;
	private int beta;
	private int depth;
	public static final int Minimum = -200;
	public static final int Maximum = 200;
	private List<AlphaBetaNode> childrenBoards = new ArrayList<AlphaBetaNode>();
	private List<DiscAction> childrenActions = new ArrayList<DiscAction>();
	
	//construct function
	public AlphaBetaNode(Board board, int alpha, int beta, int depth){
		this.board = board;
		this.alpha = alpha;
		this.beta = beta;
		this.depth = depth;
	}
	
	//get the corresponding board
	public Board getBoard(){
		return this.board;
	}
	
	//update alpha value 
	public void setAlpha(int alpha){
		this.alpha = alpha;
	}
	
	//update beta value
	public void setBeta(int beta){
		this.beta = beta;
	}
	
	//generate child node
	public void setChildrenNode(AlphaBetaNode childrenNode, DiscAction childrenAction){
		this.childrenBoards.add(childrenNode);
		this.childrenActions.add(childrenAction);
	}
	
	//get all the children nodes
	public List<AlphaBetaNode> getChildrenBoards(){
		return this.childrenBoards;
	}
	
	// get the actions result in the children nodes
	public List<DiscAction> getChildrenActions(){
		return this.childrenActions;
	}
	
	//get alpha value of the node 
	public int getAlpha(){
		return this.alpha;
	}
	
	//get beta value of the node
	public int getBeta(){
		return this.beta;
	}
	
	//get depth of the node
	public int getDepth(){
		return this.depth;
	}
	
}
