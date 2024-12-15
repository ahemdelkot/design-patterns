package GameObjects;

import javax.media.opengl.*;

public class Ball extends GameObjects {
  private double m, dx, dy = m * dx;
  private boolean move;
  private Hand hand1;

  public Ball(int textureIndex, int x, int y, Hand hand1, GL gl) {
    super(textureIndex, x, y, gl);
    this.hand1 = hand1;
  }

  public void draw() {
    super.draw();
    checkCollide();
    move();
  }

  private void checkCollide() {
    if (x >= 530 || x <= -530)
      dx = -dx;
    if (y >= 280 || y <= -280)
      dy = -dy;

    if(GameObjects.collides(this, hand1)){
      dy = -dy;
      dx = -dx;
    }
  }

  public void method(double thatX, double thatY) {
    // System.out.println("method");
    move = true;
    // m = (thatY - this.y) / (thatX - this.x);

    if (thatX > this.x + 40 || thatX < this.x - 40) {
      m = (thatX - this.x) / (thatY - this.y);
      dx = thatX > this.x ? -5 : 5;
      dy = m * dx;
    } else if (thatY < this.y - 40) {
      m = (thatY - this.y) / (thatX - this.x);
      dy = thatY > this.y ? 5 : -5;
      dx = dy / m;
    } else {
      m = (thatY - this.y) / (thatX - this.x);
      dy = thatY > this.y ? -5 : 5;
      dx = dy / m;
    }

    System.out.println(m);
  }

  public void move() {
    if (!this.move)
      return;

    x += dx;
    y += dy;
  }
}
