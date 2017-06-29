/*
 */
package dk.tv2.bulldog.backend.db.entities;

import dk.tv2.bulldog.backend.db.annotations.Entity;
import dk.tv2.bulldog.backend.db.managers.ClientManager;
import java.util.Date;
import java.util.List;

/**
 *
 * @author migo
 */
@Entity(manager = ClientManager.class)
public abstract class Client {

    private int id;
    private String name;
    private String description;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    
    public abstract List<Client> loadAll();
    
    public abstract Client load();
    public abstract boolean update();
    public abstract boolean delete();
    public abstract boolean create();
    
    public abstract Client load(int id);
    


    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name=" + name + ", description=" + description + ", created=" + created + '}';
    }

}
