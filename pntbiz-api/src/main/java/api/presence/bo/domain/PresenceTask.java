package api.presence.bo.domain;

import api.presence.bo.handler.PresenceTaskEventHandler;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ucjung on 2017-06-05.
 */
public class PresenceTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String taskName;

    private AbstractPresenceStrategy strategy;
    private PresenceTaskEventHandler postEventHandler;
    private PresenceTaskEventHandler preEventHandler;

    public String getTaskName() {
        return taskName;
    }

    public PresenceTask setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public AbstractPresenceStrategy getStrategy() {
        return strategy;
    }

    public PresenceTask setStrategy(AbstractPresenceStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public PresenceTaskEventHandler getPostEventHandler() {
        return postEventHandler;
    }

    public PresenceTask setPostEventHandler(PresenceTaskEventHandler postEventHandler) {
        this.postEventHandler = postEventHandler;
        return this;
    }

    public PresenceTaskEventHandler getPreEventHandler() {
        return preEventHandler;
    }

    public PresenceTask setPreEventHandler(PresenceTaskEventHandler preEventHandler) {
        this.preEventHandler = preEventHandler;
        return this;
    }
}
