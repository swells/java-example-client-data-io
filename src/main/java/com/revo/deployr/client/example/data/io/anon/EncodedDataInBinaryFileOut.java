/*
 * GeneratedAppDataInput.java
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
package com.revo.deployr.client.example.data.io.anon;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.factory.*;
import com.revo.deployr.client.params.*;
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;

public class GeneratedAppDataInput {

    private static Logger log = Logger.getLogger(GeneratedAppDataInput.class);
    /*
     * Hipparcos star dataset URL endpoint.
     */

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
             * Simulate application generated data. This data
             * is first encoded using the RDataFactory before
             * being passed as an input on the execution.
             *
             * This encoded R input is automatically converted
             * into a workspace object before script execution.
             */
            RData generatedData = simulateGeneratedData();
            if(generatedData != null) {
                List<RData> rinputs = Arrays.asList(generatedData);
                options.rinputs = rinputs;
            }

            log.info("R object data.frame input set on execution, " +
                                                    generatedData);

            /*
             * Request the retrieval of two vector objects and a 
             * data.frame from the workspace following the execution.
             * The corresponding R objects are named as follows:
             * 'hipDim', 'hipNames', 'hipSubset'.
             */
            options.routputs =
                Arrays.asList("hipDim", "hipNames", "hipSubset");

            /*
             * Execute a public analytics Web service as an anonymous
             * user based on a repository-managed R script:
             * /testuser/example-data-io/hipStar.R
             */
            RScriptExecution exec =
                    rClient.executeScript("hipStar.R",
                            "example-data-io", "testuser", null, options);

            log.info("Script execution completed, " +
                                        "rScriptExecution=" + exec);

            /*
             * Retrieve the requested R object data encodings from
             * the results of the script execution. 
             *
             * See the R Object Data Decoding chapter in the
             * Client Library Tutorial on the DeployR website for
             * further details.
             */
            String console = exec.about().console;
            List<RData> objects = exec.about().workspaceObjects;

            for(RData rData : objects) {
                log.info("Encoded R object " +
                    rData.getName() + " returned, class=" + rData);
                if(rData instanceof RNumericVector) {
                    List<Double> hipDimVal =
                        ((RNumericVector) rData).getValue();
                    log.info("Encoded R object, hipDim=" + hipDimVal);
                } else
                if(rData instanceof RStringVector) {
                    List<String> hipNamesVal =
                        ((RStringVector) rData).getValue();
                    log.info("Encoded R object, hipNames=" + hipNamesVal);
                } else
                if(rData instanceof RDataFrame) {
                    List<RData> hipSubsetVal =
                        ((RDataFrame) rData).getValue();
                    // log.info("Encoded R object, hipSubset=" + hipSubsetVal);
                } else {
                    log.info("Unexpected R object data type returned, " +
                        "object name=" + rData.getName() + ", encoding=" +
                                                        rData.getClass());
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

    /*
     * simulateGeneratedData
     *
     * This method is used to generate sample data within the
     * application. In a real-world application this data may 
     * originate from any number of sources, for example:
     *
     * - Data read from a database
     * - Data read from an external Web service
     * - Data read from direct user input
     * - Data generated in real time by the application itself
     *
     * This data is then encoded using the RDataFactory and then
     * passed as an input the execution.
     *
     */
    private static RData simulateGeneratedData() {

        RData df = null;

        try {

            String[] COLUMN_NAMES = null;

            List[] dataLists = new List[] { 
                new ArrayList(), new ArrayList(), new ArrayList(),
                new ArrayList(), new ArrayList(), new ArrayList(),
                new ArrayList(), new ArrayList(), new ArrayList()
            };

            InputStream inputStream =
                new URL("http://astrostatistics.psu.edu/datasets/HIP_star.dat").openStream();

            BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream));
            boolean headerSkipped = false;
            String rowData = null;

            while((rowData = reader.readLine()) != null) {


                if(headerSkipped) {

                    String[] colData = rowData.trim().split("\\s+");

                    for(String cellData : colData) {

                        for(int i=0; i < dataLists.length; i++) {
                            dataLists[i].add(Double.parseDouble(cellData));
                        }
                    }

                } else {
                    COLUMN_NAMES = rowData.trim().split("\\s+");
                    headerSkipped = true;
                }

            }

            List<RData> dfValues = new ArrayList<RData>();

            for(int j=0; j<dataLists.length; j++) {

                RData numVector =
                    RDataFactory.createNumericVector(COLUMN_NAMES[j],
                                        dataLists[j]);
                dfValues.add(numVector);
            }

            if(dfValues.size() > 0) {
                df = RDataFactory.createDataFrame("hip", dfValues);
            }
                

        } catch(Exception ex) {
            log.warn("Simulate generated data failed, ex=" + ex);
        } finally {
            return df;
        }
    }

}
