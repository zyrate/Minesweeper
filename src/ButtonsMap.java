import java.io.Serializable;

/**
 * 按钮地图
 */
public class ButtonsMap implements Serializable{
    private static final long serialVersionUID = -331165561281752614L;
    /**行*/
    private int rows;
    /**列*/
    private int cols;
    /**按钮数组*/
    private MineButton[][] mineButtons;
    /**等级*/
    private int level;
    /**最小列数*/
    public static final int MIN_COLS = 8;
    /**最小行数*/
    public static final int MIN_ROWS = 2;
    /**最大列数*/
    public static final int MAX_COLS = 60;
    /**最大行数*/
    public static final int MAX_ROWS = 30;
    public ButtonsMap(int level){
        this.level = level;
        if(level == 1){
            this.rows = 9;
            this.cols = 9;
        }else if(level == 2){
            this.rows = 16;
            this.cols = 16;
        }else if(level == 3){
            this.rows = 16;
            this.cols = 30;
        }else if(level == 4){//自定义，先不生成地图，通过customize方法实现
            return;
        }else{
            this.rows = 9;
            this.cols = 9;
        }
        createMap();
    }
    //自定义
    public void customize(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        createMap();
    }
    //创建地图
    public void createMap(){
        mineButtons = new MineButton[rows][cols];
        for(int i = 0; i < rows; i++){
            mineButtons[i] = new MineButton[cols];
            for(int j = 0; j < cols; j++){
                mineButtons[i][j] = new MineButton();
                mineButtons[i][j].setIx(j);
                mineButtons[i][j].setIy(i);
            }
        }
    }
    //重置按钮属性
    public void resetButtons(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                mineButtons[i][j].reset();
            }
        }
    }
    //全部插旗 - 返回插了几个
    public int flagAll(){
        int cnt = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(!mineButtons[i][j].isSweeped() && !mineButtons[i][j].isFlag()){//没被扫，不是旗
                    mineButtons[i][j].setHasFlag();
                    cnt++;
                }
            }
        }
        return cnt;
    }
    //全部去旗
    public void unFlagAll(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(!mineButtons[i][j].isSweeped()){//没被扫
                    mineButtons[i][j].removeFlag();
                }
            }
        }
    }
    //显示按钮们状态
    public void showMapStatus(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                mineButtons[i][j].showButtonStatus();
            }
        }
    }
    //显示周围
    public void showAround(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        if(y-1 >= 0) mineButtons[y-1][x].setPressed();
        if(y+1 < this.getRows()) mineButtons[y+1][x].setPressed();
        if(x+1 < this.getCols()) mineButtons[y][x+1].setPressed();
        if(x-1 >= 0) mineButtons[y][x-1].setPressed();
        if(y-1 >= 0 && x-1 >= 0) mineButtons[y-1][x-1].setPressed();
        if(y+1 < this.getRows() && x+1 < this.getCols()) mineButtons[y+1][x+1].setPressed();
        if(x+1 < this.getCols() && y-1 >= 0) mineButtons[y-1][x+1].setPressed();
        if(x-1 >= 0 && y+1 < this.getRows()) mineButtons[y+1][x-1].setPressed();
    }
    //覆盖周围 - 返回是否触雷
    public boolean coverAround(MineButton mb, GameBiz gb){
        boolean touchMines = false;
        if(flagCntAround(mb) != mb.getMineCntAround()){//如果周围旗数不等于数字，就覆盖回去
            coverAround(mb);
        }else{//自动扫开
            touchMines = autoSweep(mb, gb);
        }
        return touchMines;
    }
    //单纯的覆盖周围，不存在触雷判断
    public void coverAround(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        if(y-1 >= 0) mineButtons[y-1][x].setReleased();
        if(y+1 < this.getRows()) mineButtons[y+1][x].setReleased();
        if(x+1 < this.getCols()) mineButtons[y][x+1].setReleased();
        if(x-1 >= 0) mineButtons[y][x-1].setReleased();
        if(y-1 >= 0 && x-1 >= 0) mineButtons[y-1][x-1].setReleased();
        if(y+1 < this.getRows() && x+1 < this.getCols()) mineButtons[y+1][x+1].setReleased();
        if(x+1 < this.getCols() && y-1 >= 0) mineButtons[y-1][x+1].setReleased();
        if(x-1 >= 0 && y+1 < this.getRows()) mineButtons[y+1][x-1].setReleased();
    }
    //自动扫开 - 返回是否触雷
    public boolean autoSweep(MineButton mb, GameBiz gb){
        int x = mb.getIx();
        int y = mb.getIy();
        //先扫开
        if(y-1 >= 0) gb.autoSweep(mineButtons[y-1][x]);
        if(y+1 < this.getRows()) gb.autoSweep(mineButtons[y+1][x]);
        if(x+1 < this.getCols()) gb.autoSweep(mineButtons[y][x+1]);
        if(x-1 >= 0) gb.autoSweep(mineButtons[y][x-1]);
        if(y-1 >= 0 && x-1 >= 0) gb.autoSweep(mineButtons[y-1][x-1]);
        if(y+1 < this.getRows() && x+1 < this.getCols()) gb.autoSweep(mineButtons[y+1][x+1]);
        if(x+1 < this.getCols() && y-1 >= 0) gb.autoSweep(mineButtons[y-1][x+1]);
        if(x-1 >= 0 && y+1 < this.getRows()) gb.autoSweep(mineButtons[y+1][x-1]);
        if(judge(mb)) {//如果有雷
            //showAutoTouched(mb);在外面调用
            return true;
        }
        //在扫的时候判断输赢
        if(gb.judge()){//赢了
            int currTime = gb.timer.getCurrTime();
            gb.timer.end();
            gb.reseter.setIconWin();
            gb.scorer.setFlagCnt(gb.bm.flagAll()+gb.scorer.getFlagCnt());//赢了就自动全扫 - 别忘了加
            gb.scorer.update();
            gb.scorer.compToRecord(gb.gw, gb.mc.getLevel(), currTime);//比较记录
            gb.setWin(true);
        }
        return false;
    }
    //自动触雷 - 在外面调用
    public void showAutoTouched(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        if(y-1 >= 0) mineButtons[y-1][x].showButtonResult(true);//如果是雷会显示红色背景
        if(y+1 < this.getRows()) mineButtons[y+1][x].showButtonResult(true);
        if(x+1 < this.getCols()) mineButtons[y][x+1].showButtonResult(true);
        if(x-1 >= 0) mineButtons[y][x-1].showButtonResult(true);
        if(y-1 >= 0 && x-1 >= 0) mineButtons[y-1][x-1].showButtonResult(true);
        if(y+1 < this.getRows() && x+1 < this.getCols()) mineButtons[y+1][x+1].showButtonResult(true);
        if(x+1 < this.getCols() && y-1 >= 0) mineButtons[y-1][x+1].showButtonResult(true);
        if(x-1 >= 0 && y+1 < this.getRows()) mineButtons[y+1][x-1].showButtonResult(true);
    }
    //自动插旗 - 在自动扫雷调用
    public void autoFlag(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        if(y-1 >= 0) mineButtons[y-1][x].setHasFlag();
        if(y+1 < this.getRows()) mineButtons[y+1][x].setHasFlag();
        if(x+1 < this.getCols()) mineButtons[y][x+1].setHasFlag();
        if(x-1 >= 0) mineButtons[y][x-1].setHasFlag();
        if(y-1 >= 0 && x-1 >= 0) mineButtons[y-1][x-1].setHasFlag();
        if(y+1 < this.getRows() && x+1 < this.getCols()) mineButtons[y+1][x+1].setHasFlag();
        if(x+1 < this.getCols() && y-1 >= 0) mineButtons[y-1][x+1].setHasFlag();
        if(x-1 >= 0 && y+1 < this.getRows()) mineButtons[y+1][x-1].setHasFlag();
    }
    //返回周围旗数
    public int flagCntAround(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        int cnt = 0;
        if(y-1 >= 0 && mineButtons[y-1][x].isFlag()) cnt++;
        if(y+1 < this.getRows() && mineButtons[y+1][x].isFlag()) cnt++;
        if(x+1 < this.getCols() && mineButtons[y][x+1].isFlag()) cnt++;
        if(x-1 >= 0 && mineButtons[y][x-1].isFlag()) cnt++;
        if(y-1 >= 0 && x-1 >= 0 && mineButtons[y-1][x-1].isFlag()) cnt++;
        if(y+1 < this.getRows() && x+1 < this.getCols() && mineButtons[y+1][x+1].isFlag()) cnt++;
        if(x+1 < this.getCols() && y-1 >= 0 && mineButtons[y-1][x+1].isFlag()) cnt++;
        if(x-1 >= 0 && y+1 < this.getRows() && mineButtons[y+1][x-1].isFlag()) cnt++;
        return cnt;
    }
    //返回周围没被扫的格数
    public int notSweepedCntAround(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        int cnt = 0;
        if(y-1 >= 0 && !mineButtons[y-1][x].isSweeped()) cnt++;
        if(y+1 < this.getRows() && !mineButtons[y+1][x].isSweeped()) cnt++;
        if(x+1 < this.getCols() && !mineButtons[y][x+1].isSweeped()) cnt++;
        if(x-1 >= 0 && !mineButtons[y][x-1].isSweeped()) cnt++;
        if(y-1 >= 0 && x-1 >= 0 && !mineButtons[y-1][x-1].isSweeped()) cnt++;
        if(y+1 < this.getRows() && x+1 < this.getCols() && !mineButtons[y+1][x+1].isSweeped()) cnt++;
        if(x+1 < this.getCols() && y-1 >= 0 && !mineButtons[y-1][x+1].isSweeped()) cnt++;
        if(x-1 >= 0 && y+1 < this.getRows() && !mineButtons[y+1][x-1].isSweeped()) cnt++;
        return cnt;
    }
    //判断是否触雷
    public boolean judge(MineButton mb){
        int x = mb.getIx();
        int y = mb.getIy();
        if(y-1 >= 0 && mineButtons[y-1][x].notFlagButMine()) return true;
        if(y+1 < this.getRows() && mineButtons[y+1][x].notFlagButMine()) return true;
        if(x+1 < this.getCols() &&  mineButtons[y][x+1].notFlagButMine()) return true;
        if(x-1 >= 0  && mineButtons[y][x-1].notFlagButMine()) return true;
        if(y-1 >= 0 && x-1 >= 0  && mineButtons[y-1][x-1].notFlagButMine()) return true;
        if(y+1 < this.getRows() && x+1 < this.getCols() && mineButtons[y+1][x+1].notFlagButMine()) return true;
        if(x+1 < this.getCols() && y-1 >= 0 && mineButtons[y-1][x+1].notFlagButMine()) return true;
        if(x-1 >= 0 && y+1 < this.getRows() && mineButtons[y+1][x-1].notFlagButMine()) return true;
        return false;
    }
    public int getTotalCnt(){
        return rows * cols;
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
    public MineButton getMineButton(int i, int j) {
        return mineButtons[i][j];
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}
