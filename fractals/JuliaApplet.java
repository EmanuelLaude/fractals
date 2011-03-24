package fractals;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class JuliaApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private int zoom = 200;
	private double reC;
	private double imC;
	private Point o;
	
	public void init() {
	    reC = 0.9;//Double.parseDouble(this.getParameter("reC"));
	    imC = 0.76;//Double.parseDouble(this.getParameter("imC"));
		o = new Point(this.getSize().width / 2, this.getSize().height / 2);	
	}
	
	private boolean isOutside(double reZo, double imZo) {
		for(int i = 0; i < 100; i++) {
			if(Math.pow(reZo, 2) + Math.pow(imZo, 2) > 20)
				return true;
			double reZ1 = Math.pow((reZo + reC), 2) - Math.pow((imZo + imC), 2);
			double imZ1 = 2 * (reZo + reC) * (imZo + imC);
			

			reZo = reZ1;
			imZo = imZ1;		
		}
				
		return false;
	}
	
    public void paint (Graphics g) {
    	g.setColor(Color.orange);
    	for(double imZ = -2; imZ < 2; imZ += 0.005) {
    		for(double reZ = -4; reZ < 4; reZ += 0.005) {

    			if(!isOutside(reZ, imZ)) {
    				drawPoint(g, new Point(reZ, imZ));
    			}
    			
    		}
    	}
    }
    
	private void drawPoint(Graphics g, Point point) {
		g.drawOval((int) (point.getX() * zoom + o.getX()), (int) (o.getY() - point.getY() * zoom), 0, 0);
	}    
}
