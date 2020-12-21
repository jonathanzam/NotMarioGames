import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JButton;
import java.awt.Color;

class View extends JPanel
{
	Model model;
	Controller controller;

	View(Controller c, Model m)
	{
		model = m;
		c.setView(this);
	}

	public static BufferedImage loadImage(String filename)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(filename));
		} catch(Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return image;
	}

	public void paintComponent(Graphics g)
	{
		//new window
		g.setColor(new Color(128,255,255));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		//draw sprites
		for(int i = 0; i < model.sprites.size(); i++)
		{
			model.sprites.get(i).drawSprite(g);
		}
		//ground
		g.setColor(new Color(0,100,0));
		g.fillRect(0, 400, this.getWidth(), 400);
	}
}
