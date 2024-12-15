
import Texture.TextureReader;

import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import java.util.*;
import javax.media.opengl.glu.GLU;

public class Timer implements GLEventListener {

  String textureNames[] = { "00.png", "01.png", "02.png", "03.png", "04.png", "05.png", "06.png", "07.png", "08.png","09.png", "..png" };
  TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
  int textures[] = new int[textureNames.length];
  protected String assetsFolderName = "Assets//Numbers";
  GL gl;

  public void init(GLAutoDrawable gld) {
    this.gl = gld.getGL();
    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glGenTextures(textureNames.length, textures, 0);
    gl.glViewport(-300, 300, -300, -300);
    gl.glOrtho(-300, 300, -300, 300, -1, 1);

    for (int i = 0; i < textureNames.length; i++) {
      try {
        texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
        new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(),
            GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
      } catch (IOException e) {
        System.out.println(e);
        e.printStackTrace();
      }
    }

  }

  int timer = 0;
  double idx1 = 0, idx2 = 0, idx3 = 0, idx4 = 0;

  public void display(GLAutoDrawable gld) {
    this.gl = gld.getGL();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    timer++;
    gl.glLoadIdentity();
    // when timer get the animator fps it's increment the seconds by 1
    if (timer >= 30) {
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
    // if (idx4 > 43){
    // idx2++;
    // }
    gl.glPushMatrix();
    gl.glScaled(0.1, 0.1, 1);
    Draw(gl, 0, 0, 10);
    Draw(gl, 0, 0.2, 10);
    Draw(gl, 0.4, 0.1, (int) idx1);
    Draw(gl, 0.2, 0.1, (int) idx2);
    Draw(gl, -0.2, 0.1, (int) idx3);
    Draw(gl, -0.4, 0.1, (int) idx4);
    gl.glPopMatrix();
  }

  public void Draw(GL gl, double x, double y, int idx) {
    gl.glEnable(GL.GL_BLEND);

    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[idx]);
    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

    gl.glPushMatrix();
    gl.glTranslated(x, y, 0);
    gl.glScaled(0.09, 0.09, 1);

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

    gl.glPopMatrix();
    gl.glDisable(GL.GL_BLEND);

  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
  }
}
