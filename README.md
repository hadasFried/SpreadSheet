SpreadSheet

Assumptions about the project:
1. Ids starts from 1 (and not 0)
2. If a cell is null- there will be no exception- it will be added to the cell
3. Lookup function is called lookup(name,index) and not lookup("name",index)


Instructions for running:
1. open the terminal in the directory of the project (the 'SpreadSheets' directory)
2. run 'mvn install' in the directory
3. copy SpreadSheets-0.0.1-SNAPSHOT.jar from the 'target' directory to the 'SpreadSheets' directory
3. run 'java -cp SpreadSheets-0.0.1-SNAPSHOT.jar;***/SpreadSheets/target/dependency/junit-4.13.jar;***/SpreadSheets/target/dependency/hamcrest-core-1.3.jar org.junit.runner.JUnitCore Tests.SpreadSheetTests' replace your path to the directory in ***
