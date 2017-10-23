package api.presence.bo;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.PresenceTask;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import api.presence.bo.handler.PresenceTaskEventHandler;
import framework.exception.PresenceException;
import api.presence.bo.service.PresenceServiceDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucjung on 2017-06-05.
 */
public class PresenceTaskExecutor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String taskExecutorName = "";

    // 수행할 Task
    private List<PresenceTask> presenceTasks = new ArrayList<>();

    // 다음에 수행할 TaskExecutor
    private List<PresenceTaskExecutor> nextPresenceTaskExcutor = new ArrayList<>();

    // Paramater Data Package
    private PresenceDataPackage presenceVo;

    // Spring 서비스 호출을 위한 객체
    private PresenceServiceDelegator serviceDelegator;

    public void excute(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        this.presenceVo = presenceVo;
        this.serviceDelegator = serviceDelegator;

        logger.debug("[{}] [{}] executeTask ", getClass().getSimpleName(), taskExecutorName);

        doTask();

        // 다음 실행된 Executor 실행
        for (PresenceTaskExecutor nextExecutor : nextPresenceTaskExcutor) {
            nextExecutor.excute(presenceVo, serviceDelegator);
        }
    }

    private void doTask() {
        // 등록된 Task 수행
        for (PresenceTask presenceTask : presenceTasks) {
            doTaskEvent(presenceTask.getPreEventHandler());
            doTaskStartegy(presenceTask.getStrategy());
            doTaskEvent(presenceTask.getPostEventHandler());
        }
    }

    private void doTaskEvent(PresenceTaskEventHandler eventHandler) {
        try{
            eventHandler.doEvent(presenceVo, serviceDelegator);
            logger.debug("{} {} execute Event Handler Do Event ", getClass().getSimpleName(), eventHandler.getClass());
        } catch (NullPointerException ex) {
            logger.debug("{} {} execute Event Handler is Null ", getClass().getSimpleName(), taskExecutorName);
        } catch (PresenceException ex) {
            throw ex;
        }
    }

    private void doTaskStartegy(AbstractPresenceStrategy strategy) {
        try{
            strategy.execute(presenceVo, serviceDelegator);
            logger.debug("{} {} execute Task strategy do success ", getClass().getSimpleName(), strategy.getClass());
        } catch (NullPointerException ex) {
            logger.debug("{} {} execute Task strategy is Null ", getClass().getSimpleName(), taskExecutorName);
        } catch (PresenceException ex) {
            throw ex;
        }
    }

    public String getTaskExecutorName() {
        return taskExecutorName;
    }

    public PresenceTaskExecutor setTaskExecutorName(String taskExecutorName) {
        this.taskExecutorName = taskExecutorName;
        return this;
    }

    public PresenceTaskExecutor setPresenceTask(PresenceTask presenceTask) {
        this.presenceTasks.add(presenceTask);
        return this;
    }

    public PresenceTaskExecutor setPresenceTasks(List<PresenceTask> presenceTasks) {
        this.presenceTasks = presenceTasks;
        return this;
    }

    public PresenceTaskExecutor setNextPresenceTaskExcutor(PresenceTaskExecutor nextPresenceTaskExcutor) {
        this.nextPresenceTaskExcutor.add(nextPresenceTaskExcutor);
        return nextPresenceTaskExcutor;
    }

}
