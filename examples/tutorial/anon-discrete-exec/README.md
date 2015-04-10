Anonymous Users & Data I/O
==========================

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

## License ##

Copyright (C) 2010-2015 by Revolution Analytics Inc.

This program is licensed to you under the terms of Version 2.0 of the
Apache License. This program is distributed WITHOUT
ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
Apache License 2.0 (http://www.apache.org/licenses/LICENSE-2.0) for more 
details.
