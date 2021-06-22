## Report

[See final team report](https://github.com/CS453-Team-Project/report/blob/main/main.pdf)

## Python Path Condition Solver

[See Path Condition Solver project](https://github.com/CS453-Team-Project/parse-jbse-output)

## Docker build

### Docker 빌드

```sh
docker build -t <image name> .
```

### Start Maven

* `mvn clean` : Maven 초기화
* `mvn package` : Maven 빌드


### MutRe 실행 방법
```sh
# Append following lines to ~/.bashrc (you don't have to add following on a docker container)
export CS453_PROJECT_HOME="/root/jbse-pitest-integration"
alias mytool="java -cp target/classes:$CS453_PROJECT_HOME/target/classes:$CS453_PROJECT_HOME/res/javassist.jar:$CS453_PROJECT_HOME/res/jbse-0.10.0-SNAPSHOT-shaded.jar:$CS453_PROJECT_HOME/res/picocli-4.6.1.jar:$CS453_PROJECT_HOME/res/asm-all-3.3.1.jar:$CS453_PROJECT_HOME/res/json-20210307.jar com.cs453.group5.symbolic.SymMain"

Usage: <main class> [-co] [-m=<mutantNumbers>[,<mutantNumbers>...]...]...
                    <classBinaryName> [<methods>...]
      <classBinaryName>   Class name with dot syntax (e.g. com.cs453.group5.
                            examples.Calculator)
      [<methods>...]      Method names (e.g. isPositive isNegative ...).
                            Default will run all methods of survived mutants.
                            If option -o or -m was specified, then default will
                            run nothing.
      -c, --clean         Clear and rebuild.
      -m, --mutants=<mutantNumbers>[,<mutantNumbers>...]...
                          Run specific mutants. Parameters are the mutant
                            indexes splitted with `,` (e.g. -m 1,2,3,4,5). The
                            program will not modify the byte code and return
                            pure jbse report.
      -o, --original      Run original class. In his run, Run original class.
                            The program will not modify the byte code and
                            return pure jbse report.

# Run Examples
# 1. Run pitest if needed. Find test cases for survived mutants.
$ mytool com.cs453.group5.examples.Parenthese

# 2. Run pitest if needed. Run symbolic execution for mutant 1 and 3. You need to specify method names.
$ mytool com.cs453.group5.examples.Parenthese -m 1,3 check

# 3. Run pitest if needed. Run symbolic execution for the original binary file. You need to specify method names.
$ mytool com.cs453.group5.examples.Parenthese -o check

# 4. Clean build and run pitest if needed. Same with example 1.
$ mytool com.cs453.group5.examples.Parenthese -c

# 5. Run pitest if needed. Find test cases for survived mutants that has mutated method "check".
$ mytool com.cs453.group5.examples.Parenthes check
```

# Legacy

## PITest

`./run -p`

* PITest Execution

```
mvn org.pitest:pitest-maven:mutationCoverage
```

* Storing mutation bianary code

```
mvn org.pitest:pitest-maven:mutationCoverage -Dfeatures=+EXPORT
```

* Directory storing mutants: `.target\pit-reports\export`

## JBSE

* `addUserClassPath()`: 실행하기 위한 binary 파일이 위치한 directory

```
p.addUserClasspath("./target/classes");
```

* `setMethodSignature()`: Class binary path, method signature, and method name

```
p.setMethodSignature("com/cs453/group5/examples/Calculator", "(I)I", "isPositive");
```

* 참고
  * `./target/classes/com/cs453/group5/examples/Calculator`에 대하여 Symbolic execution을 진행
  * `(I)`: 타겟 함수 파라미터 타입
  * `I`: 타겟 함수 리턴 타입
  * `IsPositive`: 타겟 함수 명칭

  ### JBSE 실행방법
  ```
  java -cp 'target/classes:res/jbse-0.10.0-SNAPSHOT-shaded.jar' com.cs453.group5.examples.{ClassName}
  ```


### Mutants 실행 방법

```sh
$ ./run
Usage:

# Run methods of either original one or mutated one.
# If [-m <mutant index>] is omitted, this script will run JBSE for the original method.
# The output can be piped into a text file using `> result.txt`.
./run [-m <mutant index>] [-c] <class name> <method name> [> result.txt]

# Run PIT
./run -p

<class name>          Class name with dot syntax (e.g. com.cs453.group5.examples.Calculator)
<method name>         Method name (e.g. isPositive)
-m <mutant index>     Mutant index. By default, this script will run the original one.
-c                    Clear and rebuild the project.
-p                    Run PIT.
```
