package yilan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class yilan {

	int genislik = 350, yukseklik = 350, hiz = 60, yon = 0,boyut = 10,uzunluk = 5,puan = 0, bitti = 0;
	boolean bekle = false,devam = true,intro = true;
	Point yem;
	Point[] yilan;
	Timer t;
	JFrame pencere;
	Platform pl;
	
	public static void main(String[] args) {
		new yilan();
	}
	
	public yilan() {
		yilan = new Point[uzunluk];
		for(int i = 0; i < uzunluk; i++)
			yilan[i] = new Point(boyut * (uzunluk-i), boyut);
		yem_at();
		pencere = new JFrame("YILAN ");
		pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pl = new Platform();
		pencere.add(pl);
		pencere.setSize(genislik , yukseklik);
		pencere.setResizable(false);
		pencere.setVisible(true);	
		
		pencere.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 80) bekle();
				if(intro && e.getKeyCode() == 10) baslat();
				if(bitti != 0 && e.getKeyCode() == 10) yeniden_baslat();
				if(!devam) return;
				switch (e.getKeyCode()) {
					case 38:
						if(yon != 2)
							yon = 3;
					break;
					case 37:
						if(yon != 0)
							yon = 1;
					break;
					case 40:
						if(yon != 3)
							yon = 2;
					break;
					case 39:
						if(yon != 1)
							yon = 0;
					break;
				}
				devam = false;
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
			
			}
			@Override
			public void keyReleased(KeyEvent e) {		
			}
		});
	}
	
	private void bekle() {
		if(bitti != 0 || intro) return;
		if(bekle){
			bekle = false;
			t.start();
		} else {
			bekle = true;
			t.stop();
			pl.repaint();
		}
	}
	
	private void yeniden_baslat() {
		puan = 0;
		yon = 0;
		bitti = 0;
		uzunluk = 5;
		yilan = new Point[uzunluk];
		for(int i = 0; i < uzunluk; i++)
			yilan[i] = new Point(boyut * (uzunluk-i), boyut);
		t.start();
		yem_at();
		
	}
	
	public void yem_at() {
		while(true) {
			Random salla = new Random();
			yem = new Point(salla.nextInt((genislik / boyut) - 2) * boyut, salla.nextInt((yukseklik / boyut) - 2) * boyut);
			if(yem.x < boyut || yem.y < boyut || yem.x > genislik - (boyut * 3) - 1 || yem.y > yukseklik - (boyut * 6) - 1) continue;
			boolean carpisma = false;
			for(int i = 0; i < yilan.length; i++) {
				if(yilan[i].x == yem.x && yilan[i].y == yem.y) {
					carpisma = true;
				}
			}
			if(!carpisma) break;
		}
	}
	
	public void baslat() {
		intro = false;
		t.start();
	}
	
	public class Platform extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public Platform() {
			this.setPreferredSize(new Dimension(400, 400));
			Dimension ekran = getToolkit().getScreenSize();
			pencere.setLocation((ekran.width - genislik) / 2, 
					(ekran.height - yukseklik) / 2);

			ActionListener gorev = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					int _x = 0;
					int _y = 0;
					switch (yon) {
						case 0:
							_x = boyut;
						break;
						case 1:
							_x = boyut * -1;
						break;
						case 2:
							_y = boyut;
						break;
						case 3:
							_y = boyut * -1;
						break;
					}
					
					for(int i = yilan.length - 1; i >= 0; i--) {
						if(i == 0) { 
							yilan[i] = new Point(yilan[i].x + _x, yilan[i].y + _y);
						} else {
							yilan[i] = new Point(yilan[i-1].x, yilan[i-1].y);
						}
					}
					if(carpisma_kontrol()){
						t.stop();
					}
					
					yem_kontrol();
					
					devam = true;
					repaint();
				}

				private void yem_kontrol() {
					if(yilan[0].x == yem.x && yilan[0].y == yem.y) {
						puan = puan + 8;
						yem_at();
						yilani_uzat();
					}
				}

				private void yilani_uzat() {
					Point[] klon = new Point[uzunluk];
					klon = yilan;
					uzunluk++;
					yilan = new Point[uzunluk];
					
					for(int yubo=0;yubo<=(klon.length-1);yubo++)
					{
						yilan[yubo]=klon[yubo];
					}
					
					yilan[yilan.length-1] = new Point(yilan[yilan.length-2].x, yilan[yilan.length-2].y);			
				}

				private boolean carpisma_kontrol() {
					if(yilan[0].x < boyut || yilan[0].y < boyut || yilan[0].x > genislik - (boyut * 3) || yilan[0].y > yukseklik - (boyut * 6)) {
						bitti = 1;
						return true;
					}
					for(int i = 0; i < yilan.length; i++) {
						if(yilan[i].x == yilan[0].x && yilan[i].y == yilan[0].y && i != 0) {
							bitti = 2;
							return true;
						}
					}
					return false;				
				}
			};
			
			t = new Timer(hiz, gorev);
		}
		
		public void paint(Graphics g) {
			Graphics2D gr = (Graphics2D) g;
			gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if(intro) {
				gr.setColor(new Color(120, 120, 100));
				gr.fillRect(0, 0, genislik, yukseklik);
				gr.setColor(Color.BLACK);
				gr.setFont(new Font("tahoma", 1, 80));
				gr.drawString("Yýlan", 80, 160);
				gr.setFont(new Font("tahoma", 1, 13));
				gr.drawString("Yýlan oyununa hoþgeldin baþlamak için ENTER'a bas!", 10, 190);
				
				return;
			}
			if(bitti != 0) {
				gr.setColor(new Color(120, 120, 100, 190));
				gr.fillRect(0, 0, genislik, yukseklik);
				gr.setColor(Color.BLACK);
				gr.setFont(new Font("tahoma", 1, 25));
				if(bitti == 2) {
					gr.drawString("Kendini yedin!", 80, 130);
					gr.setFont(new Font("tahoma", 1, 13));
					gr.drawString(puan + " puan toplayabildin.", 82, 150);
				} else {
					gr.drawString("Duvara Çarptýn!", 75, 130);
					gr.setFont(new Font("tahoma", 1, 13));
					gr.drawString(puan + " puan toplayabildin.", 77, 150);
				}
				
				gr.setFont(new Font("tahoma", 1, 10));
				gr.drawString("Yeniden baþlamak için ENTER'a bas.", 80, 190);
				return;
			}
			
			gr.setColor(new Color(120, 120, 100));
			gr.fillRect(0, 0, genislik, yukseklik);
			gr.setColor(Color.BLACK);
			gr.drawRect(boyut, boyut, genislik - (boyut * 3) - 1, yukseklik - (boyut * 6) - 1);
			gr.setFont(new Font("Tahoma", 0, 11));
			gr.drawString("Puan: " + puan, 10, 315);
			for(int i = 0; i < yilan.length; i++) {
				gr.drawRect(yilan[i].x, yilan[i].y, boyut, boyut);
			}

			gr.fillOval(yem.x, yem.y, boyut, boyut);
			
		}
	}
}