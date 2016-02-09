package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import environment.Cell;
import environment.Entrepot;
import environment.TypeCell;

/**
 * panel on which the environment is displayed<br>
 * user can change the position of the goal by a mouse click
 * @author emmanuel adam*/
@SuppressWarnings("serial")
public class PanelEnvironment extends JPanel{
	/** entrepot (environment) linked to this gui*/
	Entrepot ent;

	/** width of a cell in pixel*/
	int depX;
	/** height of a cell in pixel*/
	int depY;
	
	/** define the panel and the mouse behaviour*/
	PanelEnvironment(int _width, int _height, Entrepot _ent) {
		setSize(_width, _height);
		ent = _ent;
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e) {
				int i = e.getX()/depX;
				int j = e.getY()/depY;
				ent.setGoal(i, j);
				ent.removeVisit();
			}
		});
	}

	/** paint the environment*/
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// to have a good rendering

		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);

		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		Cell[][] content = ent.getContent();
		
		//compute the size of a cell in pixels
		depX = (int) (((double) getWidth()) / ((double) ent.getWidth()));
		depY = (int) (((double) getHeight()) / ((double) ent.getHeight()));
		
		for (int i = 0; i < ent.getWidth(); i++)
			for (int j = 0; j < ent.getHeight(); j++) {
				if (content[i][j].getType() == TypeCell.TERRE) {
					g2d.setColor(Color.darkGray);
					g2d.fillRect(i * depX , j * depY , depX , depY ); }
				if (content[i][j].getType() == TypeCell.SABLE) {
					g2d.setColor(Color.YELLOW);
					g2d.fillRect(i * depX , j * depY , depX , depY ); }
				if (content[i][j].getType() == TypeCell.EAU) {
					g2d.setColor(Color.BLUE);
					g2d.fillRect(i * depX , j * depY , depX , depY ); }
				if (content[i][j].isContainer()) {
					g2d.setColor(Color.BLACK);
					g2d.fillOval(i * depX, j * depY, depX, depY);
				}
				else if (content[i][j].isStart()) {
					g2d.setColor(Color.WHITE);
					g2d.fillOval(i * depX, j * depY, depX, depY);
				}
				else if (content[i][j].isGoal()) {
					g2d.setColor(Color.GREEN);
					g2d.fillOval(i * depX, j * depY, depX, depY);
				}
//				else if (content[i][j].isVisited()) {
//					g2d.setColor(Color.RED);
//					g2d.fillRect(i * depX + 1, j * depY + 1, depX - 2, depY - 2);
//				}
				else if (content[i][j].getF()>0) {// if the cell has been evaluated
					g2d.setColor(Color.GRAY);
					g2d.fillRect(i * depX + 2, j * depY + 2, depX - 4, depY - 4);
				}
					
			}
		ArrayList<Cell> solution = ent.getSolution();
		if(solution!=null)
		{
			g2d.setColor(Color.RED);
			int smaller = (depX<depY?depX:depY);
			BasicStroke penSize = new BasicStroke(smaller-1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f);
			
			g2d.setStroke(penSize);
			
			Cell c0 = ent.getStart();
			for(Cell c1:solution)
			{
				g2d.drawLine(c0.getX() * depX + depX/2, c0.getY() * depY + depY/2, c1.getX() * depX + depX/2, c1.getY()  * depY + depY/2);
				c0 = c1;
			}
		}

	}

}
