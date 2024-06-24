
//import java.util.ArrayList;
//import java.util.List; 
public class MyStack {
    private String word[]; 
    private int size;
    private int idex;

    public MyStack(){
        word = new String[10];
        this.size = 0;
        this.idex = -1;
    }

    public boolean isEmpety(){
        return this.size == 0; 
    }
    public boolean isFull(){
        return this.size == word.length; 
    }
    public int getSize(){
        return size;
    }
    public void push(String wrd){
        //MyStack f = new MyStack();
        assert !isFull(): "The Stack is full!";
        this.idex++;
        word[this.idex] = wrd;
        this.size++;
    }
    public String pop(){
        assert !isEmpety(): "The Stack is Empety!";
        String wrd =  word[this.idex];
        word[this.idex] = null;
        this.idex--;
        this.size--;
        return wrd;
    }
    public String peek(){
        assert !isEmpety(): "The Stack is Empety!";
        return word[this.idex];  
    }
    public int search(String wrd){
        assert !isEmpety(): "The Stack is Empety!";
        assert contains(wrd): "The Stack does not contains this String!";
        int idx = -1;
        for(int i = 0; i < word.length; i++){
            if (word[i].equals(wrd))
                idx = i;
        }
        return idx;
    }
    public boolean contains(String wrd){
        assert !isEmpety(): "The Stack is Empety!";
        boolean contain = false;
        for(int i = 0; i < word.length; i++){
            if (word[i].equals(wrd))
              contain =  true;
        }
        return contain;
    }
    public void clear(){
        assert !isEmpety(): "The Stack is Empety!";
        for(int i = 0; i < this.size; i++){
            word[i] = null;
        }
        this.size = 0;
        this.idex = -1;
    }
    public void print(){
        System.out.println("Stack: ");
        for(int i = this.size-1; i >-1; i-- ){
            System.out.println(word[i]);
        }
    }
}
