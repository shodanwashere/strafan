
/**
 * A small little loading bar class i made. makes things easier to see
 * 
 * @author nunoDias, fc56330
 */
public class LoadingBar {
    private int total_perc; // loads the number that should be equal to 100%
    private int curr_status; // loads a current number. we're assuming that it's under total_perc

    /**
     * Creates a new instance of LoadingBar
     * @param total The value that should be == 100%
     */
    public LoadingBar(int total){
        this.total_perc = total;
        this.curr_status = 0;
    }

    /**
     * Updates the current status
     * @param new_status - Status update
     */
    public void update_curr_status(int new_status){
        this.curr_status = new_status;
    }

    /**
     * Gets the current percentage of the loading bar
     * @return Current percentage
     */
    public float get_perc(){
        return ((float)curr_status/(float)total_perc) * 100;
    }

    /**
     * Shows an instant of the loading bar on screen and
     * returns the carriage. This makes it perfect for constant
     * updates without having to fill the whole terminal with lines.
     */
    public void show_loading_bar(){
        System.out.print("|");
        for(int j = 1; j <= 10; j++){
            if(j < (get_perc() / 10))
                System.out.print("=");
            else
                System.out.print(" ");
            }
            System.out.print("| " + get_perc() + "% \r");
    }
}
