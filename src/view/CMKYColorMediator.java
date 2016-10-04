package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

public class CMKYColorMediator extends Object implements SliderObserver, ObserverIF {
	
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	float[] cmyk;
	
		
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider blackCS;
	
	float cyan;
	float magenta;
	float yellow;
	float black;
	
	
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;

	int red;
	int green;
	int blue;
	
	
	CMKYColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		
		rgbToCmyk(result.getPixel());
		
		this.result = result;
		result.addObserver(this);
		
		cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		blackImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		
		Pixel pixel = cmykTorgb(cyan,magenta,yellow,black);
		
		computeCyanImage(cyan,black,pixel);
		computeMagentaImage(magenta,black,pixel);
		computeYellowImage(yellow, black,pixel);
		computeBlackImage(cyan,magenta,yellow, black,pixel); 
	}
	

	/*
	 * @see View.SliderObserver#update(double)
	 */
	public void update(ColorSlider s, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		boolean updateBalck = false;
		double newValue = (double)v / 255.0;
		
		if (s == cyanCS && newValue != cyan) {
			cyan = v;
			updateMagenta = true;
			updateYellow = true;
			updateBalck = true;
		}
		if (s == magentaCS && newValue != magenta) {
			magenta = v;
			updateCyan = true;
			updateYellow = true;
			updateBalck = true;
		}
		if (s == yellowCS && newValue != yellow) {
			yellow = v;
			updateCyan = true;
			updateMagenta = true;
			updateBalck = true;
		}
		if (s == blackCS && newValue != black){
			black = v;
			updateCyan = true;
			updateMagenta = true;
			updateYellow = true;
		}
		
		Pixel pix = cmykTorgb(cyan,magenta,yellow,black);
		if (updateCyan) {
			computeCyanImage(cyan,black,pix);
			
		}
		if (updateMagenta) {
			computeMagentaImage(magenta,black,pix);
			
		}
		if (updateYellow) {
			computeYellowImage(yellow, black,pix);
		}
		if (updateBalck) {
			computeBlackImage(cyan,magenta,yellow, black,pix);  
		}
		result.setPixel(pix);
	}
	
	public void computeCyanImage(float cyan, float black, Pixel px) { 
		Pixel p = new Pixel(px.getARGB());
		
		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int) (((float)i / (float)imagesWidth)*255.0)); 
			p.setBlue((int)(((float)i /(float) imagesWidth)*255.0));
			p.setRed((int)(black*255.0));
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	public void computeMagentaImage(float magenta, float black,Pixel px ) {
		Pixel p = new Pixel(px.getARGB());
		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(black*255.0)); 
			p.setBlue((int)(((float)i / (float)imagesWidth)*255.0));
			p.setRed((int)(((float)i / (float)imagesWidth)*255.0));
			
			int rgb = p.getARGB();	
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
	}
	
	public void computeYellowImage(float yellow, float black, Pixel px) {
		Pixel p = new Pixel(px.getARGB());
		for (int i = 0; i<imagesWidth; ++i) {	
			p.setGreen((int)(((float)i /(float) imagesWidth)*255.0)); 
			p.setBlue((int)(black*255.0));
			p.setRed((int)(((float)i / (float)imagesWidth)*255.0));
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
	}
	
	public void computeBlackImage(float cyan, float magenta, float yellow, float black, Pixel px) { 

		Pixel p = new Pixel(px.getARGB());
		
		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(((float)i / (float)imagesWidth)*255.0)); 
			p.setBlue((int)(((float)i / (float)imagesWidth)*255.0));
			p.setRed((int)(((float)i / (float)imagesWidth)*255.0));
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				blackImage.setRGB(i, j, rgb);
			}
		}
		if (blackCS != null) {
			blackCS.update(blackImage);
		}
	}
	
	/**
	 * @return
	 */
	public BufferedImage getYellowImage() {
		return yellowImage;
	}

	/**
	 * @return
	 */
	public BufferedImage getMagentaImage() {
		return magentaImage;
	}

	/**
	 * @return
	 */
	public BufferedImage getCyanImage() {
		return cyanImage;
	}
	public BufferedImage getBlackImage() {
		return blackImage;
	}

	/**
	 * @param slider
	 */
	public void setCyanCS(ColorSlider slider) {
		this.cyanCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setMagentaCS(ColorSlider slider) {
		this.magentaCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setYellowCS(ColorSlider slider) {
		this.yellowCS = slider;
		slider.addObserver(this);
	}
	
	public void setBlackCS(ColorSlider slider) {
		this.blackCS = slider;
		slider.addObserver(this);
	}
	/**
	 * @return
	 */
	public double getBlue() {
		return this.blue;
	}

	/**
	 * @return
	 */
	public double getGreen() {
		return this.green;
	}

	/**
	 * @return
	 */
	public double getRed() {
		return this.red;
	}

	

	public float getCyan() {
		return this.cyan;
	}


	public float getMagenta() {
		return this.magenta;
	}


	public float getYellow() {
		return this.yellow;
	}


	public float getBlack() {
		return this.black;
	}


	public void update() {
		
		Pixel p = cmykTorgb(cyan,magenta,yellow,black);
		if(p.getARGB() == result.getPixel().getARGB())
			return;
		
		rgbToCmyk(result.getPixel());
		cyanCS.setValue((int) (cyan*255));
		magentaCS.setValue((int) (magenta*255));
		yellowCS.setValue((int) (yellow*255));
		blackCS.setValue((int) (black*255));
		
		computeCyanImage(cyan,black,result.getPixel());
		computeMagentaImage(magenta,black, result.getPixel());
		computeYellowImage(yellow, black,result.getPixel());
		computeBlackImage(cyan,magenta,yellow, black,result.getPixel()); 		
	}
	
	

	
	private Pixel cmykTorgb(float cyan2, float magenta2, float yellow2, float black2)
	{
		int red, bleu,green;
				
		Pixel p;
		
		float cy = cyan2/255;
		float ma = magenta2 /255;
		float yell = yellow2 / 255;
		float black = black2 / 255;
		
		red =  (int) ((1 - black) * (1 - cy) * 255);
		green = (int) ((1 - black) * (1 - ma) * 255);
		bleu = (int) ((1 - black) * (1 - yell) * 255);
		
		
		p = new Pixel (red,bleu,green);		
		
		return p;
		
	}
	
	private void rgbToCmyk(Pixel pix)
	{
		int r = pix.getRed();
		int g = pix.getGreen();
		int b = pix.getBlue();
		
		float redP = r/255;
		float greenP = g/255;
		float bleuP = b/255;
		
		float c = 1 - redP;
		float m = 1 - greenP;
		float y = 1 - bleuP;
		
		int k = (int) Math.min(c, Math.min(m, y));
		
		if(k == 1)
		{
			this.cyan = 0;
			this.magenta = 0;
			this.yellow = 0;
		}else
		{
			float s = 1 - k;
			this.cyan = (c - k)/s;
			this.magenta = (m - k)/s;
			this.yellow = (y - k)/s;
		}
		
	
	}
	



}
