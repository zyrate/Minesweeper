import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * 重置器
 */

public class Reseter extends JButton implements Serializable {
    private static final long serialVersionUID = 277422627147136563L;
    ButtonsMap bm;
    Scorer scorer;
    Timer timer;
    MinesCreater mc;
    MinesCounter counter;
    GameWindow gw;
    GameMenu gm;
    Reseter reseter;

    public Reseter(ButtonsMap bm, Scorer scorer, Timer timer, MinesCreater mc, MinesCounter counter){
        this.bm = bm;
        this.mc = mc;
        this.scorer = scorer;
        this.timer = timer;
        this.counter = counter;
        this.setPreferredSize(new Dimension(40, 40));
        this.setBackground(new Color(138, 212, 237));
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setIconDefault();
    }
    //设为按下去
    public void setPressed(){
        this.setBorder(BorderFactory.createLoweredBevelBorder());
    }
    //设为松开
    public void setReleased(){
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }
    //设置为笑脸
    public void setIconDefault(){
        ImageIcon mineIcon = new ImageIcon(Reseter.class.getResource("/icons/smile.png"));
        mineIcon.setImage(mineIcon.getImage().getScaledInstance(32, 32,Image.SCALE_SMOOTH));
        this.setIcon(mineIcon);
    }

    //设置为正在点方块
    public void setIconPress(){
        ImageIcon mineIcon = new ImageIcon(Reseter.class.getResource("/icons/click.png"));
        mineIcon.setImage(mineIcon.getImage().getScaledInstance(32, 32,Image.SCALE_SMOOTH));
        this.setIcon(mineIcon);
    }

    //设置为赢
    public void setIconWin(){
        ImageIcon mineIcon = new ImageIcon(Reseter.class.getResource("/icons/win.png"));
        mineIcon.setImage(mineIcon.getImage().getScaledInstance(32, 32,Image.SCALE_SMOOTH));
        this.setIcon(mineIcon);
    }

    //设置为输
    public void setIconFail(){
        ImageIcon mineIcon = new ImageIcon(Reseter.class.getResource("/icons/lose.png"));
        mineIcon.setImage(mineIcon.getImage().getScaledInstance(32, 32,Image.SCALE_SMOOTH));
        this.setIcon(mineIcon);
    }

    //同等级重置
    public void reset(){
        bm.resetButtons();
        scorer.setFlagCnt(0);
        scorer.setSweepedCnt(0);
        scorer.setMinesCnt(mc.getMinesCnt());
        scorer.update();
        timer.end();
        timer.setCurrTime(0);
        timer.updateTime();
        this.setIconDefault();
    }

    //重置 - 要把重置好的对象传回去 - 按顺序
    public LinkedList reset(int level){
        LinkedList list = new LinkedList();

        gw = new GameWindow("扫雷");
        bm = new ButtonsMap(level);
        mc = new MinesCreater(level);
        counter = new MinesCounter(bm);
        scorer = new Scorer(mc.getMinesCnt());
        timer = new Timer();
        gm = new GameMenu();

        gw.add(bm);
        gw.add(scorer);
        gw.add(timer);
        gw.set(gm);
        gw.pack();
        gw.setLocationRelativeTo(null);//居中显示！！！


        list.add(gw);
        list.add(bm);
        list.add(mc);
        list.add(counter);
        list.add(scorer);
        list.add(timer);
        list.add(gm);

        return list;
    }

    //自定义
    public LinkedList reset(int rows, int cols, int minesCnt){
        LinkedList list = new LinkedList();

        gw = new GameWindow("扫雷");
        bm = new ButtonsMap(4);
        bm.customize(rows, cols);
        mc = new MinesCreater(4);
        mc.customize(minesCnt);//自定义
        counter = new MinesCounter(bm);
        scorer = new Scorer(mc.getMinesCnt());
        timer = new Timer();
        gm = new GameMenu();

        gw.add(bm);
        gw.add(scorer);
        gw.add(timer);
        gw.set(gm);
        gw.pack();
        gw.setLocationRelativeTo(null);//居中显示！！！

        list.add(gw);
        list.add(bm);
        list.add(mc);
        list.add(counter);
        list.add(scorer);
        list.add(timer);
        list.add(gm);

        return list;
    }

}
