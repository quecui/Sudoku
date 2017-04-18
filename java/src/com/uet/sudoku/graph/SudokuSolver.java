package com.uet.sudoku.graph;// A Java program to implement greedy algorithm for graph coloring

// A Java program to implement greedy algorithm for graph coloring

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

// This class represents an undirected graph using adjacency list
class SudokuSolver
{
    private Graph g;
    private int cColored;
    int result[];
    public Writer fileOut;
    int [] copyResult = new int[81];
    //Constructor
    SudokuSolver(int v, String name)
    {
        cColored = 0;
        g = new Graph(v);
        result = new int[v];
        g.readGraph(name, result);
    }

    void AvailableColor(boolean available[])
    {
        System.out.println("------------------------------");
        for (int cr = 1; cr < 10; cr++)
        {
            if(available[cr]==false)
                System.out.print(cr + " ");
        }
        System.out.println();
    }

    void GraphColoring(String output_name)
    {

        for (int i = 0; i < 81; i++){
            copyResult[i] = result[i];
        }

        // Initialize remaining V-1 vertices as unassigned
        for (int u = 0; u < g.getV(); u++)
            if(result[u]!=0)
                cColored++;
        // Do something here
        for (int i = 0; i < result.length; i++) {
            if (result[i] != 0)
                continue;

            if (!fillColor(9, i)) {
                System.out.println("Solution does not exist");
                return;
            }
            break;
        }

        writeSolvePuzzle(output_name);
        System.out.println("Done !!!!!!");
    }


    boolean fillColor(int m, int v) {
        if (v == 81)
            return true;

        for (int c = 1; c <= m; c++) {
            if (isSafe(v, c)) {
                if(result[v] == 0)
                    result[v] = c;

                System.out.println("\n");
                for (int i = 1; i <= result.length; i++){
                    System.out.print(result[i - 1] + " ");
                    if (i % 9 == 0)
                        System.out.println();
                }

                if (fillColor(m, v + 1))
                    return true;

                if(copyResult[v] == 0)
                    result[v] = 0;
                else
                    return false;
            }
        }
        return false;
    }

    boolean isSafe(int v, int c) {
        for (int i = 0; i < g.getListAdj(v).size(); i++)
            if (c == result[g.getListAdj(v).get(i)])
                return false;
        return true;
    }

    public boolean writeSolvePuzzle(String output_name)
    {
        try{
            fileOut = new FileWriter(output_name);
            int n = g.getV() / 9;
            for(int i=0 ;i < n; i++)
            {
                for(int j=0 ;j < n; j++)
                    fileOut.write(result[i*n + j] + " ");
                fileOut.write("\n");
            }
            fileOut.close();
            return true;
        }
        catch(IOException e)
        {
            System.out.println(e);
            return false;
        }
    }

    public static void main(String args[])
    {
        SudokuSolver g1 = new SudokuSolver(81, args[0]);
        g1.GraphColoring(args[1]);
    }
}
// This code is contributed by Anh Vo 
