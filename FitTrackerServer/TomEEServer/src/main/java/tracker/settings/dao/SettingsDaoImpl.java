package tracker.settings.dao;

import org.json.JSONObject;
import tracker.utils.dao.GenericDAOImpl;
import tracker.settings.Details;
import tracker.authenticate.GenericUser;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.UUID;

@SettingsDaoQualifier
public class SettingsDaoImpl extends GenericDAOImpl<Details, UUID> implements SettingsDao{

    public SettingsDaoImpl(){}

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
        Query query = getEntityManager().createNativeQuery("Select * from user_settings WHERE last_sync > :arg1" +
                "id = :arg2");
        query.setParameter("arg1", user.getClientSyncTimestamp());
        query.setParameter("arg2", user.getId());
        return (Details) query.getSingleResult();
    }
}
