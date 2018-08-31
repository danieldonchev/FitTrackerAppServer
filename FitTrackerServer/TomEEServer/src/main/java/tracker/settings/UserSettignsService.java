package tracker.settings;

import tracker.authenticate.GenericUser;
import tracker.settings.Details;

public interface UserSettignsService {

    Details create(Details holder);
    Details update(Details holder, GenericUser user);
    Details get(GenericUser user);
}
