package org.jsoft;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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
import javax.swing.JOptionPane;

public class MyTank extends Unit implements Runnable, ImageObserver {

	// 生命值：
	private int life = 3;

	// 是否存活
	private boolean isAlive = true;

	// 坦克底座的图片
	private BufferedImage tempImage = null;

	// 移动速度
	private final int MOVESPEED = 10;

	// 加入线程
	Thread t = null;

	// 当前的状态
	public String status = "";

	// 移动式当前的图片
	private int moving = 0;

	// 是否爆炸
	private int booming = 0;

	// 动态显示炮筒
	private int shooting = 0;

	// 所处在的背景
	private BackGround bg;

	// 是否在射击
	private boolean isShooting = false;

	public void moveLeft() {
		this.status = "left-moving";
	}

	public void moveRight() {
		this.status = "right-moving";
	}

	public void moveUp() {
		this.status = "up-moving";
	}

	public void moveDown() {
		this.status = "down-moving";
	}

	public void stop() {
		if (this.status.indexOf("left") != -1) {
			this.status = "left-standing";
		} else if (this.status.indexOf("right") != -1) {
			this.status = "right-standing";
		} else if (this.status.indexOf("down") != -1) {
			this.status = "down-standing";
		} else if (this.status.indexOf("up") != -1) {
			this.status = "up-standing";
		}
	}

	public void draw() {

	}

	public void shoot() {
		this.isShooting = true;
		int bulletX = this.x;
		int bulletY = this.y;
		String bulletStatus = "";
		if (this.status.indexOf("left") != -1) {
			bulletY = this.y + 15;
			bulletStatus = "left-moving";
		} else if (this.status.indexOf("right") != -1) {
			bulletStatus = "right-moving";
			bulletY = this.y + 15;
		} else if (this.status.indexOf("down") != -1) {
			bulletStatus = "down-moving";
			bulletX = this.x + 15;
		} else if (this.status.indexOf("up") != -1) {
			bulletStatus = "up-moving";
			bulletX = this.x + 15;
		}
		Bullet bul = new Bullet(bulletX, bulletY, bulletStatus, true);
		this.bg.getAllBullet().add(bul);
		try {
			// From file
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(
					System.getProperty("user.dir") + File.separator
							+ "Explosion.wav"));

			// From URL
			// stream = AudioSystem.getAudioInputStream(new
			// URL("http://hostname/audiofile"));

			// At present, ALAW and ULAW encodings must be converted
			// to PCM_SIGNED before it can be played
			AudioFormat format = stream.getFormat();
			if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(),
						format.getSampleSizeInBits() * 2, format.getChannels(),
						format.getFrameSize() * 2, format.getFrameRate(), true); // big
																					// endian
				stream = AudioSystem.getAudioInputStream(format, stream);
			}

			// Create the clip
			DataLine.Info info = new DataLine.Info(Clip.class,
					stream.getFormat(),
					((int) stream.getFrameLength() * format.getFrameSize()));
			Clip clip = (Clip) AudioSystem.getLine(info);

			// This method does not return until the audio file is completely
			// loaded
			clip.open(stream);

			// Start playing
			clip.start();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (LineUnavailableException e) {
		} catch (UnsupportedAudioFileException e) {
		}
	}

	/**
	 * 在坦克车身上画出炮筒
	 * 
	 * @param g
	 */
	public void drawImage(Graphics g) {
		g = this.tempImage.getGraphics();
		if (this.isShooting) {
			int temp = 0;
			temp += this.shooting;
			this.shooting++;
			if (this.shooting == 15) {
				this.isShooting = false;
				this.shooting = 0;
			}
			if (this.status.indexOf("left") != -1) {
				g.drawImage(StaticValue.SelfLeftShootImage.get(temp), 0, 6,
						this);
			} else if (this.status.indexOf("right") != -1) {
				g.drawImage(StaticValue.SelfRightShootImage.get(temp), 15, 6,
						this);
			} else if (this.status.indexOf("down") != -1) {
				g.drawImage(StaticValue.SelfDownShootImage.get(temp), 6, 15,
						this);
			} else if (this.status.indexOf("up") != -1) {
				g.drawImage(StaticValue.SelfUpShootImage.get(temp), 6, 0, this);
			}
		} else {
			if (this.status.indexOf("left") != -1) {
				g.drawImage(StaticValue.SelfLeftShootImage.get(0), 0, 6, this);
			} else if (this.status.indexOf("right") != -1) {
				g.drawImage(StaticValue.SelfRightShootImage.get(0), 15, 6, this);
			} else if (this.status.indexOf("down") != -1) {
				g.drawImage(StaticValue.SelfDownShootImage.get(0), 6, 15, this);
			} else if (this.status.indexOf("up") != -1) {
				g.drawImage(StaticValue.SelfUpShootImage.get(0), 6, 0, this);
			}
		}
	}

	public void run() {
		while (true) {
			this.bg.getTank().x = this.getX();
			this.bg.getTank().y = this.getY();
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

			Iterator<Bullet> bulletIt = this.bg.getAllBullet().iterator();
			while (bulletIt.hasNext()) {
				Bullet bul = bulletIt.next();
				if (bul.getX() > this.getX() && bul.getX() < this.getX() + 64
						&& bul.getY() > this.getY()
						&& bul.getY() < this.getY() + 64 && !bul.isType()) {
					//this.bg.getAllBullet().remove(bul);
					this.isAlive = false;
					// this.x = 450;
					// this.y = 500;
				}
			}

			Iterator<EnemyTank> it = this.bg.getAllEnemy().iterator();
			while (it.hasNext()) {
				EnemyTank enemy = it.next();
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
				if (this.getX() - enemy.getX() <= 5 + 65
						&& (this.getX() - enemy.getX() >= 0)
						&& (this.y - enemy.getY() <= 5 + 50 && enemy.getY()
								- this.getY() <= 5 + 60)) {
					canLeft = false;
				}
				if (enemy.getX() - this.getX() <= 5 + 65
						&& (enemy.getX() - this.getX() >= 0)
						&& (this.y - enemy.getY() <= 5 + 50 && enemy.getY()
								- this.getY() <= 5 + 60)) {
					canRight = false;
				}
			}
			// 坦克的移动
			if (this.status.equals("left-moving") && canLeft) {
				this.x -= this.MOVESPEED;
			} else if (this.status.equals("right-moving") && canRight) {
				this.x += this.MOVESPEED;
			} else if (this.status.equals("up-moving") && canUp) {
				this.y -= this.MOVESPEED;
			} else if (this.status.equals("down-moving") && canDown) {
				this.y += this.MOVESPEED;
			}
			// 以下代码用于使坦克显示图片轮流显示
			int temp = 0;
			if (this.status.indexOf("moving") != -1) {
				temp += this.moving;
				this.moving++;
				if (this.moving == 2) {
					this.moving = 1;
				}
			}
			// 改变显示图片
			if (this.status.indexOf("left") != -1) {
				this.tempImage = StaticValue.allSelfLeftImage.get(temp);
				this.drawImage(null);
			} else if (this.status.indexOf("right") != -1) {
				this.tempImage = StaticValue.allSelfRightImage.get(temp);
				this.drawImage(null);
			} else if (this.status.indexOf("down") != -1) {
				this.tempImage = StaticValue.allSelfDownImage.get(temp);
				this.drawImage(null);
			} else if (this.status.indexOf("up") != -1) {
				this.tempImage = StaticValue.allSelfUpImage.get(temp);
				this.drawImage(null);
			}

			if (this.isAlive == false) {
				this.booming++;
				if (this.booming == 5) {
					this.isAlive = true;
					this.x = 450;
					this.y = 500;
					this.booming = 0;
					this.life--;
				}

				this.tempImage = StaticValue.allBooming.get(this.booming);
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
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public MyTank(int x, int y, BackGround bg) {
		this.life = 3;
		this.x = x;
		this.y = y;
		this.status = "up-standing";
		this.bg = bg;
		this.t = new Thread(this);
		this.t.start();
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	public BufferedImage getTempImage() {
		return tempImage;
	}

	public void setTempImage(BufferedImage tempImage) {
		this.tempImage = tempImage;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getMoving() {
		return moving;
	}

	public void setMoving(int moving) {
		this.moving = moving;
	}

	public BackGround getBg() {
		return bg;
	}

	public void setBg(BackGround bg) {
		this.bg = bg;
	}

	public int getMOVESPEED() {
		return MOVESPEED;
	}

}
