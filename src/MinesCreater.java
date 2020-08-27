import java.io.Serializable;
import java.util.Random;

/**
 * 生成雷并相关到按钮上
 */

public class MinesCreater implements Serializable {
    private static final long serialVersionUID = 7077516197894917713L;
    private int level;
    private int minesCnt;
    public static final int MAX_MINESCNT = 600;//最大雷数
    public static final int MIN_MINESCNT = 1;//最小雷数
    public static final int MIN_NONMINESCNT = 1;//最小非雷数

    public MinesCreater(int level){
        this.level = level;
        if(level != 4)//自定义
            setMinesCnt();
    }

    //自定义
    public void customize(int minesCnt){
        this.minesCnt = minesCnt;
    }

    //生成雷 - 避免传进来的坐标是雷
    public void createMines(ButtonsMap bm, int X, int Y){
        int cnt = this.minesCnt;
        int x, y;
        Random random = new Random();
        while(cnt-- != 0){
            x = random.nextInt(bm.getCols());
            y = random.nextInt(bm.getRows());
            //这里x和y的位置
            if(bm.getMineButton(y, x).isMine() || x == X && y == Y) {//也避免传进来的坐标是雷
                cnt++;
                continue;
            }
            //设置为有雷
            bm.getMineButton(y, x).setMine(true);
        }
    }

    private void setMinesCnt(){
        if(level == 1){
            this.minesCnt = 10;
        }else if(level == 2){
            this.minesCnt = 40;
        }else if(level == 3){
            this.minesCnt = 99;
        }else{
            this.minesCnt = 10;
        }
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMinesCnt() {
        return minesCnt;
    }

    public void setMinesCnt(int minesCnt) {
        this.minesCnt = minesCnt;
    }
}
