package com.icaras84.rrcodegenerator.core.utils;

public class CodeGenVariable<T> {
    private T content;
    private boolean isReference;
    private String referenceName;

    public CodeGenVariable(T nContent){
        this.content = nContent;
        isReference = false;
        referenceName = "";
    }

    public CodeGenVariable(){
        this.content = null;
        isReference = true;
        referenceName = "";
    }

    public boolean isReference(){
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public String getReferenceName(){
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public T getContent(){
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
