public class Multiplication implements Runnable{
    private int start, end;
    private Polynomial p1, p2, result;

    public Multiplication(int start, int end, Polynomial p1, Polynomial p2, Polynomial result){
        this.start = start;
        this.end = end;
        this.p1 = p1;
        this.p2 = p2;
        this.result = result;
    }

    //calculate coefficients from the result in the interval [start, end]
    @Override
    public void run() {
        for (int index = start; index < end; index++){
            if(index > result.getLength())
                return;
            for(int index2 = 0; index2 <= index; index2++){
                if(index2 < p1.getLength() && (index-index2) < p2.getLength()){
                    int value = p1.getCoefficients().get(index2) * p2.getCoefficients().get(index - index2);
                    result.getCoefficients().set(index, result.getCoefficients().get(index) + value);
                }
            }
        }
    }
}
