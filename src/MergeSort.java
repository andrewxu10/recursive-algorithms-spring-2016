import java.util.Arrays;

//mergesort
public class MergeSort {
	
	public static void main(String[] args) {
		int[] test = {17, 12, 36, 19, 25, 6, 32, 30};
		
		System.out.println("Original");
		for (int i : test) {
			System.out.print(i + " ");
		}
		System.out.println();
		
		mergeSort(test);
		
		System.out.println("Sorted");
		for (int i : test) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

	public static void mergeSort(int[] arrayInt){
		if(arrayInt.length <= 1) {
			// do nothing -- already sorted
		} else {
			int[] firstHalf = Arrays.copyOfRange( arrayInt, 0, arrayInt.length/2);
			int[] secondHalf = Arrays.copyOfRange( arrayInt, arrayInt.length/2, arrayInt.length);
			
			// recursively call mergeSort here
			mergeSort(firstHalf); //pretend that this is calling an outside method
			mergeSort(secondHalf);
			
			// merge
			int i = 0;
			int j = 0;
			int k = 0;
			
			while (i < firstHalf.length && j < secondHalf.length){
				if(firstHalf[i] < secondHalf[j]){
					arrayInt[k] = firstHalf[i];
					i++;
					k++;
				} else {
					arrayInt[k] = secondHalf[j];
					j++;
					k++;
				}
			}

			while (i < firstHalf.length) {
				arrayInt[k] = firstHalf[i];
				i++;
				k++;
			}
			
			while (j < secondHalf.length){
				arrayInt[k] = secondHalf[j];
				j++;
				k++;
			}
		}
		
	}
	
}
