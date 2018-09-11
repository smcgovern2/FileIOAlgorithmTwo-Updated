package us.mattgreen;

import java.util.*;
import java.util.Scanner;

public class Main {

    private final static FileInput cardAccts = new FileInput("movie_cards.csv");
    private final static FileInput cardPurchases = new FileInput("movie_purchases.csv");
    private final static FileInput cardRatings = new FileInput("movie_rating.csv");
    private static Scanner keyboard = new Scanner(System.in);
    public final static FileOutput outFile = new FileOutput("SortedOutput.csv");

    public static void main(String[] args) {
        String line;
        String[] fields;
        int[] nums = new int[2];
        String avgRating;
        ArrayList<String[]> toSort = new ArrayList<String[]>();
        System.out.format("%8s  %-18s %6s %6s %6s\n","Account","Name", "Movies", "Points", "Average Rating");
        while ((line = cardAccts.fileReadLine()) != null) {
            fields = line.split(",");
            findPurchases(fields[0], nums);
            avgRating = getRatings(fields[0]).toString();
            System.out.format("00%6s  %-18s  %2d   %4d  %6s\n",fields[0],fields[1], nums[0], nums[1], avgRating.substring(0,3));

            toSort.add(new String[]{fields[0], fields[1], Double.toString(nums[0]), Double.toString(nums[1]), avgRating.substring(0,3)});
        }


        ArrayList<String[]> sorted = bubbleSort(toSort);

        for (String[] array : sorted) {
            System.out.println(array[4]);
        }



        for (String[] array : sorted) {
            for(int i=0; i < array.length; i++){
                if (i < array.length -1) {
                    outFile.fileWrite(array[i] + ",");
                } else {
                    outFile.fileWrite(array[i] + "\n");
                }
            }
        }


        outFile.fileClose();


    }

    public static ArrayList<String[]> bubbleSort(ArrayList<String[]> al) {
        ArrayList<String[]> toSort = al;
        int n = toSort.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (Double.parseDouble(toSort.get(i)[4]) > Double.parseDouble(toSort.get(k)[4])) {
                    String[] hold = toSort.get(i);
                    toSort.set(i, toSort.get(k));
                    toSort.set(k, hold);

                }
            }

        }

        return toSort;
    }

    public static Double getRatings(String acct) {
        String line;
        int total = 0;
        int count = 0;
        String[] ratings;
        boolean done = false;
        while (((line = cardRatings.fileReadLine()) != null) && !(done)) {
            ratings = line.split(",");
            if (ratings[0].compareTo(acct) > 0) {
                done = true;
            } else if (ratings[0].equals(acct)){
                total += Integer.parseInt(ratings[1]);
                count++;
            }


        }
//        return Double.valueOf(total);
        if (count!=0) {
            return (Double.valueOf(total) / Double.valueOf(count));
        } else {
            return 0.0;
        }
    }

    public static void findPurchases(String acct, int[] nums) {
        nums[0] = 0;
        nums[1] = 0;
        String line;
        String[] fields;
        boolean done = false;
        while (((line = cardPurchases.fileReadLine()) != null) && !(done)) {
            fields = line.split(",");
            if (fields[0].compareTo(acct) > 0) {
                done = true;
                System.out.println("true");
            }
            else if (fields[0].equals(acct)) {
                nums[0]++;
                nums[1] += Integer.parseInt(fields[2]);

                System.out.println(nums[0] + " " + nums[1]);
            }

        }
    }
}