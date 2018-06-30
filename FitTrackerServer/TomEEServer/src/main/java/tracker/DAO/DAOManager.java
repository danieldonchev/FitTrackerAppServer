package tracker.DAO;

import java.sql.SQLException;

public class DAOManager {

    public static DAOManager getInstance(){
        return DAOManagerSingleton.INSTANCE.get();
    }

    private DAOManager(){ }

    private static class DAOManagerSingleton{
        private static ThreadLocal<DAOManager> INSTANCE;
        static
        {
            ThreadLocal<DAOManager> dm;
            try{
                dm = ThreadLocal.withInitial(() -> new DAOManager());
            } catch (Exception ex) {
                dm = null;
            }
            INSTANCE = dm;
        }
    }
    public enum Table{GOAL}

//    public GenericDao getDAO(Table t) throws SQLException{
//
//        switch (t){
//            case GOAL:
//                return new GenericDAOImpl();
//
//                default:
//                    throw new SQLException("Table " + t + " does not exist");
//        }
//    }
}
