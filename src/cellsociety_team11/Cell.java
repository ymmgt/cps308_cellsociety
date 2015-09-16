package cellsociety_team11;

import javafx.scene.shape.Polygon;

public abstract class Cell extends Polygon{

	protected State myState;
	protected Location myLoc;
	protected boolean visited;
	
	Cell(State s, Location l){
		myState = s;
		myLoc = l;
		visited = false;
	}

	public State getState(){
		return myState;
	}

	public void setState(State s){
		myState = s;
	}
	
	public Location getLocation(){
		return myLoc;
	}
	
	public boolean getVisited(){
		return visited;
	}
	
	public void setVisited(boolean b){
		visited = b;
	}

	public abstract void setNeighborCells();
	
	public abstract State determineNextState();

	public void setNextState(){
		myState = this.determineNextState();
		this.setState(myState);
	}

	public void goToNextState(){
		
	}

}
