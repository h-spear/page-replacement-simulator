# Pace Replacement Simulator

-   OS
-   Java 11
-   Gradle

<br>

## REQUIREMENTS

-   다양한 페이지 교체 정책 시뮬레이션 제공
-   데이터 스트림을 직접 입력하거나 파일로 입력
-   Hit, Miss, Hit Ratio 계산
-   페이지 교체 애니메이션 출력
-   결과 보고서 출력, 결과 파일 생성

<br>

## DOC

-   [PDF](docs/doc.pdf)

<br>

## RESULT

-   [시연 영상](https://youtu.be/bhc0smhrTc4)

<br>

## LIBRARY

-   poi-bin-5.2.3
    -   https://www.apache.org/dyn/closer.lua/poi/release/bin/poi-bin-5.2.3-20220909.zip
-   log4j-core-2.19.0
    -   https://www.apache.org/dyn/closer.lua/logging/log4j/2.19.0/apache-log4j-2.19.0-bin.zip

<br>

## PACKAGES

![package-diagram](docs/img/package.png)

<br>

## CLASS

![class-diagram](docs/img/class.png)

<br>

## DESIGN PATTERN

-   Template Method
-   Strategy

<br>

## EXAMPLE

-   입력 예시

    ![example-1](docs/img/example/example-1.png)

-   결과 출력

    ![example-2](docs/img/example/example-2.png)

-   애니메이션 출력(예시: optimal simulator)

    ![example-3](docs/img/example/example-3.png)

-   엑셀 저장(예시: clock simulator)

    ![example-4](docs/img/example/example-4.png)
