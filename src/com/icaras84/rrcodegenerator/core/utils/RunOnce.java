package com.icaras84.rrcodegenerator.core.utils;

public class RunOnce implements Runnable{

    private boolean ran;
    private Runnable task;

    public RunOnce(Runnable task){
        this.task = task;
        this.ran = false;
    }

    public RunOnce(boolean initialState, Runnable task){
        this.task = task;
        this.ran = initialState;
    }

    @Override
    public void run() {
        if (!ran){
            task.run();
            ran = true;
        }
    }

    public void reset(){
        ran = false;
    }
}
