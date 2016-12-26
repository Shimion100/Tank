package org.jsoft;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyFrame extends JFrame implements KeyListener, Runnable {
	private int width = 900;
	private int height = 600;
	// 判断上下左右键是否按下，用来处理转向的时候的卡顿现象
	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;
	private boolean upKeyPressed = false;
	private boolean downKeyPressed = false;
	// private List<BackGround> allBG = new ArrayList<BackGround>();
	private BackGround nowBG = null;
	private MyTank tank = null;
	private Thread t = new Thread(this);

	// private Font f = new Font("隶书", Font.ITALIC, 20);

	// 判断游戏是否已经开始
	// private boolean isStart = false;

	public MyFrame() {
		super("TankBattle");
		this.setSize(width, height);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screenWidth - this.width) / 2,
				(screenHeight - this.height) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		// 初始化静态量
		new StaticValue().init();
		// 创建所有的场景
		// for (int i = 1; i <= 3; i++) {
		// this.allBG.add(new BackGround(i, i == 3 ? true : false));
		// }
		// this.nowBG = this.allBG.get(0);
		this.nowBG = new BackGround();
		this.tank = new MyTank(400, 400, this.nowBG);
		EnemyTank enemy1 = new EnemyTank(20, 20, 2, 5, true, this.nowBG);
		EnemyTank enemy2 = new EnemyTank(60, 60, 2, 5, false, this.nowBG);
		EnemyTank enemy3 = new EnemyTank(300, 20, 2, 5, true, this.nowBG);
		EnemyTank enemy4 = new EnemyTank(330, 200, 2, 5, false, this.nowBG);
		this.nowBG.getAllEnemy().add(enemy1);
		this.nowBG.getAllEnemy().add(enemy2);
		this.nowBG.getAllEnemy().add(enemy3);
		this.nowBG.getAllEnemy().add(enemy4);
		// //调用方法设置Mario的BackGround;
		// this.mario.setBg(nowBG);
		t.start();
		this.addKeyListener(this);
		this.setVisible(true);
		this.repaint();
	}

	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		// System.out.println(key.getKeyCode());
		/**
		 * up-->38 down-->40 left-->37 right-->39 space-->32
		 */
		if (key.getKeyCode() == 37) {
			this.tank.moveLeft();
			this.leftKeyPressed = true;
		}
		if (key.getKeyCode() == 39) {
			this.tank.moveRight();
			this.rightKeyPressed = true;
		}
		if (key.getKeyCode() == 38) {
			this.tank.moveUp();
			this.upKeyPressed = true;
		}
		if (key.getKeyCode() == 40) {
			this.tank.moveDown();
			this.downKeyPressed = true;
		}
		if (key.getKeyCode() == 32) {
			this.tank.shoot();
		}
	}

	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub
		if (key.getKeyCode() == 37) {
			this.leftKeyPressed = false;
		}
		if (key.getKeyCode() == 39) {
			this.rightKeyPressed = false;
		}
		if (key.getKeyCode() == 40) {
			this.downKeyPressed = false;
		}
		if (key.getKeyCode() == 38) {
			this.upKeyPressed = false;
		}

		// 当不是发子弹键松开而且没有其他键按下时停止
		if (key.getKeyCode() != 32
				&& (!(this.leftKeyPressed || this.rightKeyPressed
						|| this.downKeyPressed || this.upKeyPressed))) {
			this.tank.stop();
		}
	}

	// }

	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		BufferedImage image = new BufferedImage(900, 600,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g2 = image.getGraphics();

		// // 绘制背景
		// g2.drawImage(this.nowBG.getBgImage(), 0, 0, this);
		//
		// //显示生命
		// g2.drawString("生命：" + this.mario.getLife(), 750,60);
		//
		// //绘制敌人
		Iterator iterEnemy = this.nowBG.getAllEnemy().iterator();
		while (iterEnemy.hasNext()) {
			EnemyTank e = (EnemyTank) iterEnemy.next();
			g2.drawImage(e.getShowImage(), e.getX(), e.getY(), this);
		}
		//
		// // 绘制障碍物
		// Iterator<Obstruction> iter =
		// this.nowBG.getAllObstruction().iterator();
		// while (iter.hasNext()) {
		// Obstruction ob = iter.next();
		// g2.drawImage(ob.getShowImage(), ob.getX(), ob.getY(), this);
		// }
		// 绘制子弹
		// Iterator<Bullet> iter = this.nowBG.getAllBullet().iterator();
		// while (iter.hasNext()) {
		// Bullet ob = iter.next();
		// if(!ob.isAlive()){
		// this.nowBG.getAllBullet().remove(ob);
		// continue;
		// }
		// g2.drawImage(ob.getShowImage(), ob.getX(), ob.getY(), this);
		// }

		for (int i = 0; i < this.nowBG.getAllBullet().size(); i++) {
			Bullet ob = this.nowBG.getAllBullet().get(i);
			if (!ob.isAlive()) {
				this.nowBG.getAllBullet().remove(i);
				continue;
			}
			g2.drawImage(ob.getShowImage(), ob.getX(), ob.getY(), this);
		}

		// 绘制MyTank
		// System.out.println("x:" + this.tank.getX() + "y:" + this.tank.getY()
		// );
		g2.drawImage(this.tank.getTempImage(), this.tank.getX(),
				this.tank.getY(), this);
		//
		//
		//
		// 二级缓存
		g.drawImage(image, 0, 0, this);
		// 测试代码
		// g.drawImage(StaticValue.allSelfImage.get(0), 400, 400, this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (this.tank.getLife() == 0) {
				this.tank.t.stop();
				JOptionPane.showMessageDialog(this, "Game Over！");
				System.exit(0);
			}
			this.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
