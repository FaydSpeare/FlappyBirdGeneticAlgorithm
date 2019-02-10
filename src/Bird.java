import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import NeuralNetwork.NeuralNetwork;

public class Bird {
	private Image FLAP_IMAGE;
	private Image FLY_IMAGE;

	private int height;
	private int width;
	public String title;
	
	public int points = 0;
	public int distance = 0;
	private int flapCount = 0;
	
	private int birdX = 100;
	private int birdY = 100;
	
	private static final int GRAVITY = 10;
	private int velocity = 0;

	private boolean falling = true;
	public Rectangle birdRECT;
	public boolean DEAD = false;
	public boolean cleared = false;
	private double output;
	private double DDx;
	private double DDy;
	
	public boolean RENDER_INPUTS_ON = false;
	private Image elimImage;
	public NeuralNetwork net;
	
	public Bird(Bird b) {
		title = b.title;
		FLAP_IMAGE = b.FLAP_IMAGE;
		FLY_IMAGE = b.FLY_IMAGE;
		points = b.points;
		distance = b.distance;
		flapCount = b.flapCount;
		DEAD = b.DEAD;
	}

	private Image LoadElim() {
		BufferedImage elim = null;
		try {
			elim = ImageIO.read(new File("Eliminated.png"));
			elim = MainFrame.resize(elim, width, height);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return elim;
	}
	
	public Bird(Image fly, Image flap, int by, String title) {
		this.title = title;
		FLAP_IMAGE = flap;
		FLY_IMAGE = fly;
		width = fly.getWidth(null)*50/51;
		height = fly.getWidth(null)*8/11;
		Random r = new Random();
		this.birdX = 50+r.nextInt(200);
		
		this.birdY = by+r.nextInt(200)-100;
		birdRECT = new Rectangle(birdX, birdY, birdX + width, birdY + height);
		elimImage = LoadElim();
		
		// Decide on how many nodes in each layer
		int[] layers = {2,3,2,1};
		
		// Open Neural Network
		net = new NeuralNetwork(layers);
		
	}
	
	public void tick(BirdGame game) {
		if(!DEAD) {
			if(points > 20 || cleared) {
				cleared = true;
				title = "DEITY";
			}
			velocity += 1;
			if(velocity >= 0) {
				falling = true;
			}
			else {
				falling = false;
			}
			
			if(birdY > game.panelHeight || birdY < 0) {
				DEAD = true;
			}
			
			flapLogic(game);
			birdY += velocity;
			birdRECT.update(birdX, birdX + width, birdY, birdY + height);
		}
		else {
			cleared = false;
		}
	}
	
	public void render(Graphics2D g) {
		if(RENDER_INPUTS_ON && !DEAD) {

			g.setColor(Color.blue);

			if(DDx > 0) {
				g.fillRect(birdX + width /2, birdY +(height)/2 -3, (int) DDx, 6);
			}
			else {
				g.fillRect(birdX + width /2 + (int)DDx, birdY +(height)/2 -3, (int) -DDx, 6);
			}
			
			g.setColor(Color.magenta);

			if(DDy > 0) {
				g.fillRect(birdX + width /2 - 3, birdY + height /2, 6, (int) DDy);
				
			}
			else {
				g.fillRect(birdX + width /2 -3,(int) (birdY +DDy) , 6, (int) DDy*-1);
			}
			
		}
		if(!DEAD) {

			if(falling) {
				g.drawImage(FLY_IMAGE, birdX, birdY,null);
			}
			else {
				g.drawImage(FLAP_IMAGE, birdX, birdY,null);
			}

			birdRECT.render(g);
		}

	}
	
	private void flapLogic(BirdGame game) {

		if(true) {
			DDx = game.wall.x - birdX - game.wall.width /2;
			if(DDx < 0) {
				
			}
			DDy = (game.wall.gapy + game.wall.gap /2) - birdY;
			double[] p = {Math.abs(DDx/100),DDy/100};
			output = net.queryOutput(p)[0][0];
			if(output>0.6) {
				if(falling) {
					flap();
				}
			}
		}
		

	}

	private void flap() {
		velocity = -18;
		flapCount++;
	}
	
	public void setDead() {
		DEAD = true;
	}
	
	public void setAlive() {
		DEAD = false;
	}
	
	public boolean isAlive() {
		if(DEAD) {
			return false;
		}
		return true;
	}

	public int calculateFitness() {
		return (int) (points*1000 + distance - Math.abs(DDy)/100);
	}

	public void reset() {
		Random r = new Random();
		DEAD = false;
		birdY = 500 +r.nextInt(200)-100;
		birdX = 50+r.nextInt(200);
		points = 0;
		distance = 0;
		if(!title.equals("DEITY")) {
			if(!title.equals("DEMIGOD")) {
				title = "PARENT";
			}
		}
		flapCount = 0;
		output = 0;
		DDx = 0;
		DDy = 0;
		falling = true;
		velocity = 0;
		RENDER_INPUTS_ON = false;
		birdRECT.RENDER_ON = false;
	}
	
	public void renderInfo(int x, int y, Graphics2D g) {
		if(falling) {
			g.drawImage(FLY_IMAGE,x+50,y,null);
		}
		else {
			g.drawImage(FLAP_IMAGE,x+50,y,null);
		}
		if(DEAD) {
			g.drawImage(elimImage,x+50,y,null);
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(x, y+100, 500, 10);
		
		 int fontSize = 20;
		 g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

		 g.drawString("FLAPS: "+flapCount, x+250, y+20);
		 g.drawString("DISTANCE: "+distance+"m", x+250, y+45);
		 g.drawString("SCORE: "+points, x+250, y+70);
		 
		 g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
		 g.setColor(Color.red);

		 if(title.equals("PARENT")) {
			 g.setColor(Color.magenta);
		 }
		 if(title.equals("BASTARD")) {
			 g.setColor(Color.ORANGE);
		 }
		 if(title.equals("CHILD")) {
			 g.setColor(Color.blue);
		 }
		 if(title.equals("DEMIGOD")) {
			 g.setColor(Color.black);
		 }
		 if(title.equals("DEITY")) {
			 fontSize = 30;
			 g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
			 g.setColor(Color.BLACK);
			 g.fillRect(x+250, y-25, 93, 25);
			 g.setColor(Color.white);
			 g.drawString(title, x+248, y-2);
		 }
		 else {
			 g.drawString(title, x+250, y);
		 }
		 fontSize = 40;
		 g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));

		 if(!DEAD) {
			 g.setColor(Color.gray);
			 g.drawString(""+Math.round(output * 1000.0) / 1000.0, x+400, y);
			 
			 fontSize = 25;
			 g.setColor(Color.LIGHT_GRAY);
			 g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
			 g.drawString(""+Math.round(DDx/100 * 1000.0) / 1000.0, x+415, y+30);
			 g.drawString(""+Math.round(DDy/100 * 1000.0) / 1000.0, x+415, y+60);
		 }

	}
	
	public void setWeights(ArrayList<double[][]> weights) {
		net.weights = weights;
	}
}
