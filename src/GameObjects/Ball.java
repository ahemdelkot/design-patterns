package GameObjects;

import javax.media.opengl.*;

public class Ball extends GameObjects {
  public double m = 1, dx = 2 , dy = m * dx;
  private boolean move = true;
  private Hand hand1;
  private Hand hand2;
  boolean flag;

  public Ball(int textureIndex, int x, int y, Hand hand1, Hand hand2, GL gl) {
    super(textureIndex, x, y, gl);
    this.hand1 = hand1;
    this.hand2 = hand2;
    hand1.ball = this;
    hand2.ball = this;
  }

  public void draw() {
    super.draw();
    checkCollapse();
    checkCollide();
    move();
  }

  public void move() {
    if (!this.move)
      return;

    super.x += dx;
    super.y += dy;
  }

  private void checkCollide() {
    
    if (super.x >= 530 || super.x <= -530){
      if (y > 135 || y < -90) {
        dx = -dx;
      }
      if(x > 640){
        x = 90;
        y = 0;
        // move = false;
        hand2.score++;
        System.out.println(hand1.score + " " + hand2.score);
      } 
      else if(x < -640){
        x = -90;
        y = 0;
        // move = false;
        hand1.score++;
        System.out.println(hand1.score + " " + hand2.score);
      } 
    }
    if (super.y >= 280 || super.y <= -280) dy = -dy;
    
    double d1 = GameObjects.distance(this, hand1);
    double d2 = GameObjects.distance(this, hand2);
    if (d1 <= 80 || d2 <= 80) {
      dy = -dy;
      dx = -dx;
    }
  }

  public void checkCollapse(){
    if(distance(this, hand1) <= 75)
      flag = true;
  }
}
