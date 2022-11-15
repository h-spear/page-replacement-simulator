public class Main {
    public static void main(String[] args) {
        Program program = new Program();
        try {
            program.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("terminate.");
        }
    }
}
