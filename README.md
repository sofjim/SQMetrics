***************
SQMetrics v 0.7
***************


INTRODUCTION

    - This is a simple application to measure the most common software quality metrics. It has a simple graphical interface, so the user should not have much experience to use it. 


USAGE

    - At first, you must choose the directory that contains both, the source code (*.java files) and the binary code (*.class files). 
    - Then you must choose which metrics to be counted. Feel free to choose any of the metrics or the whole set of them. A tool tip is shawn when you hover the mouse pointer over each metric, with its description.
    - After that, a progress bar will be displayed and then a table with the results appears on the screen. You have the option to export them into a .csv file. In this case, you have to choose where to save the file and the name of the file. If you don’t add the .csv extension or if you add a wrong one, the program will correct it. The default separator that has been set is the semicolon (;) such as in Microsoft Excell. If you use another spreadsheet programm, you must import the data manually.
    - You may leave this window open as long as you work with the application. It will be closed automatically if you exit the program (works with the "Exit" button, but not work with the "Finish" button).


LIMITATIONS

    - The directory you choose, must contains both, java and class files. If they are at different folders, choose their common parent directory.
    - You have to compile your program first, so the *.class files must exist and should have the same names as the *.java files do. If a ".java" file is missing, the program will run and you 'll get the results for the other classes, but if a ".class" file is missing or has a different name than the corresponding ".java" file, the program won't work and will crash.
    - The declarations of the methods into the code, must be in 1 line, otherwise the results of some metrics will be slightly different.


REQUIREMENTS

    - This program will run on Microsoft Windows, Linux and Mac OS X. It has been tested on Microsoft Windows 7 and 10, on Ubuntu Linux and on Mac OS X High Sierra. It should run on the other versions of these platforms too, but has not been tested.
    - JRE 1.8 or JDK 1.8 (or newer) is required to be installed on your system, so this program is able to run.


DISCRIPTION
    - The application supports 3 size metrics, the 6 C & K metrics and the 11 QMOOD metrics.

    - SIZE METRICS
        - Physical Line of Code (PLOC): It counts the "CR" or "ENTER" characters. All the lines are counted, included the comment and the blank lines.
        - Logical Lines of Code (LLOC): All lines that contain code are counted even if it is only 1 character. If a line contains both code and comments it will be counted.
        - Lines of Comments (LC): Only the lines that contain comments are counted. If a line contains both code and comments it will be counted.

    - C && K METRICS
        - Number of Children (NOC): The NOC of a class is the number of the classes that inherit from this class.
        - Depth of Inheritance Tree (DIT): It calculates the depth of the inheritance tree of a class, into the project. If the class inherits from an external class out of the project, it’s DIT is "1".
        - Coupling Between Objects (CBO): Two classes are coupled when one of them uses methods, fields or objects of the other class or if they are parent – child.
        - Weight Method per Class (WMC): It counts the sum of the cyclomatic complexity of all the methods of the class.
        - Lack of Cohesion of Methods (LCOM): There are 3 types. LCOM1, LCOM2 and LCOM3. The LCOM1 is the oldest and is an integer. It adds "1" for every pair of methods that use at least 1 common instance field and abstracts "1" for every pair which doesn’t. If it is below "0", then it is "0". The LCOM2 is calculated by the following formula: LCOM2 = 1 – sum(nA)/(m * a), where m = number of methods and a = number of fields, nA = number of methods that access the field "A". LCOM3 is a little bit different. LCOM3 = (m - sum(nA)/a)/(m - 1). LCOM2 takes values between "0" and "1" and LCOM3 between "0" and "2". For all 3 metrics, the closer to "0" the better. Getters and setters such as constructors are excluded.
        - Response For a Class (RFC): It is the number of methods that could be called to answer a message from an object of another class. It is calculated by the formula: RFC = M + R, where M is the number of the methods of the class and R is the number of methods of other classes, calling directly some method of the class.

    - QMOOD METRICS
        - Design Size in Classes (DSC): It is the number of the classes of the project.
        - Number Of Hierarchies (NOH): It is the number of the subtrees of the project, under the System.Object class, which means the number of the classes that have at least 1 child and DIT = 1.
        - Average Number of Ancestors (ANA): It is the average of the classes from which a class inherits. It is given by the formula ANA = DIT / DSC.
        - Data Access Metric (DAM): It is the division between the number of "private" and "protected" fields by the total number of fields. The closer to "1" the better.
        - Direct Class Coupling (DCC): It is the number of the other classes that are directly coupled with this class. A class is directly coupled with another class when it is parent or child of the other class, it has a field with the type of the other class, or its methods return a value or have a parameter with a type of the other class.
        - Cohesion Among Methods (CAM): It is calculated by the formula CAM = (sum Mi) / (n * (T + 1)). "Mi" is the number of the distinct parameters of the method "i", "sum Mi" is the sum for all methods of the class, "n" is the number of the methods of the class and "T" is the number distinct parameter type of all the methods of the class. In "Mi", the type of the class itself is included, but in the "T" it’s not included.
        - Measure of Aggregation (MOA): This method returns the number of variables which are declared by the user. This means the variables, that their type is another class of the project. It counts the class's fields, the constructor's variables and the methods' variables and return types.
        - Measure of Functional Abstraction (MFA): It is the "Measure of Functional Abstraction" (MFA) metric, which is the division between the inherited methods, by all the methods of the class (inherited and declared).
        - Number of Polymorphic Methods (NOP): It is the number of the methods of a class, which may have polymorphic behavior.
        - Class Interface Size (CIS): It is the number of the "public" methods of the class.
        - Number of Methods (NOM): It is the number of the methods that are declared in a class, which means the number of the public, protected, default (package) access, and private methods of the class. Constructors are excluded.

    - QMOOD QUALITY CHARACTERISTICS
        Each of the following characteristics is been calculated, only if all of its parameters have been checked for calculation.

        - REUSABILITY = (- 0.25) * DCC + 0.25 * CAM + 0.5 * CIS + 0.5 * DSC
        - FLEXIBILITY = 0.25 * DAM - 0.25 * DCC + 0.5 * MOA + 0.5 * NOP
        - UNDERSTANDABILITY = (- 0.33) * ANA + 0.33 * DAM - 0.33 * DCC + 0.33 * CAM - 0.33 * NOP - 0.33 * NOM - 0.33 * DSC
        - FUNCTIONALITY = 0.12 * CAM + 0.22 * NOP + 0.22 * CIS + 0.22 * DSC + 0.22 * NOH
        - EXTENDIBILITY = 0.5 * ANA - 0.5 * DCC + 0.5 * MFA * 0.5 * NOP
        - EFFECTIVENESS = 0.2 * ANA + 0.2 * DAM + 0.2 * MOA + 0.2 * MFA + 0.2 * NOP


KNOWN BUGS

    - The "New Measurement" button works on Microsoft Windows, but does not work on Linux nor Mac OS X. On these two platforms, this button terminates the program and if you want to make another measurement, you have to run it again.
    - On some Windows systems the runnable ".jar" file won't run. In this case you have to execute the "RunMe_on_Windows" script and the program will run perfectly.
    - Please, report any new bug at std131050@ac.eap.gr.


HISTORY

    - v 0.1: 
	All 6 Cidamber & Kemerer metrics are supported, plus 3 size metrics as physical and logical lines of code and lines of comments. The results are displayed in the terminal.

    - v 0.2: 
	A graphical environment and the ability to export data to a.csv file have been added.

    - v 0.3: 
	All 11 QMOOD metrics have been added.

    - v 0.4: 
	Calculation for the 6 quality characteristics, that the QMOOD model suggests, has been added. A bug, that led to wrong calculation of the NOM metric and, by extension, to other metrics such as LCOM1, RFC, CAM etc, has been fixed. 

    - v 0.5: 
	The ability to choose the separate character for the exported csv file, between coma (,) and semicolon (;), has been added. Tooltips' delay time increased to infinity.

    - v 0.6: 
	Improved and faster algorithm for LCOM1, LCOM2 and LCOM3 metric (now only instance fields are counted). Names of classes and metrics, now appear on the progress bar.

    - v 0.7: 
	Total new interface. More flexible, more stable. Faster algorithms up to 60%. The problem with non utf-8 characters has been fixed.
