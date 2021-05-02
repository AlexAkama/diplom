package project;

public class ProfileRequestWithoutPhoto {

    private String name;
    private String email;
    private String password;
    private int removePhoto;


    public ProfileRequestWithoutPhoto(String name, String email, String password, int removePhoto) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(int removePhoto) {
        this.removePhoto = removePhoto;
    }

}
