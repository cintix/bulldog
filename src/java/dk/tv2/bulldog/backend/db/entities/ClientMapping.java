/*
 */
package dk.tv2.bulldog.backend.db.entities;

import dk.tv2.bulldog.backend.db.annotations.Entity;
import dk.tv2.bulldog.backend.db.managers.ClientMappingManager;
import java.util.Date;
import java.util.List;

/**
 *
 * @author migo
 */
@Entity(manager = ClientMappingManager.class)
public abstract class ClientMapping {

    private int id;
    private int clientId;
    private String name;
    private String path;
    private String url;
    private String pattern;
    private int actions;
    private Date created;
    private Date updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getActions() {
        return actions;
    }

    public void setActions(int actions) {
        this.actions = actions;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public abstract List<ClientMapping> loadAll();

    public abstract ClientMapping load();

    public abstract boolean update();

    public abstract boolean delete();

    public abstract boolean deleteAllFromClient(int clientId);

    public abstract boolean create();

    public abstract ClientMapping load(int id);

    @Override
    public String toString() {
        return "ClientMapping{" + "id=" + id + ", clientId=" + clientId + ", name=" + name + ", path=" + path + ", url=" + url + ", pattern=" + pattern + ", actions=" + actions + ", created=" + created + ", updated=" + updated + '}';
    }

}
