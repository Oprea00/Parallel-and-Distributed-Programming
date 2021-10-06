import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Algorithms {

    //sequential multiplication over 2 polynomials
    //Time complexity: O(n^2)
    public static Polynomial sequentialMultiplication(Polynomial p1, Polynomial p2){
        //initialize the result
        int resultSize = p1.getDegree() + p2.getDegree() + 1;
        ArrayList<Integer> result = new ArrayList<>();
        for (int index = 0; index < resultSize; index++){
            result.add(0);
        }
        //multiply current term of first polynomial with every term of the second and set the result
        for (int i = 0; i < p1.getCoefficients().size(); i++){
            for (int j = 0; j < p2.getCoefficients().size(); j++){
                int position = i + j;
                int value = p1.getCoefficients().get(i) * p2.getCoefficients().get(j);
                result.set(position, result.get(position) + value);
            }
        }

        return new Polynomial(result);
    }

    //multiplication over 2 polynomials using threads
    //each thread will calculate a part of the resulting polynomial
    //Time complexity: O(n^2)
    public static Polynomial parallelizedMultiplication(Polynomial p1, Polynomial p2, int threads) throws InterruptedException{
        //initialize result polynomial
        int resultSize = p1.getDegree() + p2.getDegree() + 1;
        ArrayList<Integer> coefficients = IntStream.of(new int[resultSize]).boxed().collect(Collectors.toCollection(ArrayList::new));
        Polynomial result = new Polynomial(coefficients);
        //each thread will get a number of terms to be executed
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        int step = result.getLength() / threads;
        if (step == 0)
            step = 1;
        int end;
        //for each step one thread is launched when the previous step is calculated
        for (int i = 0; i < result.getLength(); i+= step){
            end = i + step;
            //the task is submitted
            Multiplication multiplication = new Multiplication(i, end, p1, p2, result);
            executorService.execute(multiplication);
        }
        executorService.shutdown();
        //wait for service to stop
        executorService.awaitTermination(500, TimeUnit.SECONDS);

        return result;
    }


    //multiplication over 2 polynomials using sequential form of the Karatsuba algorithm
    public static Polynomial KaratsubaSequentialMultiplication(Polynomial p1, Polynomial p2){
        //if the degree is too small the simple multiplication is called
        if (p1.getDegree() < 2 || p2.getDegree() < 2){
            return sequentialMultiplication(p1, p2);
        }
        //split the polynomial in low and higher parts
        int length = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial low1 = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(0, length)));
        Polynomial height1 = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(length, p1.getLength())));
        Polynomial low2 = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(0, length)));
        Polynomial high2 = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(length, p2.getLength())));

        // the result is called recursively to the lower parts
        Polynomial z1 = KaratsubaSequentialMultiplication(low1, low2);
        // the result is called recursively to the sum of the lower and higher parts
        Polynomial z2 = KaratsubaSequentialMultiplication(addPolynomials(low1, height1), addPolynomials(low2, high2));
        // the result is called recursively to the higher parts
        Polynomial z3 = KaratsubaSequentialMultiplication(height1, high2);

        //calculate the final result
        //shift is to move the higher part to it's position in the polynomial
        Polynomial r1 = shiftPolynomial(z3, 2 * length);
        //subtracting the a*c and b*d from the result of z2
        Polynomial r2 = shiftPolynomial(subtractPolynomial(subtractPolynomial(z2, z3), z1), length);
        //summing up the final result
        return addPolynomials(addPolynomials(r1, r2), z1);

    }


    //multiplication over 2 polynomials using parallel form of the Karatsuba algorithm
    public static Polynomial KaratsubaParallelizedMultiplication(Polynomial p1, Polynomial p2) throws InterruptedException, ExecutionException{
        //if the degree is too small simple multiplication is called
        if (p1.getDegree() < 2 || p2.getDegree() < 2){
            return KaratsubaSequentialMultiplication(p1, p2);
        }
        //split the polynomial in low and higher parts
        int length = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial low1 = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(0, length)));
        Polynomial high1 = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(length, p1.getLength())));
        Polynomial low2 = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(0, length)));
        Polynomial high2 = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(length, p2.getLength())));
        //functions called asynchronously
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<Polynomial> task1 = () -> KaratsubaParallelizedMultiplication(low1, low2);
        Callable<Polynomial> task2 = () -> KaratsubaParallelizedMultiplication(addPolynomials(low1, high1), addPolynomials(low2, high2));
        Callable<Polynomial> task3 = () -> KaratsubaParallelizedMultiplication(high1, high2);

        Future<Polynomial> f1 = executor.submit(task1);
        Future<Polynomial> f2 = executor.submit(task2);
        Future<Polynomial> f3 = executor.submit(task3);

        executor.shutdown();
        //the results are obtained after the multiplication is finished
        Polynomial z1 = f1.get();
        Polynomial z2 = f2.get();
        Polynomial z3 = f3.get();

        //wait for service to stop
        executor.awaitTermination(60, TimeUnit.SECONDS);
        //calculate final result also in a sequential way
        Polynomial r1 = shiftPolynomial(z3, 2 * length);
        Polynomial r2 = shiftPolynomial(subtractPolynomial(subtractPolynomial(z2, z3), z1), length);
        return addPolynomials(addPolynomials(r1, r2), z1);
    }

    //simple addition over 2 polynomials
    public static Polynomial addPolynomials(Polynomial p1, Polynomial p2){
        int min = Math.min(p1.getDegree(), p2.getDegree());
        int max = Math.max(p1.getDegree(), p2.getDegree());
        ArrayList<Integer> coefficients = new ArrayList<>(max + 1);
        for (int index = 0; index <= min; index++){
            coefficients.add(p1.getCoefficients().get(index) + p2.getCoefficients().get(index));
        }

        addRemainingCoefficients(p1, p2, min, max, coefficients);

        return new Polynomial(coefficients);
    }

    //if the degree's are different add the remaining coefficients from the bigger polynomial
    public static void addRemainingCoefficients(Polynomial p1, Polynomial p2, int minDegree, int maxDegree, ArrayList<Integer> coefficients){
        if (minDegree != maxDegree){
            if (maxDegree == p1.getDegree()) {
                for (int index = minDegree + 1; index <= maxDegree; index++)
                    coefficients.add(p1.getCoefficients().get(index));
            }
            else{
                for (int index = minDegree + 1; index <= maxDegree; index++)
                    coefficients.add(p2.getCoefficients().get(index));
            }
        }
    }

    //simple substraction over 2 polynomials
    public static Polynomial subtractPolynomial(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        ArrayList<Integer> coefficients = new ArrayList<>(maxDegree + 1);
        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) - p2.getCoefficients().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        //remove coefficients starting from biggest power if coefficient is 0
        int i = coefficients.size() - 1;
        while (coefficients.get(i) == 0 && i > 0) {
            coefficients.remove(i);
            i--;
        }

        return new Polynomial(coefficients);
    }

    //increase the degree of a polynomial and add a number of zeros for the polynomial with the smaller degree
    public static Polynomial shiftPolynomial(Polynomial polynomial, int offset){
        ArrayList<Integer> coefficients = new ArrayList<>();
        for (int index = 0; index < offset; index++){
            coefficients.add(0);
        }
        for (int index = 0; index < polynomial.getLength(); index ++){
            coefficients.add(polynomial.getCoefficients().get(index));
        }
        return new Polynomial(coefficients);
    }
}
