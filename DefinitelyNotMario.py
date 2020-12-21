import pygame
import time

from pygame.locals import*
from time import sleep

class Sprite():
    def __init__(self, setX, setY, setImage):
        self.x = setX
        self.y = setY
        self.imagesrc = pygame.image.load(setImage)


class Mario(Sprite):
    def __init__(self, setX, setY, image_url):
        super(Mario, self).__init__(setX, setY, image_url)
        self.w = 60
        self.h = 95
        self.marioImageNum = 0
        self.vertVelocity = 12.0
        self.airTime = 0
        self.marioLocation = 150
        self.flip = False

    def saveCoordinates(self):
        self.prevX = self.x
        self.prevY = self.y

    #jumps
    def jump(self):
        #self.airTime += 1
        if self.airTime < 8:
            self.vertVelocity = -25

    #get out of tube
    def getOutOfTube(self, tube):
        if isinstance (tube, Tube):
            if self.prevX + self.w <= tube.x:
                self.x = tube.x - self.w
            elif self.prevX >= tube.x + tube.w:
                self.x = tube.x + tube.w
            elif self.prevY < tube.y:
                self.y = tube.y - self.h
                self.airTime = 0
            elif self.prevY >= tube.y + tube.h:
                self.vertVelocity = 0
                self.airTime = 0
                self.y = tube.y + tube.h

    #increment air time and gravity
    #cycles through mario image number
    def update(self):
        self.vertVelocity += 5.5
        self.y += self.vertVelocity
        self.airTime += 1

        if self.y > 400 - self.h:
            self.vertVelocity = 0
            self.y = 400 - self.h
            self.airTime = 0

        if self.y < 0:
            self.y = 0

        if self.marioImageNum > 4:
            self.marioImageNum = 0

class Goomba(Sprite):
    def __init__(self, setX, setY):
        super(Goomba, self).__init__(setX, setY, "goomba.png")
        self.w = 37
        self.h = 45
        self.onFire = False
        self.collisionCount = 0
        self.vertVelocity = 12.0
        self.burningTime = 0

    def saveCoordinates(self):
        self.prevX = self.x
        self.prevY = self.y

    #bounce off tube
    def bounceOffTube(self, tube):
        #print ("bouncing")
        if self.x + self.w <= tube.x:
            self.x = tube.x - self.w
        elif self.prevX >= tube.x + tube.w:
            self.x = tube.x + tube.w

        self.collisionCount += 1

    #bounces left and right between tubes and catches fire
    def update(self):
        self.saveCoordinates()

        if self.onFire:
            self.imagesrc = pygame.image.load("goomba_fire.png")
            self.burningTime += 1
            return

        if(self.collisionCount%2 == 0):
            self.x += 5
        if(self.collisionCount%2 == 1):
            self.x -=5

        self.y += self.vertVelocity

        if self.y > 400-self.h:
            self.vertVelocity = 0
            self.y = 400 - self.h
        
        if self.y < 0:
            self.y = 0


class FireBall(Sprite):
    def __init__(self, setX, setY):
        super(FireBall, self).__init__(setX, setY, "fireball.png")
        self.w = 47
        self.h = 47
        self.vertVelocity = 5
        self.bounceTime = 0

    #bounces
    def update(self):
        if self.y > 400 - self.h:
            self.vertVelocity = -self.vertVelocity
        if self.y < 305:
            self.vertVelocity = 5.5
            self.y += self.vertVelocity
        
        self.y += self.vertVelocity
        self.x += 6
        self.bounceTime += 1

class Tube(Sprite):
    def __init__(self, setX, setY):
        super(Tube, self).__init__(setX, setY, "tube.png")
        self.w = 55
        self.h = 400

    #stays tube
    def update(self):
        pass


class Model():
    def __init__(self):
        self.dest_x = 0
        self.dest_y = 0
        self.mario = Mario(150, 295, "mario1.png")
        self.sprites = []
        self.sprites.append(self.mario)
        self.sprites.append(Tube(3,47))
        self.sprites.append(Tube(448, 269))
        self.sprites.append(Tube(958, 122))
        self.sprites.append(Goomba(536,355))

    #checks for collisions between sprites
    def collision(self, a, t):
        if(a.x + a.w < t.x):
            return False
        if(a.x > t.x + t.w):
            return False
        if(a.y + a.h < t.y):
            return False
        if(a.y > t.y + t.h):
            return False
        return True

    def update(self):
        #loop over sprites and update
        #if sprite is tube, check collisions
        for i in self.sprites:
            i.update()
            if isinstance (i, Tube):
                if self.collision(self.mario, i):
                    #print("collision")
                    self.mario.getOutOfTube(i)

                #loop for goombas, collide off tubes, remove when on fire long enough
                for g in self.sprites:
                    if isinstance (g, Goomba):
                        if self.collision(g, i):
                            #print("goomba collision")
                            g.bounceOffTube(i)
                        if g.burningTime > 50:
                            self.sprites.remove(g)

                        #check for fireball and remove when colliding with goomba
                        #set goomba on fire
                        for f in self.sprites:
                            if isinstance (f, FireBall):
                                if self.collision(g, f):
                                    g.onFire = True
                                    self.sprites.remove(f)
                
                #remove fireball when colliding with tube
                #works when there isn't a goomba
                for f in self.sprites:
                    if isinstance (f, FireBall):
                        if self.collision(f, i):
                            self.sprites.remove(f)
    #adds fireball to sprite array
    def addFireBall(self, x, y):
        self.sprites.append(FireBall(x, y))


class View():
    def __init__(self, model):
        screen_size = (1000,500)
        self.screen = pygame.display.set_mode(screen_size, 32)
        self.model = model
        self.mario_images = [pygame.image.load("mario1.png"),pygame.image.load("mario2.png"),pygame.image.load("mario3.png"),
                            pygame.image.load("mario4.png"), pygame.image.load("mario5.png")]

    #updates drawn window
    def update(self):
        self.screen.fill([128, 255, 255])

        for sprite in self.model.sprites:
            if isinstance(sprite, Mario):
                self.screen.blit(pygame.transform.flip(self.mario_images[self.model.mario.marioImageNum], self.model.mario.flip, False), (self.model.mario.marioLocation, self.model.mario.y))
            else:
                self.screen.blit(sprite.imagesrc, (sprite.x-self.model.mario.x + self.model.mario.marioLocation, sprite.y))

        pygame.draw.lines(self.screen, (0, 100, 0), False, [(0, 500), (10000, 500)], 200)
        pygame.display.flip()


class Controller():
    def __init__(self, model):
        self.model = model
        self.keep_going = True

    #user controls
    def update(self):
        self.model.mario.saveCoordinates()
        for event in pygame.event.get():
            if event.type == QUIT:
                self.keep_going = False
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE:
                    self.keep_going = False
        keys = pygame.key.get_pressed()
        if keys[K_LEFT]:
            self.model.mario.x -= 5
            self.model.mario.marioImageNum += 1
            self.model.mario.flip = True
        if keys[K_RIGHT]:
            self.model.mario.x += 5
            self.model.mario.marioImageNum += 1
            self.model.mario.flip = False
        if keys[K_UP] or keys[K_SPACE]:
            self.model.mario.jump()
        if keys[K_LCTRL]:
            pygame.key.set_repeat()
            self.model.addFireBall(self.model.mario.x, self.model.mario.y)
            

print("Use the arrow keys to move. Press Esc to quit.")
pygame.init()
m = Model()
v = View(m)
c = Controller(m)
while c.keep_going:
    c.update()
    m.update()
    v.update()
    sleep(0.04)
print("Goodbye")