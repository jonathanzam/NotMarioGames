import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements ActionListener, MouseListener, KeyListener
{
	View view;
	Model model;
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean spaceBar;
	boolean ctrl;
	boolean addTubesEditor = false;
	boolean addGoombasEditor = false;

	Controller(Model m)
	{
		model = m;
	}

	void setView(View v)
	{
		view = v;
	}

	public void actionPerformed(ActionEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		if(addTubesEditor)
			model.addTube(e.getX() + model.mario.x - model.mario.marioLocation, e.getY());
		if(addGoombasEditor)
			model.addGoomba(e.getX() + model.mario.x - model.mario.marioLocation, e.getY());
	}

	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = true; break;
			case KeyEvent.VK_LEFT: keyLeft = true; break;
			case KeyEvent.VK_UP: keyUp = true; break;
			case KeyEvent.VK_DOWN: keyDown = true; break;
			case KeyEvent.VK_SPACE: spaceBar = true; break;
			case KeyEvent.VK_CONTROL: ctrl = false; break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_DOWN: keyDown = false; break;
			case KeyEvent.VK_SPACE: spaceBar = false; break;
			case KeyEvent.VK_CONTROL: ctrl = true; break;
		}

		char c = e.getKeyChar();

		//save map
		if(c == 's')
		{
			//System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSS");
			model.marshal().save("map.json");
			System.out.println("Saved map.json");
		}

		// //load map
		// if(c == 'l')
		// {
		// 	System.out.println("loading map...");
		// 	Json j = Json.load("map.json");
		// 	model.unmarshal(j);
		// 	System.out.println("map.json loaded!");
		// }
		
		//allow for adding tubes for troubleshooting
		if(c == 't')
		{
			System.out.println("Tube editing");
			addTubesEditor = !addTubesEditor;
			addGoombasEditor = false;
		}

		//add goombas for troubleshooting
		if(c == 'g')
		{
			System.out.println("Goomba editing");
			addGoombasEditor = !addGoombasEditor;
			addTubesEditor = false;
		}
			
	}

	public void keyTyped(KeyEvent e)
	{
	}

	void update()
	{
		model.mario.saveCoordinates();

		if(keyRight)
		{
			model.mario.x+=8;
			model.mario.marioImageNum++;
			model.mario.flip=false;
		}
		if(keyLeft)
		{
			model.mario.x-=8;
			model.mario.marioImageNum++;
			model.mario.flip = true;
		}
		if(keyUp || spaceBar)
		{
			model.mario.jump();
			model.mario.marioImageNum++;
		}
		if(ctrl)
		{
			model.addFireball(model.mario.x, model.mario.y);
			ctrl = !ctrl;
		}
	}
}
