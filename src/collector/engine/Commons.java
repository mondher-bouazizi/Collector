package collector.engine;

import java.util.Arrays;

public class Commons {
	
	//=================================//
	//   PRINT A TEXT IN THE CONSOLE   //
	//=================================//
	
	public static void print(String text) {
		System.out.println(text);
	}

	public static void print(String[] array) {
		System.out.println(Arrays.asList(array));
	}

	public static void print(int[] array) {
		System.out.println(Arrays.asList(array));
	}

	public static void print(double[] array) {
		System.out.println(Arrays.asList(array));
	}

	public static void print(int[][] array) {
		if (array.length == 0)
			System.out.println("[]");

		else if (array.length == 1) {
			System.out.println("[");
			print(array[0]);
			System.out.println("]");
		} else {
			System.out.print("[");
			for (int i = 0; i < array.length - 1; i++) {
				print(array[i]);
				print(" , ");
			}
			print(array[array.length - 1]);
			System.out.println("]");
		}
	}

	public static void print(double[][] array) {
		if (array.length == 0)
			System.out.println("[]");

		else if (array.length == 1) {
			System.out.println("[");
			print(array[0]);
			System.out.println("]");
		} else {
			System.out.print("[");
			for (int i = 0; i < array.length - 1; i++) {
				print(array[i]);
				print(" , ");
			}
			print(array[array.length - 1]);
			System.out.println("]");
		}
	}
	
	
	//=================================//
	//      ARRAY-RELATED METHODS      //
	//=================================//
	
	/**
	 * Sort an array of doubles
	 *
	 * @param array : the array to sort
	 * @return the array sorted
	 */
	public static void sort(double[] array) {

		int length = array.length;

		for (int j = 1; j < length; j++) {
			double temp = array[j];
			int i = j - 1;
			while ((i > -1) && (array[i] > temp)) {
				array[i + 1] = array[i];
				i--;
			}
			array[i + 1] = temp;
		}
	}
	
	/**
	 * Sort an array of integers
	 *
	 * @param array : the array to sort
	 * @return the array sorted
	 */
	public static void sort(int[] array) {

		int length = array.length;

		for (int j = 1; j < length; j++) {
			int temp = array[j];
			int i = j - 1;
			while ((i > -1) && (array[i] > temp)) {
				array[i + 1] = array[i];
				i--;
			}
			array[i + 1] = temp;
		}
	}
	

}
