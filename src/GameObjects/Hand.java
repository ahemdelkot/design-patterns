package GameObjects;
import javax.media.opengl.*;

public class Hand extends GameObjects{
  Ball ball;
  int score;

  public Hand(int textureIndex, double x, double y, GL gl){
    super(textureIndex, x, y, gl);
  }

  public void moveTo(double x, double y) {
    // double oldX = this.x, oldY = this.y;
    this.x = x;
    this.y = y;
    // if(distance(this, ball) <= 80){
    //   this.x = oldX;
    //   this.y = oldY;
    // }

    // ? uncomment this
    // if (this.x < 0) 
    //   this.x = 0;
    if (this.x > 530)
      this.x = 530;
    if (this.y > 280)
      this.y = 280;
    if (this.y < -280)
      this.y = -280;

    
  }
}
