package m_vasyliev.ukma.zlagoda_ais.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String idEmployee;
    private String role;

    public User(String username, String hashedPassword, String idEmployee) {
        this.username = username;
        this.password = hashedPassword;
        this.idEmployee = idEmployee;
    }

    public User() {

    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }
}
