package Pages;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;

public class HighScores {
  final int textures[];
  ArrayList<Score> scores = new ArrayList<>();
  GL gl;

  public HighScores(GL gl, int[] textures) throws FileNotFoundException {
    this.gl = gl;
    this.textures = textures;
    getScores();
  }

  public void draw(int index, double x, double y) {
    int width = 40;
    if (index == 1) {
      width = 20;
    } else if (index == 18) {
      width = 10;
    }
    draw(index, x, y, width, 40);
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

  private void getScores() throws FileNotFoundException {
    Scanner in = new Scanner(new File("data\\score.txt"));
    while (in.hasNext()) {
      scores.add(new Score(in.next(), in.next()));
    }
    in.close();
  }

  public void printScores() {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    draw(40, 0, 0, 1200, 700);

    String heading = "high scores";
    for (int i = 0, y = 280, x = -200; i < heading.length(); i++) {
      char ch = heading.charAt(i);
      if (ch != ' ') {
        draw(ch - 'a' + 10, x, y);
      }

      x += 40;
    }

    for (int i = 0, y = 150; i < scores.size(); i++) {
      // for name printing names
      String name = scores.get(i).name;
      for (int j = 0, x = -240; j < name.length(); j++) {
        char ch = name.charAt(j);
        draw(ch - 'a' + 10, x, y);
        x += 40;
      }

      // for score printing score
      String score = scores.get(i).score;
      for (int j = score.length() - 1, x = 240; j >= 0; j--) {
        char ch = score.charAt(j);
        draw(ch - '0', x, y);
        x -= 40;
      }
      y -= 60;
    }
    draw(37, -575, 325);
  }
}

class Score {
  String name;
  String score;

  public Score(String name, String score) {
    this.name = name;
    this.score = score;
  }
}
