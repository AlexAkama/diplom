package diploma.controller;

import diploma.main.Connection;
import diploma.model.GlobalSetting;
import diploma.model.GlobalSettingsValue;
import diploma.model.Tag;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ApiGeneralController {

    @Value("${persona.title}")
    private String title;
    @Value("${persona.subtitle}")
    private String subtitle;
    @Value("${persona.phone}")
    private String phone;
    @Value("${persona.email}")
    private String email;
    @Value("${persona.copyright}")
    private String copyright;
    @Value("${persona.copyrightFrom}")
    private String copyrightFrom;

    public ApiGeneralController() {
    }

    @GetMapping("/api/init")
    public HashMap<String, String> getPersonalInformation() {
        HashMap<String, String> personalInformation = new HashMap<>();
        personalInformation.put("title", title);
        personalInformation.put("subtitle", subtitle);
        personalInformation.put("phone", phone);
        personalInformation.put("email", email);
        personalInformation.put("copyright", copyright);
        personalInformation.put("copyrightFrom", copyrightFrom);
        return personalInformation;
    }

    @GetMapping("/api/tag")
    public TagDto getTags() {
        List<Tag> tagList;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            //FIXME доделать весы тега
            String hql = "FROM " + Tag.class.getSimpleName();
            tagList = session.createQuery(hql).getResultList();

            transaction.commit();
        }
        return new TagDto(tagList);

    }

    @GetMapping("/api/settings")
    public HashMap<String, Boolean> getSettings() {
        List<GlobalSetting> globalSettingList;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "FROM " + GlobalSetting.class.getSimpleName();
            globalSettingList = session.createQuery(hql).getResultList();

            transaction.commit();
        }

        HashMap<String, Boolean> settingsMap = new HashMap<>();
        for (GlobalSetting globalSetting : globalSettingList) {
            settingsMap.put(
                    globalSetting.getCode(),
                    globalSetting.getValue() == GlobalSettingsValue.YES
            );
        }

        return settingsMap;
    }

    private static class TagDto {
        private List<Tag> tagList;

        public TagDto(List<Tag> tagList) {
            this.tagList = tagList;
        }

        public List<Tag> getTags() {
            return tagList;
        }

        public void setTags(List<Tag> tagList) {
            this.tagList = tagList;
        }
    }

}
