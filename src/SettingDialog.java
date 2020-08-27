import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * 设置速度对话框
 */
class SettingDialog extends JDialog {
    private JLabel message;
    private JSlider slider;
    private JCheckBox isSuper;
    private int pressTime = 200;//按下停顿时间
    private int sleepTime = 1000;//默认间隔时间
    AutoBiz autoBiz;

    public SettingDialog(AutoBiz autoBiz, GameWindow gw){
        this.autoBiz = autoBiz;
        setSize(300, 160);
        setLocationRelativeTo(gw);
        setResizable(false);
        setTitle("自动扫雷速度：");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(null);

        message = new JLabel();
        message.setFont(new Font("宋体", 1, 20));
        message.setBounds(128, 10, 100, 20);

        slider = new JSlider(0, 200);
        slider.setBounds(10, 30, 270, 70);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(50);//设置主刻度标记的间隔
        slider.setMinorTickSpacing(10);
        slider.setValue((2000-getSleepTime())/10);//要减
        message.setText(String.valueOf(slider.getValue()));

        isSuper = new JCheckBox("飞速");
        isSuper.setFont(new Font("楷体", 1, 16));
        isSuper.setBounds(120, 90, 60, 50);
        this.add(message);
        this.add(slider);
        this.add(isSuper);

        addListener();
    }


    public void addListener(){
        this.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setSleepTime(2000 - slider.getValue()*10);
                message.setText(String.valueOf(slider.getValue()));

                if(autoBiz != null)
                    autoBiz.setSleepTime(getSleepTime());
            }
        });
        this.isSuper.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(isSuper.isSelected()){
                    slider.setEnabled(false);
                    message.setEnabled(false);
                    setSleepTime(0);
                    setPressTime(0);
                    if(autoBiz != null){
                        autoBiz.setSleepTime(0);
                        autoBiz.setPressTime(0);
                    }
                }else if(!isSuper.isSelected()){
                    slider.setEnabled(true);
                    message.setEnabled(true);
                    setSleepTime(2000 - slider.getValue()*10);
                    setPressTime(200);
                    if(autoBiz != null){
                        autoBiz.setSleepTime(getSleepTime());
                        autoBiz.setPressTime(getPressTime());
                    }
                }
            }
        });
    }

    public int getPressTime() {
        return pressTime;
    }

    public void setPressTime(int pressTime) {
        this.pressTime = pressTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setAutoBiz(AutoBiz autoBiz){
        this.autoBiz = autoBiz;
    }
}

