/**
* 
*/
package ar.com.wuik.swing.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class WGridLayout implements LayoutManager {


	private int columns;
	private int gap;

	public WGridLayout(int columns, int gap) {
		this.columns = columns;
		this.gap = gap;
	}

	private int vgap;
	private int minWidth = 0, minHeight = 0;

	/* Required by LayoutManager. */
	public void addLayoutComponent( String name, Component comp ) {
		System.out.println( name );
	}

	/* Required by LayoutManager. */
	public void removeLayoutComponent( Component comp ) {
		System.out.println( comp.toString() );
	}

	/* Required by LayoutManager. */
	public Dimension preferredLayoutSize( Container parent ) {
		Dimension dim = new Dimension( 0, 0 );
		int nComps = parent.getComponentCount();
		int w = 0;
		int h = 0;
		for ( int i = 0; i < nComps; i++ ) {
			Component c = parent.getComponent( i );
			if ( c.isVisible() ) {
				Dimension d = c.getSize();
				if ( ( ( i + 1 ) % columns ) == 0 ) {
					break;
				} else {
					w += d.width + gap;
				}
			}
		}

		int rows = 1;
		int height = 0;
		for ( int i = 0; i < nComps; i++ ) {
			Component c = parent.getComponent( i );
			if ( c.isVisible() ) {
				Dimension d = c.getSize();
				height = d.height;
				if ( ( ( i + 1 ) % columns ) == 0 ) {
					rows++;
				}
			}
		}
		h += rows * ( height + gap );

		// Always add the container's insets!
		Insets insets = parent.getInsets();
		dim.width = w + insets.left + insets.right;
		dim.height = h + insets.top + insets.bottom;

		return dim;
	}

	/* Required by LayoutManager. */
	public Dimension minimumLayoutSize( Container parent ) {
		Dimension dim = new Dimension( 0, 0 );

		// Always add the container's insets!
		Insets insets = parent.getInsets();
		dim.width = minWidth + insets.left + insets.right;
		dim.height = minHeight + insets.top + insets.bottom;

		return dim;
	}

	/* Required by LayoutManager. */
	/*
	 * This is called when the panel is first displayed, and every time its size
	 * changes. Note: You CAN'T assume preferredLayoutSize or minimumLayoutSize
	 * will be called -- in the case of applets, at least, they probably won't
	 * be.
	 */
	public void layoutContainer( Container parent ) {
		int nComps = parent.getComponentCount();
		int x = 0;
		int y = 0;
		for ( int i = 0; i < nComps; i++ ) {
			Component c = parent.getComponent( i );
			if ( c.isVisible() ) {
				Dimension d = c.getSize();
				c.setBounds( x + gap, y + gap, d.width, d.height );
				if ( ( ( i + 1 ) % columns ) == 0 ) {
					x = 0;
					y += d.height + gap;
				} else {
					x += d.width + gap;
				}
			}
		}
	}

	public String toString() {
		String str = "";
		return getClass().getName() + "[vgap=" + vgap + str + "]";
	}

}
