import java.util.Scanner;

public class Palindrome {
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        //testando o myStack;
       /* MyStack test = new MyStack();
        test.push("pop");
        test.push("tot");
        test.print();
        test.pop();
        System.out.println();
        test.print();
        System.out.println();
        String top = test.peek();
        System.out.println(top); 
        test.add("pop");
        test.add("tot");
        test.print();
        test.remove();
        System.out.println();
        test.print();
        System.out.println();
        String top = test.peek();
        System.out.println(top);
        */

        Queue queue = new Queue();
        MyStack stack = new MyStack();
        String word = "";
        do{
            System.out.print("Introduza a palavra -->:  ");
            word = in.nextLine();

            for(int i = 0; i < word.length(); i++){
                char c = word.charAt(i);
                queue.add(String.valueOf(c));
                stack.push(String.valueOf(c));
            }
            boolean palindrome = false;
            int size = queue.getSize();
            for(int i = 0; i < size;i++ ){
                queue.print();
                stack.print();
                if(!queue.remove().equals(stack.pop())){
                    System.out.printf("The word %s is not a palindrome!\n",word);
                    palindrome = false;
                    break;
                }else{
                    palindrome = true;
                }
            }

            if(palindrome == true){
                System.out.printf("The word \"%s\" is a palindrome!\n",word);
            }
            queue.clear();
            stack.clear();
        }while(!word.equals("sair"));
        


    }
}
