package GameObjects;
import javax.media.opengl.*;

public class Ball extends GameObjects{
  double m, dx, dy = m * dx;
  boolean move;

  public Ball(int textureIndex, int x, int y, GL gl) {
    super(textureIndex, x, y, gl);
  }

  public void draw() {
    super.draw();
    move();
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

    if (x >= 530 || x <= -530)
      dx = -dx;
    if (y >= 280 || y <= -280)
      dy = -dy;
  }

  public static boolean collides(Ball ball1, Ball ball2) {
    return Math.abs(ball1.x - ball2.x) <= 70 && Math.abs(ball1.y - ball2.y) <= 70;
  }
}
