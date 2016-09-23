package view;

import java.awt.image.BufferedImage;

import controller.YCbCrConversion;
import model.ObserverIF;
import model.Pixel;

public class YCbCrColorMediator extends Object implements SliderObserver, ObserverIF {

	ColorSlider yCS;
	ColorSlider cbCS;
	ColorSlider crCS;
	
	int red;
	int green;
	int blue;
	
	float y;
	float cb;
	float cr;
	float[] ycbcr;
	
	BufferedImage yImage;
	BufferedImage cbImage;
	BufferedImage crImage;
	
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	YCbCrColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		
		this.ycbcr = YCbCrConversion.RGBtoYCbCr(result.getPixel());
		
		this.y = (int) ycbcr[0];
		this.cb = (int) ycbcr[1];
		this.cr = (int) ycbcr[2];
		
		
		
		this.result = result;
		result.addObserver(this);
		
		yImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		crImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		cbImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		computeYImage(y, cb, cr);
		computeCbImage(y, cb, cr);
		computeCrImage(y, cb, cr); 	
	}
	
	

	@Override
	public void update(ColorSlider s, int v) {
		// TODO Auto-generated method stub
		boolean updateY = false;
		boolean updateCb = false;
		boolean updateCr = false;
		
		if (s == yCS && v != y) {
			y = v;
			updateCb = true;
			updateCr = true;
		}
		if (s == cbCS && v != cb) {
			cb = v;
			updateY = true;
			updateCr = true;
		}
		if (s == crCS && v != cr) {
			cr = v;
			updateY = true;
			updateCb = true;
		}
		
		
		if (updateY) {
			computeYImage(y, cb, cr);
		}
		if (updateCb) {
			computeCbImage(y, cb, cr);
		}
		if (updateCr) {
			computeCrImage(y, cb, cr);
		}
		
		Pixel pixel = YCbCrConversion.YCbCrtoRGB(y, cb, cr);
		
		result.setPixel(pixel);
		
	}
	
	//INTERPOLATION POUR LES SLIDES
	public void computeYImage(float y, float cb, float cr) {
		for (int i = 0; i<imagesWidth; ++i) {	
			Pixel pixel = YCbCrConversion.YCbCrtoRGB((((float) i / (float) imagesWidth)), cb, cr);
			int rgb = pixel.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yImage.setRGB(i, j, rgb);
			}
		}
		
		if (yCS != null) {
			yCS.update(yImage);
		}
	}
	
	public void computeCbImage(float y, float cb, float cr) {
		for (int i = 0; i<imagesWidth; ++i) {	
			Pixel pixel = YCbCrConversion.YCbCrtoRGB(y, (((float) i / (float) imagesWidth)), cr);
			int rgb = pixel.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cbImage.setRGB(i, j, rgb);
			}
		}
		
		if (cbCS != null) {
			cbCS.update(yImage);
		}
	}
	
	public void computeCrImage(float y, float cb, float cr) {
		for (int i = 0; i<imagesWidth; ++i) {	
			Pixel pixel = YCbCrConversion.YCbCrtoRGB(y, cb, (((float) i / (float) imagesWidth)));
			int rgb = pixel.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				crImage.setRGB(i, j, rgb);
			}
		}
		
		if (crCS != null) {
			crCS.update(yImage);
		}
	}
	/**
	 * @return
	 */
	public BufferedImage getYImage() {
		return yImage;
	}
	
	/**
	 * @return
	 */
	public BufferedImage getCbImage() {
		return cbImage;
	}
	
	/**
	 * @return
	 */
	public BufferedImage getCrImage() {
		return crImage;
	}
	
	
	/**
	 * @param slider
	 */
	public void setYCS(ColorSlider slider) {
		yCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setCbCS(ColorSlider slider) {
		cbCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setCrCS(ColorSlider slider) {
		crCS = slider;
		slider.addObserver(this);
	}
	

	
	/**
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return
	 */
	public double getCb() {
		return cb;
	}

	/**
	 * @return
	 */
	public double getCr() {
		return cr;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		ycbcr = YCbCrConversion.RGBtoYCbCr(result.getPixel());
		y = ycbcr[0];
		cb = ycbcr[1];
		cr = ycbcr[2];
		
		yCS.setValue((int)y);
		cbCS.setValue((int)cb);
		crCS.setValue((int)cr);
		
		
		computeYImage(y, cb, cr);
		computeCbImage(y, cb, cr);
		computeCrImage(y, cb, cr);
		
	}

}
