/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sysml.test.integration.functions.aggregate;

import org.apache.sysml.api.DMLScript;
import org.apache.sysml.api.DMLScript.RUNTIME_PLATFORM;
import org.apache.sysml.lops.LopProperties.ExecType;
import org.apache.sysml.runtime.matrix.data.MatrixValue.CellIndex;
import org.apache.sysml.test.integration.AutomatedTestBase;
import org.apache.sysml.test.integration.TestConfiguration;
import org.apache.sysml.test.utils.TestUtils;
import org.junit.Test;

import java.util.HashMap;

/**
 * Test the column standard deviations function, "colSds(X)".
 */
public class ColStdDevsTest extends AutomatedTestBase {

    private static final String TEST_NAME = "ColStdDevs";
    private static final String TEST_DIR = "functions/aggregate/";
    private static final String TEST_CLASS_DIR =
            TEST_DIR + ColStdDevsTest.class.getSimpleName() + "/";
    private static final String INPUT_NAME = "X";
    private static final String OUTPUT_NAME = "colStdDevs";

    private static final int rows = 1234;
    private static final int cols = 1432;
    private static final double sparsitySparse = 0.2;
    private static final double sparsityDense = 0.7;
    private static final double eps = Math.pow(10, -10);

    private enum Sparsity {EMPTY, SPARSE, DENSE}
    private enum DataType {MATRIX, ROWVECTOR, COLUMNVECTOR}

    @Override
    public void setUp() {
        TestUtils.clearAssertionInformation();
        TestConfiguration config = new TestConfiguration(TEST_CLASS_DIR, TEST_NAME);
        addTestConfiguration(TEST_NAME, config);
    }
    
    // Dense matrix
    @Test
    public void testColStdDevsDenseMatrixCP() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.MATRIX, ExecType.CP);
    }

    @Test
    public void testColStdDevsDenseMatrixSpark() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.MATRIX, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsDenseMatrixMR() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.MATRIX, ExecType.MR);
    }

    // Dense row vector
    @Test
    public void testColStdDevsDenseRowVectorCP() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.ROWVECTOR, ExecType.CP);
    }

    @Test
    public void testColStdDevsDenseRowVectorSpark() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.ROWVECTOR, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsDenseRowVectorMR() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.ROWVECTOR, ExecType.MR);
    }

    // Dense column vector
    @Test
    public void testColStdDevsDenseColVectorCP() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.COLUMNVECTOR, ExecType.CP);
    }

    @Test
    public void testColStdDevsDenseColVectorSpark() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.COLUMNVECTOR, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsDenseColVectorMR() {
        testColStdDevs(TEST_NAME, Sparsity.DENSE,  DataType.COLUMNVECTOR, ExecType.MR);
    }

    // Sparse matrix
    @Test
    public void testColStdDevsSparseMatrixCP() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.MATRIX, ExecType.CP);
    }

    @Test
    public void testColStdDevsSparseMatrixSpark() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.MATRIX, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsSparseMatrixMR() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.MATRIX, ExecType.MR);
    }

    // Sparse row vector
    @Test
    public void testColStdDevsSparseRowVectorCP() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.ROWVECTOR, ExecType.CP);
    }

    @Test
    public void testColStdDevsSparseRowVectorSpark() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.ROWVECTOR, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsSparseRowVectorMR() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.ROWVECTOR, ExecType.MR);
    }

    // Sparse column vector
    @Test
    public void testColStdDevsSparseColVectorCP() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.COLUMNVECTOR, ExecType.CP);
    }

    @Test
    public void testColStdDevsSparseColVectorSpark() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.COLUMNVECTOR, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsSparseColVectorMR() {
        testColStdDevs(TEST_NAME, Sparsity.SPARSE,  DataType.COLUMNVECTOR, ExecType.MR);
    }

    // Empty matrix
    @Test
    public void testColStdDevsEmptyMatrixCP() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.MATRIX, ExecType.CP);
    }

    @Test
    public void testColStdDevsEmptyMatrixSpark() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.MATRIX, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsEmptyMatrixMR() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.MATRIX, ExecType.MR);
    }

    // Empty row vector
    @Test
    public void testColStdDevsEmptyRowVectorCP() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.ROWVECTOR, ExecType.CP);
    }

    @Test
    public void testColStdDevsEmptyRowVectorSpark() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.ROWVECTOR, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsEmptyRowVectorMR() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.ROWVECTOR, ExecType.MR);
    }

    // Empty column vector
    @Test
    public void testColStdDevsEmptyColVectorCP() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.COLUMNVECTOR, ExecType.CP);
    }

    @Test
    public void testColStdDevsEmptyColVectorSpark() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.COLUMNVECTOR, ExecType.SPARK);
    }

    @Test
    public void testColStdDevsEmptyColVectorMR() {
        testColStdDevs(TEST_NAME, Sparsity.EMPTY,  DataType.COLUMNVECTOR, ExecType.MR);
    }

    /**
     * Test the column standard deviations function, "colSds(X)", on
     * dense/sparse matrices/vectors on the CP/Spark/MR platforms.
     *
     * @param testName The name of this test case.
     * @param sparsity Selection between empty, sparse, and dense data.
     * @param dataType Selection between a matrix, a row vector, and a
     *                 column vector.
     * @param platform Selection between CP/Spark/MR platforms.
     */
    private void testColStdDevs(String testName, Sparsity sparsity, DataType dataType,
                                ExecType platform) {
    	boolean sparkConfigOld = DMLScript.USE_LOCAL_SPARK_CONFIG;
    	RUNTIME_PLATFORM platformOld = setRuntimePlatform(platform);
    	if(shouldSkipTest())
			return;

        try {
            // Create and load test configuration
            getAndLoadTestConfiguration(testName);
            String HOME = SCRIPT_DIR + TEST_DIR;
            fullDMLScriptName = HOME + testName + ".dml";
            programArgs = new String[]{"-explain", "-stats", "-args",
                    input(INPUT_NAME), output(OUTPUT_NAME)};
            fullRScriptName = HOME + testName + ".R";
            rCmd = "Rscript" + " " + fullRScriptName + " " + inputDir() + " " + expectedDir();

            // Generate data
            // - sparsity
            double sparsityVal;
            switch (sparsity) {
                case EMPTY:
                    sparsityVal = 0;
                    break;
                case SPARSE:
                    sparsityVal = sparsitySparse;
                    break;
                case DENSE:
                default:
                    sparsityVal = sparsityDense;
            }
            // - size
            int r;
            int c;
            switch (dataType) {
                case ROWVECTOR:
                    r = 1;
                    c = cols;
                    break;
                case COLUMNVECTOR:
                    r = rows;
                    c = 1;
                    break;
                case MATRIX:
                default:
                    r = rows;
                    c = cols;
            }
            // - generation
            double[][] X = getRandomMatrix(r, c, -1, 1, sparsityVal, 7);
            writeInputMatrixWithMTD(INPUT_NAME, X, true);

            // Run DML and R scripts
            runTest(true, false, null, -1);
            runRScript(true);

            // Compare output matrices
            HashMap<CellIndex, Double> dmlfile = readDMLMatrixFromHDFS(OUTPUT_NAME);
            HashMap<CellIndex, Double> rfile  = readRMatrixFromFS(OUTPUT_NAME);
            TestUtils.compareMatrices(dmlfile, rfile, eps, "Stat-DML", "Stat-R");
        }
        finally {
            // Reset settings
            rtplatform = platformOld;
            DMLScript.USE_LOCAL_SPARK_CONFIG = sparkConfigOld;
        }
    }
}
