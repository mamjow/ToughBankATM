package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInformationViewModel {
    private long client_id;
//    private String firstname;
//    private String infix;
//    private String lastname;
    private String fullName;

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //    public String getFullName(){
//        return firstname + (infix==null || infix.equals("") ? " ": " " + infix + " ") + lastname;
//    }
}
