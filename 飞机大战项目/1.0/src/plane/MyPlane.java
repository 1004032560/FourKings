package plane;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * 
 * @author 方银�?
 * 本类描述玩家飞机
 *
 */

public class MyPlane {

	private int myPlane_x = 230, myPlane_y = 450;//玩家飞机坐标
	private Image planePic;//存储飞机图片的数�?
	private final int STEP = 7;//飞机速度
	private boolean isPress01, isPress02, isPress03, isPress04;//记录按键状�??
	boolean stayed = true;//玩家飞机生存标识
	private Break b;//爆炸图片对象
	int id;//爆炸图片ID
	static int planeID;//玩家飞机编号
	
	public MyPlane() {
		
//		planePic = new Image[5];
//		for(int i = 1; i <= planePic.length; i++) {
//			planePic[i-1] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/Plane0" + i + ".png"));
//		}
		planePic = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/Plane01.png"));
		
	}
	/**
	 * 绘制玩家飞机方法
	 * @param g
	 * @param j
	 */
	void drawMyPlane(Graphics g, Canvas c) {
		if(stayed)
			g.drawImage(planePic, myPlane_x, myPlane_y, GamePanel.PLANE_SIZE, GamePanel.PLANE_SIZE, c);//绘制玩家飞机
		else if(id == 0) {
			b = new Break(myPlane_x, myPlane_y);
			b.plane_break(g, c, id);
			id++;
		} else {
			b.plane_break(g, c, id);
			id++;
		}
			
	}
	/**
	 * 玩家飞机移动键盘监听方法
	 * @param c
	 */
	void adapter(Canvas c) {
		
		c.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_UP)
					isPress01 = true;

				if(key == KeyEvent.VK_DOWN)
					isPress02 = true;

				if(key == KeyEvent.VK_LEFT)
					isPress03 = true;

				if(key == KeyEvent.VK_RIGHT)
					isPress04 = true;
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_UP)
					isPress01 = false;

				if(key == KeyEvent.VK_DOWN)
					isPress02 = false;

				if(key == KeyEvent.VK_LEFT)
					isPress03 = false;

				if(key == KeyEvent.VK_RIGHT)
					isPress04 = false;
			}
			
		});
		
	}
	/**
	 * 控制飞机移动方法
	 */
	void planeMove() {
				
		if(isPress01)
			if(myPlane_y > 0)
				myPlane_y -= STEP;

		if(isPress02)
			if(myPlane_y < GamePanel.MAP_HEIGHT-GamePanel.PLANE_SIZE)
				myPlane_y += STEP;

		if(isPress03)
			if(myPlane_x > 0)
				myPlane_x -= STEP;

		if(isPress04)
			if(myPlane_x < GamePanel.MAP_HEIGHT-GamePanel.PLANE_SIZE)
				myPlane_x += STEP;

	}
	/**
	 * 获取玩家飞机坐标方法
	 * @return Point
	 */
	Point getX_Y() {
		
		return new Point(myPlane_x, myPlane_y);
		
	}
}
