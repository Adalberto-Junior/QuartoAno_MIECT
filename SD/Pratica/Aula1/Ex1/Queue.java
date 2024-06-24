public class Queue {
    private String word[]; 
    private int size;
    private int idex;

    public Queue(){
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
    public boolean add(String wrd){
        //MyStack f = new MyStack();
        assert !isFull(): "The Queue is full!";
        if(isFull()){
            return false;
        }else{
            this.idex++;
            word[this.idex] = wrd;
            this.size++;
            return true;
        }
        
    }
    public String remove(){
        assert !isEmpety(): "The Queue is Empety!";
        String wrd =  word[0];
        
        for(int i = 0; i < this.size-1; i++){
            //fazer um shift a direita ou seja uma troca de posição: [1,2,3,4]--> [2,3,4,null];
            int j = i +1;
            word[i] = word[j];   
        }
        word[this.idex] = null;
        this.idex--;
        this.size--;
        return wrd;
    }

    public String peek(){
        assert !isEmpety(): "The Queue is Empety!";
        return word[this.idex];  
    }
    public int search(String wrd){
        assert !isEmpety(): "The Queue is Empety!";
        assert contains(wrd): "The Queue does not contains this String!";
        int idx = -1;
        for(int i = 0; i < word.length; i++){
            if (word[i].equals(wrd))
                idx = i;
        }
        return idx;
    }
    public boolean contains(String wrd){
        assert !isEmpety(): "The Queue is Empety!";
        boolean contain = false;
        for(int i = 0; i < word.length; i++){
            if (word[i].equals(wrd))
              contain =  true;
        }
        return contain;
    }
    public void clear(){
        assert !isEmpety(): "The Queue is Empety!";
        for(int i = 0; i < this.size; i++){
            word[i] = null;
        }
        this.size = 0;
        this.idex = -1;
    }
    public void print(){
        System.out.println("Queue: ");
        for(int i = 0; i < this.size;i++ ){
            System.out.println(word[i]);
        }
    }
}
