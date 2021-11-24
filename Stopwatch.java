/**
 * This time recording class was shown and developed by the regent teacher
 * of the Algorithms and Data Structures (Algoritmos e Estruturas de Dados, AED)
 * Curricular Unit, João Neto, during the second semester of the school year of
 * 2021/2022 and was used for testing purposes.
 */
public class Stopwatch {
    private final long start; 
    public Stopwatch(){
        start = System.currentTimeMillis();
    } 
    // return time (in seconds) since this object was created
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
}
