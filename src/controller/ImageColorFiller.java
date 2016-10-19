package controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;
import java.util.Stack;

import model.ImageX;
import model.Pixel;
import model.Shape;

public class ImageColorFiller extends AbstractTransformer {
	private ImageX currentImage;
	private Pixel fillColor = new Pixel(0xFF00FFFF);
	private Pixel borderColor = new Pixel(0xFFFFFF00);
	private boolean floodFill = true;
	private int hueThreshold = 1;
	private int saturationThreshold = 2;
	private int valueThreshold = 3;
	
	/**
	 * Creates an ImageLineFiller with default parameters.
	 * Default pixel change color is black.
	 */
	public ImageColorFiller() {
	}
	
	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_FLOODER; } 
	
	protected boolean mouseClicked(MouseEvent e){
		List<?> intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
		if (!intersectedObjects.isEmpty()) {
			Shape shape = (Shape)intersectedObjects.get(0);
			if (shape instanceof ImageX) {
				currentImage = (ImageX)shape;
				currentImage.getImageWidth();

				Point pt = e.getPoint();
				Point ptTransformed = new Point();
				try {
					shape.inverseTransformPoint(pt, ptTransformed);
				} catch (NoninvertibleTransformException e1) {
					e1.printStackTrace();
					return false;
				}
				ptTransformed.translate(-currentImage.getPosition().x, -currentImage.getPosition().y);
				if (0 <= ptTransformed.x && ptTransformed.x < currentImage.getImageWidth() &&
				    0 <= ptTransformed.y && ptTransformed.y < currentImage.getImageHeight()) {
					currentImage.beginPixelUpdate();
					
					if(isFloodFill())
					{
						floodFill(ptTransformed);
					}
					else
						boundaryFill(ptTransformed);
					
					currentImage.endPixelUpdate();											 	
					return true;
				}
			}
		}
		return false;
	}

	
	public void floodFill(Point ptClicked)
	{
		
		Stack<Point> stack = new Stack<Point>();
		//Couleur initiale
		Pixel couleurActuel = currentImage.getPixel(ptClicked.x, ptClicked.y);		
		stack.push(ptClicked);		
		
		while (!stack.empty()) 
		{
			Point current = (Point)stack.pop();
			//Pixel couleurSuivante = currentImage.getPixel(current.x, current.y);
						
			if(0 <= current.x && current.x < currentImage.getImageWidth() 
					&& 0 <= current.y && current.y < currentImage.getImageHeight())
			{
				if (!this.currentImage.getPixel(current.x, current.y).equals(this.fillColor) &&
					     this.currentImage.getPixel(current.x, current.y).equals(couleurActuel) && estDansSeuil(current))
					{
						// peinturer
						this.currentImage.setPixel(current.x, current.y, this.fillColor);
						
						// voir les voisins
						Point nextLeft = new Point(current.x-1, current.y);
						Point nextRight = new Point (current.x+1, current.y);
						Point nextUp = new Point (current.x, current.y-1);
						Point nextDown = new Point (current.x, current.y+1);
						
						stack.push(nextLeft);
						stack.push(nextRight);
						stack.push(nextUp);
						stack.push(nextDown);
					}
			}			
			
		}			
		
	}
	
	public void boundaryFill(Point ptClicked)
	{
		
		Stack<Point> stack = new Stack<Point>();
		currentImage.getPixel(ptClicked.x, ptClicked.y);		
		stack.push(ptClicked);		
		
		System.out.println("Boudary fill ...");
		while (!stack.empty()) 
		{
			Point current = (Point)stack.pop();
			//Pixel couleurSuivante = currentImage.getPixel(current.x, current.y);
						
			if(0 <= current.x && current.x < currentImage.getImageWidth() 
					&& 0 <= current.y && current.y < currentImage.getImageHeight())
			{
				//Si couleurActuel different du fillColor...
				if (!this.currentImage.getPixel(current.x, current.y).equals(this.fillColor) &&
						estDansSeuil(current))
						
					  //  !this.currentImage.getPixel(current.x, current.y).equals(this.borderColor))
				{	
					this.currentImage.setPixel(current.x, current.y, this.fillColor);
					// Regarder les voisins
					Point nextLeft = new Point(current.x-1, current.y);
					Point nextRight = new Point (current.x+1, current.y);
					Point nextUp = new Point (current.x, current.y-1);
					Point nextDown = new Point (current.x, current.y+1);
					
					stack.push(nextLeft);
					stack.push(nextRight);
					stack.push(nextUp);
					stack.push(nextDown);
					//System.out.println("Iteration ...");
				}
			}			
		}			
	}
    
    private boolean estDansSeuil(Point pointActuel){
    	
		Pixel pixelActuel = this.currentImage.getPixel(pointActuel.x, pointActuel.y);
		Pixel pixelBoundary = this.borderColor;
		
		//Valeur HSV du pixel actuel
		HSVConversion hsvpixelActuel = new HSVConversion(pixelActuel.getRed(), pixelActuel.getGreen(), pixelActuel.getBlue());
		float[] hsvActuel = {hsvpixelActuel.hue, hsvpixelActuel.saturation, hsvpixelActuel.value}; 
		
		//Valeur HSV du boundary
		HSVConversion hsvpixelBoundary = new HSVConversion(pixelBoundary.getRed(), pixelBoundary.getGreen(), pixelBoundary.getBlue());
		float[] hsvBoundary = {hsvpixelBoundary.hue, hsvpixelBoundary.saturation, hsvpixelBoundary.value}; 
    	
		//hsvActuel[0] = Hue Actuel				hsvBorder[0] = Hue Boundary
		//hsvActuel[1] = Saturation Actuel		hsvBorder[1] = Saturation Boundary
		//hsvActuel[2] = Valeur Actuel			hsvBorder[2] = Valeur Boundary
		float hueDifference = hsvBoundary[0] - this.hueThreshold ;
		float saturationDifference = hsvBoundary[1] - this.saturationThreshold ;
		float valueDifference = hsvBoundary[2] - this.valueThreshold ;
		
		if ( hueDifference <= hsvActuel[0] && saturationDifference <= hsvActuel[1] && valueDifference <= hsvActuel[2] ){
			//test
			System.out.println("Faux");
			return false;
			}
		//test
		System.out.println("Vrai");
		return true;
		}
	
	/**
	 * @return
	 */
	public Pixel getBorderColor() {
		return borderColor;
	}

	/**
	 * @return
	 */
	public Pixel getFillColor() {
		return fillColor;
	}

	/**
	 * @param pixel
	 */
	public void setBorderColor(Pixel pixel) {
		borderColor = pixel;
		System.out.println("new border color");
	}

	/**
	 * @param pixel
	 */
	public void setFillColor(Pixel pixel) {
		fillColor = pixel;
		System.out.println("new fill color");
	}
	/**
	 * @return true if the filling algorithm is set to Flood Fill, false if it is set to Boundary Fill.
	 */
	public boolean isFloodFill() {
		return floodFill;
	}

	/**
	 * @param b set to true to enable Flood Fill and to false to enable Boundary Fill.
	 */
	public void setFloodFill(boolean b) {
		floodFill = b;
		if (floodFill) {
			System.out.println("now doing Flood Fill");
		} else {
			System.out.println("now doing Boundary Fill");
		}
	}

	/**
	 * @return
	 */
	public int getHueThreshold() {
		return hueThreshold;
	}

	/**
	 * @return
	 */
	public int getSaturationThreshold() {
		return saturationThreshold;
	}

	/**
	 * @return
	 */
	public int getValueThreshold() {
		return valueThreshold;
	}

	/**
	 * @param i
	 */
	public void setHueThreshold(int i) {
		hueThreshold = i;
		System.out.println("new Hue Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setSaturationThreshold(int i) {
		saturationThreshold = i;
		System.out.println("new Saturation Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setValueThreshold(int i) {
		valueThreshold = i;
		System.out.println("new Value Threshold " + i);
	}

}
