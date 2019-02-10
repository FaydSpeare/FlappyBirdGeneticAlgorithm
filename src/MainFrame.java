import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainFrame extends JFrame{
	
	public MainFrame(int width, int height, JPanel game) {
		this.setSize(width, height+70);
		this.add(game);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		if(game instanceof BirdGame) {
			this.setLocation(0,0);
			this.setTitle("BirdGame");
		}
		else if(game instanceof InfoPanel) {
			this.setLocation(1700,0);
			this.setTitle("Data Panel");
		}
	}
	
	public static void main(String[] args) {

		int width = 1700;
		int height = 1000;
		
		int birds = 0;
		ArrayList<Bird> B = new ArrayList<Bird>();
		while(birds<40) {
			B.add(new Bird(ImageFly(), ImageFlap(), 500,"NEWCOMER"));
			birds++;
		}

		BirdGame game = new BirdGame(width, height, 50);
		
		for(Bird b: B) {
			game.addBird(b);
		}
		
		MainFrame main = new MainFrame(width,height,game);
		
		int IW = 1000;
		int IH = 12*140;
		
		InfoPanel info = new InfoPanel(IW,IH,game,60);
		MainFrame main2 = new MainFrame(IW,IH,info);
		
        Thread t1 = new Thread(game);
        Thread t2 = new Thread(info);
        
       
        t2.start();
        t1.start();
        
		

	}
	
	public static Image ImageFly() {
		int resizer = 30;
		BufferedImage fly = null;
		try {
			fly = ImageIO.read(new File("BlueBird.png"));
			fly = fly.getSubimage(0,0, 150,90);
			fly = resize(fly, resizer*3, resizer*2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return fly;
	}
	
	public static Image ImageFlap() {
		int resizer = 30;
		BufferedImage flap = null;
		try {
			flap = ImageIO.read(new File("BlueBird.png"));
			flap = flap.getSubimage(300,0, 150,90);
			flap = resize(flap, resizer*3, resizer*2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return flap;
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage resizedImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = resizedImage.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return resizedImage;
	}  
}
