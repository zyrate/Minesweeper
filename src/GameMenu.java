import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 菜单
 */

public class GameMenu extends JMenuBar implements Serializable {
    private static final long serialVersionUID = 3944252560798350733L;
    private JMenu game;
    private JMenuItem level1;
    private JMenuItem level2;
    private JMenuItem level3;
    private JMenuItem level4;//自定义
    private JMenuItem flagAll;
    private JMenuItem unFlagAll;
    private JMenuItem record;
    private JMenuItem save;//存档
    private JMenuItem read;//读档
    private JMenuItem exit;
    private JMenu help;
    private JMenuItem about;
    private JMenu auto;//自动扫雷
    private JMenuItem start;
    private JMenuItem stop;
    private JMenuItem speed;



    public GameMenu(){
        Font font = new Font("宋体", 1, 18);

        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());

        game = new JMenu("游戏");
        level1 = new JMenuItem("初级");
        level2 = new JMenuItem("中级");
        level3 = new JMenuItem("高级");
        level4 = new JMenuItem("-自定义-");
        flagAll = new JMenuItem("全部插旗");
        unFlagAll = new JMenuItem("全部去旗");
        record = new JMenuItem("记录榜");
        save = new JMenuItem("存档 *");
        read = new JMenuItem("读档 *");
        exit = new JMenuItem("退出");
        help = new JMenu("帮助");
        about = new JMenuItem("关于");
        auto = new JMenu("自动扫雷");
        start = new JMenuItem("开始");
        stop = new JMenuItem("停止");
        speed = new JMenuItem("设置速度");

        game.setFont(font);
        level1.setFont(font);
        level2.setFont(font);
        level3.setFont(font);
        level4.setFont(font);
        flagAll.setFont(font);
        unFlagAll.setFont(font);
        record.setFont(font);
        save.setFont(font);
        read.setFont(font);
        exit.setFont(font);
        help.setFont(font);
        about.setFont(font);
        auto.setFont(font);
        start.setFont(font);
        stop.setFont(font);
        speed.setFont(font);

        game.add(level1);
        game.add(level2);
        game.add(level3);
        game.add(level4);
        game.insertSeparator(4);
        game.add(flagAll);
        game.add(unFlagAll);
        game.insertSeparator(7);
        game.add(record);
        game.add(save);
        game.add(read);
        game.insertSeparator(11);
        game.add(exit);

        help.add(about);
        auto.add(start);
        auto.add(stop);
        auto.add(speed);

        this.add(game);
        this.add(auto);
        this.add(help);

    }

    public JMenuItem getFlagAll() {
        return flagAll;
    }

    public void setFlagAll(JMenuItem flagAll) {
        this.flagAll = flagAll;
    }

    public JMenuItem getUnFlagAll() {
        return unFlagAll;
    }

    public void setUnFlagAll(JMenuItem unFlagAll) {
        this.unFlagAll = unFlagAll;
    }

    public JMenu getGame() {
        return game;
    }

    public void setGame(JMenu game) {
        this.game = game;
    }

    public JMenuItem getLevel1() {
        return level1;
    }

    public void setLevel1(JMenuItem level1) {
        this.level1 = level1;
    }

    public JMenuItem getLevel2() {
        return level2;
    }

    public void setLevel2(JMenuItem level2) {
        this.level2 = level2;
    }

    public JMenuItem getLevel3() {
        return level3;
    }

    public void setLevel3(JMenuItem level3) {
        this.level3 = level3;
    }

    public JMenuItem getLevel4() {
        return level4;
    }

    public void setLevel4(JMenuItem level4) {
        this.level4 = level4;
    }

    public JMenuItem getExit() {
        return exit;
    }

    public void setExit(JMenuItem exit) {
        this.exit = exit;
    }

    public JMenu getHelp() {
        return help;
    }

    public void setHelp(JMenu help) {
        this.help = help;
    }

    public JMenuItem getAbout() {
        return about;
    }

    public void setAbout(JMenuItem about) {
        this.about = about;
    }

    public JMenuItem getRecord() {
        return record;
    }

    public void setRecord(JMenuItem record) {
        this.record = record;
    }

    public JMenuItem getSave() {
        return save;
    }

    public void setSave(JMenuItem save) {
        this.save = save;
    }

    public JMenuItem getRead() {
        return read;
    }

    public void setRead(JMenuItem read) {
        this.read = read;
    }

    public JMenuItem getStart() {
        return start;
    }

    public void setStart(JMenuItem start) {
        this.start = start;
    }

    public JMenuItem getStop() {
        return stop;
    }

    public void setStop(JMenuItem stop) {
        this.stop = stop;
    }

    public JMenu getAuto() {
        return auto;
    }

    public void setAuto(JMenu auto) {
        this.auto = auto;
    }

    public JMenuItem getSpeed() {
        return speed;
    }

    public void setSpeed(JMenuItem speed) {
        this.speed = speed;
    }
}
