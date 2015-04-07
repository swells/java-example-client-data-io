/*
 * MultipleDataInMultipleDataOut.java
 *
 * Copyright (C) 2010-2015 by Revolution Analytics Inc.
 *
 * This program is licensed to you under the terms of Version 2.0 of the
 * Apache License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more details.
 *
 */
package com.revo.deployr.client.example.data.io.anon.discrete;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.factory.*;
import com.revo.deployr.client.params.*;
import java.util.*;
import java.io.*;
import java.net.*;
import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;

public class MultipleDataInMultipleDataOut {

    private static Logger log = Logger.getLogger(MultipleDataInMultipleDataOut.class);

    /*
     * Hipparcos star dataset URL endpoint.
     */
    private static String HIP_DAT_URL =
        "http://astrostatistics.psu.edu/datasets/HIP_star.dat";

    public static void main(String args[]) throws Exception {

        RClient rClient = null;

        try {

            /*
             * Determine DeployR server endpoint.
             */
            String endpoint = System.getProperty("endpoint");
            log.info("Using endpoint=" + endpoint);

            /*
             * Establish RClient connection to DeployR server.
             *
             * An RClient connection is the mandatory starting
             * point for any application using the client library.
             */
            rClient = RClientFactory.createClient(endpoint);

            log.info("Established anonymous " +
                    "connection, rClient=" + rClient);

            /*
             * Create the AnonymousProjectExecutionOptions object·
             * to specify data inputs and output to the script.
             *
             * This options object can be used to pass standard
             * execution model parameters on execution calls. All
             * fields are optional.
             *
             * See the Standard Execution Model chapter in the
             * Client Library Tutorial on the DeployR website for
             * further details.
             */
            AnonymousProjectExecutionOptions options =
                    new AnonymousProjectExecutionOptions();

            /*
             * MultipleDataInMultipleDataOut Example Note:
             * 
             * The inputs sent on this example are contrived
             * and superfluous as the hipStar.rData binary R
             * object input and the hipStarUrl input perform
             * the exact same purpose...to load the Hip STAR
             * dataset into the workspace ahead of execution.
             * 
             * The example is provided to simply demonstrate
             * the mechanism of specifying multiple inputs.
             */

            /* 
             * Preload from the DeployR repository the following
             * binary R object input file:
             * /testuser/example-data-io/hipStar.rData
             *
             * As this is an anonymous operation "hipStar.rData"
             * must have it's repository-managed access controls
             * set to "public".
             */
            ProjectPreloadOptions preloadWorkspace =
                                new ProjectPreloadOptions();
            preloadWorkspace.filename = "hipStar.rData";
            preloadWorkspace.directory = "example-data-io";
            preloadWorkspace.author = "testuser";
            options.preloadWorkspace = preloadWorkspace;

            log.info("Binary file input set on execution, " +
                                            preloadWorkspace);

            /* 
             * Load an R object literal "hipStarUrl" into the
             * workspace prior to script execution.
             *
             * The R script checks for the existence of "hipStarUrl"
             * in the workspace and if present uses the URL path
             * to load the Hipparcos star dataset from the DAT file
             * at that location.
             */
            RData hipStarUrl =
                RDataFactory.createString("hipStarUrl", HIP_DAT_URL);
            List<RData> rinputs = Arrays.asList(hipStarUrl);
            options.rinputs = rinputs;

            log.info("External data source input set on execution, " +
                                                    hipStarUrl);

            /*
             * Request the retrieval of the "hip" data.frame and
             * two vector objects from the workspace following the
             * execution. The corresponding R objects are named as
             * follows:
             * 'hip', hipDim', 'hipNames'.
             */
            options.routputs =
                Arrays.asList("hip", "hipDim", "hipNames");

            /*
             * Execute a public analytics Web service as an anonymous
             * user based on a repository-managed R script:
             * /testuser/example-data-io/dataIO.R
             */
            RScriptExecution exec =
                    rClient.executeScript("dataIO.R",
                            "example-data-io", "testuser", null, options);

            log.info("R script execution completed, " +
                                        "rScriptExecution=" + exec);

            /*
             * Retrieve multiple outputs following the execution:
             *
             * 1. R console output.
             * 2. R console output.
             * 3. R console output.
             * 4. R console output.
             * 5. R console output.
             */

            String console = exec.about().console;
            log.info("Retrieved R console output.");

            /*
             * Retrieve the requested R object data encodings from
             * the workspace follwing the script execution. 
             *
             * See the R Object Data Decoding chapter in the
             * Client Library Tutorial on the DeployR website for
             * further details.
             */
            List<RData> objects = exec.about().workspaceObjects;

            for(RData rData : objects) {
                log.info("Retrieved DeployR-encoded output " +
                    rData.getName() + ", class=" + rData);
                if(rData instanceof RDataFrame) {
                    List<RData> hipSubsetVal =
                        ((RDataFrame) rData).getValue();
                } else
                if(rData instanceof RNumericVector) {
                    List<Double> hipDimVal =
                        ((RNumericVector) rData).getValue();
                    log.info("Retrieved DeployR-encoded output " +
                        rData.getName() + ", value=" + hipDimVal);
                } else
                if(rData instanceof RStringVector) {
                    List<String> hipNamesVal =
                        ((RStringVector) rData).getValue();
                    log.info("Retrieved DeployR-encoded output " +
                        rData.getName() + ", value=" + hipNamesVal);
                } else {
                    log.info("Unexpected DeployR-encoded data type returned, " +
                        "object name=" + rData.getName() + ", encoding=" +
                                                        rData.getClass());
                }
            }

            /*
             * Retrieve the working directory files (artifact)
             * was generated by the execution.
             */
            List<RProjectFile> wdFiles = exec.about().artifacts;

            for(RProjectFile wdFile : wdFiles) {
                log.info("Retrieved working directory " +
                    "binary file output " + wdFile.about().filename +
                    ", rProjectFile=" + wdFile);

                InputStream fis = null;
                try { fis = wdFile.download(); } catch(Exception ex) {
                    log.warn("Working directory binary file " + ex);
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }

            /*
             * Retrieve R graphics device plots (results) called
             * unnamedplot*.png that was generated by the execution.
             */
            List<RProjectResult> results = exec.about().results;

            for(RProjectResult result : results) {
                log.info("Retrieved graphics device " +
                    "plot output " + result.about().filename +
                    ", rProjectResult=" + result);

                InputStream fis = null;
                try { fis = result.download(); } catch(Exception ex) {
                    log.warn("Graphics device plot " + ex);
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }

        } catch (Exception ex) {
            log.warn("Unexpected runtime exception=" + ex);
        } finally {
            try {
                if (rClient != null) {
                    /*
                     * Release rClient connection before application exits.
                     */
                    rClient.release();
                }
            } catch (Exception fex) {
            }
        }

    }

}
