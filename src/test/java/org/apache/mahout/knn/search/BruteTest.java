/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.knn.search;

import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.knn.UpdatableSearcher;
import org.apache.mahout.knn.WeightedVector;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.MatrixSlice;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BruteTest extends AbstractSearchTest {
    private static Iterable<MatrixSlice> data;

    @Before
    public void fillData() {
        data = randomData();
    }

    @Override
    public Iterable<MatrixSlice> testData() {
        return data;
    }

    @Override
    public UpdatableSearcher getSearch(int n) {
        return new Brute(new EuclideanDistanceMeasure());
    }

    @Test
    public void testMatrixSearch() {
        Matrix m = new DenseMatrix(8, 3);
        for (int i = 0; i < 8; i++) {
            m.viewRow(i).assign(new double[]{0.125 * (i & 4), i & 2, i & 1});
        }

        Brute s = (Brute) getSearch(3);
        s.addAll(m);

        final List<List<WeightedVector>> searchResults = s.search(m, 3);
        for (List<WeightedVector> r : searchResults) {
            assertEquals(0, r.get(0).getWeight(), 1e-8);
            assertEquals(0.5, r.get(1).getWeight(), 1e-8);
            assertEquals(1, r.get(2).getWeight(), 1e-8);
        }
    }
}
