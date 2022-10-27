# CAFP
Implementation of CAFP Algorithm

### Prerequisites

1. Installed Java version should be >= 8
2. Dependency used : [guava-22.0.jar](https://mvnrepository.com/artifact/com.google.guava/guava/22.0) (Has been included in this project)

## HOW TO EXECUTE

First <b>download</b> the <b>CAFP.jar</b> file.

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

For using it in editors -
1.Clone this repository 
2.Open it in your desired editor
3.Build it using maven with goals clean and install
4.As an example execute MainTest.java in main package.

## EXAMPLE
To execute it download the <b>CAFP.jar</b> file , <b>sample.txt</b> and type the following command --<br>
```  
CAFP.jar "sample.txt" " " "output.txt"  0.5
```
## ORIGINAL PAPER
* **Moumita Ghosh, Anirban Roy, Pritam Sil, Kartck Chandra Mondal** - *Frequent Itemset Mining Using FP-Tree: A CLA-based Approach and Its Extended Application in Biodiversity* - [To Be Added](link)
