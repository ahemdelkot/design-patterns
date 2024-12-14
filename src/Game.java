import javax.media.opengl.*;
import javax.swing.*;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.*;
import Texture.TextureReader;
import javax.media.opengl.glu.GLU;
import java.io.*;

public class Game extends JFrame {
  public Game() {
    GameEventListener listener = new GameEventListener();
    GLCanvas glcanvas = new GLCanvas();
    glcanvas.addGLEventListener(listener);
    glcanvas.addMouseListener(listener);
    glcanvas.addMouseMotionListener(listener);
    getContentPane().add(glcanvas, BorderLayout.CENTER);
    Animator animator = new FPSAnimator(60);
    animator.add(glcanvas);
    animator.start();

    setTitle("Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 700); // ! set size of the window
    setLocationRelativeTo(null);
    setVisible(true);
    setFocusable(true);
    glcanvas.requestFocus();
  }
}

class GameEventListener implements GLEventListener, MouseMotionListener, MouseListener {

  /*
   * // ? number from 0 to 9
   * // ? letter from 10 to 35
   * // ? how to play instruction in index 36
   * // ? background in index 37
   * // ? ball in index 38
   * // ? hand in index 39
   */
  final static String ASSETS_PATH = "Assets\\Letters";
  final static String[] textureNames = new File(ASSETS_PATH).list();
  TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
  final int textures[] = new int[textureNames.length];
  // boolean[] action = {true};

  GL gl; // global gl drawable to use in the class
  final int orthoX = 600, orthoY = 350;
  int windowWidth = 2 * orthoX, windowHight = 2 * orthoY, fliped;
  Ball ball, hand1, hand2;
  boolean pressed;

  @Override
  public void init(GLAutoDrawable arg0) {
    this.gl = arg0.getGL(); // set the gl drawable
    gl.glClearColor(0, 0, 0, 1); // This Will Clear The Background Color To Black
    gl.glOrtho(-orthoX, orthoX, -orthoY, orthoY, -1, 1); // setting the orth and the coordinates of the window

    gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glGenTextures(textureNames.length, textures, 0);

    for (int i = 0; i < textureNames.length; i++) {
      try {
        texture[i] = TextureReader
            .readTexture(ASSETS_PATH + "\\" + textureNames[i], true);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

        // mipmapsFromPNG(gl, new GLU(), texture[i]);
        new GLU().gluBuild2DMipmaps(
            GL.GL_TEXTURE_2D,
            GL.GL_RGBA, // Internal Texel Format,
            texture[i].getWidth(), texture[i].getHeight(),
            GL.GL_RGBA, // External format from image,
            GL.GL_UNSIGNED_BYTE,
            texture[i].getPixels() // Imagedata
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    ball  = new Ball(textures, 38, 0, 0, gl);
    hand1 = new Ball(textures, 39, 440, 0, gl);
    hand2 = new Ball(textures, 39, -440, 0, gl);
  }

  @Override
  public void display(GLAutoDrawable arg0) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    drawBackground();
    ball.draw();
    hand1.draw();
    hand2.draw();

    if(Ball.collides(ball, hand1)) {
      System.out.println("collides");
      ball.method(hand1.x, hand2.x);
    }
  }

  public void drawBackground() {
    draw(37, 0, 0, 1200, 700);
  }

  private void draw(int index, double x, double y) {
    draw(index, x, y, 40, 40);
  }

  public void draw(int index, double x, double y, double width, double height) {
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

  private void color(float r, float g, float b) {
    gl.glColor3f(r / 255, g / 255, b / 255);
  }

  @Override
  public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
  }

  @Override
  public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
  }


  @Override
  public void mouseEntered(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    // ! System.out.println(convertX(e.getX()) + " " + convertY(e.getY()));
  }

  @Override
  public void mousePressed(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    pressed = true;
    hand1.moveTo(convertX(e.getX()), convertY(e.getY()));
    // ! System.out.println(convertX(e.getX()) + " " + convertY(e.getY()));
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
    pressed = false;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    hand1.moveTo(convertX(e.getX()), convertY(e.getY()));
    // ! System.out.println(convertX(e.getX()) + " " + convertY(e.getY()));
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    if(!pressed) return;
    hand1.moveTo(convertX(e.getX()), convertY(e.getY()));
  }

  private double convertX(double x) {
    return x * (2 * orthoX) / windowWidth - orthoX;
  }

  private double convertY(double y) {
    return orthoY - (2 * orthoY) / windowHight * y;
  }
}