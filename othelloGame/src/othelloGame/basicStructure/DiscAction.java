package othelloGame.basicStructure;


//This class abstract the operation of the disc:
public class DiscAction {
	private int xAxis;
	private int yAxis;
	private int color;
	
	// construct function
	public DiscAction(int xAxis, int yAxis, int color){
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.color = color;
	}
	// get x-Axis of the action
	public int getxAxis(){
		return this.xAxis;
	}
	// get y-Axis of the action
	public int getyAxis(){
		return this.yAxis;
	}
	// get color of the action
	public int getColor(){
		return this.color;
	}

}
