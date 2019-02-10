import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import NeuralNetwork.Matrix;

public class BirdGame extends JPanel implements Runnable {

	private int panelWidth;
	public int panelHeight;
	private int FPS;

	public Wall wall;
	private Image background;

    private boolean generationComplete = false;
	boolean loadingNextGeneration = false;
	private double loadedAmount = 0;
    private double count = 0;
    private double average = 0;
    private double topFitness = 0;
    private int genCount = 0;
	
	ArrayList<Bird> birds = new ArrayList<>();
	
	public BirdGame(int width, int height, int fps) {
		panelWidth = width;
		panelHeight = height;
		FPS = fps;
		wall = new Wall(width,width/20,height,300);
		background = loadBackground();
	}
	
	private Image loadBackground() {
		BufferedImage bg = null;
		try {
			bg = ImageIO.read(new File("bg.png"));
			bg = MainFrame.resize(bg, panelWidth, panelHeight);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return bg;
	}
	
	public void run() {
		
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
                if(count >= 100) {
                    step();
                }
            }
        }
	}
	
	private void step() {
		if(!loadingNextGeneration) {
			tick();
			repaint();

		}
		else {
			repaint();
			loadedAmount += 5;
			if(loadedAmount >= 600) {
				loadedAmount = 0;
				loadingNextGeneration = false;
				count = 0;
				average = 0;
			}
		}
		
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
		if(!loadingNextGeneration) {
			g.drawImage(background, 0, 0, null);
			
			g.setColor(Color.white);
			g.fillRect(0, 0, panelWidth, 20);
			g.fillRect(0, panelHeight -20, panelWidth, 20);
			
			for(Bird b: birds) {
				b.render(g);
			}
			wall.render(g);
			
			int fontSize = 30;
			g.setColor(Color.blue);
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
			g.drawString("Top Fitness: "+topFitness, 1700,50);
			g.setColor(Color.black);
			g.drawString("Generation: "+genCount, 1500,50);
		}
		else {

			g.drawImage(background, 0, 0, null);
			
			int fontSize = 60;
			g.setColor(Color.black);
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
			g.drawString("Loading Next Generation", (panelWidth /2) -300,(panelHeight /2)-100);

			g.setColor(Color.white);
			//g.fillRect(0, 0, width, height);
			g.setColor(Color.black);
			g.fillRect((panelWidth /2) -300,(panelHeight /2),600,80);
			g.setColor(Color.green);
			g.fillRect((panelWidth /2) -300,(panelHeight /2),(int) loadedAmount,80);
		}
		g.dispose();
	}
	
	private void tick() {
		int alivecount = 0;
		Bird lastBird = null;
		generationComplete = true;
		for(Bird b: birds) {
			b.tick(this);
			if(b.isAlive()) {
				alivecount++;
				lastBird = b;
				generationComplete = false;
			}
		}
		

		wall.tick(this);
		for(Bird b: birds) {
			if(wall.wallRECT1.intersects(b.birdRECT) || wall.wallRECT2.intersects(b.birdRECT)) {
				b.setDead();
				b.cleared = false;

			}
		}
		boolean allclear = true;
		for(Bird b: birds) {
			b.tick(this);
			if(b.isAlive()) {
				if(!b.cleared) {
					allclear = false;
				}
			}
		}
		
		if(generationComplete || alivecount == 1 && lastBird.cleared) {
			startNextGeneration();
		}

		else if(allclear) {

			startNextGeneration();
		}
		if(alivecount == 1 && !loadingNextGeneration) {
			lastBird.RENDER_INPUTS_ON = true;
			lastBird.birdRECT.RENDER_ON = true;
			
		}
		
	}
	
	public void addBird(Bird b) {
		birds.add(b);
	}
	
	private void startNextGeneration() {
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		loadingNextGeneration = true;
		generateNewGeneration();
		generationComplete = false;
		wall.x = panelWidth;
		wall.gapy = 300;
		wall.tick(this);
	}
	
	private void generateNewGeneration() {
		ArrayList<Bird> newGenBIRDS = new ArrayList<>();
		genCount++;

		int highscore1 = 0;
		int highscore2 = 0;
		int highscore3 = 0;
		int highscore4 = 0;

		Bird fittestBird1 = null;
		Bird fittestBird2 = null;
		Bird fittestBird3 = null;
		Bird fittestBird4 = null;

		for(Bird b: birds) {
			if(b.calculateFitness()>highscore1 && !b.cleared) {
				fittestBird1 = b;
				highscore1 = fittestBird1.calculateFitness();
			}
			else if(b.calculateFitness()>highscore2 && !b.cleared) {
				fittestBird2 = b;
				highscore2 = fittestBird2.calculateFitness();
				
			}
			else if(b.calculateFitness()>highscore3 && !b.cleared) {
				fittestBird3 = b;
				highscore3 = fittestBird3.calculateFitness();
				
			}
			else if(b.calculateFitness()>highscore4 && !b.cleared) {
				fittestBird4 = b;
				highscore4 = fittestBird4.calculateFitness();
				
			}
			else if(b.cleared) {
				b.reset();
				newGenBIRDS.add(b);
			}

		}
		if(highscore1 > topFitness) {
			topFitness = highscore1;
		}

		fittestBird1.reset();
		newGenBIRDS.add(fittestBird1);
		fittestBird2.reset();
		newGenBIRDS.add(fittestBird2);
		fittestBird3.reset();
		newGenBIRDS.add(fittestBird3);
		fittestBird4.reset();
		newGenBIRDS.add(fittestBird4);
		
		int parentsize = newGenBIRDS.size();
		for(int i=0; i < parentsize+2; i++) {
			
			Random rand = new Random();
			
			int index = rand.nextInt(newGenBIRDS.size());
			int index2 = (index+1)%newGenBIRDS.size();

			Bird parent1 = newGenBIRDS.get(index);
			Bird parent2 = newGenBIRDS.get(index2);
			if(parent1.equals(parent2)) {
				System.out.println("SAMMMME");
			}
			Bird spliced = new Bird(MainFrame.ImageFly(), MainFrame.ImageFlap(), 500, "CHILD");
			spliced.setWeights(Matrix.SpliceWeights(parent1.net.weights, parent2.net.weights,rand));
			
			if(parent1.title.equals("DEITY") && parent2.title.equals("PARENT") || parent2.title.equals("DEITY") && parent1.title.equals("PARENT") ) {
				spliced.title = "DEMIGOD";
			}
			newGenBIRDS.add(spliced);
			
		}
			
		for(int i=0; i < 3; i++) {
			Random rand = new Random();
			Bird newbie = new Bird(MainFrame.ImageFly(), MainFrame.ImageFlap(), 500,"BASTARD");
			newbie.setWeights(Matrix.SpliceWeights(fittestBird1.net.weights, newbie.net.weights,rand));
			
			Bird newbie2 = new Bird(MainFrame.ImageFly(), MainFrame.ImageFlap(), 500,"BASTARD");
			newbie.setWeights(Matrix.SpliceWeights(fittestBird2.net.weights, newbie2.net.weights,rand));
			
			newGenBIRDS.add(newbie);
			newGenBIRDS.add(newbie2);
		}
		
		int num = 36-newGenBIRDS.size();
		for(int i=0; i < num; i++) {
			newGenBIRDS.add(new Bird(MainFrame.ImageFly(), MainFrame.ImageFlap(), 500,"NEWCOMER"));
		}
		
		while(newGenBIRDS.size() > 36) {
			Random rand = new Random();
			int index = rand.nextInt(newGenBIRDS.size());
			if(!newGenBIRDS.get(index).title.equals("DEITY")) {
				newGenBIRDS.remove(index);
			}
		}
		
		birds = newGenBIRDS;
		
	}
}
