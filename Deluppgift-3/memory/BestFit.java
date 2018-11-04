package memory;

import java.util.HashMap;
import java.util.Map;

/**
 * This memory model allocates memory cells based on the best-fit method.
 * 
 * @author "Johan Holmberg, Malmö university"
 * Barry Al-Jawari
 * @since 1.0
 */
public class BestFit extends Memory {
	private Map<Pointer, Integer> StartSizePosition;

	/**
	 * Initializes an instance of a best fit-based memory.
	 * 
	 * @param size
	 *            The number of cells.
	 */
	public BestFit(int size) {
		super(size);
		this.StartSizePosition = new HashMap<>();
	}

	/**
	 * Allocates a number of memory cells.
	 * the number of cells to allocate.
	 * @param size
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		int minSize = Integer.MAX_VALUE;
		int minStartValue = Integer.MAX_VALUE;
		int x = 0;

		while (x < cells.length) {
			if (cells[x] == 0) { // Checkar om positionerna är tillängliga.
				int z = x;
				int count = 1; // Count representerar antalet tillängliga platser i rad.
				while (cells[z] == 0 && z < (cells.length - 1)) {
					z++;
					count++;
				}
				if (count >= size && count <= minSize) {
					// Ett tillräckligt stort utrymme har funnits.
					minStartValue = x; // Här så vill vi sätta in värdena.
					minSize = count;
					x = z;
				} else {
					x = z; // här är den nästa positionen där vi kontrollerar tillängliheten.
				}
			}
			x++;
		}

		Pointer pointer = new Pointer(this);
		pointer.pointAt(minStartValue);
		StartSizePosition.put(pointer, size);
		return pointer;
	}

	/**
	 * Releases a number of data cells.
	 * The pointer to release.
	 * @param p            
	 */
	@Override
	public void release(Pointer p) {
		int count = p.pointsAt() + StartSizePosition.get(p);
		for (int i = p.pointsAt(); i < count; i++) {
			cells[i] = 0;
			StartSizePosition.remove(p);
		}
	}

	/**
	 * Prints a simple model of the memory.
	 * 
	 *  0 - 110 - Allocated 
	 *  111 - 150 - Free 
	 *  151 - 999 - Allocated 
	 *  1000 - 1024 - Free
	 */
	@Override
	public void printLayout() {		
		boolean Fylld = false;

		for (int i = 0; i < cells.length; i = i) {
			if (cells[i] == 0) {
				Fylld = false;
				for (int y = i; Fylld == false; y++) {
					try {
						if (cells[y] == 0) {
							Fylld = false;
						} else {
							Fylld = true;
							System.out.println( i + " - " + (y - 1) + " - Free");
							i = y;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						Fylld = true;
						System.out.println( i + " - " + (y - 1) + " - Free");
						i = y;
					}
				}
			} else {
				Fylld = true;
				for (int y = i; Fylld == true; y++) {
					try {
						if (cells[y] > 0) {
							Fylld = true;
						} else {
							Fylld = false;
							System.out.println( i + " - " + (y - 1) + " - Allocated");
							i = y;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						Fylld = false;
						System.out.println( i + " - " + (y - 1) + " - Allocated");
						i = y;
					}
				}
			}
		}	
	}
}