package controller;

import model.Pixel;

public class YCbCrConversion {
	
	//The conversion of RGB colors into full-range YCbCr colors
	public static float[] RGBtoYCbCr (Pixel pixel) {
		
		float[] ycbcr = new float [3];
		
		int red = pixel.getRed();
 		int green = pixel.getGreen();
 		int blue = pixel.getBlue();
 		
 		 //Transformation des couleurs
		ycbcr [0]  = (int)( 0.299   * red + 0.587   * green + 0.114   * blue );
		ycbcr [1] = (int)(-0.16874 * red - 0.33126 * green + 0.50000 * blue + 128 ) ;
		ycbcr [2] = (int)( 0.50000 * red - 0.41869 * green - 0.08131 * blue + 128 );
	
		return ycbcr;
		
	}
	 //The conversion of full-range YCbCr color into RGB
	public static Pixel YCbCrtoRGB (float y, float cb, float cr) {
		
		int red = (int) ( y + 1.4 * (cr - 128) );
		int green = (int) ( y - 0.343 * (cb - 128) - 0.711 * (cr - 128));
		int blue = (int) ( y + (1.765 * (cb - 128)));
		System.out.println("Y: " + y);
		System.out.println("Cb: " + cb);
		System.out.println("Cr: " + cr);
		System.out.println("Red: " + red);
		System.out.println("Green: " + green);
		System.out.println("Blue: " + blue);
		
	    Pixel rgbPixel = new Pixel(red, green, blue);
		
		return rgbPixel;
		
	}
	
	public static float getYFromRGB(Pixel rgb){
		float[] ycbcr = RGBtoYCbCr(rgb);
		return ycbcr[0];
	}
	
	public static float getCbFromRGB(Pixel rgb){
		float[] ycbcr = RGBtoYCbCr(rgb);
		return ycbcr[1];
	}
	
	public static float getCrFromRGB(Pixel rgb){
		float[] ycbcr = RGBtoYCbCr(rgb);
		return ycbcr[2];
	}

}
