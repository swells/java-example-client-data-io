## Tutorial: Anonymous Discrete Execution Data I/O

```
Source: src/main/java/com/revo/deployr/client/example/data/io/anon/discrete/exec/*.java
```

## Tutorial: Summary

These examples demonstrate data I/O on discrete executions of R scripts by anonymous users on stateless projects.

| Example Source File | Example Data Input | Example Data Output |
| ------------------- | ------------------ | ------------------- |
| EncodedDataInBinaryFileOut | DeployR-encoded application data | Working directory binary file |
| RepoFileInEncodedDataOut | Reference to repository-managed binary file | DeployR-encoded workspace data |
| RepoFileInGraphicsPlotOut | Reference to repository-managed data file | Graphics device generated plot | 
| ExternalDataInDataFileOut | Reference to external data source | Working directory data file |
| MultipleDataInMultipleDataOut | Multilple data inputs | Multiple data outputs |


## Tutorial: Running the Examples

Use the DeployR CLI to download and run the examples.


## Tutorial: Example-by-Example

### 1. EncodedDataInBinaryFileOut

```
Example: com/revo/deployr/client/example/data/io/anon/discrete/exec/EncodedDataInBinaryFileOut.java
```

The following table describes the steps and the associated log output for this example:

| Step          | Log Output                                   |
| --------------| ---------------------------------------------|
| CONFIGURATION | Using endpoint=http://localhost:7400/deployr |
| CONNECTION    | Established anonymous connection [ RClient ] |
| DATA INPUT    | DeployR-encoded R input set on execution, [ ProjectExecutionOptions.rinputs ]|
| EXECUTION     | Discrete R script execution completed [ RScriptExecution ] |
| DATA OUTPUT   | Retrieved working directory file output hip.rData [ RProjectFile ] |


### 2. RepoFileInEncodedDataOut

```
Example: com/revo/deployr/client/example/data/io/anon/discrete/exec/RepoFileInDataEncodedOut.java
```

The following table describes the steps and the associated log output for this example:

| Step           | Log Output                                   |
| -------------- | ---------------------------------------------|
| CONFIGURATION  | Using endpoint=http://localhost:7400/deployr |
|   CONNECTION   | Established anonymous connection [ RClient ] |
|   DATA INPUT   | DeployR-encoded R input set on execution, [ ProjectExecutionOptions.rinputs ] |
|   EXECUTION    | Discrete R script execution completed [ RScriptExecution ] |
|  DATA OUTPUT   | Retrieved working directory file output hip.rData [ RProjectFile ] |


## License ##

Copyright (C) 2010-2015 by Revolution Analytics Inc.

This program is licensed to you under the terms of Version 2.0 of the
Apache License. This program is distributed WITHOUT
ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more 
details.
