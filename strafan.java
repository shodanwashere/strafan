import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

/**
 * strafan, short for "Shodan's Traffic Analyzer", is a program written as an auxiliator for a mini-project for the
 * Computer Networking Curricular Unit of the school year of 2021/2022, Bachelor's in Computer Engineering at the
 * Faculty of Sciences of the University of Lisbon (Faculdade de Ciências da Universidade de Lisboa).
 * 
 * {-- Update Log --}
 * ((v1.1)) -> Fixed all inherent bugs
 * 
 * ((v1.2)) -> Moved parts of the code into external functions for better modularity
 * 
 * ((v1.3)) -> New functions are now ready for CSV files with a column for an ICMP type
 * 
 * @author nunoDias fc56330
 */
public class strafan {
    public static void main(String argv[]) throws Exception{
        if(argv.length < 1){
            System.err.println("Error: not enough args!\nUsage: java STrafAn <filename> [OPTIONS]");
            System.exit(1);
        }

        if(!CompClass.checkFile(argv[0])){
            System.err.println("Error: file does not exist or is directory");
            System.exit(1);
        }

        // Confirmed good behavior, as there are enough arguments and the file specified in argv[0] exists

        // Checking program flags
        int ipVersion = 0;
        String flagSeq = "";
        String mOption = new String("");
        boolean r4Flag = false;
        boolean spFlag = false;
        boolean gnuPlotFlag = false;
        boolean compGPFlag = false;
        int headNumber = 0;
        if(argv.length > 1){
            int i = 1;
            while(i < argv.length){
                if(argv[i].equals("-a")){
                    if(ipVersion != 0){
                        System.err.println("Error: duplicate flag");
                        System.out.println("Usage: java STrafAn <filename> -a <version>");
                        System.exit(5);
                    }
                    if(i++ >= argv.length){
                        System.err.println("Error: IP version not specified!");
                        System.out.println("Usage: java STrafAn <filename> -a <version>");
                        System.exit(2);
                    } else {
                        ipVersion = Integer.parseInt(argv[i]);
                        if((ipVersion != 4) && (ipVersion != 6)){
                            System.err.println("Error: IP Version not valid (must be 4 or 6)");
                            System.exit(4);
                        }
                    }
                }
                if(argv[i].equals("-f")){
                    if(!flagSeq.equals("")){
                        System.err.println("Error: duplicate flag");
                        System.out.println("Usage: java STrafAn <filename> -f <flags_bin>");
                        System.exit(5);
                    }
                    if(i++ >= argv.length){
                        System.err.println("Error: Flag binary sequence not specified!");
                        System.out.println("Usage: java STrafAn <filename> -f <flags_bin>");
                        System.exit(2);
                    } else {
                        flagSeq = CompClass.binSeqToHex(argv[i]);
                    }
                }
                if(argv[i].equals("-m")){
                    if(i++ >= argv.length){
                        System.err.println("Error: Option not specified\nUsage: java STrafAn <filename> -m <MAX|MIN|AVG>");
                        System.exit(2);
                    } else {
                        switch(argv[i]){
                            case "MAX":
                            case "MIN":
                            case "AVG": break;
                            default: System.err.println("Error: Invalid option\nUsage: java STrafAn <filename> -m <MAX|MIN|AVG>"); System.exit(3);
                        }
                        mOption = new String(argv[i]);
                    }
                }
                if(argv[i].equals("-r4")){
                    if(r4Flag){
                        System.err.println("Error: Duplicate flag");
                        System.out.println("Usage: java STrafAn <filename> -r4");
                        System.exit(5);
                    } else r4Flag = true;
                }
                if(argv[i].equals("-sp"))
                    spFlag = true;
                if(argv[i].equals("-d"))
                    gnuPlotFlag = true;
                if(argv[i].equals("-h")){
                    if(i++ >= argv.length){
                        System.err.println("Error: number not specified");
                        System.exit(2);
                    } else {
                        headNumber = Integer.parseInt(argv[i]);
                    }
                }
                if(argv[i].equals("-c")){
                    compGPFlag = true;
                }
                i++;
            }
        }

        // Reading the CSV file
        File file = new File(argv[0]);
      	
        if(ipVersion != 0){
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            respondToIPFlag(br, ipVersion);
            br.close();
            fr.close();
        }
        if(!flagSeq.equals("")){
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            respondToBinSeqFlag(br, flagSeq);
            br.close();
            fr.close();
        }
        if(!mOption.equals("")){
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            respondToMaleableFlag(br, mOption);

            br.close();
            fr.close();
        }
        if(r4Flag){
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            respondTor4Flag(br);
            br.close();
            fr.close();
        }
        if(spFlag) {
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
			
			respondToSPFlag(br);
			br.close();
            fr.close();
		}
        if(gnuPlotFlag){
            // doing the CCDF
            Scanner sc = new Scanner(System.in);
            System.out.println("MAX VALUE? ");
            int maxVal = sc.nextInt();
            System.out.println("MIN VALUE? ");
            int minVal = sc.nextInt();
            sc.close();

            List<Integer> listOfSizes = new ArrayList<Integer>();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int counter = 0;
            System.out.print("Getting packet sizes... ");
            while((line = br.readLine()) != null) {
                if(counter == 0){
                    counter++;
                    continue;
                }
                String[] newLine = CompClass.splitter(line);
                listOfSizes.add(Integer.parseInt(newLine[7]));
                counter++;
            }
            counter -= 1;
            System.out.println(counter);
            // Next, we sort the list of
            Collections.sort(listOfSizes);
            System.out.println(listOfSizes.toString());
            br.close();
            fr.close();
            System.out.println("Done!");
            int bufferCounter = 0;
            BufferedWriter myDataFile = new BufferedWriter(new FileWriter("myData.txt"));
            System.out.println("Building data set for the CCDF gnuplot... ");
            LoadingBar lb = new LoadingBar(listOfSizes.size());
            int i = 0;
            for(int checkSize = minVal; (checkSize <= maxVal); checkSize ++){

                if(i == counter) break;
                    boolean checkNice = true;
                    while(checkNice){
                        if(i == counter) break;
                        checkNice = listOfSizes.get(i) <= checkSize;
                        if(checkNice){
                            bufferCounter++;
                            i++;
                        }
                        System.out.print("["+ checkSize +"] :: ");
                        lb.show_loading_bar();
                        lb.update_curr_status(bufferCounter);
                    }
                
                myDataFile.write(checkSize + " " + (((float) bufferCounter)/ ((float) counter)) + "\n");
            }
            myDataFile.close();
            System.out.println("Done!");

            BufferedWriter myGnuplotFile = new BufferedWriter(new FileWriter("myPlot.gp"));
        	myGnuplotFile.write("set terminal svg size 350,262\n");
		    myGnuplotFile.write("set output 'plot.svg'\n");
    		myGnuplotFile.write("set xrange ["+ minVal+":"+maxVal+"]\n");
    		myGnuplotFile.write("set yrange [0:1]\n");
    		myGnuplotFile.write("plot 'myData.txt'\n");

    		// myDataFile.close();
    		myGnuplotFile.close();
        }
        if(headNumber != 0){
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            BufferedWriter bw = new BufferedWriter(new FileWriter("myHeadFile.csv"));
            for(int i = 0; i < headNumber; i++){
                String line = br.readLine();
                bw.write(line + "\n");
            }
            bw.close();
            br.close();
            fr.close();
        }
        if(compGPFlag){
            FileReader fr = new FileReader(new File("myData.txt"));
            BufferedReader br = new BufferedReader(fr);
            BufferedWriter bw = new BufferedWriter(new FileWriter("myCompData.txt"));

            String line;
            while((line = br.readLine()) != null){
                String[] newLine = line.split(" ");
                float compValue = Float.parseFloat(newLine[1]);
                compValue = 1 - compValue;
                bw.write(newLine[0] + " " + compValue + "\n");
            }

            bw.close();
            br.close();
            fr.close();
        }
    }

    /**
     * Function with code to respond when the user invokes strafan with the version checking flag
     * @param input A source of input from a CSV file
     * @param ipVersion The version of IP to be checked
     * @assumes {@code ipVersion == 4 || ipVersion == 6}
     * @throws IOException
     */
    public static void respondToIPFlag(BufferedReader input, int ipVersion) throws IOException{
        String line;
		int counter = 0;

		while ((line = input.readLine()) != null) {
            String[] newLine = CompClass.splitter(line);
            if(newLine[0].equals("No.")) continue;
            if(CompClass.checkIPAdressVersion(newLine[2], ipVersion) &&
               CompClass.checkIPAdressVersion(newLine[3], ipVersion))
                counter++;
        }
        System.out.println("Source & Destination with IPv" + ipVersion + " :: Count: " + counter);
    }

    /**
     * Function with code to respond when the user invokes strafan with a flag sequence check flag
     * @param input A source of input from a CSV file
     * @param flagSeq The processed flag sequence, now a Hex String
     * @throws IOException
     */
    public static void respondToBinSeqFlag(BufferedReader input, String flagSeq) throws IOException{
        String line;
        int counter = 0;

        while ((line = input.readLine()) != null){
            String[] newLine = CompClass.splitter(line);
            if(newLine[0].equals("No.")) continue;

            // System.out.println("Line " + newLine[0] + " :: " + newLine[6] + " Protocol :: Flags = " + newLine[8]);
            if(flagSeq.equals(newLine[9]) && newLine[6].equals("TCP")) counter++;
        }
        System.out.println("Datagram with flag sequence " + flagSeq + " :: " + counter);
    }

    /**
     * Function with code to respond when the user invokes strafan with the Maleable Check Flag
     * The Maleable Check is named like that because it can be supplied with an "option" to present
     * different behavior when wanted.
     * 
     * 1. Maleable MAX gives the maximum packet in the CSV file
     * 2. Maleable MIN gives the minimum packet in the CSV file
     * 3. Maleable AVG gives the average packet in the CSV file
     * 
     * @param input A source of input from a CSV file
     * @param maleableOption An option to give to the maleable function
     * @assumes {@code maleableOption.equals("MAX") || maleableOption.equals("MIN") || maleableOption.equals("AVG")}
     * @throws IOException
     */
    public static void respondToMaleableFlag(BufferedReader input, String maleableOption) throws IOException{
        String line;
        long objVal = 0;
        long buf = 0;

        while((line = input.readLine()) != null){
            String[] newLine = CompClass.splitter(line);
            if(newLine[0].equals("No.")) continue;

            if(Integer.parseInt(newLine[0]) == 1){ objVal = Long.parseLong(newLine[8]); continue; }
            switch(maleableOption){
                case "MAX": buf = Long.parseLong(newLine[8]); objVal = buf > objVal ? buf : objVal; break;
                case "MIN": buf = Long.parseLong(newLine[8]); objVal = buf < objVal ? buf : objVal; break;
                case "AVG": objVal += Long.parseLong(newLine[8]); buf++; break;
            }
        }
        switch(maleableOption){
            case "MAX": System.out.println("Maximum packet size: " + objVal); break;
            case "MIN": System.out.println("Minimum packet size: " + objVal); break;
            case "AVG": System.out.println("Average packet size: " + (objVal/buf)); break;
        }
    }

    /**
     * Function with code to respond when the user invokes strafan with the r4 flag, which presents the number
     * of unique IPv4 Destination Addresses in the CSV file
     * @param input A source of input from a CSV file
     * @throws IOException
     */
    public static void respondTor4Flag(BufferedReader input) throws IOException{
        String line;
        Set<String> addressList = new HashSet<String>();

        while((line = input.readLine()) != null){
            String[] newLine = CompClass.splitter(line);

            if(newLine[0].equals("No.")) continue;
            System.out.print("[ " + newLine[0] + "] Current address: " + newLine[3] + "\r");
            if(CompClass.checkIPAdressVersion(newLine[3], 4)){
                 addressList.add(newLine[3]);
            }
        }
        System.out.println("Number of Unique IPv4 Destination Addresses :: " + addressList.size());
    }

    /**
     * Function with code to respond when the user invokes strafan with the SP flag, which presents the number
     * of unique TCP Source ports in the CSV file
     * @param input
     * @throws IOException
     */
    public static void respondToSPFlag(BufferedReader input) throws IOException{
        String line;
		List<String> srcPortsList = new LinkedList<String>();
			
		while((line = input.readLine()) != null) {
			String[] newLine = CompClass.splitter(line);
				
			if(newLine[0].equals("No.")) continue;
			System.out.print("[ " + newLine[0] + "] Current Source Port: " + newLine[5] + "\r");
			if(newLine[6].equals("TCP") && !srcPortsList.contains(newLine[5])) {
				srcPortsList.add(newLine[5]);
			}
		}
        System.out.print("\n");
		System.out.println("Number of Unique TCP Source Ports :: " + srcPortsList.size());
    }
}
