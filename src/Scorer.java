import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

/**
 * 计分器
 */

public class Scorer extends JButton implements Serializable{
    private static final long serialVersionUID = -7021786508730606485L;
    /**旗数*/
    private int flagCnt;
    /**雷数*/
    private int minesCnt;
    /**剩余雷数*/
    private int restMinesCnt;
    /**已扫块数*/
    private int sweepedCnt;

    //文件路径
    private static final String ScoresPath = "C:/MinesweeperData/scores.dat";
    //历史最佳
    private int lv1, lv2, lv3;
    private String lv1Name, lv2Name, lv3Name;
    private String levelName, inputName = "";

    GameWindow gw;

    //构造传入雷数
    public Scorer(int minesCnt){
        this.minesCnt = minesCnt;
        this.flagCnt = 0;
        this.sweepedCnt = 0;
        this.setPreferredSize(new Dimension(90, 50));
        this.setEnabled(true);//不可按
        this.setBackground(new Color(172, 172, 172));
        this.setForeground(new Color(162, 62, 27));
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setText(String.valueOf(this.getRestMinesCnt()));
        this.setFont(new Font("STENCIL STD", 1, 35));
        this.setFocusable(false);//设置为不可对焦
    }
    //插旗子
    public void addFlag(){
        flagCnt++;
        this.setText(String.valueOf(this.getRestMinesCnt()));
    }
    public void addFlag(int cnt){
        flagCnt+=cnt;
        this.setText(String.valueOf(this.getRestMinesCnt()));
    }
    //去旗子
    public void removeFlag(){
        flagCnt--;
        this.setText(String.valueOf(this.getRestMinesCnt()));
    }
    //扫数加一
    public void sweepOne(){
        sweepedCnt++;
    }
    //更新显示
    public void update(){
        this.setText(String.valueOf(this.getRestMinesCnt()));
    }

    //查看记录
    public void showRecord(){
        new ShowRecord();
    }

    //比较记录
    public void compToRecord(GameWindow gw, int level, int time){
        this.gw = gw;
        File f = new File(ScoresPath);
        if(!f.exists()){
            createFile();
        }
        readData();
        if(level == 1 && time < lv1){
            levelName = "初级";
            new NewRecord(time);
            if(!inputName.equals("")) {//用户按了回车
                lv1Name = inputName;
                lv1 = time;
                saveData();
                showRecord();
            }
        }else if(level == 2 && time < lv2){
            levelName = "中级";
            new NewRecord(time);
            if(!inputName.equals("")) {//用户按了回车
                lv2Name = inputName;
                lv2 = time;
                saveData();
                showRecord();
            }
        }else if(level == 3 && time < lv3){
            levelName = "高级";
            new NewRecord(time);
            if(!inputName.equals("")) {//用户按了回车
                lv3Name = inputName;
                lv3 = time;
                saveData();
                showRecord();
            }
        }
    }

    //存储数据
    public void saveData(){
        File f = new File(ScoresPath);
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(f));
            writer.write(lv1 + "\n");
            writer.write(lv2 + "\n");
            writer.write(lv3 + "\n");
            writer.write(lv1Name + "\n");
            writer.write(lv2Name + "\n");
            writer.write(lv3Name + "\n");
        }catch (Exception e) {
            System.out.println("error!");
            return;
        }finally {
            try{
                writer.close();
            }catch (Exception e){
                System.out.println("error!");
                return;
            }
        }
    }

    //读取数据
    public void readData(){
        File f = new File(ScoresPath);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(f));
            lv1 = Integer.valueOf(reader.readLine());
            lv2 = Integer.valueOf(reader.readLine());
            lv3 = Integer.valueOf(reader.readLine());
            lv1Name = String.valueOf(reader.readLine());
            lv2Name = String.valueOf(reader.readLine());
            lv3Name = String.valueOf(reader.readLine());
        }catch (Exception e){
            System.out.println("error!");
            return;
        }finally {
            try{
                reader.close();
            }catch (Exception e){
                System.out.println("error!");
                return;
            }
        }
    }

    //初始化新文件
    public void createFile(){
        File f = new File(ScoresPath);
        BufferedWriter writer = null;
        try{
            new File("C:/MinesweeperData").mkdir();
            f.createNewFile();
            writer = new BufferedWriter(new FileWriter(f));
            writer.write("999\n");
            writer.write("999\n");
            writer.write("999\n");
            writer.write("--\n");
            writer.write("--\n");
            writer.write("--\n");
        }catch (Exception e) {
            System.out.println("error!");
            return;
        }finally {
            try{
                writer.close();
            }catch (Exception e){
                System.out.println("error!");
                return;
            }
        }
    }

    /**
     * 破纪录对话框
     */
    class NewRecord extends JDialog{
        private JPanel p1, p2;
        private JLabel l1, l2;
        private JTextField tf;
        private String name;
        private NewRecord self = this;//自己指针

        public NewRecord(int time){
            setModal(true);//不允许点其他

            setTitle("破纪录啦");
            setSize(410, 200);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭后自动销毁
            setLocationRelativeTo(gw);//位置，基于主窗体
            //图标
            ImageIcon gameIcon = new ImageIcon(NewRecord.class.getResource("/icons/record.png"));
            gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
            this.setIconImage(gameIcon.getImage());
            setResizable(false);
            Font font = new Font("楷体", 1, 20);
            setLayout(new GridLayout(2, 1, 8, 8));
            p1 = new JPanel();p2 = new JPanel();
            l1 = new JLabel();l2 = new JLabel();tf = new JTextField(28);
            add(p1);add(p2);
            p1.add(l1);p1.add(l2);p2.add(tf);

            p1.setLayout(new GridLayout(2, 1, 8, 8));
            l1.setFont(font);
            l1.setText(" 恭喜你！创造了"+levelName+"关卡的记录："+time+"秒");
            l2.setFont(font);
            l2.setText(" 请输入你的尊姓大名：");

            tf.setFont(font);
            addListener();

            setVisible(true);
        }
        public void addListener(){
            tf.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if(e.getKeyChar() == KeyEvent.VK_ENTER){
                        inputName = tf.getText();
                        if(inputName.equals(""))
                            inputName = "匿名";
                        self.dispose();
                    }
                }
            });
        }
    }
    /**
     * 查看纪录对话框
     */
    class ShowRecord extends JDialog{
        private JPanel p;
        private JLabel l1, l2, l3, l4;
        private JButton b1, b2;
        private ShowRecord self = this;//自己指针

        public ShowRecord(){
            setModal(true);//不允许点其他

            setTitle("纪录榜");
            setSize(380, 300);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭后自动销毁
            setLocationRelativeTo(gw);//位置，基于主窗体
            //图标
            ImageIcon gameIcon = new ImageIcon(ShowRecord.class.getResource("/icons/records.png"));
            gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
            this.setIconImage(gameIcon.getImage());

            setResizable(true);
            Font font = new Font("楷体", 1, 22);
            setLayout(new GridLayout(5, 1, 8, 8));
            p = new JPanel();
            l1 = new JLabel();l2 = new JLabel();l3 = new JLabel();l4 = new JLabel();
            b1 = new JButton();b2 = new JButton();
            add(l1);add(l2);add(l3);add(l4);add(p);

            p.setLayout(new FlowLayout(FlowLayout.CENTER, 20,5));
            p.add(b1);p.add(b2);

            l1.setFont(font);
            l2.setFont(font);   l2.setBorder(BorderFactory.createRaisedBevelBorder());
            l3.setFont(font);   l3.setBorder(BorderFactory.createRaisedBevelBorder());
            l4.setFont(font);   l4.setBorder(BorderFactory.createRaisedBevelBorder());
            b1.setFont(font);
            b1.setText("重置");
            b1.setFocusable(false);
            b2.setFont(font);
            b2.setText("取消");
            b2.setFocusable(false);
            readData();
            showContent();
            addListener();

            setVisible(true);
        }
        public void showContent(){
            l1.setText("___________历史最佳___________");
            l2.setText(" 初级：   "+lv1+"秒     "+lv1Name);
            l3.setText(" 中级：   "+lv2+"秒     "+lv2Name);
            l4.setText(" 高级：   "+lv3+"秒     "+lv3Name);
        }
        public void addListener(){
            b1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        //重置
                        createFile();
                        readData();
                        showContent();
                    }
                }
            });
            b2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        //取消
                        self.dispose();
                    }
                }
            });
        }
    }


    public int getFlagCnt() {
        return flagCnt;
    }

    public void setFlagCnt(int flagCnt) {
        this.flagCnt = flagCnt;
    }

    public int getMinesCnt() {
        return minesCnt;
    }

    public void setMinesCnt(int minesCnt) {
        this.minesCnt = minesCnt;
    }

    public int getRestMinesCnt() {
        return minesCnt-flagCnt;
    }

    public void setRestMinesCnt(int restMinesCnt) {
        this.restMinesCnt = restMinesCnt;
    }

    public int getSweepedCnt() {
        return sweepedCnt;
    }

    public void setSweepedCnt(int sweepedCnt) {
        this.sweepedCnt = sweepedCnt;
    }
}
