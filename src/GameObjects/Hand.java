package GameObjects;
import javax.media.opengl.*;

public class Hand extends GameObjects{
  Ball ball;
  public int score , level;
  public boolean right , AI;
  int[] textures;

  public Hand(int textureIndex, double x, double y, boolean right, int[] textures, GL gl ){
    super(textureIndex, x, y, gl);
    this.right = right;
    this.textures = textures;
  }

  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;

    if(right){
      if (this.x < 0) this.x = 0;
      if (this.x > 530) this.x = 530;
    } 
    else{
      if (x > 0) x = 0;
      if (this.x < -530) this.x = -530;
    }

    if (this.y > 280) this.y = 280;
    if (this.y < -280) this.y = -280;
  }

  public void move(double dx, double dy) {
    this.x += dx;
    this.y += dy;

    if(right){
      if (this.x < 0) this.x = 0;
      if (this.x > 530) this.x = 530;
    } 
    else{
      if (x > 0) x = 0;
      if (this.x < -530) this.x = -530;
    }
    
    if (this.y > 280) this.y = 280;
    if (this.y < -280) this.y = -280;
  }

  public void draw(){
    super.draw();
    printScore();
  }

  public void printScore() {
    int x = right ? 500 : -500;
    int y = 280;

    gl.glEnable(GL.GL_BLEND);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[score]); // Turn Blending On

    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f); // bottom left point
    gl.glVertex3f((float) (x - 0.5 * 40), (float) (y - 0.5 * 40), -1.0f);
    gl.glTexCoord2f(1.0f, 0.0f); // top left point
    gl.glVertex3f((float) (x + 0.5 * 40), (float) (y - 0.5 * 40), -1.0f);
    gl.glTexCoord2f(1.0f, 1.0f); // top right point
    gl.glVertex3f((float) (x + 0.5 * 40), (float) (y + 0.5 * 40), -1.0f);
    gl.glTexCoord2f(0.0f, 1.0f); // bottom right point
    gl.glVertex3f((float) (x - 0.5 * 40), (float) (y + 0.5 * 40), -1.0f);
    gl.glEnd();

    gl.glDisable(GL.GL_BLEND);
  }

  public void reset() {
    this.x = right ? 440 : -440;
    this.y = 0;
    this.score = 0;
    this.level = 0;
    this.AI = false;
  }
}
