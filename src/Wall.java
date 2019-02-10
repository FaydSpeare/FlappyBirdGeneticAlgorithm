import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Wall {
	public int x;
	public int gapy;
	public int gap;
	private int height;
	public int width;
	private Random rand;
	public Rectangle wallRECT1;
	public Rectangle wallRECT2;
	
	public Wall(int x, int width, int height, int gap) {
		this.gap = gap;
		this.x = x;
		this.width = width;
		this.height = height;
		
		rand = new Random();
		gapy = 300;
		
		wallRECT1 = new Rectangle(x,0, x+ this.width, gapy);
		wallRECT2 = new Rectangle(x,gapy+ this.gap, x+ this.width, this.height);
		
	}
	
	public void tick(BirdGame game) {
		int moveX = -15;
		if(x<=-width) {
			
			for(Bird b: game.birds) {
				if(!b.DEAD) {
					b.points++;
				}
			}
			x = width *20;
			gapy = rand.nextInt(height -(gap +200)+100);
		}
		
		for(Bird b: game.birds) {
			if(!b.DEAD) {
				b.distance+=Math.abs(1);
			}
		}
		
		move(moveX);
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(x,0, width, gapy);
		g.fillRect(x,gapy+ gap, width, height -(gap +gapy));
		
		wallRECT1.render(g);
		wallRECT2.render(g);
	}
	
	private void move(int amount) {
		x += amount;

		wallRECT1.update(x,x+ width,0, gapy);
		wallRECT2.update(x,x+ width, gapy+ gap, height);
	}
}
