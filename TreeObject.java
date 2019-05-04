class TreeObject {
    private int frequency;
    private long key;

    public TreeObject(long data){
        this.key = data;
        this.frequency = 1;
    }

    /**
     *
     */
    public void increaseFrequency(){
        this.frequency++;
    }

    /**
     *
     * @return
     */
    public int getFrequency(){
        return this.frequency;
    }

    /**
     *
     * @return
     */
    public long getKey(){
        return this.key;
    }

    /**
     *
     * @param frequency
     */
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }

    @Override
    public int compareTo(TreeObject o) {
        if(this.key > o.getKey()){
            return 1;
        }else if(this.key < o.getKey()){
            return -1;
        }else{
            return 0;
        }
    }

    public String toString(){
        return "Key value: " + key + "\tFrequency: " + frequency;
    }

}
}
