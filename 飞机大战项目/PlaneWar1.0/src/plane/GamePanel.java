package plane;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * 本类描述游戏开始界面
 *
 */

public class GamePanel extends JPanel {
	
	private Image bg1, bg2;//图片
	private final int SCREEN_WIDTH = 800;//屏幕�?
	private final int SCREEN_HEIGHT = 600;//屏幕�?
	static final int MAP_WIDTH = 600;//地图面板�?
	static final int MAP_HEIGHT = 600;//地图面板�?
	static final int BULLET_WIDTH = 15;//子弹�?
	static final int BULLET_HEIGHT = 30;//子弹�?
	static final int PLANE_SIZE = 100;//玩家飞机边长
	static final int ENEMY_SIZE = 80;//敌机边长
	static final int BOSS_WIDTH = 250;//boss�?
	static final int BOSS_HEIGHT = 180;//boss�?
	private int bg1_y;//bg1图片的y坐标
	private int bg2_y;//bg2图片的y坐标
	private boolean isRunning = false;//线程是否循环的标�?
	private Canvas jp;//地图面板
	private JButton jb1, jb2, jb3;//按钮
	private JLabel jl;//标签
	private MainPanel m;//主面板对�?
	static long sum;//分数
	static int live;//玩家飞机生命
	static int live1;//boss飞机生命
	
	static int time;//计时
	static long before_time2 = System.currentTimeMillis();//过去绘制boss子弹时间
	
	//双缓�?
	private Image iBuffer;
	private Graphics gBuffer;

	public GamePanel(MainPanel m) {
		
		bg1_y = 0;//bg1图片的y坐标
		bg2_y = -SCREEN_HEIGHT;//bg2图片的y坐标
		
		
		setLayout(null);//清除布局管理�?
		setBackground(new Color(83, 163, 238));
		
		showPanel();//设置界面
		
		this.m = m;//主面板对象将地址传过�?
		
		live = 100;//生命初始�?
		live1 = 2000;//boss生命初始�?
		
	}
	/**
	 * 此方法作用是显示界面
	 */
	private void showPanel() {
		
		sum = 0;//设置分数显示�?0
		
		//设置地图面板
		jp = new MapPanel();
		jp.setBounds(200, 0, 600, 600);
		add(jp);
		
		//设置按钮
		jb1 = new JButton("开始(P)");
		jb1.setBounds(50, 60, 100, 50);
		add(jb1);
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stop_start();//暂停和开始监�?
				
			}
		});
		
		jb2 = new JButton("重新开始");
		jb2.setBounds(50, 140, 100, 50);
		add(jb2);
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//刷新游戏界面
				remove(jp);
				jp = new MapPanel();
				jp.setBounds(200, 0, 600, 600);
				add(jp);
				//让游戏暂�?
				isRunning = false;
				jb1.setText("开始(P)");//改变按钮文字
				Bullet.before_time = System.currentTimeMillis();
				
				jb1.setEnabled(true);//设置暂停\�?始按钮为可按
				sum = 0;//设置分数显示�?0
				
				live = 100;//设置生命�?100
				live1 = 2000;//设置boss生命�?2000
				
				time = 0;//赋予时间

			}
		});
		
		jb3 = new JButton("返回主菜单");
		jb3.setBounds(50, 220, 100, 50);
		add(jb3);
		jb3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//返回主菜单的设置
				Point p = PanelFrame.e1.getLocation();
				PanelFrame.e1.dispose();
				isRunning = false;
				PanelFrame.main(null);
				PanelFrame.e1.setLocation(p);
				
			}
		});
		
		//设置标签，用于存放分�?
		jl = new JLabel("0", JLabel.CENTER);
		jl.setBounds(40, 300, 120, 50);
		jl.setFont(new Font("acefont-family", Font.BOLD, 30));
		jl.setForeground(Color.red);
		add(jl);
	}
	
	/**
	 * 
	 * 本内部类描述地图面板
	 *
	 */
	
	private class MapPanel extends Canvas implements Runnable {
		
		private MyPlane mp ;//玩家飞机对象
		private BossPlane bp;//boss飞机对象
		private Bullet b;//子弹对象
		private Thread th;//线程对象
		private ArrayList<Bullet> array;//玩家飞机子弹数组
		private ArrayList<Bullet> array1;//敌机子弹数组
		private Enemy[] e_array;//敌机数组
		private Collide c;//碰撞判断的对�?
		private Image x;//�?条图片对�?
		private ArrayList<Bullet[]> arr;//boss子子弹数�?
		
		
		MapPanel() {
			
			requestFocusInWindow();//设置请求焦点
			bg1 = bg2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/mapback.png"));
			
			//创建玩家飞机对象
		    mp = new MyPlane();
			mp.adapter(this);
			
			//创建boss飞机对象
			bp = new BossPlane();
			
			array = new ArrayList<Bullet>();//初始化玩家飞机子弹数�?
			array1 = new ArrayList<Bullet>();//初始化敌机子弹数�?
			arr = new ArrayList<Bullet[]>();//初始化boss子弹数组
			
			e_array = new Enemy[8];//创建敌机数组
			
			c = new Collide();
			
			adapter();//暂停快捷键�?�P”监�?
			
			//初始化敌机数�?
			for(int i = 0; i < e_array.length; i++)
				e_array[i] = new Enemy((-i-1)*ENEMY_SIZE-BULLET_HEIGHT);
			
			th = new Thread(this);//创建线程
			th.start();//�?启线�?
						
		}
		/**
		 * 此方法是回调函数paint，用于绘制界�?
		 */
		public void paint(Graphics g) {
			
			jl.setText("" + sum);//显示分数
			
//			if(sum == 30000) {
//				jb1.setEnabled(false);//设置暂停\�?始按钮为不可�?
//				isRunning = false;//设置线程不循�?
//				new Dialog(m, 2);//挑战成功对话�?
//			}
			
			if(iBuffer == null)
			{
				iBuffer = createImage(MAP_WIDTH, MAP_HEIGHT);
				gBuffer = iBuffer.getGraphics();
			}
			gBuffer.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);//清除画笔
						
			//绘制背景地图进缓冲的画笔
			gBuffer.drawImage(bg1, 0, bg1_y, 600, 600, this);
			gBuffer.drawImage(bg2, 0, bg2_y, 600, 600, this);
			
			mp.drawMyPlane(gBuffer, this);//绘制玩家飞机
			
			//boss子弹循环绘制
			for(int i = 0; i < arr.size()&&arr.size()>0; i++)
				for(int j = 0; j < 5; j++)
					if(arr.get(i)[j]!=null)
						arr.get(i)[j].drawBullet(gBuffer, this, 2);
			
			if(time >= 2500) {//50秒后出现boss
				if(bp.getX_Y().getY() >= 80 && System.currentTimeMillis()-before_time2 >= 2500 && bp.stayed) {//boss发射子弹时间限制
					//绘制boss子弹
					Bullet[] bt = new Bullet[5];
					for(int i = 0; i < 5; i++) {
						bt[i] = new Bullet((int)(bp.getX_Y().getX())+BOSS_WIDTH/2-5, (int)(bp.getX_Y().getY())+BOSS_HEIGHT-30);
						bt[i].drawBullet_3(gBuffer, this);
					}
					arr.add(bt);
					before_time2 = System.currentTimeMillis();
				}
				if(!bp.stayed && bp.id == 30) {
					jb1.setEnabled(false);//设置暂停\�?始按钮为不可�?
					isRunning = false;//设置线程不循�?
					new Dialog(m, 2);//挑战成功对话�?
				}
				bp.drawBoss(gBuffer, this);//绘制boss飞机
			}
			
			
			if(mp.stayed) {	
				//创建玩家飞机子弹对象
				b = new Bullet((int)(mp.getX_Y().getX())+PLANE_SIZE/2-BULLET_WIDTH/2, (int)(mp.getX_Y().getY())-BULLET_HEIGHT);
				b.drawBullet_1(array, gBuffer, this);//子弹绘制
				for(int i = 0; i<array.size() && array.size()>1; i++) {
					array.get(i).drawBullet(gBuffer, this, 1);//子弹绘制
				}
			} else if(mp.id == 30){
				jb1.setEnabled(false);//设置暂停\�?始按钮为不可�?
				isRunning = false;//设置线程不循�?
				new Dialog(m, 1);//打开对话�?
			}
			
			//绘制敌机、敌机子�?
			for(int i = 0; i < e_array.length; i++)
			{
				e_array[i].drawEnemy(gBuffer, this, i%5);
				if(e_array[i].stayed && e_array[i].getX_Y().getY()>0) {
					int t = (int)(e_array[i].getX_Y().getY())+ENEMY_SIZE;
					if(t < MAP_HEIGHT) {
						//创建敌机子弹对象
						b = new Bullet((int)(e_array[i].getX_Y().getX())+ENEMY_SIZE/2-BULLET_WIDTH/2, t);
						b.drawBullet_2(array1, gBuffer, this, i);//子弹绘制
					}
					
				}
				for(int j = 0; j<array1.size(); j++) {
					array1.get(j).drawBullet(gBuffer, this, 2);//子弹绘制
				}
			}
			
			if(live1 >= 0 && bp.stayed) {
				//绘制boss�?�?
				x = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/xue_" + ((2000-live1)/100+1) + ".png"));
				gBuffer.drawImage(x, (int)(bp.getX_Y().getX()), (int)(bp.getX_Y().getY()-10), 250, 10, this);
			}
			
			if(live >= 0) {
				//绘制玩家飞机�?�?
				x = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/xue_" + ((100-live)/5+1) + ".png"));
				gBuffer.drawImage(x, 7, 7, 200, 20, this);
			}
												
			g.drawImage(iBuffer, 0, 0, null);//把缓冲图像载入屏�?
		}
		/**
		 * 此方法在调用paint前调�?
		 */
		public void update(Graphics g) {
			paint(g);
		}
		
		/**
		 * 此方法是线程run方法
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRunning) {
				
				draw();//刷新界面
			try {
				Thread.sleep(20);//延时0.02s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			time += 1;
		  }
		}
		/**
		 * 此方法用于线程中对所绘制对象的位置与存在与否的判�?
		 */
		private void draw() {
			// TODO Auto-generated method stub
			
			mapMove();
			
			mp.planeMove();//控制玩家飞机移动
			
			if(time >= 2500) {
				c.boss_plane(bp, mp);
				bp.bossMove();//boss飞机移动
			}
			
			//玩家飞机子弹移动	
			for(int i = 0; i < array.size(); i++)
				array.get(i).bulletMove(array, i);
			
			//敌机子弹移动
			for(int i = 0; i < array1.size(); i++)
			{
				c.bullet_plane(array1.get(i), mp);//玩家飞机和敌机子弹碰�?
				array1.get(i).bulletMove1(array1, i);
				
			}
			
			//敌机移动
			for(int i = 0; i < e_array.length; i++)
			{
				e_array[i].enemyMove();
				c.plane_enemy(mp, e_array[i]);//敌机与玩家飞机碰�?
			}
			
			//玩家飞机子弹和敌机�?�boss碰撞判断
			for(int i = 0; i < array.size(); i++)
				for(int j = 0; j < e_array.length; j++) {
					c.bullet_enemy(array.get(i), e_array[j]);
					c.bullet_boss(array.get(i), bp);
				}
			
			//boss子弹移动和子弹与玩家飞机碰撞判断
			for(int i = 0; i < arr.size(); i++)
				for(int j = 0; j < 5; j++) {
					if(arr.get(i)[j]!=null) {
						c.bullet_plane(arr.get(i)[j], mp);
						arr.get(i)[j].bulletMove2(arr, i, j);
					}
				}
				
			repaint();//重画
		}
		/**
		 * 地图位置的滚�?
		 */
		private void mapMove() {
			
			//背景地图移动
			bg1_y += 1;
			bg2_y += 1;
			if(bg1_y == SCREEN_HEIGHT)
				bg1_y = -SCREEN_HEIGHT;
			if(bg2_y == SCREEN_HEIGHT)
				bg2_y = -SCREEN_HEIGHT;
			
		}
		/**
		 * 此方法是对暂停键P的监�?
		 */
		private void adapter() {
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if(e.getKeyCode() == KeyEvent.VK_P)
						stop_start();
				}
			});
		}
				
	}
	/**
	 * 此方法用于执行暂停与�?�?
	 */
	private void stop_start() {
		if(isRunning == true)
		{
			isRunning = false;//设置线程不循�?
			jb1.setText("开始(P)");//改变按钮文字
			
		} else {
			isRunning = true;//设置线程循环
			Thread d = new Thread((Runnable) jp);//创建线程
			d.start();//�?启线�?
			jb1.setText("暂停(P)");//改变按钮文字
		}
	}
		
}
