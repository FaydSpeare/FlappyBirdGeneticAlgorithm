import java.awt.Color;
import java.awt.Graphics2D;

public class Rectangle {
	private int x0;
	private int y0;
	private int y1;
	private int x1;
	boolean RENDER_ON = false;
	
	public Rectangle(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.x1 = x1;
		
		this.y0 = y0;
		this.y1 = y1;
	}
	
	public boolean intersects(Rectangle r) {
		if(r.x0 > this.x0 && r.x0 < this.x1) {
			if(r.y0 > this.y0 && r.y0 < this.y1) {
				return true;
			}
			else if(r.y1 > this.y0 && r.y1 < this.y1) {
				return true;
			}
		}
		else if(r.x1 > this.x0 && r.x1 < this.x1) {
			if(r.y0 > this.y0 && r.y0 < this.y1) {
				return true;
			}
			else if(r.y1 > this.y0 && r.y1 < this.y1) {
				return true;
			}
		}
		return false;
	}
	
	public void update(int x0, int x1, int y0,  int y1) {
		this.x0 = x0;
		this.x1 = x1;
		
		this.y0 = y0;
		this.y1 = y1;
	}
	
	public void render(Graphics2D g) {
		if(RENDER_ON) {
			int border = 6;
			g.setColor(Color.red);
			g.fillRect(x0, y0, x1-x0, border);
			g.fillRect(x0, y0, border, y1-y0);
			
			g.fillRect(x1-border, y0, border, y1-y0);
			g.fillRect(x0, y1-border, x1-x0, border);
		}
	}
}
