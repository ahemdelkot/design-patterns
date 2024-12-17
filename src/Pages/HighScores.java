package Pages;

import javax.media.opengl.*;
import java.io.*;
import java.util.*;

public class HighScores {
  final int textures[];
  ArrayList<Score> scores = new ArrayList<>();
  GL gl;
  public boolean getScores = false;

  public HighScores(GL gl, int[] textures) throws FileNotFoundException {
    this.gl = gl;
    this.textures = textures;
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
    scores.clear();
    try (Scanner in = new Scanner(new File("data\\score.txt"))) {
      while (in.hasNext()) {
        scores.add(new Score(in.next(), in.next()));
      }
    }
  }

  public void printScores() {
    if (!getScores) {
      try {
        getScores();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      getScores = true;
    }

    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    draw(40, 0, 0, 1200, 700);

    // using the Quad as a background for "high scores" title
    gl.glColor3f(1, 1, 1);
    gl.glBegin(GL.GL_QUADS);
    gl.glVertex2d(-230, 310);
    gl.glVertex2d(230, 310);
    gl.glVertex2d(230, 250);
    gl.glVertex2d(-230, 250);
    gl.glEnd();

    String heading = "high scores";
    for (int i = 0, y = 280, x = -200; i < heading.length(); i++) {
      char ch = heading.charAt(i);
      if (ch != ' ') {
        draw(ch - 'a' + 10, x, y);
      }

      x += 40;
    }
    /*
     * sorting the scores objects descending depends on the score value using the
     * comparator class and lambda expression
     */
    Collections.sort(scores, (e1, e2) -> Integer.parseInt(e1.score) > Integer.parseInt(e2.score) ? -1 : 1);
    for (int i = 0, y = 140; i < scores.size(); i++) {
      // for name printing
      String name = scores.get(i).name;
      for (int j = 0, x = -240; j < name.length(); j++) {
        char ch = name.charAt(j);
        draw(ch - 'a' + 10, x, y);
        x += 40;
      }

      // for score printing
      String score = scores.get(i).score;
      for (int j = score.length() - 1, x = 240; j >= 0; j--) {
        char ch = score.charAt(j);
        draw(ch - '0', x, y);
        x -= 40;
      }
      y -= 60;
    }
    draw(57, -575, 325);
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
