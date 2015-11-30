package othelloGame.GUIMode;

import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JLabel;
import othelloGame.basicStructure.*;
import othelloGame.commandLineMode.CommandLineMode;

//realize the GUI mode of the game
public class GUIMode extends JFrame
{
	private int first = 1;
	private int differentLevel = 0;
	private int difficultyLevel = AlphaBetaTree.LevelEasy;;
	private Dialog dialog=null;
	private Board board=new Board();
	private static String chessBoard="chessboard.JPG";
	private static String black="black.jpg";
	private static String white="white.JPG";	
	private Image chessBoardImage=Toolkit.getDefaultToolkit().getImage(getPackagePath() + "\\" + chessBoard); 
	private Image blackImage=Toolkit.getDefaultToolkit().getImage(getPackagePath() + "\\" + black); 
	private Image whiteImage=Toolkit.getDefaultToolkit().getImage(getPackagePath() + "\\" + white);

	
	// used to find resource file
	private static String getPackagePath()
	{
		return Paths.get(".\\res").toAbsolutePath().toString();
	}
	
	// main function
	public static void main(String[] args)
	{
		GUIMode guiMode=new GUIMode();
		guiMode.setVisible(true);
	}
	
	
	//construct function
	public GUIMode()
	{
		System.out.printf("Do you want to use Default Setting? Y(yes) or N(no):\n");
		try{
			BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));
			String str = buf.readLine();
			if (str.equalsIgnoreCase("N")){
				System.out.printf("Do you want to go first? Y(yes) or N(no):\n");
				str = buf.readLine();
				if (str.equalsIgnoreCase("Y")){
					first = 1;
				}else{
					first = 0;
				}
				System.out.printf("Do you want to select diffculty level? (default: The hardest level) Y(yes) or N(no):\n");
				differentLevel = 0;
				difficultyLevel = AlphaBetaTree.LevelEasy;
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
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.setTitle("Othello Chess");
		this.setSize(180, 180);
		this.setResizable(false);
		this.addMouseListener(mouseAdapter);
		this.addWindowListener(windowAdapter);
		if (first == 0){
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
			}
		}
		//this.update(this.getGraphics());
	}
	
	
	// update the view
	public void paint(Graphics g)
	{	
		
		
		super.paint(g);

		g.drawImage(chessBoardImage,0,0,this);
		
		for (int i=0; i<4; i++)
		{
			for (int j=0; j<4; j++)
			{
				if (this.board.getColorFromPoint(i+1, j+1) == Board.Black)
				{

					g.drawImage(blackImage,26*j+31,26*i+31,this);
				}
				else if (this.board.getColorFromPoint(i+1, j+1) == Board.White)
				{
					g.drawImage(whiteImage,26*j+31,26*i+31,this);
				}
			}
		}
		
		if ( Board.terminalTest(board) ){
			String win = "";
			if ( Board.utility(board) > 0){
				win = "You Lose!\n";
				
			}else if ( Board.utility(board) < 0){
				win = "You Win!\n";
			}else{
				win = "Equal!\n";
			}
			if (dialog == null)
			{
				dialog = new Dialog(this, "Game Result:", true);
			}
            
			JLabel label = new JLabel(win);
			dialog.add(label);
			dialog.addWindowListener(windowAdapter);
			dialog.pack();
			dialog.setVisible(true);
		}
		
	}

	// used to process the event of mouse click, first human move and computer moves by using alpha-beta search or cut-off search
	private void processMouseClick(MouseEvent e)
	{
		if ( Board.terminalTest(board) ){
			String win = "";
			if ( Board.utility(board) > 0){
				win = "You Lose!\n";
			}else if ( Board.utility(board) < 0){
				win = "You Win!\n";
			}else{
				win = "Equal!\n";
			}
			if (dialog == null)
			{
				dialog = new Dialog(this, "Game Result:", true);
			}
            
			JLabel label = new JLabel(win);
			dialog.add(label);
			dialog.addWindowListener(windowAdapter);
			dialog.pack();
			dialog.setVisible(true);
		}

		int mousex=0,mousey=0;
		mousex = (e.getY() - 27) / 25;
		mousey = (e.getX() - 27) / 25;
		System.out.println("You want to place black pile in (" + (mousex+1) + "," + (mousey+1) +")\n");
		
		
		if ( board.checkPutDiscForColor(Board.Black)){
			if(!board.checkPutDisc(mousex+1, mousey+1, Board.Black)){
				System.out.printf("Input wrong!\n");
			}else{
				board.PutDisc(mousex+1, mousey+1, Board.Black);
				//System.out.printf("Input succesfully!\n");
				this.update(this.getGraphics());
				CommandLineMode.commandPrintBoard(board);
				if ( board.checkPutDiscForColor(Board.White)){
					AlphaBetaTree alphaBetaTree = null;
					if ( differentLevel == 0 ){
						alphaBetaTree = new AlphaBetaTree(board);
					}else{
						alphaBetaTree = new AlphaBetaTree(board, difficultyLevel);
					}
					alphaBetaTree.printAlphaBetaTreeInfo();
					DiscAction discAction = alphaBetaTree.getNextAction();
					System.out.println();
					board.PutDisc(discAction.getxAxis(), discAction.getyAxis(), Board.White);
					System.out.println("Computer want to place white pile in (" + discAction.getxAxis() + "," + discAction.getyAxis() +")\n");
					this.update(this.getGraphics());
					CommandLineMode.commandPrintBoard(board);
				}
				
			}
		}else{
			if ( board.checkPutDiscForColor(Board.White)){
				AlphaBetaTree alphaBetaTree = null;
				if ( differentLevel == 0 ){
					alphaBetaTree = new AlphaBetaTree(board);
				}else{
					alphaBetaTree = new AlphaBetaTree(board, difficultyLevel);
				}
				alphaBetaTree.printAlphaBetaTreeInfo();
				DiscAction discAction = alphaBetaTree.getNextAction();
				System.out.println();
				board.PutDisc(discAction.getxAxis(), discAction.getyAxis(), Board.White);
				System.out.println("Computer want to place white pile in (" + discAction.getxAxis() + "," + discAction.getyAxis() +")\n");
				this.update(this.getGraphics());
				CommandLineMode.commandPrintBoard(board);
			}
		}

	}
	
	// process event of close window
	private void processClosing(WindowEvent e)
	{
		if (e.getComponent() instanceof Dialog)
		{
			dialog.setVisible(false);
			board=new Board();
			this.update(this.getGraphics());
		}
		else
		{
			System.exit(0);
		}
	}
	
	private MouseAdapter mouseAdapter=new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			processMouseClick(e);			
		}
	};
	
	private WindowAdapter windowAdapter=new WindowAdapter()
	{
		public void windowClosing(WindowEvent e)   
		{ 
			processClosing(e);
		}
	};
	
}
