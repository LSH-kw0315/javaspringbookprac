package org.zerock.chap1_practice.todo.dto;

import java.time.LocalDate;

public class TodoDTO {
    private Long tno;
    private String title;
    private LocalDate duedate;
    private boolean finished;

    public Long getTno(){
        return tno;
    }

    public void setTno(Long tno){
        this.tno=tno;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public LocalDate getDueDate(){
        return duedate;
    }

    public void setDueDate(LocalDate duedate){
        this.duedate=duedate;
    }

    public boolean getFinished(){
        return finished;
    }

    public void setFinished(boolean finished){
        this.finished=finished;
    }

    @Override
    public String toString() {
        return "TodoDTO{"+
                "tno="+tno+
                ", title='"+title+'\''+
                ", dueDate="+duedate+
                ", finished="+finished+
                '}';
    }
}
