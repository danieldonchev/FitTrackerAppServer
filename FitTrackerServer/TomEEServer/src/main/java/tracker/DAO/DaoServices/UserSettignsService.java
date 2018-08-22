package tracker.DAO.DaoServices;

import tracker.Entities.Details;
import tracker.Entities.GenericUser;

public interface UserSettignsService {

    Details create(Details holder);
    Details update(Details holder, GenericUser user);
    Details get(GenericUser user);
}
