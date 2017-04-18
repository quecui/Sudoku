package com.uet.sudoku.graph;// A Java program to implement graph coloring


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// This class represents an undirected graph using adjacency list
class Graph {
    private int V;   // No. of vertices
    private LinkedList<Integer> adj[]; //Adjacency List

    //Constructor
    Graph(int v) {
        V = v;

        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adj[i] = new LinkedList();

    }

    //Function to add an edge into the graph
    void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v); //Graph is undirected
    }

    public int getV() {
        return V;
    }

    public void printGraph() {
        for (int i = 0; i < V; i++) {
            System.out.print(i + ": ");
            Iterator<Integer> itr = adj[i].iterator();
            while (itr.hasNext()) {
                System.out.print(itr.next() + " ");
            }
            System.out.print(" - Size: " + adj[i].size() + "  ");
            System.out.println();
        }
    }

    public LinkedList<Integer> getListAdj(int u) {
        return adj[u];
    }

    public boolean readGraph(String input_name, int result[]) {
        int index = 0;
        String inLine;
        File file = new File(input_name);

        if(!file.exists()){
            System.out.println("File Puzzle.txt don't exist");
            return false;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((inLine = br.readLine()) != null) {
                String[] tmp = inLine.split(" ");
                for (int i = 0; i < tmp.length; i++) {
                    int n = Integer.parseInt(tmp[i]);
                    result[index] = n;
                    index++;
                }
            }

            br.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        this.V = 81;
        int start = 9;
        int[][] array = new int[start][start];
        int count = 0;
        for (int i = 0; i < start; i++) {
            for (int j = 0; j < start; j++) {
                array[i][j] = count;
                count++;
            }
        }

        createGraphByGetColum(0, 0, 0, start * (start - 1), start);
        createGraphByGetRow(0, 0, 0, start - 1, start);
        createGraphByFillOtherCellInColAndRow(start);
        createGraphByBlock(array);

//        for(int i = 0; i < 81; i++){
//            Collections.sort(adj[i]);
//        }


        return true;
    }

    //des: Gia tri tai bien cuoi cung | v: dinh goc | w: dinh chay | step: gia tri dung de chuyen o
    void createGraphByGetColum(int v, int w, int step, int des, int maxStep) {
        if (step < maxStep - 1) {
            createGraphByGetColum(v, w + 1, step + 1, des + 1, maxStep);
        }

        while (true) {
            step += maxStep;
            if (step > des)
                break;
            addEdge(w, step);
        }

        return;
    }

    void createGraphByGetRow(int v, int w, int step, int des, int maxStep) {
        if (w < (maxStep - 1) * maxStep) {
            createGraphByGetRow(v + maxStep, w + maxStep, step + maxStep, des + maxStep, maxStep);
        }

        while (true) {
            step += 1;
            if (step > des)
                break;
            addEdge(w, step);
        }

        return;
    }

    void createGraphByFillOtherCellInColAndRow(int maxStep) {
        int step = 0;

        while (true) {
            if (step == 0) {
                for (int i = maxStep - 1; i < adj[step].size(); i++) {
                    for (int j = i + 1; j < adj[step].size(); j++) {
                        addEdge(adj[step].get(i), adj[step].get(j));
                    }
                }
            }

            for (int i = 0; i < maxStep - 1; i++) {
                for (int j = i + 1; j < maxStep - 1; j++) {
                    addEdge(adj[step].get(i), adj[step].get(j));
                }
            }
            step++;
            if (step >= maxStep)
                break;
        }

        while (true) {
            for (int i = 1; i < maxStep; i++) {
                for (int j = i + 1; j < maxStep; j++) {
                    addEdge(adj[step].get(i), adj[step].get(j));
                }
            }

            step += maxStep;
            if (step > maxStep * (maxStep - 1))
                break;
        }
    }

    int[][] createSmallMatrix(int[][] bigMatrix, int startC, int startH) {
        int step = 3, x = 0, y = 0;
        int[][] smallMatrix = new int[3][3];

        foo:
        for (int i = startC; ; i++) {
            for (int j = startH; ; j++) {

                if (j < (startH + 3)) {
                    smallMatrix[x][y] = bigMatrix[i][j];
                    y++;
                } else {
                    if (x == 2)
                        break foo;
                    i++;
                    x++;
                    y = 0;
                    j = startH - 1;
                }
            }
        }

        return smallMatrix;
    }
    
    void createGraphByBlock(int[][] bigMatrix) {
        int[][] smallMatrix;
        int startC = 0, startH = 0;
        List<Integer> list;

        while (true) {
            list = new ArrayList<>();
            smallMatrix = new int[3][3];

            smallMatrix = createSmallMatrix(bigMatrix, startH, startC);

            int mocH = 0, mocC = 0;
            while (true) {
                list = new ArrayList<>();
                for (int i = mocH + 1; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (j != mocC) {
                            addEdge(smallMatrix[mocH][mocC], smallMatrix[i][j]);
                            list.add(smallMatrix[i][j]);
                        }
                    }
                }

                loop:
                for (int i = 0; i < 2; i++) {
                    for (int j = list.size() - 1; j > 1; j--) {
                        addEdge(list.get(i), list.get(j));
                        i++;
                        if (i >= 2)
                            break loop;
                    }
                }

                mocC++;
                if (mocC >= 3) {
                    mocC = 0;
                    mocH++;
                }

                if (mocH == 1 && mocC == 0)
                    break;
            }
            
            startC += 3;
            if (startC >= 9) {
                startC = 0;
                startH += 3;
                if (startH >= 9)
                    break;
            }
        }
    }

    void printEgdes() {
        int count = 0;

        for (int i = 0; i < adj.length; i++) {
            count += adj[i].size();
        }

        System.out.println("So canh " + count / 2);
    }
}
// This code is contributed by Anh Vo 
