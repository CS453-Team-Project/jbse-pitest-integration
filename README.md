## Docker build

### Docker 빌드

```sh
docker build -t <image name> .
```

### Start Maven

* `mvn clean` : Maven 초기화
* `mvn package` : Maven 빌드

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

### Mutants 실행 방법 2
```sh
# Append this line to ~/.bashrc
alias mytool="java -cp 'target/classes:res/javassist.jar:res/jbse-0.10.0-SNAPSHOT-shaded.jar:res/picocli-4.6.1.jar' com.cs453.group5.symbolic.SymMain"

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

# Examples
# 1. Run pitest if needed. Find survived mutants. Insert jbse ass3rt. run jbse.
$ mytool com.cs453.group5.examples.Parenthese

# 2. Run pitest if needed. Find mutant 1, 2, 3, 4. Run jbse.
$ mytool com.cs453.group5.examples.Parenthese -m 1,2,3,4

# 3. Run pitest if needed. Find original class file. Run jbse.
$ mytool com.cs453.group5.examples.Parenthese -o

# 4. Clean build and run pitest. Same with ex 1.
$ mytool com.cs453.group5.examples.Parenthese -c

# 5. Run pitest if needed. Find survived mutants that has mutated method check1 or check2. Insert jbse ass3rt. run jbse.
$ mytool com.cs453.group5.examples.Parenthes check1 check2
```