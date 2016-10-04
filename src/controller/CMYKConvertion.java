package controller;

import model.Pixel;

public class CMYKConvertion {

public static float[] RGBtoCMYK (Pixel pixel) {
		
		float[] cmyk = new float [4];
		
		int red = pixel.getRed();
 		int green = pixel.getGreen();
 		int blue = pixel.getBlue();
		
		float r = (float)red/255;
		float g = (float)green/255;
		float b = (float)blue/255;
		float k = 1 -Math.max(r, Math.max(g, b));
	
		if ( k != 0){ // pour éviter une division par zéro si telle est la valeur de k
			cmyk[0] = (r-k)/(k);
			cmyk[1] = (g-k)/(k);
			cmyk[2] = (b-k)/(k);
			cmyk[3] = k;
		} else {
			cmyk[0] = r;
			cmyk[1] = g;
			cmyk[2] = b;
			cmyk[3] = k;
		}
	       
		return cmyk;
		
	}
	 
	public static Pixel CMYKtoRGB (float cyan, float magenta, float yellow, float black) {
		float c = cyan/255;
		float m = magenta/255;
		float y = yellow/255;
		float b = black/255;
		
		int red = (int) (255 * (1-c)*(1-b));
		int green = (int) (255 * (1-m)*(1-b));
		int blue = (int) (255 * (1-y)*(1-b));

	    Pixel rgbPixel = new Pixel(red, green, blue);
		
		return rgbPixel;
		
	}
	
	public static float getCyanFromRGB(Pixel rgb){
		float[] cmyk = RGBtoCMYK(rgb);
		return cmyk[0];
	}
	
	public static float getMagentaFromRGB(Pixel rgb){
		float[] cmyk = RGBtoCMYK(rgb);
		return cmyk[1];
	}
	
	public static float getYellowFromRGB(Pixel rgb){
		float[] cmyk = (RGBtoCMYK(rgb));
		return cmyk[2];
	}
	
	public static float getBlackFromRGB(Pixel rgb){
		float[] cmyk = RGBtoCMYK(rgb);
		return cmyk[3];
	}
}
