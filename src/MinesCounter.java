import java.io.Serializable;

/**
 * 雷计数器 - 计算周围雷数并关联到按钮
 */

public class MinesCounter implements Serializable {
    private static final long serialVersionUID = -6003667686542091934L;
    ButtonsMap bm;

    public MinesCounter(ButtonsMap bm){
        this.bm = bm;
    }

    //得到并关联雷数
    public void countMines(){
        for(int i = 0; i < bm.getRows(); i++){
            for(int j = 0; j < bm.getCols(); j++){
                //如果是雷就不用算
                if(bm.getMineButton(i, j).isMine())
                    continue;
                bm.getMineButton(i, j).setMineCntAround(getCountAround(i, j));
            }
        }
    }
    //得到一个按钮周围的雷数
    public int getCountAround(int i, int j){
        int cnt = 0;

        if (isMine(i - 1, j - 1))
            cnt++;
        if (isMine(i - 1, j))
            cnt++;
        if (isMine(i - 1, j + 1))
            cnt++;
        if (isMine(i, j - 1))
            cnt++;
        if (isMine(i, j + 1))
            cnt++;
        if (isMine(i + 1, j - 1))
            cnt++;
        if (isMine(i + 1, j))
            cnt++;
        if (isMine(i + 1, j + 1))
            cnt++;

        return cnt;
    }
    //返回是否为雷（包括判断边界）
    public boolean isMine(int i, int j){
        if(i < 0 || i >= bm.getRows() || j < 0 || j >= bm.getCols())
            return false;
        return bm.getMineButton(i, j).isMine();
    }
}
