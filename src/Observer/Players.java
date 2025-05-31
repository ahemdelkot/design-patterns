package Observer;

import GameObjects.GameObjects;
import Pages.Timer;

import javax.media.opengl.GL;

public class Players extends GameObjects {
    int[] textures ;
    public boolean move = true;

    Timer timer;

    public Players(int[] textures ,Timer timer) {
        this.textures = textures;

        this.timer = timer;
    }

    void update(int player1Score , int player2Score){

        if (player1Score >= 5){
            System.out.println("you win 1");
//            int x = 120;
//            String heading = "you win";
//            for (int i = 0, y = 0; i < heading.length(); i++) {
//                char ch = heading.charAt(i);
//                if (ch != ' ') {
//                    draw(textures[ch - 'a' + 10], x, y, ch != 'i' ? 40 : 10, 40);
//                }
//
//                x += 40;
//
//            }
            timer.stop();
            move = false;

        }else if (player2Score >= 5){
            System.out.println("you win 2");
//            int x = -360 ;
//            String heading = "you win";
//            for (int i = 0, y = 0; i < heading.length(); i++) {
//                char ch = heading.charAt(i);
//                if (ch != ' ') {
//                    draw(textures[ch - 'a' + 10], x, y, ch != 'i' ? 40 : 10, 40);
//                }
//
//                x += 40;
//            }
            timer.stop();
            move = false;

        }
    }

    public boolean isMove(){
        return move;
    }



}
