import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException{
        var polynomial1 = new Polynomial(new ArrayList<Integer>(List.of(1,1,1,1,1,1)));
        var polynomial2 = new Polynomial(new ArrayList<Integer>(List.of(1,1,1,1,1,1)));
        var polynomial3 = new Polynomial(new ArrayList<Integer>(List.of(1,1,9,8,7,6,5,4,3,2,1)));
        var polynomial4 = new Polynomial(new ArrayList<Integer>(List.of(1,1,9,8,7,6,5,4,3,2,1)));
        var polynomial5 = new Polynomial(new ArrayList<Integer>(List.of(1,2,3,4,5,6,7,8,9,8,7,5,4,3,2,1)));
        var polynomial6 = new Polynomial(new ArrayList<Integer>(List.of(1,2,3,4,5,6,7,8,8,7,6,5,4,3,2,1)));


        System.out.println("Polynomial 1:" + polynomial1 + '\n');
        System.out.println("Polynomial 2:" + polynomial2 + '\n');
        System.out.println(startSequentialMultiplication(polynomial1, polynomial2));
        System.out.println(startParallelMultiplication(polynomial1, polynomial2));
        System.out.println(startKaratsubaSequential(polynomial1, polynomial2));
        System.out.println(startKaratsubaParallel(polynomial1, polynomial2));

    }

    static Polynomial startSequentialMultiplication(Polynomial p1, Polynomial p2){
        var startTime = System.currentTimeMillis()/1000.0;
        Polynomial result = Algorithms.sequentialMultiplication(p1, p2);
        var endTime = System.currentTimeMillis()/1000.0;
        System.out.println("Time for sequential multiplication: " + (endTime-startTime));
        System.out.println("Sequential Multiplication result: ");
        return result;
    }

    static Polynomial startParallelMultiplication(Polynomial p1, Polynomial p2) throws InterruptedException{
        var startTime = System.currentTimeMillis()/1000.0;
        Polynomial result = Algorithms.parallelizedMultiplication(p1, p2, 5);
        var endTime = System.currentTimeMillis()/1000.0;
        System.out.println('\n' + "Time for parallel multiplication: " + (endTime-startTime));
        System.out.println("Parallel Multiplication result: ");
        return result;
    }

    static Polynomial startKaratsubaSequential(Polynomial p1, Polynomial p2){
        var startTime = System.currentTimeMillis()/1000.0;
        Polynomial result = Algorithms.KaratsubaSequentialMultiplication(p1, p2);
        var endTime = System.currentTimeMillis()/1000.0;
        System.out.println("\n Time for Karatsuba multiplication: " + (endTime-startTime));
        System.out.println("Karatsuba Multiplication result: ");
        return result;
    }

    static Polynomial startKaratsubaParallel(Polynomial p1, Polynomial p2) throws InterruptedException, ExecutionException {
        var startTime = System.currentTimeMillis()/1000.0;
        Polynomial result = Algorithms.KaratsubaParallelizedMultiplication(p1, p2);
        var endTime = System.currentTimeMillis()/1000.0;
        System.out.println("\n Time for Karatsuba Parallel multiplication: " + (endTime-startTime));
        System.out.println("Karatsuba Parallel Multiplication result: ");
        return result;
    }
}
