package plane;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JDialog;

/**
 * 
 * 本类描述飞机爆炸
 *
 */

public class Break {
	
	private Image enemy_b[];//敌机爆炸图片数组
	private int x, y;//坐标
	JDialog f;
	public Break(int x, int y) {
		//实例化敌机爆炸图片数�?
		enemy_b = new Image[6];
		for(int i = 0; i < enemy_b.length; i++) {
			enemy_b[i] = Toolkit.getDefaultToolkit().getImage(getClass()
					.getResource("/images/bomb_enemy_" + i + ".png"));
		}
		//初始化坐�?
		this.x = x;
		this.y = y;
	}

	/**
	 * 敌机爆炸图片绘制方法
	 * @param g
	 * @param c
	 * @param i
	 */
	void enemy_break(Graphics g, Canvas c, int i) {
		g.drawImage(enemy_b[i/5], x, y, GamePanel.ENEMY_SIZE, GamePanel.ENEMY_SIZE, c);
	}
	/**
	 * 飞机爆炸图片绘制方法
	 * @param g
	 * @param c
	 * @param i
	 */
	void plane_break(Graphics g, Canvas c, int i) {
		if(i < 30)
			g.drawImage(enemy_b[i/5], x, y, GamePanel.PLANE_SIZE, GamePanel.PLANE_SIZE, c);
	}
	
	/**
	 * boss爆炸
	 * @param g
	 * @param c
	 * @param i
	 */
	void boss_break(Graphics g, Canvas c, int i) {
		if(i < 30)
			g.drawImage(enemy_b[i/5], x, y, GamePanel.BOSS_WIDTH, GamePanel.BOSS_HEIGHT, c);
	}
	
}
