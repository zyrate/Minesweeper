
import java.awt.*;
import java.util.Random;

/**
 * 自动扫雷的逻辑
 */
public class AutoBiz extends Thread{
    GameBiz gb;
    private AutoEvents autoEvents;
    private int pressTime = 200;//按下停顿时间
    private int sleepTime = 1000;//默认间隔时间
    private boolean isPause = false;

    public AutoBiz(GameBiz gb){
        this.gb = gb;
        autoEvents = new AutoEvents(gb);
    }

    @Override
    public void run() {
        begin();
    }

//    //设置速度
//    public void setSpeed(){
//        if(sd == null)
//            sd = new SettingDialog(this, gb.gw);
//        else
//            sd.setVisible(true);
//    }
    //开始扫雷
    public void begin(){
        //没输没赢没暂停
        while(!gb.isFail() && !gb.isWin() && !isPause){
            autoSweep();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {}
        }
    }

    //自动扫雷（一次遍历，处理完一个按钮就返回）
    public void autoSweep(){
        for(int i = 0; i < gb.bm.getRows(); i++){
            for(int j = 0; j < gb.bm.getCols(); j++){
                MineButton mb = gb.bm.getMineButton(i, j);
                if(mb.isSweeped() && mb.getMineCntAround()!=0){//只分析被扫过的格子 - 并且不空
                    if(gb.bm.notSweepedCntAround(mb) == mb.getMineCntAround() &&
                            gb.bm.flagCntAround(mb) != mb.getMineCntAround()){//没被扫过的等于自身周围雷数，并且旗数不等于雷数
                        gb.scorer.addFlag(mb.getMineCntAround()-gb.bm.flagCntAround(mb));
                        gb.bm.autoFlag(mb);//插旗
                        return;
                    }else if(gb.bm.flagCntAround(mb) == mb.getMineCntAround() &&
                            gb.bm.notSweepedCntAround(mb) != gb.bm.flagCntAround(mb)){//周围旗数等于自身数字，并且不等于没被扫过的
                        //左右键同击
                        autoEvents.mousePressed(mb, AutoEvents.LEFT_RIGHT);
                        mb.setBackground(new Color(184, 207, 229));//双击颜色
                        try {
                            Thread.sleep(pressTime);
                        } catch (InterruptedException e) {}
                        autoEvents.mouseReleased(mb, AutoEvents.LEFT_RIGHT);
                        mb.setBackground(MineButton.backColor);//颜色复原
                        return;
                    }
                }
            }
        }
        //以上两种情况不成立，即没有确定的一步，就碰运气点
        /*以下是按顺序点*/
//        for(int i = 0; i < gb.bm.getRows(); i++) {
//            for (int j = 0; j < gb.bm.getCols(); j++) {
//                MineButton mb = gb.bm.getMineButton(i, j);
//                if(!mb.isSweeped() && !mb.isFlag()){//没被扫过不是旗，就点
//                    autoEvents.mousePressed(mb, AutoEvents.LEFT);
//                    try {
//                        Thread.sleep(pressTime);
//                    } catch (InterruptedException e) {}
//                    autoEvents.mouseReleased(mb, AutoEvents.LEFT);
//                    autoEvents.mouseClicked(mb, AutoEvents.LEFT);
//                    return;
//                }
//            }
//        }
        /*以下是随机点*/
        int x, y;
        Random random = new Random();
        while(true){
            x = random.nextInt(gb.bm.getCols());
            y = random.nextInt(gb.bm.getRows());
            MineButton mb = gb.bm.getMineButton(y, x);
            if(mb.isFlag() || mb.isSweeped())   continue;
            autoEvents.mousePressed(mb, AutoEvents.LEFT);
            try {
                Thread.sleep(pressTime);
            } catch (InterruptedException e) {}
            autoEvents.mouseReleased(mb, AutoEvents.LEFT);
            autoEvents.mouseClicked(mb, AutoEvents.LEFT);
            return;
        }
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

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}

/**
 * 雷区鼠标事件模拟器 - 模拟事件的发生，需要时调用相应的事件方法
 */
class AutoEvents {
    GameBiz gb;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int LEFT_RIGHT = 2;

    public AutoEvents(GameBiz gb){
        this.gb = gb;
    }

    //鼠标压下
    public void mousePressed(MineButton mb, int button) {
        if(!gb.isFail() && !gb.isWin()){//没输赢才反应

            if(button == LEFT_RIGHT){//如果左右键同时按下
                if(mb.isSweeped()) {//如果是被扫了的
                    gb.bm.showAround(mb);
                    gb.reseter.setIconPress();
                }
            }
            //左键
            else if(!mb.isFlag() && button == LEFT){
                mb.setPressed();
                gb.reseter.setIconPress();
            }
            //右键 - 插旗   (是第一下，也反应)不在这里判赢
            else if(!mb.isFlag() && !mb.isUnknown() && !mb.isSweeped() && button == RIGHT){
                mb.setHasFlag();//设为已经插旗
                gb.scorer.addFlag();//计分器

            }else if(mb.isFlag() && !mb.isSweeped() && button == RIGHT){
                mb.removeFlag();//再点一下去掉旗子，并且加问号
                mb.setHasUnknown();
                gb.scorer.removeFlag();//计分器
            }else if(mb.isUnknown() && button == RIGHT){
                mb.removeUnknown();//再点一下取消问号
            }
        }
    }

    //松开
    public void mouseReleased(MineButton mb, int button) {
        if(!gb.isFail() && !gb.isWin()){
            //左右键按下
            if(mb.isSweeped() && button == LEFT_RIGHT){//如果是已被扫 - 松开时进行触雷判断
                    if(gb.bm.coverAround(mb, gb)) {//输了
                        gb.fail(mb);
                        gb.bm.showAutoTouched(mb);
                        return;
                    }
                }
            if(!gb.isWin())
                gb.reseter.setIconDefault();
        }
    }

    //点击
    public void mouseClicked(MineButton mb, int button) {
        if(!gb.isFail() && !gb.isWin()){
            if(gb.isFirst() && button == LEFT){//如果是点的第一下 - 初始化雷和计时器
                //为避免第一次点到雷
                gb.mc.createMines(gb.bm, mb.getIx(), mb.getIy());//生成雷
                gb.counter.countMines();//计算雷
                gb.timer.restart();
                gb.setFirst(false);
            }
            if(gb.isPause() && button == LEFT) {//如果停止，则开始计时
                gb.timer.end();//开始之前end一次比较保险
                gb.timer.start();
                gb.setPause(false);
            }
            else if(button == LEFT) {//左击一下
                //传入有动作的按钮
                gb.leftClickResult(mb);
            }
        }
    }
}
