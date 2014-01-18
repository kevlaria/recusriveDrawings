package drawings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Program to do recursive drawings.
 * 
 * @author David Matuszek
 * @author Kevin Lee
 * @version 0.0
 */
public class RecursiveDrawings extends JFrame implements  ActionListener {
    private JPanel drawingPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private JRadioButton[] depthButtons = new JRadioButton[6];
    private ButtonGroup group = new ButtonGroup();
    private JButton[] drawingButtons = new JButton[6];

    private int depth;         // maximum depth of the recursion
    private int drawingNumber; // which drawing to make

    /**
     * Main method for this application.
     * @param args Unused.
     */
    public static void main(String[] args) {
        new RecursiveDrawings().run();
    }

    /**
     * Runs this RecursiveDrawings application.
     */
    public void run() {
        createWidgets();
        layOutGui();
        setSize(600, 600);
        attachListeners();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates all the components needed by the application.
     */
    private void createWidgets() {
        for (int i = 0; i < 6; i++) {
            depthButtons[i] = new JRadioButton(" " + (i + 1));
            group.add(depthButtons[i]);
        }
        depthButtons[0].setSelected(true);
        for (int i = 0; i < 6; i++) {
            drawingButtons[i] = new JButton("Drawing " + (i + 1));
        }
        // TODO: Put better names on the buttons; first one done for you
        drawingButtons[0].setText("Squares");
        drawingButtons[1].setText("Random Trees");
        drawingButtons[2].setText("Sierpinski Triangles");
        drawingButtons[3].setText("Partial Koch Flake");
        drawingButtons[4].setText("Hypnotic Squares");
        drawingButtons[5].setText("Fractal");
        
    }

    /**
     * Arranges the components for this application.
     */
    private void layOutGui() {
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        controlPanel.setLayout(new GridLayout(3, 4));
        for (int i = 0; i < 3; i++) {
            controlPanel.add(depthButtons[i]);
            controlPanel.add(depthButtons[i + 3]);
            controlPanel.add(drawingButtons[i]);
            controlPanel.add(drawingButtons[i + 3]);
        }
        setSize(400, 300);
    }

    /**
     * Attaches this RecursiveDrawings object as a listener for
     * all the drawing buttons. The depth radio buttons don't
     * get listeners.
     */
    private void attachListeners() {
        for (int i = 0; i < 6; i++) {
            drawingButtons[i].addActionListener(this);
        }
    }

    /**
     * Responds to a button press by setting the global variables
     * <code>depth</code> and <code>drawingNumber</code>, then
     * requesting that the painting be done.
     *  
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 6; i++) {
            if (depthButtons[i].isSelected()) depth = i + 1;
            if (e.getSource() == drawingButtons[i]) drawingNumber =  i + 1;
        }
        repaint();
    }
    
    /**
     * Paints one of the drawings, based on the global variables
     * <code>depth</code> and <code>drawingNumber</code>.
     * 
     * @see java.awt.Container#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = drawingPanel.getWidth();
        int height = drawingPanel.getHeight();

        // clear panel
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);

        g.setClip(0, 0, width, height);
        int x = drawingPanel.getWidth() / 2;
        int y = drawingPanel.getHeight() / 2 + 15;
        int baseY = drawingPanel.getHeight() - 15;
        int topY = drawingPanel.getHeight() / 4 + 15;;
        int length = Math.min(x, y - 20);
        
        switch (drawingNumber) {
            case 1:
                squares(g, x, y,  length, depth);
                break;
            case 2:
                trees(g, x, baseY, 100, depth, -90);
              break;
            case 3:
            	sierpinskiTriangle(g, x, y, length, depth);
              break;
            case 4:
            	partialKochFlake(g, x, topY, length, depth, false);
              break;
            case 5:
            	hypnoticSquare(g, x - length / 2, y - length / 2, length, depth, 150);
            	break;
            case 6:
            	fractal(g, 5, y, length / 3, depth);
            	break;
        }
    }

    /**
     * Recursively draw half-sized squares in each corner of the square drawn
     * @param g - graphics element on which to draw
     * @param x - starting x-coordinate
     * @param y - starting y-coordinate
     * @param length - length of one edge of the square
     * @param depth - number of recursions
     */
    private void squares(Graphics g, int startX, int startY, int length, int  depth) {
    	if (depth == 0) return;
        g.drawRect(startX - length / 2, startY - length / 2, length, length);
        squares(g, startX - length / 2, startY - length / 2, length / 2, depth - 1);
        squares(g, startX + length / 2, startY + length / 2, length / 2, depth - 1);        	
        squares(g, startX - length / 2, startY + length / 2, length / 2, depth - 1);        	
        squares(g, startX + length / 2, startY - length / 2, length / 2, depth - 1);        	

    }
    
    /**
     * Recursively draw 2^3 branches to a tree at each level of the recursion. 
     * The angle of tilt for depth > 1 branches has an element of randomness in it
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of the branch
     * @param depth - number of recursions
     * @param angle - angle at which branch tilts from its stem
     */
    private void trees(Graphics g, int startX, int startY, int length, int depth, double angle){
    	if (depth == 0) return;
        int endX = startX + (int) (Math.cos(Math.toRadians(angle)) * length);
        int endY = startY + (int) (Math.sin(Math.toRadians(angle)) * length);
        g.drawLine(startX, startY, endX, endY);
        Random random = new Random();
        int factorLeft = random.nextInt(25) + 20;
        int factorRight = random.nextInt(25) + 20;
        int i = 0;
        while (i < 3){
            trees(g, endX, endY, (int) (length / 1.3), depth - 1, angle - factorLeft);
            trees(g, endX, endY, (int) (length / 1.3), depth - 1, angle + factorRight);
            i ++;
        }

    }
    
    /**
     * Recursively draw three equilateral triangles within each triangle.
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
     * @param depth - number of recursions
     */
    private void sierpinskiTriangle(Graphics g, int startX, int startY, int length, int depth){
    	if (depth == 0) return;

    	Polygon triangle = createEquilateralTriangle(startX, startY, length, false);
    	g.drawPolygon(triangle);
    	if (depth == 1) g.fillPolygon(triangle);

    	sierpinskiTriangle(g, startX, startY, length / 2, depth - 1);
    	sierpinskiTriangle(g, getLeftX(startX, length / 2), getY(startY, length / 2, false), length / 2, depth - 1);
    	sierpinskiTriangle(g, getRightX(startX, length / 2), getY(startY, length /2 , false), length / 2, depth - 1);
    	
    }
    
    /**
     * Recursively draw three equilateral triangles on the sides of each triangle (partial implementation of a Koch Snowflake)
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
     * @param depth - number of recursions
     * @param facingUp - true if triangle is facing up (apex at the top)
     */
    private void partialKochFlake(Graphics g, int startX, int startY, int length, int depth, boolean facingUp){
    	if (depth == 0) return;
    	
		Polygon triangle = createEquilateralTriangle(startX, startY, length, facingUp);
    	g.drawPolygon(triangle);
    	g.setColor(Color.BLACK);
        g.fillPolygon(triangle);



    	if (depth - 1 % 2 == 0){
    		// Determine the starting coordinates for the left and right triangles
        	
    		int newLeftStartX = getLeftX(startX, length * 2 / 3);
    		int newRightStartX = getRightX(startX, length * 2 / 3);
    		int newStartY = getY(startY, length * 2 / 3, !facingUp);
    		partialKochFlake(g, newLeftStartX, newStartY, length / 3, depth - 1, facingUp);
    		partialKochFlake(g, newRightStartX, newStartY, length / 3, depth - 1, facingUp);
    		
    		
    		// Determine the starting coordinates for the top / bottom triangle
    		int existingStartX = getLeftX(startX, length);
    		int existingStartY = getY(startY, length, !facingUp);
    		existingStartX = existingStartX + length / 3; // at corner of the bottom triangle
    		int bottomNewStartX = getRightX(existingStartX, length / 3);
    		int bottomNewStartY = getY(existingStartY, length / 3, !facingUp);
    		partialKochFlake(g, bottomNewStartX, bottomNewStartY, length / 3, depth - 1, facingUp);

    		
    	
    	} else {
    		// Determine the starting coordinates for the left and right triangles
    		
    		int newLeftStartX = getLeftX(startX, length * 2 / 3);
    		int newRightStartX = getRightX(startX, length * 2 / 3);
    		int newStartY = getY(startY, length * 2 / 3, facingUp);
    		partialKochFlake(g, newLeftStartX, newStartY, length / 3, depth - 1, !facingUp);
    		partialKochFlake(g, newRightStartX, newStartY, length / 3, depth - 1, !facingUp);
    		
    		
    		// Determine the starting coordinates for the top / bottom triangle
    		int existingStartX = getLeftX(startX, length);
    		int existingStartY = getY(startY, length, facingUp);
    		existingStartX = existingStartX + length / 3; // at corner of the bottom triangle
    		int bottomNewStartX = getRightX(existingStartX, length / 3);
    		int bottomNewStartY = getY(existingStartY, length / 3, facingUp);
    		partialKochFlake(g, bottomNewStartX, bottomNewStartY, length / 3, depth - 1, !facingUp);
    	}

    	
    }
    
    /**
     * Recursively draw squares within each square, alternating colours at each level
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
     * @param depth - number of recursions
     */
	private void hypnoticSquare(Graphics g, int startX, int startY, int length, int depth, int intensity){
		if (depth == 0) return;
        if (depth % 2 == 1){
            g.setColor(Color.BLACK);
            g.drawRect(startX, startY, length, length);
            g.fillRect(startX, startY, length, length);
        } else {
            g.setColor(new Color(intensity, intensity, intensity));
            g.drawRect(startX, startY, length, length);
            g.fillRect(startX, startY, length, length);
        	
        }
        int newX = startX + length / 4;
        int newY = startY + length / 4;
        hypnoticSquare(g, newX, newY, length / 2, depth - 1, Math.min(intensity + 30, 255));
		
	}
	
	/**
	 * Recursively draw the word "FRACTAL"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 * @param depth - number of recursions
	 */
	private void fractal(Graphics g, int startX, int startY, int length, int depth){
		
		if (depth == 0) return;
		int letterSpace = length + 8;
		drawF(g, startX, startY, length);
		drawR(g, startX + (letterSpace * 1), startY, length);
		drawA(g, startX + (letterSpace * 2), startY, length);
		drawC(g, startX + (letterSpace * 3), startY, length);
		drawT(g, startX + (letterSpace * 4), startY, length);
		drawA(g, startX + (letterSpace * 5), startY, length);
		drawL(g, startX + (letterSpace * 6), startY, length);
		int endX = startX + length;
		int topY = startY - length;
		fractal(g, endX, topY, length / 2, depth - 1);
		
	}
    
	/**
	 * Draws the letter "F"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 */
	private void drawF(Graphics g, int startX, int startY, int length){
		
		// Draw the vertical stem of the 'F'
		int baseY = startY + length;
		g.drawLine(startX, startY, startX, baseY);
		int topY = startY - length;
		g.drawLine(startX, startY, startX, topY);
		
		// Draw the top horizontal line of the 'F'
		int rightX = startX + length;
		g.drawLine(startX, topY, rightX, topY);
		
		// Draw the bottom horizontal line of the 'F' 
		g.drawLine(startX, startY, rightX, startY);
	}
	
	/**
	 * Draws the letter "R"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 */
	private void drawR(Graphics g, int startX, int startY, int length){
		
		// Draw the top horizontal
		
		int rightX = startX + length;
		g.drawLine(startX, startY, rightX, startY);
		
		// Draw the left vertical
		
		int bottomY = startY + length;
		g.drawLine(startX, startY, startX, bottomY);
		
		// Draw the middle horizontal
		
		int midY = startY + (length / 2);
		g.drawLine(startX, midY, rightX, midY);
		
		// Draw the right vertical
		
		g.drawLine(rightX, startY, rightX, midY);
		
		// Draw the diagonal
		
		g.drawLine(startX, midY, rightX, bottomY);
		
	}
	
	/**
	 * Draws the letter "A"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 */
	private void drawA(Graphics g, int startX, int startY, int length){
		
		// Draw the top horizontal
		
		int rightX = startX + length;
		g.drawLine(startX, startY, rightX, startY);
		
		// Draw the left vertical
		
		int bottomY = startY + length;
		g.drawLine(startX, startY, startX, bottomY);
		
		// Draw the middle horizontal
		
		int midY = startY + (length / 2);
		g.drawLine(startX, midY, rightX, midY);
		
		// Draw the right vertical
		
		g.drawLine(rightX, startY, rightX, bottomY);
		
	}
	
	/**
	 * Draws the letter "C"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 */
	private void drawC(Graphics g, int startX, int startY, int length){
		
		// Draw the top horizontal
		
		int rightX = startX + length;
		g.drawLine(startX, startY, rightX, startY);
		
		// Draw the left vertical
		
		int bottomY = startY + length;
		g.drawLine(startX, startY, startX, bottomY);
		
		// Draw the bottom horizontal
		
		g.drawLine(startX, bottomY, rightX, bottomY);

	}
	
	/**
	 * Draws the letter "T"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 */
	private void drawT(Graphics g, int startX, int startY, int length){
		
		// Draw the top horizontal
		
		int rightX = startX + length;
		g.drawLine(startX, startY, rightX, startY);
		
		// Draw the middle vertical
		
		int bottomY = startY + length;
		int midX = startX + (length / 2);
		g.drawLine(midX, startY, midX, bottomY);

	}
	
	/**
	 * Draws the letter "L"
     * @param g - graphics element on which to draw
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of each side
	 */
	private void drawL(Graphics g, int startX, int startY, int length){
				
		// Draw the left vertical
		
		int bottomY = startY + length;
		g.drawLine(startX, startY, startX, bottomY);
		
		// Draw the bottom horizontal
		int rightX = startX + length;		
		g.drawLine(startX, bottomY, rightX, bottomY);

	}
    
    /**
     * Create an equilateral triangle
     * @param startX - starting x-coordinate
     * @param startY - starting y-coordinate
     * @param length - length of a side
     * @param facingDown - true if triangle is facing down (apex at the bottom)
     * @return - resulting equilateral triangle
     */
    private Polygon createEquilateralTriangle(int startX, int startY, int length, boolean facingUp){
    	int bottomLeftX = getLeftX(startX, length);
    	int bottomLeftY = getY(startY, length, facingUp);
    	int bottomRightX = getRightX(startX, length);
    	int bottomRightY = bottomLeftY;

    	int[] xCoordinates = {startX, bottomLeftX, bottomRightX};
    	int[] yCoordinates = {startY, bottomLeftY, bottomRightY};
    	
    	Polygon triangle = new Polygon(xCoordinates, yCoordinates, 3);
    	return triangle;
    }
    
    /**
     * Calculate the left x-coordinate for an equilateral triangle
     * @param startX - starting x-coordinate
     * @param length - length of a side
     * @return
     */
    private int getLeftX(int startX, int length){
    	int leftX = startX - (int)(Math.cos(Math.toRadians(60)) * length);    		
    	return leftX;
    }
    
    /**
     * Calculate the y-coordinate for an equilateral triangle
     * @param startY - starting y-coordinate
     * @param length - length of a side
     * @param facingDown - true if triangle is facing down (apex at the bottom)
     * @return
     */
    private int getY(int startY, int length, boolean facingDown){
    	int y;
    	if (facingDown){
    		y = startY - (int)(Math.sin(Math.toRadians(60)) * length);
    	} else {
    		y = startY + (int)(Math.sin(Math.toRadians(60)) * length);
    	}
    	return y;
    }
    
    /**
     * Calculate the right x-coordinate for an equilateral triangle
     * @param startX - starting x-coordinate
     * @param length - length of a side
     * @return
     */
    private int getRightX(int startX, int length){
    	int rightX = startX + (int)(Math.cos(Math.toRadians(60)) * length);
    	return rightX;
    }

    
}
