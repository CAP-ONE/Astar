package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import environment.Cell;
import environment.Entrepot;
import environment.TypeCell;
import gui.EnvironmentGui;

/** this class propose an implementation of the a star algorithm */
public class AlgoAStar {

	/** nodes to be evaluated*/
	ArrayList<Cell> freeNodes;
	/** evaluated nodes*/
	ArrayList<Cell> closedNodes;

	/** Start Cell*/
	Cell start;
	/** Goal Cell*/
	Cell goal;
	

	/** graphe / map of the nodes*/
	Entrepot ent;
	/** gui*/
	EnvironmentGui gui;

	/**initialize the environment (100 x 100 with a density of container +- 20%) */
	AlgoAStar()
	{
		ent = new Entrepot(100, 100, 0.2, this);
		gui = new EnvironmentGui(ent);
		reCompute();
	}

	/** a* algorithm to find the best path between two states 
	 * @param _start initial state
	 * @param _goal final state*/
	ArrayList<Cell> algoASTAR(Cell _start, Cell _goal)
	{
		start = _start;
		goal = _goal;
		// list of visited nodes
		closedNodes = new ArrayList<Cell>();
		// list of nodes to evaluate
		freeNodes = new ArrayList<Cell>();
		freeNodes.add(start);
		
		// no cost to go from start to start
		// TODO: g(start) <- 0
		// TODO: h(start) <- evaluation(start)
		// TODO: f(start) <- h(start)
		
		
		start.setG(0);
		start.setH(evaluation(start));
		start.setF(0);
		
		
		// while there is still a node to evaluate
		while(!freeNodes.isEmpty())
		{
			// choose the node having a F minimal
			Cell n = chooseBestNode();
			// stop if the node is the goal
			if (isGoal(n)) return rebuildPath(n);
			// TODO: freeNodes <- freeNodes - {n}
			// TODO: closedNodes <- closedNodes U {n}
			
			freeNodes.remove(n);
			closedNodes.add(n);
			
			// construct the list of neighbourgs
			ArrayList<Cell> nextDoorNeighbours  = neighbours4(n);
			for(Cell ndn:nextDoorNeighbours)
			{
				// if the neighbour has been visited, do not reevaluate it
				if (closedNodes.contains(ndn))
					continue;
				// cost to reach the neighbour is the cost to reach n + cost from n to the neighbourg
				
				int cost = n.getG() + costBetween(n, ndn);
				
				//System.out.println("cost : "+cost);
				
				boolean bestCost = false;
				// if the neighbour has not been evaluated
				if (!freeNodes.contains(ndn))
				{
					// TODO: freeNodes <- freeNodes U {ndn}
					// TODO: h(ndn) -> evaluation(ndn)
					freeNodes.add(ndn);
					ndn.setH(evaluation(ndn));
					bestCost = true;
				}
				
				
				else
					 //if the neighbour has been evaluated to a more important cost, change its evaluation
					if (cost < ndn.getG())
						bestCost = true;
				if(bestCost)
				{
					ndn.setParent(n);
					ndn.setG(cost);
					ndn.setF(ndn.getG() + ndn.getH());
					//TODO : g(ndn) <- cost
					//TODO : f(ndn) <- g(ndn) + h(ndn)
				}
				
				
			}
		}
		return null;
	}

	/** return the path from start to the node n*/
	ArrayList<Cell> rebuildPath(Cell n)
	{
		if (n.getParent()!=null)
		{
			ArrayList<Cell> p = rebuildPath(n.getParent());
			n.setVisited(true);
			p.add(n);
			return p;
		}
		else
			return (new ArrayList<Cell>());
	}

	/** algo called to (re)launch a star algo*/
	public void reCompute()
	{
		ArrayList<Cell>  solution = algoASTAR(ent.getStart(), ent.getGoal());
		ent.setSolution(solution);
		if (solution==null) 
			System.out.println("solution IMPOSSIBLE");
		
		gui.repaint();
	}
	

	/** return the estimation of the distance from c to the goal*/
	int evaluation(Cell c)
	{		
		// TODO: cf cours : sur Terre : 10* distance vol d'oiseau entre but(goal) et c
		int cx = c.getX(); int cy = c.getY();
		int gx = goal.getX() ; int gy = goal.getY();
		
		return (int) Math.sqrt(Math.pow((cx-gx), 2.0) + Math.pow((cy-gy), 2.0));
	}

	/** return the free node having the minimal f*/
	Cell chooseBestNode()
	{		
		ArrayList<Cell> costList = new ArrayList<>();
		
		for(Cell n : freeNodes) {
			int Fcost = costBetween(start, n) + evaluation(n);
			System.out.println("f : "+Fcost);
			n.setF(Fcost);
			costList.add(n);
		}
		
		Collections.sort(costList,new Comparator<Cell>(){
			   @Override
			   public int compare(final Cell lhs,Cell rhs) {
				   if(rhs.getF() < lhs.getF()) return 1;
				   if(rhs.getF() > lhs.getF()) return -1;
				   
				   return 0;
			     }
			 });
		
		System.out.println("minimal f : "+costList.get(0).getF());
		
		return costList.get(0);
	}

	/** return weither n is a goal or not */
	boolean isGoal(Cell n)
	{
		return (n.getX() == goal.getX() && n.getY() == goal.getY());
	}

	/** return the neighbouring of a node n; a diagonal avoid the containers */
	ArrayList<Cell> neighbours(Cell n)
	{
		ArrayList<Cell> diagNodes = new ArrayList<Cell>();
		for(Cell neigh : neighboursDiag(n)) {
			if(neigh.isContainer()) continue;
			else diagNodes.add(neigh);
		}
		
		return diagNodes;
	}

	/** return the neighbouring of a node n*/
	ArrayList<Cell> neighboursDiag(Cell n)
	{
		ArrayList<Cell> diagNodes = new ArrayList<Cell>();
		
		for(int i=0; i<ent.getWidth(); i++) {
			for(int j=0; j<ent.getHeight(); j++) {
				Cell neigh = ent.getCell(i, j);

				int nx = neigh.getX(); int ny = neigh.getY();
				int sx = n.getX() ; int sy = n.getY();

				if((nx==sx && ny==sy+1) || (nx==sx+1 && ny==sy+1) || (nx==sx+1 && ny==sy) || (nx==sx+1 && ny==sy-1) 
						|| (nx==sx && ny==sy-1) || (nx==sx-1 && ny==sy-1) || (nx==sx-1 && ny==sy) || (nx==sx-1 && ny==sy+1)) {
					//if (neigh.isContainer() || neigh.isVisited()) break;

					diagNodes.add(neigh);
				}
			}
		}


		System.out.println("Start node x: " +start.getX()+" y: "+start.getY());
//
//		for(Cell diag : diagNodes) {
//			System.out.println("Diag node x: " +diag.getX()+" y: "+diag.getY());
//		}

		return diagNodes;
	}

	/** return the neighbouring of a node n without permission to go in diagonal*/
	ArrayList<Cell> neighbours4(Cell n)
	{
		ArrayList<Cell> diagNodes = new ArrayList<Cell>();
		
		for(int i=0; i<ent.getWidth(); i++) {
			for(int j=0; j<ent.getHeight(); j++) {
				Cell neigh = ent.getCell(i, j);

				int nx = neigh.getX(); int ny = neigh.getY();
				int sx = n.getX() ; int sy = n.getY();

				if((nx==sx && ny==sy+1) || (nx==sx+1 && ny==sy) 
						|| (nx==sx && ny==sy-1) || (nx==sx-1 && ny==sy)) {
					if (neigh.isContainer()) continue;
					diagNodes.add(neigh);
				}
			}
		}


		System.out.println("Start node x: " +start.getX()+" y: "+start.getY());


		return diagNodes;
	}

	/** return the cost from n to c : 10 for a longitudinal move, 14 (squareroot(2)*10) for a diagonal move */
	int costBetween(Cell n, Cell c)
	{	
		//TODO : sur terre, deplacement horizontal ou vertical = 10; en diagonale = 14
		int nx = n.getX(); int ny = n.getY();
		int cx = c.getX() ; int cy = c.getY();
		
		
		return (int) Math.sqrt(Math.pow((cx-nx), 2.0) + Math.pow((cy-ny), 2.0));
	}


	public static void main(String []args)
	{
		new AlgoAStar();

	}
}
