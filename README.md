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

### Options
- `-a <ip version>` : checks the number of packets whose sender and receiver have addresses specified in the same IP version (IPv4 or IPv6)
- `-f <flags_bin>` : checks the given flag binary sequence, and checks how many packets have those flags raised
- `-m <MAX|MIN|AVG>` : checks the maximum, minimum or average packet size
- `-r4` : checks how many unique IPv4 addresses are in the "Receiver" column
- `-sp` : checks how many unique TCP source ports are present in the given trace
- `-d` : generates a gnuplot script for the cumulative distribution function of X, where X is the size of the packets in the trace
- `-h <n_lines>` : generates a new .csv file with only the first n_lines of the original. You can use this new file for testing purposes.
- `-c` : ATTENTION! TO USE THIS FLAG, YOU *MUST* HAVE A DATASET FILE "myData.txt"! YOU CAN GET THIS FILE BY RUNNING STRAFAN WITH THE `-d` FLAG! Uses a normal gnuplot dataset file and generates a new dataset file with the data to render a complementary cumulative distribution function. Editing the gnuplot script is up to the user's own discretion.

### Examples:
```
$ java STrafAn dataFile.csv -a 4
```
This command will return how many packets have both the source and destination addresses written in the IPv4 notation.
```
$ java STrafAn dataFile.csv -f 000100
```
As 000100 is the equivalent of 0x004, this command will return how many packets have their flags set to 0x004 (Only RST is raised).
```
$ java STrafAn dataFile.csv -m MAX
```
This command returns the maximum packet size of all the packets specified in dataFile.csv

## Comments and Suggestions
I am not, nor do I claim to be the best programmer out there. I wanted to make this program to be as efficient as possible, since it's supposed to be handling large amounts of data (and, yet, I wrote it in Java). Aside from my current effort to rewrite this as a C program, what are your suggestions to make it more efficient? Am I making mistakes in my algorithms that make them slower? Am I misusing data structures in ways that make the code inefficient? Please let me know! I welcome all suggestions and critiques.
