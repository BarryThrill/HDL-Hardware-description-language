package memory;

import java.util.HashMap;
import java.util.Map;

/**
 * This memory model allocates memory cells based on the first-fit method.
 * 
 * @author "Johan Holmberg, Malmö university"
 * Barry Al-Jawari
 * @since 1.0
 */
public class FirstFit extends Memory {

	private int nextPosition;
	private Map<Integer, Integer> startFinishPositions;

	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size
	 *            The number of cells.
	 */
	public FirstFit(int size) {
		super(size);
		this.nextPosition = 0;
		this.startFinishPositions = new HashMap<Integer, Integer>();
	}

	/**
	 * Allocates a number of memory cells.
	 * 
	 * @param size
	 *            the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {

		for (int i = this.nextPosition; i < this.cells.length; i++) {
			if (this.cells[i] == 0) {	// tom cell funnen.

				boolean Fylld = true;
				for (int x = i; x <= size + 1; x++) {	// Här checkar man ifall de finns plats i allocate
					if (this.cells[i] != 0) { // fanns ej plats så vi måste titta vidare tills vi finner de
						while (this.cells[x] != 0) {
							x = x + this.startFinishPositions.get(x);
						}
						i = x - 1; // l�gger i till n�sta lediga utrymme.
						Fylld = false;
						break;
					}
				}
				if (Fylld) {
					this.nextPosition = i + size;
					Pointer p = new Pointer(i, this);
					this.startFinishPositions.put(i, size);
					return p;
				} else {
					continue;
				}
			}
		}
		return null;
	}

	/**
	 * Releases a number of data cells.
	 * The pointer to release.
	 * @param p
	 */
	@Override
	public void release(Pointer p) {
		int startPosition = p.pointsAt();
		int length = this.startFinishPositions.get(startPosition).intValue();
		for (int i = 0; i < length; i++) {
			this.cells[startPosition + i] = 0;
		}
		this.startFinishPositions.remove(startPosition);
		this.nextPosition = startPosition;
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

		for (int i = 0; i < cells.length; i++) {
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