package tracker.DAO.Daos;

import org.json.JSONObject;
import tracker.Entities.Details;
import tracker.Entities.Users.GenericUser;

import javax.enterprise.inject.Alternative;
import javax.persistence.Query;
import java.util.Iterator;

@Alternative
public class SettingsDao extends GenericDAOImpl<Details, String> {

    public SettingsDao(){}

    public Details update(Details details, GenericUser user){
        JSONObject data = new JSONObject(details.getSettings());
        Iterator<String> iterator = data.keys();
        StringBuilder builder = new StringBuilder();
        int size = data.keySet().size();
        int counter = 0;

        while (iterator.hasNext()) {
            builder.append("\'$." + iterator.next() + "\'");
            builder.append(", ");
            builder.append("?");

            if (counter == size - 1) {
                break;
            }
            builder.append(", ");
            counter++;
        }
        Query query = getEntityManager().createNativeQuery("UPDATE user_settings SET settings = JSON_SET(:arg1), last_modified = :arg2, last_sync = :arg3 " +
                "where id = :arg4");
        query.setParameter("arg1", data);
        query.setParameter("arg2", details.getLastModified());
        query.setParameter("arg3", user.getNewServerTimestamp());
        query.setParameter("arg4", user.getId());

        return details;
    }

    public Details read(GenericUser user){
        Query query = getEntityManager().createNativeQuery("Select * from Details WHERE last_sync > :arg1" +
                "id = :arg2");
        query.setParameter("arg1", user.getClientSyncTimestamp());
        query.setParameter("arg2", user.getId());
        return (Details) query.getSingleResult();
    }
}