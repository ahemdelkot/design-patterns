// graphics packages
import javax.media.opengl.*;
import javax.swing.*;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.glu.GLU;
import Texture.TextureReader;

// java packages
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

// our own packages
import Pages.*;

public class Home extends JFrame {
  AudioInputStream audioStream;
  Clip clip;

  public Home() {
    try {
      audioStream = AudioSystem.getAudioInputStream(new File("Assets\\sound\\game.wav"));
      clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (Exception e){
      e.printStackTrace();
    }

    HomeEventListener listener = new HomeEventListener(clip);
    GLCanvas glcanvas = new GLCanvas();
    glcanvas.addKeyListener(listener);
    glcanvas.addGLEventListener(listener);
    glcanvas.addMouseListener(listener);
    glcanvas.addMouseMotionListener(listener);
    getContentPane().add(glcanvas, BorderLayout.CENTER);

    Animator animator = new FPSAnimator(60);
    animator.add(glcanvas);
    animator.start();

    setTitle("Home");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 700);
    setLocationRelativeTo(null);
    setVisible(true);
    setFocusable(true);
    glcanvas.requestFocus();

    // Play background music or sound effect
    clip.start();
  }
}

class HomeEventListener implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {

  final static String ASSETS_PATH = "Assets\\Sprites";
  final static String[] textureNames = new File(ASSETS_PATH).list();
  final TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
  final int textures[] = new int[textureNames.length];
  final int orthoX = 600, orthoY = 350;
  int windowWidth = 2 * orthoX, windowHight = 2 * orthoY, flag[] = {0};

  GL gl; // global gl drawable to use in the class
  int[] mouse = new int[2]; // tracking mouse position
  boolean[] mouseClicked = {false}; // tracking mouseClicked
  BitSet keyBits = new BitSet(256); // tracking keyPressing
  ArrayList<Integer> input = new ArrayList<>(7); // tracking key inputs for user name
  String inputUserName;

  UserName userName;
  HowToPlay howToPlay;
  HighScores HighScores;
  Levels levels;
  Game game;
  Clip clip;

  public HomeEventListener(Clip clip) {
      this.clip = clip;
  }

  @Override
  public void init(GLAutoDrawable arg0) {
    this.gl = arg0.getGL(); // set the gl drawable
    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // This Will Clear The Background Color To Black
    gl.glOrtho(-orthoX, orthoX, -orthoY, orthoY, -1, 1); // setting the orth and the coordinates of the window

    // set textures
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


    // initialize the value of the objects
    try {
      howToPlay = new HowToPlay(textures, 36, gl);
      HighScores = new HighScores(gl, textures);
      game = new Game(gl, textures, mouse, mouseClicked, keyBits);
      userName = new UserName(gl, input, textures);
      levels = new Levels(textures, mouse, flag, userName, game, gl);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void display(GLAutoDrawable arg0) {
    // Clear the screen
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    
    // displaying the pages based on flag value and button clicked
    transfer();
  }

  private void drawBackGround() {
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
  public void mousePressed(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();
    mouse[0] = (int) convertX(e.getX()); // set the mouse position
    mouse[1] = (int) convertY(e.getY()); // set the mouse position
    mouseClicked[0] = true; // set the mouse clicked
    
    if (flag[0] == 0) {
      if (mouse[0] > -130 && mouse[0] < 130) {
        if (mouse[1] > 200 && mouse[1] < 300) {
          flag[0] = 1;
          playSound("Assets\\sound\\letsGo.wav");
        }
        if (mouse[1] > 50 && mouse[1] < 150) {
          flag[0] = 2;
          playSound("Assets\\sound\\letsGo.wav");
        }
        if (mouse[1] > -100 && mouse[1] < 0) {
          flag[0] = 3;
          playSound("Assets\\sound\\letsGo.wav");

        }
        if (mouse[1] > -250 && mouse[1] < -150) {
          flag[0] = 4;
          playSound("Assets\\sound\\letsGo.wav");
        }
      }

      // music button
      if (mouse[1] < -250 && mouse[1] > -350) {
        if (mouse[0] > 550 && mouse[0] < 600) {
          // control music when click (music toggle)
          if(clip.isActive()){
            clip.stop();
          } else {
            clip.start();
          }
        }
      }
    } 

    /*
     ? flag[0] 1 for 1 player button
     ? flag[0] 2 for 2 player button
     ? flag[0] 3 for How to play button
     ? flag[0] 4 for HighScores button
    */

    else if (flag[0] == 1 || flag[0] == 2 || flag[0] == 3 || flag[0] == 4) {
      // back button
      if (mouse[0] > -600 && mouse[0] < -550 && mouse[1] > 250 && mouse[1] < 350) {
        flag[0] = 0;
        levels.levelChosen = 0;
      }

      if (flag[0] == 1) {
        if (mouse[0] > -130 && mouse[0] < 130) {
          if (mouse[1] > 50 && mouse[1] < 150) {
            System.out.println("level 1");
            levels.levelChosen = 1;
            userName.takeingInput = true;
          }
          if (mouse[1] > -100 && mouse[1] < 0) {
            System.out.println("level 2");
            levels.levelChosen = 2;
            userName.takeingInput = true;
          }
          if (mouse[1] > -250 && mouse[1] < -150) {
            System.out.println("level 3");
            levels.levelChosen = 3;
            userName.takeingInput = true;
          }
        }
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    mouseClicked[0] = false; // set the mouse clicked
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    mouse[0] = (int) convertX(e.getX()); // set the mouse position
    mouse[1] = (int) convertY(e.getY()); // set the mouse position
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    windowHight = e.getComponent().getHeight();
    windowWidth = e.getComponent().getWidth();

    mouse[0] = (int) convertX(e.getX()); // set the mouse position
    mouse[1] = (int) convertY(e.getY()); // set the mouse position
  }

  @Override
  public void keyTyped(KeyEvent e) {
    int ch = e.getKeyChar();
    int backSpaceCode = 8;

    if(ch >= 'a' && ch <= 'z' && input.size() <= 6){
      if (userName.takeingInput) input.add(ch - 'a' + 10);
    }
    else if(ch == backSpaceCode && input.size() > 0){
      if (userName.takeingInput) input.remove(input.size() - 1);
    }

    if(ch == '\n'){
      userName.takeingInput = false;
      for (int i = 0; i < input.size(); i++) {
        inputUserName += input.get(i);
      }
      flag[0] = 2;
    }
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

  private double convertX(double x) {
    return x * (2 * orthoX) / windowWidth - orthoX;
  }

  private double convertY(double y) {
    return orthoY - ((2f * orthoY) / windowHight * y);
  }

  private void transfer() {
    if (flag[0] == 0) {
      // main window page (base case)
      drawBackGround();
      drawHome();
    } else if (flag[0] == 1) {
      // single player
      levels.draw();
    } else if (flag[0] == 2) {
      // 2 player
      game.draw();
    } else if (flag[0] == 3) {
      // how to play page
      howToPlay.draw();
    } else if (flag[0] == 4) {
      // highScores page
      HighScores.printScores();
    } else if (flag[0] == 5) {
      userName.printInput();
    }
  }

  private void draw(int index, double x, double y) {
    draw(index, x, y, 260, 100);
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

  private void playSound(String filePath) {
    try {
      File soundFile = new File(filePath);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
      Clip clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.start();
    } catch (Exception e) {
      System.err.println("Error playing sound: " + e.getMessage());
    }
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
}