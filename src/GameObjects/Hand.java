package GameObjects;
import javax.media.opengl.*;

public class Hand extends GameObjects{
  Ball ball;
  int score;
  boolean right;

  public Hand(int textureIndex, double x, double y, boolean right, GL gl){
    super(textureIndex, x, y, gl);
    this.right = right;
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

  public void move(double dx, double dy) {
    // double oldX = this.x, oldY = this.y;
    this.x += dx;
    this.y += dy;

    if(right){
      if (this.x < 0) this.x = 0;
      if (this.x > 530) this.x = 530;
      if (this.y > 280) this.y = 280;
      if (this.y < -280) this.y = -280;
    } 
    else{
      if (x > 0) x = 0;
      if (this.x < -530) this.x = -530;
      if (this.y > 280) this.y = 280;
      if (this.y < -280) this.y = -280;
    }


    if (this.x > 530)
      this.x = 530;
    if (this.y > 280)
      this.y = 280;
    if (this.y < -280)
      this.y = -280;
  }
}
