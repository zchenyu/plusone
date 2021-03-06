package recommend.util;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import java.util.*;
import recommend.util.WordIndex;

public class DocAlgebra {
    /* Returns a matrix with docs.size() rows and WordIndex.size() columns. */
    public static DoubleMatrix2D docsToMatrix(Map<Integer, Double>[] docs) {
        DoubleMatrix2D matrix = new SparseDoubleMatrix2D(docs.length, WordIndex.size());
        for (int i = 0; i < docs.length; ++i)
            for (Map.Entry<Integer, Double> e : docs[i].entrySet())
                matrix.set(i, e.getKey(), e.getValue());
        return matrix;
    }
}
