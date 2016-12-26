package org.jsoft;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StaticValue {
	// 静态的图片
	public static List<BufferedImage> allSelfDownImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allSelfLeftImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allSelfRightImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allSelfUpImage = new ArrayList<BufferedImage>();

	public static List<BufferedImage> allEnemyDownImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allEnemyLeftImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allEnemyRightImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allEnmeyUpImage = new ArrayList<BufferedImage>();

	public static List<BufferedImage> SelfDownShootImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> SelfLeftShootImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> SelfRightShootImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> SelfUpShootImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allBulletImage = new ArrayList<BufferedImage>();
	public static List<BufferedImage> allObstructionImage = new ArrayList<BufferedImage>();

	public static List<BufferedImage> allBooming = new ArrayList<BufferedImage>();
	public static String imagePath = System.getProperty("user.dir")
			+ File.separator;

	public static void init() {
		// 倒入所有的自己坦克的图片
		try {
			allSelfDownImage.add(ImageIO.read(new File(imagePath
					+ "tankStillDown.png")));
			allSelfLeftImage.add(ImageIO.read(new File(imagePath
					+ "tankStillLeft.png")));
			allSelfRightImage.add(ImageIO.read(new File(imagePath
					+ "tankStillRight.png")));
			allSelfUpImage.add(ImageIO.read(new File(imagePath
					+ "tankStillUp.png")));

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		for (int i = 1; i <= 3; i++) {
			try {
				allSelfDownImage.add(ImageIO.read(new File(imagePath
						+ "tankMoveDown" + i + ".png")));
				allSelfLeftImage.add(ImageIO.read(new File(imagePath
						+ "tankMoveLeft" + i + ".png")));
				allSelfRightImage.add(ImageIO.read(new File(imagePath
						+ "tankMoveRight" + i + ".png")));
				allSelfUpImage.add(ImageIO.read(new File(imagePath
						+ "tankMoveUp" + i + ".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 倒入所有自己坦克射击的图片
		for (int i = 1; i <= 16; i++) {
			try {
				SelfDownShootImage.add(ImageIO.read(new File(imagePath
						+ "tankShootDown" + i + ".png")));
				SelfLeftShootImage.add(ImageIO.read(new File(imagePath
						+ "tankShootLeft" + i + ".png")));
				SelfRightShootImage.add(ImageIO.read(new File(imagePath
						+ "tankShootRight" + i + ".png")));
				SelfUpShootImage.add(ImageIO.read(new File(imagePath
						+ "tankShootUp" + i + ".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 导入敌人图片
		for (int i = 1; i <= 3; i++) {
			try {
				allEnmeyUpImage.add(ImageIO.read(new File(imagePath
						+ "enemyUpMove" + i + ".png")));
				allEnemyDownImage.add(ImageIO.read(new File(imagePath
						+ "enemyDownMove" + i + ".png")));
				allEnemyLeftImage.add(ImageIO.read(new File(imagePath
						+ "enemyLeftMove" + i + ".png")));
				allEnemyRightImage.add(ImageIO.read(new File(imagePath
						+ "enemyRightMove" + i + ".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 倒入障碍物
		for (int i = 1; i <= 6; i++) {
			try {
				allObstructionImage.add(ImageIO.read(new File(imagePath
						+ "brick" + i + ".png")));
				allBooming.add(ImageIO.read(new File(imagePath + "bomb" + i
						+ ".gif")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 导入子弹
		for (int i = 8; i <= 34; i++) {
			try {
				allBulletImage.add(ImageIO.read(new File(imagePath + "tankbomb"
						+ i + ".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// From file
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(
					imagePath + "start.wav"));

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
}
