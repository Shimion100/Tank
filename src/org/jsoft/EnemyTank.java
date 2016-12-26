package org.jsoft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 敌人坦克类
 * 
 * @author ACE 敌人可以自由移动，根据AI强度判断其职能情况
 * 
 */

public class EnemyTank extends Unit implements Runnable {

	// 加入背景
	private BackGround bg = null;

	// AI强度
	private boolean AI = false;

	// 移动速度
	private int moveSpeed = 10;

	// 方向
	private int direction = 0;

	// 加入线程
	Thread t = null;

	// 显示动态图片
	private int moving = 0;

	// 移动
	public void moveLeft() {
		this.x -= this.moveSpeed;
	}

	public void moveRight() {
		this.x += this.moveSpeed;
	}

	public void moveUp() {
		this.y -= this.moveSpeed;
	}

	public void moveDown() {
		this.y += this.moveSpeed;
	}

	// 构造方法
	public EnemyTank(int x, int y, int direction, int moveSpeed, boolean AI,
			BackGround bg) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.moveSpeed = moveSpeed;
		this.AI = AI;
		this.bg = bg;
		this.t = new Thread(this);
		t.start();
	}

	public void shoot() {
		int bulletX = this.x;
		int bulletY = this.y;
		String bulletStatus = "";
		if (this.direction == 3) {
			bulletY = this.y + 23;
			bulletStatus = "left-moving";
		} else if (this.direction == 4) {
			bulletStatus = "right-moving";
			bulletX = this.x + 35;
			bulletY = this.y + 23;
		} else if (this.direction == 2) {
			bulletStatus = "down-moving";
			bulletX = this.x + 23;
			bulletY = this.y + 35;
		} else if (this.direction == 1) {
			bulletStatus = "up-moving";
			bulletX = this.x + 23;
		}
		Bullet bul = new Bullet(bulletX, bulletY, bulletStatus, false);
		this.bg.getAllBullet().add(bul);
	}

	// 复写run方法
	public void run() {
		while (true) {
			// 判断是否可以移动
			boolean canLeft = true;
			boolean canRight = true;
			boolean canUp = true;
			boolean canDown = true;
			if (this.x <= 0) {
				canLeft = false;
			}
			if (this.x >= 900 - 62) {
				canRight = false;
			}
			if (this.y <= 25) {
				canUp = false;
			}
			if (this.y >= 600 - 62) {
				canDown = false;
			}

			if (Math.random() > 0.9) {
				this.shoot();
			}

			Iterator<Bullet> bulletIt = this.bg.getAllBullet().iterator();
			while (bulletIt.hasNext()) {
				Bullet bul = bulletIt.next();
				if (bul.getX() > this.getX() && bul.getX() < this.getX() + 64
						&& bul.getY() > this.getY()
						&& bul.getY() < this.getY() + 64 && bul.isType()) {
					this.bg.getAllEnemy().remove(this);
					this.bg.getAllBullet().remove(bul);
					try {
						// From file
						AudioInputStream stream = AudioSystem
								.getAudioInputStream(new File(System
										.getProperty("user.dir")
										+ File.separator + "boom.wav"));

						// From URL
						// stream = AudioSystem.getAudioInputStream(new
						// URL("http://hostname/audiofile"));

						// At present, ALAW and ULAW encodings must be converted
						// to PCM_SIGNED before it can be played
						AudioFormat format = stream.getFormat();
						if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
							format = new AudioFormat(
									AudioFormat.Encoding.PCM_SIGNED,
									format.getSampleRate(),
									format.getSampleSizeInBits() * 2,
									format.getChannels(),
									format.getFrameSize() * 2,
									format.getFrameRate(), true); // big
																	// endian
							stream = AudioSystem.getAudioInputStream(format,
									stream);
						}

						// Create the clip
						DataLine.Info info = new DataLine.Info(Clip.class,
								stream.getFormat(),
								((int) stream.getFrameLength() * format
										.getFrameSize()));
						Clip clip = (Clip) AudioSystem.getLine(info);

						// This method does not return until the audio file is
						// completely
						// loaded
						clip.open(stream);

						// Start playing
						clip.start();
					} catch (MalformedURLException e) {
					} catch (IOException e) {
					} catch (LineUnavailableException e) {
					} catch (UnsupportedAudioFileException e) {
					}
					this.t.stop();
				}
			}

			Iterator<EnemyTank> it = this.bg.getAllEnemy().iterator();
			while (it.hasNext()) {
				EnemyTank enemy = it.next();
				if (enemy.equals(this)) {
					continue;
				}
				if (this.y - enemy.getY() <= 5 + 62
						&& (this.y - enemy.getY() >= 0)
						&& (this.x - enemy.getX() <= 64 && enemy.getX()
								- this.x < 5 + 60)) {
					canUp = false;
				}
				if (enemy.getY() - this.y <= 5 + 62
						&& (enemy.getY() - this.y >= 0)
						&& (this.x - enemy.getX() <= 64 && enemy.getX()
								- this.x < 5 + 60)) {
					canDown = false;
				}
				if (this.getX() - enemy.getX() <= 5 + 64
						&& (this.getX() - enemy.getX() >= 0)
						&& (this.y - enemy.getY() <= 5 + 50 && enemy.getY()
								- this.getY() <= 5 + 60)) {
					canLeft = false;
				}
				if (enemy.getX() - this.getX() <= 5 + 64
						&& (enemy.getX() - this.getX() >= 0)
						&& (this.y - enemy.getY() <= 5 + 50 && enemy.getY()
								- this.getY() <= 5 + 60)) {
					canRight = false;
				}
			}

			if (this.y - this.bg.getTank().getY() <= 5 + 62
					&& (this.y - this.bg.getTank().getY() >= 0)
					&& (this.x - this.bg.getTank().getX() <= 64 && this.bg.getTank().getX()
							- this.x < 5 + 60)) {
				canUp = false;
			}
			if (this.bg.getTank().getY() - this.y <= 5 + 62
					&& (this.bg.getTank().getY() - this.y >= 0)
					&& (this.x - this.bg.getTank().getX() <= 64 && this.bg.getTank().getX()
							- this.x < 5 + 60)) {
				canDown = false;
			}
			if (this.getX() - this.bg.getTank().getX() <= 5 + 64
					&& (this.getX() - this.bg.getTank().getX() >= 0)
					&& (this.y - this.bg.getTank().getY() <= 5 + 50 && this.bg.getTank().getY()
							- this.getY() <= 5 + 60)) {
				canLeft = false;
			}
			if (this.bg.getTank().getX() - this.getX() <= 5 + 64
					&& (this.bg.getTank().getX() - this.getX() >= 0)
					&& (this.y - this.bg.getTank().getY() <= 5 + 50 && this.bg.getTank().getY()
							- this.getY() <= 5 + 60)) {
				canRight = false;
			}

			// 根据AI生成移动方向
			if (Math.random() > 0.9) {
				if (this.AI == false) {
					this.direction = (int) (Math.random() * 4 + 1);
				} else if (this.AI == true) {
					// 判断往纵向走还是横向走
					if (Math.random() > 0.5) {
						if (this.x < this.bg.getTank().x) {
							this.direction = 4;
						} else {
							this.direction = 3;
						}
					} else {
						if (this.y < this.bg.getTank().y) {
							this.direction = 2;
						} else {
							this.direction = 1;
						}

					}
				}
			}

			// 动态显示图片
			int temp = 0;
			temp += this.moving;
			this.moving++;
			if (this.moving == 2) {
				this.moving = 0;
			}

			switch (this.direction) {
			case 1: {
				this.showImage = StaticValue.allEnmeyUpImage.get(temp);
				if (canUp) {
					this.moveUp();
				}
				break;
			}
			case 2: {
				this.showImage = StaticValue.allEnemyDownImage.get(temp);
				if (canDown) {
					this.moveDown();
				}
				break;
			}
			case 3: {
				this.showImage = StaticValue.allEnemyLeftImage.get(temp);
				if (canLeft) {
					this.moveLeft();
				}
				break;
			}
			case 4: {
				this.showImage = StaticValue.allEnemyRightImage.get(temp);
				if (canRight) {
					this.moveRight();
				}
				break;
			}
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void draw() {
	}
}
