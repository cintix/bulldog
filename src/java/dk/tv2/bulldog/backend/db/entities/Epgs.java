package dk.tv2.bulldog.backend.db.entities;

import dk.tv2.bulldog.backend.db.annotations.Entity;
import dk.tv2.bulldog.backend.db.managers.EpgsManager;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author hjep
 */
@Entity(manager = EpgsManager.class)
public abstract class Epgs {

    private int channelId;
    private int pageId;
    private String channelName;
    private int programId;
    private String title;
    private long startTime;
    private long endTime;
    private Date createdAt;
    private Date updatedAt;
    private String category;
    private String data;

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public abstract boolean create();

    public abstract boolean update();

    public abstract boolean updatePageId();

    public abstract boolean delete(int channelId, int programId);
    
    public abstract Epgs load(int channelId, int programId);

    public abstract boolean deleteOldRecords(int channelId, long timeout);

    public abstract Map<String, Epgs> loadProgramsWithInTime(int channelId, long startTime);

    public abstract Map<String, Epgs> loadProgramsWithInTime(int channelId, long startTime, long endTime);

    public abstract List<Epgs> loadRunningPrograms();

    public abstract Map<String, Epgs> loadPrgramsByCategoryForChannelWithInTime(String category, int channelId, long startTime);

    @Override
    public int hashCode() {
        int hash = 6;
        hash = 89 * hash + this.channelId;
        hash = 89 * hash + Objects.hashCode(this.channelName);
        hash = 89 * hash + this.programId;
        hash = 89 * hash + Objects.hashCode(this.title);
        hash = 89 * hash + (int) (this.startTime ^ (this.startTime >>> 32));
        hash = 89 * hash + (int) (this.endTime ^ (this.endTime >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Epgs other = (Epgs) obj;
        if (this.channelId != other.channelId) {
            return false;
        }
        if (this.programId != other.programId) {
            return false;
        }
        if (this.startTime != other.startTime) {
            return false;
        }
        if (this.endTime != other.endTime) {
            return false;
        }
        if (!Objects.equals(this.channelName, other.channelName)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Epgs{" + "channelId=" + channelId + ", pageId=" + pageId + ", channelName=" + channelName + ", programId=" + programId + ", title=" + title + ", startTime=" + startTime + ", endTime=" + endTime + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", category=" + category + ", data=" + data + '}';
    }

}
