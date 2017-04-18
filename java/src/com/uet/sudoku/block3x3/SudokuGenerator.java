package com.uet.sudoku.block3x3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

/**
 * The SudokuGenerator class creates a random standard (9x9)
 * Sudoku board through the use of permutation techniques.
 */
public class SudokuGenerator {

    public static final int n = 3;
    public Writer fileOut;
    private ArrayList<Block> boardS;
    private SudokuSolver sudokuSolver;
    /**
     * Constructor.  Resets board to zeros
     */
    public SudokuGenerator() {
        sudokuSolver = new SudokuSolver();
    }

    /**
     * Driver method for nextBoard.
     *
     * @param difficult the number of blank spaces to insert
     * @return board, a partially completed 9x9 Sudoku board
     */
    public void generatingSudoku(String output1, int number_blanks) throws InterruptedException {
        ArrayList<int[]> a = new ArrayList<>();
        boardS = Block.generateBlock(a);
        int[][] matrixRoot = new int[9][9];
        sudokuSolver.convertTo9x9(null, -1, matrixRoot, boardS);
        swapRow(1, 3, matrixRoot);
        swapRow(2, 6, matrixRoot);
        swapRow(5, 7, matrixRoot);
        createPuzzleSudoku(matrixRoot, number_blanks);
        writePuzzle(boardS, output1);
    }

    void createPuzzleSudoku(int[][] matrix, int numberBlanks) throws InterruptedException {
        int count = 0, value;
        Random rd = new Random();
        while (count < numberBlanks){
            int row = rd.nextInt(9);
            int col = rd.nextInt(9);
            if(matrix[row][col] == 0)
                continue;

            value = matrix[row][col];
            matrix[row][col] = 0;

            sudokuSolver.matrix9x9First = matrix;
            if(sudokuSolver.solve(-1)){
                count ++;
                if (count == numberBlanks) {
                    for(int i = 0; i < 9; i++){
                        for (int j = 0; j < 9; j++){
                            System.out.print(matrix[i][j] + " ");
                        }
                        System.out.println();
                    }
                }

            }else {
                matrix[row][col] = value;
            }
        }

        SudokuSolver ss = new SudokuSolver();
        ss.convertToSmallMatrix(matrix);
        boardS = ss.getBoardS();
    }

    public void swapRow(int sour, int des, int[][] matrix){
        int tmp;

        for (int i = 0; i < 9; i++){
            tmp = matrix[sour][i];
            matrix[sour][i] = matrix[des][i];
            matrix[des][i] = tmp;
        }
    }

    public void writePuzzle(ArrayList<Block> boardS, String output_name) {
        int i = 0;
        int count = 0;
        try {
            fileOut = new FileWriter(output_name);
            int[][] matrix = new int[9][9];

            sudokuSolver.convertTo9x9(null, -1, matrix, boardS);

            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    fileOut.write(matrix[r][c] + " ");
                }
                fileOut.write("\n");
            }
            System.out.println("Done");

            fileOut.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SudokuGenerator sg = new SudokuGenerator();
        sg.generatingSudoku(args[0], Integer.parseInt(args[1]));
    }


}
