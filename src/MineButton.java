import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 雷区按钮
 */

public class MineButton extends JButton implements Serializable {
    private static final long serialVersionUID = 731990634439768153L;
    /**是否扫除*/
    private boolean isSweeped;
    /**是否是雷*/
    private boolean isMine;
    /**是否插旗*/
    private boolean isFlag;
    /**是否问号*/
    private boolean isUnknown;
    /**周围雷数*/
    private int mineCntAround;
    /**坐标*/
    private int ix, iy;

    /*颜色*/
    public static final Color backColor = new Color(217, 217, 217);

    public MineButton(){
        setFlag(false);
        setSweeped(false);
        setMine(false);
        setUnknown(false);
        setMineCntAround(0);
        this.setPreferredSize(new Dimension(30, 30));
        this.setBackground(backColor);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setFocusable(false);//设置为不可对焦，即点了之后不会出现线框
    }
    //属性展示
    @Override
    public String toString() {
        return "[isMine="+isMine+", isSweeped="+isSweeped+", isFlag="+isFlag+", isUnknown="+isUnknown+", mineCntAround="+mineCntAround+"]";
    }

    //重写的设置内容方法，传入数字，自动按颜色显示
    public void setText(int cnt){
        Color color = null;
        if(cnt == 1){
            color = Color.blue;
        }else if(cnt == 2){
            color = new Color(14, 119, 90);
        }else if(cnt == 3){
            color = Color.red;
        }else if(cnt == 4){
            color = new Color(37, 43, 154);
        }else if(cnt == 5){
            color = new Color(162, 62, 27);
        }else if(cnt == 6){
            color = new Color(85, 181, 180);
        }else if(cnt == 7){
            color = Color.black;
        }else if(cnt == 8){
            color = Color.gray;
        }
        this.setText(String.valueOf(cnt));
        this.setForeground(color);
        this.setFont(new Font("Calibri", 1, 28));
    }

    //设置已经被扫了（数字）
    public void setHasSweeped(){
        //不是雷和旗
        if(!this.isMine() && !this.isFlag()){
            this.setText(null);//先清空
            this.setBorder(BorderFactory.createLineBorder(new Color(183, 183, 183)));//按下后的背景线条颜色
            if(mineCntAround != 0)
                this.setText(this.mineCntAround);
            this.setSweeped(true);
            this.setUnknown(false);//被扫了就没疑问了
        }
    }
    //设置已经插旗
    public void setHasFlag(){
        if(isSweeped)//被扫的不能插旗
            return;
        int width = 25;
        int height = 25;
        this.setText(null);
        ImageIcon mineIcon = new ImageIcon(MineButton.class.getResource("/icons/flag.png"));
        mineIcon.setImage(mineIcon.getImage().getScaledInstance(width, height,Image.SCALE_SMOOTH));
        this.setIcon(mineIcon);//慢应该是因为加载图片
        this.setFlag(true);//设为已经插旗
    }
    //设置除去旗子
    public void removeFlag(){
        this.setIcon(null);
        this.setFlag(false);
    }
    //设置疑问
    public void setHasUnknown(){
        this.setText("?");
        this.setForeground(Color.BLACK);
        this.setFont(new Font("Consoles", 1, 18));
        this.setUnknown(true);
    }
    //设置去除疑问
    public void removeUnknown(){
        this.setText(null);
        this.setUnknown(false);
    }
    //设置被按下
    public void setPressed(){
        if(!this.isFlag && !this.isSweeped)//不是旗没被扫才会被按下
            this.setBorder(BorderFactory.createLineBorder(new Color(183, 183, 183)));
    }
    //设置被松开
    public void setReleased(){
        if(!this.isSweeped)//没被扫才松开
            this.setBorder(BorderFactory.createRaisedBevelBorder());
    }
    //显示按钮结果 - 参数：是否被踩
    //只管是雷和旗的
    public void showButtonResult(boolean isTouched){
        int width = 40;
        int height = 40;
        if(this.isMine()){
            if(isTouched)
                this.setBackground(Color.RED);//踩雷颜色
            this.setText(null);
            this.setBorder(BorderFactory.createLineBorder(new Color(183, 183, 183)));
            ImageIcon mineIcon = new ImageIcon(GameBiz.class.getResource("/icons/mine.png"));
            mineIcon.setImage(mineIcon.getImage().getScaledInstance(width, height,Image.SCALE_SMOOTH));
            this.setIcon(mineIcon);
            if(this.isFlag())//插对旗的情况
                this.setBackground(new Color(17, 225, 184));
        }else if(this.isFlag()){//插错旗的情况
            this.setBackground(Color.PINK);
        }
    }
    //显示按钮状态 - 一切
    public void showButtonStatus(){
        if(this.isSweeped){
            setHasSweeped();
        }else if(this.isFlag){
            setHasFlag();
        }else if(this.isUnknown){
            setHasUnknown();
        }
    }
    //重置
    public void reset(){
        setFlag(false);
        setSweeped(false);
        setMine(false);
        setUnknown(false);
        setMineCntAround(0);
        setBackground(backColor);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setIcon(null);
        setText(null);
    }

    //得到坐标
    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }

    public int getIy() {
        return iy;
    }

    public void setIy(int iy) {
        this.iy = iy;
    }

    //不是旗但是雷
    public boolean notFlagButMine(){
        if(!this.isFlag && this.isMine)
            return true;
        return false;
    }

    public boolean isSweeped(){
        return this.isSweeped;
    }
    public void setSweeped(boolean isSweeped){
        this.isSweeped = isSweeped;
    }
    public boolean isMine(){
        return this.isMine;
    }
    public void setMine(boolean isMine){
        this.isMine = isMine;
    }
    public boolean isFlag(){
        return this.isFlag;
    }
    public void setFlag(boolean isFlag){
        this.isFlag = isFlag;
    }
    public int getMineCntAround(){
        return this.mineCntAround;
    }
    public void setMineCntAround(int mineCntAround){
        this.mineCntAround = mineCntAround;
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public void setUnknown(boolean unknown) {
        isUnknown = unknown;
    }
}
