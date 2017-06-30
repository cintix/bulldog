/*
 */
package dk.tv2.bulldog.backend.db.entities;

import dk.tv2.bulldog.backend.db.annotations.Entity;
import dk.tv2.bulldog.backend.db.managers.ClientResponseManager;
import java.util.Date;
import java.util.List;

/**
 *
 * @author migo
 */
@Entity(manager = ClientResponseManager.class)
public abstract class ClientResponse {

    private int clientMappingId;
    private int clientId;
    private String filename;
    private String clientName;
    private String mappingName;
    private Date created;
    private int responseCode;
    private String response;

    public int getClientMappingId() {
        return clientMappingId;
    }

    public void setClientMappingId(int clientMappingId) {
        this.clientMappingId = clientMappingId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

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
    
    public abstract List<ClientResponse> loadAllFromClient(int clientId);
    public abstract List<ClientResponse> loadAll();

    public abstract boolean delete();
    public abstract boolean create();

    @Override
    public String toString() {
        return "ClientResponse{" + "clientMappingId=" + clientMappingId + ", clientId=" + clientId + ", filename=" + filename + ", clientName=" + clientName + ", mappingName=" + mappingName + ", created=" + created + ", responseCode=" + responseCode + ", response=" + response + '}';
    }

}
