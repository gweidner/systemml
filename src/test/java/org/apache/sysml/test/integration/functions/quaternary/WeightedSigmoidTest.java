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

package org.apache.sysml.test.integration.functions.quaternary;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.sysml.api.DMLScript;
import org.apache.sysml.api.DMLScript.RUNTIME_PLATFORM;
import org.apache.sysml.hops.OptimizerUtils;
import org.apache.sysml.hops.QuaternaryOp;
import org.apache.sysml.lops.LopProperties.ExecType;
import org.apache.sysml.lops.WeightedSigmoid;
import org.apache.sysml.runtime.matrix.MatrixCharacteristics;
import org.apache.sysml.runtime.matrix.data.MatrixValue.CellIndex;
import org.apache.sysml.test.integration.AutomatedTestBase;
import org.apache.sysml.test.integration.TestConfiguration;
import org.apache.sysml.test.utils.TestUtils;
import org.apache.sysml.utils.Statistics;

/**
 * 
 * 
 */
public class WeightedSigmoidTest extends AutomatedTestBase 
{
	
	private final static String TEST_NAME1 = "WeightedSigmoidP1";
	private final static String TEST_NAME2 = "WeightedSigmoidP2";
	private final static String TEST_NAME3 = "WeightedSigmoidP3";
	private final static String TEST_NAME4 = "WeightedSigmoidP4";

	private final static String TEST_DIR = "functions/quaternary/";
	private final static String TEST_CLASS_DIR = TEST_DIR + WeightedSigmoidTest.class.getSimpleName() + "/";
	
	private final static double eps = 1e-10;
	
	private final static int rows = 1201;
	private final static int cols = 1103;
	private final static int rank = 10;
	private final static double spSparse = 0.001;
	private final static double spDense = 0.6;
	
	@Override
	public void setUp() 
	{
		TestUtils.clearAssertionInformation();
		addTestConfiguration(TEST_NAME1,new TestConfiguration(TEST_CLASS_DIR, TEST_NAME1,new String[]{"R"}));
		addTestConfiguration(TEST_NAME2,new TestConfiguration(TEST_CLASS_DIR, TEST_NAME2,new String[]{"R"}));
		addTestConfiguration(TEST_NAME3,new TestConfiguration(TEST_CLASS_DIR, TEST_NAME3,new String[]{"R"}));
		addTestConfiguration(TEST_NAME4,new TestConfiguration(TEST_CLASS_DIR, TEST_NAME4,new String[]{"R"}));

		if (TEST_CACHE_ENABLED) {
			setOutAndExpectedDeletionDisabled(true);
		}
	}

	@BeforeClass
	public static void init()
	{
		TestUtils.clearDirectory(TEST_DATA_DIR + TEST_CLASS_DIR);
	}

	@AfterClass
	public static void cleanUp()
	{
		if (TEST_CACHE_ENABLED) {
			TestUtils.clearDirectory(TEST_DATA_DIR + TEST_CLASS_DIR);
		}
	}
	
	@Test
	public void testSigmoidDenseBasicNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, false, ExecType.CP);
	}
	
	@Test
	public void testSigmoidDenseLogNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, false, ExecType.CP);
	}
	
	@Test
	public void testSigmoidDenseMinusNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, false, ExecType.CP);
	}
	
	@Test
	public void testSigmoidDenseLogMinusNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, false, ExecType.CP);
	}

	@Test
	public void testSigmoidSparseBasicNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, false, ExecType.CP);
	}
	
	@Test
	public void testSigmoidSparseLogNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, false, ExecType.CP);
	}
	
	@Test
	public void testSigmoidSparseMinusNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, false, ExecType.CP);
	}
	
	@Test
	public void testSigmoidSparseLogMinusNoRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, false, ExecType.CP);
	}

	@Test
	public void testSigmoidDenseBasicNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, false, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseLogNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, false, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseMinusNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, false, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseLogMinusNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, false, ExecType.MR);
	}

	@Test
	public void testSigmoidSparseBasicNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, false, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseLogNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, false, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseMinusNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, false, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseLogMinusNoRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, false, ExecType.MR);
	}
	
	//with rewrites

	@Test
	public void testSigmoidDenseBasicRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, true, ExecType.CP);
	}
	
	@Test
	public void testSigmoidDenseLogRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, true, ExecType.CP);
	}
	
	@Test
	public void testSigmoidDenseMinusRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, true, ExecType.CP);
	}
	
	@Test
	public void testSigmoidDenseLogMinusRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, true, ExecType.CP);
	}

	@Test
	public void testSigmoidSparseBasicRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, true, ExecType.CP);
	}
	
	@Test
	public void testSigmoidSparseLogRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, true, ExecType.CP);
	}
	
	@Test
	public void testSigmoidSparseMinusRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, true, ExecType.CP);
	}
	
	@Test
	public void testSigmoidSparseLogMinusRewritesCP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, true, ExecType.CP);
	}

	@Test
	public void testSigmoidDenseBasicRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseLogRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseMinusRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseLogMinusRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, true, ExecType.MR);
	}

	@Test
	public void testSigmoidSparseBasicRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseLogRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseMinusRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseLogMinusRewritesMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, true, ExecType.MR);
	}

	@Test
	public void testSigmoidDenseBasicRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseLogRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseMinusRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseLogMinusRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, true, ExecType.SPARK);
	}

	@Test
	public void testSigmoidSparseBasicRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidSparseLogRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidSparseMinusRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidSparseLogMinusRewritesSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, true, ExecType.SPARK);
	}
	
	
	//the following tests force the replication based mr operator because
	//otherwise we would always choose broadcasts for this small input data
	
	@Test
	public void testSigmoidSparseBasicRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseLogRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseMinusRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidSparseLogMinusRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseBasicRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseLogRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseMinusRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, true, true, ExecType.MR);
	}
	
	@Test
	public void testSigmoidDenseLogMinusRewritesRepMR() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, true, true, ExecType.MR);
	}
	

	@Test
	public void testSigmoidSparseBasicRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, true, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidSparseLogRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, true, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidSparseMinusRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, true, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidSparseLogMinusRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, true, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseBasicRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME1, false, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseLogRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME2, false, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseMinusRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME3, false, true, true, ExecType.SPARK);
	}
	
	@Test
	public void testSigmoidDenseLogMinusRewritesRepSP() 
	{
		runMLUnaryBuiltinTest(TEST_NAME4, false, true, true, ExecType.SPARK);
	}
	
	
	/**
	 * 
	 * @param testname
	 * @param sparse
	 * @param rewrites
	 * @param instType
	 */
	private void runMLUnaryBuiltinTest( String testname, boolean sparse, boolean rewrites, ExecType instType)
	{
		runMLUnaryBuiltinTest(testname, sparse, rewrites, false, instType);
	}
	
	/**
	 * 
	 * @param sparseM1
	 * @param sparseM2
	 * @param instType
	 */
	private void runMLUnaryBuiltinTest( String testname, boolean sparse, boolean rewrites, boolean rep, ExecType instType)
	{
		boolean sparkConfigOld = DMLScript.USE_LOCAL_SPARK_CONFIG;
		RUNTIME_PLATFORM platformOld = setRuntimePlatform(instType);
		if(shouldSkipTest())
			return;

		boolean rewritesOld = OptimizerUtils.ALLOW_ALGEBRAIC_SIMPLIFICATION;
		boolean forceOld = QuaternaryOp.FORCE_REPLICATION;
		
		OptimizerUtils.ALLOW_ALGEBRAIC_SIMPLIFICATION = rewrites;
		QuaternaryOp.FORCE_REPLICATION = rep;
	    
		try
		{
			double sparsity = (sparse) ? spSparse : spDense;
			String TEST_NAME = testname;
			
			TestConfiguration config = getTestConfiguration(TEST_NAME);

			String TEST_CACHE_DIR = "";
			if (TEST_CACHE_ENABLED)
			{
				TEST_CACHE_DIR = TEST_NAME + "_" + sparsity + "/";
			}
			
			loadTestConfiguration(config, TEST_CACHE_DIR);
			
			// This is for running the junit test the new way, i.e., construct the arguments directly
			String HOME = SCRIPT_DIR + TEST_DIR;
			fullDMLScriptName = HOME + TEST_NAME + ".dml";
			programArgs = new String[]{"-stats", "-explain", "runtime", "-args", 
				input("X"), input("U"), input("V"), input("W"), output("R") };
			
			fullRScriptName = HOME + TEST_NAME + ".R";
			rCmd = "Rscript" + " " + fullRScriptName + " " + inputDir() + " " + expectedDir();
	
			//generate actual dataset 
			double[][] X = getRandomMatrix(rows, cols, 0, 1, sparsity, 7); 
			writeInputMatrixWithMTD("X", X, true);
			double[][] U = getRandomMatrix(rows, rank, 0, 1, 1.0, 213); 
			writeInputMatrixWithMTD("U", U, true);
			double[][] V = getRandomMatrix(cols, rank, 0, 1, 1.0, 312); 
			writeInputMatrixWithMTD("V", V, true);
			
			runTest(true, false, null, -1); 
			runRScript(true); 
			
			//compare matrices 
			HashMap<CellIndex, Double> dmlfile = readDMLMatrixFromHDFS("R");
			HashMap<CellIndex, Double> rfile  = readRMatrixFromFS("R");
			TestUtils.compareMatrices(dmlfile, rfile, eps, "Stat-DML", "Stat-R");
			checkDMLMetaDataFile("R", new MatrixCharacteristics(rows,cols,1,1));

			//check statistics for right operator in cp
			if( instType == ExecType.CP && rewrites )
				assertTrue(Statistics.getCPHeavyHitterOpCodes().contains(WeightedSigmoid.OPCODE_CP));
		}
		finally
		{
			rtplatform = platformOld;
			DMLScript.USE_LOCAL_SPARK_CONFIG = sparkConfigOld;
			OptimizerUtils.ALLOW_ALGEBRAIC_SIMPLIFICATION = rewritesOld;
			QuaternaryOp.FORCE_REPLICATION = forceOld;
		}
	}	
}
