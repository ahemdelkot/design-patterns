package GameObjects;

import javax.media.opengl.*;

public class Ball extends GameObjects {
  public double m = 1, dx = 0 , dy =  0;
  private boolean move = true;
  private Hand handRight;
  private Hand handLeft;
  boolean flag , flag2 = true , flag3 = true , up_wallFlag =true , left_wallFlag =true;
  int[] textures;

  public Ball(int[] textures, int x, int y, Hand handRight, Hand handLeft, GL gl) {
    super(textures[38], x, y, gl);
    this.textures = textures;
    this.handRight = handRight;
    this.handLeft = handLeft;
    handRight.ball = this;
    handLeft.ball = this;
  }

  public void draw() {
    super.draw();
//    checkCollapse();
    checkCollide();
    move();
    checkWinner();
  }

  public void move() {
    if (!this.move)
      return;

    super.x += dx;
    super.y += dy;
  }

  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;

    if (this.x < -605) this.x = -605;
    if (this.x > 605) this.x = 605;

    if (this.y > 280) this.y = 280;
    if (this.y < -280) this.y = -280;
  }
  private void checkCollide() {
    moveTo(this.x,this.y);
    if ((super.x >= 605 || super.x <= -605) ){
      if (y >= 100 || y <= -100) {
        if (left_wallFlag){
          dx = -dx;
//        dy = -dy;
          left_wallFlag = false;
        }else{
          left_wallFlag = true;
        }
      }else{
        if(x > 0){
          x = 90;
          y = 0;
          dy = 0;
          dx = 0;
          handLeft.score++;
        }
        else if(x < 0){
          x = -90;
          y = 0;
          dy = 0;
          dx = 0;
          handRight.score++;
        }
      }


    }
    if ((super.y >= 280 || super.y <= -280) && up_wallFlag){
      up_wallFlag = false;
      dy = -dy;
    }else{
      up_wallFlag =true;
    }

    double d1 = GameObjects.distance(this, handRight);
    double d2 = GameObjects.distance(this, handLeft);
//    double ddx = handRight.x-handRight.prev_x;
//    double ddy = handRight.y-handRight.prev_y;
//    double dis = Math.sqrt((ddx*ddx)+(ddy*ddy));
    if ((d1 <= 7000 && flag2) ) {
      double nx = this.x-handRight.x , ny = this.y-handRight.y, mag = Math.sqrt((nx*nx)+(ny*ny));
      nx/=mag ; ny/=mag;
      double tan_x = -1*(ny/mag) , tan_y = (nx/mag) ;
      double v2_n = (5*nx) + (5*ny) , v2_t = (dx*tan_x) + (dy*tan_y) ;
      dx = -1* Math.max((v2_n*nx) + (v2_t*tan_x),11);
      dy = Math.max ((v2_n*ny) + (v2_t*tan_y),11);
      flag2 = false;
      System.out.println(nx + " "+ ny+" "+mag);
    }
    if ((d2 <= 7000 && flag3) ) {
      double nx = this.x-handLeft.x , ny = this.y-handLeft.y, mag = Math.sqrt((nx*nx)+(ny*ny));
      nx/=mag ; ny/=mag;
      double tan_x = -1*(ny/mag) , tan_y = (nx/mag) ;
      double v2_n = (5*nx) + (5*ny) , v2_t = (dx*tan_x) + (dy*tan_y) ;
      dx =  Math.max((v2_n*nx) + (v2_t*tan_x),11);
      dy =  -1*Math.max ((v2_n*ny) + (v2_t*tan_y),11);
//
//      dx = (v2_n*nx) + (v2_t*tan_x);
//      dy = (v2_n*ny) + (v2_t*tan_y);
      flag3 = false;
    }
    if (d1 > 7000){
      flag2 = true;
    }else{
//      x += dx;
//      y +=dy;

      dy*=1.1;
      dx*=1.1;
    }
    if (d2 > 7000){
      flag3 = true;
    }else{
//      x += dx;
//      y += dy;
      dy*=1.1;
      dx*=1.1;

    }
//    double d1 = GameObjects.distance(this, handRight);
//    double d2 = GameObjects.distance(this, handLeft);
//    double ddx = handRight.x-handRight.prev_x;
//    double ddy = handRight.y-handRight.prev_y;
//    double dis = Math.sqrt((ddx*ddx)+(ddy*ddy));
//    if ((d1 <= 5000 && flag2) ) {
//      if (ddy != 0){
//        int po1 = handRight.y - handRight.prev_y > 0? 1 : -1;
//        dy = 10* Math.atan(ddy/dis) * po1 ;
//      }
//      if (ddx != 0){
//        int po2 = handRight.x - handRight.prev_y > 0 ? 1: -1;
//        dx = 10* Math.atan(ddx/dis) * po2 ;
//      }
//      flag2 = false;
//    }
//    if ((d2 <= 5000 && flag3) ) {
//      dy = -dy;
//      dx = -dx;
//      flag3 = false;
//    }
//    if (d1 > 5000){
//      flag2 = true;
//    }else{
//      dy*=1.1;
//      dx*=1.1;
//    }
//    if (d2 > 5000){
//      flag3 = true;
//    }else{
//      dy*=1.1;
//      dx*=1.1;
//
//    }
  }

  public void checkCollapse(){
    if(distance(this, handRight) <= 7000)
      flag = true;
  }

  public void checkWinner() {
    final int SCORE = 5;
    if (handLeft.score < SCORE && handRight.score < SCORE) return;

    int x = handLeft.score == SCORE ? -360 : 120;
    String heading = "you win";
    for (int i = 0, y = 0; i < heading.length(); i++) {
      char ch = heading.charAt(i);
      if (ch != ' ') {
        draw(textures[ch - 'a' + 10], x, y, 40, 40);
      }

      x += 40;
    }
    move = false;
  }

  public void reset(){
    super.x = 0;
    super.y = 0;
  }
}