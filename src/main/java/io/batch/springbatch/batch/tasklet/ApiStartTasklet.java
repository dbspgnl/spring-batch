package io.batch.springbatch.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ApiStartTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
}
