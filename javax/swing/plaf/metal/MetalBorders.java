/*
 * @(#)MetalBorders.java	1.40 09/08/10
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.swing.plaf.metal;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.JTextComponent;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;


/**
 * Factory object that can vend Borders appropriate for the metal L & F.
 * @author Steve Wilson
 * @version 1.40 08/10/09
 */

public class MetalBorders {

    /**
     * Client property indicating the button shouldn't provide a rollover
     * indicator. Only used with the Ocean theme.
     */
    static Object NO_BUTTON_ROLLOVER = new Object(); // NoButtonRollover


    public static class Flush3DBorder extends AbstractBorder implements UIResource{

        public void paintBorder(Component c, Graphics g, int x, int y,
			  int w, int h) {
            if (c.isEnabled()) {
                MetalUtils.drawFlush3DBorder(g, x, y, w, h);
            } else {
                MetalUtils.drawDisabledBorder(g, x, y, w, h);
            }
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top = 2;
            newInsets.left = 2;
            newInsets.bottom = 2;
            newInsets.right = 2;
	    return newInsets;
	}	    
    }

    public static class ButtonBorder extends AbstractBorder implements UIResource {

        protected static Insets borderInsets = new Insets( 3, 3, 3, 3 );

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            if (MetalLookAndFeel.usingOcean()) {
                paintOceanBorder(c, g, x, y, w, h);
                return;
            }
            AbstractButton button = (AbstractButton)c;
	    ButtonModel model = button.getModel();

	    if ( model.isEnabled() ) {
                boolean isPressed = model.isPressed() && model.isArmed();
                boolean isDefault = (button instanceof JButton && ((JButton)button).isDefaultButton());

	        if (isPressed && isDefault) {
                    MetalUtils.drawDefaultButtonPressedBorder(g, x, y, w, h);
                } else if (isPressed) {
                    MetalUtils.drawPressed3DBorder( g, x, y, w, h );
	        } else if (isDefault) {
                    MetalUtils.drawDefaultButtonBorder( g, x, y, w, h, false);
                } else {
                    MetalUtils.drawButtonBorder( g, x, y, w, h, false);
	        }
	    } else { // disabled state
	        MetalUtils.drawDisabledBorder( g, x, y, w-1, h-1 );
	    }
        }

        private void paintOceanBorder(Component c, Graphics g, int x, int y,
                                      int w, int h) {
            AbstractButton button = (AbstractButton)c;
	    ButtonModel model = ((AbstractButton)c).getModel();

            g.translate(x, y);
            if (MetalUtils.isToolBarButton(button)) {
                if (model.isEnabled()) {
                    if (model.isPressed()) {
                        g.setColor(MetalLookAndFeel.getWhite());
                        g.fillRect(1, h - 1, w - 1, 1);
                        g.fillRect(w - 1, 1, 1, h - 1);
                        g.setColor(MetalLookAndFeel.getControlDarkShadow());
                        g.drawRect(0, 0, w - 2, h - 2);
                        g.fillRect(1, 1, w - 3, 1);
                    }
                    else if (model.isSelected() || model.isRollover()) {
                        g.setColor(MetalLookAndFeel.getWhite());
                        g.fillRect(1, h - 1, w - 1, 1);
                        g.fillRect(w - 1, 1, 1, h - 1);
                        g.setColor(MetalLookAndFeel.getControlDarkShadow());
                        g.drawRect(0, 0, w - 2, h - 2);
                    }
                    else {
                        g.setColor(MetalLookAndFeel.getWhite());
                        g.drawRect(1, 1, w - 2, h - 2);
                        g.setColor(UIManager.getColor(
                                "Button.toolBarBorderBackground"));
                        g.drawRect(0, 0, w - 2, h - 2);
                    }
                }
                else {
                   g.setColor(UIManager.getColor(
                           "Button.disabledToolBarBorderBackground"));
                   g.drawRect(0, 0, w - 2, h - 2); 
                }
            }
	    else if (model.isEnabled()) {
                boolean pressed = model.isPressed();
                boolean armed = model.isArmed();

                if ((c instanceof JButton) && ((JButton)c).isDefaultButton()) {
                    g.setColor(MetalLookAndFeel.getControlDarkShadow());
                    g.drawRect(0, 0, w - 1, h - 1);
                    g.drawRect(1, 1, w - 3, h - 3);
                }
                else if (pressed) {
                    g.setColor(MetalLookAndFeel.getControlDarkShadow());
                    g.fillRect(0, 0, w, 2);
                    g.fillRect(0, 2, 2, h - 2);
                    g.fillRect(w - 1, 1, 1, h - 1);
                    g.fillRect(1, h - 1, w - 2, 1);
                }
                else if (model.isRollover() && button.getClientProperty(
                               NO_BUTTON_ROLLOVER) == null) {
                    g.setColor(MetalLookAndFeel.getPrimaryControl());
                    g.drawRect(0, 0, w - 1, h - 1);
                    g.drawRect(2, 2, w - 5, h - 5);
                    g.setColor(MetalLookAndFeel.getControlDarkShadow());
                    g.drawRect(1, 1, w - 3, h - 3);
                }
                else {
                    g.setColor(MetalLookAndFeel.getControlDarkShadow());
                    g.drawRect(0, 0, w - 1, h - 1);
                }
            }
            else {
                g.setColor(MetalLookAndFeel.getInactiveControlTextColor());
                g.drawRect(0, 0, w - 1, h - 1);
                if ((c instanceof JButton) && ((JButton)c).isDefaultButton()) {
                    g.drawRect(1, 1, w - 3, h - 3);
                }
            }
        }

        public Insets getBorderInsets( Component c ) {
            return new Insets(3, 3, 3, 3);
        }
        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top = 3;
            newInsets.left = 3;
            newInsets.bottom = 3;
            newInsets.right = 3;
	    return newInsets;
	}
    }

    public static class InternalFrameBorder extends AbstractBorder implements UIResource {

        private static final int corner = 14;

        public void paintBorder(Component c, Graphics g, int x, int y,
			  int w, int h) {

            Color background;
            Color highlight;
            Color shadow;

            if (c instanceof JInternalFrame && ((JInternalFrame)c).isSelected()) {
                background = MetalLookAndFeel.getPrimaryControlDarkShadow();
	        highlight = MetalLookAndFeel.getPrimaryControlShadow();
	        shadow = MetalLookAndFeel.getPrimaryControlInfo();
            } else {
                background = MetalLookAndFeel.getControlDarkShadow();
	        highlight = MetalLookAndFeel.getControlShadow();
	        shadow = MetalLookAndFeel.getControlInfo();
            }

              g.setColor(background);
              // Draw outermost lines
              g.drawLine( 1, 0, w-2, 0);
              g.drawLine( 0, 1, 0, h-2);
              g.drawLine( w-1, 1, w-1, h-2);
              g.drawLine( 1, h-1, w-2, h-1);

              // Draw the bulk of the border
              for (int i = 1; i < 5; i++) {
	          g.drawRect(x+i,y+i,w-(i*2)-1, h-(i*2)-1);
              }

              if (c instanceof JInternalFrame &&
                               ((JInternalFrame)c).isResizable()) {
                  g.setColor(highlight);
                  // Draw the Long highlight lines
                  g.drawLine( corner+1, 3, w-corner, 3);
                  g.drawLine( 3, corner+1, 3, h-corner);
                  g.drawLine( w-2, corner+1, w-2, h-corner);
                  g.drawLine( corner+1, h-2, w-corner, h-2);

                  g.setColor(shadow);
                  // Draw the Long shadow lines
                  g.drawLine( corner, 2, w-corner-1, 2);
                  g.drawLine( 2, corner, 2, h-corner-1);
                  g.drawLine( w-3, corner, w-3, h-corner-1);
                  g.drawLine( corner, h-3, w-corner-1, h-3);
              }

          }

          public Insets getBorderInsets(Component c) {
              return new Insets(5, 5, 5, 5);
          }
          public Insets getBorderInsets(Component c, Insets newInsets) {
              newInsets.top = 5;
              newInsets.left = 5;
              newInsets.bottom = 5;
              newInsets.right = 5;
	      return newInsets;
	  }
    }

    /**
     * Border for a Frame.
     * @since 1.4
     */
    static class FrameBorder extends AbstractBorder implements UIResource {

        private static final int corner = 14;

        public void paintBorder(Component c, Graphics g, int x, int y,
            int w, int h) {

            Color background;
            Color highlight;
            Color shadow;

            Window window = SwingUtilities.getWindowAncestor(c);
            if (window != null && window.isActive()) {
                background = MetalLookAndFeel.getPrimaryControlDarkShadow();
                highlight = MetalLookAndFeel.getPrimaryControlShadow();
                shadow = MetalLookAndFeel.getPrimaryControlInfo();
            } else {
                background = MetalLookAndFeel.getControlDarkShadow();
                highlight = MetalLookAndFeel.getControlShadow();
                shadow = MetalLookAndFeel.getControlInfo();
            }

            g.setColor(background);
            // Draw outermost lines
            g.drawLine( x+1, y+0, x+w-2, y+0);
            g.drawLine( x+0, y+1, x+0, y +h-2);
            g.drawLine( x+w-1, y+1, x+w-1, y+h-2);
            g.drawLine( x+1, y+h-1, x+w-2, y+h-1);

            // Draw the bulk of the border
            for (int i = 1; i < 5; i++) {
                g.drawRect(x+i,y+i,w-(i*2)-1, h-(i*2)-1);
            }

            if ((window instanceof Frame) && ((Frame) window).isResizable()) {
                g.setColor(highlight);
                // Draw the Long highlight lines
                g.drawLine( corner+1, 3, w-corner, 3);
                g.drawLine( 3, corner+1, 3, h-corner);
                g.drawLine( w-2, corner+1, w-2, h-corner);
                g.drawLine( corner+1, h-2, w-corner, h-2);

                g.setColor(shadow);
                // Draw the Long shadow lines
                g.drawLine( corner, 2, w-corner-1, 2);
                g.drawLine( 2, corner, 2, h-corner-1);
                g.drawLine( w-3, corner, w-3, h-corner-1);
                g.drawLine( corner, h-3, w-corner-1, h-3);
            }

        }

        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5);
        }
    	  
        public Insets getBorderInsets(Component c, Insets newInsets) 
        {
            newInsets.top = 5;
            newInsets.left = 5;
            newInsets.bottom = 5;
            newInsets.right = 5;
            return newInsets;
        }
    }

    /**
     * Border for a Frame.
     * @since 1.4
     */
    static class DialogBorder extends AbstractBorder implements UIResource 
    {		
        private static final int corner = 14;

        protected Color getActiveBackground()
        {
            return MetalLookAndFeel.getPrimaryControlDarkShadow();
        }

        protected Color getActiveHighlight()
        {
            return MetalLookAndFeel.getPrimaryControlShadow();
        }

        protected Color getActiveShadow()
        {
            return MetalLookAndFeel.getPrimaryControlInfo();
        }

        protected Color getInactiveBackground()
        {
            return MetalLookAndFeel.getControlDarkShadow();
        }

        protected Color getInactiveHighlight()
        {
            return MetalLookAndFeel.getControlShadow();
        }

        protected Color getInactiveShadow()
        {
            return MetalLookAndFeel.getControlInfo();
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) 
        {
            Color background;
            Color highlight;
            Color shadow;

            Window window = SwingUtilities.getWindowAncestor(c);
            if (window != null && window.isActive()) {
                background = getActiveBackground();
                highlight = getActiveHighlight();
                shadow = getActiveShadow();
            } else {
                background = getInactiveBackground();
                highlight = getInactiveHighlight();
                shadow = getInactiveShadow();
            }

            g.setColor(background);
            // Draw outermost lines
            g.drawLine( x + 1, y + 0, x + w-2, y + 0);
            g.drawLine( x + 0, y + 1, x + 0, y + h - 2);
            g.drawLine( x + w - 1, y + 1, x + w - 1, y + h - 2);
            g.drawLine( x + 1, y + h - 1, x + w - 2, y + h - 1);

            // Draw the bulk of the border
            for (int i = 1; i < 5; i++) {
                g.drawRect(x+i,y+i,w-(i*2)-1, h-(i*2)-1);
            }


            if ((window instanceof Dialog) && ((Dialog) window).isResizable()) {
                g.setColor(highlight);
                // Draw the Long highlight lines
                g.drawLine( corner+1, 3, w-corner, 3);
                g.drawLine( 3, corner+1, 3, h-corner);
                g.drawLine( w-2, corner+1, w-2, h-corner);
                g.drawLine( corner+1, h-2, w-corner, h-2);

                g.setColor(shadow);
                // Draw the Long shadow lines
                g.drawLine( corner, 2, w-corner-1, 2);
                g.drawLine( 2, corner, 2, h-corner-1);
                g.drawLine( w-3, corner, w-3, h-corner-1);
                g.drawLine( corner, h-3, w-corner-1, h-3);
            }
            
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5);
        }

        public Insets getBorderInsets(Component c, Insets newInsets) 
        {
            newInsets.top = 5;
            newInsets.left = 5;
            newInsets.bottom = 5;
            newInsets.right = 5;
            return newInsets;
        }
    }

    /**
     * Border for an Error Dialog.
     * @since 1.4
     */
    static class ErrorDialogBorder extends DialogBorder implements UIResource
    {
        protected Color getActiveBackground() {
            return UIManager.getColor("OptionPane.errorDialog.border.background");
        }
    }
    

    /**
     * Border for a QuestionDialog.  Also used for a JFileChooser and a 
     * JColorChooser..
     * @since 1.4
     */
    static class QuestionDialogBorder extends DialogBorder implements UIResource
    {
        protected Color getActiveBackground() {
            return UIManager.getColor("OptionPane.questionDialog.border.background");
        }
    }
    

    /**
     * Border for a Warning Dialog.
     * @since 1.4
     */
    static class WarningDialogBorder extends DialogBorder implements UIResource
    {
        protected Color getActiveBackground() {
            return UIManager.getColor("OptionPane.warningDialog.border.background");
        }
    }
    

    /**
     * Border for a Palette.
     * @since 1.3
     */
    public static class PaletteBorder extends AbstractBorder implements UIResource {
        int titleHeight = 0;

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {  

	    g.translate(x,y);  
	    g.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
	    g.drawLine(0, 1, 0, h-2);
	    g.drawLine(1, h-1, w-2, h-1);
	    g.drawLine(w-1,  1, w-1, h-2);
	    g.drawLine( 1, 0, w-2, 0);
	    g.drawRect(1,1, w-3, h-3);
	    g.translate(-x,-y);
      
	}

        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top = 1;
            newInsets.left = 1;
            newInsets.bottom = 1;
            newInsets.right = 1;
	    return newInsets;
	}
    }

    public static class OptionDialogBorder extends AbstractBorder implements UIResource {
        int titleHeight = 0;

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {  

	    g.translate(x,y);  

            int messageType = JOptionPane.PLAIN_MESSAGE;
            if (c instanceof JInternalFrame) {
                Object obj = ((JInternalFrame) c).getClientProperty(
                              "JInternalFrame.messageType"); 
                if (obj != null && (obj instanceof Integer)) {
                    messageType = ((Integer) obj).intValue();
                }
            }

            Color borderColor;

	    switch (messageType) {
            case(JOptionPane.ERROR_MESSAGE):
                borderColor = UIManager.getColor(
                    "OptionPane.errorDialog.border.background");
                break;
            case(JOptionPane.QUESTION_MESSAGE):
                borderColor = UIManager.getColor(
                    "OptionPane.questionDialog.border.background");
                break;
            case(JOptionPane.WARNING_MESSAGE):
                borderColor = UIManager.getColor(
                    "OptionPane.warningDialog.border.background");
                break;
            case(JOptionPane.INFORMATION_MESSAGE):
            case(JOptionPane.PLAIN_MESSAGE):
            default:
                borderColor = MetalLookAndFeel.getPrimaryControlDarkShadow();
                break;
	    }

	    g.setColor(borderColor);

              // Draw outermost lines
              g.drawLine( 1, 0, w-2, 0);
              g.drawLine( 0, 1, 0, h-2);
              g.drawLine( w-1, 1, w-1, h-2);
              g.drawLine( 1, h-1, w-2, h-1);

              // Draw the bulk of the border
              for (int i = 1; i < 3; i++) {
	          g.drawRect(i, i, w-(i*2)-1, h-(i*2)-1);
              }

	    g.translate(-x,-y);
      
	}

        public Insets getBorderInsets(Component c) {
            return new Insets(3, 3, 3, 3);
        }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top = 3;
            newInsets.left = 3;
            newInsets.bottom = 3;
            newInsets.right = 3;
	    return newInsets;
	}
    }


    public static class MenuBarBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets( 1, 0, 1, 0 );

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {
	    g.translate( x, y );

            if (MetalLookAndFeel.usingOcean()) {
                // Only paint a border if we're not next to a horizontal
                // toolbar
                if (!MetalToolBarUI.doesMenuBarBorderToolBar((JMenuBar)c)) {
                    g.setColor(MetalLookAndFeel.getControl());
                    g.drawLine(0, h - 2, w, h - 2);
                    g.setColor(UIManager.getColor("MenuBar.borderColor"));
                    g.drawLine(0, h - 1, w, h - 1);
                }
            }
            else {
                g.setColor( MetalLookAndFeel.getControlShadow() );
                g.drawLine( 0, h-1, w, h-1 );
            }

	    g.translate( -x, -y );

        }

        public Insets getBorderInsets( Component c ) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            if (MetalLookAndFeel.usingOcean()) {
                newInsets.set(0, 0, 2, 0);
            }
            else {
                newInsets.top = 1;
                newInsets.left = 0;
                newInsets.bottom = 1;
                newInsets.right = 0;
            }
	    return newInsets;
	}
    }

    public static class MenuItemBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets( 2, 2, 2, 2 );

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {
            JMenuItem b = (JMenuItem) c;
	    ButtonModel model = b.getModel();

	    g.translate( x, y );

	    if ( c.getParent() instanceof JMenuBar ) {
	        if ( model.isArmed() || model.isSelected() ) {
	            g.setColor( MetalLookAndFeel.getControlDarkShadow() );
	            g.drawLine( 0, 0, w - 2, 0 );
	            g.drawLine( 0, 0, 0, h - 1 );
	            g.drawLine( w - 2, 2, w - 2, h - 1 );

	            g.setColor( MetalLookAndFeel.getPrimaryControlHighlight() );
	            g.drawLine( w - 1, 1, w - 1, h - 1 );

	            g.setColor( MetalLookAndFeel.getMenuBackground() );
	            g.drawLine( w - 1, 0, w - 1, 0 );
	        }
	    } else {
	        if (  model.isArmed() || ( c instanceof JMenu && model.isSelected() ) ) {
	            g.setColor( MetalLookAndFeel.getPrimaryControlDarkShadow() );
	            g.drawLine( 0, 0, w - 1, 0 );

	            g.setColor( MetalLookAndFeel.getPrimaryControlHighlight() );
	            g.drawLine( 0, h - 1, w - 1, h - 1 );
	        } else {
	            g.setColor( MetalLookAndFeel.getPrimaryControlHighlight() );
	            g.drawLine( 0, 0, 0, h - 1 );
	        }
	    }

	    g.translate( -x, -y );
        }

        public Insets getBorderInsets( Component c ) {
            return new Insets(2, 2, 2, 2);
        }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top = 2;
            newInsets.left = 2;
            newInsets.bottom = 2;
            newInsets.right = 2;
	    return newInsets;
	}
    }

    public static class PopupMenuBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets( 3, 1, 2, 1 );

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {
	    g.translate( x, y );

            g.setColor( MetalLookAndFeel.getPrimaryControlDarkShadow() );
	    g.drawRect( 0, 0, w - 1, h - 1 );

            g.setColor( MetalLookAndFeel.getPrimaryControlHighlight() );
	    g.drawLine( 1, 1, w - 2, 1 );
	    g.drawLine( 1, 2, 1, 2 );
	    g.drawLine( 1, h - 2, 1, h - 2 );

	    g.translate( -x, -y );

        }

        public Insets getBorderInsets( Component c ) {
             return new Insets(3, 1, 2, 1);
        }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top = 3;
            newInsets.left = 1;
            newInsets.bottom = 2;
            newInsets.right = 1;
	    return newInsets;
	}
    }


    public static class RolloverButtonBorder extends ButtonBorder {

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();

            if ( model.isRollover() && !( model.isPressed() && !model.isArmed() ) ) {
	        super.paintBorder( c, g, x, y, w, h );
            }
        }

    }

    /**
     * A border which is like a Margin border but it will only honor the margin
     * if the margin has been explicitly set by the developer.
     * 
     * Note: This is identical to the package private class
     * BasicBorders.RolloverMarginBorder and should probably be consolidated.
     */
    static class RolloverMarginBorder extends EmptyBorder {
	
	public RolloverMarginBorder() {
	    super(3,3,3,3); // hardcoded margin for JLF requirements.
	}

	public Insets getBorderInsets(Component c) {
	    return getBorderInsets(c, new Insets(0,0,0,0));
	}

	public Insets getBorderInsets(Component c, Insets insets) {
	    Insets margin = null;

	    if (c instanceof AbstractButton) {
		margin = ((AbstractButton)c).getMargin();
	    }
	    if (margin == null || margin instanceof UIResource) {
		// default margin so replace
		insets.left = left;
		insets.top = top;
		insets.right = right;
		insets.bottom = bottom;
	    } else {
		// Margin which has been explicitly set by the user.
		insets.left = margin.left;
		insets.top = margin.top;
		insets.right = margin.right;
		insets.bottom = margin.bottom;
	    }
	    return insets;
	}
    }

    public static class ToolBarBorder extends AbstractBorder implements UIResource, SwingConstants
    {
        protected MetalBumps bumps = new MetalBumps( 10, 10,
				      MetalLookAndFeel.getControlHighlight(),
				      MetalLookAndFeel.getControlDarkShadow(),
                                     UIManager.getColor("ToolBar.background"));

        public void paintBorder( Component c, Graphics g, int x, int y, int w, int h )
	{
	    g.translate( x, y );

	    if ( ((JToolBar) c).isFloatable() )
	    {
	        if ( ((JToolBar) c).getOrientation() == HORIZONTAL )
		{
                    int shift = MetalLookAndFeel.usingOcean() ? -1 : 0;
		    bumps.setBumpArea( 10, c.getSize().height - 4 );
                    if( MetalUtils.isLeftToRight(c) ) {
                        bumps.paintIcon( c, g, 2, 2 + shift );
                    } else {
                        bumps.paintIcon( c, g, c.getBounds().width-12,
                                         2 + shift );
                    }
	        }
		else // vertical
		{
		    bumps.setBumpArea( c.getSize().width - 4, 10 );
		    bumps.paintIcon( c, g, 2, 2 );
	        }

	    }

            if (((JToolBar) c).getOrientation() == HORIZONTAL && 
                               MetalLookAndFeel.usingOcean()) {
                g.setColor(MetalLookAndFeel.getControl());
                g.drawLine(0, h - 2, w, h - 2);
                g.setColor(UIManager.getColor("ToolBar.borderColor"));
                g.drawLine(0, h - 1, w, h - 1);
            }

	    g.translate( -x, -y );
        }

        public Insets getBorderInsets( Component c ) {
	    return getBorderInsets(c, new Insets(0,0,0,0));
	}

	public Insets getBorderInsets(Component c, Insets newInsets) {
            if (MetalLookAndFeel.usingOcean()) {
                newInsets.set(1, 2, 3, 2);
            }
            else {
                newInsets.top = newInsets.left = newInsets.bottom = newInsets.right = 2;
            }

	    if ( ((JToolBar) c).isFloatable() ) {
	        if ( ((JToolBar) c).getOrientation() == HORIZONTAL ) {
		    if (c.getComponentOrientation().isLeftToRight()) {
	        	newInsets.left = 16;
		    } else {
	        	newInsets.right = 16;
		    }
	        } else {// vertical
		    newInsets.top = 16;
	        }
	    }

	    Insets margin = ((JToolBar) c).getMargin();

	    if ( margin != null ) {
	        newInsets.left   += margin.left;
	        newInsets.top    += margin.top;
	        newInsets.right  += margin.right;
	        newInsets.bottom += margin.bottom;
	    }

            return newInsets;
        }
    }

    private static Border buttonBorder;

    /**
     * Returns a border instance for a JButton
     * @since 1.3
     */
    public static Border getButtonBorder() {
	if (buttonBorder == null) {
	    buttonBorder = new BorderUIResource.CompoundBorderUIResource(
						   new MetalBorders.ButtonBorder(),
						   new BasicBorders.MarginBorder());
	}
	return buttonBorder;
    }

    private static Border textBorder;

    /**
     * Returns a border instance for a text component
     * @since 1.3
     */
    public static Border getTextBorder() {
	if (textBorder == null) {
	    textBorder = new BorderUIResource.CompoundBorderUIResource(
						   new MetalBorders.Flush3DBorder(),
						   new BasicBorders.MarginBorder());
	}
	return textBorder;
    }

    private static Border textFieldBorder;

    /**
     * Returns a border instance for a JTextField
     * @since 1.3
     */
    public static Border getTextFieldBorder() {
	if (textFieldBorder == null) {
	    textFieldBorder = new BorderUIResource.CompoundBorderUIResource(
						   new MetalBorders.TextFieldBorder(),
						   new BasicBorders.MarginBorder());
	}
	return textFieldBorder;
    }

    public static class TextFieldBorder extends Flush3DBorder {

        public void paintBorder(Component c, Graphics g, int x, int y,
				int w, int h) {

	  if (!(c instanceof JTextComponent)) {
	        // special case for non-text components (bug ID 4144840)
	        if (c.isEnabled()) {
		    MetalUtils.drawFlush3DBorder(g, x, y, w, h);
		} else {
		    MetalUtils.drawDisabledBorder(g, x, y, w, h);
		}
		return;
	    }

	    if (c.isEnabled() && ((JTextComponent)c).isEditable()) {
	        MetalUtils.drawFlush3DBorder(g, x, y, w, h);
	    } else {
	        MetalUtils.drawDisabledBorder(g, x, y, w, h);
	    }

	}
    }

    public static class ScrollPaneBorder extends AbstractBorder implements UIResource {

        public void paintBorder(Component c, Graphics g, int x, int y,
			  int w, int h) {

            JScrollPane scroll = (JScrollPane)c;
            JComponent colHeader = scroll.getColumnHeader();
            int colHeaderHeight = 0;
            if (colHeader != null)
               colHeaderHeight = colHeader.getHeight();

            JComponent rowHeader = scroll.getRowHeader();
            int rowHeaderWidth = 0;
            if (rowHeader != null)
               rowHeaderWidth = rowHeader.getWidth();


            g.translate( x, y);

            g.setColor( MetalLookAndFeel.getControlDarkShadow() );
            g.drawRect( 0, 0, w-2, h-2 );
            g.setColor( MetalLookAndFeel.getControlHighlight() );

            g.drawLine( w-1, 1, w-1, h-1);
            g.drawLine( 1, h-1, w-1, h-1);

            g.setColor( MetalLookAndFeel.getControl() );
            g.drawLine( w-2, 2+colHeaderHeight, w-2, 2+colHeaderHeight );
            g.drawLine( 1+rowHeaderWidth, h-2, 1+rowHeaderWidth, h-2 );

            g.translate( -x, -y);

        }

        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 2, 2);
        }
    }

    private static Border toggleButtonBorder;

    /**
     * Returns a border instance for a JToggleButton
     * @since 1.3
     */
    public static Border getToggleButtonBorder() {
	if (toggleButtonBorder == null) {
	    toggleButtonBorder = new BorderUIResource.CompoundBorderUIResource(
						   new MetalBorders.ToggleButtonBorder(),
						   new BasicBorders.MarginBorder());
	}
	return toggleButtonBorder;
    }

    /**
     * @since 1.3
     */
    public static class ToggleButtonBorder extends ButtonBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
	    AbstractButton button = (AbstractButton)c;
	    ButtonModel model = button.getModel();
            if (MetalLookAndFeel.usingOcean()) {
                if(model.isArmed()) {
                    super.paintBorder(c, g, x, y, w, h);
                }
                else {
                    MetalUtils.drawPressed3DBorder( g, x, y, w + 1, h + 1 );
                }
                return;
            }
	    if (! c.isEnabled() ) {
	        MetalUtils.drawDisabledBorder( g, x, y, w-1, h-1 );
	    } else {
	        if ( model.isPressed() && model.isArmed() ) {
                   MetalUtils.drawPressed3DBorder( g, x, y, w, h );
		} else if ( model.isSelected() ) {
		    MetalUtils.drawDark3DBorder( g, x, y, w, h );
                } else {
		    MetalUtils.drawFlush3DBorder( g, x, y, w, h );
                }
	    }
	}
    }

    /**
     * Border for a Table Header
     * @since 1.3
     */
    public static class TableHeaderBorder extends javax.swing.border.AbstractBorder {
        protected Insets editorBorderInsets = new Insets( 2, 2, 2, 0 );

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
	    g.translate( x, y );
	    
	    g.setColor( MetalLookAndFeel.getControlDarkShadow() );
	    g.drawLine( w-1, 0, w-1, h-1 );
	    g.drawLine( 1, h-1, w-1, h-1 );
	    g.setColor( MetalLookAndFeel.getControlHighlight() );
	    g.drawLine( 0, 0, w-2, 0 );
	    g.drawLine( 0, 0, 0, h-2 );

	    g.translate( -x, -y );
	}

        public Insets getBorderInsets( Component c ) {
	    return editorBorderInsets;
	}
    }

    /**
     * Returns a border instance for a Desktop Icon
     * @since 1.3
     */
    public static Border getDesktopIconBorder() {
	return new BorderUIResource.CompoundBorderUIResource(
                                          new LineBorder(MetalLookAndFeel.getControlDarkShadow(), 1),
                                          new MatteBorder (2,2,1,2, MetalLookAndFeel.getControl()));
    }

    static Border getToolBarRolloverBorder() {
        if (MetalLookAndFeel.usingOcean()) {
            return new CompoundBorder(
                new MetalBorders.ButtonBorder(), 
                new MetalBorders.RolloverMarginBorder());
        }
	return new CompoundBorder(new MetalBorders.RolloverButtonBorder(), 
				  new MetalBorders.RolloverMarginBorder());
    }

    static Border getToolBarNonrolloverBorder() {
        if (MetalLookAndFeel.usingOcean()) {
            new CompoundBorder(
                new MetalBorders.ButtonBorder(), 
                new MetalBorders.RolloverMarginBorder());
        }
	return new CompoundBorder(new MetalBorders.ButtonBorder(),
				  new MetalBorders.RolloverMarginBorder());
    }
}

