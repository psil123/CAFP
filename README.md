# CAFP
Implementation of CAFP Algorithm

Execution command --
'''CAFP.jar "input file path" "delimiter" "output file path" "minsup value as a fraction" ''' <br>
"input file path"            : the path to an input file containing a transaction database.<br>
"delimiter"                  : the delimiter between each item in the input file.For example it will be "," for csv files<br>
"output file path"           : the output file path for saving the result (if null, the result will also be printed).<br>
"minsup value as a fraction" : the minimum support threshold.<br>
  <br>
To execute it download the jar file , sample.txt and type the following command --<br>
  CAFP.jar "sample.txt" " " "output.txt"  0.5
