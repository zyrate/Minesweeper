import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

/**
 * 游戏业务逻辑类
 * 改动 - 亲测原版扫雷的判赢机制不是插对多少旗，而是非雷方块是否全部被扫开，所以应该在左键判赢而不是右键
 */

public class GameBiz implements Serializable{//可序列化接口，用于存读档
    private static final long serialVersionUID = 715919700682963666L;
    GameWindow gw;
    MinesCreater mc;
    MinesCounter counter;
    ButtonsMap bm;
    Scorer scorer;
    Timer timer;
    Reseter reseter;
    GameMenu gm;
    private boolean isFail = false;//是否失败
    private boolean isWin = false;//是否赢
    private boolean isFirst = true;//是否第一次点击
    private boolean isPause = false;//是否暂停
    private GameBiz self = this;//自己的指针
    public Date date;//获取时间

    transient private AutoBiz autoBiz;//不序列化
    transient private SettingDialog sd;//设置速度对话框
    /**菜单事件**/
    public static final int LEVEL1 = 0;
    public static final int LEVEL2 = 1;
    public static final int LEVEL3 = 2;
    public static final int LEVEL4 = 3;
    public static final int FLAG_ALL = 4;
    public static final int UNFLAG_ALL = 5;
    public static final int RECORD = 6;
    public static final int SAVE = 7;
    public static final int READ = 8;
    public static final int START = 9;
    public static final int ABOUT = 10;
    public static final int SPEED = 11;
    public static final int STOP = 12;

    public GameBiz(){}

    public GameBiz(GameWindow gw, MinesCreater mc, MinesCounter counter,
                   ButtonsMap bm, Scorer scorer, Timer timer, Reseter reseter, GameMenu gm){
        this.gm = gm;
        this.gw = gw;
        this.mc = mc;
        this.counter = counter;
        this.bm = bm;
        this.scorer = scorer;
        this.timer = timer;
        this.reseter = reseter;

        sd = new SettingDialog(autoBiz, gw);
        init();
    }

    //初始化
    public void init(){
        gw.add(bm);//把地图加到窗口里
        this.addListener();//添加监听器
        gw.add(scorer);
        gw.add(timer);
        gw.add(reseter);
        gw.set(gm);//!设置！菜单栏，不是添加
        gw.pack();
        gw.setLocationRelativeTo(null);//居中显示！！！

    }

    //同等级重置
    public void reset(){
        reseter.reset();

        this.setFail(false);
        this.setWin(false);
        this.setFirst(true);
    }

    //重置
    public void reset(int level){
        //之前的窗口关闭
        gw.dispose();
        //接收传回来的新对象
        LinkedList list = reseter.reset(level);
        this.gw = (GameWindow) list.get(0);
        this.bm = (ButtonsMap) list.get(1);
        this.mc = (MinesCreater) list.get(2);
        this.counter = (MinesCounter) list.get(3);
        this.scorer = (Scorer) list.get(4);
        this.timer = (Timer) list.get(5);
        this.gm = (GameMenu) list.get(6);
        this.reseter = new Reseter(bm, scorer, timer, mc, counter);

        this.addListener();
        gw.add(reseter);
        this.setFirst(true);
        reset();//经常会出现换窗口后点击无反应的情况，为了保险起见，加这句话
    }
    //读档 - 重置
    public void resetForSaved(GameBiz gbNew){
        if(gbNew == null)   return;
        gw.dispose();

        this.gw = gbNew.gw;
        this.mc = gbNew.mc;
        this.counter = gbNew.counter;
        this.bm = gbNew.bm;
        this.scorer = gbNew.scorer;
        this.timer = gbNew.timer;
        this.reseter = gbNew.reseter;
        this.gm = gbNew.gm;
        this.isFail = gbNew.isFail;
        this.isWin = gbNew.isWin;
        this.isFirst = gbNew.isFirst;

        addListener();
        timer.end();
        setPause(true);
        gw.setVisible(true);
    }
    //自定义
    public void reset(int rows, int cols, int minesCnt){
        //之前的窗口销毁
        gw.dispose();
        //接收传回来的新对象
        LinkedList list = reseter.reset(rows, cols, minesCnt);
        this.gw = (GameWindow) list.get(0);
        this.bm = (ButtonsMap) list.get(1);
        this.mc = (MinesCreater) list.get(2);
        this.counter = (MinesCounter) list.get(3);
        this.scorer = (Scorer) list.get(4);
        this.timer = (Timer) list.get(5);
        this.gm = (GameMenu) list.get(6);
        this.reseter = new Reseter(bm, scorer, timer, mc, counter);

        this.addListener();
        gw.add(reseter);
        this.setFirst(true);
        reset();//经常会出现换窗口后点击无反应的情况，为了保险起见，加这句话
    }


    //事件处理 - 这里原来的死循环太费内存，改成了多线程
    //点菜单的时候，它会执行完再关闭菜单，这样可以解决
    public void eventDeal(int event){
        new Thread(){
            @Override
            public void run() {
                if(event == LEVEL1){
                    reset(1);
                }else if(event == LEVEL2){
                    reset(2);
                }else if(event == LEVEL3){
                    reset(3);
                }else if(event == LEVEL4){
                    new InDialog(gw, self);//传入外部类的指针，因为要调用方法资源
                }else if(event == FLAG_ALL){
                    if(!isWin() && !isFail()) {//如果输了或赢了就不插拔旗了
                        scorer.setFlagCnt(bm.flagAll());
                        scorer.update();
                    }
                }else if(event == UNFLAG_ALL){
                    if(!isWin() && !isFail()) {//如果输了或赢了就不插拔旗了
                        bm.unFlagAll();
                        scorer.setFlagCnt(0);
                        scorer.update();
                    }
                }else if(event == RECORD){
                    scorer.showRecord();
                }else if(event == SAVE){
                    timer.end();//存档时暂停
                    Archiver ar = new Archiver(gw);
                    ar.chooseToSave(self);//存档
                    if(!isFirst && !isFail && !isWin)
                        timer.start();//存档后继续
                }else if(event == READ){
                    Archiver ar = new Archiver(gw);
                    self.resetForSaved(ar.chooseToRead());//读档
                }else if(event == START){//这里的开始，停止每次都是一个新线程，和对话框相关联
                    if(autoBiz != null){
                        autoBiz.setPause(true);
                    }
                    autoBiz = new AutoBiz(self);
                    autoBiz.setSleepTime(sd.getSleepTime());
                    autoBiz.setPressTime(sd.getPressTime());
                    autoBiz.setPause(false);
                    sd.setAutoBiz(autoBiz);//设为不空
                    autoBiz.start();
                }else if(event == STOP){
                    if(autoBiz != null)    autoBiz.setPause(true);
                    sd.setAutoBiz(null);//设为空
                }else if(event == SPEED){
                    sd.setLocationRelativeTo(gw);
                    sd.setVisible(true);
                }else if(event == ABOUT){
                    ImageIcon showIcon = new ImageIcon(GameBiz.class.getResource("/icons/win.png"));
                    showIcon.setImage(showIcon.getImage().getScaledInstance(100, 100,Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(gw, "作者：郑云瑞\n开发语言：Java\n开发环境：Intellij Idea\n完成时间：2018-12-10", "关于", JOptionPane.OK_OPTION, showIcon);
                }
            }
        }.start();
    }

    //添加监听器
    public void addListener(){
        //雷区监听
        for(int i = 0; i < bm.getRows(); i++){
            for(int j = 0; j < bm.getCols(); j++){
                bm.getMineButton(i, j).addMouseListener(new MineMouseListener());
                bm.getMineButton(i, j).addActionListener(new MineActionListener());
            }
        }
        //重置器监听
        this.reseter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        this.reseter.addMouseListener(new MouseAdapter() {
            //压下
            @Override
            public void mousePressed(MouseEvent e) {
                Reseter reseter = (Reseter)e.getSource();
                //左键按下
                if(e.getButton() == MouseEvent.BUTTON1){
                    reseter.setPressed();
                }
            }
            //松开
            @Override
            public void mouseReleased(MouseEvent e) {
                Reseter reseter = (Reseter)e.getSource();
                //左键按下
                if(e.getButton() == MouseEvent.BUTTON1){
                    reseter.setReleased();
                }
            }
        });
        //菜单项监听
        this.gm.getLevel1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(LEVEL1);
            }
        });
        this.gm.getLevel2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(LEVEL2);
            }
        });
        this.gm.getLevel3().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(LEVEL3);
            }
        });
        //自定义
        this.gm.getLevel4().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(LEVEL4);
            }
        });

        this.gm.getFlagAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(FLAG_ALL);
            }
        });
        this.gm.getUnFlagAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(UNFLAG_ALL);
            }
        });
        this.gm.getRecord().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(RECORD);
            }
        });
        this.gm.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(SAVE);
            }
        });
        this.gm.getRead().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(READ);
            }
        });
        this.gm.getExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.gm.getAbout().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(ABOUT);
            }
        });
        this.gm.getStart().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(START);
            }
        });
        this.gm.getStop().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(STOP);
            }
        });
        this.gm.getSpeed().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDeal(SPEED);
            }
        });
    }

    //左键按下某个按钮的结果
    public void leftClickResult(MineButton mb){
        if(!mb.isSweeped() && !mb.isFlag()){
            if(mb.isMine()) {//踩到雷啦
                fail(mb);
            }
            else if(mb.isFlag()){//点旗子不管用
            }else{
                autoSweep(mb);//自动设置为已扫
                //在扫的时候判断输赢
                if(judge()){//赢了
                    int currTime = timer.getCurrTime();
                    timer.end();
                    reseter.setIconWin();
                    scorer.setFlagCnt(bm.flagAll()+scorer.getFlagCnt());//赢了就自动全扫 - 别忘了加
                    scorer.update();
                    scorer.compToRecord(gw, mc.getLevel(), currTime);//比较记录
                    setWin(true);
                }
            }
        }
    }

    //自动开辟 - 递归
    public void autoSweep(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();

        if(mb.isFlag() || mb.isMine() || mb.isSweeped()){//问号是可以被扫的
            return;
        }else if(mb.getMineCntAround() != 0){
            mb.setHasSweeped();
            scorer.sweepOne();//已扫块数+1
            return;
        }
        //雷数是0的情况
        mb.setHasSweeped();
        scorer.sweepOne();//已扫块数+1

        //递归
        if(y-1 >= 0) autoSweep(bm.getMineButton(y-1, x));
        if(y+1 < bm.getRows()) autoSweep(bm.getMineButton(y+1, x));
        if(x+1 < bm.getCols()) autoSweep(bm.getMineButton(y, x+1));
        if(x-1 >= 0) autoSweep(bm.getMineButton(y, x-1));
        if(y-1 >= 0 && x-1 >= 0) autoSweep(bm.getMineButton(y-1, x-1));
        if(y+1 < bm.getRows() && x+1 < bm.getCols()) autoSweep(bm.getMineButton(y+1, x+1));
        if(x+1 < bm.getCols() && y-1 >= 0) autoSweep(bm.getMineButton(y-1, x+1));
        if(x-1 >= 0 && y+1 < bm.getRows()) autoSweep(bm.getMineButton(y+1, x-1));
    }

    //失败
    public void fail(MineButton mb){

        //设置为输了
        this.setFail(true);
        reseter.setIconFail();
        //计时器处理
        timer.end();
        //地图处理
        for(int i = 0; i < bm.getRows(); i++){
            for(int j = 0; j < bm.getCols(); j++){
                bm.getMineButton(i, j).showButtonResult(false);//不是被踩
            }
        }
        mb.showButtonResult(true);//被踩
    }

    //判断是否赢
    public boolean judge(){
        //当已扫数等于总数减雷数
        if(scorer.getSweepedCnt() == bm.getTotalCnt() - mc.getMinesCnt()){
            return true;
        }
        return false;
    }
    /**
     * 雷区动作监听器 - 点的更顺畅
     */
    class MineActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isFail() && !isWin()) {
                MineButton mb = (MineButton) e.getSource();
                if (isFirst()) {//如果是点的第一下 - 初始化雷和计时器
                    //为避免第一次点到雷
                    mc.createMines(bm, mb.getIx(), mb.getIy());//生成雷
                    counter.countMines();//计算雷
                    timer.restart();
                    setFirst(false);
                }
                if (isPause()) {//如果停止，则开始计时
                    timer.end();//开始之前end一次比较保险
                    timer.start();
                    setPause(false);
                }
                leftClickResult(mb);
            }
        }
    }

    /**
     * 雷区鼠标监听器
     */
    class MineMouseListener extends MouseAdapter {
        boolean isLandR = false;//是否左右键一块按，目前只能想到这样了
        @Override//鼠标压下
        public void mousePressed(MouseEvent e) {
            if(!isFail() && !isWin()){//没输赢才反应
                MineButton mb = (MineButton)e.getSource();
                if(e.getModifiersEx()==(e.BUTTON3_DOWN_MASK + e.BUTTON1_DOWN_MASK)){//如果左右键同时按下
                    if(mb.isSweeped()) {//如果是被扫了的
                        bm.showAround(mb);
                        reseter.setIconPress();
                        isLandR = true;
                    }
                }
                //左键
                else if(!mb.isFlag() && e.getButton() == MouseEvent.BUTTON1){
                    mb.setPressed();
                    reseter.setIconPress();
                }
                //右键 - 插旗   (是第一下，也反应)不在这里判赢
                else if(!mb.isFlag() && !mb.isUnknown() && !mb.isSweeped() && e.getButton() == MouseEvent.BUTTON3){
                    mb.setHasFlag();//设为已经插旗
                    scorer.addFlag();//计分器

                }else if(mb.isFlag() && !mb.isSweeped() && e.getButton() == MouseEvent.BUTTON3){
                    mb.removeFlag();//再点一下去掉旗子，并且加问号
                    mb.setHasUnknown();
                    scorer.removeFlag();//计分器
                }else if(mb.isUnknown() && e.getButton() == MouseEvent.BUTTON3){
                    mb.removeUnknown();//再点一下取消问号
                }
            }
        }

        @Override//松开
        public void mouseReleased(MouseEvent e) {
            if(!isFail() && !isWin()){
                MineButton mb = (MineButton)e.getSource();
                if(!mb.isFlag() && !mb.isSweeped() && e.getButton() == MouseEvent.BUTTON1){
                    mb.setReleased();
                }else if(mb.isSweeped()) {//左右击的情况
                    bm.coverAround(mb);//覆盖回去，不判断
                }
                reseter.setIconDefault();
            }
        }

        @Override//点击
        public void mouseClicked(MouseEvent e) {
            if(!isFail() && !isWin()){
                MineButton mb = (MineButton) e.getSource();
                //左右键按下
                if(mb.isSweeped() && isLandR){//如果是已被扫 - 松开时进行触雷判断
                    if(bm.coverAround(mb, self)) {//输了
                        fail(mb);
                        bm.showAutoTouched(mb);
                        isLandR = false;
                        return;
                    }
                }
            }
        }
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
