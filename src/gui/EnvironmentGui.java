package gui;

import java.awt.BorderLayout;
import javax.swing.*; 

import environment.Entrepot;


/** window used to display the environment
 * @author emmanuel adam*/
@SuppressWarnings("serial")
public class EnvironmentGui extends JFrame{

    /** environment to display*/
    Entrepot ent;
    
    public EnvironmentGui(Entrepot _ent ) {
        ent = _ent;
    	setBounds(10, 10, 800, 800);     
    	setTitle("A* resolution for shortest way");
    	buildGui();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    /** add a PanelEnvironment in the center of the window*/
    private void buildGui()
    {
    	
    	getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, new PanelEnvironment(700, 700, ent));
    }


}
