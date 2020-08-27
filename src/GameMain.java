/**
 * 主函数
 */

public class GameMain {
    public static void main(String[] args){
        //游戏等级 1 2 3 4(自定义)
        int level = 1;

        //游戏窗口
        GameWindow gw = new GameWindow("扫雷");
        //按钮地图
        ButtonsMap bm = new ButtonsMap(level);
        //雷生成器
        MinesCreater mc = new MinesCreater(level);
        //雷计数器
        MinesCounter counter = new MinesCounter(bm);
        //计分器
        Scorer scorer = new Scorer(mc.getMinesCnt());
        //计时器
        Timer timer = new Timer();
        //重置器
        Reseter reseter = new Reseter(bm, scorer, timer, mc, counter);
        //菜单
        GameMenu gm = new GameMenu();
        //游戏逻辑
        GameBiz gb = new GameBiz(gw, mc, counter, bm, scorer, timer, reseter, gm);


    }
}
