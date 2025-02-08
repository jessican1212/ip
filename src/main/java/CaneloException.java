public class CaneloException extends Exception{
    public CaneloException(String message) {
        super("Canelo says uh oh! " + message);
    }
}
