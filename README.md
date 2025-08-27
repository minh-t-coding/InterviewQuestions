# Minh Nguyen - Implementation of PunchLogicTest

A Java application to calculate payroll summaries from employee time punch data.

---

## Further Documentation

For more detailed thoughts on the payroll application, see this document:

[eBacon TakeHome Notes](https://docs.google.com/document/d/1Ln6R-jBAxLBiWu6ZhfoAXDD4xWnfBSlhs_71Df3f_PI/edit?usp=sharing)

---

## Prerequisites

Make sure the following are installed on your system:

* **Java 22** (tested with 22.0.2)
* **Maven 3.9.9** or later
* Git (for cloning the repository)

> **Note:** Your Java version should be 17 or higher, since the project is compiled with `maven.compiler.release=17`.

---

## Clone the Repository

```bash
git clone <your-repo-url>
cd hr-app
```

---

## Build the Project

To compile the project and package it as a JAR:

```bash
mvn clean package
```

This will generate a JAR in the `target/` directory:

```
target/hr-app-1.0-SNAPSHOT.jar
```

---

## Run the Application

The JAR can be run with or without specifying an input file:

```bash
# Default input file (PunchLogicTest.jsonc)
java -jar target/hr-app-1.0-SNAPSHOT.jar

# Or specify a custom input file
java -jar target/hr-app-1.0-SNAPSHOT.jar path/to/your/input.jsonc
```

**Output:**
The program will generate a payroll report in `output.json` and print progress messages to the console.

---

## Run Unit Tests

To run all JUnit 5 tests:

```bash
mvn test
```

All test reports will be displayed in the console, and a report will be available under:

```
target/surefire-reports
```

---

## Notes

* Ensure your input JSONC/JSON files match the expected schema for **employees**, **jobs**, and **time punches**.
* Payroll calculations respect the rules defined in `PayrollRulesConfig`. You can adjust overtime/doubletime limits and rates or even DateTime formats if needed.
* This project has been tested with **Java 22.0.2** and **Maven 3.9.9**. Using earlier versions may require minor adjustments.