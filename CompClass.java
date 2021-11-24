import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A little class with complementary functions to help with the
 * development of strafan. All of these functions can be used for
 * other purposes and this class can be expanded to fit even more
 * auxiliary functions in the future.
 * 
 * @author nunoDias fc56330
 */
public class CompClass{
    /**
     * Takes a line from a csv file, splits it by its commas, and then
     * removes the quotes (") from the beginning and end of each item,
     * making the line prepared for analysis.
     * @param line - String that corresponds to the line to be processed
     * @return String array with each item without any artifacts
     * @assumes All items are separated by commas and begin and end with quotation marks (").
     */
    public static String[] splitter(String line){
        String[] product = line.split(",");
        for(int i = 0; i < product.length; i++){
            product[i] = new String (product[i].substring(1, product[i].length() - 1));
        }
        return product;
    }

    /**
     * Checks if a file exists and is not a directory
     * @param filename - Name of the file to examined
     * @return True if the file exists and is not a directory, False otherwise
     */
    public static boolean checkFile(String filename){
        boolean check = true;
        if(!(new File(filename).exists()) || (new File(filename).isDirectory())){
            check = false;
        }
        return check;
    }

    /**
     * Checks if a supplied string is an IP Address of a specific version.
     * @param address - String corresponding to the supplied address.
     * @param version - Number of IP version (4 or 6).
     * @return True if the address matches the given version, False otherwise
     */
    public static boolean checkIPAdressVersion(String address, int version){
        Pattern patt = null;
        switch(version){
            case 4: patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"); break;
            case 6: patt = Pattern.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))"); break;
        }
        Matcher matt = patt.matcher(address);
        return matt.find();
    }

    /**
     * Takes a String with a binary sequence and converts it into a
     * String with a hexadecimal representation of that binary sequence.
     * @param binSeq - Binary Sequence
     * @return binSeq in hexadecimal form
     * @throws Exception if binSeq is not a binary string
     */
    public static String binSeqToHex(String binSeq) throws Exception{
        int binSeqInt = Integer.parseInt(binSeq,2);
        String flagSeq = new String(Integer.toHexString(binSeqInt));
        switch(flagSeq.length()){
            case 1 : flagSeq = "0" + flagSeq;
            case 2 : flagSeq = "0" + flagSeq;
            default : flagSeq = "0x" + flagSeq; break;
        }
        return flagSeq;
    }
}