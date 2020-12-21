import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Mario extends Sprite
{
	int prevX, prevY;
    int marioImageNum;
    Controller controller;
    double vertVelocity;
    int airTime;
    int marioLocation;
    boolean flip;
    static BufferedImage[] mario_images = null;
	
	Mario(int x, int y)
	{
		this.x = x;
        this.y = y;
        this.type = "mario";
		this.w = 60;
		this.h = 95;
		marioImageNum = 0;
        vertVelocity = 12.0;
        airTime = 0;
        marioLocation = 150;
        flip = false;

        if(mario_images == null)
        {
            mario_images = new BufferedImage[5];
            mario_images[0] = View.loadImage("mario1.png");
            mario_images[1] = View.loadImage("mario2.png");
            mario_images[2] = View.loadImage("mario3.png");
            mario_images[3] = View.loadImage("mario4.png");
            mario_images[4] = View.loadImage("mario5.png");
        }

	}

    @Override
    boolean isMario()
    {
        return true;
    }

	void jump()
	{
        //if in air too long, bring us back down quickly
        if(airTime < 8)
            vertVelocity = -25;
    }
    
    void saveCoordinates()
    {
        prevX = x;
        prevY = y;
    }

    //gets mario out of tubes using previous coordinates
    void getOutOfTube(Tube t)
    {
        if(prevX + w <= t.x)
            x = t.x - w;
        else if(prevX >= t.x+t.w)
            x = t.x + t.w;
        else if(prevY < t.y)
        {
            y = t.y - h;
            airTime = 0;
        }
        else if(prevY >= t.y + t.h)
        {
            vertVelocity = 0;
            airTime = 0;
            y = t.y + t.h;
        }
    }

	void update()
	{
		vertVelocity += 5.5;
        y+= vertVelocity;
        airTime++;

		if(y>400-h)
		{
			vertVelocity = 0;
            y = 400-h;
            airTime = 0;
        }
        
        if(y<0)
            y = 0;

		if(marioImageNum>4)
			marioImageNum = 0;
    }
    
    void drawSprite(Graphics g)
    {
        if(flip)
            g.drawImage(mario_images[marioImageNum], marioLocation+w, y, -w, h, null);
        else
            g.drawImage(mario_images[marioImageNum], marioLocation, y, w, h, null); 
    }
}