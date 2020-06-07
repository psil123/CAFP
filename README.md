# CAFP
Implementation of CAFP Algorithm

## HOW TO EXECUTE
Execution command --
```
CAFP.jar "input file path" "delimiter" "output file path" "minsup value as a fraction"
```
<br>
Each of these parameters represent -- <br>
<b>"input file path"</b>            : the path to an input file containing a transaction database.<br>
<b>"delimiter"</b>                  : the delimiter between each item in the input file.For example it will be "," for csv files<br>
<b>"output file path"</b>           : the output file path for saving the result (if null, the result will also be printed).<br>
<b>"minsup value as a fraction"</b> : the minimum support threshold.<br>
 <br>

## EXAMPLE
To execute it download the <b>CAFP.jar</b> file , <b>sample.txt</b> and type the following command --<br>
```  
CAFP.jar "sample.txt" " " "output.txt"  0.5
```
## ORIGINAL PAPER
* **Author** - *Initial work* - [Link](link)
