import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){

        //represent the graph as an Adjacency Matrix
        ArrayList<ArrayList<Integer>> graph1 = new ArrayList(List.of(
                new ArrayList(List.of(0, 1, 0, 1, 0)),     //   (0)--(1)--(2)
                new ArrayList(List.of(1, 0, 1, 1, 1)),     //    |   / \   |
                new ArrayList(List.of(0, 1, 0, 0, 1)),     //    |  /   \  |
                new ArrayList(List.of(1, 1, 0, 0, 1)),     //    | /     \ |
                new ArrayList(List.of(0, 1, 1, 1, 0))      //   (3)-------(4)
        ));

        ArrayList<ArrayList<Integer>> graph2 = new ArrayList(List.of(
                new ArrayList(List.of(0, 1, 0, 1, 0)),     //   (0)--(1)--(2)
                new ArrayList(List.of(1, 0, 1, 1, 1)),     //    |   / \   |
                new ArrayList(List.of(0, 1, 0, 0, 1)),     //    |  /   \  |
                new ArrayList(List.of(1, 1, 0, 0, 0)),     //    | /     \ |
                new ArrayList(List.of(0, 1, 1, 0, 0))      //   (3)       (4)
        ));

        ArrayList<ArrayList<Integer>> graph3 = new ArrayList(List.of(
                new ArrayList(List.of(0, 1, 0, 0, 0, 1, 0, 0, 1)),  //   ---------------
                new ArrayList(List.of(1, 0, 1, 0, 1, 0, 0, 0, 0)),  //   |              \
                new ArrayList(List.of(0, 1, 0, 1, 0, 0, 0, 0, 0)),  //  (0)--(1)--(2)   |
                new ArrayList(List.of(0, 0, 1, 0, 1, 0, 0, 0, 1)),  //   |    |    |    |
                new ArrayList(List.of(0, 1, 0, 1, 0, 1, 0, 1, 0)),  //   |    |    |    |
                new ArrayList(List.of(1, 0, 0, 0, 1, 0, 1, 0, 0)),  //  (5)--(4)--(3)   |
                new ArrayList(List.of(0, 0, 0, 0, 0, 1, 0, 1, 0)),  //   |    |    |    |
                new ArrayList(List.of(0, 0, 0, 0, 1, 0, 1, 0, 1)),  //   |    |    |    /
                new ArrayList(List.of(1, 0, 0, 1, 0, 0, 0, 1, 0))   //  (6)--(7)--(8)---
        ));

        ArrayList<ArrayList<Integer>> graph4 = new ArrayList(List.of(
                new ArrayList(List.of(0, 1, 0, 0, 0, 1, 0, 0, 0)),
                new ArrayList(List.of(1, 0, 1, 0, 1, 0, 0, 0, 0)),
                new ArrayList(List.of(0, 1, 0, 1, 0, 0, 0, 0, 0)),  //  (0)--(1)--(2)
                new ArrayList(List.of(0, 0, 1, 0, 1, 0, 0, 0, 1)),  //   |    |    |
                new ArrayList(List.of(0, 1, 0, 1, 0, 1, 0, 1, 0)),  //   |    |    |
                new ArrayList(List.of(1, 0, 0, 0, 1, 0, 1, 0, 0)),  //  (5)--(4)--(3)
                new ArrayList(List.of(0, 0, 0, 0, 0, 1, 0, 1, 0)),  //   |    |    |
                new ArrayList(List.of(0, 0, 0, 0, 1, 0, 1, 0, 1)),  //   |    |    |
                new ArrayList(List.of(0, 0, 0, 1, 0, 0, 0, 1, 0))   //  (6)--(7)--(8)
        ));

        //graph1.forEach(System.out::println);
        //System.out.println("\nGraph 2: \n");
        //graph2.forEach(System.out::println);

        var startTime = System.currentTimeMillis()/1000.0;

        try{
            Service service = new Service(graph1);
            service.run();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        var endTime = System.currentTimeMillis()/1000.0;
        var time = endTime - startTime;
        System.out.println(time);
    }
}
