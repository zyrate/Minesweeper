import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.TimerTask;

/**
 * 计时器
 */

public class Timer extends JButton implements Serializable {
    private static final long serialVersionUID = -3451745779567892274L;
    private int currTime = 0;
    private boolean isPause = false;

    //有命名冲突
    transient java.util.Timer t;//前面的关键字是不序列化该对象的意思，因为Timer类好像没有实现该接口，会报错，具体查资料

    public Timer(){
        this.setPreferredSize(new Dimension(90, 50));
        this.setEnabled(true);//可按
        this.setBackground(new Color(172, 172, 172));
        this.setForeground(new Color(162, 62, 27));
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setText(String.format("%03d", this.getCurrTime()));//0补齐数字
        this.setFont(new Font("STENCIL STD", 1, 35));
        this.setFocusable(false);//设置为不可对焦
    }

    //开始计时
    public void start(){
            t = new java.util.Timer(true);
            t.schedule(new TimerTask() {
            @Override
            public void run() { //每隔一秒要做的事
                if(!isPause){//这个基本没用了，要开始就start，要结束就end
                    if(currTime != 999)//最大999秒
                        currTime++;
                    setText(String.format("%03d", getCurrTime()));
                }
            }
        }, 1000, 1000);//延迟1，间隔1秒
    }

    //结束
    public void end(){
        try{
            t.cancel();
        }catch (Exception e){
            //空指针也没事
        }
    }
    //重新开始
    public void restart(){
        end();
        setCurrTime(0);
        start();
    }
    public void updateTime(){
        setText(String.format("%03d", getCurrTime()));
    }

    public int getCurrTime() {
        return currTime;
    }

    public void setCurrTime(int currTime) {
        this.currTime = currTime;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
