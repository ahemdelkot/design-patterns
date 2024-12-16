import javax.media.opengl.*;
import javax.swing.*;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.*;
import Texture.TextureReader;
import javax.media.opengl.glu.GLU;
import java.io.*;
import java.util.*;

public class UserName extends JFrame {
  public UserName() {
    UserNameEventListener listener = new UserNameEventListener();
    GLCanvas glcanvas = new GLCanvas();
    glcanvas.addGLEventListener(listener);
    glcanvas.addMouseListener(listener);
    glcanvas.addKeyListener(listener);
    getContentPane().add(glcanvas, BorderLayout.CENTER);
    Animator animator = new FPSAnimator(60);
    animator.add(glcanvas);
    animator.start();

    setTitle("User Name");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 700); // ! set size of the window
    setLocationRelativeTo(null);
    setVisible(true);
    setFocusable(true);
    glcanvas.requestFocus();
  }
}

class UserNameEventListener implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {

  /*
   * // ? number from 0 to 9
   * // ? letter from 10 to 35
   */

  final static String ASSETS_PATH = "Assets\\Letters";
  final static String[] textureNames = new File(ASSETS_PATH).list();
  TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
  final int textures[] = new int[textureNames.length];

  GL gl; // global gl drawable to use in the class
  final int orthoX = 600, orthoY = 350;
  int windowWidth = 2 * orthoX, windowHight = 2 * orthoY, fliped;
  ArrayList<Integer> input = new ArrayList<>(7);

  @Override
  public void init(GLAutoDrawable arg0) {
    this.gl = arg0.getGL(); // set the gl drawable
    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // This Will Clear The Background Color To Black
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

  }

  @Override
  public void display(GLAutoDrawable arg0) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    printInput();
    // System.out.print("\r" + input);
  }

  public void draw(int index, double x, double y) {
    // draw the character
    gl.glEnable(GL.GL_BLEND);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]); // Turn Blending On

    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f); // bottom left point
    gl.glVertex3f((float) (x - 20), (float) (y - 20), -1.0f);
    gl.glTexCoord2f(1.0f, 0.0f); // top left point
    gl.glVertex3f((float) (x + 20), (float) (y - 20), -1.0f);
    gl.glTexCoord2f(1.0f, 1.0f); // top right point
    gl.glVertex3f((float) (x + 20), (float) (y + 20), -1.0f);
    gl.glTexCoord2f(0.0f, 1.0f); // bottom right point
    gl.glVertex3f((float) (x - 20), (float) (y + 20), -1.0f);
    gl.glEnd();

    gl.glDisable(GL.GL_BLEND);
  }

  @Override
  public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
  }

  @Override
  public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
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
  public void mousePressed(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
  }

  private double convertX(double x) {
    return x * (2 * orthoX) / windowWidth - orthoX;
  }

  private double convertY(double y) {
    return orthoY - 2 * orthoY / windowHight * y;
  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void keyTyped(KeyEvent e) {
    char ch = e.getKeyChar();
    int backSpaceCode = 8;

    // System.out.println(ch + " " + (ch == backSpaceCode));
    if(ch >= 'a' && ch <= 'z' && input.size() <= 6){
      input.add(ch - 'a' + 10);
    }
    else if(ch == backSpaceCode && input.size() > 0)
      input.remove(input.size() - 1);
  }

  public void printInput() {
    for (int i = 0, y = 280, x = -200; i < input.size(); i++) {
      draw(input.get(i), x, 0);

      x += 45;
    }
  }
}