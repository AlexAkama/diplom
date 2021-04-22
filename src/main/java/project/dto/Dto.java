package project.dto;

import org.hibernate.Session;
import org.hibernate.Transaction;
import project.config.Connection;

import java.awt.*;
import java.awt.image.BufferedImage;

import static project.model.emun.GlobalSettingsValue.getSetValue;

public class Dto {

//    public static final String baseCondition = "p.isActive=1 AND p.moderationStatus='ACCEPTED' AND p.time < NOW()";

//    public static String dateToSqlDate(Date date) {
//        return new java.sql.Timestamp(date.getTime()).toString();
//    }


    public static BufferedImage resizeForCaptcha(BufferedImage image) {
        return createResizedImage(image, 100, 35);
    }

    private static BufferedImage createResizedImage(BufferedImage original, int width, int heigth) {
        BufferedImage result = new BufferedImage(width, heigth, original.getType());
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(original, 0, 0, width, heigth, null);
        graphics2D.dispose();
        return result;
    }

    public static void saveGlobalParameter(String name, boolean value) {
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "update GlobalSetting set value=:value where code=:code";
            session.createQuery(hql)
                    .setParameter("value", getSetValue(value))
                    .setParameter("code", name)
                    .executeUpdate();

            transaction.commit();
        }
    }


}
