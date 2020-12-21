import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Goomba extends Sprite
{
    int prevX, prevY;
    static BufferedImage goomba, goombaFire;
    Model model;
    boolean onFire;
    int collisionCount, burningTime;
    double vertVelocity;

    public Goomba(int x, int y, Model m)
    {
        this.x = x;
        this.y = y;
        this.w = 37;
        this.h = 45;
        this.model = m;
        onFire = false;
        collisionCount = 0;
        vertVelocity = 12.0;
        burningTime = 0;
        this.type = "goomba";
        if(goomba == null)
            goomba = View.loadImage("goomba.png");
        if(goombaFire == null)
            goombaFire = View.loadImage("goomba_fire.png");
    }

    public Goomba(Json ob, Model m)
    {
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = 37;
        h = 45;
        model = m;
        onFire = false;
        collisionCount = 0;
        vertVelocity = 12.0;
        burningTime = 0;
        type = "goomba";
        if(goomba == null)
            goomba = View.loadImage("goomba.png");
        if(goombaFire == null)
            goombaFire = View.loadImage("goomba_fire.png");  
    }

    @Override
    boolean isGoomba()
    {
        return true;
    }
    
    void saveCoordinates()
    {
        prevX = x;
        prevY = y;
    }

    void bounceOffTube(Tube t)
    {
        if(prevX + w <= t.x)
            x = t.x - w;
        else if(prevX >= t.x+t.w)
            x = t.x + t.w;

        collisionCount++;
    }

    void update()
    {
        saveCoordinates();
        //bounce goomba off tubes
        if(collisionCount%2 == 0)
            x +=  5;
        if(collisionCount%2==1)
            x -= 5;

        y += vertVelocity;

        //keep goomba on floor
        if(y > 400-h)
        {
            vertVelocity = 0;
            y = 400 - h;
        }
        //just in case goomba starts flying
        if(y < 0)
            y = 0;

        if(onFire)
            burningTime++;
    }

    void drawSprite(Graphics g)
    {
        if(!onFire)
            g.drawImage(goomba, x-model.mario.x + model.mario.marioLocation, y, null);
        else
            g.drawImage(goombaFire, x-model.mario.x + model.mario.marioLocation, y, null);
    }

    Json Marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }
}
