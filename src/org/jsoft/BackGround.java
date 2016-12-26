package org.jsoft;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

public class BackGround {
	
	private Vector<EnemyTank> allEnemy = new Vector<EnemyTank>();
	private Vector<Obstraction> allObstruction = new Vector<Obstraction>();
	//private List<EnemyTank> removedEnemy = new ArrayList<EnemyTank>();
	//private List<Obstraction> removedObstruction = new ArrayList<Obstraction>();
	private Vector<Bullet> allBullet = new Vector<Bullet>();
	private Point tank = new Point();

	public Point getTank() {
		return tank;
	}

	public void setTank(Point tank) {
		this.tank = tank;
	}

	// 构造方法用来初始化
	public BackGround() {
	}

//	public void enemyStartMove() {
//		for (int i = 0; i < this.allEnemy.size(); i++) {
//			// this.allEnemy.get(i).startMove();
//		}
//	}

	public Vector<Obstraction> getAllObstruction() {
		return allObstruction;
	}

	public Vector<Bullet> getAllBullet() {
		return allBullet;
	}

	public void setAllBullet(Vector allBullet) {
		this.allBullet = allBullet;
	}

	public void setAllEnemy(Vector<EnemyTank> allEnemy) {
		this.allEnemy = allEnemy;
	}

	public void setAllObstruction(Vector<Obstraction> allObstruction) {
		this.allObstruction = allObstruction;
	}

	public Vector<EnemyTank> getAllEnemy() {
		return allEnemy;
	}

}
