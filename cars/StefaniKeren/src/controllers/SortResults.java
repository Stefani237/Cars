package controllers;
import Message_Objects.NewCarDetails;

/** 
 * This SortResults sorts the results array with quickSort. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class SortResults {
	
	private NewCarDetails array[];
    private int length;
    
    public void sort(NewCarDetails[] inputArr) {
        
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        quickSort(0, length - 1);
    }
 
    private void quickSort(int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        NewCarDetails pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (array[i].getDistance() > pivot.getDistance()) {
                i++;
            }
            while (array[j].getDistance() < pivot.getDistance()) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
 
    private void exchangeNumbers(int i, int j) {
        NewCarDetails temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


}
