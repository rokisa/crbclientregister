package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.Constraint;
import java.util.List;

/**
 * Created by weaversoft on 2/16/2015.
 */
@Entity
public class ClientDetails extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long clientId;

    @Constraints.Required
    public String nationalId;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Email
    public String emailAddress;

    @Constraints.Required
    public String phoneNumber;

    @Constraints.Required
    public String nationality;

    @Constraints.Required
    public String physicalAddress;

    @Constraints.Required
    public Long dateOfBirth;

    @Constraints.Required
    public String occupation;

    @Constraints.Required
    public String nationalIdPhoto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientDetails")
    public List<NextOfKin> nextOfKinList;

    public static Finder<Long, ClientDetails> find = new Finder<Long, ClientDetails>(
            Long.class, ClientDetails.class
    );

    public static List<ClientDetails> fetchAllClients(){
        return ClientDetails.find.setDistinct(true).findList();
    }

    public static List<ClientDetails> fetchByFirstName(String firstName){
        List<ClientDetails> clientDetailsList = find.where()
                .ilike("firstName", firstName)
                .findList();

        return clientDetailsList;
    }

    public static ClientDetails findByNationalId(String nationalId){
        ClientDetails clientDetails = find.where()
                .eq("nationalId",nationalId)
                .findUnique();

        return clientDetails;
    }

    public static ClientDetails findByClientId(Long clientId){
        ClientDetails clientDetails = find.where()
                .eq("clientId",clientId)
                .findUnique();

        return clientDetails;
    }

}
