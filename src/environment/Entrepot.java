package environment;

import java.util.ArrayList;

import algo.AlgoAStar;

/** entrepot : rectangle of cells; some cells contains container*/
public class Entrepot {

	/** width of the environment*/
	int width = 30;
	/** height of the environment*/
	int height = 30;
	/** table of cells*/
	Cell[][] content;
	/** density of containers*/
	double density = 0;
	/** initial Cell*/
	Cell start;
	/** goal Cell*/
	Cell goal;
	
	/**solution found by atar*/
	ArrayList<Cell>  solution;
	

	/** A* algo linked to this environment*/
	AlgoAStar algo;
	
	/**create empty environment*/
	Entrepot()
	{
		content = new Cell[width][height];
		init();
	}
	
	/**create empty environment*/
	public Entrepot(int _width, int _height)
	{
		width = _width;
		height = _height;
		content = new Cell[width][height];
		init();
	}
	
	/**create an environment filled with container <br>
	 * set up the initial state and the goal state
	 * @param _density density of cells that own a container*/
	public Entrepot(int _width, int _height, double _density)
	{
		this(_width, _height);
		density = _density;
		fill();
		start = content[width/10][height/10];
		start.setStart(true);
		start.setContainer(false);
		goal = content[width-width/10][height-height/10];
		goal.setGoal(true);
		goal.setContainer(false);
	}

	
	/**create an environment filled with container <br>
	 * set up the initial state and the goal state
	 * @param _density density of cells that own a container
	 * @param _algo A* algo linked to this environment*/
	public Entrepot(int _width, int _height, double _density, AlgoAStar _algo)
	{
		this(_width, _height, _density);
		algo = _algo;
	}

	/** initialise the cells*/
	void init()
	{
		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
				content[i][j] = new Cell(i,j);
	}
	
	/** set some cells as container*/
	void fill()
	{
		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
			{
				if (Math.random() <= density) 
					content[i][j].setContainer(true);
			}
	}
	
	
	/** return the cell int x,y*/
	public Cell getCell(int x, int y) {
		return content[x][y];
	}

	/** change the goal*/
	public void setGoal(int i, int j) {
		goal.setGoal(false);
		goal = content[i][j];
		goal.setGoal(true);
	}

	/** clear the previous visits */
	public void removeVisit()
	{
		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
			{
				content[i][j].setVisited(false);
				content[i][j].setF(0);
				content[i][j].setG(0);
				content[i][j].setH(0);
			}
		algo.reCompute();
	}

	
	////// getters and setters
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public Cell[][] getContent() {
		return content;
	}

	public Cell getStart() {
		return start;
	}

	public void setStart(Cell start) {
		this.start = start;
	}

	public Cell getGoal() {
		return goal;
	}

	public void setGoal(Cell goal) {
		this.goal = goal;
	}
	
	public void setSolution(ArrayList<Cell> solution) {
		this.solution = solution;
	}

	public ArrayList<Cell> getSolution() {
		return solution;
	}

	////// end of getters and setters

}
