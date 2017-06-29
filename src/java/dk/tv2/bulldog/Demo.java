package dk.tv2.bulldog;

import dk.tv2.bulldog.backend.db.DatasourceMock;
import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.entities.Client;
import dk.tv2.bulldog.backend.db.managers.DataSourceManager;
import dk.tv2.bulldog.backend.io.Actions;
import dk.tv2.bulldog.backend.io.Configuration;
import java.util.List;

/**
 *
 * @author migo
 */
public class Demo {



    public static void main(String[] args) {
        
        Configuration.initilizeTest();
        Configuration.setProperty("datasource", "jdbc/bulldog");
        DataSourceManager.addDataSource("jdbc/bulldog", new DatasourceMock());
        
        Client client = EntityManager.create(Client.class);        
        client.setName("TTV NXT");
        client.setDescription("TTV Kastrup Airport XML Files");
        
        if (client.create()) {
            System.out.println("Created " + client.toString());
        }
        
        
        
        List<Client> allClients = client.loadAll();
        for(Client c : allClients) {            
            System.out.println("Client " + c.getName() + " (" + c.getDescription() + ")");            
        }
        
        
        
        
        Actions delete = Actions.DELTETED;
        Actions create = Actions.CREATED;
        Actions update = Actions.UPDATED;
        
        
        int deletedAndCreated = Actions.getActions(delete, create);
        
        System.out.println(" deletedAndCreated contains delete  " + Actions.contains(deletedAndCreated, delete) );
        System.out.println(" deletedAndCreated contains create  " + Actions.contains(deletedAndCreated, create) );
        System.out.println(" deletedAndCreated contains update  " + Actions.contains(deletedAndCreated, update) );
        System.out.println("");
        int updateAndCreate = Actions.getActions(update, create);
        
        System.out.println(" updateAndCreate contains delete  " + Actions.contains(updateAndCreate, delete) );
        System.out.println(" updateAndCreate contains create  " + Actions.contains(updateAndCreate, create) );
        System.out.println(" updateAndCreate contains update  " + Actions.contains(updateAndCreate, update) );
        System.out.println("");
        int onlyCreated = Actions.getActions(create);
        
        System.out.println(" onlyCreated contains delete  " + Actions.contains(onlyCreated, delete) );
        System.out.println(" onlyCreated contains create  " + Actions.contains(onlyCreated, create) );
        System.out.println(" onlyCreated contains update  " + Actions.contains(onlyCreated, update) );
        System.out.println("");
        
        
    }

    
}
