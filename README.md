# strafan
strafan, short for "Shodan's Traffic Analyzer", is a program written as an auxiliator for a mini-project for the
Computer Networking Curricular Unit of the school year of 2021/2022, Bachelor's in Computer Engineering at the
Faculty of Sciences of the University of Lisbon (Faculdade de Ciências da Universidade de Lisboa).

## Update Log
((v1.1)) -> Fixed all inherent bugs
 
((v1.2)) -> Moved parts of the code into external functions for better modularity
 
((v1.3)) -> New functions are now ready for CSV files with a column for an ICMP type

## Building and using
You can use `javac` to compile each file individually into .class bytecode files, or you can use `make` to build everything all at once with the following command:

```
$ make
```

Afterwards, just run the program. I assume your machine has at least a version >=11.0 of JVM.
```
$ java strafan <filename> [OPTIONS]
```
