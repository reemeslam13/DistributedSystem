package de.uniba.rz.backend;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import com.rabbitmq.client.AMQP.Exchange.Delete;

import de.uniba.rz.Get.ReadAll;
import de.uniba.rz.Get.ReadSingle;
import de.uniba.rz.Get.Search;
import de.uniba.rz.Post.Create;
import de.uniba.rz.Put.Update;


@ApplicationPath("/")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RESTApi extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(ReadAll.class);
        resources.add(Search.class);
        resources.add(ReadSingle.class);
        resources.add(Delete.class);
        resources.add(Create.class);
        resources.add(Update.class);

        return resources;
    }
}
