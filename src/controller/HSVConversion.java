package controller;

public class HSVConversion {
	
	//Variables RGB
	public int red;
	public int green;
	public int blue;
	//Variables HSV
	public float hue;
	public float saturation;
	public float value;
	
	/**
	 * Constructeur qui fait appele a la methode de convertion
	 * RGB a HSV.
	 */
	public HSVConversion(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
		
		rgbToHsv();
	}
	
	/**
	 * Constructeur qui fait appele a la methode de convertion
	 * HSV a RGB.
	 */
	public HSVConversion(float h, float s, float v) {
		this.hue = h;
		this.saturation = s;
		this.value = v;
		
		hsvToRgb();
	}
	
	/**
	 * Cette methode permet de convertir les valeurs HSV en RGB.
	 * Methode de conversion dans powerpoint cours 01 GTI410.
	 */
	public void rgbToHsv() {	
		double r = this.red / 255.0;
		double g = this.green / 255.0;
		double b = this.blue / 255.0;
		
		double min = Math.min(r, Math.min(g, b));
		double max = Math.max(r, Math.max(g, b));
		
	    double h = 0.0;
	    double s = 0.0;
	    double v = max;
		
		if (r == max && g == min)
			h = 5 + (r - b) / (r - g);
		else if (r == max && b == min)
			h = 1 - (r - g) / (r - b);
		else if (g == max && b == min)
			h = 1 + (g - r) / (g - b);
		else if (g == max && r == min)
			h = 3 - (g - b) / (g - r);
		else if (b == max && r == min)
			h = 3 + (b - g) / (b - r);
		else if (b == max && g == min)
			h = 5 - (b - r) / (b - g);
		
		h = h * 60;
		
		if (h < 0)
			h += 360;
		
		s = (v - min) / v;
		
		this.hue = Math.round(h / 360 * 255);
		this.saturation = Math.round(s * 255);
		this.value = Math.round(v * 255);
	}
	

	/**
	 * Cette methode permet de convertir les valeurs HSV en RGB.
	 * Ressources: http://www.had2know.com/technology/hsv-rgb-conversion-formula-calculator.html
	 */
	
	public void hsvToRgb() {
		
		float h = this.hue / 255 * 360;
		float s = this.saturation / 255;
		float v = this.value / 255;
		
		float r = 0;
		float g = 0;
		float b = 0;
		
		float M = 255* v;
		float m = M*(1-s);
		float z = (M-m)*(1 - Math.abs((h/60)%2 - 1));
		
		if (h >= 0 && h < 60){
			r = M; g = z+m; b = m;
		}
		else if(h >= 60 && h < 120){
			r = z+m; g = M; b = m;
		}
		else if(h >= 120 && h < 180){
			r = m; g = M; b = z+m;
		}
		else if(h >= 180 && h < 240){
			r = m; g = z+m; b = M;
		}
		else if(h >= 240 && h < 300){
			r = z+m; g = m; b = M;
		}
		else if(h >= 300 && h < 360){
			r = M; g = m; b = z+m;
		}
		this.red = (int) r;
		this.green = (int) g;
		this.blue = (int) b;
	}
}

