package com.uet.sudoku.block3x3;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuSolver {
    private Writer fileOut;

    public ArrayList<Block> getBoardS() {
        return boardS;
    }

    private ArrayList<Block> boardS;
    private final int BlockSize = 3;
    private final int nBlock = 9;
    private final int nBoard = 81;
    private List<List<int[][]>> listPermution;
    private List<int[][]> listMatrix;
    public int[][] matrix9x9First = new int[9][9];

    public SudokuSolver(){
        boardS = new ArrayList<>();
    }


    public boolean readSudoku(String filename) throws IOException {
        File file = new File(filename);
        if(!file.exists())
            return false;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String inLine;
        int i = 0;
        while ((inLine = br.readLine()) != null){
            String[] tmp = inLine.split(" ");
            for (int j = 0; j < 9; j++) {
                matrix9x9First[i][j] = Integer.parseInt(tmp[j]);
            }
            i++;
        }
        br.close();

        return true;
    }

    //convert from 9x9 to 3x3
    public boolean convertToSmallMatrix(int[][] matrix){
        int startRow = 0, startCol = 0;

        while (true) {
            Block newBlock = new Block();
            if (startCol >= 9) {
                startRow += 3;
                startCol = 0;
            }
            if (startRow >= 9) {
                return true;
            }

            for (int i = startRow, h = 0; i < startRow + 3; i++, h++) {
                for (int j = startCol, c = 0; j < startCol + 3; j++, c++) {
                    int value = matrix[i][j];
                    newBlock.block[h][c] = value;
                }
            }
            boardS.add(newBlock);
            startCol += 3;
        }
    }

    /*
        Root
     */
    public void generatePermute(){
        List<List<Integer>> listAvaiableValue = new ArrayList<>();
        getAvaiableValues(listAvaiableValue);

        for (int i = 0; i < listAvaiableValue.size(); i++) {
            List<List<Integer>> values = new ArrayList<>();
            List<int[][]> tmpList = new ArrayList<>();
            permute(listAvaiableValue.get(i), 0, values);
            convertToMatrixHV(tmpList, values, i);
            listPermution.add(tmpList);
        }

        createCloneBroadS();
    }

    void createCloneBroadS(){
        for (int i = 0; i < boardS.size(); i++) {
            int[][] tmp = new int[3][3];
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tmp[j][k] = boardS.get(i).block[j][k];
                }
            }
            listMatrix.add(tmp);
        }
    }

    void permute(List<Integer> arr, int k, List<List<Integer>> values) {
        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);
            permute(arr, k + 1, values);
            Collections.swap(arr, k, i);
        }

        if (k == arr.size() - 1) {
            values.add(copyList(arr));
        }
    }

    List<Integer> copyList(List<Integer> src) {
        List<Integer> des = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            int tmp = src.get(i);
            des.add(tmp);
        }
        return des;
    }

    void convertToMatrixHV(List<int[][]> tmpList, List<List<Integer>> values, int index) {
        int cur = 0;

        foo:
        for (int i = 0; i < values.size(); i++) {
            int[][] matrix = new int[3][3];
            copySmallMatrix(boardS.get(index).block, matrix);
            cur = 0;
            for (int j = 0; j < 3; j++) {
                for (int c = 0; c < 3; c++) {
                    if (matrix[j][c] == 0) {
                        matrix[j][c] = values.get(i).get(cur);
                        cur++;
                    }
                }
            }
            tmpList.add(matrix);
        }
    }

    void copySmallMatrix(int[][] srcMatrix, int[][] desMatrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                desMatrix[i][j] = srcMatrix[i][j];
            }
        }
    }

    public void getAvaiableValues(List<List<Integer>> listAvaiableValue) {
        int[] tmp = new int[9];
        int k = 0;

        while (true) {
            if (k >= 9) {
                return;
            }

            List<Integer> check = generateCheckList();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int value = boardS.get(k).block[i][j];
                    removeValue(check, value);
                }
            }

            listAvaiableValue.add(check);
            k++;
        }
    }

    private void removeValue(List<Integer> check, int key) {
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i) == key) {
                check.remove(i);
            }
        }
    }

    public List<Integer> generateCheckList() {
        List<Integer> check = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            check.add(i);
        }

        return check;
    }


    public void SudokuSolver(String filename) {
        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        sudokuGenerator.writePuzzle(boardS, filename);
    }

    public boolean solve(int level) throws InterruptedException {
        listMatrix = new ArrayList<>();
        boardS = new ArrayList<>();

        int[] saveIndex = new int[9];
        for (int i = 0; i < 9; i++)
            saveIndex[i] = -1;

        boolean check = false;
        int index = 0, cur = 0, countSuccess = 0;
        boolean repeat = false;
        int[][] matrix9x9 = new int[9][9];
        listPermution = new ArrayList<>();

        convertToSmallMatrix(matrix9x9First);
        generatePermute();

        if(level == -1){
            level = 0;
            repeat = true;
        }

        foo:
        while (true){
            if (level == 0 && cur >= listPermution.get(0).size() && listPermution.get(0).size() != 0 && repeat){
                return true;
            }

            if (level == nBlock){
                if(repeat){
                    countSuccess++;
                    if(countSuccess == 2){
                        return false;
                    }

                    for (int i = saveIndex.length - 1; i >= 0; i--){
                        if (saveIndex[i] < listPermution.get(i).size() - 1){
                            level = i;
                            index = i;
                            cur = saveIndex[i] + 1;
                            continue foo;
                        }
                    }

                    return true;
                }else {
                    return true;
                }
            }

            matrix9x9 = new int[9][9];
            if (level < 0){
                return false;
            }

            if(listPermution.get(index).size() == 0){
                if (check){
                    copySmallMatrix(listMatrix.get(index), boardS.get(index).block);
                    index --;
                    level --;
                    if (level < 0 && repeat){
                        return true;
                    }
                    cur = saveIndex[index];
                    check = false;
                    continue;
                }
                index ++;
                level ++;
                if (level >= 8){
                    return true;
                }
                cur = 0;
                continue;
            }

            if(cur >= listPermution.get(index).size() && level > 0 && listPermution.get(index).size() != 0){
                copySmallMatrix(listMatrix.get(index), boardS.get(index).block);
                index --;
                level --;
                if (level < 0 && repeat)
                    return true;
                cur = saveIndex[index] + 1;
                check = true;
                continue;
            }

            saveIndex[index] = cur;
            if(listPermution.get(index).size() != 0){
                convertTo9x9(listPermution.get(index).get(cur), index, matrix9x9, boardS);
            }

            if(checkSudoku(matrix9x9)){
                index ++;
                cur = 0;
                level ++;
                continue;
            }

            cur++;
        }
    }

    public boolean checkSudoku(int[][] sudoku) { //boolean
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!isValid(sudoku, i, j, sudoku[i][j])) {//xac dinh o block thu may ?
                    return false;
                }
            }
        }

        return true;
    }

    boolean isValid(int[][] matrix, int row, int col, int value) {


        for (int c = 0; c < 9; c++) {
            if (c == col || matrix[row][c] == 0)
                continue;
            if (matrix[row][c] == value) {
                return false;
            }

        }

        for (int r = 0; r < 9; r++) {
            if (r == row || matrix[r][col] == 0)
                continue;
            if (matrix[r][col] == value){
                return false;
            }
        }

        return true;
    }
    
    void convertTo9x9(int[][] srcMatrix, int index, int[][] matrix, ArrayList<Block> boardS) {
        if(index != -1 && srcMatrix != null)
            copySmallMatrix(srcMatrix, boardS.get(index).block);

        int cout = 0, row = 0, col = 0;

        while (true) {
            if (col >= 9) {
                col = 0;
                row += 3;
            }

            if (row >= 9 || cout > 8) {
                break;
            }

            for (int i = row, x = 0; i < row + 3 && x < 3; i++, x++) {
                for (int j = col, y = 0; j < col + 3 && y < 3; j++, y++) {
                    matrix[i][j] = boardS.get(cout).block[x][y];
                }
            }
            cout++;
            col += 3;
        }
    }


    public static void main(String args[]) throws IOException, InterruptedException {
        SudokuSolver sol = new SudokuSolver();
        sol.readSudoku(args[0]);
        sol.solve(0);
        sol.SudokuSolver(args[1]);
    }
}
