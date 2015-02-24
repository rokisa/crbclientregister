package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by weaversoft on 2/16/2015.
 */
@Entity
public class NextOfKin extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long nextOfKinId;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public String relationship;

    public String phoneNumber;

    @Constraints.Email
    public String emailAddress;

    public String nationalId;

    @ManyToOne
    @JoinColumn(name = "clientDetails", nullable = false) @JsonBackReference
    public ClientDetails clientDetails;

    public static Finder<Long, NextOfKin> find = new Finder<Long, NextOfKin>(
            Long.class, NextOfKin.class
    );

    public static NextOfKin findNextOfKinById(String nextOfKinId){
        NextOfKin nextOfKin = find.where()
                .eq("nextOfKinId", nextOfKinId)
                .findUnique();

        return nextOfKin;
    }

    public static List<NextOfKin> getNextOfKinByClient(ClientDetails clientDetails){
        List<NextOfKin> nextOfKinList = find.where()
                .ge("clientDetails.clientId", clientDetails.clientId)
                .findList();

        return nextOfKinList;
    }
}
