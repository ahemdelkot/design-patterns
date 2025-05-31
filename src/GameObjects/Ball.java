package GameObjects;

import Observer.Players;
import Observer.Ruler;

import javax.media.opengl.*;
import java.io.*;
import java.util.Scanner;

public class Ball extends GameObjects {
  public double dx = 0, dy = 0;
  public boolean move = true;
  private Hand handRight;
  private Hand handLeft;
  String[] playerName;
  boolean flag, flag2 = true, flag3 = true, up_wallFlag = true, left_wallFlag = true, scoreAdded = false;
  int[] textures;
  int[] levels = { 5, 5, 7, 9 };

  Players players ;
  Ruler ruler = new Ruler();
  Pages.Timer timer;

  public Ball(int[] textures, int x, int y, Hand handRight, Hand handLeft, String[] playerName, Pages.Timer timer,GL gl) {
    super(textures[38], x, y, gl);
    this.textures = textures;
    this.handRight = handRight;
    this.handLeft = handLeft;
    handRight.ball = this;
    handLeft.ball = this;
    this.timer = timer;
    this.playerName = playerName;
    players = new Players(textures, timer);
    ruler.startGame(players);
  }

  public void draw() {
    super.draw();
    // checkCollapse();
    checkCollide();
    move();
    checkWinner();

  }

  public void move() {
    if (!players.move)
      return;

    super.x += dx;
    super.y += dy;

    moveTo();

    if (handLeft.AI) {
      if (x <= 0) {
        double ddx = (x - handLeft.x), ddy = (y - handLeft.y);
        double dis = Math.sqrt((ddx * ddx) + (ddy * ddy));
        handLeft.x += levels[handLeft.level] * Math.atan(ddx / dis);
        handLeft.y += levels[handLeft.level] * Math.atan(ddy / dis);

      } else if (handLeft.level <= 2) {
        if (handLeft.x <= -300 && handLeft.y == 0)
          return;
        double ddx = (-440 - handLeft.x), ddy = (0 - handLeft.y);
        double dis = Math.sqrt((ddx * ddx) + (ddy * ddy));
        handLeft.x += levels[handLeft.level] * Math.atan(ddx / dis);
        handLeft.y += levels[handLeft.level] * Math.atan(ddy / dis);

      } else {
        if (handLeft.x <= -150 && handLeft.y == 0)
          return;
        double ddx = (-350 - handLeft.x), ddy = (0 - handLeft.y);
        double dis = Math.sqrt((ddx * ddx) + (ddy * ddy));
        handLeft.x += levels[handLeft.level] * Math.atan(ddx / dis);
        handLeft.y += levels[handLeft.level] * Math.atan(ddy / dis);

      }
    }
  }

  public void moveTo() {
    if (this.x < -530)
      this.x = -530;
    if (this.x > 530)
      this.x = 530;

    if (this.y > 280)
      this.y = 280;
    if (this.y < -280)
      this.y = -280;
  }

  private void checkCollide() {
    if ((super.x >= 530 || super.x <= -530)) {
      if (y >= 100 || y <= -100) {
        if (left_wallFlag) {
          dx = -dx;
          // dy = -dy;
          left_wallFlag = false;
        } else {
          left_wallFlag = true;
        }
      } else {
        if (x > 0) {
          x = 90;
          y = 0;
          dy = 0;
          dx = 0;
          ruler.increasingPlayer2Score();
          handLeft.score = ruler.getPlayer2Score() ;
          System.out.println(ruler.getPlayer2Score());
        } else if (x < 0) {
          x = -90;
          y = 0;
          dy = 0;
          dx = 0;
          ruler.increasingPlayer1Score();
          handRight.score = ruler.getPlayer1Score();
          System.out.println(ruler.getPlayer1Score());
        }
      }

    }
    if ((super.y >= 280 || super.y <= -280) && up_wallFlag) {
      up_wallFlag = false;
      dy = -dy;
    } else {
      up_wallFlag = true;
    }

    double d1 = GameObjects.distance(this, handRight);
    double d2 = GameObjects.distance(this, handLeft);
    if ((d1 <= 7000 && flag2)) {
      if (x - handRight.x == 0) {
        if (y < handRight.y) {
          dx = 0;
          dy = -10;
        } else {
          dx = 0;
          dy = 10;
        }
      } else if (y - handRight.y == 0) {
        if (x < handRight.x) {
          dx = -10;
          dy = 0;
        } else {
          dx = 10;
          dy = 0;
        }
      } else {
        int p1 = handRight.x - this.x > 0 ? -1 : 1;
        int p2 = handRight.y - this.y > 0 ? -1 : 1;
        double angle = Math.atan(-1 * (y - handRight.y) / (x - handRight.x));

        dx = p1 * Math.abs(Math.cos(angle) * 10);
        dy = p2 * Math.abs(Math.sin(angle) * 10);
      }
      flag2 = false;
    }
    if ((d2 <= 7000 && flag3)) {
      if (x - handLeft.x == 0) {
        // check if the ball hit the player from behind
        if (y < handLeft.y) {
          dx = 0;
          dy = -10;
        } else {
          dx = 0;
          dy = 10;
        }
      } else if (y - handLeft.y == 0) {
        if (x < handLeft.x) {
          dx = -10;
          dy = 0;
        } else {
          dx = 10;
          dy = 0;
        }
      } else {
        int p1 = handLeft.x - this.x > 0 ? -1 : 1;
        int p2 = handLeft.y - this.y > 0 ? -1 : 1;
        double angle = Math.atan(-1 * (y - handLeft.y) / (x - handLeft.x));
        dx = p1 * Math.abs(Math.cos(angle) * 10);
        dy = p2 * Math.abs(Math.sin(angle) * 10);
      }
      flag3 = false;
    }
    if (d1 > 7000) {
      flag2 = true;
    } else {
      dy *= 1.1;
      dx *= 1.1;
    }
    if (d2 > 7000) {
      flag3 = true;
    } else {
      dy *= 1.1;
      dx *= 1.1;
    }

  }

  public void checkCollapse() {
    if (distance(this, handRight) <= 7000)
      flag = true;
  }

  public void checkWinner() {
//    final int SCORE = 5;
//    if (handLeft.score < SCORE && handRight.score < SCORE)
//      return;
//
//    int x = handLeft.score == SCORE ? -360 : 120;
//    String heading = "you win";
//    for (int i = 0, y = 0; i < heading.length(); i++) {
//      char ch = heading.charAt(i);
//      if (ch != ' ') {
//        draw(textures[ch - 'a' + 10], x, y, ch != 'i' ? 40 : 10, 40);
//      }
//
//      x += 40;
//    }
//    move = false;
//    timer.stop();

    // using handLeft boolean AI variable to know if the game in 1-player or
    // 2-player mode cause score will be added only in the 1-player mode
    if (!handLeft.AI)
      return;

    // check if we already added the score to the file 
    if (scoreAdded)
      return;
    
    // add in the winner score to the file
    try {
      // taking the exists high scores from the file
      File file = new File("data\\score.txt");
      Scanner in = new Scanner(file);
      String str = "";
      while (in.hasNext()) {
        str += in.nextLine() + '\n';
      }
      in.close();

      // update the high scores to the new player
      int score = 1000 - timer.getSeconds() * 5;
      str += playerName[0] + " " + score;
      PrintWriter print = new PrintWriter(file);
      print.print(str);
      print.close();
      scoreAdded = true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  public void reset() {
    super.x = 0;
    super.y = 0;
    this.dx = 0;
    this.dy = 0;
    ruler.reset(players);
    this.scoreAdded = false;
    this.move = true;
  }
}