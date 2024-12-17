package Pages;
import javax.media.opengl.GL;

public class Timer {
  private GL gl;
  private int[] textures;
  private int timer, fps;
  private int idx1 = 0, idx2 = 0, idx3 = 0, idx4 = 0;
  private boolean addFlag = true;

  public Timer(int fps, int[] textures, GL gl) {
    this.gl = gl;
    this.textures = textures;
    this.fps = fps;
  }

  public void add() {
    if(addFlag) timer++;
    setTime();
  }

  private void setTime() {
    if (timer >= fps) {
      idx1++;
      timer = 0;
    }
    if (idx1 > 9) {
      idx2++;
      idx1 = 0;
    }
    if (idx2 >= 6) {
      idx3++;
      idx2 = 0;
    }
    if (idx3 > 9) {
      idx4++;
    }
  }

  public void draw() {
    draw(45, 0, 290, 20, 20);
    draw(45, 0, 265, 20, 20);
    draw(idx4, -80, 280, 20, 20);
    draw(idx3, -40, 280, 20, 20);
    draw(idx2, 40, 280, 20, 20);
    draw(idx1, 80, 280, 20, 20);
  }

  private void draw(int index, double x, double y, double width, double height) {
    gl.glEnable(GL.GL_BLEND);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]); // Turn Blending On

    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f); // bottom left point
    gl.glVertex3f((float) (x - width), (float) (y - height), -1.0f);
    gl.glTexCoord2f(1.0f, 0.0f); // top left point
    gl.glVertex3f((float) (x + width), (float) (y - height), -1.0f);
    gl.glTexCoord2f(1.0f, 1.0f); // top right point
    gl.glVertex3f((float) (x + width), (float) (y + height), -1.0f);
    gl.glTexCoord2f(0.0f, 1.0f); // bottom right point
    gl.glVertex3f((float) (x - width), (float) (y + height), -1.0f);
    gl.glEnd();

    gl.glDisable(GL.GL_BLEND);
  }

  public void stop() {
    addFlag = false;
  }

  public void reset() {
    idx1 = 0;
    idx2 = 0;
    idx3 = 0;
    idx4 = 0;
  }
}