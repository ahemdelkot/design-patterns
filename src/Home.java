import javax.media.opengl.*;
import javax.swing.*;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.*;
import Texture.TextureReader;
import javax.media.opengl.glu.GLU;
import java.io.*;
import java.util.BitSet;
import javax.sound.sampled.*;
import Pages.*;

public class Home extends JFrame {
  public Home() {
    HomeEventListener listener = new HomeEventListener();
    GLCanvas glcanvas = new GLCanvas();
    glcanvas.addKeyListener(listener);
    glcanvas.addGLEventListener(listener);
    glcanvas.addMouseListener(listener);
    glcanvas.addMouseMotionListener(listener);
    getContentPane().add(glcanvas, BorderLayout.CENTER);

    // glcanvas.addKeyListener((KeyListener) listener);
    Animator animator = new FPSAnimator(60);
    animator.add(glcanvas);
    animator.start();

    setTitle("Home");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 700); // ! set size of the window
    setLocationRelativeTo(null);
    setVisible(true);
    setFocusable(true);
    glcanvas.requestFocus();
    // Play background music or sound effect
    // if (flag == 0){
    playSound("Assets\\sound\\game.wav");
    // }
  }

  public void playSound(String filePath) {
    try {
      File soundFile = new File(filePath);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
      Clip clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.loop(Clip.LOOP_CONTINUOUSLY); // Use this for continuous background music
      clip.start();
    } catch (Exception e) {
      System.err.println("Error playing sound: " + e.getMessage());
    }
  }
}

class HomeEventListener implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {
  /*
   * // ? number from 0 to 9
   * // ? letter from 10 to 35
   * // ? how to play in 36
   */
  final static String ASSETS_PATH = "Assets\\Sprites";
  final static String[] textureNames = new File(ASSETS_PATH).list();
  TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
  final int textures[] = new int[textureNames.length];

  GL gl; // global gl drawable to use in the class
  final int orthoX = 600, orthoY = 350;
  int windowWidth = 2 * orthoX, windowHight = 2 * orthoY, flag = 0;
  int[] mouse = new int[2];
  boolean[] mouseClicked = {false};
  HowToPlay howToPlay;
  HighScores HighScores;
  Levels levels;
  Game game;
  BitSet keyBits = new BitSet(256);

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
    try {
      howToPlay = new HowToPlay(textures, 36, gl);
      HighScores = new HighScores(gl, textures);
      levels = new Levels(textures, gl);
      game = new Game(gl, textures, mouse, mouseClicked, keyBits);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void display(GLAutoDrawable arg0) {
    // Clear the screen
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // Draw based on the current flag
    // Home screen
    if (flag == 0) {
      drawBackGround();
      drawHome();
    } else {
      transfer();
      if (flag == 1) {
        // drawBackGround();
        drawLevels();
      }
    }
  }

  private void draw(int index, double x, double y) {
    color(255, 255, 255);
    draw(index, x, y, 260, 100);
  }

  public void drawBackGround() {
    draw(40, 0, 0, 1200, 700);
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
    mouse[0] = (int) convertX(e.getX());
    mouse[1] = (int) convertY(e.getY());
    mouseClicked[0] = true;
    
    if (flag == 0) {
      if (mouse[0] > -130 && mouse[0] < 130) {
        if (mouse[1] > 200 && mouse[1] < 300) {
          flag = 1;
          playSound("Assets\\sound\\background.wav");
        }
        if (mouse[1] > 50 && mouse[1] < 150) {
          flag = 2;
          playSound("Assets\\sound\\background.wav");
        }
        if (mouse[1] > -100 && mouse[1] < 0) {
          flag = 3;
          playSound("Assets\\sound\\background.wav");

        }
        if (mouse[1] > -250 && mouse[1] < -150) {
          flag = 4;
          playSound("Assets\\sound\\background.wav");
        }
      }
      if (mouse[1] < -250 && mouse[1] > -350) {
        if (mouse[0] > -600 && mouse[0] < -550) {
          System.exit(0);
        } else if (mouse[0] > 550 && mouse[0] < 600) {
          // music on/off
        }
      }
    } else if (flag == 3 || flag == 4 || flag == 1 || flag == 2) {
      if (flag == 1) {
        if (mouse[0] > -130 && mouse[0] < 130) {
          if (mouse[1] > 50 && mouse[1] < 150) {
            System.out.println("level 1");
          }
          if (mouse[1] > -100 && mouse[1] < 0) {
            System.out.println("level 2");
          }
          if (mouse[1] > -250 && mouse[1] < -150) {
            System.out.println("level 3");
          }
        }
      }
      if (mouse[0] > -600 && mouse[0] < -550 && mouse[1] > 250 && mouse[1] < 350) {
        flag = 0;
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
    mouseClicked[0] = false;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
    mouse[0] = (int) convertX(e.getX());
    mouse[1] = (int) convertY(e.getY());
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
    mouse[0] = (int) convertX(e.getX());
    mouse[1] = (int) convertY(e.getY());
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    keyBits.set(key);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    keyBits.clear(key);
  }

  private void color(float r, float g, float b) {
    gl.glColor3f(r / 255, g / 255, b / 255);
  }

  private double convertX(double x) {
    return x * (2 * orthoX) / windowWidth - orthoX;
  }

  private double convertY(double y) {
    return orthoY - ((2f * orthoY) / windowHight * y);
  }

  private void transfer() {
    if (flag == 1) {
      // single player-not implemented
      levels.drawLevels();
    } else if (flag == 2) {
      // new Game();
      game.draw();
    } else if (flag == 3) {
      howToPlay.draw();
    } else if (flag == 4) {
      // new HighScores();
      HighScores.printScores();
    } else if (flag == 5) {
      System.out.println("level one");
    }
  }

  private void drawHome() {
    draw(46, 0, 250);

    draw(46, 0, 100);

    draw(46, 0, -50);

    draw(46, 0, -200);

    draw(46, -575, -325, 50, 50);

    draw(46, 575, -325, 50, 50);

    if (mouse[0] > -130 && mouse[0] < 130) {
      if (mouse[1] > 200 && mouse[1] < 300) {
        draw(41, 0, 250);
      }
      if (mouse[1] > 50 && mouse[1] < 150) {
        draw(42, 0, 100);
      }
      if (mouse[1] > -100 && mouse[1] < 0) {
        draw(43, 0, -50);
      }
      if (mouse[1] > -250 && mouse[1] < -150) {
        draw(44, 0, -200);
      }
    }
  }

  public void playSound(String filePath) {
    try {
      File soundFile = new File(filePath);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
      Clip clip = AudioSystem.getClip();
      clip.open(audioStream);
      // clip.loop(Clip.LOOP_CONTINUOUSLY); // Use this for continuous background
      // music
      clip.start();
    } catch (Exception e) {
      System.err.println("Error playing sound: " + e.getMessage());
    }
  }

  public void drawLevels() {
    draw(46, 0, 100);
    draw(46, 0, -50);
    draw(46, 0, -200);
    if (mouse[0] > -130 && mouse[0] < 130) {
      if (mouse[1] > 50 && mouse[1] < 150) {
        draw(42, 0, 100);
      }
      if (mouse[1] > -100 && mouse[1] < 0) {
        draw(43, 0, -50);
      }
      if (mouse[1] > -250 && mouse[1] < -150) {
        draw(44, 0, -200);
      }
    }
  }
}