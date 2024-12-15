import javax.media.opengl.GL;

public class Timer2 {
  GL gl;
  int[] textures;
  int timer; int fps;
  int idx1 = 0, idx2 = 0, idx3 = 0, idx4 = 0;

  public Timer2(int fps, int[] textures, GL gl) {
    this.gl = gl;
    this.textures = textures;
    this.fps = fps;
  }

  public void addSecond() {
    timer++;
    setTime();
  }

  private void setTime(){
    if (timer >= 60) {
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

    draw(gl, 0, 0, 10);
    draw(gl, 0, 0.2, 10);
    draw(gl, 0.4, 0.1, idx1);
    draw(gl, 0.2, 0.1, idx2);
    draw(gl, -0.2, 0.1, idx3);
    draw(gl, -0.4, 0.1, idx4);
  }

  public void draw(GL gl, double x, double y, int idx) {
    gl.glEnable(GL.GL_BLEND);

    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[idx]);
    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex3f(-1.0f, -1.0f, -1.0f);
    gl.glTexCoord2f(1.0f, 0.0f);
    gl.glVertex3f(1.0f, -1.0f, -1.0f);
    gl.glTexCoord2f(1.0f, 1.0f);
    gl.glVertex3f(1.0f, 1.0f, -1.0f);
    gl.glTexCoord2f(0.0f, 1.0f);
    gl.glVertex3f(-1.0f, 1.0f, -1.0f);
    gl.glEnd();

  }
}