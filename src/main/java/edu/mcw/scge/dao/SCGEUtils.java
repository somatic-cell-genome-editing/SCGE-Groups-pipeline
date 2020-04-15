package edu.mcw.scge.dao;

import edu.mcw.scge.Manager;
import edu.mcw.scge.dao.implementation.GroupDAO;
import edu.mcw.scge.dao.implementation.PersonDao;
import edu.mcw.scge.dao.implementation.RoleDAO;
import edu.mcw.scge.datamodel.Person;
import edu.mcw.scge.datamodel.SCGEGroup;
import engineer.nightowl.groupsio.api.domain.Group;
import engineer.nightowl.groupsio.api.domain.Subscription;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jthota on 10/4/2019.
 */
public class SCGEUtils {
    GroupDAO gdao=new GroupDAO();
    PersonDao pdao=new PersonDao();
    RoleDAO rdao=new RoleDAO();
    private Logger logger= LogManager.getLogger(Manager.class);
    public boolean verifyGroupExists(String workingGroupName) throws Exception {
      int id=  gdao.getGroupId(workingGroupName);
        return id != 0;
    }
    public boolean verifyUserExistsByEmail(String userEmail) throws Exception {
        List<Person> personList=pdao.getPersonByEmail(userEmail);
        return personList != null && personList.size() > 0;
    }
    public Person getPersonByEmail(String userEmail) throws Exception {
        List<Person> personList=pdao.getPersonByEmail(userEmail);
        return personList != null && personList.size() > 0?personList.get(0):null;
    }
    public int insertOrGetGroup(String groupName) throws Exception {
        int subgroupId=  gdao.getGroupId(groupName);
        int groupId=gdao.getGroupId("working group");
        if(subgroupId!=0){
            return subgroupId;
        }else{
            SCGEGroup subgroup=new SCGEGroup();
            subgroupId=gdao.getNextKey("group_seq");
            subgroup.setGroupId(subgroupId);
            subgroup.setGroupName(groupName);
            subgroup.setGroupType("subgroup");
            gdao.insert(subgroup);
            if(groupId==0){
                SCGEGroup group=new SCGEGroup();
                groupId= gdao.getNextKey("group_seq");
                group.setGroupId(groupId);
                group.setGroupName("working group");
                group.setGroupType("group");
                gdao.insert(group);

            }
            gdao.makeAssociations(groupId, subgroupId);
            return subgroupId;
        }
    }
    public void updateSubscription(Subscription s, int subgroupId, Person p) throws Exception {
        String role=s.getModStatus().equals("sub_modstatus_owner")?"owner":"member";
       // logger.info(s.getUserId() + "\t" + s.getFullName() + "\t" + s.getEmail() + "\t" + s.getUserStatus() + "\t" + s.getStatus() + "\t" +s.getModStatus());
        int roleKey=rdao.getRoleKeyOfRole(role);
        if(roleKey==0){
           roleKey= rdao.getNextKey("role_seq");
           rdao.insert(roleKey, role);
        }
     pdao.insertPersonInfo(p.getId(), Arrays.asList(roleKey), subgroupId);
    }
    public void addSubscription(){

    }
}
