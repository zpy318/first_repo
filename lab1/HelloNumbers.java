public class HelloNumbers{
    public static void main(String[] args) {
        int x = 0;
        int s = 0;
        while (x < 10){
            System.out.print(x + " ");
            s = s + x;
            x = x + 1;
        }
        System.out.println(s + 10);
    }
}
