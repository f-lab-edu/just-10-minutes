package com.flab.just_10_minutes.redis.helper;

public interface WaitingQueueHelper {

    public void clean(String key);

    public void deleteKey(String key);

    public void addFinishingQueue(String key, String member);

    public Long fetchFinishingQueue(String key);

    public Long addProcessingQueue(final String key, final String member, final Integer ttlMinutes);
}
