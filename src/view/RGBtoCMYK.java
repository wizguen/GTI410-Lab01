package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

public class RGBtoCMYK extends Object implements SliderObserver, ObserverIF {
	
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
		
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider blackCS;
	
	int cyan;
	int magenta;
	int yellow;
	int black;
	
	int redP;
	int greenP;
	int bleuP;
	int blackP;
	
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;

	int red;
	int green;
	int blue;
	
	
	RGBtoCMYK(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		
		this.redP = this.red / 255;
		this.greenP = this.green / 255;
		this.bleuP = this.blue / 255;
		int temp = Math.max(redP, greenP);
		this.blackP = 1 - Math.max(temp, bleuP);
		this.black = this.blackP;
		
		if(blackP != 1){		
		this.cyan = (1 - this.redP - this.blackP) / (1 - this.blackP);
		this.magenta = (1 - this.greenP - this.blackP) / (1 - this.blackP);
		this.yellow = (1 - this.bleuP - this.blackP) / (1 - this.blackP);		
		}
		else
		{
			this.cyan = 0;
			this.magenta = 0;
			this.yellow = 0;
		}
		
		
		
		this.result = result;
		result.addObserver(this);
		
		cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		blackImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		Pixel p = new Pixel(cyanToRed(cyan,black),magentaToGreen(magenta,black),yellowToBlue(yellow,black));		
		computeCyanImage(cyan, black, p);
		computeMagentaImage(magenta, black, p);
		computeYellowImage(yellow, black, p);
		computeBlackImage(cyan,magenta,yellow, black,p); 
	}
	

	/*
	 * @see View.SliderObserver#update(double)
	 */
	public void update(ColorSlider s, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		boolean updateBalck = false;
		
		if (s == cyanCS && (v/255) != cyan) {
			cyan = v/255;
			updateMagenta = true;
			updateYellow = true;
			updateBalck = true;
		}
		if (s == magentaCS && (v/255) != magenta) {
			magenta = v/255;
			updateCyan = true;
			updateYellow = true;
			updateBalck = true;
		}
		if (s == yellowCS && (v/255) != yellow) {
			yellow = v/255;
			updateMagenta = true;
			updateCyan = true;
			updateBalck = true;
		}
		if (s == blackCS && (v/255) != black) {
			black = v;
			updateMagenta = true;
			updateCyan = true;
			updateYellow = true;
		}
		Pixel p = new Pixel(cyanToRed(cyan,black),magentaToGreen(magenta,black),yellowToBlue(yellow,black));
		if (updateCyan) {
			computeCyanImage(cyan, black, p);
			
		}
		if (updateMagenta) {
			computeMagentaImage(magenta, black, p);
			
		}
		if (updateYellow) {
			computeYellowImage(yellow, black, p);
		}
		if (updateBalck) {
			computeBlackImage(cyan,magenta,yellow, black,p); 
		}
		
		result.setPixel(p);
	}
	
	public void computeCyanImage(int cyan, int black, Pixel ipRGB) { 
		Pixel p = new Pixel(ipRGB.getARGB()); 
		int redNe;
		for (int i = 0; i<imagesWidth; ++i) {
			redNe=cyanToRed((i / imagesWidth), black);
			p.setRed(redNe); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	public void computeMagentaImage(int magenta, int black, Pixel ipRGB) {
		Pixel p = new Pixel(ipRGB.getARGB());
		int magNe;
		for (int i = 0; i<imagesWidth; ++i) {
			magNe = yellowToBlue((i / imagesWidth), black);
			p.setGreen(magNe); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
	}
	
	public void computeYellowImage(int yellow, int black, Pixel ipRGB) {
		Pixel p = new Pixel(ipRGB.getARGB());
		int yellNe;
		for (int i = 0; i<imagesWidth; ++i) {
			yellNe = yellowToBlue((i / imagesWidth), black);
			p.setBlue(yellNe); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
	}
	
	public void computeBlackImage(int cyan, int magenta, int yellow, int black, Pixel ipRGB) { 

		Pixel p = new Pixel(ipRGB.getARGB());
		int redN, greenN, blueN, blackN;
		
		
		for (int i = 0; i<imagesWidth; ++i) {
			blackN = ((i / imagesWidth));
			redN = cyanToRed(cyan, blackN);
			greenN = magentaToGreen(magenta, blackN);
			blueN = yellowToBlue(yellow, blackN);
			p.setRed(redN); 
			p.setGreen(greenN); 
			p.setBlue(blueN); 
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

	

	public int getCyan() {
		return this.cyan;
	}


	public int getMagenta() {
		return this.magenta;
	}


	public int getYellow() {
		return this.yellow;
	}


	public int getBlack() {
		return this.black;
	}


	/* (non-Javadoc)
	 * @see model.ObserverIF#update()
	 */
	public void update() {
		// When updated with the new "result" color, if the "currentColor"
		// is aready properly set, there is no need to recompute the images.
		Pixel p = new Pixel(cyanToRed(cyan,black),magentaToGreen(magenta,black),yellowToBlue(yellow,black));
		
		if(p.getARGB() == result.getPixel().getARGB()) return;
		
		cyanCS.setValue(cyan*255);
		magentaCS.setValue(magenta*255);
		yellowCS.setValue(yellow*255);
		blackCS.setValue(black*255);
		
		computeCyanImage(cyan, black, p);
		computeMagentaImage(magenta, black, p);
		computeYellowImage(yellow, black, p);
		computeBlackImage(cyan,magenta,yellow, black,p); 		
		
		
		// Efficiency issue: When the color is adjusted on a tab in the 
		// user interface, the sliders color of the other tabs are recomputed,
		// even though they are invisible. For an increased efficiency, the 
		// other tabs (mediators) should be notified when there is a tab 
		// change in the user interface. This solution was not implemented
		// here since it would increase the complexity of the code, making it
		// harder to understand.
	}
	
	private int cyanToRed(int cyan, int black)
	{
		return (1 - black) * (1 - cyan) * 255;
	}
	
	private int magentaToGreen(int magenta, int black)
	{
		return (1 - black) * (1 - magenta) * 255;
	}
	
	private int yellowToBlue(int blue, int black)
	{
		return (1 - black) * (1 - blue) * 255;
	}

	



}
