package plane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * 本类是主界面�?
 *
 */

public class MainPanel extends JFrame {

	private JLabel back, label01,label02, label03;
	private PlaySound p;
	private MainPanel m;//本类对象
	
	public MainPanel() {
		
		super("雷电");
		
		setSize(800, 600);//设置窗体大小
		
		setLayout(null);//清除布局管理�?
				
		showBackground();//设置背景
		
		showLabel();//显示界面
		
		adapter();//监听键盘
		m = this;
	}
	/**
	 * 键盘监听方法
	 */
	public void adapter() {
		
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(PlaySound.b[1]) {
					//声音设置
					p = new PlaySound();
						p.open("sounds/ClickSound.wav");
						p.play();
						p.start();

					
				}
				
				int key = e.getKeyCode();
				
				//监听回车按键
				if(key == KeyEvent.VK_ENTER && label02.isVisible()) {
					
					GamePanel game = new GamePanel(m);
					add(game);
					game.setSize(800, 600);//设置游戏面板界面大小
					//移除组件
					remove(label01);
					remove(label02);
					remove(label03);
					Bullet.before_time = System.currentTimeMillis();
				}
				//监听回车按键
				if(key == KeyEvent.VK_ENTER && label03.isVisible()) {
					//跳转到设置对话框
					new Dialog(m, 0);
				}

			}
			
		});
		
	}
	/**
	 * 背景制作方法
	 */
	private void showBackground() {
		
		 //背景图片
	 	 ImageIcon background = new ImageIcon(getClass().getResource("/images/mainback.png"));
	  	 //设置背景标签
         back = new JLabel(background);
         //设置背景图片位置大小
         back.setBounds(0, 0, getWidth(), getHeight());
         //面板透明
         JPanel j = (JPanel)getContentPane();
         j.setOpaque(false);
         //设置背景
         getLayeredPane().add(back, new Integer(Integer.MIN_VALUE));
	}
	/**
	 * 标签设置方法
	 */
	private void showLabel() {
		
		 //指示的飞机图�?
	   	 ImageIcon icon = new ImageIcon(getClass().getResource("/images/point.png"));
		
		 //设置标签
		 label01 = new JLabel("开始游戏");
		 label01.setFont(new Font("acefont-family", Font.BOLD, 50));
		 label01.setForeground(Color.blue);//设置前景�?
		 label01.setBounds(220, 340, 400, 120);
		 label02 = new JLabel(icon);
		 label02.setBounds(40, 340, 250, 120);
		
		 label03 = new JLabel(icon);
		 label03.setBounds(130, 435, 250, 120);
		 label03.setVisible(false);
		
		 add(label01);
		 add(label02);
		 add(label03);
	}
	
}
