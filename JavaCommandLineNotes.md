# Running Java From the Command Line

- [Introduction (Basic Compilation)](#introduction)
- [Jar Files](#jar-files)
  - [Running Checkstyle](#running-checkstyle)
- [Classpath](#classpath)
  - [Running JUnit](#running-junit)
- [Packages](#packages)
- [226 Homework Setup](#226-homework-setup)
  - [Full Example](#example)
  - [Automating](#automating)

---

## Introduction

For the Java code that you have written thus far (from any Java
pre-requisite such as JHU's 601.107 or 500.112), the files all just sit in the
same directory and you compile and run your code from that same
directory. So, the most basic form of running Java from the command
line is as follows:

Say you have a main program in a file called `Hello.java` with the following folder structure:
```text
~/cs226/
|___hello/
        Hello.java
```

If you navigate to the `hello/` directory, you can compile `Hello.java` using the `javac` command as follows:

```text
~/cs226/hello$ javac -Xlint:all Hello.java
```

`javac` is the Java compiler, `-Xlint:all` is just a flag that tells the compiler to report warnings, etc. and `Hello.java` is the file we want to compile. Now the directory should look like this:

```text
~/cs226/
|___hello/
        Hello.java
        Hello.class
```

`Hello.class` is the Java compiled byte code. If you've taken 601.220 and know how to compile C/C++ code from the command line, the output of that compilation is actually an executable file. It is important to note `.class` files are _not_ executable in the same way. GCC compiles C code to byte-code that actually runs on the machine. `.class` files don't run on your actual machine, they actually run inside a "Java Virtual Machine" (or JVM) that runs on top of whatever machine you have. That part is not important but can be helpful in understanding how working with Java from the command line works with more complex cases.

Now, to run what we just compiled, simply use the `java` command (which spawns one of those JVM's) and runs whatever class you give it:

```text
~/cs226/hello$ java Hello
Hello, World!
```

Woohoo, we're running Java from the command line.

If your program uses command line arguments, they are passed as `java Hello args[0] args[1] ...`

## Jar Files
One thing we will encounter this semester is dealing with what are called "jar" files, that end in `.jar`. A jar file is essentially a file that bundles a bunch of Java files together that a user can include in their own program. Some of these jar files can also be run as if they are a main program. This is how we will be using checkstyle this year. There is a way to download checkstyle to run checkstyle from the command line, but since this is exactly how the autograder will run checkstyle it is worth showing this for demonstration purposes.

### Running Checkstyle
First, you'd want to download the checkstyle
jar from their releases page (the autograder uses
[checkstyle-8.16-all.jar](https://github.com/checkstyle/checkstyle/releases/tag/checkstyle-8.16))
and save it locally. So let's say we want to run checkstyle on that
`Hello.java` file. We'll also want to have a configuration file (the
[cs226_checks.xml](https://cs.jhu.edu/~joanne/cs226_checks.xml)) nearby. The configuration file is just an
xml file that specifies which style rules checkstyle should pay
attention to.

So say we have this setup (where `res/` stands for "resources").
```text
~/cs226/
|___res/
|       checkstyle-8.16-all.jar
|       cs226_checks.xml
|___hello/
        Hello.java
```

To run the checkstyle jar as an executable (note we don't need to compile anything) we can use the `-jar` flag.
```
~/cs226/hello$ java -jar ../res/checkstyle-8.16-all.jar -c ../res/cs226_checks.xml Hello.java
Starting audit...
Audit done.
```

The `-jar` flag says to treat that jar like it is executable, and the `-c` flag says we are specifying a configuration file (the xml file we provide). If you wanted to check multiple files, you could list them after `Hello.java`, or alternatively just use `*.java` to match all Java files.

## Classpath
You're probably familiar with the term "library" as far
as programming is concerned. For example, when you import
`java.util.Scanner` in your code, you are basically telling Java that
your program _depends_ on some other code in that library. When you
install Java, it comes with this collection of things you can
import. By default, when you compile, Java will look for these other
files wherever Java is installed. Now let's say you want to use/run
some code that is not part of the default java installation. To do
this, we need to use the `-classpath` argument.

For this example, we will discuss JUnit - we don't cover JUnit until a few weeks in to the semester but this is a good introduction to what a Java classpath is.

### Running JUnit
JUnit is again a library that you can download in the form of a jar
file. We use the
[junit-4.12.jar](https://github.com/junit-team/junit4/releases/tag/r4.12). JUnit
itself actually depends on another jar file,
[hamcrest-core-1.3.jar](https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core/1.3). Say
you have a file called `MyUnitTest.java` and that code imports
something like `org.junit.Test`. If you tried plain old `$javac
MyUnitTest.java`, the compiler would complain that it doesn't know
what `org.junit.Test` is. Because, that code lives in `junit-4.12.jar`
and we didn't tell the compiler where to look for it. To do this we
add the `-classpath` argument (alternatively, `-cp` or
`--class-path`).

So say we have this setup (where `lib/` stands for "library", and `src/` for source code).
```text
~/cs226/
|___lib/
|       junit-4.12.jar
|       hamcrest-core-1.3.jar
|___src/
        MyUnitTest.java
        MyProgram.java
```

To compile this from the `src/` directory we can do the following:
```
~/cs226/src$ javac -classpath ../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar:. *.java
```

The classpath argument, which is the `../lib/junit-4.12.jar:.` tells Java where to look for files. It is a list, separated by colons. The first two things in the list (`../lib/junit-4.12.jar` and `../lib/hamcrest-core-1.3.jar`) is where our jar files are with the other classes we depend on. The last thing in the list (`.`) stands for the current directory. Without the `.` we'd miss the `MyUnitTest.java` and `MyProgram.java`. We could string together as many of these as we want. We can actually consolidate the classpath to just be `../lib/*:.` since our dependencies are in the same file (this is a common thing to do).

Now, we should have a compiled `MyUnitTest.class` file. However, the way JUnit works, we don't actually run this file ourselves. We run a program that JUnit provides, and we pass it `MyUnitTest` as an argument. The program that JUnit provides (the main program) is `org.junit.runner.JUnitCore`. We again will need to provide a classpath for java to know where files are (and again including the current directory).

```text
~/cs226/src$ java -cp ../lib/*:. org.junit.runner.JUnitCore MyUnitTest
JUnit version 4.12
..
Time: 0.003

OK (2 tests)
```

To recap, you can (and often must) use the `-classpath`/`-cp`/`--class-path` argument to tell `java` and `javac` where all potential classes your program uses could exist. Don't forget to include a `:.` at the end to also include the files in the current directory.

## Packages
This semester we are going to introduce you to Java packages. In the real world, and any significant java program, packages are a way to organize your code in to natural groups.

__Every Java program belongs to a package.__ Without a `package`
statement in the Java file, it belongs to what is called the "default
package", which is the enclosing file folder. This is why the Java
files you've written can automatically find each other (without an
import statement) as long as they are in the same directory. Because
if they are in the same directory then they belong to the same default
package.

Let's say you have a little bit more code now than a simple `Hello.java` program. For this example, let's say we are building a Java program that collects and reports on baseball statistics. You may have some files like this:

```text
~/cs226/
|___baseball_stats/
    |___src/
            Game.java
            Hitter.java
            Pitcher.java
            StatisticsCalculator.java
            RunReport.java
            Reporter.java
            GameReporter.java
            SeasonReporter.java
```

This is manageable, but imagine we add more and more classes, it may
be difficult to keep everything organized. Naturally we could add
`RunReport`, `Reporter`, `GameReporter` and `SeasonReporter` into
their own folder called `report`, or in Java-speak, their own
package (and all other files into a `stats/` package). The resulting directory would look like this:

```text
~/cs226/
|___baseball_stats/
    |___src/
        |___model/
        |       Game.java
        |       Hitter.java
        |       Pitcher.java
        |       StatisticsCalculator.java
        |___report/
                RunReport.java
                Reporter.java
                GameReporter.java
                SeasonReporter.java
```

There would be a few source-code level things to note:
1. Each class in the `report/` directory would need a `package report;` statement.
2. Each class in the `model/` directory would need a `package model;` statement.
3. Since they are no longer in the same package, if any class in the `report` package uses something from the other package (like `Game`), it would require an `import model.Game;` statement (and vise-versa if anything in the `model` package uses something now in the `report` package).
4. Packages can be nested arbitrarily deep, and a `.` in the source code is the selector. So if you had a few nested directories, you could have a `package mypackage1.mypackage2.mypackage3;` statement that could contain an `import otherpackage.blah.SomeClass;` statement.

Now, on to running this from the command line.

First, let's go ahead and compile this how we used to (using `*.java` to include all files in a directory). _Note that we are running this from the `src/` directory, which is the "root" of our source code. If we run
```
~/cs226/baseball_stats/src$ javac -Xlint:all model/*.java report/*.java
```
we actually compile all of the files. But now our directory would look something like this (if you expand it):

<details><summary>Directory</summary>
<p>

```text
~/cs226/
|___baseball_stats/
    |___src/
        |___model/
        |       Game.java
        |       Game.class
        |       Hitter.java
        |       Hitter.class
        |       Pitcher.java
        |       Pitcher.class
        |       StatisticsCalculator.java
        |       StatisticsCalculator.class
        |___report/
                RunReport.java
                RunReport.class
                Reporter.java
                Reporter.class
                GameReporter.java
                GameReporter.class
```

</p></details>

That's not the most visually appealing setup and it gets hard to read with a lot of files. Before we worry about running this, it would be nice to separate all of the class files from our source code. So let's clear out those class files and recompile using the `-d` flag. This flag allows you to specify a directory to output class files to.

Instead, let's make a `classes/` directory at the same level as the `src/` directory and run the following:

```
~/cs226/baseball_stats/src$ javac -Xlint:all -d ../classes/ *.java report/*.java
```

Now we'd have the following setup:

```text
~/cs226/
|___baseball_stats/
    |____classes/
    |       model/
    |           Game.class
    |           Hitter.class
    |           Pitcher.class
    |           StatisticsCalculator.class
    |       report/
    |           RunReport.class
    |           Reporter.class
    |           GameReporter.class
    |           SeasonRepoorter.class
    |___src/
        |   Game.java
        |   Hitter.java
        |   Pitcher.java
        |   StatisticsCalculator.java
        |___report/
                RunReport.java
                Reporter.java
                GameReporter.java
                SeasonReporter.java
```

This is a little easier to work with. So now let's get to running some code. Let's say `RunReport` is the class with a main method we can run. Remember from the [Classpath](#classpath) section how we had to tell Java where to look for class files? We can just use that to let Java know about our `classes/` directory. Note that this is run from the `baseball_stats/` directory.

```text
~/cs226/baseball_stats/$ java -cp classes/ report.RunReport
```

Again, the `.` is the selector when selecting from a package. If you go back to the example where we run JUnit, `JUnitCore` is actuall a class that lives inside a package named `runner` that lives inside a package named `junit` that lives inside a package named `org`.

So now we can run code from the command line with a nice package setup!

## 226 Homework Setup

For assignments in this class, we will provide you with skeleton/starter code for everything you need to write. As long as you don't modify the scope/signature of any methods/classes then your code will compile and work with the autograder. If you are struggling to get your code to compile on Gradescope, please see the Gradescope submission notes we have posted. Anything you need to do will be commented with a `// TODO` in the code (many editors will highlight this too).

Typically, the setup we will provide you with is a very simple package structure with just one or two packages. It could look something like this:

```text
hw1-student.zip
--
exceptions/
    SomeException.java
hw1/
    SomeDataStructure.java
    SomeDataStructureTest.java
    SomeMainProgram.java
    ...
```

Note that wherever you "unzip" this to, the root of your source will be
one level above thw `hw1/` folder, so you would likely run commands from
the folder you extract to. The first package
level is the same level as the `hw1/` directory. So each file in that
folder will have a `package hw1;` statement. Let's say you extract
this to a `assignments` folder inside your `cs226` folder where you keep everything
for the class, and you have some other folder in there (like a `res/`
folder) with all the resources for the class. So given the following
setup, here is a guide for compiling and running everything.

### Example

Let's assume you are following the strategy from these notes and are using an extra `classes/` folder. _If not, you can ignore the `classes/` part of the classpath argument and instead run from the `assignments/` level._

#### Setup:
```text
cs226/
|___res/
|       checkstyle-8.16-all.jar
|       hamcrest-core-1.3.jar
|       junit-4.12.jar
|       cs226_checks.xml
|___homework/
    |___classes/
    |___exceptions/
    |       SomeException.java
    |___hw0/
            SomeDataStructure.java
            SomeDataStructureTest.java
            SomeMainProgram.java
            README.md
```

#### Compiling:
Note that this is run from the `assignments/` directory.
```text
~/cs226/assignments$ mkdir classes/
~/cs226/assignments$ javac -cp ../res/:. -d classes hw1/*.java exceptions/*.java
```

Note that the files you need to change on any given assignment will all be in the same package. Any files in another package are provided without change, so you only need to compile the other packages once. After the initial compilation, you can achieve all further compilation with

```
~/cs226/assignments$ javac -cp ../res/:. -d classes hw1/*.java
```

If you want a "clean" build, you just have to delete the `classes/` folder.

#### Running `SomeMainProgram`
This could be run from anywhere as long as the classpath argument is set correctly.
```text
~/cs226/assignments$ java -cp classes/:. hw1.SomeMainProgram arg1
```

#### Running `SomeDataStructureTest`
```text
~/cs226/assignments$ java -cp ../res/*.jar:classes/:. \
  org.junit.runner.JUnitCore hw1.SomeDataStructureTest
```

#### Running Checkstyle
Typically, unit test files will not need to be checkstyle compliant. So you only need to run checkstyle on the non-unit test files. You can run the command for each file individually, or use the matcher in this example to exclude any files with "Test" in their name.
```text
~/cs226/assignments$ java -jar ../res/checkstyle-8.16-all.jar \
  -c ../res/cs226_checks.xml hw1/*[!Test].java
```

### Automating
There are a few alternatives for automating the build process that you can play around with and decide what you like for your own development.

#### Bash Scripts
If you haven't used bash scripts, they are just a way to write some commands in script-form that can be executed. You can throw any commands you want in to a bash script. So, say you want to save that compile command. You can make a new file, `compile.sh` and add the following:

```bash
#!/usr/bin/env bash

javac -cp ../res/:. -d classes hw1/*.java exceptions/*.java
```

The first line is called a shebang, if you're curious about it you can read about it [here](https://en.wikipedia.org/wiki/Shebang_(Unix)).

Now running `~/somepath/hw01$ sh compile.sh` will execute the compilation. Furthermore, if you run `$ chmod u+x compile.sh` it will mark `compile.sh` as an executable. Now you can run `./compile.sh` and achieve the same thing.

Say you want to do the same for `SomeMainProgram`. If you want the script to take arguments and call `SomeMainProgram` with those arguments, you can add `"$@"` for all arguments, or something like `$1`, `$2`, etc., for a specific argument. You can also call other shell scripts. This could be useful if you want to make sure you compile before each time you run.

So, a `run_main.sh` might look like this:
```bash
#!/usr/bin/env bash

sh compile.sh
java -cp classes/:. hw1.SomeMainProgram "$@"
```

#### Makefiles
If you've taken 601.220 or have done some C-programming, you've probably encountered makefiles. They aren't actually exclusive to C/C++. If you name a file `makefile`, you can still specify targets and dependents. It may not work as smooth as a C setup, but it could still help your development. Remember tabs vs. spaces is important for Makefiles. They can also call other scripts, or include the command directly.

```make
# example makefile

classes:
    sh compile.sh

test:
    sh run_tests.sh

checks:
    java -jar ../res/checkstyle-8.16-all.jar -c ../res/cs226_checks.xml hw1/*[!Test].java

submission:
    zip hw1_submission.zip java/hw1/*.java README.md

clean:
    rm -rf classes
```

Now a simple `make classes` will do all compiling, and `make test` will run your unit tests.

#### Aliases
The last tool you can use is aliasing. This is a way to tell your terminal to remember a command universally. So previously, we dealt with relative paths (meaning we specify the path to something like a jar file based on our current working directory). Any alias you write should probably work no matter where you run it.

So let's say you want to alias `junitr` so that when you type `$
junitr` into the terminal it interprets it as the command to run
JUnit. One caveat is that with this we will be specifying the entire
classpath argument, so to pass in the class that runs the tests you'll
have to run this command wherever the root of your `.class` files
are. If following the examples from here, you'd have to actually run
it from the `classes/` folder. But you would have to open up the
`~/.bash_aliases` file (or just the `~/.bashrc` or `~/.bash_profile`
(Mac)), and add the following line:

```bash
alias junitr="java -cp /absolutepath/junit-4.12.jar:/absolutepath/hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore"
```
substituting the actual full path of the folder containing your jar files for `absolutepath`.

Then, after re-loading the shell (or running the `bash` command), you
should be able to type `junitr hw1.SomeDataStructureTest` into the
shell (from wherever your class files are) and it will run JUnit!

You can make a similar alias to compile your junit files before runnning (it doesn't hurt to include hamcrest here, but it's not needed):

```bash
alias junitc="javac -Xlint:all -cp /absolutepath/junit-4.12.jar:."
```

and similarly for your checkstyle command. Note that depending on your unix environment, your default shell might be something other than bash, so there would be similar profile files such as .cshrc in which to save your aliases.

If you have a simpler package structure set-up, say only a folder hw1 containing your source code MyClass.java (declared to be in package hw1) and a corresponding JUnit test file MyClassTest.java (also declared to be in package hw1 and importing hw1.MyClass). Then after navigating up to the parent folder (the one containing hw1), execute the following commands, substituting the actual full path to the folder containing your jar files for `absolutepath` (or use your aliases):

```bash
> javac -Xlint:all hw1/MyClass.java
> javac -Xlint:all -cp /absolutepath/junit-4.12.jar:. hw1/MyClassTest.java
> java -cp /absolutepath/junit-4.12.jar:/absolutepath/hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore hw1.MyClassTest
```

## Compile Script

Say you have a more advanced package setup, with lots of sub-packages that themselves have sub-packages, and so on and so forth. The one tricky thing about compilation is that there is no "recursive" option to compile sub-folders of a folder. With lots of packages, it is harder to achieve this. This is an example `compile.sh` script that will work for any package setup (assuming it sits in the directory to compile from).

```bash
#!/usr/bin/env bash

# make sure the classes/ directory exists
mkdir -p classes

# the only thing you'd have to set is the folder to start looking for -
# in this example, it says everything inside the java/ folder
JAVA_FILES=$(find ./ -name "*.java")
javac -Xlint:all -d classes/ ${JAVA_FILES}
```

The nice thing about this setup is you can build the following setup for all of your assignments as we release more assignments to you:

```text
cs226/
|___res/
|       checkstyle-8.16-all.jar
|       hamcrest-core-1.3.jar
|       junit-4.12.jar
|       cs226_checks.xml
|___assignments/
    |   compile.sh
    |___classes/
    |___exceptions/
    |   SomeException.java
    |___hw1/
    |   SomeDataStructure.java
    |   SomeDataStructureTest.java
    |   SomeMainProgram.java
    |   README.md
    |___hw2/
    |   SomeHw2Thing.java
    ...
```

None of the assignments depend on each other but some rely on some code we give you in previous assignments. We will provide you with that code again, but you could also just copy in the new homework files to your existing setup if you want.
