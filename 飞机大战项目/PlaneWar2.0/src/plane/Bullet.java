package plane;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * 本类描述子弹
 *
 */

public class Bullet {

	private int bullet_x, bullet_y;//子弹坐标
	private final int STEP = 9;//玩家飞机子弹速度
	private final int STEP1 =5;//敌机子弹速度
	private Image bullet01, bullet02;//弹图�?
	private final int TIME = 200;//玩家飞机子弹发射间隔时间
	private final int TIME1 = 5000;//敌机子弹发射间隔时间
	static long before_time;//过去绘制玩家飞机子弹时间
	static long[] before_time1 = new long[10];//过去绘制敌机子弹时间
	boolean stayed = true;//子弹存在标识
	static int bulletNum = 0;
	static int bulletType = 1;
	static int num;
	
	public Bullet(int x, int y) {
		
		//初始化坐�?
		bullet_x = x;
		bullet_y = y;
		
		bullet01 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/bullet_01.png"));//设置子弹图片
		bullet02 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/bullet_02.png"));//设置敌机子弹图片

		}
	/**
	 * 子弹绘制方法
	 * @param g
	 * @param c
	 * @param i
	 */
    int drawBullet(Graphics g, Canvas c, int i) {
    	if(i == 1) {
    		if(bulletNum<=100) {
    			g.drawImage(bullet01, bullet_x, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_HEIGHT, c);//绘制子弹
    			return 1;
    		}else if(bulletNum<=200){
    			g.drawImage(bullet01, bullet_x-8, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_HEIGHT, c);//绘制子弹
    			g.drawImage(bullet01, bullet_x+8, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_HEIGHT, c);//绘制子弹		
    			return 2;
			}else {
				g.drawImage(bullet01, bullet_x-15, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_HEIGHT, c);//绘制子弹
    			g.drawImage(bullet01, bullet_x, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_HEIGHT, c);//绘制子弹
    			g.drawImage(bullet01, bullet_x+15, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_HEIGHT, c);//绘制子弹
    			return 3;
			}
    	}else {
    		g.drawImage(bullet02, bullet_x, bullet_y, GamePanel.BULLET_WIDTH, GamePanel.BULLET_WIDTH, c);//绘制子弹
    		return 0;
    	}
	}
    /**
     * 玩家飞机子弹绘制方法，包括时间间隔的判断
     * @param array
     * @param g
     * @param c
     */
    int drawBullet_1(ArrayList<Bullet> array, Graphics g, Canvas c) {
    	long now_time = System.currentTimeMillis();
    	if(now_time-before_time >= TIME)//判断是否发射子弹
    	{
    		
    		if (bulletType == 1) {			
    			bulletType = drawBullet(g, c, 1);
    			array.add(this);//子弹对象加进数组
			}else if (bulletType == 2) {
				bulletType = drawBullet(g, c, 1);
				array.add(this);
			}else if(bulletType == 3){
				bulletType = drawBullet(g, c, 1);
				array.add(this);
			}
			before_time = now_time;//将现在时间作为过去时�?
			bulletNum++;
    	}
    	return bulletType;
	}
    
    /**
     * 敌机子弹绘制方法，包括时间间隔判�?
     * @param array
     * @param g
     * @param c
     * @param i
     */
    void drawBullet_2(ArrayList<Bullet> array, Graphics g, Canvas c, int i) {
    	long now_time = System.currentTimeMillis();
    	if(now_time-before_time1[i] >= TIME1)//判断是否发射子弹
    	{
    		drawBullet(g, c, 2);
    		array.add(this);//子弹对象加进数组
			before_time1[i] = now_time;//将现在时间作为过去时�?
			
    	}
	}
    
    /**
     * boss子弹绘制方法
     * @param g
     * @param c
     */
    void drawBullet_3(Graphics g, Canvas c) {
    	drawBullet(g, c, 2);    	
    }
	/**
	 * 控制玩家飞机子弹移动方法
	 * @param array
	 * @param i
	 */
	void bulletMove(ArrayList<Bullet> array, int i) {
		
		if(bullet_y < -GamePanel.BULLET_HEIGHT || stayed == false) {
			if (bulletType == 1) {
				array.remove(i);//从数组中移除子弹对象				
			}else if (bulletType == 2) {
				array.remove(i);
			}else if (bulletType == 3) {
				array.remove(i);
			}
		}
		else {				
				bullet_y -= STEP;			
		}
	}
	
	void bulletModle2(ArrayList<Bullet[]> arr, int i, int j) {
		if(bullet_y < -GamePanel.BULLET_HEIGHT || stayed == false)
			switch (j) {
			case 0:
				bullet_x -= 2;
				bullet_y += STEP;
				break;
			case 1:
				bullet_y += STEP;
				break;
			case 2:
				bullet_x += 2;
				bullet_y += STEP;
				break;
			default:
				break;
			}
		else
			arr.get(i)[j] = null;
	}
	
	/**
	 * 控制敌机子弹移动方法
	 * @param array
	 * @param i
	 */
	void bulletMove1(ArrayList<Bullet> array, int i) {
		
		if(bullet_y > GamePanel.MAP_HEIGHT || stayed == false)
			array.remove(i);//从数组中移除子弹对象
		else
			bullet_y += STEP1;
	}
	/**
	 * boss子弹移动方法
	 * @param i
	 */
	void bulletMove2(ArrayList<Bullet[]> arr, int i, int j) {
		if(stayed)
			switch (j) {
			case 0:
				bullet_x -= 2;
				bullet_y += 2;
				break;
			case 1:
				bullet_x -= 1;
				bullet_y += 2;
				break;
			case 2:
				bullet_y += 2;
				break;
			case 3:
				bullet_x += 1;
				bullet_y += 2;
				break;
			case 4:
				bullet_x += 2;
				bullet_y += 2;
				break;
			default:
				break;
			}
		else
			arr.get(i)[j] = null;
	}
	/**
	 * 获取子弹坐标方法
	 * @return Point
	 */
	Point getX_Y() {
		
		return new Point(bullet_x, bullet_y);
		
	}
}
