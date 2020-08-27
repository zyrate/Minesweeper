import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

/**
 * 自定义的对话框类
 */
class InDialog extends JDialog  implements Serializable {
    private static final long serialVersionUID = 2841667548205423237L;
    private JTextField tRows, tCols, tCnt;
    private JLabel lRows, lCols, lCnt;
    private JPanel p1, p2, p3, p4, p5;
    private JButton b1, b2;
    private Document d1, d2, d3;

    GameBiz gb;
    JDialog self = this;//自己的指针

    private String sRows, sCols, sCnt;
    private Color defaultColor;


    public InDialog(GameWindow gw, GameBiz gb){
        setModal(true);//不允许点其他

        setTitle("自定义");
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭后自动销毁
        setLocationRelativeTo(gw);//位置，基于主窗体
        //图标
        ImageIcon gameIcon = new ImageIcon(InDialog.class.getResource("/icons/setting.png"));
        gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
        this.setIconImage(gameIcon.getImage());

        setResizable(true);
        Font font = new Font("宋体", 1, 20);
        lRows = new JLabel("行数");
        tRows = new JTextField(12);
        lCols = new JLabel("列数");
        tCols = new JTextField(12);
        lCnt = new JLabel("雷数");
        tCnt = new JTextField(12);
        lRows.setFont(font);
        tRows.setFont(font);
        lCols.setFont(font);
        tCols.setFont(font);
        lCnt.setFont(font);
        tCnt.setFont(font);
        p1 = new JPanel(new GridLayout(3, 1));
        p2 = new JPanel(new FlowLayout());
        p3 = new JPanel(new FlowLayout());
        p4 = new JPanel(new FlowLayout());

        p2.add(lRows);
        p2.add(tRows);
        p3.add(lCols);
        p3.add(tCols);
        p4.add(lCnt);
        p4.add(tCnt);

        p1.add(p2);
        p1.add(p3);
        p1.add(p4);

        p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        //按钮
        b1 = new JButton("确定");
        b2 = new JButton("取消");
        b1.setFont(new Font("", 1, 18));
        b2.setFont(new Font("", 1, 18));
        b1.setBackground(new Color(217, 217, 217));
        b2.setBackground(new Color(217, 217, 217));
        p5.add(b1);
        p5.add(b2);
        add(p1, BorderLayout.NORTH);
        add(p5, BorderLayout.SOUTH);

        this.gb = gb;
        deal();
        //setVisible与setModal的位置是个谜，会阻塞线程
        setVisible(true);
    }

    //其他处理
    private void deal(){
        sRows = String.format("%d-%d", ButtonsMap.MIN_ROWS, ButtonsMap.MAX_ROWS);
        sCols = String.format("%d-%d", ButtonsMap.MIN_COLS, ButtonsMap.MAX_COLS);
        sCnt = String.format("%d-%d", MinesCreater.MIN_MINESCNT, MinesCreater.MAX_MINESCNT);
        defaultColor = new Color(211, 211, 211);
        setDefault();
        //确定按钮最初不可按
        b1.setEnabled(false);
        addListener();
    }

    //设置默认显示
    public void setDefault(){
        b2.requestFocus();
        if(tRows.getText().equals("")){
            tRows.setText(sRows);
            tRows.setForeground(defaultColor);
        }
        if(tCols.getText().equals("")){
            tCols.setText(sCols);
            tCols.setForeground(defaultColor);
        }
        if(tCnt.getText().equals("")){
            tCnt.setText(sCnt);
            tCnt.setForeground(defaultColor);
        }
        //焦点监听
        tRows.addFocusListener(new JTextFieldHintListener(sRows));
        tCols.addFocusListener(new JTextFieldHintListener(sCols));
        tCnt.addFocusListener(new JTextFieldHintListener(sCnt));
        //文本监听
        d1 = tRows.getDocument();
        d2 = tCols.getDocument();
        d3 = tCnt.getDocument();
        d1.addDocumentListener(new TextListener());
        d2.addDocumentListener(new TextListener());
        d3.addDocumentListener(new TextListener());
    }

    //检查合规性
    public boolean check(){
        int rows, cols, cnt;
        try{
            rows = Integer.valueOf(tRows.getText());
            cols = Integer.valueOf(tCols.getText());
            cnt = Integer.valueOf(tCnt.getText());
        }catch (Exception e){
            return false;
        }
        //当满足以下条件时，自定义成功
        if(rows >= ButtonsMap.MIN_ROWS && rows <= ButtonsMap.MAX_ROWS &&
                cols >= ButtonsMap.MIN_COLS && cols <= ButtonsMap.MAX_COLS &&
                cnt >= MinesCreater.MIN_MINESCNT && cnt <= MinesCreater.MAX_MINESCNT &&
                cnt <= rows * cols - MinesCreater.MIN_NONMINESCNT){//至少有?个不是雷
            return true;
        }
        return false;
    }


    //监听
    public void addListener(){
        this.b1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(b1.isEnabled()){//只要确定按钮被按了就重置
                    gb.reset(Integer.valueOf(tRows.getText()), Integer.valueOf(tCols.getText()),
                            Integer.valueOf(tCnt.getText()));
                    self.dispose();
                }
            }
        });
        this.b2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                self.dispose();
            }
        });
        this.b1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(b1.isEnabled()){//只要确定按钮被按了就重置
                    gb.reset(Integer.valueOf(tRows.getText()), Integer.valueOf(tCols.getText()),
                            Integer.valueOf(tCnt.getText()));
                    self.dispose();
                }
            }
        });
    }
    /**
     * 文本监听
     */
    class TextListener implements DocumentListener{
        @Override
        public void changedUpdate(DocumentEvent e) {
            if(check()){
                b1.setEnabled(true);
            }else{
                b1.setEnabled(false);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if(check()){
                b1.setEnabled(true);
            }else{
                b1.setEnabled(false);
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if(check()){
                b1.setEnabled(true);
            }else{
                b1.setEnabled(false);
            }
        }
    }

    /**
     * 默认提示 - 焦点监听
     */
    class JTextFieldHintListener implements FocusListener{
        private String content;
        public JTextFieldHintListener(String content){
            this.content = content;
        }

        //获得焦点
        @Override
        public void focusGained(FocusEvent e) {
            JTextField t = (JTextField) e.getSource();
            if(t.getText().equals(content)){
                t.setText("");
                t.setForeground(Color.BLACK);
            }
        }
        //失去焦点
        @Override
        public void focusLost(FocusEvent e) {
            JTextField t = (JTextField) e.getSource();
            if(t.getText().equals("")){
                t.setText(content);
                t.setForeground(defaultColor);
            }
        }
    }

    public JButton getB1() {
        return b1;
    }

    public void setB1(JButton b1) {
        this.b1 = b1;
    }

    public JButton getB2() {
        return b2;
    }

    public void setB2(JButton b2) {
        this.b2 = b2;
    }
}