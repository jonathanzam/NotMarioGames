import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Fireball extends Sprite
{
    int prevX, prevY;
    Model model;
    double vertVelocity;
    int bounceTime;
    static BufferedImage image;

    public Fireball(int x, int y, Model m)
    {
        this.x = x;
        this.y = y;
        this.w = 47;
        this.h = 47;
        this.model = m;
        vertVelocity = 5;
        bounceTime = 0;
        this.type = "fireball";
        if(image == null)
            image = View.loadImage("fireball.png");
    }

    public Fireball(Json ob, Model m)
    {
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = 47;
        h = 47;
        vertVelocity = 5;
        bounceTime = 0;
        type = "fireball";
        if(image == null)
            image = View.loadImage("fireball.png");
    }

    @Override
    boolean isFireball()
    {
        return true;
    }

    void update()
    {
        //vertVelocity+=2.5;
        if(y > 400-h)
        {
            vertVelocity = -vertVelocity;
            //y += vertVelocity;
        }
        if(y < 400 - model.mario.h)
        {
            //System.out.println("above mario");
            vertVelocity = 5.5;
            y += vertVelocity;
        }
        y+=vertVelocity;
        x+=8;
        bounceTime++;
    }

    void drawSprite(Graphics g)
    {
        if(model.mario.flip)
            g.drawImage(image, x-model.mario.x + model.mario.marioLocation, y, null);
        else
        g.drawImage(image, x-model.mario.x + model.mario.marioLocation, y, null);
    }
}
