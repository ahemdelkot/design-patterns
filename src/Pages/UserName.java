package Pages;
import javax.media.opengl.GL;
import java.util.*;

public class UserName {
  GL gl;
  ArrayList<Integer> input;
  int[] textures;
  public boolean takeingInput;

  public UserName(GL gl, ArrayList<Integer> input, int[] textures) {
    this.gl = gl;
    this.input = input;
    this.textures = textures;
  }

  public void printInput() {
    draw(40, 0, 0, 1200,700);

    int x = -200;
    for (int i : input) {
      draw(i, x, 0);

      x += 45;
    }
  }

  public void draw(int index, double x, double y) {
    // draw the character
    draw(index, x, y, 40, 40);
  }

  private void draw(int index, double x, double y, double width, double height) {
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
  }
}
