# My STudent Automated Registration System (MySTARS) 

MySTARS is a console-based application meant for each School’s academic staff and undergraduate students

## Installation

Classes have already been compiled. However, to recompile the application, go to ```./oopAssignment/classes``` and run

```bash
javac -cp "./javax.mail.jar" controllers/*.java entities/*.java interfaces/*.java
```

To run the application, go to the main directory ```./oopAssignment``` and run

```bash
java -cp .:javax.mail.jar:javax.activation.jar classes/interfaces/LoginForm
```

## Dependencies

This application requires ```javax.mail.jar``` and ```javax.activation.jar```. They have already been included in the directory.

## Requirements
This application was built and tested on JDK 14

## Contributors
This application was built by Chew Jie Ying Perlyn, Tan Song Yun, Yong Duan Kai, Shao Yakun and Justin Yip Jia En for CZ2002
