import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class InfoPanel extends JPanel implements Runnable {

	private int FPS;
	private BirdGame game;
	private int panelWidth;
	private int panelHeight;
	private ArrayList<Bird> copy = new ArrayList<>();
	
	public InfoPanel(int width, int height, BirdGame game, int fps) {
		this.FPS = fps;
		this.game = game;
		panelWidth = width;
		panelHeight = height;
	}
	
	public void run() {
		double count = 0;
		double average = 0;
		double currentTime;
		double timeSinceLast;
		double lastStep = System.currentTimeMillis();
		
		while(true) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentTime = System.currentTimeMillis();
			timeSinceLast = currentTime - lastStep;
			if (timeSinceLast >= 1000/FPS) {
				count++;
				lastStep = currentTime + (timeSinceLast%(1000/FPS));
				average = (timeSinceLast+average*(count-1))/count;
				if(count%50 == 0) {
					System.out.println("FPS: "+ 1000/average);
				}
				step();
			}
		}
	}
	
	public void step() {
		repaint();
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.white);
		g.fillRect(0,0, panelWidth, panelHeight);
		

		int birdNum = 0;
		int x = 0;
		if(!game.loadingNextGeneration) {
			copy.clear();
			
			for(Bird b: game.birds) {
				copy.add(new Bird(b));
				b.renderInfo(x,30+birdNum*140, g);
				birdNum++;
				if(birdNum%12 == 0) {
					birdNum = 0;
					x += 500;
				}
			}
		}
		if(game.loadingNextGeneration) {
			for(Bird b: copy) {
				b.renderInfo(x,30+birdNum*140, g);
				birdNum++;
				if(birdNum%12 == 0) {
					birdNum = 0;
					x += 500;
				}
			}
		}
		
	}
}
