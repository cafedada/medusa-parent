package io.vilya.server.medusa.jdbc.demo;

import java.time.LocalDateTime;

public class Log {
    private Integer id;

    private LocalDateTime logTime;

    private Integer logType;

    private String logContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent == null ? null : logContent.trim();
    }

	@Override
	public String toString() {
		return String.format("Log [id=%s, logTime=%s, logType=%s, logContent=%s]", id, logTime, logType, logContent);
	}
    
}