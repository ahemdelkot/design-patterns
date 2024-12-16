package GameObjects;
import javax.media.opengl.*;

public abstract class GameObjects {
  final int width = 80, height = 80;
  int textureIndex;
  public double x, y;
  GL gl;

  public GameObjects(int textureIndex, double x, double y, GL gl) {
    this.textureIndex = textureIndex;
    this.x = x;
    this.y = y;
    this.gl = gl;
  }

  public void draw(){
    draw(this.textureIndex, this.x, this.y, this.width, this.height);
  }
  
  public void draw(int index, double x, double y, double width, double height) {
    gl.glEnable(GL.GL_BLEND);
    gl.glBindTexture(GL.GL_TEXTURE_2D, index); // Turn Blending On

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
  }

  public static double distance(GameObjects ball1, GameObjects ball2) {
    double d = Math.pow(ball1.x - ball2.x, 2) + Math.pow(ball1.y - ball2.y, 2);
    return d;
  }
}
