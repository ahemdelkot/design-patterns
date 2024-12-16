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
    gl.glEnable(GL.GL_BLEND);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex3f((float) (x - 20), (float) (y - 20), -1.0f);
    gl.glTexCoord2f(1.0f, 0.0f);
    gl.glVertex3f((float) (x + 20), (float) (y - 20), -1.0f);
    gl.glTexCoord2f(1.0f, 1.0f);
    gl.glVertex3f((float) (x + 20), (float) (y + 20), -1.0f);
    gl.glTexCoord2f(0.0f, 1.0f);
    gl.glVertex3f((float) (x - 20), (float) (y + 20), -1.0f);
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
