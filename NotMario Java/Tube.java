import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Tube extends Sprite
{
    //width and height of tube.png
    static BufferedImage tube_image = null;
    Model model;

    Tube(int x, int y, Model m)
    {
        this.x = x;
        this.y = y;
        this.w = 55;
        this.h = 400;
        model = m;
        type = "tube";
        loadTube();
    }

    void loadTube()
    {
        if(Tube.tube_image == null)
            Tube.tube_image = View.loadImage("tube.png");
    }

    @Override
    boolean isTube()
    {
        return true;
    }

    void update()
    {
        //tube remains tubular
    }

    //unmarshal tube list
    public Tube(Json ob, Model m)
    {
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = 55;
        h = 400;
        model = m;
        type = "tube";
        loadTube();
    }

    //marshal tube
    Json Marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

    //checks if we have clicked on top of a tube
    public boolean isThereATube(int mouse_x, int mouse_y)
    {
        //System.out.println("checking " + mouse_x + "x and " + mouse_y + "y");
        if(mouse_x < x)
            return false;
        else if(mouse_x > x+w)
            return false;
        else if(mouse_y < y)
            return false;
        else if(mouse_y > y+h)
            return false;
        else 
            return true;
    }

    void drawSprite(Graphics g)
    {
        g.drawImage(tube_image, x - model.mario.x + model.mario.marioLocation, y, null);
    }

}