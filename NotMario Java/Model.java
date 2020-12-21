import java.util.ArrayList;
import java.util.Iterator;

class Model
{
	ArrayList<Sprite> sprites;
	Mario mario;

	Model()
	{
		sprites = new ArrayList<Sprite>();
		mario = new Mario(150,295);
		sprites.add(mario);
	}

	public void update()
	{
		//check for collisions
		for(int i = 0; i < sprites.size(); i++)
		{
			Sprite s = sprites.get(i);
			s.update();
			//loop through all tubes to see if mario is colliding
			//if collide, get back out
			if(s instanceof Tube)
			{
				Tube t = (Tube)sprites.get(i);
				if(collision(mario, t))
					mario.getOutOfTube(t);

				//loop through tubes to see if Gommba collides
				for(int x = 0; x < sprites.size(); x++)
				{
					Sprite goomba = sprites.get(x);
					if(goomba instanceof Goomba)
					{
						Goomba g = (Goomba)sprites.get(x);
						//if goomba collides with tube, bounces off
						if(collision(g, t))
							g.bounceOffTube(t);
						if(g.burningTime > 100)
							sprites.remove(x);
							
						//loop for fireball collisions
						for(int y = 0; y < sprites.size(); y++)
						{
							Sprite ball = sprites.get(y);
							if(ball instanceof Fireball)
							{
								Fireball f = (Fireball)sprites.get(y);
								//removes fireball if bounces long enough
								if(f.bounceTime > 50)
									sprites.remove(y);
								//removes fireball if collides with tube
								if(collision(f, t))
									sprites.remove(y);
								//burns goomba if collision
								if(collision(f, g))
									g.onFire = true;
							}
						}
					}
				}

			}
		}
	}

	//checks for collisions between sprites
	boolean collision(Sprite a, Sprite t)
	{
		if(a.x+a.w<t.x)
			return false;
		if(a.x>t.x+a.w)
			return false;
		if(a.y + a.h < t.y)
			return false;
		if(a.y >= t.y + t.h)
			return false;

		return true;
	}

	//add tube to screen
	public void addTube(int mx, int my)
	{
		Tube t = new Tube(mx, my, this);
		boolean tubeAlreadyExists = false;

		for(int i = 0; i < sprites.size(); i++)
		{
			if(sprites.get(i) instanceof Tube)
			{
				Tube temp = (Tube)sprites.get(i);
				if(temp.isThereATube(mx, my))
				{
					sprites.remove(temp);
					tubeAlreadyExists = true;
					break;
				}
			}
		}
		if(!tubeAlreadyExists)
			sprites.add(t);
	}

	//add goomba
	public void addGoomba(int mx, int my)
	{
		Goomba g = new Goomba(mx, my, this);
		sprites.add(g);
	}

	//add fireball
	public void addFireball(int mx, int my)
	{
		Fireball f = new Fireball(mx, my, this);
		sprites.add(f);
	}

	//unmarshal sprites
	void unmarshal(Json ob)
	{
		sprites = new ArrayList<Sprite>();
		sprites.add(mario);
		Json jsonList = ob.get("sprites");
		Json tubeList = jsonList.get("tubes");
		Json goombaList = jsonList.get("goombas");
		//add Tubes into sprites
		for(int i = 0; i < tubeList.size(); i++)
		{
			sprites.add(new Tube(tubeList.get(i), this));
		}
		//add Goombas into sprites
		for(int i = 0; i < goombaList.size(); i++)
		{
			sprites.add(new Goomba(goombaList.get(i), this));
		}
	}
	
	//marshal sprites
	Json marshal()
	{
		Json ob = Json.newObject();
		Json spritesOb = Json.newObject();
		Json temp = Json.newList();
		ob.add("sprites", spritesOb);
		spritesOb.add("tubes", temp);
		for(int i = 0; i < sprites.size(); i++)
		{
			if(sprites.get(i).isTube())
			{
				Tube t = (Tube)sprites.get(i);
				temp.add(t.Marshal());
			}
		}

		temp = Json.newList();
		spritesOb.add("goombas", temp);
		for(int i = 0; i < sprites.size(); i++)
		{
			if(sprites.get(i).isGoomba())
			{
				Goomba g = (Goomba)sprites.get(i);
				temp.add(g.Marshal());
			}
		}

		return ob;
	}
}


