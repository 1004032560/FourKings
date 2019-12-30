package plane;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 *
 * 本类描述boss飞机
 *
 */
public class BossPlane {

	private int bossPlane_x, bossPlane_y;//boss飞机坐标
	private Image ima;//boss飞机图片
	private final int step = 1;//boss移动速度
	boolean stayed;//飞机存在标识
	private int position = -1, point;//飞机位置标记
	private Break b;//爆炸图片对象
	int id;//爆炸图片ID
	
	BossPlane() {
		//坐标初始�?
		bossPlane_x = 175;
		bossPlane_y = -500;
		//图片初始�?
		ima = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/boss.png"));
		
		stayed = true;
	}
	
	/**
	 * 绘制boss
	 * @param g
	 * @param c
	 */
	void drawBoss(Graphics g, Canvas c) {
		if(stayed)
			g.drawImage(ima, bossPlane_x, bossPlane_y, GamePanel.BOSS_WIDTH, GamePanel.BOSS_HEIGHT, c);
		else if(id == 0) {
			b = new Break(bossPlane_x, bossPlane_y);
			b.boss_break(g, c, id);
			id++;
		} else {
			b.boss_break(g, c, id);
			id++;
		}
	}
	
	/**
	 * 控制boss移动
	 */
	void bossMove() {
		if(bossPlane_y < 80)
			bossPlane_y += step;
		else
			switch (position) {//让飞机曲线运�?
			case 0://向右下移�?
				bossPlane_y += step;
				bossPlane_x += step;
				if(bossPlane_x == GamePanel.MAP_WIDTH-GamePanel.BOSS_WIDTH)
					position++;
				break;
			case 1://向左下移�?
				if((point/700)%2 == 0) {
					bossPlane_y += 3;
					bossPlane_x -= step;
				} else {
					bossPlane_y += 2;
					bossPlane_x -= 3;
				}
				if(bossPlane_y >= GamePanel.MAP_HEIGHT-GamePanel.BOSS_HEIGHT)
					position++;
				break;
			case 2://向左上移�?
				if((point/700)%2 == 0) {
					bossPlane_y -= step;
					bossPlane_x -= step;
				} else {
					bossPlane_y -= 3;
					bossPlane_x -= step;
				}
				if(bossPlane_x <= 0)
					position++;
				break;
			case 3://向右上移�?
				bossPlane_x += step;
				if(bossPlane_x == 175)
					position = -1;
				break;
			default://向右移动
				point++;
				if(point%300 == 0)
					position = 0;
				break;
			}
	}
	
	/**
	 * 获取玩家飞机坐标方法
	 * @return Point
	 */
	Point getX_Y() {
		
		return new Point(bossPlane_x, bossPlane_y);
		
	}
	
}
