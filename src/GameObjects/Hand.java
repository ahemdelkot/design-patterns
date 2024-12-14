package GameObjects;
import javax.media.opengl.*;

public class Hand extends GameObjects{
  Hand(int textureIndex, double x, double y, GL gl){
    super(textureIndex, x, y, gl);
  }

  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;

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
