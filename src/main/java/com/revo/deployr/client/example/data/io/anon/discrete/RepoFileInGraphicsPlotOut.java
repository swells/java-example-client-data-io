/*
 * RepoFileInGraphicsPlotOut.java
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

public class RepoFileInGraphicsPlotOut {

    private static Logger log = Logger.getLogger(RepoFileInGraphicsPlotOut.class);

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

            log.info("Data file input set on execution, " +
                                            preloadDirectory);

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
             * Retrieve R graphics device plot (result) called
             * unnamedplot*.png that was generated by the execution.
             *
             * Outputs generated by an execution can be used in any
             * number of ways by client applications, including:
             *
             * 1. Use output data to perform further calculations.
             * 2. Display output data to an end-user.
             * 3. Write output data to a database.
             * 4. Pass output data along to another Web service.
             * 5. etc.
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
