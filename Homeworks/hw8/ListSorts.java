/* ListSorts.java */
import java.util.concurrent.ThreadLocalRandom;
import list.*;

public class ListSorts {

  private final static int SORTSIZE = 10000;

  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
    // Replace the following line with your solution.
    LinkedQueue queues = new LinkedQueue();
    try {
      while (q.size() > 0) {
        LinkedQueue tempQueue = new LinkedQueue();
        tempQueue.enqueue(q.dequeue());
        queues.enqueue(tempQueue);
      }
    } catch (QueueEmptyException e) {
      System.err.print("Catch a queueEmptyException in makeQueueOfQueues.");
    }
    return queues;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
  public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
    // Replace the following line with your solution.
    LinkedQueue q = new LinkedQueue();
    try {
      while (!q1.isEmpty() && !q2.isEmpty()) {
        if (((Comparable) q1.front()).compareTo((Comparable) q2.front()) <= 0) {
          q.enqueue(q1.dequeue());
        } else {
          q.enqueue(q2.dequeue());
        }
      }
    } catch (QueueEmptyException e) {
        System.err.print("Catch a QueueEmptyException in mergeSortedQueues.");
    } 
    if (q1.isEmpty()) {
      q.append(q2);
    }
    if (q2.isEmpty()) {
      q.append(q1);
    }
    return q;
  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  public static void partition(LinkedQueue qIn, Comparable pivot, 
                               LinkedQueue qSmall, LinkedQueue qEquals, 
                               LinkedQueue qLarge) {
    // Your solution here.
    try {
      while (qIn.size() > 0) {
        Comparable item = (Comparable) qIn.dequeue();
        if (item.compareTo(pivot) < 0) {
          qSmall.enqueue(item);
        } else if (item.compareTo(pivot) == 0) {
          qEquals.enqueue(item);
        } else {
          qLarge.enqueue(item);
        }
      }
    } catch (QueueEmptyException e) {
      System.err.print("Catch a QueueEmptyException in partition.");
    }
  }

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
    // Your solution here.
    LinkedQueue queues = makeQueueOfQueues(q);
    try {
      while (queues.size() > 1) {
        LinkedQueue q1 = (LinkedQueue) queues.dequeue();
        LinkedQueue q2 = (LinkedQueue) queues.dequeue();
        queues.enqueue(mergeSortedQueues(q1,q2));
      }
      q.append((LinkedQueue) queues.dequeue());
    } catch (QueueEmptyException e) {
      System.err.print("Catch a QueueEmptyException in mergeSort.");
    }
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void quickSort(LinkedQueue q) {
    // Your solution here.
    if (q.size() > 1) {
      int order = ThreadLocalRandom.current().nextInt(1,q.size()+1);
      Comparable pivot = (Comparable) q.nth(order);
      LinkedQueue qSmall = new LinkedQueue();
      LinkedQueue qEquals = new LinkedQueue();
      LinkedQueue qLarge = new LinkedQueue();
      partition(q,pivot,qSmall,qEquals,qLarge);
      quickSort(qSmall);
      quickSort(qLarge);
      q.append(qSmall);
      q.append(qEquals);
      q.append(qLarge);
    }
  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }

  /**
   *  main() performs some tests on mergesort and quicksort.  Feel free to add
   *  more tests of your own to make sure your algorithms works on boundary
   *  cases.  Your test code will not be graded.
   **/
  public static void main(String [] args) {

    LinkedQueue q = makeRandom(10);
    System.out.println(q.toString());
    mergeSort(q);
    System.out.println(q.toString());

    q = makeRandom(10);
    System.out.println(q.toString());
    quickSort(q);
    System.out.println(q.toString());


    Timer stopWatch = new Timer();
    q = makeRandom(10);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + "10" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(10);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + "10" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    q = makeRandom(100);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + "100" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(100);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + "100" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    q = makeRandom(1000);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + "1000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(1000);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + "1000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    q = makeRandom(10000);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + "10000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(10000);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + "10000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    q = makeRandom(100000);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + "100000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(100000);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + "100000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    q = makeRandom(1000000);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + "1000000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(1000000);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + "1000000" + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

  }

}