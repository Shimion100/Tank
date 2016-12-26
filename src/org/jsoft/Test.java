package org.jsoft;

import java.awt.Color;
import java.awt.EventQueue;

public class Test {
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				MyFrame frame = new MyFrame();
			}
		});
	}
}
 