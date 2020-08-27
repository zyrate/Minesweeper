import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 游戏窗口
 */
public class GameWindow extends JFrame implements Serializable {
    private static final long serialVersionUID = -7673336490024786648L;
    JPanel pMap, pUp, pUpC;

    public GameWindow(String title){
        this.setTitle(title);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setResizable(false);
        //图标
        ImageIcon gameIcon = new ImageIcon(GameWindow.class.getResource("/icons/icon.png"));
        gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
        this.setIconImage(gameIcon.getImage());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pUp = new JPanel(new BorderLayout());
        this.add(pUp, BorderLayout.NORTH);
    }

    /*添加各种组件*/

    public void add(ButtonsMap bm){
        pMap = new JPanel(new GridLayout(bm.getRows(), bm.getCols()));
        //Map边界 - 以下两种
        pMap.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        //pMap.setBorder(BorderFactory.createRaisedBevelBorder());
        for(int i = 0; i < bm.getRows(); i++){
            for(int j = 0; j < bm.getCols(); j++){
                pMap.add(bm.getMineButton(i, j));
            }
        }
        this.add(pMap, BorderLayout.CENTER);
    }
    public void add(Scorer scorer){
        //上部边界
        pUp.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.BLACK));
        pUp.add(scorer, BorderLayout.WEST);

    }
    public void add(Timer timer){
        //上部边界
        pUp.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.BLACK));
        pUp.add(timer, BorderLayout.EAST);
    }
    public void add(Reseter reseter){
        //上部的中央
        pUpC = new JPanel(new FlowLayout());
        pUp.add(pUpC);
        //这样就把重置器放到了中央
        pUpC.add(reseter);
    }
    //这里是set
    public void set(GameMenu gm){
        this.setJMenuBar(gm);
    }
}
