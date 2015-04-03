/*
 * RepoDataFileInput.java
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
package com.revo.deployr.client.example.data.io.auth.discrete;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.factory.*;
import com.revo.deployr.client.params.*;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import java.util.*;

import org.apache.log4j.Logger;

public class RepoDataFileInput {

    private static Logger log = Logger.getLogger(RepoDataFileInput.class);

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
             * Build a basic authentication token.
             */
            RAuthentication rAuth =
                    new RBasicAuthentication(System.getProperty("username"),
                            System.getProperty("password"));

            /*
             * Establish an authenticated handle with the DeployR
             * server, rUser. Following this call the rClient 
             * connection is operating as an authenticated connection
             * and all calls on rClient inherit the access permissions
             * of the authenticated user, rUser.
             */
            RUser rUser = rClient.login(rAuth);
            log.info("Upgraded to authenticated " +
                    "connection, rUser=" + rUser);

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
             * Preload from the DeployR repository the following
             * data input file:
             * /testuser/example-data-io/hipStar.dat
             */
            ProjectPreloadOptions preloadDirectory =
                                new ProjectPreloadOptions();
            preloadDirectory.filename = "hipStar.dat";
            preloadDirectory.directory = "example-data-io";
            preloadDirectory.author = "testuser";
            options.preloadDirectory = preloadDirectory;
            options.blackbox = true;

            log.info("Data input file set for preload, " +
                                            preloadDirectory);

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

}
