import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Service {
    public ArrayList<ArrayList<Integer>> directedGraph;

    public Service(ArrayList<ArrayList<Integer>> graph){
        directedGraph = graph;
    }

    public void run(){
        ArrayList<Integer> path = new ArrayList<Integer>();
        //initialize the path
        IntStream.range(0, directedGraph.size()).forEach(value -> path.add(-1));
        //start from first element
        try{
            path.set(0,0);
            findHamiltonian(0, path, 1);
            throw new Exception("Solution does not exist. Not Hamiltonian Cycle found");
        }
        catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    //true is the vertex is in the path, false otherwise
    public boolean visitedVertex(int vertex, ArrayList<Integer> path, int nrOfElemsPath){
        for (int index=0; index < nrOfElemsPath-1; index++){
            if (path.get(index) == vertex)
                return true;
        }
        return false;
    }

    public void findHamiltonian(int vertex, ArrayList<Integer> path, int nrOfElemsPath) throws Exception{
        System.out.println(path);
        //graph is hamiltonian if:  1) size of the path is equal with the size of the graph
        //                          2) we are back at the first vertex
        if (nrOfElemsPath == directedGraph.size() && directedGraph.get(vertex).get(0) == 1){
            System.out.println("Solution exists. Following is one Hamiltonian Cycle: " + path);
            throw new Exception("Hamiltonian Cycle found");
        }
        //return if visited all vertices but don't found a cycle
        if (nrOfElemsPath == directedGraph.size())
            return;
        //start looking in the vertex list, ex: [0,1,0,1,0]
        for (int index=0; index < directedGraph.size(); index++){
            //if we find an edge
            if(directedGraph.get(vertex).get(index) == 1){
                //add it to the path
                path.set(nrOfElemsPath++, index);
                //remove the edge for now to not return to it by putting 0 in the graph representation
                directedGraph.get(vertex).set(index, 0);
                directedGraph.get(index).set(vertex, 0);

                //if this vertex is unvisited call the function recursively on a new thread
                if (!visitedVertex(index, path, nrOfElemsPath)){
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    final int currentVertex = index;
                    final int nrOfElements = nrOfElemsPath;
                    final Runnable task = () ->{
                        try{
                            findHamiltonian(currentVertex, path, nrOfElements);
                        } catch (Exception exception) {
                            throw new RuntimeException(exception.getMessage());
                        }
                    };
                    //this will be a future
                    executorService.submit(task).get();
                }
                //repair the removed edge
                directedGraph.get(vertex).set(index, 1);
                directedGraph.get(index).set(vertex, 1);
                path.set(--nrOfElemsPath, -1);
            }
        }
    }
}
