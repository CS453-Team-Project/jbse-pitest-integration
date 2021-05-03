## Docker build

### Docker 빌드

```
docker build -t 
```

### Start Maven

* `mvn clean` : Maven 초기화
* `mvn install` : Maven 빌드



## PITest

* PITest Execution

```
mvn org.pitest:pitest-maven:mutationCoverage
```

* Storing mutation bianary code

```
mvn org.pitest:pitest-maven:mutationCoverage -Dfeatures
```

* stored directory: `target\pit-reports\export`

## JBSE

* `addUserClassPath()`: 실행하기 위한 binary 파일이 위치한 directory

```
p.addUserClasspath("./target/classes");
```

* `setMethodSignature()`: 실행하기위한 클래스  경로

```
p.setMethodSignature("com/cs453/group5/examples/Calculator", "(I)I", "isPositive");
```

* 참고
  * `./target/classes/com/cs453/group5/examples/Calculator`에 대하여 Symbolic execution을 진행
  * `(I)`: 타겟 함수 파라미터 타입
  * `I`: 타겟 함수 리턴 타입
  * `IsPositive`: 타겟 함수 명칭

