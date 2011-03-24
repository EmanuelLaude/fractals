package fractals;

public class Square {
	private Point[] points;

	public Square(Point a, Point b, Point c, Point d) {
		points = new Point[] { a, b, c, d };
	}

	public Square() {
		points = new Point[4];
	}

	public Point getA() {
		return points[0];
	}

	public void setA(Point a) {
		this.points[0] = a;
	}

	public Point getB() {
		return points[1];
	}

	public void setB(Point b) {
		this.points[1] = b;
	}

	public Point getC() {
		return points[2];
	}

	public void setC(Point c) {
		this.points[2] = c;
	}

	public Point getD() {
		return points[3];
	}

	public void setD(Point d) {
		this.points[3] = d;
	}

	public Point[] getPoints() {
		return points;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}

}
