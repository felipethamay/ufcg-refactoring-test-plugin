RefactoringTestPlugin version 1.1.x
February 2008

RefactoringTestPlugin runs automatically generated refactoring tests
for the Eclipse IDE <http://www.eclipse.org> and provides tools for
running the tests in NetBeans <http://www.netbeans.org>.  It is a part
of the ASTGen Project <http://mir.cs.uiuc.edu/astgen/>.

COMPILING
========================================

Note: We have tested these steps in Eclipse versions 3.2.2 and 3.3.

 1) Open Eclipse. Select File > Import. The "Import" dialog should pop up.

    a) Choose General > Existing Projects into Workspace.  

    b) Click "Next"

    c) Set the root directory to the directory containing the ASTGen,
       TestOrrery, and RefactoringTestPlugin projects (all three projects
       should have been bundled together in the same download). They
       should each appear in the "Projects:" box.

    d) Check "Copy projects into workspace"

    e) Click "Finish". The projects should appear in the package
       explorer.  Eclipse should compile ASTGen and TestOrrery automatically
       (that is, no red "x" should appear on their icon in the package
       explorer). RefactoringTestPlugin will have compilation problems
       because we have not yet set up its dependencies.

 2) Right-click the RefactoringTestPlugin project in the package
    explorer and choose Build Path > Configure Build Path.  The
    properties dialog should pop up.
    
    a) Choose the "Libraries" tab.

    b) Click the "JDK_TOOLS" entry in the tree view.

    c) Click the "Edit" button. The "Edit Variable Entry" dialog
       should pop up.

    d) Click the "Variable" button. The "Variable Selection" dialog
       should pop up.

    e) Click the "New" button. The "New Variable Entry" dialog should
       pop up.  Type "JDK_TOOLS" (without quotes) for the "Name" text
       box. For the "Path" text box, type or browse for the location
       of the tools.jar library that came with the Java Development Kit.
       The location is usually something like /usr/lib/java/lib/tools.jar
       or C:\Program Files\Java\lib\tools.jar.

    f) Click "OK" several times to return to the properties dialog.
       The "JDK_TOOLS" variable should appear in the tree view along
       with the path.

    g) Click "OK" to exit the properties dialog.

 3) Expand the RefactoringTestPlugin project in the package explorer.
    Right-click build-dependencies.xml and choose Run As > Ant Build.
    This will compile TestOrrery and ASTGen and place their JAR files in
    RefactoringTestPlugin's lib directory.

 4) Right-click the RefactoringTestPlugin project in the package
    explorer and choose "Refresh".  RefactoringTestPlugin should no
    longer have build errors.

RUNNING
========================================

Running Refactoring Tests in Eclipse
----------------------------------------

 1) Right-click the RefactoringTestPlugin project in the package
    explorer and choose Run As > Run...  The "Run" dialog will pop up.

 2) Click "Eclipse Application" in with list at the left of the
    dialog.  Click the "New" button above the box to create a new run
    configuration.

 3) In the "Main" tab:
   
    a) Check "Clear workspace data before launching". 

       IMPORTANT: Make sure you do not set the "Location" text box to
       your normal workspace!  It is probably best to leave the dummy
       location that Eclipse automatically filled in.
    
    b) Choose "Run an appliction"

    c) Select "RefactoringTestPlugin.MainRunner" from the drop-down

    d) Make sure the Runtime JRE is 1.5 or above

 4) Click "Run".  A blank Eclipse window should pop up.  The console
    in the original window should print the following:
   
       No test classes specified
       Usage: RefactoringTestPlugin.MainRunner [-list] [-runall] [-[-]h[elp]] TestClass [TestClass ...]
       
 5) Reopen the run dialog (right-click the project, choose Run >
    Run...) and choose the "Arguments" tab.  Type the following into
    the "Program arguments" box:

       -list

 6) Click "Run".  The console should print a list of the available
    refactoring tests.

 7) Repeat step five and six, but type the following in the arguments box.

       encapsulatefield.Demo

    This should run a series of demonstration refactoring tests
    followed by a summary.  It will also output test results to the
    local machine's temporary directory (i.e. /tmp).  One can supply
    any number of refactoring tests as arguments to the plugin.

Eclipse Tests as a Standalone Application
-------------------------------------------

 1) Right-click the RefactoringTestPlugin project in the package
    explorer and choose "Export".  The export dialog should pop up.

 2) Expand "Plug-in Development", choose "Deployable plug-ins and
    fragments", and click "Next"

 3) Make sure only RefactoringTestPlugin is checked in the window at
    the top of the dialog.  

 4) Type or browse for the Eclipse installation directory (not the
    plugin directory!).

 5) Click "Finish".

 6) In a command line window, cd to the installation directory and
    execute the following:

       eclipse -application RefactoringTestPlugin.MainRunner

    A blank Eclipse window should appear, and the console should print
    the following:

       No test classes specified
       Usage: RefactoringTestPlugin.MainRunner [-list] [-runall] [-[-]h[elp]] TestClass [TestClass ...]
       
 7) Use the -list argument to list available plugin tests.  Supply any
    number of them as arguments to the application to run refactoring
    tests.  When run, they will output test results to the console and
    the local machine's temporary directory (i.e. /tmp).

Running Refactoring Tests in NetBeans
--------------------------------------------

Pre-requisites:
- Ant 1.6.3 or higher
- Java version 1.5 or higher
- Results from running refactoring tests in Eclipse (see previous sections)

1) Obtain NetBeans 6.0 M3 (code + refactoring tests) from NetBeans'
   CVS (note that this will take several hours)

   $ cvs -z6
       -d:pserver:anoncvs@cvs.netbeans.org:/cvs co -r release60_m3
       stable_nowww

2) Replace NetBeans' test directory with our custom tests located in
   netbeans/test

     $ rm -rf  PATH/TO/netbeans60m3/refactoring/test
     $ cp PATH/TO/RefactoringTestPlugin/netbeans/test test

3) Build NetBeans (it could take approximately 1 hour)
     $ cd PATH/TO/netbeans60m3/nbbuild
     $ ant

4) Set global parameters
     Edit file netbeans60m3/refactoring/test/testOur_1_Common.args.
     The first line is the working directory for NetBeans:
       /PATH/TO/netbeans60m3/refactoring/test/work/sys/data/projects/default/src/
     The second line is the root directory for generated tests, e.g.:
       /tmp
     The third line is the root directory of existing Eclipse tests.
     This parameter is not used for generated tests, so leave it as:
       UNUSED
     The fourth line is the output directory for NetBeans refactorings, e.g.:
       outNB

5) Configure the refactoring tests to be run in NetBeans
     The file netbeans60m3/refactoring/test/cfg-qa-functional.xml specifies
     the test to run, by default "testOur_1_Encapsulate_Singleclassfieldreference".

     For each refactoring test, there is a file .args that specifies additional
     parameters for the test.  For example, the file
     netbeans60m3/refactoring/test/testOur_1_Encapsulate_Singleclassfieldreference.args
     specifies the following:
       default                      (NetBeans work directory)
       encapsulatefield             (refactoring name directory)
       singleclassfieldreference0   (refactoring test directory)
       A.java                       (class name) 
       90                           (# tests to be run, call it N)
       1                            (# group of tests to be run, call it I:
                                     runs tests numbered from N*(I-1)+1 to N*I)

6) Run refactoring tests in NetBeans
     $ cd netbeans60m3/refactoring/test
     $ ant runtests -Dxtest.testtype=qa-functional

7) View results
     Open netbeans60m3/refactoring/test/results/index.html
       This file lists the tests that were run and their corresponding status
     Open the log file, e.g.:
       /tmp/encapsulatefield/testOur_1_Encapsulate_Singleclassfieldreference_Log.txt
       This file shows a summary for running tests, e.g.:
         Results for Refactoring: encapsulatefield Type of Test: singleclassfieldreference0 is >>>>>>>>>
         Total Time in seconds was: 202.373
         Process Files, Range: 1 Group of tests: 90

         The total # of input tests was: 90
         The total # of compileds tests was: 37
         The total # of refactored tests was: 37
      If some tests were not refactored, it will include a short description
      of the problem.

CONTACT
========================================

Problems and questions can be directed to the ASTGen team via
http://mir.cs.uiuc.edu/astgen/.

CHANGE HISTORY
========================================

1.1.x 
 * Updated to use TestOrrery instead of edu.uiuc.edsg package

1.0.x
 * Release for FSE 2007 publication