package plane;

import javax.swing.JFrame;



/**
 * 
 * 本类是主类，其中有main方法
 *
 */

public class PanelFrame {

	static MainPanel e1;//主界面对�?
	static PlaySound p;//声音对象
	
	public static void main(String[] args) {
		
		e1 = new MainPanel();
		//监听关闭窗体按钮
		e1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置可视
		e1.setVisible(true);
		//设置不可拉伸
		e1.setResizable(false);
		if(p == null) {
			//声音设置
			p = new PlaySound();
//			try {
//				p.open(PanelFrame.class.getResource("/sounds/OPSound.mid").toURI().getPath());
				p.open("sounds/OPSound.mid");	
				p.play();
				p.loop();
				p.start();
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
	}
	
}
