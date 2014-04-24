import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * The Sorting class implements various sorting methods.
 * 
 * @author Sean Collins
 */
public class Sorting implements SortingInterface {

	@Override
	public <T extends Comparable<T>> void bubblesort(T[] arr) {
		T temp;
		for (int x = 0; x < (arr.length - 1) && !sorted(arr); x++) {
			for (int i = 0; i < (arr.length - 1); i++) {
				if (arr[i].compareTo(arr[i + 1]) > 0) {
					// Current is greater than next, swap them
					temp = arr[i];
					arr[i] = arr[i + 1];
					arr[i + 1] = temp;
				}
			}
		}
	}

	@Override
	public <T extends Comparable<T>> void insertionsort(T[] arr) {
		for (int x = 1; x < arr.length && !sorted(arr); x++) {
			if (arr[x].compareTo(arr[x - 1]) < 0) {
				int i = (x - 1);
				while (i != 0 && arr[i].compareTo(arr[x]) > 0) {
					i--;
				}
				
				if (arr[x].compareTo(arr[i]) < 0) {
					// Insert arr[x] at index i and shift array up
					insertAndShift(arr, arr[x], i, x);
				} else {
					// Insert arr[x] at index (i + 1) and shift array up
					insertAndShift(arr, arr[x], (i + 1), x);
				}
			}	
		}
	}
	
	/**
	 * A private helper method which inserts an element at a given index and shifts
	 * all the elements in front of the index in the array up by one.
	 * 
	 * @param arr The array to insert into
	 * @param insert The element to insert
	 * @param position The position of the array to insert the element at
	 * @param max The max index to stop shifting at
	 */
	private <T extends Comparable<T>> void insertAndShift(T[] arr, T insert, int position, int max) {
		if (position <= max) {
			T temp;
			temp = arr[position];
			arr[position] = insert;
			insertAndShift(arr, temp, (position + 1), max);
		}
	}

	@Override
	public <T extends Comparable<T>> void selectionsort(T[] arr) {
		T temp, currentSmallest = null;
		int index = -1;
		
		for (int x = 0; x < (arr.length - 1) && !sorted(arr); x++) {
			// Choose one element 
			for (int i = (x + 1); i < arr.length; i++) {
				// ...and compare to all other elements in array
				if (currentSmallest == null 
						|| arr[i].compareTo(currentSmallest) < 0) {
					currentSmallest = arr[i];
					index = i;
				}
			}
			
			if (arr[x].compareTo(currentSmallest) > 0) {
				// Current smallest is smaller than current element, swap them
				temp = arr[x];
				arr[x] = currentSmallest;
				arr[index] = temp;
			}
			
			currentSmallest = null;
		}
	}

	@Override
	public <T extends Comparable<T>> void quicksort(T[] arr, Random r) {
		int pivotIndex;
		T temp;
		
		if (!sorted(arr)) {
			 pivotIndex = r.nextInt(arr.length - 1);
			 int leftIndex = 0;
			 int rightIndex = (arr.length - 1);
			 
			 while (leftIndex < rightIndex) {
				 while (arr[leftIndex].compareTo(arr[pivotIndex]) < 0) {
					 leftIndex++;
				 }
				 
				 while (arr[rightIndex].compareTo(arr[pivotIndex]) > 0) {
					 rightIndex--;
				 }
				 
				 if (leftIndex < rightIndex) {
					 // Swap values
					 temp = arr[leftIndex];
					 arr[leftIndex] = arr[rightIndex];
					 arr[rightIndex] = temp;
					 leftIndex++;
					 rightIndex--;
				 }
			 }
			 
			 quicksort(arr, r);
		 }
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Comparable<T>> T[] mergesort(T[] arr) {
		T[][] splits = (T[][]) new Comparable[arr.length][1];
		T[] temp;
		
		// Split original array into individual arrays
		for (int i = 0; i < arr.length; i++) {
			T[] currentIndex = (T[]) new Comparable[1];
			currentIndex[0] = arr[i];
			splits[i] = currentIndex;
		}
		
		// Compare each individual array to each other
		for (int x = 0; x < (splits.length - 1); x++) {
			for (int y = 0; y < (splits.length - 1) && splits[y + 1] != null; y++) {
				if (splits[y][(splits[y].length - 1)].compareTo(splits[y + 1][0]) > 0) {
					// Next array is less than current array, switch them before combining
					temp = splits[y];
					splits[y] = splits[y + 1];
					splits[y + 1] = temp;
				}

				T[] newArray = (T[]) new Comparable[splits[y].length + splits[y + 1].length];
				int newArrayIndex = 0;
				int firstIndex = 0;
				int secondIndex = 0;
				
				while (firstIndex < splits[y].length && secondIndex < splits[y + 1].length) {
					if (splits[y][firstIndex].compareTo(splits[y + 1][secondIndex]) < 0) {
						newArray[newArrayIndex] = splits[y][firstIndex];
						firstIndex++;
					} else {
						newArray[newArrayIndex] = splits[y + 1][secondIndex];
						secondIndex++;
					}
					
					newArrayIndex++;
				}
				
				if (firstIndex < splits[y].length) {
					while (newArrayIndex < newArray.length) {
						newArray[newArrayIndex] = splits[y][firstIndex];
						firstIndex++;
						newArrayIndex++;
					}
				} else if (secondIndex < splits[y + 1].length) {
					while (newArrayIndex < newArray.length) {
						newArray[newArrayIndex] = splits[y + 1][secondIndex];
						secondIndex++;
						newArrayIndex++;
					}
				}
				
				splits[y] = newArray;
				for (int j = (y + 1); j < (splits.length - 1); j++) {
					splits[j] = splits[j + 1];
				}
				
				for (int j = 0; j < (splits.length - 1) && splits[j + 1] != null; j++) {
					if (arrayEquals(splits[j], splits[j + 1])) {
						splits[j + 1] = null;
					} 
				}
				
				
			}
		}
		
		arr = splits[0];
		return arr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int[] radixsort(int[] arr) {
		if (!sorted(intToInteger(arr))) {
			int currentPosition = 1;
			boolean notAtEnd = true;

			Queue<Integer>[] buckets = new LinkedList[10];
			for (int i = 0; i < buckets.length; i++) {
				buckets[i] = new LinkedList<Integer>();
			}
			
			while (notAtEnd) {
				notAtEnd = false;
				
				for (int i = 0; i < arr.length; i++) {
					buckets[getValAtPosition(arr[i], currentPosition)]
							.add(new Integer(arr[i]));
					if (getValAtPosition(arr[i], currentPosition) > 0) {
						// Value at currentPosition's place is non-zero, continue
						notAtEnd = true;
					}
				}
				
				int index = 0;
				for (int j = 0; j < buckets.length; j++) {
					while (!buckets[j].isEmpty()) {
						arr[index] = buckets[j].remove().intValue();
						index++;
					}
				}
				
				currentPosition *= 10;
			}
		}
		
		return arr;
	}
	
	/**
	 * Returns the value at position's place of some base-10 number.
	 * 
	 * @param number The given base-10 number
	 * @param position The position to return the value from
	 * @return The value of a digit in number at position
	 */
	private int getValAtPosition(int number, int position) {
		return (number / position) % 10;
	}
	
	/**
	 * Returns if the array is sorted or not.
	 * 
	 * @return true if the array is sorted (ascending); false otherwise
	 */
	private <T extends Comparable<T>> boolean sorted(T[] arr) {
		boolean result = true;
		for (int i = 0; i < (arr.length - 1) && result; i++) {
			if (arr[i].compareTo(arr[i + 1]) > 0) {
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Compares the contents of two arrays to check if they are equal.
	 * 
	 * @param arr1 The first given array to check
	 * @param arr2 The second given array to check
	 * @return true of the arrays are equal; false otherwise
	 */
	private <T extends Comparable<T>> boolean arrayEquals(T[] arr1, T[] arr2) {
		boolean result = true;
		if (arr1.length != arr2.length) {
			result = false;
		}
		
		for (int i = 0; i < arr1.length && result; i++) {
			if (!arr1[i].equals(arr2[i])) {
				result = false;
			}
		}
		
		return result;
	}

	/**
	 * Converts an int[] to an Integer[] of same length.
	 * 
	 * @param input The input int[]
	 * @return The output Integer[]
	 */
	private Integer[] intToInteger(int[] input) {
		Integer[] output = new Integer[input.length];
		for (int i = 0; i < input.length; i++) {
			output[i] = input[i];
		}
		
		return output;
	}
}
