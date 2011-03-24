package fractals;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Random;


public class ChaosGameApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private int zoom;
	private double[][] ifs;
	private Point o;
	private double p[];
	
	
	public void init() {
		Properties properties = new Properties();
	    try {
	    	URL url= new URL(this.getCodeBase() + "/" + this.getParameter("properties"));
	    	InputStream in = url.openStream();
	        properties.load(in);
	    } catch (IOException e) {
	    }
	    
	    int numberOfTransformations = Integer.parseInt(properties.getProperty("numberOfTransformations"));
	    ifs = new double[numberOfTransformations][6];
	    
	    p = new double[numberOfTransformations];
	    for(int i = 0; i < numberOfTransformations; i++) {
	    	String[] f = properties.getProperty("f" + (i + 1)).split(",");
	    	for(int j = 0; j < f.length; j++) {
	    		ifs[i][j] = Double.parseDouble(f[j]);
	    	}
	    	p[i] = Double.parseDouble(properties.getProperty("p" + (i + 1)));
	    }

		
	    int shiftX = Integer.parseInt(properties.getProperty("shiftX"));
	    int shiftY = Integer.parseInt(properties.getProperty("shiftY"));
	    zoom = Integer.parseInt(properties.getProperty("zoom"));
		o = new Point(this.getSize().width / 2 + shiftX, this.getSize().height / 2 - shiftY);	
	}
	
    public void paint (Graphics g) {
    	Point startPoint = new Point(-30, 20);
    	Random random = new Random();
    	for(int i = 0; i < 100000; i++) {
    		int rnd = random.nextInt(10000) + 1;   	
    		double[] f = null;
		
    		int sum = 0;
    		int j;
    		for(j = 0; j < p.length; j++) {
    			sum = (int) (sum + p[j] * 100);

    			if(rnd <= sum) {
    				f = ifs[j];
    				break;
    			}
    			
    		}
    		
    		
    		double a11 = f[0];
    		double a12 = f[1];
    		double a21 = f[2];
    		double a22 = f[3];
    		double b1 = f[4];
    		double b2 = f[5];
    		
    		Point newStartPoint = new Point();
    		newStartPoint.setX(a11 * startPoint.getX() + a12 * startPoint.getY() + b1);
    		newStartPoint.setY(a21 * startPoint.getX() + a22 * startPoint.getY() + b2);	
    		if(i > 100) {
    			this.drawPoint(g, newStartPoint);
    		}
    		startPoint = newStartPoint;
    	}
    }
    
	private void drawPoint(Graphics g, Point point) {
		g.drawOval((int) (point.getX() * zoom + o.getX()), (int) (o.getY() - point.getY() * zoom), 0, 0);
	}    
}
