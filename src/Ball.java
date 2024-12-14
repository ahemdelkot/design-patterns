import javax.media.opengl.*;
import javax.swing.*;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.*;
import Texture.TextureReader;
import javax.media.opengl.glu.GLU;

public class Ball {
  final int width = 80, height = 80;
  int[] textures;
  double x, y;
  int index;
  GL gl;
  double m, dx, dy = m * dx;
  boolean move;

  public Ball(int[] textures, int index, int x, int y, GL gl) {
    this.textures = textures;
    this.index = index;
    this.gl = gl;
    this.x = x;
    this.y = y;
  }

  public void draw() {
    gl.glEnable(GL.GL_BLEND);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]); // Turn Blending On

    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f); // bottom left point
    gl.glVertex3f((float) (x - 0.5 * width), (float) (y - 0.5 * height), -1.0f);
    gl.glTexCoord2f(1.0f, 0.0f); // top left point
    gl.glVertex3f((float) (x + 0.5 * width), (float) (y - 0.5 * height), -1.0f);
    gl.glTexCoord2f(1.0f, 1.0f); // top right point
    gl.glVertex3f((float) (x + 0.5 * width), (float) (y + 0.5 * height), -1.0f);
    gl.glTexCoord2f(0.0f, 1.0f); // bottom right point
    gl.glVertex3f((float) (x - 0.5 * width), (float) (y + 0.5 * height), -1.0f);
    gl.glEnd();

    gl.glDisable(GL.GL_BLEND);

    move();
  }

  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;

    // if (this.x < 0) this.x = 0;
    if (this.x > 530)
      this.x = 530;
    if (this.y > 280)
      this.y = 280;
    if (this.y < -280)
      this.y = -280;
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
