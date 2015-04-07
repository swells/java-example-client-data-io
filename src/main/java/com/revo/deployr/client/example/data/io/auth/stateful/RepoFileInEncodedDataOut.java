/*
 * RepoFileInEncodedDataOut.java
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
package com.revo.deployr.client.example.data.io.auth.stateful;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.factory.*;
import com.revo.deployr.client.params.*;
import com.revo.deployr.client.auth.RAuthentication;
import com.revo.deployr.client.auth.basic.RBasicAuthentication;
import java.util.*;

import org.apache.log4j.Logger;

public class RepoFileInEncodedDataOut {

    private static Logger log = Logger.getLogger(RepoFileInEncodedDataOut.class);

    public static void main(String args[]) throws Exception {

        RClient rClient = null;
        RProject rProject = null;

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
             * Create a temporary project (R session).
             *
             * Optionally:
             * ProjectCreationOptions options =
             * new ProjectCreationOptions();
             *
             * Populate options as needed, then:
             *
             * rProject = rUser.createProject(options);
             */
            rProject = rUser.createProject();

            log.info("Created stateful temporary " +
                    "R session, rProject=" + rProject);

            /*
             * Create a ProjectExecutionOptions instance
             * to specify data inputs and output to the
             * execution of the repository-managed R script.
             *
             * This options object can be used to pass standard
             * execution model parameters on execution calls. All
             * fields are optional.
             *
             * See the Standard Execution Model chapter in the
             * Client Library Tutorial on the DeployR website for
             * further details.
             */
            ProjectExecutionOptions options =
                new ProjectExecutionOptions();

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
             * Request the retrieval of the "hip" data.frame and
             * two vector objects from the workspace following the
             * execution. The corresponding R objects are named as
             * follows:
             * 'hip', hipDim', 'hipNames'.
             */
            options.routputs =
                Arrays.asList("hip", "hipDim", "hipNames");

            /*
             * Execute a public analytics Web service as an authenticated
             * user based on a repository-managed R script:
             * /testuser/example-data-io/dataIO.R
             */
            RProjectExecution exec =
                    rProject.executeScript("dataIO.R",
                            "example-data-io", "testuser", null, options);

            log.info("Project script " +
                    "execution completed, rProjectExecution=" + exec);

            /*
             * Retrieve the requested R object data encodings from
             * the results of the script execution. 
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


        } catch (Exception ex) {
            log.warn("Unexpected runtime exception=" + ex);
        } finally {
            try {
                if (rProject != null) {
                    /*
                     * Close rProject before application exits.
                     */
                    rProject.close();
                }
            } catch (Exception fex) { }
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
