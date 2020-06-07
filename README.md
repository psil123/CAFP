# CAFP
Implementation of CAFP Algorithm

Execution command : CAFP.jar <input file path> <delimiter> <output file path> <minsup value as a fraction>
<input file path>            : the path to an input file containing a transaction database.
<delimiter>                  : the delimiter between each item in the input file.For example it will be "," for csv files
<output file path>           : the output file path for saving the result (if null, the result will also be printed).
<minsup value as a fraction> : the minimum support threshold.
  
To execute it download the jar file , sample.txt and type the following command --
  CAFP.jar "sample.txt" " " "output.txt"  0.5
