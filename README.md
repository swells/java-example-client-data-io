java-example-data-io
====================

Example use of data inputs and outputs when executing R code or R scripts on [DeployR-managed](http://deployr.revolutionanalytics.com) R sessions.

## About

These examples demonstrate how different types of data inputs and outputs
can be sent to and retrieved from DeployR-managed R sessions when executing R code or R scripts using the [DeployR client library](http://deployr.revolutionanalytics.com/dev).

DeployR supports the following types of data inputs:

1. [DeployR-encoded](http://deployr.revolutionanalytics.com/dev/encoding) application generated data (database, calculated, user input, etc) that can be decoded into the workspace of an R session when executing R code or an R script.
2. References to repository-managed binary data files (rData) that cause the corresponding file data to be loaded into the workspace of an R session when executing R code or an R script.
3. References to repository-managed data files (csv, dat, xls, etc) that cause the corresponding file data to be loaded into the working directory of an R session when executing R code or an R script.
4. References to external data sources (URL, Web services, database, etc) that can be used to load data directly into an R session when executing R code or an R script.

DeployR supports the following types of data outputs:

1. [DeployR-encoded](http://deployr.revolutionanalytics.com/dev/encoding) R object workspace data (data.frame, list, primitive, etc) that were generated by the execution of R code or an R script.
2. References to R session working directory binary files (rData, png, jpg, etc) that were generated by the execution of R code or an R script.
3. References to R session working directory data files (csv, dat, xls, etc) that were generated by the execution of R code or an R script.
4. References to R session graphics device plots (png, jpg, svg, etc) that were generated by the execution of R code or an R script.
5. References to repository-managed data or binary files that were stored following the execution of R code or an R script.


## Authenticated Users & Data I/O

Authenticated (trusted) users can execute both R code and repository-managed R scripts that they themselves have created or R scripts that have been shared by other authenticated (trusted) users.

Authenticated users can use the full set of data inputs and outputs indicated above.

### Authenticated Discrete Execution Data I/O Examples

```
Source: src/main/java/com/revo/deployr/client/example/data/io/auth/discrete/exec/*
```

These examples demonstrate data I/O on discrete executions of R scripts by authenticated users on stateless projects.

| Example Source File | Example Data Input | Example Data Output |
| ------------------- | ------------------ | ------------------- |
| EncodedDataInBinaryFileOut | DeployR-encoded application data | Working directory binary file |
| RepoFileInDataEncodedOut | Reference to repository-managed binary file | DeployR-encoded workspace data |
| RepoFileInGraphicsPlotOut | Reference to repository-managed data file | Graphics device generated plot | 
| ExternalDataInDataFileOut | Reference to external data source | Working directory data file |
| RepoFileInRepoFileOut | DeployR-encoded application data | Reference to stored repository-managed file |
| MultipleDataInMultipleDataOut | Multilple data inputs | Multiple data outputs |

Use the DeployR CLI to download and run the examples.

### Authenticated Stateful Execution Data I/O Examples

```
Source: src/main/java/com/revo/deployr/client/example/data/io/auth/stateful/exec/*
```

These examples demonstrate data I/O on executions of R code and R scripts by authenticated users on stateful projects.

| Example Source File | Example Data Input | Example Data Output |
| ------------------- | ------------------ | ------------------- |
| EncodedDataInBinaryFileOut | DeployR-encoded application data | Working directory binary file |
| RepoFileInDataEncodedOut | Reference to repository-managed binary file | DeployR-encoded workspace data |
| RepoFileInGraphicsPlotOut | Reference to repository-managed data file | Graphics device generated plot | 
| ExternalDataInDataFileOut | Reference to external data source | Working directory data file |
| RepoFileInRepoFileOut | DeployR-encoded application data | Reference to stored repository-managed file |
| MultipleDataInMultipleDataOut | Multilple data inputs | Multiple data outputs |

Use the DeployR CLI to download and run the examples.

### Authenticated Stateful Preload & Execution Data I/O Examples

```
Source: src/main/java/com/revo/deployr/client/example/data/io/auth/stateful/preload/*
```

These examples demonstrate data I/O on project initialization, prior to executions of R code and R scripts by authenticated users on stateful projects.

| Example Source File | Example Data Input | Example Data Output |
| ------------------- | ------------------ | ------------------- |
| EncodedDataInBinaryFileOut | DeployR-encoded application data | Working directory binary file |
| RepoFileInDataEncodedOut | Reference to repository-managed binary file | DeployR-encoded workspace data |
| RepoFileInGraphicsPlotOut | Reference to repository-managed data file | Graphics device generated plot | 
| ExternalDataInDataFileOut | Reference to external data source | Working directory data file |
| RepoFileInRepoFileOut | DeployR-encoded application data | Reference to stored repository-managed file |
| MultipleDataInMultipleDataOut | Multilple data inputs | Multiple data outputs |

Use the DeployR CLI to download and run the examples.

## Anonymous Users & Data I/O

Anonymous (untrusted) users can only execute repository-managed R scripts that have been assigned _public_ access privileges by authenticated (trusted) users.

Anonymous users can use the full set of data inputs and outputs indicated above with a single exception: anonymous users can not cause data or binary files to be stored into the DeployR-repository and as such can not retrieve references to repository-managed data or binary files following an execution.

### Anonymous Discrete Execution Data I/O Examples

```
Source: src/main/java/com/revo/deployr/client/example/data/io/anon/discrete/exec/*.java
```

These examples demonstrate data I/O on discrete executions of R scripts by anonymous users on stateless projects.

| Example Source File | Example Data Input | Example Data Output |
| ------------------- | ------------------ | ------------------- |
| EncodedDataInBinaryFileOut | DeployR-encoded application data | Working directory binary file |
| RepoFileInDataEncodedOut | Reference to repository-managed binary file | DeployR-encoded workspace data |
| RepoFileInGraphicsPlotOut | Reference to repository-managed data file | Graphics device generated plot | 
| ExternalDataInDataFileOut | Reference to external data source | Working directory data file |
| MultipleDataInMultipleDataOut | Multilple data inputs | Multiple data outputs |

Use the DeployR CLI to download and run the examples.

## Example R Analytics

```
Source: analytics/*
```

Some of the examples use a sample data file based on the Hipparcos Star
Dataset, found [here](http://astrostatistics.psu.edu/datasets/HIP_star.html).

This data file is found here:

```
analytics/hipStar.dat
```

Some of the examples use a sample binary file containing an R data.frame based
on the Hipparcos Star Dataset, found [here](http://astrostatistics.psu.edu/datasets/HIP_star.html).

This binary file is found here:

```
analytics/hipStar.rData
```

All of the examples use a sample R script to handle inputs and generate outputs each time the script is executed.

This R script is found here:

```
analytics/dataIO.R
```

The R scripts and data files used by these example application are
*not bundled* by default within the DeployR 7.4 repository.

The simplest way to deploy these files for use when running these
example applications is to use the [DeployR CLI](https://github.com/deployr/cli).
The CLI will automatically deploy the file dependencies to the DeployR
repository before running the examples.

If for any reason you want to manually deploy these files for testing
purposes you can use the DeployR Repository Manager as follows:

1. Log in as testuser into the Repository Manager
2. Create a new repository directory called example-data-io
3. Upload analytics/hipStar.dat to the example-data-io
   directory and set the access control to _public_.
4. Upload analytics/hipStar.rData to the example-data-io directory
   directory and set the access control to _public_.
5. Upload analytics/dataIO.R to the example-data-io directory
   directory and set the access control to _public_.


## License ##

Copyright (C) 2010-2015 by Revolution Analytics Inc.

This program is licensed to you under the terms of Version 2.0 of the
Apache License. This program is distributed WITHOUT
ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more 
details.
