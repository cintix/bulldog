package dk.tv2.bulldog.backend.models;

import java.util.Date;

/**
 *
 * @author migo
 */
public class Reponse {

    private String clientName;
    private String mappingName;
    private String md5;
    private String action; // enum CREATED, UPDATED, DELETED
    private String content; // BASE64 ENCODED

    private long filesize;
    private Date created = new Date();

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Reponse{" + "clientName=" + clientName + ", mappingName=" + mappingName + ", filesize=" + filesize + ", created=" + created + ", md5=" + md5 + ", action=" + action + ", content=" + content + '}';
    }

}
