package GameObjects;

import javax.media.opengl.*;

public class Ball extends GameObjects {
  public double m = 1, dx = -5 , dy =  0 *m * dx;
  private boolean move = true;
  private Hand handRight;
  private Hand handLeft;
  boolean flag;
  int[] textures;

  public Ball(int[] textures, int x, int y, Hand handRight, Hand handLeft, GL gl) {
    super(textures[38], x, y, gl);
    this.textures = textures;
    this.handRight = handRight;
    this.handLeft = handLeft;
    handRight.ball = this;
    handLeft.ball = this;
  }

  public void draw() {
    super.draw();
    checkCollapse();
    checkCollide();
    move();
    checkWinner();
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
        // move = false; // ! ///////////////
        handLeft.score++;
      } 
      else if(x < -640){
        x = -90;
        y = 0;
        // move = false; // ! ///////////////
        handRight.score++;
      } 
    }
    if (super.y >= 280 || super.y <= -280) dy = -dy;
    
    double d1 = GameObjects.distance(this, handRight);
    double d2 = GameObjects.distance(this, handLeft);
    if (d1 <= 80 || d2 <= 80) {
      dy = -dy;
      dx = -dx;
    }
  }

  public void checkCollapse(){
    if(distance(this, handRight) <= 75)
      flag = true;
  }

  public void checkWinner() {
    final int SCORE = 5;
    if (handLeft.score < SCORE && handRight.score < SCORE) return;

    int x = handLeft.score == SCORE ? -360 : 120;
    String heading = "you win";
    for (int i = 0, y = 0; i < heading.length(); i++) {
      char ch = heading.charAt(i);
      if (ch != ' ') {
        draw(textures[ch - 'a' + 10], x, y, 40, 40);
      }

      x += 40;
    }
    move = false;
  }

  public void reset() {
    this.x = 0;
    this.y = 0;
  }
}
