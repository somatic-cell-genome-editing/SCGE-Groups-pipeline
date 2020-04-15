package edu.mcw.scge;

import edu.mcw.scge.dao.SCGEUtils;

import edu.mcw.scge.datamodel.Person;
import engineer.nightowl.groupsio.api.GroupsIOApiClient;
import engineer.nightowl.groupsio.api.domain.Group;
import engineer.nightowl.groupsio.api.domain.Subscription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


/**
 * Created by jthota on 10/4/2019.
 */
public class Manager {
    private static final Logger logger= LogManager.getLogger(Manager.class);
    public static void main(String[] args) throws Exception {

        SCGEUtils utils=new SCGEUtils();
        GroupsIOApiClient client= new GroupsIOApiClient("nonce",args[0]);
        String apiToken= client.user().login(args[1]);
        List<Group> subgroups= client.group().getSubgroups(Integer.parseInt(args[2]));
        System.out.println("API TOKEN: " + apiToken);
        System.out.println("Sub Groups Size: " + subgroups.size());
        for(Group g: subgroups){
    //      if(g.getId()==32848) {
    //    int subgroupId=  utils.insertOrGetGroup(g.getDesc());
          logger.info("\n========================\n"+g.getDesc()+"\t"+g.getId()+"\tSCGE GROUP ID: "+ "subgroupId");
          try {

                List<Subscription> subscriptions = client.member().getMembersInGroup(g.getId());
                System.out.println("SUBCRIPTIONS: " + subscriptions.size());
                for (Subscription s : subscriptions) {
                    if (s.getUserStatus().toString().equals("user_status_confirmed")) {
                       Person p=utils.getPersonByEmail(s.getEmail());
                        if(p!=null){
             //              utils.updateSubscription(s, subgroupId, p);
                        }else{
                            logger.info(s.getFullName()+"\t-\t"+s.getEmail() +"\t******USER NOT EXISTS IN SCGE DB *******");
           //                utils.addSubscription();
                        }

                    }
                }
            } catch (Exception e) {
                System.err.println();
                e.printStackTrace();
            }
        //       }
        }

    }
}


