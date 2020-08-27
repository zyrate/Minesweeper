import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 存档器 - 用于存档和读档
 */

public class Archiver {
    //存档路径
    private static final String ARCHIVE_PATH = "C:/MinesweeperData/archives.sav";
    private GameBiz gb1 = null, gb2 = null, gb3 = null;//目前最多存三档
    private int chosen;//被选中的
    Color chosenColor = new Color(223, 245, 157);

    GameWindow gw;

    public Archiver(GameWindow gw){
        this.gw = gw;
    }

    //选择存档
    public void chooseToSave(GameBiz gb){
        read();//读出
        //选择
        showSaveDialog(gb);
    }
    //选择读档
    public GameBiz chooseToRead(){

        read();//读出
        //选择
        showReadDialog();
        if(chosen == 1){
            return gb1;
        }else if(chosen == 2){
            return gb2;
        }else if(chosen == 3){
            return gb3;
        }
        return null;
    }
    //显示存档对话框 - 获取位置
    public void showSaveDialog(GameBiz gb){
        new SaveDialog(gb);
    }
    //显示读档对话框 - 获取位置
    public void showReadDialog(){
        new ReadDialog();
    }

    //生成新文件
    public void createFile(){
        File f = new File(ARCHIVE_PATH);
        try{
            new File("C:/MinesweeperData").mkdir();
            f.createNewFile();
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    //存档
    public void save(){
        File f = new File(ARCHIVE_PATH);
        if(!f.exists()){
            createFile();
        }
        ObjectOutputStream oout = null;
        try{
            oout = new ObjectOutputStream(new FileOutputStream(f));
            oout.writeObject(gb1);
            oout.writeObject(gb2);
            oout.writeObject(gb3);
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }finally {
            try{
                oout.close();
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
    }
    //读档
    public void read(){
        File f = new File(ARCHIVE_PATH);
        if(!f.exists()){
            createFile();
        }
        ObjectInputStream oin = null;
        try{
            oin = new ObjectInputStream(new FileInputStream(f));
            gb1 = (GameBiz) oin.readObject();
            gb2 = (GameBiz) oin.readObject();
            gb3 = (GameBiz) oin.readObject();
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }finally {
            try{
                oin.close();
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
    }

    //返回开辟率 %
    public int getSweepedRate(GameBiz gb){
        int sweeped = 0;
        for(int i = 0; i < gb.bm.getRows(); i++){
            for(int j = 0; j < gb.bm.getCols(); j++){
                if(gb.bm.getMineButton(i, j).isSweeped())
                    sweeped++;
            }
        }
        int rate = (int)((1.0*sweeped / (gb.bm.getTotalCnt()-gb.mc.getMinesCnt()))*100);
        return rate;
    }
    /**
     * 存档对话框
     */
    class SaveDialog extends ParDialog{
        private JButton b1, b2, b3;
        private SaveDialog self = this;
        GameBiz gb;

        public SaveDialog(GameBiz gb){
            super("存档", "请从下面选择存档：");
            this.gb = gb;

            //图标
            ImageIcon gameIcon = new ImageIcon(SaveDialog.class.getResource("/icons/save.png"));
            gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
            this.setIconImage(gameIcon.getImage());

            b1 = new JButton("存档");
            b2 = new JButton("取消");
            b3 = new JButton("删除");
            Font font = new Font("宋体", 1, 20);
            b1.setFont(font);
            b2.setFont(font);
            b3.setFont(font);

            p2.add(b3);
            p2.add(b1);
            p2.add(b2);

            addListener();
            setVisible(true);//不能在父类
        }
        public void addListener(){
            b1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){//存档时
                        //记录日期
                        gb.date = new Date();

                        if(chosen == 1) gb1 = gb;
                        else if(chosen == 2) gb2 = gb;
                        else if(chosen == 3) gb3 = gb;
                        else return;
                        pp1.showMessage(gb1);
                        pp2.showMessage(gb2);
                        pp3.showMessage(gb3);
                        save();//写入
                    }
                }
            });
            b2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        self.dispose();
                        chosen = 0;//如果关闭，置为默认0
                    }
                }
            });
            b3.addMouseListener(new MouseAdapter() {//删除
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        if(chosen == 1) gb1 = null;
                        else if(chosen == 2) gb2 = null;
                        else if(chosen == 3) gb3 = null;
                        else return;
                        pp1.showMessage(gb1);
                        pp2.showMessage(gb2);
                        pp3.showMessage(gb3);
                        save();
                    }
                }
            });
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    chosen = 0;
                }
            });
        }
    }

    /**
     * 读档对话框
     */
    class ReadDialog extends ParDialog{
        private JButton b1, b2;
        private ReadDialog self = this;

        public ReadDialog(){
            super("读档", "请从下面选择读档：");

            //图标
            ImageIcon gameIcon = new ImageIcon(ReadDialog.class.getResource("/icons/read.png"));
            gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
            this.setIconImage(gameIcon.getImage());

            b1 = new JButton("读档");
            b2 = new JButton("取消");
            Font font = new Font("宋体", 1, 20);
            b1.setFont(font);
            b2.setFont(font);

            p2.add(b1);
            p2.add(b2);

            addListener();
            setVisible(true);//不能在父类
        }
        private void addListener(){
            b1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        self.dispose();
                    }
                }
            });
            b2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        self.dispose();
                        chosen = 0;//如果关闭，置为默认0
                    }
                }
            });
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    chosen = 0;
                }
            });
            pp1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {//双击
                        chosen = 1;
                        self.dispose();
                    }
                }
            });
            pp2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {//双击
                        chosen = 2;
                        self.dispose();
                    }
                }
            });
            pp3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {//双击
                        chosen = 3;
                        self.dispose();
                    }
                }
            });
        }
    }

    /**
     * 父对话框类
     */
    class ParDialog extends JDialog{
        protected JPanel p1, p2;
        protected PosPanel pp1, pp2, pp3;

        public ParDialog(String Title, String message){
            setModal(true);//不允许点其他

            setTitle(Title);
            setSize(450, 270);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭后自动销毁
            setLocationRelativeTo(gw);//位置，基于主窗体

            setResizable(false);
            Font font = new Font("宋体", 1, 20);
            setLayout(new BorderLayout());
            p1 = new JPanel();p2 = new JPanel();
            pp1 = new PosPanel(gb1);pp2 = new PosPanel(gb2);pp3 = new PosPanel(gb3);

            add(p1, BorderLayout.CENTER);add(p2, BorderLayout.SOUTH);
            p1.setLayout(new GridLayout(1, 3, 15, 8));
            p1.add(pp1);p1.add(pp2);p1.add(pp3);
            p1.setFont(font);
            p1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3), message));
            p2.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 1));

            addListener();
        }

        private void addListener(){//方法一定要定义成私有的，不然和子类的会乱
            pp1.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    pp1.setBackground(chosenColor);
                    pp2.setBackground(null);
                    pp3.setBackground(null);
                    chosen = 1;
                }
            });
            pp2.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    pp2.setBackground(chosenColor);
                    pp1.setBackground(null);
                    pp3.setBackground(null);
                    chosen = 2;
                }
            });
            pp3.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    pp3.setBackground(chosenColor);
                    pp2.setBackground(null);
                    pp1.setBackground(null);
                    chosen = 3;
                }
            });
        }
    }
    /**
     * 档位信息类
     */
    class PosPanel extends JPanel{
        private PosPanel self = this;
        JLabel l1, l2, l3, l4, l5;

        public PosPanel(GameBiz gb){
            this.setBorder(BorderFactory.createLoweredBevelBorder());
            this.setLayout(new GridLayout(5, 1));

            l1 = new JLabel();
            l2 = new JLabel();
            l3 = new JLabel();
            l4 = new JLabel();
            l5 = new JLabel();

            Font font = new Font("宋体", 1, 20);
            l1.setFont(font);
            l2.setFont(font);
            l3.setFont(font);
            l4.setFont(font);
            l5.setFont(font);

            this.add(l1);
            this.add(l2);
            this.add(l3);
            this.add(l4);
            this.add(l5);

            showMessage(gb);
            addListener();

        }
        //显示信息
        public void showMessage(GameBiz gb){
            if(gb != null) {
                String sLevel = null;
                if (gb.mc.getLevel() == 1) sLevel = "初级";
                else if (gb.mc.getLevel() == 2) sLevel = "中级";
                else if (gb.mc.getLevel() == 3) sLevel = "高级";
                else if (gb.mc.getLevel() == 4) sLevel = "自定义";

                l1.setText(sLevel);
                l2.setText(String.valueOf(gb.bm.getRows() + " X " + gb.bm.getCols()));
                l3.setText(String.valueOf("雷数：" + gb.mc.getMinesCnt()));
                l4.setText(String.valueOf("进度：" + getSweepedRate(gb) + "%"));
                if(gb.isFail())//输的残局
                    l4.setText(String.valueOf("进度：" + getSweepedRate(gb) + "%败"));
                l5.setText(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(gb.date)));
            }else{
                l1.setText(null);
                l2.setText(null);
                l3.setText(null);
                l4.setText(null);
                l5.setText(null);
            }
        }

        private void addListener(){
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    self.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2, true));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    self.setBorder(BorderFactory.createLoweredBevelBorder());
                }
            });
        }
    }
}
