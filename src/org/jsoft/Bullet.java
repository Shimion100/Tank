package org.jsoft;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Bullet extends Unit implements Runnable {
	//�ӵ����� ��true Ϊ��ҵ� ��falseΪ���˵�
	private boolean type = false;
	
	//�ƶ��ٶ�
	private final int MOVESPEED = 10;
	
	//�����߳�
	Thread t = null;
	
	//��ǰ��״̬
	public String status = "";
	
	// �ƶ�ʽ��ǰ��ͼƬ
	private int moving = 0;
	
	//�ӵ��Ƿ���ڵı�־λ
	private boolean isAlive = true;
	
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void stop(){
		this.status = "stop";
	}

	public void draw() {
		
	}
	
	/**
	 * ��̹�˳����ϻ�����Ͳ
	 * @param g
	 */
	public void drawImage(Graphics g) {
		
	}
	
	public void run() {
		while(true) {
			boolean canLeft = true;
			boolean canRight = true;
			boolean canUp = true;
			boolean canDown = true;
			if(this.x <= 0){
				canLeft = false;
				this.status = "stop";
			}
			if(this.x >= 900-20){
				canRight = false;
				this.status = "stop";
			}
			if(this.y <= 25){
				canUp = false;
				this.status = "stop";
			}
			if(this.y >= 600-20){
				canDown = false;
				this.status = "stop";
			}
		
			
			//̹�˵��ƶ�
			if(this.status.equals("left-moving") && canLeft){
				this.x -= this.MOVESPEED;
			}else if(this.status.equals("right-moving") && canRight){
				this.x += this.MOVESPEED;
			}else if(this.status.equals("up-moving") && canUp) {
				this.y -= this.MOVESPEED;
			}else if(this.status.equals("down-moving") && canDown){
				this.y += this.MOVESPEED;
			}
			//���´�������ʹ̹����ʾͼƬ������ʾ
			int temp = 0;
			if (this.status.indexOf("stop") != -1) {
				temp += this.moving;
				this.moving++;
				if (this.moving == 26) {
					this.isAlive = false;
					this.t.stop();
					return;
				}
			}else{
				temp = 0;
			}
			// �ı���ʾͼƬ
			this.showImage = StaticValue.allBulletImage.get(temp);
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public Bullet(int x, int y, String status , boolean type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.status = status;
		this.t = new Thread(this);
		this.t.start();
	}
}
