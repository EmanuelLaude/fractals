package fractals;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

public class IFSApplet extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;

	private int frameNumber = -1;

	private int delay;

	private Thread animatorThread;

	private Image buffer = null;

	private Graphics2D gBuffer = null;

	private Point o;

	private int zoom;

	private Square firstSquare;

	private Collection squares;

	private double[][] ifs;

	private int numberOfIterations;

	public void init() {
		delay = 1000;

		if (buffer == null) {
			buffer = createImage(this.getSize().width, this.getSize().height);
			gBuffer = (Graphics2D) buffer.getGraphics();
		}

		Properties properties = new Properties();
	    try {
	    	URL url= new URL(this.getCodeBase() + "/" + this.getParameter("properties"));
	    	InputStream in = url.openStream();
	        properties.load(in);
	    } catch (IOException e) {
	    }
	    
	    int numberOfTransformations = Integer.parseInt(properties.getProperty("numberOfTransformations"));
	    ifs = new double[numberOfTransformations][6];
	    
	    for(int i = 0; i < numberOfTransformations; i++) {
	    	String[] f = properties.getProperty("f" + (i + 1)).split(",");
	    	for(int j = 0; j < f.length; j++) {
	    		ifs[i][j] = Double.parseDouble(f[j]);
	    	}
	    }

		
	    int shiftX = Integer.parseInt(properties.getProperty("shiftX"));
	    int shiftY = Integer.parseInt(properties.getProperty("shiftY"));
	    zoom = Integer.parseInt(properties.getProperty("zoom"));
		o = new Point(this.getSize().width / 2 + shiftX, this.getSize().height / 2 - shiftY);
		numberOfIterations = Integer.parseInt(properties.getProperty("numberOfIterations"));
			
		squares = new ArrayList();
		String[] a = properties.getProperty("a").split(",");
		String[] b = properties.getProperty("b").split(",");
		String[] c = properties.getProperty("c").split(",");
		String[] d = properties.getProperty("d").split(",");
		firstSquare = new Square(new Point(Double.parseDouble(a[0]), Double.parseDouble(a[1])), new Point(Double.parseDouble(b[0]), Double.parseDouble(b[1])), new Point(Double.parseDouble(c[0]), Double.parseDouble(c[1])), new Point(Double.parseDouble(d[0]), Double.parseDouble(d[1])));
		squares.add(firstSquare);
		animatorThread = new Thread(this);
		animatorThread.start();
	}

	public void destroy() {
		if (animatorThread != null)
			animatorThread = null;
	}

	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	public void run() {
		Thread currentThread = Thread.currentThread();

		while (animatorThread == currentThread) {
			frameNumber++;

			gBuffer.clearRect(0, 0, this.getSize().width,
							this.getSize().height);
			gBuffer.drawString("Frame " + frameNumber, 20, 20);

			if (frameNumber == 0) {
				gBuffer.setColor(Color.RED);
				this.drawSquare(firstSquare);
				gBuffer.setColor(Color.BLACK);
				gBuffer.drawString("0 squares calculated", 20, 40);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					break;
				}
				continue;
			}


			
			Collection sourceSquares = squares;
			squares = null;
			Collection transformedSquares = new ArrayList();

			Iterator iterator = sourceSquares.iterator();

			while (iterator.hasNext()) {
				Square sourceSquare = (Square) iterator.next();

				Square transformedSquare;
				for (int j = 0; j < ifs.length; j++) {
					double a11 = ifs[j][0];
					double a12 = ifs[j][1];
					double a21 = ifs[j][2];
					double a22 = ifs[j][3];
					double b1 = ifs[j][4];
					double b2 = ifs[j][5];

					transformedSquare = new Square();
					Point[] points = sourceSquare.getPoints();

					for (int k = 0; k < 4; k++) {
						double transformedX = a11 * points[k].getX() + a12
								* points[k].getY() + b1;
						double transformedY = a21 * points[k].getX() + a22
								* points[k].getY() + b2;
						transformedSquare.getPoints()[k] = new Point(
								transformedX, transformedY);
					}

					transformedSquares.add(transformedSquare);
				}
			}

			squares = transformedSquares;
			gBuffer.drawString(squares.size() + " squares calculated", 20, 40);
			this.drawSquares();

			repaint();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				break;
			}
			if (frameNumber == numberOfIterations) {
				animatorThread = null;
			}

		}
	}

	private void drawSquares() {
		Iterator iterator = squares.iterator();
		while (iterator.hasNext()) {
			this.drawSquare((Square) iterator.next());
		}
	}

	private void drawSquare(Square square) {
		gBuffer.drawPolygon(new int[] {
				(int) (square.getA().getX() * zoom + o.getX()),
				(int) (square.getB().getX() * zoom + o.getX()),
				(int) (square.getC().getX() * zoom + o.getX()),
				(int) (square.getD().getX() * zoom + o.getX()) }, new int[] {
				(int) (o.getY() - square.getA().getY() * zoom),
				(int) (o.getY() - square.getB().getY() * zoom),
				(int) (o.getY() - square.getC().getY() * zoom),
				(int) (o.getY() - square.getD().getY() * zoom) }, 4);

	}
}
