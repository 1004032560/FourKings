package plane;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * 
 * 
 * 本来描述敌机
 *
 */

public class Enemy {
	
	private int enemy_x, enemy_y;//敌机坐标
	private int enemy_y0;//敌机初始y坐标
	private Image ememyPic[];//敌机图片数组
	private final int STEP = 2;//敌机移动速度
	boolean stayed = true;//敌机生存标识
	private Break b;//爆炸图片对象
	private int id;//爆炸图片ID

	public Enemy(int y) {
		//敌机坐标初始�?
		enemy_x = (int) (Math.random()*500);
		enemy_y0 = enemy_y = y;
				
		ememyPic = new Image[5];
		for(int i = 1; i <= ememyPic.length; i++) {
			ememyPic[i-1] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/bullet0" + i + ".png"));
		}
	}
	/**
	 * 绘制敌机方法
	 * @param g
	 * @param c
	 */
	void drawEnemy(Graphics g, Canvas c, int i) {
		if(stayed)
			g.drawImage(ememyPic[i], enemy_x, enemy_y, GamePanel.ENEMY_SIZE, GamePanel.ENEMY_SIZE, c);//绘制敌机
		else if(id == 0) {
				b = new Break(enemy_x, enemy_y);
				b.enemy_break(g, c, id);
				id++;
			}

		if(b != null && id != 0)
			if(id == 29){
				b.enemy_break(g, c, id);
				id = 0;
			} else {
				b.enemy_break(g, c, id);
				id++;
			}
	}
	/**
	 * 敌机移动控制方法
	 */
	void enemyMove() {
		if(enemy_y > GamePanel.MAP_HEIGHT || stayed == false)
		{
			
			if(GamePanel.time >= 2500) {//50秒过后敌机不在出�?
				enemy_x = 0;
				enemy_y = GamePanel.MAP_HEIGHT+GamePanel.PLANE_SIZE;
			} else {
				enemy_x = (int) (Math.random()*500);
				enemy_y = enemy_y0;
				stayed = true;//敌机设置为生存状�?
			}
		} else
			enemy_y += STEP;
	}
	/**
	 * 获取敌机坐标方法
	 * @return Point
	 */
	Point getX_Y() {
		
		return new Point(enemy_x, enemy_y);
		
	}
	
}
