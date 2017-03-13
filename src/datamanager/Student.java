package datamanager;

/**
 * Created by Prohos on 11/23/2016.
 */
public class Student {
    private int id;
    private String fullName;
    private String gender;
    private int subjectID;
    private String email;
    private String password;



    public Student(int id, String fullName, String gender, int subjectID, String email,String password) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.subjectID = subjectID;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
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
}
