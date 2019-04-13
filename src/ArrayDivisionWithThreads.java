import java.util.*;

public class ArrayDivisionWithThreads {

    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    static int[] array;
    static Thread[] threadArray;
    static int arrayLength;
    static int numOfThreads;
    static HashMap<Integer, Integer> threadStarts = new HashMap<>();
    static HashMap<Integer, Integer> threadEnds = new HashMap<>();
    static int indexA;
    static List<Integer> minList = new ArrayList<>();
    static List<Integer> maxList = new ArrayList<>();
    static List<Integer> listaSum = new ArrayList<>();
    static int maxOfArray;
    static int minOfArray;
    static Integer sumOfArray;





    static void fillArray() {
        for (int i = 0; i < arrayLength; i++) {
            array[i] = random.nextInt(10000);
          //  System.out.print(array[i] + " ");
        }

    }


    public static void main(String[] args) throws InterruptedException {
        System.out.println(" Give length of array  ");
        arrayLength = scanner.nextInt();

        System.out.println(" Give the amount of threads ");
        numOfThreads = scanner.nextInt();

        array = new int[arrayLength];
        threadArray = new Thread[numOfThreads];

        fillArray();
        divideArray();
        initializeThreads();
        indexA = 1;
        long timeStarter = System.nanoTime();
        for (int i = 0; i < threadArray.length-1; i++) {
            threadArray[i].start();

            if(indexA < threadArray.length-1){
                indexA++;
            }
        }
        for (int i = 0; i < threadArray.length-1; i++) {
            threadArray[i].join();
        }
        long timeEnder = System.nanoTime();
        System.out.println("Time of thread processing = "  +(timeEnder-timeStarter));
        threadArray[threadArray.length-1].start();
        threadArray[threadArray.length-1].join();

        System.out.println();
        System.out.println("Max  from array = " + maxOfArray);
        System.out.println("Min  from array = " + minOfArray);
        System.out.println("Sum  from array = " + sumOfArray);
    }

    static void divideArray() {
        int arrayPart;
        int lastThread;
        if(numOfThreads == 1){
            arrayPart = arrayLength;
            lastThread = 0;
        }else   {
            arrayPart = (int) Math.ceil(arrayLength / (numOfThreads - 1));
            lastThread = arrayLength % (numOfThreads - 1);
        }

        int index = 1;
        int i;
        for (i = 0; i < arrayLength - lastThread; i++) {

            threadStarts.put(index, i);
            i += (arrayPart - 1);
            threadEnds.put(index, i);
            index++;
        }
        if (lastThread != 0) {
            threadStarts.put(index, i);
            threadEnds.put(index, i + lastThread - 1);
        }
    }


    static void initializeThreads() {
        if (numOfThreads == 1) {
            threadArray[0] = new Thread(new Runnable() {

                int sum = 0;
                int max = 0;
                int min = array[0];

                @Override
                public void run() {
                    for (int i = 0; i < array.length; i++) {


                        sum += array[i];
                        if (array[i] > max) {
                            max = array[i];
                        } else if (array[i] < min) {
                            min = array[i];

                        }

                    }
                    maxOfArray = max;
                    minOfArray = min;
                    sumOfArray = Integer.valueOf(sum);
                }
            });
        } else {

            for (int i = 0; i < numOfThreads - 1; i++) {


                threadArray[i] = new Thread(new Runnable() {

                    @Override
                    public void run() {


                        int min = array[threadStarts.get((indexA))];
                        int max = 0;
                        int sum = 0;

                        for (int j = threadStarts.get((indexA)); j <= threadEnds.get((indexA)); j++) {
                            sum += array[j];
                            if (array[j] > max) {
                                max = array[j];
                            } else if (array[j] < min) {
                                min = array[j];

                            }

                        }
                        // synchronized (this) {
                        maxList.add(max);
                        minList.add(min);
                        listaSum.add(sum);
                        // }
                    }
                });

            }
            threadArray[numOfThreads -1] = new Thread(new Runnable() {

                @Override
                public void run() {
                    minOfArray = Collections.min(minList);
                    maxOfArray = Collections.max(maxList);
                    sumOfArray = listaSum.stream()
                            .mapToInt(Integer::intValue)
                            .sum();
                }
            });

        }
    }

}
