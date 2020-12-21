import java.awt.Graphics;

abstract class Sprite 
{
    int x,y;
    int w,h;
    String type;

    abstract void update();
    abstract void drawSprite(Graphics g);

    boolean isTube()    {   return false;   }
    boolean isMario()    {   return false;   }
    boolean isGoomba()    {   return false;   }
    boolean isFireball()    {   return false;   }

}
