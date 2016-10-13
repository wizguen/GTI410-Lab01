package view;

import java.awt.image.BufferedImage;
import controller.HSVConversion;
import model.ObserverIF;
import model.Pixel;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF {
	
	//Color slide pour le hue
	ColorSlider hueCS;
	//Color slider pour la saturation
	ColorSlider saturationCS;
	//Color slider for le value
	ColorSlider valueCS;
	//Variables RGB
	int red;
	int green;
	int blue;
	//Variables HSV
	float hue;
	float saturation;
	float value;
	//Hue slider image
	BufferedImage hueImage;
	//Saturation slider image
	BufferedImage saturationImage;
	//Value slider image
	BufferedImage valueImage;
	//Width(horizontal) for color slider image 
	int imagesWidth;
	//Height(vertical) for color slider image
	int imagesHeight;
	//
	ColorDialogResult result;

	public HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		
		this.result = result;
		result.addObserver(this);
		
		//Faire la convertion RBG a HSV pour obternir les valeurs en Hue, Saturation, Valeur
		HSVConversion hsvValues = new HSVConversion(red, green, blue);
		//Les valeurs de HSV converties
		this.hue = hsvValues.hue;
		this.saturation = hsvValues.saturation;
		this.value = hsvValues.value;
		
		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value); 
	}
	
	
	public void computeHueImage(float hue, float saturation, float value) {
		
		//On a les valeur HSV, on veut les valeurs RGB
		HSVConversion rbgValues = new HSVConversion(hue, saturation, value);
		this.red = rbgValues.red;
		this.green = rbgValues.green;
		this.blue = rbgValues.blue;
		
		Pixel p = new Pixel(red, green, blue, 255);
		
		for (int i = 0; i < imagesWidth; i++){
			
			HSVConversion hsvValues = new HSVConversion(
									(int)(((double)i / (double)imagesWidth)*255.0), 
									rbgValues.saturation, rbgValues.value);
			p.setRed(hsvValues.red);
			p.setGreen(hsvValues.green);
			p.setBlue(hsvValues.blue);
			
			int rgb = p.getARGB();
			
			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, rgb);
			}
			
		}
		
		if (hueCS != null) {
			hueCS.update(hueImage);
		}			
	
	}
	
	public void computeSaturationImage(float hue, float saturation, float value) {
		//On a les valeur HSV, on veut les valeurs RGB
		HSVConversion rbgValues = new HSVConversion(hue, saturation, value);
		this.red = rbgValues.red;
		this.green = rbgValues.green;
		this.blue = rbgValues.blue;
		
		Pixel p = new Pixel(red, green, blue, 255);
		
		for (int i = 0; i < imagesWidth; i++){
			
			HSVConversion hsvValues = new HSVConversion(rbgValues.hue, 
								(int)(((double)i / (double)imagesWidth)*255.0), 
								rbgValues.value);
		
			p.setRed(hsvValues.red);
			p.setGreen(hsvValues.green);
			p.setBlue(hsvValues.blue);
			
			int rgb = p.getARGB();
			
			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
			
		}
		
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}			
	}
	
	
	public void computeValueImage(float hue, float saturation, float value) {
		//On a les valeur HSV, on veut les valeurs RGB
		HSVConversion rbgValues = new HSVConversion(hue, saturation, value);
		this.red = rbgValues.red;
		this.green = rbgValues.green;
		this.blue = rbgValues.blue;
		
		Pixel p = new Pixel(red, green, blue, 255);
		
		for (int i = 0; i < imagesWidth; i++){
			
			HSVConversion hsvValues = new HSVConversion(rbgValues.hue, 
								rbgValues.saturation, 
								(int)(((double)i / (double)imagesWidth)*255.0));
		
			p.setRed(hsvValues.red);
			p.setGreen(hsvValues.green);
			p.setBlue(hsvValues.blue);
			
			int rgb = p.getARGB();
			
			for (int j = 0; j<imagesHeight; ++j) {
				valueImage.setRGB(i, j, rgb);
			}
			
		}
		
		if (valueCS != null) {
			valueCS.update(valueImage);
		}	
	}
	
	
	@Override
	public void update() {

		// When updated with the new "result" color, if the "currentColor"
		// is aready properly set, there is no need to recompute the images.
		Pixel currentColor = new Pixel(red, green, blue, 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		//Convertir pour obtenir valeur HSV
		HSVConversion hsvValues = new HSVConversion(red, green, blue);
		this.hue = hsvValues.hue;
		this.saturation = hsvValues.saturation;
		this.value = hsvValues.value;
		
		//Setter les bonnes valeurs pour les CS de HSV
		hueCS.setValue((int) this.hue);
		saturationCS.setValue((int) this.saturation);
		valueCS.setValue((int) this.value);
		
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value); 
		
	}

	@Override
	public void update(ColorSlider s, int v) {
		
		boolean updateHue = false;
		boolean updateSaturation = false;
		boolean updateValue = false;
		if (s == hueCS && v != hue) {
			hue = v;
			updateSaturation = true;
			updateValue = true;
		}
		if (s == saturationCS && v != saturation) {
			saturation = v;
			updateHue = true;
			updateValue = true;
		}
		if (s == valueCS && v != value) {
			value = v;
			updateHue = true;
			updateSaturation = true;
		}
		if (updateHue) {
			computeHueImage(hue, saturation, value);
		}
		if (updateSaturation) {
			computeSaturationImage(hue, saturation, value);
		}
		if (updateValue) {
			computeValueImage(hue, saturation, value);
		}
		
		HSVConversion rgbValues = new HSVConversion(hue, saturation, value);
		this.red = rgbValues.red;
		this.green = rgbValues.green;
		this.blue = rgbValues.blue;
		
		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);
		
	}
	public void setHueCS(ColorSlider slider) {
		hueCS = slider;
		slider.addObserver(this);
		
	}
	public void setSaturationCS(ColorSlider slider) {
		saturationCS = slider;
		slider.addObserver(this);
		
	}
	public void setValueCS(ColorSlider slider) {
		valueCS = slider;
		slider.addObserver(this);
		
	}
	public BufferedImage getHueImage() {
		// TODO Auto-generated method stub
		return hueImage;
	}
	public BufferedImage getSaturationImage() {
		// TODO Auto-generated method stub
		return saturationImage;
	}
	public BufferedImage getValueImage() {
		// TODO Auto-generated method stub
		return valueImage;
	}

}
