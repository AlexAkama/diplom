//package project.dto;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import project.config.Connection;
//
//import static project.model.emun.GlobalSettingsValue.getSetValue;
//
//public class Dto {
//
//    public static void saveGlobalParameter(String name, boolean value) {
//        try (Session session = Connection.getSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            String hql = "update GlobalSetting set value=:value where code=:code";
//            session.createQuery(hql)
//                    .setParameter("value", getSetValue(value))
//                    .setParameter("code", name)
//                    .executeUpdate();
//
//            transaction.commit();
//        }
//    }
//
//}
