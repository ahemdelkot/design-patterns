package Pages;
import javax.media.opengl.*;
import java.io.*;
// import javax.swing.*;
// import com.sun.opengl.util.*;
// import java.awt.*;
// import java.awt.event.*;

public class Levels {
  int index;
  int textures[];
  GL gl;
  int[] mouse;
  Game game;
  public int levelChosen;
  UserName userName;
  int[] flag;

  public Levels(int[] textures, int[] mouse, int[] flag, UserName userName, Game game, GL gl) throws FileNotFoundException {
    this.textures = textures;
    this.gl = gl;
    this.mouse = mouse;
    this.game = game;
    this.userName = userName;
    this.flag = flag;
  }

  public void draw() {
    if (levelChosen == 0) {
      drawLevels();
    }
    else {
      flag[0] = 5;
    }
  }

  private void drawLevels() {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    drawBackGround();
    String heading = "levels";
    for (int i = 0, y = 280, x = -100; i < heading.length(); i++) {
      char ch = heading.charAt(i);
      if (ch != ' ') {
        draw(ch - 'a' + 10, x, y);
      }

      x += 40;
    }

    // easy button
    draw(50, 0, 100, 260, 100);
    
    // medium button
    draw(51, 0, -50, 260, 100);
    
    //  button
    draw(52, 0, -200, 260, 100);

    // handle hover mouse
    if (mouse[0] > -130 && mouse[0] < 130) {
      if (mouse[1] > 50 && mouse[1] < 150) {
        draw(53, 0, 100, 260, 100);
      }
      if (mouse[1] > -100 && mouse[1] < 0) {
        draw(54, 0, -50, 260, 100);
      }
      if (mouse[1] > -250 && mouse[1] < -150) {
        draw(55, 0, -200, 260, 100);
      }
    }
    
    // back button
    draw(57, -575, 325, 50, 50);
  }

  private void draw(int index, double x, double y) {
    draw(index, x, y, 40, 40);
  }

  private void drawBackGround() {
    draw(40, 0, 0, 1200, 700);
  }

  private void draw(int index, double x, double y, double width, double height) {
    // draw the character
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