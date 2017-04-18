package com.uet.sudoku.block3x3;

import java.util.ArrayList;
import java.util.Random;

public class Block {
    int[][] block;
    public static final int n = 3;

    public Block() {
        block = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                block[i][j] = 0;
    }

    public static ArrayList<Block> generateBlock(ArrayList<int[]> a) {
        ArrayList localArrayList = new ArrayList();
        int[][] arrayOfInt1 = createBaseMatrix();
        int[][] arrayOfInt2 = createBaseMatrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
            {
                Block localBlock = new Block();
                localBlock.block = calculateMatrix(arrayOfInt1[i][j], arrayOfInt2);
                localArrayList.add(localBlock);
            }
        }
        return localArrayList;
    }

    private static int[][] calculateMatrix(int paramInt, int[][] paramArrayOfInt)
    {
        int[][] arrayOfInt = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
            {
                int k = paramInt * 3 + 1 + paramArrayOfInt[i][j];
                arrayOfInt[i][j] = k;
            }
        }
        return arrayOfInt;
    }

    private static int[][] createBaseMatrix()
    {
        int[][] arrayOfInt = new int[3][3];
        Random localRandom = new Random();
        int i = localRandom.nextInt(3);
        arrayOfInt[0][i] = 1;
        if (i == 2) {
            arrayOfInt[0][0] = 2;
        } else {
            arrayOfInt[0][(i + 1)] = 2;
        }
        for (int j = 1; j < 3; j++) {
            for (int k = 0; k < 2; k++)
            {
                arrayOfInt[j][k] = arrayOfInt[(j - 1)][(k + 1)];
                if (k + 1 == 2) {
                    arrayOfInt[j][(k + 1)] = arrayOfInt[(j - 1)][(k - 1)];
                }
            }
        }
        return arrayOfInt;
    }

    public boolean checkCol(int id) {
        return false;
    }

    public boolean checkRow(int id) {
        return false;
    }

}
