package tracker.settings;

import tracker.authentication.users.UserPrincipal;

public interface UserSettignsService {

    Details create(Details holder);
    Details update(Details holder, UserPrincipal user);
    Details get(UserPrincipal user);
}
