package a8;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class FramePuzzleWidget extends JPanel implements MouseListener, KeyListener {
	
	private PictureView[][] picture_view_grid;
	private JPanel grid_panel;
	private PictureView all_white;
	
	public FramePuzzleWidget(Picture picture) {
		// Creates an array to store the PictureView objects
		picture_view_grid = new PictureView[5][5];
		int tile_w = picture.getWidth()/5;
		int tile_h = picture.getHeight()/5;
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				Picture sub_picture = new SubPictureImpl(picture,tile_w*i,tile_h*j,tile_w,tile_h);
				picture_view_grid[j][i] = new PictureView(sub_picture.createObservable());
				picture_view_grid[j][i].addMouseListener(this);
			}
		}
		// Sets the bottow right element of the grid to solid white
		Picture all_white_temp = new PictureImpl(tile_w,tile_h);
		// Default pixel is white, so we don't need to set any of the pixels
		all_white = new PictureView(all_white_temp.createObservable());
		picture_view_grid[4][4] = all_white;
		
		create_grid();
		
		add(grid_panel);
		
		this.setFocusable(true);
		this.grabFocus();
		this.addKeyListener(this);
	}
	
	private void create_grid() {
		// Puts the PictureView elements from picture_view_grid into a JPanel grid
		grid_panel = new JPanel();
		grid_panel.setLayout(new GridLayout(5,5));
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				grid_panel.add(picture_view_grid[i][j]);
			}
		}
	}
	
	private Coordinate find(PictureView pv) {
	/* Returns a Coordinate representing the row and column, respectively, 
	 * of a PictureView object in picture_view_grid */
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				if (picture_view_grid[i][j] == pv) {
					return (new Coordinate(i,j));
				}
			}
		}
		throw new RuntimeException();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// old_row and old_col represent current row and column of white tile
		int old_row = find(all_white).getX();
		int old_col = find(all_white).getY();
		/* new_row and new_col represent the position where white tile will be moved;
		 * will be changed based on what key is pushed (see switch statement) */
		int new_row = old_row;
		int new_col = old_col;
		
	    int keyCode = e.getKeyCode();
	    switch(keyCode) { 
	        case KeyEvent.VK_UP:
	        	if (old_row > 0) {
	        		new_row = old_row-1;
	        	}
	            break;
	        case KeyEvent.VK_DOWN:
	        	if (old_row < 4) {
	        		new_row = old_row+1;
	        	}
	            break;
	        case KeyEvent.VK_LEFT:
	        	if (old_col > 0) {
	        		new_col = old_col-1;
	        	}
	            break;
	        case KeyEvent.VK_RIGHT :
	        	if (old_col < 4) {
	        		new_col = old_col+1;
	        	}
	            break;
	    }
	    // Moves tile in target location to the current location of the white tile
	    picture_view_grid[old_row][old_col] = picture_view_grid[new_row][new_col];
	    
	    // Moves the white tile to its target location
	    picture_view_grid[new_row][new_col] = all_white;
	    
	    removeAll();
	    create_grid();
		add(grid_panel);
		revalidate();
	} 



	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int old_row = find(all_white).getX();
		int old_col = find(all_white).getY();
		PictureView pv = (PictureView) arg0.getSource();
		int new_row = find(pv).getX();
		int new_col = find(pv).getY();
		
		// Moves portions to fill blank space
		if (old_row == new_row) {
			if (old_col < new_col) {
				for (int i=old_col; i<new_col; i++) {
					picture_view_grid[old_row][i] = picture_view_grid[old_row][i+1];
				}
			} else if (old_col > new_col) {
				for (int i=old_col; i>new_col; i--) {
					picture_view_grid[old_row][i] = picture_view_grid[old_row][i-1];
				}
			}
			// Moves white tile to target location
			picture_view_grid[new_row][new_col] = all_white;
		} else if (old_col == new_col) {
			if (old_row < new_row) {
				for (int i=old_row; i<new_row; i++) {
					picture_view_grid[i][old_col] = picture_view_grid[i+1][old_col];
				}
			} else if (old_row > new_row) {
				for (int i=old_row; i>new_row; i--) {
					picture_view_grid[i][old_col] = picture_view_grid[i-1][old_col];
				}
			}
			// Moves white tile to target location
			picture_view_grid[new_row][new_col] = all_white;
		}
		
		
		removeAll();
	    create_grid();
		add(grid_panel);
		revalidate();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
