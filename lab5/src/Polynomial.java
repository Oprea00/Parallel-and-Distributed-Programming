import java.util.ArrayList;


public class Polynomial {
    private ArrayList<Integer> coefficients;

    public Polynomial(ArrayList<Integer> coefficients){
        this.coefficients = coefficients;
    }

    public ArrayList<Integer> getCoefficients(){
        return this.coefficients;
    }

    public int getDegree(){
        return this.coefficients.size() - 1;
    }

    public int getLength() { return this.coefficients.size(); }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        for (int index = 0; index<coefficients.size();index++) {
            if(coefficients.get(index) !=0) {
                String sign = (coefficients.get(index) > 0) ? " + " : " - ";
                result.append(sign).append(coefficients.get(index)).append("*x^").append(coefficients.size() - index - 1).append(" ");
            }
        }
        return result.toString();
    }
}
